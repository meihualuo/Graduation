package com.example.mazigame.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mazigame.R
import com.example.mazigame.base.BaseActivity
import com.example.mazigame.base.RecycleViewDivider
import com.example.mazigame.model.ArchiveModel
import com.example.mazigame.ui.Adapter.ArchiveAdadpter
import com.example.mazigame.ui.Adapter.ArchiveEntry
import com.example.mazigame.ui.Adapter.HisScoreAdapter
import com.example.mazigame.ui.Adapter.ScoreEnty
import com.example.mazigame.util.DensityUtil
import com.example.mazigame.util.StringUtil
import kotlinx.android.synthetic.main.activity_archive.*
import kotlinx.android.synthetic.main.activity_his_score.*
import kotlinx.android.synthetic.main.activity_his_score.recycler
import org.json.JSONArray
import org.json.JSONObject

class HisScoreActivity : BaseActivity() {

    var mAdapter:HisScoreAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTitle("历史得分")
        initAdapter()
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_his_score
    }

    fun initAdapter(){
        mAdapter = HisScoreAdapter(this)

        setLinearLayoutManager(recycler)
        val divider = RecycleViewDivider(
            this,
            LinearLayoutManager.HORIZONTAL, 1, ContextCompat.getColor(this, R.color.line_gray)
        )
        divider.left = DensityUtil.dip2px(this,15f)
        recycler.addItemDecoration(divider)

        recycler.adapter = mAdapter
        val archiveText = ArchiveModel.readFile(this, StringUtil.FILE_SCORE)
        val jsonArray = JSONArray(archiveText)
        for (i in 0 until jsonArray.length()){
            val json = jsonArray.getJSONObject(i)
            val sorceEnty = ScoreEnty()
            sorceEnty.name = json.getString(StringUtil.KEY_NAME)
            sorceEnty.time = json.getString(StringUtil.KEY_TIME)
            sorceEnty.score = json.getInt(StringUtil.KEY_SCORE)
            mAdapter?.add(sorceEnty)

        }
    }

}
