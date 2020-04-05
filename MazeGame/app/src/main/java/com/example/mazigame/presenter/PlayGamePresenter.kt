package com.example.mazigame.presenter

import android.content.Context
import android.view.LayoutInflater
import com.example.mazigame.R
import com.example.mazigame.base.MaterialDialog
import com.example.mazigame.bean.GameBeam
import com.example.mazigame.model.ArchiveModel
import com.example.mazigame.model.CubeModel
import com.example.mazigame.model.MapModel
import com.example.mazigame.util.StringUtil
import com.example.mazigame.view.MazeView
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
    var mMapDialog:MaterialDialog? = null
    var mapView:MazeView? = null

    fun setListener(listener: PlayGameLinstener){
        this.listener = listener
    }

    fun onCreate(context: Context,oldGame: Boolean){
        this.mContext = context
        GlobalScope.launch(Dispatchers.Main) {
            if (oldGame){
                val archiveText = ArchiveModel.readFile(mContext!!, StringUtil.FILE_ARCHIVE)
                if (archiveText != null) {
                    withContext(Dispatchers.IO) {
                        val jsonObjectA = JSONObject(archiveText)
                        val jsonG = jsonObjectA.getJSONObject(GameBeam.getInstance().name)
                        val map: String = jsonG.getString(StringUtil.KEY_MAP)
                        val mapA= MapModel.stringToMap(map)
                        mMapModel = MapModel(mapA)
                        val ps = jsonG.getString(StringUtil.KEY_PEOPLE)
                        val psToArray = ps.split("-")
                        mPeople = CubeModel(psToArray[0].toInt(), psToArray[1].toInt())
                        GameBeam.getInstance().let {
//                            it.mMapModel = mMapModel
//                            it.people = mPeople
                            it.degree = jsonG.getInt(StringUtil.KEY_DEGREE)
                            it.type = jsonG.getString(StringUtil.KEY_TYPE)
                            it.duration = jsonG.getLong(StringUtil.KEY_DURATION)
                        }
                        ArchiveModel.saveSetUp(mContext!!)
                    }
                }
            }else{
                mMapModel = withContext(Dispatchers.IO){
                    return@withContext MapModel(GameBeam.getInstance().degree!!)
                }
                mMapModel?.setTerminal()
                mPeople = CubeModel(1,1)
            }
            listener?.updateView(mMapModel!!, mPeople!!)
            GameBeam.getInstance().let {
                it.mMapModel = mMapModel
                it.people = mPeople
                it.startTime = System.currentTimeMillis()
            }
        }
    }

    fun movePeople(direction: Int){
        if (mMapModel == null || mPeople == null)
            return
        when (direction){
            MapModel.MOVE_OF_LEFT ->
                if(mPeople?.weighe!! > 1 && mMapModel?.map!![mPeople?.weighe!! -1][mPeople?.heighe!!] != MapModel.COLUMN)
                    mPeople!!.weighe -= 2
                else return
            MapModel.MOVE_OF_RIGHT ->
                if(mPeople?.weighe!! <mMapModel?.wide!!-2 && mMapModel?.map!![mPeople?.weighe!!+1][mPeople?.heighe!!] != MapModel.COLUMN)
                    mPeople!!.weighe += 2
                else return
            MapModel.MOVE_OF_TOP ->
                if(mPeople?.heighe!! >1 && mMapModel?.map!![mPeople?.weighe!!][mPeople?.heighe!!-1] != MapModel.COLUMN)
                    mPeople!!.heighe -= 2
                else return
            MapModel.MOVE_OF_BOTTOM ->
                if(mPeople?.heighe!! < mMapModel?.wide!!-2 && mMapModel?.map!![mPeople?.weighe!!][mPeople?.heighe!!+1] != MapModel.COLUMN)
                    mPeople!!.heighe += 2
                else return
        }
        listener?.movePeople(mPeople!!,direction)
        if(mMapModel?.map!![mPeople?.weighe!!][mPeople?.heighe!!] == MapModel.TERMINAL)
            onPass()
    }

    fun onPass(){

        GlobalScope.launch(Dispatchers.Main) {
            mMapModel = withContext(Dispatchers.IO){
                GameBeam.getInstance().degreeAdd()
                ArchiveModel.saveSetUp(mContext!!)
                Thread.sleep(500)
                return@withContext MapModel(GameBeam.getInstance().degree!!)
            }
            mMapModel?.setTerminal()
            mPeople = CubeModel(1,1)
            listener?.updateView(mMapModel!!,mPeople!!)
            GameBeam.getInstance().let {
                it.mMapModel = mMapModel
                it.people = mPeople
            }
        }
    }

    fun prompRoad(){
        if (mMapModel == null || mPeople == null)
            return
        GlobalScope.launch(Dispatchers.Main) {

            val roadList:List<CubeModel> = ArrayList()
            val nowCube = CubeModel(mMapModel!!.wide-2,mMapModel!!.wide-2)
            val isPrompt = withContext(Dispatchers.IO){
                return@withContext MapModel.promptRoad(mMapModel!!.map, roadList, mPeople, nowCube)
            }
//            if (isPrompt!!){
//                listener?.showPrompRoad(roadList)
//            }
            showMap()
            mapView?.setPrompt(true,roadList)
        }
    }

    fun saveArchive(){
        ArchiveModel.setDatas(mContext!!)
    }

    fun showMap(){
        if (mMapDialog != null){
            mMapDialog?.show()
            mapView?.setPrompt(false,ArrayList())
            return
        }
        mMapDialog = MaterialDialog(mContext)
        val view = LayoutInflater.from(mContext).inflate(R.layout.game_map_item,null)
        mapView = view.findViewById<MazeView>(R.id.map_view)
        GameBeam.getInstance()?.let {
            mapView?.updateGame(it.mMapModel!!,it.people!!)
        }
        mMapDialog?.setView(view)
        mMapDialog?.setCanceledOnTouchOutside(true)
        mMapDialog?.show()

    }

    fun dropOut(){

    }
}