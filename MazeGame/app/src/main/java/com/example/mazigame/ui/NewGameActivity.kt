package com.example.mazigame.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.SeekBar
import com.example.mazigame.MyApplication
import com.example.mazigame.R
import com.example.mazigame.base.BaseActivity
import com.example.mazigame.bean.GameBeam
import com.example.mazigame.model.ArchiveModel
import com.example.mazigame.model.MapModel
import com.example.mazigame.util.StringUtil
import com.example.mazigame.view.MySeekBrd
import kotlinx.android.synthetic.main.activity_new_game.*

class NewGameActivity : BaseActivity() , View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        var aa = MapModel(20,20)
        initView()

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

    override fun onClick(v: View?) {
        when(v){
            new_game_create -> {
                GameBeam.getInstance().type = StringUtil.TYPE_MULTI_LAYER
                ArchiveModel.saveSetUp(this)
                startActivity(PlayGameActivity::class.java)
            }
        }
    }
}
