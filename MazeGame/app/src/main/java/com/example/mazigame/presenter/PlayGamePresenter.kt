package com.example.mazigame.presenter

import android.content.Context
import com.example.mazigame.bean.GameBeam
import com.example.mazigame.model.ArchiveModel
import com.example.mazigame.model.CubeModel
import com.example.mazigame.model.MapModel
import com.example.mazigame.util.StringUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject

class PlayGamePresenter {
    interface PlayGameLinstener {
        fun updateView(mapModel: MapModel,people:CubeModel)
        fun movePeople(people:CubeModel,direction: Int)
        fun showPrompRoad(list:List<CubeModel>)
        fun dropOut()
    }

    private var listener: PlayGameLinstener? = null
    var mContext:Context? = null
    var mMapModel:MapModel? = null
    var mPeople:CubeModel? = null

    fun setListener(listener: PlayGameLinstener){
        this.listener = listener
    }

    fun onCreate(context: Context,oldGame: Boolean){
        this.mContext = context
        GlobalScope.launch(Dispatchers.Main) {
            if (oldGame){
                val archiveText = ArchiveModel.readFile(mContext!!, StringUtil.FILE_ARCHIVE)
                if (archiveText != null) {
                    var ok = withContext(Dispatchers.IO) {
                        var jsonObjectA = JSONObject(archiveText)
                        var jsonG = jsonObjectA.getJSONObject("aaa")
                        var map: String = jsonG.getString(StringUtil.KEY_MAP)
                        var mapA= MapModel.stringToMap(map)
                        mMapModel = MapModel(mapA)
                        var ps = jsonG.getString(StringUtil.KEY_PEOPLE)
                        var psToArray = ps.split("-")
                        mPeople = CubeModel(psToArray[0].toInt(), psToArray[1].toInt())
                        GameBeam.getInstance().let {
                            it.mMapModel = mMapModel
                            it.people = mPeople
                            it.degree = jsonG.getInt(StringUtil.KEY_DEGREE)
                            it.type = jsonG.getString(StringUtil.KEY_TYPE)
                            it.duration = jsonG.getLong(StringUtil.KEY_DURATION)
                        }
                        ArchiveModel.saveSetUp(mContext!!)
                        return@withContext true
                    }
                    if (ok) {
                        listener?.updateView(mMapModel!!, mPeople!!)
                    }
                }
            }else{
                mMapModel = withContext(Dispatchers.IO){
                    return@withContext MapModel(GameBeam.getInstance().degree!!,GameBeam.getInstance().degree!!)
                }
                mPeople = CubeModel(1,1)
                listener?.updateView(mMapModel!!,mPeople!!)
                GameBeam.getInstance().let {
                    it.mMapModel = mMapModel
                    it.people = mPeople
                }

            }
        }
    }

    fun movePeople(direction: Int){
        if (mMapModel == null || mPeople == null)
            return
        when (direction){
            MapModel.MOVE_OF_LEFT ->
                if(mPeople?.weighe!! > 1 && mMapModel?.map!![mPeople?.weighe!! -1][mPeople?.heighe!!] == MapModel.ROAD)
                    mPeople!!.weighe -= 2
                else return
            MapModel.MOVE_OF_RIGHT ->
                if(mPeople?.weighe!! <mMapModel?.wide!!-2 && mMapModel?.map!![mPeople?.weighe!!+1][mPeople?.heighe!!] == MapModel.ROAD)
                    mPeople!!.weighe += 2
                else return
            MapModel.MOVE_OF_TOP ->
                if(mPeople?.heighe!! >1 && mMapModel?.map!![mPeople?.weighe!!][mPeople?.heighe!!-1] == MapModel.ROAD)
                    mPeople!!.heighe -= 2
                else return
            MapModel.MOVE_OF_BOTTOM ->
                if(mPeople?.heighe!! < mMapModel?.high!!-2 && mMapModel?.map!![mPeople?.weighe!!][mPeople?.heighe!!+1] == MapModel.ROAD)
                    mPeople!!.heighe += 2
                else return
        }
        listener?.movePeople(mPeople!!,direction)
        if(mPeople?.weighe == mMapModel!!.wide - 2 && mPeople?.heighe == mMapModel!!.high-2){

            GlobalScope.launch(Dispatchers.Main) {
                mMapModel = withContext(Dispatchers.IO){
                    GameBeam.getInstance().degreeAdd()
                    ArchiveModel.saveSetUp(mContext!!)
                    Thread.sleep(500)
                    return@withContext MapModel(GameBeam.getInstance().degree!!,GameBeam.getInstance().degree!!)
                }
                mPeople = CubeModel(1,1)
                listener?.updateView(mMapModel!!,mPeople!!)
            }
        }
    }

    fun prompRoad(){
        if (mMapModel == null || mPeople == null)
            return
        GlobalScope.launch {

            var roadList:List<CubeModel> = ArrayList()
            var nowCube = CubeModel(mMapModel!!.wide-2,mMapModel!!.high-2)
            var isPrompt = withContext(Dispatchers.IO){
                return@withContext MapModel.promptRoad(mMapModel!!.map, roadList, mPeople, nowCube)
            }
            if (isPrompt!!){
                listener?.showPrompRoad(roadList)
            }
        }
    }

    fun saveArchive(){
        ArchiveModel.setDatas(mContext!!)
    }

    fun dropOut(){

    }
}