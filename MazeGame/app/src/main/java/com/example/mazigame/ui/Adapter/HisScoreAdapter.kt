package com.example.mazigame.ui.Adapter

import android.content.Context
import com.example.mazigame.R
import com.example.mazigame.base.BaseRecyclerAdapter
import com.example.mazigame.base.RecyclerViewHolder

class HisScoreAdapter(var mContex:Context):BaseRecyclerAdapter<ScoreEnty>(mContex) {
    override fun bindData(holder: RecyclerViewHolder?, position: Int, item: ScoreEnty?) {
        holder?.setText(R.id.name,item?.name)
        holder?.setText(R.id.time,item?.time)
        holder?.setText(R.id.score,item?.score.toString())
    }

    override fun getItemLayoutId(viewType: Int): Int {
        return R.layout.his_score_item
    }

}