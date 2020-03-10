package com.example.mazigame.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import com.example.mazigame.model.MapModel

class MazeView : View {

    private var wallPaint = Paint()
    private var columnPaint = Paint()
    var map:kotlin.Array<kotlin.IntArray?>?

    constructor(ctx: Context) : super(ctx)

    constructor(ctx: Context, attrs: AttributeSet) : super(ctx, attrs)

    constructor(ctx: Context, attrs: AttributeSet, defStyleAttr: Int) : super(ctx, attrs, defStyleAttr)

    init {
        wallPaint.color = Color.BLUE
        columnPaint.color = Color.BLACK
        var mapModel = MapModel(81,81)
        map = mapModel.map
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
        //获取绘制的View的宽度
//        var width = width -paddingLeft-paddingRight
//        //获取绘制的View的高度
//        var height = height -paddingTop-paddingBottom
        //绘制View，左上角坐标（0+paddingLeft,0+paddingTop），右下角坐标（width+paddingLeft,height+paddingTop）
//        canvas?.drawRect((0+paddingLeft).toFloat(), (0+paddingTop).toFloat(), (100+paddingLeft).toFloat(), (100+paddingTop).toFloat(),mPaint)
//        canvas?.drawRect(Rect(paddingLeft,paddingTop,width/2,height/2),mPaint)

        //获取map的尺寸
        var widthSize = map?.size
        var heightSize = map?.get(0)?.size

        //计算每个地图格子的边长
        var wallWidth = width/ widthSize!!
        var wallHeight = height/heightSize!!

        //选取较小者作为格子边长
        var wallSide = if(wallHeight>wallWidth ) wallWidth else wallHeight

        //遍历数组，绘制地图
        for (i in 1..widthSize){
            for(j in 1..heightSize){
                if(map?.get(i-1)?.get(j-1) == 1){
                    canvas?.drawRect(Rect(wallSide*(i-1),wallSide*(j-1),wallSide*(i),wallSide*(j)),wallPaint)
                }else if(map?.get(i-1)?.get(j-1) == 2){
                    canvas?.drawRect(Rect(wallSide*(i-1),wallSide*(j-1),wallSide*(i),wallSide*(j)),columnPaint)
                }
            }
        }
    }
}