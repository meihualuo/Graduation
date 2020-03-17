package com.example.mazigame.ui

import android.os.Bundle
import android.view.View
import com.example.mazigame.R
import com.example.mazigame.base.BaseActivity
import kotlinx.android.synthetic.main.activity_play_game.*

class PlayGameActivity : BaseActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play_game)
    }

    override fun onClick(v: View?) {
        when(v){
            btn_top -> {
                maze_view.moveOfTop()
            }
            btn_bottom -> {
                maze_view.moveOfBottom()
            }
            btn_left -> {
                maze_view.moveOfLeft()
            }
            btn_right -> {
                maze_view.moveOfRight()
            }
        }
    }

}
