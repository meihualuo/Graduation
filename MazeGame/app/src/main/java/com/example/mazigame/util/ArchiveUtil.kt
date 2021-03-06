package com.example.mazigame.util

import android.content.Context
import com.example.mazigame.MyApplication
import com.example.mazigame.bean.GameBeam
import org.json.JSONObject
import java.text.SimpleDateFormat
import kotlin.math.abs

class ArchiveUtil {

    companion object {
        fun setDatas(context: Context) {
            GameBeam.getInstance().let {
                val name = it.name ?: "moren"
                val duration = (it.duration ?: 0) + abs(it.startTime!! - System.currentTimeMillis())
                it.startTime = System.currentTimeMillis()
                val degree = it.degree
                val type = it.type

                var map:String = ""
                when(it.type){
                    StringUtil.TYPE_TRADITION -> {
                        map = MapUtil.mapToString(it.mMapModel!!.map)!!
                    }
                    StringUtil.TYPE_MULTI_LAYER -> {
                        map = MapUtil.mapListToString(it.mMapModelList!!)!!
                    }
                }
                MyApplication.getApplication()
                val people =
                    it.people?.weighe.toString() + "-" + it.people?.heighe.toString()
                setDatas(context, name, duration, degree, type, map, people)
            }
        }

        fun setDatas(
            context: Context,
            name: String,
            duration: Long?,
            degree: Int?,
            type: String?,
            map: String,
            people: String
        ) {
            val formatter = SimpleDateFormat("yyyy-MM-dd:HH:mm")
            val timeToStr = formatter.format(System.currentTimeMillis())
            val jsonObject = JSONObject().apply {
                StringUtil.let {
                    put(it.KEY_DURATION, duration ?: 600000)
                    put(it.KEY_DEGREE, degree)
                    put(it.KEY_TYPE, type)
//                    put(it.KEY_MAP, map)
                    put(it.KEY_TIME, timeToStr)
                    put(it.KEY_PEOPLE, people)
                }
            }
            val jsonText = readFile(context,StringUtil.FILE_ARCHIVE)
            val allObject: JSONObject
            allObject = if (jsonText != null)
                JSONObject(jsonText)
            else
                JSONObject()
//            jsonObject.accumulate(StringUtil.KEY_NAME_ALL,name)


            allObject.put(name,jsonObject)
            allObject.put(StringUtil.KEY_NEWEST,name)
            saveFile(context, allObject.toString(), StringUtil.FILE_ARCHIVE)
            saveFile(context,map,name)
        }

        fun saveSetUp(context: Context){
            val jsonObjects = JSONObject().apply {
                GameBeam.getInstance().let {
                    put(StringUtil.KEY_DEGREE,it.degree)
                    put(StringUtil.KEY_TYPE,it.type)
                }
            }
            saveFile(context,jsonObjects.toString(),StringUtil.FILE_SET_UP)
        }

        fun saveFile(context: Context, fileString: String, fileName: String): Boolean {
            return try {
                val file = context.openFileOutput(fileName, Context.MODE_PRIVATE)
                file.write(fileString.toByteArray())
                file.flush()
                file.close()
                true
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }

        fun readFile(context: Context, fileName: String): String? {
            return try {
                val file = context.openFileInput(fileName)
                var temp = ByteArray(102400)
                var len: Int
                var result = StringBuffer()
                while (file.read(temp).also { len = it } > 0) {
                    result.append(String(temp, 0, len))
                }
                file.close()
                result.toString()
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }
}