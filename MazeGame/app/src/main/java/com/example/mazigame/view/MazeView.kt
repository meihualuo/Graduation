package com.example.mazigame.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.hardware.biometrics.BiometricPrompt
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.core.graphics.toRectF
import com.example.mazigame.MyApplication
import com.example.mazigame.model.CubeModel
import com.example.mazigame.model.MapModel
import com.example.mazigame.util.DensityUtil
import kotlin.math.log

class MazeView : View {

    val TAG = "MazeView"
    private var wallPaint = Paint()
    private var columnPaint = Paint()
    private var peoplePaint = Paint()
    private lateinit var mapModel:MapModel
    private var map:Array<IntArray>? = null
    private lateinit var people:CubeModel
    val wallSide = DensityUtil.dip2px(MyApplication.getApplication(),6f)
    var showPromptRoad = false
    var roadList:List<CubeModel> ?= null
    var mContext:Context

    var touchPaddingHor = 0f
    var touchPaddingVer = 0f

    var paddingLeft = 0f
    var paddingTop = 0f

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
    }

    fun setPrompt(prompt:Boolean,list:List<CubeModel>){
        this.showPromptRoad = prompt
        this.roadList = list
        this.invalidate()
    }

    fun updateGame(mapModel: MapModel,people: CubeModel){
        this.mapModel = mapModel
        this.map = mapModel.map
        this.people = people
        this.showPromptRoad = false
        this.invalidate()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        var widthSize = MeasureSpec.getSize(widthMeasureSpec)
        var widthMode = MeasureSpec.getMode(widthMeasureSpec)
        var heightSize = MeasureSpec.getSize(heightMeasureSpec)
        var heightMode = MeasureSpec.getMode(heightMeasureSpec)

        if (widthMode == MeasureSpec.AT_MOST && heightMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(DensityUtil.dip2px(mContext,300f), DensityUtil.dip2px(mContext,300f))
        } else if (widthMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(DensityUtil.dip2px(mContext,300f), heightSize)
        } else if (heightMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(widthSize, widthSize*9/7.toInt())
        }
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (map == null)
            return
        val rect= { x:Int, y:Int, wallSide:Int ->
            var hor = (x%2 )*(wallSide)/2+wallSide/4+1
            var ver = (y%2 )*(wallSide)/2+wallSide/4+1
            Rect(wallSide*x-hor,wallSide*y-ver,wallSide*x+hor,wallSide*y+ver)

        }
        val peopleRect= { x:Int, y:Int, wallSide:Int ->
            val hor = (x%2 )*(wallSide)/2
            val ver = (y%2 )*(wallSide)/2
            Rect(wallSide*x-hor,
                wallSide*y-ver,
                wallSide*x+hor,
                wallSide*y+ver
            )

        }

        val marginLeft = wallSide/2
        val marginTop = wallSide/2

        //遍历数组，绘制地图
        canvas?.save()
        canvas?.translate(marginLeft.toFloat()+paddingLeft, marginTop.toFloat()+paddingTop)
        for (i in 1..mapModel.wide){
            for(j in 1..mapModel.wide){
                if(map!![i-1][j-1] == MapModel.WAIT){
                    canvas?.drawRect(rect(i-1,j-1,wallSide!!),wallPaint)
                }else if(map!![i-1][j-1] == MapModel.COLUMN){
                    canvas?.drawRect(rect(i-1,j-1,wallSide!!),columnPaint)
                }
            }
        }
        //绘制人物
//        canvas?.drawRect(rect(people.weighe,people.heighe,wallSide!!),peoplePaint)
        canvas?.drawArc(peopleRect(people.weighe,people.heighe,wallSide!!).toRectF(),0f,360f,true,peoplePaint)
        var rodeRect = {x:Int,y:Int,w:Int ->
            Rect(x*w-4,y*w-4,x*w+4,y*w+4)
        }
        //绘制提示路线
        if(showPromptRoad && roadList != null){
            roadList?.forEach {
                canvas?.drawRect(rodeRect(it.weighe,it.heighe,wallSide),peoplePaint)
            }
        }
        canvas?.restore()
    }

    var downX = 0f
    var downY = 0f
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when(event?.action){
            MotionEvent.ACTION_DOWN -> {
                downX = event.x
                downY = event.y
            }
            MotionEvent.ACTION_MOVE -> {
                val moveX = event.getX()
                val moveY = event.getY()
                updateTouchPadding(moveX-downX,moveY-downY)
            }
            MotionEvent.ACTION_UP -> {
                updatePadding()
            }
        }
        return true
    }

    fun updateTouchPadding(x:Float,y:Float){
        this.paddingLeft = this.touchPaddingHor + x
        this.paddingTop = this.touchPaddingVer + y
        this.invalidate()
    }

    fun updatePadding(){
        this.touchPaddingHor  = if(this.paddingLeft < 0) this.paddingLeft else 0f
        this.touchPaddingVer = if(this.paddingTop < 0) this.paddingTop else 0f
        this.paddingLeft = this.touchPaddingHor
        this.paddingTop = this.touchPaddingVer
        this.invalidate()
    }
}