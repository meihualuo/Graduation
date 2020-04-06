package com.example.mazigame.bean

import com.example.mazigame.model.CubeModel
import com.example.mazigame.model.MapModel

class GameBeam {
    var name:String? = null
    var duration:Long? = null
    var degree:Int? = null
    var type:String? = null
    var people:CubeModel? = null
    var startTime:Long? = null
    var mMapModel:MapModel? = null

    var layers:Int = 1
    var mMapModelList:MutableList<MapModel>? = null




    //需要单例模式，不需要考虑内存，但要考虑线程安全,故采用饿汉式
    companion object{
        private val instance = GameBeam()
        fun getInstance():GameBeam{
            return instance
        }
    }

    private constructor()

    fun degreeAdd(){
        degree = degree!! + 1
    }

//
//    constructor(name:String?,duration:Long?,degree:String?,type:String?,map:Array<IntArray>,people:CubeModel){
//        this.name = name
//        this.duration = duration
//        this.degree = degree
//        this.type = type
//        this.map = map
//        this.people = people
//        startTime = System.currentTimeMillis()
//    }
}