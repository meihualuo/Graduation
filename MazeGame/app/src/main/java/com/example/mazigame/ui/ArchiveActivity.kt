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
import com.example.mazigame.util.DensityUtil
import com.example.mazigame.util.StringUtil
import kotlinx.android.synthetic.main.activity_archive.*
import org.json.JSONObject

class ArchiveActivity : BaseActivity() {

    var mAdapter:ArchiveAdadpter ? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initAdapter()
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_archive
    }

    fun initAdapter(){
        mAdapter = ArchiveAdadpter(this)

        setLinearLayoutManager(recycler)
        val divider = RecycleViewDivider(
            this,
            LinearLayoutManager.HORIZONTAL, 1, ContextCompat.getColor(this, R.color.colorAccent)
        )
        divider.left = DensityUtil.dip2px(this,15f)
        recycler.addItemDecoration(divider)

        recycler.adapter = mAdapter
        val archiveText = ArchiveModel.readFile(this, StringUtil.FILE_ARCHIVE)
        val jsonObject = JSONObject(archiveText)
        val keys: Iterator<String> = jsonObject.keys()
        while (keys.hasNext()){
            val key = keys.next()
            if(key != StringUtil.KEY_NEWEST){
                val archiveEnty = ArchiveEntry()
                val json = jsonObject.getJSONObject(key)
                archiveEnty.name = key
                archiveEnty.time = json.getString(StringUtil.KEY_TIME)
                mAdapter?.add(archiveEnty)
            }
        }
    }

}
