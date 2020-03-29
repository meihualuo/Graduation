package com.example.mazigame.base;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;


import com.example.mazigame.R;

import static androidx.core.content.ContextCompat.getColor;


/**
 * Created by drakeet on 9/28/14.
 */
public class MaterialDialog {

    private final static int BUTTON_BOTTOM = 9;
    private final static int BUTTON_TOP = 9;

    private boolean mCancel;
    private boolean mCancelable = true;
    private Context mContext;
    private AlertDialog mAlertDialog;
    private Builder mBuilder;
    private View mView;
    private int mTitleResId;
    private CharSequence mTitle;
    private int mMessageResId;
    private CharSequence mMessage;
    private Button mPositiveButton;
    private LinearLayout.LayoutParams mLayoutParams;
    private Button mNegativeButton;
    private boolean mHasShow = false;
    private Drawable mBackgroundDrawable;
    private int mBackgroundResId;
    private View mMessageContentView;
    private View.OnClickListener defaultBtnOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mAlertDialog.cancel();
        }
    };
    //默认消失即关闭
    private DialogInterface.OnDismissListener mOnDismissListener = new DialogInterface.OnDismissListener() {

        @Override
        public void onDismiss(DialogInterface dialog) {
            Log.w("MaterialDialog", "onDismiss");
            if (dialog != null)
                dialog.cancel();
            cancel();
        }
    };


    public MaterialDialog(Context context) {
        this.mContext = context;
    }

    public void show() {
        if (mHasShow == false || mAlertDialog == null)
            mBuilder = new Builder();
        else
            mAlertDialog.show();
        mHasShow = true;
        mAlertDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        setWindowWith(mAlertDialog.getWindow(), 0.9f);
    }

    public void setWindowWith(Window window, float widthPercent) {
        int wid = this.mContext.getResources().getDisplayMetrics().widthPixels;
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = (int) (wid * widthPercent);
        params.gravity = Gravity.CENTER;
        params.height = ActionBar.LayoutParams.WRAP_CONTENT;
        window.setAttributes(params);
    }

    public Window getWindow() {
        if (mAlertDialog == null)
            return null;
        return mAlertDialog.getWindow();
    }


    public MaterialDialog setView(View view) {
        mView = view;
        if (mBuilder != null) {
            mBuilder.setView(view);
        }
        return this;
    }

    public MaterialDialog setContentView(View view) {
        mMessageContentView = view;
        if (mBuilder != null) {
            mBuilder.setContentView(mMessageContentView);
        }
        return this;
    }

    public MaterialDialog setBackground(Drawable drawable) {
        mBackgroundDrawable = drawable;
        if (mBuilder != null) {
            mBuilder.setBackground(mBackgroundDrawable);
        }
        return this;
    }

    public MaterialDialog setBackgroundResource(int resId) {
        mBackgroundResId = resId;
        if (mBuilder != null) {
            mBuilder.setBackgroundResource(mBackgroundResId);
        }
        return this;
    }


    public void cancel() {
        //Log.w("MaterialDialog", "cancel");
        if (mAlertDialog != null) {
            Log.w("MaterialDialog", "mAlertDialog cancel");
            mAlertDialog.cancel();
            mAlertDialog = null;
            Log.w("MaterialDialog", "mAlertDialog  ==null -" + (mAlertDialog == null));
        }
        mBuilder = null;
    }

    public void dismiss() {
        if (mAlertDialog != null)
            mAlertDialog.dismiss();
    }

    private int dip2px(float dpValue) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    private static boolean isLollipop() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    public MaterialDialog setTitle(int resId) {
        mTitleResId = resId;
        if (mBuilder != null) {
            mBuilder.setTitle(resId);
        }
        return this;
    }

    public MaterialDialog setTitle(CharSequence title) {
        mTitle = title;
        if (mBuilder != null) {
            mBuilder.setTitle(title);
        }
        return this;
    }

    public MaterialDialog setMessage(int resId) {
        mMessageResId = resId;
        if (mBuilder != null) {
            mBuilder.setMessage(resId);
        }
        return this;
    }

    public MaterialDialog setMessage(CharSequence message) {
        mMessage = message;
        if (mBuilder != null) {
            mBuilder.setMessage(message);
        }
        return this;
    }

    /**
     * 显示软键盘
     */
    public void showSoftInput() {
        if (mAlertDialog == null) {
            return;
        }
        mAlertDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        mAlertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams
                .SOFT_INPUT_STATE_VISIBLE);
    }

    public AlertDialog getDialog() {
        return mAlertDialog;
    }

    private Button getDefaultPositiveButton() {
        Button mPositiveButton = new Button(mContext);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        mPositiveButton.setLayoutParams(params);
        mPositiveButton.setBackgroundResource(R.drawable.item_click_touming_alpha);
        mPositiveButton.setTextColor(getColor(mContext, R.color.main_blue));

        mPositiveButton.setGravity(Gravity.CENTER);
        mPositiveButton.setTextSize(15);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(dip2px(2), 0, dip2px(12), dip2px(BUTTON_BOTTOM));
        mPositiveButton.setLayoutParams(layoutParams);

        return mPositiveButton;

    }

    private Button getDefaultNegativeButton() {
        mNegativeButton = new Button(mContext);
        mLayoutParams = new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT
                , 1);
        mNegativeButton.setLayoutParams(mLayoutParams);
        mNegativeButton.setBackgroundResource(R.drawable.item_click_touming_alpha);
        mNegativeButton.setTextColor(Color.argb(255, 174, 174, 174));
        mNegativeButton.setTextSize(15);
        mNegativeButton.setGravity(Gravity.CENTER);
        return mNegativeButton;
    }

    public Button setPositiveButton(int resId, final View.OnClickListener listener) {
        mPositiveButton = getDefaultPositiveButton();
        mPositiveButton.setText(resId);
        mPositiveButton.setOnClickListener(listener == null ? defaultBtnOnClick : listener);

        return mPositiveButton;
    }


    public Button setPositiveButton(String text, final View.OnClickListener listener) {
        mPositiveButton = getDefaultPositiveButton();
        mPositiveButton.setText(text);
        mPositiveButton.setOnClickListener(listener == null ? defaultBtnOnClick : listener);
        return mPositiveButton;
    }

    public Button setNegativeButton(int resId, final View.OnClickListener listener) {
        mNegativeButton = getDefaultNegativeButton();
        mNegativeButton.setText(resId);
        mNegativeButton.setOnClickListener(listener == null ? defaultBtnOnClick : listener);


        return mNegativeButton;
    }

    public Button setNegativeButton(String text, final View.OnClickListener listener) {
        mNegativeButton = getDefaultNegativeButton();
        mNegativeButton.setText(text);
        mNegativeButton.setOnClickListener(listener == null ? defaultBtnOnClick : listener);

        return mNegativeButton;
    }

    public MaterialDialog setCanceledOnTouchOutside(boolean cancel) {
        this.mCancel = cancel;
        if (mBuilder != null) {
            mBuilder.setCanceledOnTouchOutside(mCancel);
        }
        return this;
    }

    public MaterialDialog setCancelable(boolean cancelable) {
        this.mCancelable = cancelable;
        if (mBuilder != null) {
            mBuilder.setCancelable(mCancelable);
        }
        return this;
    }

    public MaterialDialog setOnDismissListener(DialogInterface.OnDismissListener
                                                       onDismissListener) {
        this.mOnDismissListener = onDismissListener;
        return this;
    }


    private class Builder {

        private TextView mTitleView;
        private TextView mMessageView;
        private Window mAlertDialogWindow;
        private LinearLayout mButtonLayout;


        private Builder() {
            try {
                mAlertDialog = new AlertDialog.Builder(mContext).create();
                mAlertDialog.show();
            } catch (Exception e) {
                return;
            }
            //mAlertDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
            // WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
            // mAlertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams
            // .SOFT_INPUT_STATE_VISIBLE);
            if (null == mAlertDialog)
                return;
            mAlertDialogWindow = mAlertDialog.getWindow();
            View contv = LayoutInflater.from(mContext).inflate(R.layout.dialog_base_materialdialog,
                    null);
            contv.setFocusable(true);
            contv.setFocusableInTouchMode(true);

            mAlertDialogWindow.setBackgroundDrawableResource(R.drawable.material_dialog_window);

            mAlertDialogWindow.setContentView(contv);
            // mAlertDialogWindow.setContentView(R.layout.dialog_base_materialdialog);

//7
            WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.TYPE_PHONE,
                    WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM,
                    PixelFormat.TRANSLUCENT
            );

            mTitleView = mAlertDialogWindow.findViewById(R.id.title);
            mMessageView = mAlertDialogWindow.findViewById(R.id.message);
            mButtonLayout = mAlertDialogWindow.findViewById(R.id.buttonLayout);
            if (mView != null) {
                LinearLayout linearLayout = mAlertDialogWindow.findViewById(R.id
                        .contentView);
                linearLayout.removeAllViews();
                if (mView.getParent() != null)
                    ((LinearLayout) mView.getParent()).removeAllViews();
                linearLayout.addView(mView);
            }
            if (mTitleResId != 0) {
                setTitle(mTitleResId);
            }
            if (mTitle != null) {
                setTitle(mTitle);
            }
            if (mTitle == null && mTitleResId == 0) {
                mTitleView.setVisibility(View.GONE);
            }
            if (mMessageResId != 0) {
                setMessage(mMessageResId);
            }
            if (mMessage != null) {
                setMessage(mMessage);
            }

            mLayoutParams = new LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                    , 1);
            mLayoutParams.setMargins(0, 0, 0, dip2px(6));
            //mLayoutParams != null &&
            if (mNegativeButton != null) {
                if (mNegativeButton.getParent() != null) {
                    ((ViewGroup) mNegativeButton.getParent()).removeAllViews();
                }
                mNegativeButton.setPadding(0, dip2px(10), 0, 0);
                mNegativeButton.setLayoutParams(mLayoutParams);
                mButtonLayout.addView(mNegativeButton);
                mButtonLayout.addView(getLineHView());
//                }
            }
            if (mPositiveButton != null) {
                if (mPositiveButton.getParent() != null) {
                    ((ViewGroup) mPositiveButton.getParent()).removeView(mPositiveButton);
                }
                mPositiveButton.setLayoutParams(mLayoutParams);
                mPositiveButton.setPadding(0, dip2px(10), 0, 0);
                mButtonLayout.addView(mPositiveButton);
            }
            if (mButtonLayout.getChildCount() < 1) {//有一个empty占位符
                mAlertDialogWindow.findViewById(R.id.buttonLayoutView).setVisibility(View.GONE);
            }
            if (mBackgroundResId != 0) {
                LinearLayout linearLayout = mAlertDialogWindow.findViewById(R.id
                        .material_background);
                linearLayout.setBackgroundResource(mBackgroundResId);
            }
            if (mBackgroundDrawable != null) {
                LinearLayout linearLayout = mAlertDialogWindow.findViewById(R.id
                        .material_background);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    linearLayout.setBackground(mBackgroundDrawable);
                } else {
                    linearLayout.setBackgroundDrawable(mBackgroundDrawable);
                }
            }

            if (mMessageContentView != null) {
                this.setContentView(mMessageContentView);
            }
            mAlertDialog.setCanceledOnTouchOutside(mCancel);
            mAlertDialog.setCancelable(mCancelable);
            mAlertDialog.setOnDismissListener(mOnDismissListener);
        }

        public void setTitle(int resId) {
            mTitleView.setText(resId);
        }

        public void setTitle(CharSequence title) {
            mTitleView.setText(title);
        }

        public void setMessage(int resId) {
            if (mTitleView.getVisibility() == View.GONE) {
                mMessageView.setGravity(Gravity.CENTER);
            } else {
                mMessageView.setGravity(Gravity.LEFT);
            }
            mMessageView.setText(resId);
        }

        public void setMessage(CharSequence message) {
            if (mTitleView.getVisibility() == View.GONE) {
                mMessageView.setGravity(Gravity.CENTER);
            } else {
                mMessageView.setGravity(Gravity.LEFT);
            }
            mMessageView.setText(message);
        }

        public void setCancelable(boolean cancelable) {
            mAlertDialog.setCancelable(cancelable);
        }

        /**
         * set positive material_button
         *
         * @param text     the name of material_button
         * @param listener
         */
        public void setPositiveButton(String text, final View.OnClickListener listener) {
            Button button = getDefaultPositiveButton();
            button.setText(text);

            button.setOnClickListener(listener == null ? defaultBtnOnClick : listener);
            mButtonLayout.addView(button);
        }

        /**
         * set negative material_button
         *
         * @param text     the name of material_button
         * @param listener
         */
        public void setNegativeButton(String text, final View.OnClickListener listener) {
            Button button = getDefaultNegativeButton();
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout
                    .LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            button.setLayoutParams(params);
            button.setBackgroundResource(R.drawable.material_card);
            button.setText(text);

            button.setPadding(0, 0, 0, dip2px(8));
            button.setOnClickListener(listener == null ? defaultBtnOnClick : listener);
            if (mButtonLayout.getChildCount() > 0) {
                params.setMargins(20, 0, 10, dip2px(BUTTON_BOTTOM));
                button.setLayoutParams(params);
                mButtonLayout.addView(button, 1);
            } else {
                button.setLayoutParams(params);
                mButtonLayout.addView(button);
            }
        }

        public void setView(View view) {
            LinearLayout l = mAlertDialogWindow.findViewById(R.id.contentView);
            l.removeAllViews();
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup
                    .LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            view.setLayoutParams(layoutParams);

            view.setOnFocusChangeListener(
                    new View.OnFocusChangeListener() {
                        @Override
                        public void onFocusChange(View v, boolean hasFocus) {
                            System.out.println("-->" + hasFocus);
                            mAlertDialogWindow.setSoftInputMode(WindowManager.LayoutParams
                                    .SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                            // show imm
                            InputMethodManager imm = (InputMethodManager) mContext
                                    .getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.toggleSoftInput(
                                    InputMethodManager.SHOW_FORCED,
                                    InputMethodManager.HIDE_IMPLICIT_ONLY
                            );

                        }
                    }
            );

            l.addView(view);

            if (view instanceof ViewGroup) {

                ViewGroup viewGroup = (ViewGroup) view;

                for (int i = 0; i < viewGroup.getChildCount(); i++) {
                    if (viewGroup.getChildAt(i) instanceof EditText) {
                        EditText editText = (EditText) viewGroup.getChildAt(i);
                        editText.setFocusable(true);
                        editText.requestFocus();
                        editText.setFocusableInTouchMode(true);
                    }
                }
                for (int i = 0; i < viewGroup.getChildCount(); i++) {
                    if (viewGroup.getChildAt(i) instanceof AutoCompleteTextView) {
                        AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView)
                                viewGroup.getChildAt(i);
                        autoCompleteTextView.setFocusable(true);
                        autoCompleteTextView.requestFocus();
                        autoCompleteTextView.setFocusableInTouchMode(true);
                    }
                }
            }
        }

        public void setContentView(View contentView) {
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup
                    .LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            contentView.setLayoutParams(layoutParams);
            if (contentView instanceof ListView) {
                setListViewHeightBasedOnChildren((ListView) contentView);
            }
            LinearLayout linearLayout = mAlertDialogWindow.findViewById(R.id
                    .message_content_view);
            if (linearLayout != null) {
                linearLayout.removeAllViews();
                linearLayout.addView(contentView);
            }
            for (int i = 0; i < linearLayout.getChildCount(); i++) {
                if (linearLayout.getChildAt(i) instanceof AutoCompleteTextView) {
                    AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView)
                            linearLayout.getChildAt(i);
                    autoCompleteTextView.setFocusable(true);
                    autoCompleteTextView.requestFocus();
                    autoCompleteTextView.setFocusableInTouchMode(true);
                }
            }
        }

        public void setBackground(Drawable drawable) {
            LinearLayout linearLayout = mAlertDialogWindow.findViewById(R.id
                    .material_background);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                linearLayout.setBackground(drawable);
            } else {
                linearLayout.setBackgroundDrawable(drawable);
            }
        }

        public void setBackgroundResource(int resId) {
            LinearLayout linearLayout = mAlertDialogWindow.findViewById(R.id
                    .material_background);
            linearLayout.setBackgroundResource(resId);
        }


        public void setCanceledOnTouchOutside(boolean canceledOnTouchOutside) {
            mAlertDialog.setCanceledOnTouchOutside(canceledOnTouchOutside);
        }
    }


    /**
     * 动态测量listview-Item的高度
     *
     * @param listView
     */
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }


    public MaterialDialog showConfirmMsg(int msgId) {
        return showConfirmMsg(mContext.getString(msgId));
    }

    public MaterialDialog showConfirmMsg(String msg) {
        return showConfirmMsg(msg, R.string.confirm, null);
    }

    public MaterialDialog showConfirmMsg(String msg, int positiveResId, View.OnClickListener positiveClickLis) {

        setMessage(msg);
        setPositiveButton(positiveResId, positiveClickLis);
        show();
        return this;
    }

    public Button getmPositiveButton() {
        return mPositiveButton;
    }

    private View getLineHView() {
        View view = new View(mContext);
        view.setBackgroundResource(R.color.line_gray);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(dip2px(1), LinearLayout.LayoutParams.MATCH_PARENT);
        view.setLayoutParams(params);
        return view;
    }
}


