package com.example.mazigame.ui

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import com.example.mazigame.R
import com.example.mazigame.base.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)  //竖屏
        setContentView(R.layout.activity_main)
    }

    override fun onClick(v: View?) {
        when(v){
            main_newGame ->{
                startActivity(Intent(this,NewGameActivity::class.java))
            }
        }
    }
}
