package com.example.mazigame.view

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import androidx.core.graphics.toRectF
import com.example.mazigame.R
import com.example.mazigame.model.CubeModel
import com.example.mazigame.model.MapModel
import com.example.mazigame.util.DensityUtil

class GameMainView(ctx: Context, attrs: AttributeSet) :View(ctx,attrs){

    val TAG = "GameMainView"
    private var columnPaint = Paint()
    private var peoplePaint = Paint()
    private var stairPaint = Paint()
    private lateinit var mapModel: MapModel
    private var map:Array<IntArray>? = null
    private lateinit var people: CubeModel
    var showPromptRoad = false
    var roadList:List<CubeModel> ?= null
    var mContext: Context = ctx
    val wallSide:Int
    private var peoHorPad = 0f
    private var peoVerPad = 0f

    init {
        columnPaint.color = mContext.getColor(R.color.column_bk)
        stairPaint.color = mContext.getColor(R.color.Loading_color)
        peoplePaint.color = Color.RED
        wallSide = DensityUtil.dip2px(mContext,20f)
    }

    fun setPrompt(prompt:Boolean,list:List<CubeModel>){
        this.showPromptRoad = prompt
        this.roadList = list
        this.invalidate()
    }

    fun updateGame(mapModel: MapModel, people: CubeModel){
        this.mapModel = mapModel
        this.map = mapModel.map
        this.people = people
        this.showPromptRoad = false
        this.invalidate()
    }

    fun setPeoHorPad(peoHorPad:Float){
        this.peoHorPad = peoHorPad
        invalidate()
    }
    fun getPeoHorPad():Float{
        return this.peoHorPad
    }
    fun setPeoVerPad(peoVerPad:Float){
        this.peoVerPad = peoVerPad
        invalidate()
    }
    fun getPeoVerPad():Float{
        return this.peoVerPad
    }

    fun getPeople():CubeModel{
        return people
    }

    fun setPeople(people: CubeModel,direction: Int){
        var horPadding = 0f
        var verPadding = 0f
        when(direction){
            MapModel.MOVE_OF_LEFT ->
                horPadding = wallSide*2f
            MapModel.MOVE_OF_RIGHT ->
                horPadding = -wallSide*2f
            MapModel.MOVE_OF_TOP ->
                verPadding = wallSide*2f
            MapModel.MOVE_OF_BOTTOM ->
                verPadding = -wallSide*2f
        }
        this.people = people
        var animator = ObjectAnimator.ofFloat(this,"peoHorPad",horPadding,0f)
        animator.duration = 200
        animator.start()
        var animatorVer = ObjectAnimator.ofFloat(this,"peoVerPad",verPadding,0f)
        animatorVer.duration = 200
        animatorVer.start()
    }



    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        var widthSize = View.MeasureSpec.getSize(widthMeasureSpec)
        var widthMode = View.MeasureSpec.getMode(widthMeasureSpec)
        var heightSize = View.MeasureSpec.getSize(heightMeasureSpec)
        var heightMode = View.MeasureSpec.getMode(heightMeasureSpec)

        if (widthMode == View.MeasureSpec.AT_MOST && heightMode == View.MeasureSpec.AT_MOST) {
            setMeasuredDimension(300, 300)
        } else if (widthMode == View.MeasureSpec.AT_MOST) {
            setMeasuredDimension(300, heightSize)
        } else if (heightMode == View.MeasureSpec.AT_MOST) {
            setMeasuredDimension(widthSize, 300)
        }
    }

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
            var hor = (x%2 )*(wallSide)/2
            var ver = (y%2 )*(wallSide)/2
            Rect(wallSide*x-hor+peoHorPad.toInt(),
                wallSide*y-ver+peoVerPad.toInt(),
                wallSide*x+hor+peoHorPad.toInt(),
                wallSide*y+ver+peoVerPad.toInt()
            )

        }
        val horSize = width/wallSide/2+3
        val verSize = height/wallSide/2+3

        //计算偏移量
        val marginLeft = -(people.weighe.toFloat()*wallSide-width/2f)-peoHorPad
        val marginTop = -(people.heighe.toFloat()*wallSide-height/2f)-peoVerPad

        val leftPos = if (people.weighe-horSize > 0) people.weighe-horSize else 0
        val topPos = if (people.heighe-verSize > 0) people.heighe-verSize else 0
        val rightPos = if (people.weighe+horSize < mapModel.wide) people.weighe+horSize else mapModel.wide-1
        val bottomPos = if (people.heighe+verSize < mapModel.wide) people.heighe+verSize else mapModel.wide-1

        //遍历数组，绘制地图
        canvas?.save()
        canvas?.translate(marginLeft, marginTop)
        for (i in leftPos..rightPos){
            for(j in topPos..bottomPos){
                when(map!![i][j]){
                    MapModel.COLUMN ->
                        canvas?.drawRect(rect(i,j,wallSide!!),columnPaint)
                    MapModel.STAIR_OUT,MapModel.STAIR_IN ->
                        canvas?.drawRect(rect(i,j,wallSide!!),stairPaint)
                }
            }
        }
        //绘制人物
//        canvas?.drawRect(peopleRect(people.weighe,people.heighe,wallSide!!),peoplePaint)
        canvas?.drawArc(peopleRect(people.weighe,people.heighe,wallSide!!).toRectF(),0f,360f,true,peoplePaint)
        var rodeRect = {x:Int,y:Int,w:Int ->
            Rect(x*w-8,y*w-8,x*w+8,y*w+8)
        }
        //绘制提示路线
        if(showPromptRoad && roadList != null){
            roadList?.forEach {
                canvas?.drawRect(rodeRect(it.weighe,it.heighe,wallSide),peoplePaint)
            }
        }
        canvas?.restore()
    }
}