package com.example.mazigame.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.core.view.marginLeft
import com.example.mazigame.R
import com.example.mazigame.model.CubeModel
import com.example.mazigame.model.MapModel

class MazeView : View {

    val TAG = "MazeView"
    private var wallPaint = Paint()
    private var columnPaint = Paint()
    private var peoplePaint = Paint()
    var mapModel:MapModel
    var wallSide:Int?=null
    var map:Array<IntArray>
    var people:CubeModel
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
        mapModel = MapModel(10,16)
        map = mapModel.map
        people = CubeModel(1,1)
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

        var bitmap = BitmapFactory.decodeResource(mContext.resources,R.drawable.image_1)
//        canvas?.drawBitmap(bitmap,src,dst,Paint())

        var matrix = Matrix()
        var srcf = RectF(100f,100f,400f,400f)
        var dstf = RectF(0f,100f,200f,200f)
        matrix.setRectToRect(srcf,dstf,Matrix.ScaleToFit.CENTER)
        //计算每个地图格子的边长
        canvas?.drawBitmap(bitmap,matrix,Paint())
        var wallWidth = width/ mapModel.wide
        var wallHeight = height/mapModel.high

        //选取较小者作为格子边长
        wallSide = if(wallHeight>wallWidth ) wallWidth else wallHeight

        //计算左偏移量
        var marginLeft = (width- wallSide!!*mapModel.wide)/2

        //遍历数组，绘制地图
        canvas?.save()
        canvas?.translate(marginLeft.toFloat(), 0F)
        for (i in 1..mapModel.wide){
            for(j in 1..mapModel.high){
                if(map[i-1][j-1] == 1){
                    canvas?.drawRect(Rect(wallSide!!*(i-1),wallSide!!*(j-1),wallSide!!*(i),wallSide!!*(j)),wallPaint)
                }else if(map[i-1][j-1] == 2){
                    canvas?.drawRect(Rect(wallSide!!*(i-1),wallSide!!*(j-1),wallSide!!*(i),wallSide!!*(j)),columnPaint)
                }
            }
        }
        //绘制人物
        canvas?.drawRect(Rect(wallSide!!*(people.weighe),wallSide!!*(people.heighe),wallSide!!*(people.weighe+1),wallSide!!*(people.heighe+1)),peoplePaint)
        canvas?.restore()

    }

    fun moveOfTop(){
        if(people.heighe>1 && map[people.weighe][people.heighe-1] == 0){
            people.heighe -= 1
            invalidate()
        }
    }

    fun moveOfBottom(){
        if(people.heighe<mapModel.high-2 && map[people.weighe][people.heighe+1] == 0){
            people.heighe += 1
            invalidate()
        }
    }

    fun moveOfLeft(){
        if(people.weighe>1 && map[people.weighe -1][people.heighe] == 0){
            people.weighe -= 1
            invalidate()
        }
    }

    fun moveOfRight(){
        if(people.weighe<mapModel.wide-2 && map[people.weighe+1][people.heighe] == 0){
            people.weighe += 1
            invalidate()
        }
    }
}