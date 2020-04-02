package com.example.mazigame.model

import android.content.Context
import com.example.mazigame.bean.GameBeam
import com.example.mazigame.util.StringUtil
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat

class ArchiveModel {

    companion object {
        fun setDatas(context: Context) {
            GameBeam.getInstance().let {
                var name = it.name
                var duration = it.duration
                var degree = it.degree
                var type = it.type
                var map = MapModel.mapToString(it.map)
                var people =
                    it.people?.weighe.toString() + "-" + it.people?.heighe.toString()
                setDatas(context, name, duration, degree, type, map, people)
            }
        }

        fun setDatas(
            context: Context,
            name: String?,
            duration: Long?,
            degree: Int?,
            type: String?,
            map: String,
            people: String
        ) {
            var jsonObjects = JSONObject().apply {
                StringUtil.let {
                    put(it.KEY_DURATION, duration)
                    put(it.KEY_DEGREE, degree)
                    put(it.KEY_TYPE, type)
                    put(it.KEY_MAP, map)
                    val formatter = SimpleDateFormat("yyyy-MM-dd:HH:mm")
                    put(it.KEY_TIME, formatter.format(System.currentTimeMillis()))
                    put(it.KEY_PEOPLE, people)
                }
            }
            val jsonText = readFile(context,StringUtil.FILE_ARCHIVE)
            var jsonObject:JSONObject
            if (jsonText != null)
                jsonObject = JSONObject(jsonText)
            else
                jsonObject = JSONObject()
            jsonObject.put(name,jsonObjects)
            saveFile(context, jsonObject.toString(), StringUtil.FILE_ARCHIVE)
        }

        fun saveSetUp(context: Context){
            var jsonObjects = JSONObject().apply {
                GameBeam.getInstance().let {
                    put(StringUtil.KEY_DEGREE,it.degree)
                    put(StringUtil.KEY_TYPE,it.type)
                }
            }
            saveFile(context,jsonObjects.toString(),StringUtil.FILE_SET_UP)
        }

        fun saveFile(context: Context, fileString: String, fileName: String): Boolean {
            return try {
                var file = context.openFileOutput(fileName, Context.MODE_PRIVATE)
                file.write(fileString.toByteArray())
                file.flush()
                file.close()
                true
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }

        fun readFile(context: Context,fileName: String): String? {
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