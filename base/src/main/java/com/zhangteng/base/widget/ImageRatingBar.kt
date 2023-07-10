package com.zhangteng.base.widget

import android.annotation.SuppressLint
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
class ImageRatingBar : View {
    private var isIndicator = true
    private var isSliding = false
    private var slidePosition = 0f
    private var itemWidth = 0f
    private var horizontalSpacing = 0
    private var maxRate = MAX_RATE
    private var starWidth = DEFAULT_RATE_WIDTH
    private var starHeight = DEFAULT_RATE_HEIGHT
    private var rating = NO_RATING
    private var currentRating = NO_RATING
    private var listener: IRatingListener? = null
    private lateinit var points: Array<PointF?>
    private lateinit var ratingStars: Array<VectorDrawableCompat?>
    private lateinit var defaultStar: Array<VectorDrawableCompat?>

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
            val ta = context.obtainStyledAttributes(attrs, R.styleable.ImageRatingBar, 0, 0)
            try {
                starWidth =
                    ta.getDimensionPixelSize(
                        R.styleable.ImageRatingBar_irb_starWidth,
                        DEFAULT_RATE_WIDTH
                    )
                starHeight =
                    ta.getDimensionPixelSize(
                        R.styleable.ImageRatingBar_irb_starHeight,
                        DEFAULT_RATE_HEIGHT
                    )
                horizontalSpacing =
                    ta.getDimensionPixelSize(R.styleable.ImageRatingBar_irb_horizontalSpace, 0)
                isIndicator = ta.getBoolean(R.styleable.ImageRatingBar_irb_isIndicator, true)
                maxRate = ta.getInt(R.styleable.ImageRatingBar_irb_maxRate, MAX_RATE)
                val res = ta.getResourceId(
                    R.styleable.ImageRatingBar_irb_star,
                    R.drawable.default_star_yellow
                )
                val res1 = ta.getResourceId(R.styleable.ImageRatingBar_irb_star1, res)
                val res2 = ta.getResourceId(R.styleable.ImageRatingBar_irb_star2, res)
                val res3 = ta.getResourceId(R.styleable.ImageRatingBar_irb_star3, res)
                val res4 = ta.getResourceId(R.styleable.ImageRatingBar_irb_star4, res)
                val res5 = ta.getResourceId(R.styleable.ImageRatingBar_irb_star5, res)
                val default =
                    ta.getResourceId(
                        R.styleable.ImageRatingBar_irb_starBackground,
                        R.drawable.default_star_grey
                    )
                val default1 =
                    ta.getResourceId(R.styleable.ImageRatingBar_irb_starBackground1, default)
                val default2 =
                    ta.getResourceId(R.styleable.ImageRatingBar_irb_starBackground2, default)
                val default3 =
                    ta.getResourceId(R.styleable.ImageRatingBar_irb_starBackground3, default)
                val default4 =
                    ta.getResourceId(R.styleable.ImageRatingBar_irb_starBackground4, default)
                val default5 =
                    ta.getResourceId(R.styleable.ImageRatingBar_irb_starBackground5, default)
                defaultStar = arrayOfNulls(maxRate)
                ratingStars = arrayOfNulls(maxRate)
                points = arrayOfNulls(maxRate)
                for (i in 0 until maxRate) {
                    points[i] = PointF()
                    when (i) {
                        0 -> {
                            defaultStar[i] =
                                VectorDrawableCompat.create(resources, default1, context.theme)
                            ratingStars[i] =
                                VectorDrawableCompat.create(resources, res1, context.theme)
                        }

                        1 -> {
                            defaultStar[i] =
                                VectorDrawableCompat.create(resources, default2, context.theme)
                            ratingStars[i] =
                                VectorDrawableCompat.create(resources, res2, context.theme)
                        }

                        2 -> {
                            defaultStar[i] =
                                VectorDrawableCompat.create(resources, default3, context.theme)
                            ratingStars[i] =
                                VectorDrawableCompat.create(resources, res3, context.theme)
                        }

                        3 -> {
                            defaultStar[i] =
                                VectorDrawableCompat.create(resources, default4, context.theme)
                            ratingStars[i] =
                                VectorDrawableCompat.create(resources, res4, context.theme)
                        }

                        4 -> {
                            defaultStar[i] =
                                VectorDrawableCompat.create(resources, default5, context.theme)
                            ratingStars[i] =
                                VectorDrawableCompat.create(resources, res5, context.theme)
                        }

                        else -> {
                            defaultStar[i] =
                                VectorDrawableCompat.create(resources, default, context.theme)
                            ratingStars[i] =
                                VectorDrawableCompat.create(resources, res, context.theme)
                        }
                    }
                }
                if (starWidth == 0) starWidth = defaultStar[default1]!!.intrinsicWidth
                if (starHeight == 0) starHeight = defaultStar[default1]!!.intrinsicHeight
                val rating = ta.getInt(R.styleable.ImageRatingBar_irb_rating, NO_RATING)
                setRating(rating)
            } finally {
                ta.recycle()
            }
        }
    }

    fun setOnRatingListener(listener: IRatingListener?) {
        this.listener = listener
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (isIndicator) {
            // Disable all input if the slider is disabled
            return false
        }
        when (event.action) {
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
        return Math.min(position, maxRate.toFloat())
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        itemWidth = w / maxRate.toFloat()
        updatePositions()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = starWidth * maxRate + horizontalSpacing * (maxRate - 1) +
                paddingLeft + paddingRight
        val height = starHeight + paddingTop + paddingBottom
        setMeasuredDimension(width, height)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        for (i in 0 until maxRate) {
            val pos = points[i]
            canvas.save()
            canvas.translate(pos!!.x, pos.y)
            drawStar(canvas, i)
            canvas.restore()
        }
    }

    fun getStarWidth(): Int {
        return starWidth
    }

    fun setStarWidth(starWidth: Int) {
        this.starWidth = starWidth
    }

    fun getStarHeight(): Int {
        return starHeight
    }

    fun setStarHeight(starHeight: Int) {
        this.starHeight = starHeight
    }

    fun getMaxRate(): Int {
        return maxRate
    }

    fun setMaxRate(maxRate: Int) {
        this.maxRate = maxRate
    }

    fun getHorizontalSpacing(): Int {
        return horizontalSpacing
    }

    fun setHorizontalSpacing(horizontalSpacing: Int) {
        this.horizontalSpacing = horizontalSpacing
    }

    fun isIndicator(): Boolean {
        return isIndicator
    }

    fun setIndicator(isIndicator: Boolean) {
        this.isIndicator = isIndicator
    }

    fun getRating(): Int {
        return rating
    }

    fun setRating(rating: Int) {
        if (rating < 0 || rating > maxRate) throw IndexOutOfBoundsException("Rating must be between 0 and " + maxRate)
        this.rating = rating
        slidePosition = (rating - 0.1).toFloat()
        isSliding = true
        invalidate()
        if (listener != null) listener!!.onRatingFinal(rating)
    }

    private fun drawStar(canvas: Canvas, position: Int) {
        drawStar(canvas, defaultStar[position])

        // Draw the rated star
        if (isSliding && position <= slidePosition) {
            val stars = ratingStars
            val rating = Math.ceil(slidePosition.toDouble()).toInt()
            val starIndex = rating - 1
            if (rating > 0) drawStar(canvas, stars[starIndex])
        }
    }

    private fun drawStar(canvas: Canvas, star: VectorDrawableCompat?) {
        canvas.save()
        canvas.translate((-starWidth / 2).toFloat(), (-starHeight / 2).toFloat())
        star!!.setBounds(0, 0, starWidth, starHeight)
        star.draw(canvas)
        canvas.restore()
    }

    private fun updatePositions() {
        var left = 0f
        for (i in 0 until maxRate) {
            val posY = (height / 2).toFloat()
            var posX = left + starWidth / 2
            left += starWidth.toFloat()
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
        private const val DEFAULT_RATE_HEIGHT = 120
        private const val DEFAULT_RATE_WIDTH = 120
    }

    interface IRatingListener {
        fun onRatingFinal(rating: Int)

        fun onRatingCancel()

        fun onRatingPending(rating: Int)
    }
}