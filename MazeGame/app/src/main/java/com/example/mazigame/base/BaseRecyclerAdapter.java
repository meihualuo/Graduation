package com.example.mazigame.base;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.collection.SparseArrayCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.mazigame.MyApplication;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by zhongkeaixun on 2017/5/16.
 */

public abstract class BaseRecyclerAdapter<T> extends RecyclerView.Adapter<RecyclerViewHolder> {
    private final String TAG = "BaseRecyclerView";
    private static final int BASE_ITEM_TYPE_HEADER = 100000;
    private static final int BASE_ITEM_TYPE_FOOTER = 200000;
    protected SparseArrayCompat<View> mHeaderViews = new SparseArrayCompat<>();
    private SparseArrayCompat<View> mFootViews = new SparseArrayCompat<>();
    protected SparseArray<Drawable> cacheDrawables;

    protected final List<T> mData;
    protected Context mContext;
    protected LayoutInflater mInflater;
    protected int oneHeight;

    private OnItemClickListener mItemClickListener;
    private OnItemLongClickListener mItemLongClickListener;

    protected RecyclerView mRecyclerView;

    public BaseRecyclerAdapter(Context context) {
        mContext = context;
        mData = new ArrayList<T>();
        mInflater = LayoutInflater.from(mContext);
//        oneHeight = SystemUtils.dipToPixel(context, 1);
    }

    public BaseRecyclerAdapter(Context context, List<T> list) {
        mContext = context;
        mData = (list != null) ? list : new ArrayList<T>();
        mInflater = LayoutInflater.from(mContext);
//        oneHeight = SystemUtils.dipToPixel(context, 1);
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        final RecyclerViewHolder holder;
        if (mHeaderViews.get(viewType) != null) {

            holder = new RecyclerViewHolder(mContext, mHeaderViews.get(viewType));
        } else if (mFootViews.get(viewType) != null) {
            holder = new RecyclerViewHolder(mContext, mFootViews.get(viewType));
        } else {
            if (mInflater == null) {
                mInflater = LayoutInflater.from(parent.getContext());
            }
            holder = new RecyclerViewHolder(mContext, mInflater.inflate(getItemLayoutId(viewType), parent, false));
        }
        if (mItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (mItemClickListener == null) {
                        return;
                    }
                    int pos = holder.getLayoutPosition();
                    mItemClickListener.onItemClick(holder.itemView, pos, isFooterViewPos(pos) || isFooterViewPos(pos));
                }
            });
        }
        if (mItemLongClickListener != null) {
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (mItemLongClickListener != null) {
                        mItemLongClickListener.onItemLongClick(holder.itemView, holder.getLayoutPosition());
                    }
                    return true;
                }
            });
        }

//        MyLog.v("BaseRecyclerAdapter", "onCreateViewHolder : " + (holder == null ? "null" : holder.getLayoutPosition()));
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
//        MyLog.v("BaseRecyclerAdapter", "onBindViewHolder : " + position);
        if (isHeaderViewPos(position) || isFooterViewPos(position)) {
            return;
        }
        bindData(holder, position, mData.get(position - getHeadersCount()));
    }

    //GridLayoutManager
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mRecyclerView = recyclerView;
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            final GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            final GridLayoutManager.SpanSizeLookup spanSizeLookup = gridLayoutManager.getSpanSizeLookup();
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    if (isHeaderViewPos(position) || isFooterViewPos(position)) {
                        return gridLayoutManager.getSpanCount();
                    }
                    return spanSizeLookup.getSpanSize(position);
                }
            });
        }
    }

    //StaggeredGridLayoutManager
    @Override
    public void onViewAttachedToWindow(RecyclerViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        int position = holder.getLayoutPosition();
        if (isHeaderViewPos(position) || isFooterViewPos(position)) {
            ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();

            if (lp != null
                    && lp instanceof StaggeredGridLayoutManager.LayoutParams) {

                StaggeredGridLayoutManager.LayoutParams p =
                        (StaggeredGridLayoutManager.LayoutParams) lp;

                p.setFullSpan(true);
            }
        }
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        releaseCacheDrawable();
    }

    @Override
    public int getItemViewType(int position) {
        if (isHeaderViewPos(position)) {
            return mHeaderViews.keyAt(position);
        } else if (isFooterViewPos(position)) {
            return mFootViews.keyAt(position - getHeadersCount() - getRealItemCount());
        }
        return super.getItemViewType(position - getHeadersCount());
    }

    public T getItem(int position) {
        if ((position < getRealItemCount()) && (position >= 0)) {
            return mData.get(position);
        }
        return null;
    }

    public T getItemFromAll(int position) {
        position = position - getHeadersCount();
        if ((position < getRealItemCount()) && (position >= 0)) {
            return mData.get(position);
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return getHeadersCount() + getFootersCount() + getRealItemCount();
    }

    public int getCount() {
        return getHeadersCount() + getFootersCount() + getRealItemCount();
    }

    public int getRealItemCount() {
        return mData.size();
    }

    public List<T> getAllItems() {
        return mData;
    }

    public void replaceAll(Collection<T> items) {
        int preSize = mData.size();
        mData.clear();
        if (items == null || items.isEmpty()) {
            notifyItemRangeRemoved(getHeadersCount(), preSize);
        } else {
            mData.addAll(items);
            notifyDataSetChanged();
        }
    }

    public void setDatas(Collection<T> items) {
        int preSize = mData.size();
        if (mData != items) {
            mData.clear();
            if (preSize > 0) {
                notifyItemRangeRemoved(getHeadersCount(), preSize);
            }
            if (items != null) {
                mData.addAll(items);
                notifyItemRangeInserted(getHeadersCount(), items.size());
            }
        } else {
            notifyItemRangeChanged(0, preSize);
        }
    }

    public void add(int pos, T item) {
        mData.add(pos, item);
        notifyItemInserted(getHeadersCount() + pos);
    }

    public void add(T item) {
        if (item != null) {
            mData.add(item);
        }
        notifyItemInserted(mData.size() + getHeadersCount());
    }

    public boolean update(int index, T item) {
        if (index >= mData.size()) {
            return false;
        }
        mData.set(index, item);
        notifyItemChanged(index + getHeadersCount(), 0);
        return true;
    }

    public void addTo(int pos, Collection<T> items) {
        if (items == null || items.size() == 0) {
            return;
        }

        if (pos > getCount()) {
            mData.addAll(getCount(), items);
        } else {
            mData.addAll(pos, items);
        }
        notifyItemRangeInserted(pos, items.size());
    }

    public void add(Collection<T> items) {
        if (items == null || items.size() == 0) {
            return;
        }
        int start = mData.size() + getHeadersCount();
        mData.addAll(items);
        notifyItemRangeInserted(start, items.size());
    }

    public void addAll(int index, Collection<T> items) {
        if (items == null || items.size() == 0) {
            return;
        }
        int start = mData.size() + getHeadersCount();
        mData.addAll(0, items);
        notifyItemRangeInserted(index, items.size());
    }

    public void add(Collection<T> items, int childCount) {
        if (items == null || items.size() == 0) {
            return;
        }
        int start = mData.size() + getHeadersCount();
        mData.addAll(items);

        int count = items.size();
        childCount -= 1;
//        MyLog.v(TAG, "childCoutnt = " + childCount);
        if (childCount > 0) {
            notifyItemRangeInserted(start, childCount < count ? childCount : count);
        }
    }

    public boolean remove(int pos) {
        if (pos >= mData.size()) {
            return false;
        }
        mData.remove(pos);
        notifyItemRemoved(pos + getHeadersCount());
        return true;
    }

    public boolean replace(int pos, T item) {
        if (pos >= mData.size()) {
            return false;
        }
        mData.set(pos, item);
        notifyItemChanged(pos + getHeadersCount());
        return true;
    }

    public OnItemClickListener getOnItemClickListener() {
        return mItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mItemClickListener = listener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        mItemLongClickListener = listener;
    }

    abstract public void bindData(RecyclerViewHolder holder, int position, T item);

    abstract public int getItemLayoutId(int viewType);

    public interface OnItemClickListener {
        void onItemClick(View itemView, int pos, boolean isHeadOrFoot);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(View itemView, int pos);
    }


    protected boolean isHeaderViewPos(int position) {
        return position < getHeadersCount();
    }

    protected boolean isFooterViewPos(int position) {
        return position >= getHeadersCount() + getRealItemCount();
    }

    public void addHeaderView(View view) {
        mHeaderViews.put(mHeaderViews.size() + BASE_ITEM_TYPE_HEADER, view);
        notifyItemChanged(mHeaderViews.size() - 1);
    }

    public void addFootView(View view) {
        mFootViews.put(mFootViews.size() + BASE_ITEM_TYPE_FOOTER, view);
        notifyItemChanged(getItemCount() - 1);
    }

    public int getHeadersCount() {
        return mHeaderViews.size();
    }

    public int getFootersCount() {
        return mFootViews.size();
    }

    /**
     * 清空adapter,不仅仅包括数据,header和footer还包括点击事件的清除
     */
    public void clear() {
        if (mData != null) {
            mData.clear();
        }
        mContext = null;
        mInflater = null;
        if (mFootViews != null) {
            mFootViews.clear();
        }
        if (mHeaderViews != null) {
            mHeaderViews.clear();
        }
        mItemClickListener = null;
        mItemLongClickListener = null;
        if (cacheDrawables != null) {
            cacheDrawables.clear();
        }
    }

    @Override
    public long getItemId(int position) {
        return -1;
    }

    protected void addToCacheDrawables(int resID, Drawable drawable) {
        if (cacheDrawables == null) {
            cacheDrawables = new SparseArray<>();
        }
        cacheDrawables.put(resID, drawable);
    }

    protected Drawable getCacheDrawable(int resId) {
        return getCacheDrawable(resId, 22 * oneHeight, 22 * oneHeight);
    }

    protected Drawable getCacheDrawable(int resId, int w, int h) {
        if (resId <= 0) {
            return null;
        }
        Drawable drawable = null;
        if (cacheDrawables == null) {
            cacheDrawables = new SparseArray<>();
        } else {
            drawable = cacheDrawables.get(resId);
        }
        if (drawable == null) {
            drawable = getDrawable(resId, w, h);
            addToCacheDrawables(resId, drawable);
        }
        return drawable;
    }

    protected Drawable getDrawable(int resId) {
        return getDrawable(resId, 22 * oneHeight, 22 * oneHeight);
    }

    protected Drawable getDrawable(int resId, int w, int h) {
        Drawable drawable = null;
        if (Build.VERSION.SDK_INT >= 21) {
            drawable = MyApplication.Companion.getApplication().getResources().getDrawable(resId, null);
        } else {
            drawable = MyApplication.Companion.getApplication().getResources().getDrawable(resId);
        }
        drawable.setBounds(0, 0, w, h);
        return drawable;
    }

    protected void releaseCacheDrawable() {
        int size = (cacheDrawables == null ? 0 : cacheDrawables.size());
        if (size == 0) {
            return;
        }
        for (int i = 0; i < size; ++i) {
            Drawable d = cacheDrawables.valueAt(i);
            if (d != null) {
                d.setCallback(null);
            }
        }
    }

    public void setDatasNotUpdate(Collection<T> adds) {

        mData.clear();

        if (adds != null) {
            mData.addAll(adds);
        }
    }


    public SpannableStringBuilder getTextSpan(String msgContent, int start, int end, int corlor) {
        SpannableStringBuilder textbBuilder = new SpannableStringBuilder(msgContent);
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(corlor);

        textbBuilder.setSpan(colorSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        return textbBuilder;
    }

    public SpannableStringBuilder getTextSpan(SpannableStringBuilder textbBuilder, int start, int end, int corlor) {
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(corlor);

        textbBuilder.setSpan(colorSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        return textbBuilder;
    }

    protected SpannableStringBuilder getTextSpan(String message, int color, int end) {
        SpannableStringBuilder textbBuilder = new SpannableStringBuilder(message);
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(color);
        textbBuilder.setSpan(colorSpan, 0, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return textbBuilder;
    }

}
