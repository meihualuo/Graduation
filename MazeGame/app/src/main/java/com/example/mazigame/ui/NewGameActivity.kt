package com.example.mazigame.ui

import android.os.Bundle
import android.view.View
import com.example.mazigame.R
import com.example.mazigame.base.BaseActivity
import kotlinx.android.synthetic.main.activity_new_game.*

class NewGameActivity : BaseActivity() , View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_game)
    }

    override fun onClick(v: View?) {
        when(v){
            new_game_create ->
                startActivity(PlayGameActivity::class.java)
        }
    }
}
