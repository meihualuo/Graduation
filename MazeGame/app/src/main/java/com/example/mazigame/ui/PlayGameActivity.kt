package com.example.mazigame.ui

import android.annotation.SuppressLint
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.view.View
import com.example.mazigame.R
import com.example.mazigame.base.BaseActivity
import com.example.mazigame.bean.GameBeam
import com.example.mazigame.model.CubeModel
import com.example.mazigame.model.MapModel
import com.example.mazigame.presenter.PlayGamePresenter
import com.example.mazigame.util.StringUtil
import kotlinx.android.synthetic.main.activity_play_game.*
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import java.lang.Math.abs
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

class PlayGameActivity : BaseActivity(), View.OnClickListener,
    PlayGamePresenter.PlayGameLinstener,
    SensorEventListener{
    private lateinit var mSensorManage:SensorManager
    private lateinit var mSensor:Sensor
    var x = 0f
    var y = 0f

    lateinit var mPresenter:PlayGamePresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        
        super.onCreate(savedInstanceState)
        var isContinution = intent.getBooleanExtra("jixuyouxi",false)
        mPresenter = PlayGamePresenter().also{
            it.setListener(this)
            it.onCreate(this,isContinution)
        }
//        if(isContinution)
//            mPresenter.onContinue()0

//        val aa = getSystemService(SEARCH_SERVICE)

        mSensorManage = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        mSensor = mSensorManage.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)


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
            show_map -> {
                mPresenter.showMap()
            }
            stair -> {
                mPresenter.clickStair()
            }
        }
    }

    override fun setStair(visi:Int){
        stair.visibility = visi
    }

    override fun onResume() {
        super.onResume()
        if(GameBeam.getInstance().manipulation == StringUtil.MANI_TYPE_GRAVITY){
            mSensorManage.registerListener(this,mSensor,SensorManager.SENSOR_DELAY_GAME)
            listenerSensor()
        }
    }

    override fun onPause() {
        super.onPause()
        mSensorManage.unregisterListener(this)
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
    //通过手机方向控制游戏方向
    fun listenerSensor(){
        Observable.interval(0,300,TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                val threshold = 1.5f
                if (x > threshold && abs(y)< 1){
                    mPresenter.movePeople(MapModel.MOVE_OF_LEFT)
                }
                if (x < -threshold && abs(y)< 1){
                    mPresenter.movePeople(MapModel.MOVE_OF_RIGHT)
                }
                if (y > threshold && abs(x)< 1){
                    mPresenter.movePeople(MapModel.MOVE_OF_BOTTOM)
                }
                if (y < -threshold && abs(x)< 1){
                    mPresenter.movePeople(MapModel.MOVE_OF_TOP)
                }
            }
    }

    //传感器获取值发生改变时调用此函数
    override fun onSensorChanged(event: SensorEvent?) {
        this.x = event?.values?.get(0) ?: 0f
        this.y = event?.values?.get(1) ?: 0f
//        val z = event?.values?.get(2)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }

}
