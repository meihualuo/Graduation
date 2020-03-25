package com.example.mazigame.ui

import android.os.Bundle
import android.view.View
import com.example.mazigame.R
import com.example.mazigame.base.BaseActivity
import com.example.mazigame.view.MazeView
import kotlinx.android.synthetic.main.activity_play_game.*
import org.json.JSONObject

class PlayGameActivity : BaseActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_play_game
    }

    override fun onClick(v: View?) {
        when(v){
            btn_top -> {
                maze_view.movePeople(MazeView.MOVE_TOP)
            }
            btn_bottom -> {
                maze_view.movePeople(MazeView.MOVE_BOTTOM)
            }
            btn_left -> {
                maze_view.movePeople(MazeView.MOVE_LEFT)
            }
            btn_right -> {
                maze_view.movePeople(MazeView.MOVE_RIGHT)
            }
        }
    }
}
