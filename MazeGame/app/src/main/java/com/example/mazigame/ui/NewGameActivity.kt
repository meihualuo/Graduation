package com.example.mazigame.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.SeekBar
import com.example.mazigame.MyApplication
import com.example.mazigame.R
import com.example.mazigame.base.BaseActivity
import com.example.mazigame.bean.GameBeam
import com.example.mazigame.util.ArchiveUtil
import com.example.mazigame.util.StringUtil
import com.example.mazigame.view.MySeekBrd
import kotlinx.android.synthetic.main.activity_new_game.*

class NewGameActivity : BaseActivity() , View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        var aa = MapModel(20,20)
        initView()
        setPattern()
        setManipulation()
    }

    @SuppressLint("SetTextI18n")
    fun initView(){
        degree.text = getString(R.string.degree_int)+":"+GameBeam.getInstance().degree
        seek_brs.setProgressFloat(GameBeam.getInstance().degree!! - 10f)
        seek_brs.setOnSeekBarChangeListener( object : MySeekBrd.OnIndicatorSeekBarChangeListener {
            override fun onProgressChanged(
                seekBar: SeekBar?,
                progress: Int,
                indicatorOffset: Float
            ) {
                GameBeam.getInstance().degree = progress+10
                degree.text = getString(R.string.degree_int)+":"+(progress+10)
                MyApplication.getApplication()
            }
        })
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_new_game
    }

    fun setPattern(){
        val pattern = if (GameBeam.getInstance().type == StringUtil.TYPE_TRADITION) "经典" else "双层"
        new_game_pattern.text ="地图模式：$pattern"
    }

    fun setManipulation(){
        val mani = if (GameBeam.getInstance().manipulation == StringUtil.MANI_TYPE_BOARD) "键盘" else "重力"
        manipulation.text = "操纵方式：$mani"
    }

    override fun onClick(v: View?) {
        when(v){
            new_game_create -> {
//                GameBeam.getInstance().type = StringUtil.TYPE_TRADITION
                ArchiveUtil.saveSetUp(this)
                startActivity(PlayGameActivity::class.java)
            }
            new_game_pattern -> {
                GameBeam.getInstance().let {
                    it.type = if (it.type == StringUtil.TYPE_TRADITION) StringUtil.TYPE_MULTI_LAYER else StringUtil.TYPE_TRADITION
                    setPattern()
                }
            }
            manipulation -> {
                GameBeam.getInstance().let {
                    it.manipulation = if (it.manipulation == StringUtil.MANI_TYPE_BOARD) StringUtil.MANI_TYPE_GRAVITY else StringUtil.MANI_TYPE_BOARD
                    setManipulation()
                }
            }
        }
    }
}
