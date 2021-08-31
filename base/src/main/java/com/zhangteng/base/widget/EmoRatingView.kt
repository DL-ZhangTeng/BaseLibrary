package com.zhangteng.base.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.PointF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat
import com.zhangteng.base.R

/**
 * 星型进度条（使用VectorDrawable）
 * Created by Zach on 5/27/2017.
 */
class EmoRatingView : View {
    private var isSliding = false
    private var slidePosition = 0f
    private lateinit var points: Array<PointF?>
    private var itemWidth = 0f
    private lateinit var ratingSmiles: Array<VectorDrawableCompat?>
    private lateinit var defaultSmile: Array<VectorDrawableCompat?>
    private var listener: IEmoRatingListener? = null
    private var currentRating = NO_RATING
    private var smileWidth = 0
    private var smileHeight = 0
    private var horizontalSpacing = 0
    private var isEnabled = false
    private var rating = NO_RATING

    constructor(context: Context?) : super(context) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        init(attrs)
    }

    private fun init(attrs: AttributeSet? = null) {
        isSliding = false
        if (attrs != null) {
            val ta = context.obtainStyledAttributes(attrs, R.styleable.emoRate, 0, 0)
            try {
                smileWidth =
                    ta.getDimensionPixelSize(R.styleable.emoRate_smileWidth, DEFAULT_RATE_WIDTH)
                smileHeight =
                    ta.getDimensionPixelSize(R.styleable.emoRate_smileHeight, DEFAULT_RATE_HIGHT)
                horizontalSpacing = ta.getDimensionPixelSize(R.styleable.emoRate_horizontalSpace, 0)
                isEnabled = ta.getBoolean(R.styleable.emoRate_enabled, true)
                rating = ta.getInt(R.styleable.emoRate_rating, NO_RATING)
                val default1 =
                    ta.getResourceId(R.styleable.emoRate_Default1, R.drawable.default_rate1)
                val default2 =
                    ta.getResourceId(R.styleable.emoRate_Default2, R.drawable.default_rate2)
                val default3 =
                    ta.getResourceId(R.styleable.emoRate_Default3, R.drawable.default_rate3)
                val default4 =
                    ta.getResourceId(R.styleable.emoRate_Default4, R.drawable.default_rate4)
                val default5 =
                    ta.getResourceId(R.styleable.emoRate_Default5, R.drawable.default_rate5)
                val res1 = ta.getResourceId(R.styleable.emoRate_Rate1, R.drawable.rate1)
                val res2 = ta.getResourceId(R.styleable.emoRate_Rate2, R.drawable.rate2)
                val res3 = ta.getResourceId(R.styleable.emoRate_Rate3, R.drawable.rate3)
                val res4 = ta.getResourceId(R.styleable.emoRate_Rate4, R.drawable.rate4)
                val res5 = ta.getResourceId(R.styleable.emoRate_Rate5, R.drawable.rate5)
                defaultSmile = arrayOf(
                    VectorDrawableCompat.create(resources, default1, context.theme),
                    VectorDrawableCompat.create(resources, default2, context.theme),
                    VectorDrawableCompat.create(resources, default3, context.theme),
                    VectorDrawableCompat.create(resources, default4, context.theme),
                    VectorDrawableCompat.create(resources, default5, context.theme)
                )
                ratingSmiles = arrayOf(
                    VectorDrawableCompat.create(resources, res1, context.theme),
                    VectorDrawableCompat.create(resources, res2, context.theme),
                    VectorDrawableCompat.create(resources, res3, context.theme),
                    VectorDrawableCompat.create(resources, res4, context.theme),
                    VectorDrawableCompat.create(resources, res5, context.theme)
                )
                if (smileWidth == 0) smileWidth = defaultSmile[default1]!!.intrinsicWidth
                if (smileHeight == 0) smileHeight = defaultSmile[default1]!!.intrinsicHeight
            } finally {
                ta.recycle()
            }
        }
        points = arrayOfNulls(MAX_RATE)
        for (i in 0 until MAX_RATE) {
            points[i] = PointF()
        }
        if (rating != NO_RATING) setRating(rating)
    }

    override fun isEnabled(): Boolean {
        return isEnabled
    }

    override fun setEnabled(enabled: Boolean) {
        isEnabled = enabled
        super.setEnabled(enabled)
    }

    fun setOnRatingSliderChangeListener(listener: IEmoRatingListener?) {
        this.listener = listener
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (!isEnabled()) {
            // Disable all input if the slider is disabled
            return false
        }
        val action = event.action
        when (action) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> {
                isSliding = true
                slidePosition = getRelativePosition(event.x)
                rating = Math.ceil(slidePosition.toDouble()).toInt()
                if (listener != null && rating != currentRating) {
                    currentRating = rating
                    listener!!.onRatingPending(rating)
                }
            }
            MotionEvent.ACTION_UP -> {
                currentRating = NO_RATING
                if (listener != null) listener!!.onRatingFinal(
                    Math.ceil(slidePosition.toDouble()).toInt()
                )
                rating = Math.ceil(slidePosition.toDouble()).toInt()
            }
            MotionEvent.ACTION_CANCEL -> {
                currentRating = NO_RATING
                if (listener != null) listener!!.onRatingCancel()
                isSliding = false
            }
            else -> {
            }
        }
        invalidate()
        return true
    }

    private fun getRelativePosition(x: Float): Float {
        var position = x / itemWidth
        position = Math.max(position, 0f)
        return Math.min(position, MAX_RATE.toFloat())
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        itemWidth = w / MAX_RATE.toFloat()
        updatePositions()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = smileWidth * MAX_RATE + horizontalSpacing * (MAX_RATE - 1) +
                paddingLeft + paddingRight
        val height = smileHeight + paddingTop + paddingBottom
        setMeasuredDimension(width, height)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        for (i in 0 until MAX_RATE) {
            val pos = points[i]
            canvas.save()
            canvas.translate(pos!!.x, pos.y)
            drawSmile(canvas, i)
            canvas.restore()
        }
    }

    fun getRating(): Int {
        return rating
    }

    fun setRating(rating: Int) {
        if (rating < 0 || rating > MAX_RATE) throw IndexOutOfBoundsException("Rating must be between 0 and " + MAX_RATE)
        this.rating = rating
        slidePosition = (rating - 0.1).toFloat()
        isSliding = true
        invalidate()
        if (listener != null) listener!!.onRatingFinal(rating)
    }

    private fun drawSmile(canvas: Canvas, position: Int) {
        drawSmile(canvas, defaultSmile[position])

        // Draw the rated smile
        if (isSliding && position <= slidePosition) {
            val smiles = ratingSmiles
            val rating = Math.ceil(slidePosition.toDouble()).toInt()
            val smileIndex = rating - 1
            if (rating > 0) drawSmile(canvas, smiles[smileIndex])
        }
    }

    private fun drawSmile(canvas: Canvas, smile: VectorDrawableCompat?) {
        canvas.save()
        canvas.translate((-smileWidth / 2).toFloat(), (-smileHeight / 2).toFloat())
        smile!!.setBounds(0, 0, smileWidth, smileHeight)
        smile.draw(canvas)
        canvas.restore()
    }

    private fun updatePositions() {
        var left = 0f
        for (i in 0 until MAX_RATE) {
            val posY = (height / 2).toFloat()
            var posX = left + smileWidth / 2
            left += smileWidth.toFloat()
            if (i > 0) {
                posX += horizontalSpacing.toFloat()
                left += horizontalSpacing.toFloat()
            } else {
                posX += paddingLeft.toFloat()
                left += paddingLeft.toFloat()
            }
            points[i]!![posX] = posY
        }
    }

    companion object {
        private const val NO_RATING = 0
        private const val MAX_RATE = 5
        private const val DEFAULT_RATE_HIGHT = 120
        private const val DEFAULT_RATE_WIDTH = 120
    }
}