package com.example.mazigame.ui

import android.os.Bundle
import android.view.View
import com.example.mazigame.R
import com.example.mazigame.base.BaseActivity
import com.example.mazigame.model.CubeModel
import com.example.mazigame.model.MapModel
import com.example.mazigame.presenter.PlayGamePresenter
import kotlinx.android.synthetic.main.activity_play_game.*

class PlayGameActivity : BaseActivity(), View.OnClickListener, PlayGamePresenter.PlayGameLinstener {

    lateinit var mPresenter:PlayGamePresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        
        super.onCreate(savedInstanceState)
        var isContinution = intent.getBooleanExtra("jixuyouxi",false)
        mPresenter = PlayGamePresenter().also{
            it.setListener(this)
            it.onCreate(this,10,12,isContinution)
        }
//        if(isContinution)
//            mPresenter.onContinue()

    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_play_game
    }

    override fun onClick(v: View?) {
        when(v){
            btn_top -> {
                mPresenter.movePeople(MapModel.MOVE_OF_TOP)
            }
            btn_bottom -> {
                mPresenter.movePeople(MapModel.MOVE_OF_BOTTOM)
            }
            btn_left -> {
                mPresenter.movePeople(MapModel.MOVE_OF_LEFT)
            }
            btn_right -> {
                mPresenter.movePeople(MapModel.MOVE_OF_RIGHT)
            }
            road -> {
                maze_view.let {
                    if (it.showPromptRoad){
                        it.setPrompt(!it.showPromptRoad,ArrayList())
                    }else{
                        mPresenter.prompRoad()
                    }
                }
            }
            archive -> {
                mPresenter.saveArchive()
            }
        }
    }

    override fun updateView(mapModel: MapModel, people: CubeModel) {
        maze_view.updateGame(mapModel,people)
    }

    override fun movePeople(people: CubeModel,direction: Int) {
        maze_view.setPeople(people,direction)
    }

    override fun showPrompRoad(list: List<CubeModel>) {
        maze_view.setPrompt(true,list)
    }

    override fun dropOut() {
        finish()
    }

}
