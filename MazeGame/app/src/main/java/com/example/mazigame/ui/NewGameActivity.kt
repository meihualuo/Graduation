package com.example.mazigame.ui

import android.os.Bundle
import android.view.View
import com.example.mazigame.R
import com.example.mazigame.base.BaseActivity
import com.example.mazigame.model.MapModel
import kotlinx.android.synthetic.main.activity_new_game.*

class NewGameActivity : BaseActivity() , View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        var aa = MapModel(20,20)
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_new_game
    }

    override fun onClick(v: View?) {
        when(v){
            new_game_create ->
                startActivity(PlayGameActivity::class.java)
        }
    }
}
