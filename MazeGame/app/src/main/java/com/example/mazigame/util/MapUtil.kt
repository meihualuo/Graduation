package com.example.mazigame.util

import com.example.mazigame.model.CubeModel
import com.example.mazigame.model.MapModel
import java.util.*

class MapUtil {
    companion object{


        fun mapListToString(list: MutableList<MapModel>): String? {
            val result = StringBuilder()
            for (i in list.indices) {
                result.append(mapToString(list[i].map))
                result.append("M")
            }
            return result.toString()
        }

        fun stringToMapList(strMap: String): MutableList<MapModel?>? {
            val list: MutableList<MapModel?> = ArrayList()
            val strList = strMap.split("M").toTypedArray()
            for (data in strList) {
                if(data.isEmpty() || data == "")
                    continue
                val map = stringToMap(data)
                val mapModel = MapModel(map)
                list.add(mapModel)
            }
            return list
        }

        fun mapToString(map: Array<IntArray>?): String? {
            val result = StringBuilder()
            if (map == null) return result.toString()
            for (datas in map) {
                for (element in datas) {
                    when (element) {
                        MapModel.ROAD -> {
                            result.append("R")
                        }
                        MapModel.COLUMN -> {
                            result.append("C")
                        }
                        MapModel.TERMINAL -> {
                            result.append("T")
                        }
                        MapModel.STAIR_IN -> {
                            result.append("I")
                        }
                        MapModel.STAIR_OUT -> {
                            result.append("O")
                        }
                        else -> {
                            result.append("X")
                        }
                    }
                }
                result.append("N")
            }
            return result.toString()
        }

        fun stringToMap(strMap: String): Array<IntArray> {
            val temp = strMap.split("N").toTypedArray()
            val map =
                Array(temp.size-1) { IntArray(temp[0].length) }
            for (i in temp.indices) {
                val charTemp = temp[i].toCharArray()
                for (j in charTemp.indices) {
                    when (charTemp[j]) {
                        'R' -> {
                            map[i][j] = MapModel.ROAD
                        }
                        'C' -> {
                            map[i][j] = MapModel.COLUMN
                        }
                        'T' -> {
                            map[i][j] = MapModel.TERMINAL
                        }
                        'I' -> {
                            map[i][j] = MapModel.STAIR_IN
                        }
                        'O' -> {
                            map[i][j] = MapModel.STAIR_OUT
                        }
                    }
                }
            }
            return map
        }

        /**
         * 递归算法求解路径，自行设计的算法，
         * 只适用于单路迷宫，如非迷宫求解路径非最短路径
         * @param map
         * @param roadList
         * @param people
         * @param nowCube
         * @return
         */
        fun promptRoad(
            map: Array<IntArray>,
            roadList: MutableList<CubeModel>,
            people: CubeModel,
            nowCube: CubeModel
        ): Boolean {
            if (people.weighe == nowCube.weighe && people.heighe == nowCube.heighe) {
                roadList.add(nowCube)
                return true
            }
            //向4个方向查找路径
            if (map[nowCube.weighe - 1][nowCube.heighe] == MapModel.ROAD) {
                map[nowCube.weighe - 1][nowCube.heighe] = MapModel.COLUMN
                val newCube = CubeModel(nowCube.weighe - 2, nowCube.heighe)
                if (promptRoad(map, roadList, people, newCube)) {
                    roadList.add(nowCube)
                    map[nowCube.weighe - 1][nowCube.heighe] = MapModel.ROAD
                    return true
                }
                map[nowCube.weighe - 1][nowCube.heighe] = MapModel.ROAD
            }
            if (map[nowCube.weighe + 1][nowCube.heighe] == MapModel.ROAD) {
                map[nowCube.weighe + 1][nowCube.heighe] = MapModel.COLUMN
                val newCube = CubeModel(nowCube.weighe + 2, nowCube.heighe)
                if (promptRoad(map, roadList, people, newCube)) {
                    roadList.add(nowCube)
                    map[nowCube.weighe + 1][nowCube.heighe] = MapModel.ROAD
                    return true
                }
                map[nowCube.weighe + 1][nowCube.heighe] = MapModel.ROAD
            }
            if (map[nowCube.weighe][nowCube.heighe - 1] == MapModel.ROAD) {
                map[nowCube.weighe][nowCube.heighe - 1] = MapModel.COLUMN
                val newCube = CubeModel(nowCube.weighe, nowCube.heighe - 2)
                if (promptRoad(map, roadList, people, newCube)) {
                    map[nowCube.weighe][nowCube.heighe - 1] = MapModel.ROAD
                    roadList.add(nowCube)
                    return true
                }
                map[nowCube.weighe][nowCube.heighe - 1] = MapModel.ROAD
            }
            if (map[nowCube.weighe][nowCube.heighe + 1] == MapModel.ROAD) {
                map[nowCube.weighe][nowCube.heighe + 1] = MapModel.COLUMN
                val newCube = CubeModel(nowCube.weighe, nowCube.heighe + 2)
                if (promptRoad(map, roadList, people, newCube)) {
                    roadList.add(nowCube)
                    map[nowCube.weighe][nowCube.heighe + 1] = MapModel.ROAD
                    return true
                }
                map[nowCube.weighe][nowCube.heighe + 1] = MapModel.ROAD
            }
            return false
        }
    }
}