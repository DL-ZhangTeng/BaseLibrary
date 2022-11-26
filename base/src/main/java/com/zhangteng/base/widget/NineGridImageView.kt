package com.zhangteng.base.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.text.TextPaint
import android.util.AttributeSet
import android.util.TypedValue
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.view.ViewCompat
/**
 * @description: 九宫格ImageView
 * @author: Swing
 * @date: 2021/9/5
 */
class NineGridImageView @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatImageView(
    context!!, attrs, defStyleAttr
) {
    private var moreNum = 0 //显示更多的数量
    private var maskColor = -0x78000000 //默认的遮盖颜色
    private var textSize = 35f //显示文字的大小单位sp
    private var textColor = -0x1 //显示文字的颜色
    private val textPaint //文字的画笔
            : TextPaint
    private var msg = "" //要绘制的文字
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (moreNum > 0) {
            canvas.drawColor(maskColor)
            val baseY = height / 2 - (textPaint.ascent() + textPaint.descent()) / 2
            canvas.drawText(msg, (width / 2).toFloat(), baseY, textPaint)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                val drawable = drawable
                if (drawable != null) {
                    /**
                     * 默认情况下，所有的从同一资源（R.drawable.XXX）加载来的drawable实例都共享一个共用的状态，
                     * 如果你更改一个实例的状态，其他所有的实例都会收到相同的通知。
                     * 使用使 mutate 可以让这个drawable变得状态不定。这个操作不能还原（变为不定后就不能变为原来的状态）。
                     * 一个状态不定的drawable可以保证它不与其他任何一个drawabe共享它的状态。
                     * 此处应该是要使用的 mutate()，但是在部分手机上会出现点击后变白的现象，所以没有使用
                     * 目前这种解决方案没有问题
                     */
//                    drawable.mutate().setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
                    drawable.setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY)
                    ViewCompat.postInvalidateOnAnimation(this)
                }
            }
            MotionEvent.ACTION_MOVE -> {
            }
            MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> {
                val drawableUp = drawable
                if (drawableUp != null) {
//                    drawableUp.mutate().clearColorFilter();
                    drawableUp.clearColorFilter()
                    ViewCompat.postInvalidateOnAnimation(this)
                }
            }
        }
        return super.onTouchEvent(event)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        setImageDrawable(null)
    }

    fun getMoreNum(): Int {
        return moreNum
    }

    fun setMoreNum(moreNum: Int) {
        this.moreNum = moreNum
        msg = "+$moreNum"
        invalidate()
    }

    fun getMaskColor(): Int {
        return maskColor
    }

    fun setMaskColor(maskColor: Int) {
        this.maskColor = maskColor
        invalidate()
    }

    fun getTextSize(): Float {
        return textSize
    }

    fun setTextSize(textSize: Float) {
        this.textSize = textSize
        textPaint.textSize = textSize
        invalidate()
    }

    fun getTextColor(): Int {
        return textColor
    }

    fun setTextColor(textColor: Int) {
        this.textColor = textColor
        textPaint.color = textColor
        invalidate()
    }

    init {

        //转化单位
        textSize = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP,
            textSize,
            getContext().resources.displayMetrics
        )
        textPaint = TextPaint()
        textPaint.textAlign = Paint.Align.CENTER //文字居中对齐
        textPaint.isAntiAlias = true //抗锯齿
        textPaint.textSize = textSize //设置文字大小
        textPaint.color = textColor //设置文字颜色
    }
}