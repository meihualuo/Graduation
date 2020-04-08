package com.example.mazigame.presenter

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.widget.EditText
import androidx.annotation.RequiresApi
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
import java.util.concurrent.ThreadLocalRandom

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
    var mDataDialog:MaterialDialog? = null
    var mapView:MazeView? = null

    fun setListener(listener: PlayGameLinstener){
        this.listener = listener
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun onCreate(context: Context, oldGame: Boolean){
        this.mContext = context
        GlobalScope.launch(Dispatchers.Main) {
            if (oldGame){
                val archiveText = ArchiveModel.readFile(mContext!!, StringUtil.FILE_ARCHIVE)
                if (archiveText != null) {
                    withContext(Dispatchers.IO) {
                        val allObjectA = JSONObject(archiveText)
                        val jsonG = allObjectA.getJSONObject(allObjectA.getString(StringUtil.KEY_NEWEST))
                        GameBeam.getInstance().let {
//                            it.mMapModel = mMapModel
//                            it.people = mPeople
                            it.degree = jsonG.getInt(StringUtil.KEY_DEGREE)
                            it.type = jsonG.getString(StringUtil.KEY_TYPE)
                            it.duration = jsonG.getLong(StringUtil.KEY_DURATION)
                            val map = ArchiveModel.readFile(mContext!!,allObjectA.getString(StringUtil.KEY_NEWEST))
                            when(it.type){
                                StringUtil.TYPE_TRADITION -> {
                                    val mapA= MapModel.stringToMap(map)
                                    mMapModel = MapModel(mapA)
                                }
                                StringUtil.TYPE_MULTI_LAYER -> {
                                    val mapA= MapModel.stringToMapList(map)
                                    it.mMapModelList = mapA
                                    mMapModel = mapA[0]
                                }
                            }
                        }
                        val ps = jsonG.getString(StringUtil.KEY_PEOPLE)
                        val psToArray = ps.split("-")
                        mPeople = CubeModel(psToArray[0].toInt(), psToArray[1].toInt())
                        ArchiveModel.saveSetUp(mContext!!)
                    }
                }
            }else{
                creatMap()
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
        when(mMapModel?.map!![mPeople?.weighe!!][mPeople?.heighe!!]){
            MapModel.TERMINAL ->  onPass()
            MapModel.STAIR_OUT -> {
                mMapModel = GameBeam.getInstance().mMapModelList?.get(1)
                listener?.updateView(mMapModel!!, mPeople!!)
                GameBeam.getInstance().let {
                    it.mMapModel = mMapModel
                    it.people = mPeople
                }
            }
            MapModel.STAIR_IN -> {
                mMapModel = GameBeam.getInstance().mMapModelList?.get(0)
                listener?.updateView(mMapModel!!, mPeople!!)
                GameBeam.getInstance().let {
                    it.mMapModel = mMapModel
                    it.people = mPeople
                }
            }
        }
    }

    fun onPass(){
        GlobalScope.launch(Dispatchers.Main) {
            withContext(Dispatchers.IO){
                GameBeam.getInstance().degreeAdd()
                ArchiveModel.saveSetUp(mContext!!)
                Thread.sleep(500)
            }
            creatMap()
            mMapModel?.setTerminal()
            mPeople = CubeModel(1,1)
            listener?.updateView(mMapModel!!,mPeople!!)
            GameBeam.getInstance().let {
                it.mMapModel = mMapModel
                it.people = mPeople
                it.startTime = System.currentTimeMillis()
            }
        }
    }

    suspend fun creatMap():MapModel{
        mMapModel = withContext(Dispatchers.IO){
            return@withContext MapModel(GameBeam.getInstance().degree!!)
        }
        if(GameBeam.getInstance().type == StringUtil.TYPE_TRADITION)
            mMapModel?.setTerminal()
        else{
            GameBeam.getInstance().mMapModelList = ArrayList()
            var random = ThreadLocalRandom.current()
            val x1 = random.nextInt(GameBeam.getInstance().degree!!)
            val y1 = random.nextInt(GameBeam.getInstance().degree!!)
            val x2 = random.nextInt(GameBeam.getInstance().degree!!)
            val y2 = random.nextInt(GameBeam.getInstance().degree!!)
            val cu1 = CubeModel(x1*2+1,y1*2+1)
            val cu2 = CubeModel(x2*2+1,y2*2+1)
            mMapModel!!.addStairOut(cu1)
            mMapModel!!.addStairOut(cu2)
            val mapModel2 = MapModel(GameBeam.getInstance().degree!!,cu1,cu2)
            mapModel2.setTerminal()
            GameBeam.getInstance().mMapModelList?.add(mMapModel!!)
            GameBeam.getInstance().mMapModelList?.add(mapModel2)
        }
        return this.mMapModel!!
    }

    fun prompRoad(){
        if (mMapModel == null || mPeople == null)
            return
        GlobalScope.launch(Dispatchers.Main) {

            val roadList:List<CubeModel> = ArrayList()
            val nowCube = CubeModel(mMapModel!!.wide-2,mMapModel!!.wide-2)
            withContext(Dispatchers.IO){
                return@withContext MapModel.promptRoad(mMapModel!!.map, roadList, mPeople, nowCube)
            }
            showMap()
            mapView?.setPrompt(true,roadList)
        }
    }

    fun saveArchive(){
//        ArchiveModel.setDatas(mContext!!)
        if (mDataDialog != null){
            mDataDialog?.show()
            return
        }
        mDataDialog = MaterialDialog(mContext)
        val view = LayoutInflater.from(mContext).inflate(R.layout.save_data_item,null)
        val edit = view.findViewById<EditText>(R.id.edit)
        mDataDialog?.setView(view)
        mDataDialog?.setCanceledOnTouchOutside(false)
        mDataDialog?.setPositiveButton("保存") {
            val text = edit.text.toString()
            if(!(text.isEmpty() || text == "")){
                GameBeam.getInstance().name = text
                ArchiveModel.setDatas(mContext!!)
                mDataDialog?.cancel()
            }
        }
        mDataDialog?.setNegativeButton("取消",null)
        mDataDialog?.show()
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