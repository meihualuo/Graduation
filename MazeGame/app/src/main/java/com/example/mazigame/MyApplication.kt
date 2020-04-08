package com.example.mazigame

import android.app.Application
import com.example.mazigame.bean.GameBeam
import com.example.mazigame.model.ArchiveModel
import com.example.mazigame.util.StringUtil
import org.json.JSONObject

class MyApplication: Application() {

    companion object {
        lateinit var instance:MyApplication
        fun getApplication():MyApplication{
            return instance
        }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        intiGameBean()
    }



    fun intiGameBean(){
        var text = ArchiveModel.readFile(this, StringUtil.FILE_SET_UP)
        if (text != null){
            var json = JSONObject(text)
            GameBeam.getInstance().degree = (json[StringUtil.KEY_DEGREE] as Int?) ?: 10
            GameBeam.getInstance().type = (json[StringUtil.KEY_TYPE] as String?) ?: StringUtil.TYPE_TRADITION
        }else{
            GameBeam.getInstance().degree =10
            GameBeam.getInstance().type = StringUtil.TYPE_TRADITION
        }
        GameBeam.getInstance().name = "user"
        ArchiveModel.saveSetUp(this)
    }
}