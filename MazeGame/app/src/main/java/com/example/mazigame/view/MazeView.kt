package com.example.mazigame.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import com.example.mazigame.model.CubeModel
import com.example.mazigame.model.MapModel

class MazeView : View {

    val TAG = "MazeView"
    companion object{
        const val MOVE_LEFT = 1
        const val MOVE_RIGHT = 2
        const val MOVE_TOP = 3
        const val MOVE_BOTTOM = 4
    }
    private var wallPaint = Paint()
    private var columnPaint = Paint()
    private var peoplePaint = Paint()
    private lateinit var mapModel:MapModel
    private lateinit var map:Array<IntArray>
    private lateinit var people:CubeModel
    var mContext:Context

    constructor(ctx: Context) : super(ctx){
        mContext = ctx
    }

    constructor(ctx: Context, attrs: AttributeSet) : super(ctx, attrs){
        mContext = ctx
    }

    constructor(ctx: Context, attrs: AttributeSet, defStyleAttr: Int) : super(ctx, attrs, defStyleAttr){
        mContext = ctx
    }

    init {
        wallPaint.color = Color.BLUE
        columnPaint.color = Color.BLACK
        peoplePaint.color = Color.RED
        updateGame(20,30)
    }

    private fun updateGame(wide:Int, high:Int){
        this.mapModel = MapModel(wide,high)
        this.map = mapModel.map
        this.people = CubeModel(1,1)
    }

    fun setPeople(people:CubeModel){
        this.people = people
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        var widthSize = MeasureSpec.getSize(widthMeasureSpec)
        var widthMode = MeasureSpec.getMode(widthMeasureSpec)
        var heightSize = MeasureSpec.getSize(heightMeasureSpec)
        var heightMode = MeasureSpec.getMode(heightMeasureSpec)

        if (widthMode == MeasureSpec.AT_MOST && heightMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(300, 300)
        } else if (widthMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(300, heightSize)
        } else if (heightMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(widthSize, 300)
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        var rect= { x:Int, y:Int, wallSide:Int ->
            Rect(wallSide*x,wallSide*y,wallSide*(x+1),wallSide*(y+1))
        }
        var wallWidth = width/ mapModel.wide
        var wallHeight = height/mapModel.high
        //选取较小者作为格子边长
        var wallSide = if(wallHeight>wallWidth ) wallWidth else wallHeight

        //计算左偏移量
        var marginLeft = (width- wallSide!!*mapModel.wide)/2

        //遍历数组，绘制地图
        canvas?.save()
        canvas?.translate(marginLeft.toFloat(), 0F)
        for (i in 1..mapModel.wide){
            for(j in 1..mapModel.high){
                if(map[i-1][j-1] == MapModel.WAIT){
                    canvas?.drawRect(rect(i-1,j-1,wallSide!!),wallPaint)
                }else if(map[i-1][j-1] == MapModel.COLUMN){
                    canvas?.drawRect(rect(i-1,j-1,wallSide!!),columnPaint)
                }
            }
        }
        //绘制人物
        canvas?.drawRect(rect(people.weighe,people.heighe,wallSide!!),peoplePaint)
        canvas?.restore()
    }

    fun movePeople(direction: Int){
        when(direction){
            MOVE_LEFT ->
                if(people.weighe>1 && map[people.weighe -1][people.heighe] == MapModel.ROAD) people.weighe -= 2
            MOVE_RIGHT ->
                if(people.weighe<mapModel.wide-2 && map[people.weighe+1][people.heighe] == MapModel.ROAD) people.weighe += 2
            MOVE_TOP ->
                if(people.heighe>1 && map[people.weighe][people.heighe-1] == MapModel.ROAD) people.heighe -= 2
            MOVE_BOTTOM ->
                if(people.heighe<mapModel.high-2 && map[people.weighe][people.heighe+1] == MapModel.ROAD) people.heighe += 2
        }
        //过关判断
        if(people.weighe == mapModel.wide-2 && people.heighe == mapModel.high-2)
            updateGame(20,20)
        this.invalidate()
    }
}