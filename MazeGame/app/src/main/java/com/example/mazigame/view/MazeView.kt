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

    private var mPaint = Paint()
    var map:kotlin.Array<kotlin.IntArray?>?

    constructor(ctx: Context) : super(ctx)

    constructor(ctx: Context, attrs: AttributeSet) : super(ctx, attrs)

    constructor(ctx: Context, attrs: AttributeSet, defStyleAttr: Int) : super(ctx, attrs, defStyleAttr)

    init {
        mPaint.color = Color.BLUE
        var mapModel = MapModel(20,20)
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
        canvas?.drawRect(Rect(paddingLeft,paddingTop,width/2,height/2),mPaint)

    }
}