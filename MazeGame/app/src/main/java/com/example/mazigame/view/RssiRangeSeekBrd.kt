package com.example.mazigame.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.TypedValue
import android.view.MotionEvent
import android.widget.SeekBar
import androidx.appcompat.widget.AppCompatSeekBar
import com.example.mazigame.R
import kotlin.math.abs


class RssiRangeSeekBrd: AppCompatSeekBar{

    private val mThumbWidth = dp2px(35f)
    // 进度指示器宽度
    private val mIndicatorWidth = dp2px(35f)

    private val padding = dp2px(15f)
    // 进度监听
    private var mIndicatorSeekBarChangeListener: OnIndicatorSeekBarChangeListener? = null

    companion object{
        interface RssiRangeListener{
            fun updateProgress(progress:Int)
        }
    }

    private var rssiRangeListener:RssiRangeListener? = null

    fun setRssiRangeListener(rssiRangeListener:RssiRangeListener){
        this.rssiRangeListener = rssiRangeListener
    }

    constructor(context: Context?):super(context, null)

    constructor(context: Context?, attrs: AttributeSet?):super(context, attrs, R.attr.seekBarStyle)

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)


    init{
        // 设置滑动监听
        this.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(
                seekBar: SeekBar,
                progress: Int,
                fromUser: Boolean
            ) { // NO OP
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                if (mIndicatorSeekBarChangeListener != null) {
//                    mIndicatorSeekBarChangeListener!!.onStartTrackingTouch(seekBar)
                }
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                if (mIndicatorSeekBarChangeListener != null) {
//                    mIndicatorSeekBarChangeListener!!.onStopTrackingTouch(seekBar)
                }
            }
        })
    }




    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
//        val progressText = "$progress%"
        // 进度百分比
        val progressRatio = progress.toFloat() / max

        //有三个地方需要绘制刻度线 25、50、75
        var scas = 25..75 step 25
        var scaPaint = Paint()
        scaPaint.setColor(Color.parseColor("#BDBDBD"))
        scaPaint.strokeWidth = 4f
        scas.forEach(){
            if(abs(progress-it) > 4)//当游标到刻度位置时刻度应该影藏
                canvas?.drawLine((width-padding*2)*it/100f+padding,height/2-10f,(width-padding*2)*it/100f+padding,height/2+10f,scaPaint)
        }

        if (mIndicatorSeekBarChangeListener != null) {
            val indicatorOffset =
                width * progressRatio - (mIndicatorWidth - mThumbWidth) / 2 - mThumbWidth * progressRatio
            mIndicatorSeekBarChangeListener!!.onProgressChanged(this, progress, indicatorOffset)
        }
    }

    @SuppressLint("ObjectAnimatorBinding")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when(event?.actionMasked){
            MotionEvent.ACTION_DOWN -> {

                //在按下时判断按下位置，如果在5个节点上则将值设置到节点
                var scas = 0..100 step 25
                var x = event.x
                var mOneWeigh = (width-padding*2)/100f
                var isReturn = false
                scas.forEach(){
                    if(abs((x-it*mOneWeigh-padding)/mOneWeigh)<3 && it!=progress && abs((x-progress*mOneWeigh-padding)/mOneWeigh)>3){
//                        val animator: ObjectAnimator = ObjectAnimator.ofFloat(this, "progress", 0f, 65f)
//                        animator.start()
//                        progress = it
                        rssiRangeListener?.updateProgress(it)
                        isReturn = true
                    }
                }
                //如果不在节点和游标上则拦截事件
                if(abs((x-progress*mOneWeigh-padding)/mOneWeigh)>6 || isReturn) return false
            }
        }
        return super.onTouchEvent(event)
    }

    /**
     * 实现动画效果需要此get、set方法
     */
    fun setProgressFloat(progress: Float) {
        super.setProgress(progress.toInt())
        invalidate()
    }

    fun getProgressFloat(): Float {
        return progress.toFloat()
    }
    /**
     * 设置进度监听
     *
     * @param listener OnIndicatorSeekBarChangeListener
     */
    fun setOnSeekBarChangeListener(listener: OnIndicatorSeekBarChangeListener?) {
        mIndicatorSeekBarChangeListener = listener
    }

    /**
     * 进度监听
     */
    interface OnIndicatorSeekBarChangeListener {
        /**
         * 进度监听回调
         *
         * @param seekBar         SeekBar
         * @param progress        进度
         * @param indicatorOffset 指示器偏移量
         */
        fun onProgressChanged(
            seekBar: SeekBar?,
            progress: Int,
            indicatorOffset: Float
        )

//        /**
//         * 开始拖动
//         *
//         * @param seekBar SeekBar
//         */
//        fun onStartTrackingTouch(seekBar: SeekBar?)
//
//        /**
//         * 停止拖动
//         *
//         * @param seekBar SeekBar
//         */
//        fun onStopTrackingTouch(seekBar: SeekBar?)
    }

    /**
     * dp转px
     *
     * @param dp dp值
     * @return px值
     */
    fun dp2px(dp: Float): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, dp,
            resources.displayMetrics
        ).toInt()
    }
}