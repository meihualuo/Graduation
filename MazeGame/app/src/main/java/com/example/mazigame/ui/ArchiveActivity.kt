package com.example.mazigame.ui

import android.content.Intent
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mazigame.R
import com.example.mazigame.base.BaseActivity
import com.example.mazigame.base.MaterialDialog
import com.example.mazigame.base.RecycleViewDivider
import com.example.mazigame.bean.GameBeam
import com.example.mazigame.ui.Adapter.ArchiveAdadpter
import com.example.mazigame.ui.Adapter.ArchiveEntry
import com.example.mazigame.util.ArchiveUtil
import com.example.mazigame.util.DensityUtil
import com.example.mazigame.util.StringUtil
import kotlinx.android.synthetic.main.activity_archive.*
import org.json.JSONObject

class ArchiveActivity : BaseActivity() {

    var mAdapter:ArchiveAdadpter ? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initAdapter()
        setListener()
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
        val archiveText = ArchiveUtil.readFile(this, StringUtil.FILE_ARCHIVE)
        if (archiveText == null || archiveText == "") {
            return
        }
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

    fun setListener(){
        mAdapter?.setOnItemClickListener { itemView, pos, isHeadOrFoot ->
            //启动游戏
            val dialog = MaterialDialog(this).apply {
                setTitle("继续游戏")
                setMessage("你要启动该游戏吗？")
                setPositiveButton("启动"){
                    GameBeam.getInstance().name = mAdapter?.getItem(pos)?.name
                    val intent = Intent(this@ArchiveActivity,PlayGameActivity::class.java)
                    intent.putExtra("jixuyouxi",true)
                    startActivity(intent)
                    cancel()
                }
                setNegativeButton("取消",null)
            }
            dialog.show()
        }

        mAdapter?.setOnItemLongClickListener { itemView, pos ->
            //删除游戏
            val dialog = MaterialDialog(this).apply {
                setTitle("删除存档")
                setMessage("你要删除该游戏吗？")
                setPositiveButton("删除"){
                    mAdapter?.remove(pos)
                    cancel()
                    //TODO 删除本地文件
                }
                setNegativeButton("取消",null)
            }
            dialog.show()
        }
    }

}
