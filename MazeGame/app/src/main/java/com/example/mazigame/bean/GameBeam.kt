package com.example.mazigame.bean

import com.example.mazigame.model.CubeModel

class GameBeam {
    var name:String? = null
    var duration:Long? = null
    var degree:String? = null
    var type:String? = null
    var map:Array<IntArray>? = null
    var people:CubeModel? = null
    var startTime:Long? = null

    constructor(name:String?,duration:Long?,degree:String?,type:String?,map:Array<IntArray>,people:CubeModel){
        this.name = name
        this.duration = duration
        this.degree = degree
        this.type = type
        this.map = map
        this.people = people
        startTime = System.currentTimeMillis()
    }
}