package com.example.mazigame.ui.Adapter

import android.content.Context
import com.example.mazigame.R
import com.example.mazigame.base.BaseRecyclerAdapter
import com.example.mazigame.base.RecyclerViewHolder

class ArchiveAdadpter(mContex :Context) : BaseRecyclerAdapter<ArchiveEntry>(mContex) {
    override fun bindData(holder: RecyclerViewHolder?, position: Int, item: ArchiveEntry?) {
        holder?.setText(R.id.name,"名称：${item?.name}")
        holder?.setText(R.id.time,"保存时间:${item?.time}")
    }

    override fun getItemLayoutId(viewType: Int): Int {
        return R.layout.archive_item
    }
}