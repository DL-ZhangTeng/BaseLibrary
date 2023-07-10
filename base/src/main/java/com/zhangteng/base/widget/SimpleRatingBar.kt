package com.zhangteng.base.widget

import android.animation.Animator
import android.animation.ValueAnimator
import android.animation.ValueAnimator.AnimatorUpdateListener
import android.annotation.TargetApi
import android.content.Context
import android.graphics.*
import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import android.view.animation.BounceInterpolator
import android.view.animation.Interpolator
import androidx.annotation.ColorInt
import androidx.annotation.Dimension
import com.zhangteng.base.R

/**
 * A simple RatingBar for Android.(不支持自定义星型样式需要自定义可使用ImageRatingBar)
 */
class SimpleRatingBar : View {
    /**
     * Represents gravity of the fill in the bar.
     */
    enum class Gravity(var id: Int) {
        /**
         * Left gravity is default: the bar will be filled starting from left to right.
         */
        Left(0),

        /**
         * Right gravity: the bar will be filled starting from right to left.
         */
        Right(1);

        companion object {
            fun fromId(id: Int): Gravity {
                for (f in values()) {
                    if (f.id == id) return f
                }
                // default value
                Log.w(
                    "SimpleRatingBar",
                    String.format("Gravity chosen is neither 'left' nor 'right', I will set it to Left")
                )
                return Left
            }
        }
    }

    // Configurable variables
    @ColorInt
    private var borderColor = 0

    @ColorInt
    private var fillColor = 0

    @ColorInt
    private var srbBackgroundColor = 0

    @ColorInt
    private var starBackgroundColor = 0

    @ColorInt
    private var pressedBorderColor = 0

    @ColorInt
    private var pressedFillColor = 0

    @ColorInt
    private var pressedBackgroundColor = 0

    @ColorInt
    private var pressedStarBackgroundColor = 0
    private var numberOfStars = 0
    private var starsSeparation = 0f
    private var desiredStarSize = 0f
    private var maxStarSize = 0f
    private var stepSize = 0f
    private var rating = 0f
    private var isIndicator = false
    private var gravity: Gravity? = null
    private var starBorderWidth = 0f
    private var starCornerRadius = 0f
    private var drawBorderEnabled = false

    // Internal variables
    private var currentStarSize = 0f
    private var defaultStarSize = 0f
    private var paintStarOutline: Paint? = null
    private var paintStarBorder: Paint? = null
    private var paintStarFill: Paint? = null
    private var paintStarBackground: Paint? = null
    private var cornerPathEffect: CornerPathEffect? = null
    private var starPath: Path? = null
    private var ratingAnimator: ValueAnimator? = null
    private var ratingListener: OnRatingBarChangeListener? = null
    private var clickListener: OnClickListener? = null
    private var touchInProgress = false
    private lateinit var starVertex: FloatArray
    private var starsDrawingSpace: RectF? = null
    private var starsTouchSpace: RectF? = null

    // in order to delete some drawing, and keep transparency
    // http://stackoverflow.com/a/21865858/2271834
    private var internalCanvas: Canvas? = null
    private var internalBitmap: Bitmap? = null

    constructor(context: Context?) : super(context) {
        initView()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        parseAttrs(attrs)
        initView()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        parseAttrs(attrs)
        initView()
    }

    /**
     * Inits paint objects and default values.
     */
    private fun initView() {
        starPath = Path()
        cornerPathEffect = CornerPathEffect(starCornerRadius)
        paintStarOutline = Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG)
        paintStarOutline!!.style = Paint.Style.FILL_AND_STROKE
        paintStarOutline!!.isAntiAlias = true
        paintStarOutline!!.isDither = true
        paintStarOutline!!.strokeJoin = Paint.Join.ROUND
        paintStarOutline!!.strokeCap = Paint.Cap.ROUND
        paintStarOutline!!.color = Color.BLACK
        paintStarOutline!!.pathEffect = cornerPathEffect
        paintStarBorder = Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG)
        paintStarBorder!!.style = Paint.Style.STROKE
        paintStarBorder!!.strokeJoin = Paint.Join.ROUND
        paintStarBorder!!.strokeCap = Paint.Cap.ROUND
        paintStarBorder!!.strokeWidth = starBorderWidth
        paintStarBorder!!.pathEffect = cornerPathEffect
        paintStarBackground = Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG)
        paintStarBackground!!.style = Paint.Style.FILL_AND_STROKE
        paintStarBackground!!.isAntiAlias = true
        paintStarBackground!!.isDither = true
        paintStarBackground!!.strokeJoin = Paint.Join.ROUND
        paintStarBackground!!.strokeCap = Paint.Cap.ROUND
        paintStarFill = Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG)
        paintStarFill!!.style = Paint.Style.FILL_AND_STROKE
        paintStarFill!!.isAntiAlias = true
        paintStarFill!!.isDither = true
        paintStarFill!!.strokeJoin = Paint.Join.ROUND
        paintStarFill!!.strokeCap = Paint.Cap.ROUND
        defaultStarSize =
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30f, resources.displayMetrics)
    }

    /**
     * Parses attributes defined in XML.
     */
    private fun parseAttrs(attrs: AttributeSet?) {
        val arr = context.obtainStyledAttributes(attrs, R.styleable.SimpleRatingBar)
        borderColor = arr.getColor(
            R.styleable.SimpleRatingBar_srb_borderColor,
            resources.getColor(R.color.srb_stars)
        )
        fillColor = arr.getColor(R.styleable.SimpleRatingBar_srb_fillColor, borderColor)
        starBackgroundColor =
            arr.getColor(R.styleable.SimpleRatingBar_srb_starBackgroundColor, Color.TRANSPARENT)
        srbBackgroundColor =
            arr.getColor(R.styleable.SimpleRatingBar_srb_backgroundColor, Color.TRANSPARENT)
        pressedBorderColor =
            arr.getColor(R.styleable.SimpleRatingBar_srb_pressedBorderColor, borderColor)
        pressedFillColor = arr.getColor(R.styleable.SimpleRatingBar_srb_pressedFillColor, fillColor)
        pressedStarBackgroundColor = arr.getColor(
            R.styleable.SimpleRatingBar_srb_pressedStarBackgroundColor,
            starBackgroundColor
        )
        pressedBackgroundColor =
            arr.getColor(R.styleable.SimpleRatingBar_srb_pressedBackgroundColor, srbBackgroundColor)
        numberOfStars = arr.getInteger(R.styleable.SimpleRatingBar_srb_numberOfStars, 5)
        starsSeparation = arr.getDimensionPixelSize(
            R.styleable.SimpleRatingBar_srb_starsSeparation,
            valueToPixels(4f, Dimension.DP).toInt()
        ).toFloat()
        maxStarSize =
            arr.getDimensionPixelSize(R.styleable.SimpleRatingBar_srb_maxStarSize, Int.MAX_VALUE)
                .toFloat()
        desiredStarSize =
            arr.getDimensionPixelSize(R.styleable.SimpleRatingBar_srb_starSize, Int.MAX_VALUE)
                .toFloat()
        stepSize = arr.getFloat(R.styleable.SimpleRatingBar_srb_stepSize, 0.1f)
        starBorderWidth = arr.getFloat(R.styleable.SimpleRatingBar_srb_starBorderWidth, 5f)
        starCornerRadius = arr.getFloat(R.styleable.SimpleRatingBar_srb_starCornerRadius, 6f)
        rating = normalizeRating(arr.getFloat(R.styleable.SimpleRatingBar_srb_rating, 0f))
        isIndicator = arr.getBoolean(R.styleable.SimpleRatingBar_srb_isIndicator, false)
        drawBorderEnabled = arr.getBoolean(R.styleable.SimpleRatingBar_srb_drawBorderEnabled, true)
        gravity =
            Gravity.fromId(arr.getInt(R.styleable.SimpleRatingBar_srb_gravity, Gravity.Left.id))
        arr.recycle()
        validateAttrs()
    }

    /**
     * Validates parsed attributes. It will throw IllegalArgumentException if severe inconsistency is found.
     * Warnings will be logged to LogCat.
     */
    private fun validateAttrs() {
        require(numberOfStars > 0) {
            String.format(
                "SimpleRatingBar initialized with invalid value for numberOfStars. Found %d, but should be greater than 0",
                numberOfStars
            )
        }
        if (desiredStarSize != Int.MAX_VALUE.toFloat() && maxStarSize != Int.MAX_VALUE.toFloat() && (desiredStarSize
                    > maxStarSize)
        ) {
            Log.w(
                "SimpleRatingBar",
                String.format(
                    "Initialized with conflicting values: starSize is greater than maxStarSize (%f > %f). I will ignore maxStarSize",
                    desiredStarSize,
                    maxStarSize
                )
            )
        }
        require(stepSize > 0) {
            String.format(
                "SimpleRatingBar initialized with invalid value for stepSize. Found %f, but should be greater than 0",
                stepSize
            )
        }
        require(starBorderWidth > 0) {
            String.format(
                "SimpleRatingBar initialized with invalid value for starBorderWidth. Found %f, but should be greater than 0",
                starBorderWidth
            )
        }
        require(starCornerRadius >= 0) {
            String.format(
                "SimpleRatingBar initialized with invalid value for starCornerRadius. Found %f, but should be greater or equal than 0",
                starBorderWidth
            )
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        val width: Int
        val height: Int

        //Measure Width
        width = if (widthMode == MeasureSpec.EXACTLY) {
            //Must be this size
            widthSize
        } else if (widthMode == MeasureSpec.AT_MOST) {
            //Can't be bigger than...
            if (desiredStarSize != Int.MAX_VALUE.toFloat()) {
                // user specified a specific star size, so there is a desired width
                val desiredWidth =
                    calculateTotalWidth(desiredStarSize, numberOfStars, starsSeparation, true)
                Math.min(desiredWidth, widthSize)
            } else if (maxStarSize != Int.MAX_VALUE.toFloat()) {
                // user specified a max star size, so there is a desired width
                val desiredWidth =
                    calculateTotalWidth(maxStarSize, numberOfStars, starsSeparation, true)
                Math.min(desiredWidth, widthSize)
            } else {
                // using defaults
                val desiredWidth =
                    calculateTotalWidth(defaultStarSize, numberOfStars, starsSeparation, true)
                Math.min(desiredWidth, widthSize)
            }
        } else {
            //Be whatever you want
            if (desiredStarSize != Int.MAX_VALUE.toFloat()) {
                // user specified a specific star size, so there is a desired width
                val desiredWidth =
                    calculateTotalWidth(desiredStarSize, numberOfStars, starsSeparation, true)
                desiredWidth
            } else if (maxStarSize != Int.MAX_VALUE.toFloat()) {
                // user specified a max star size, so there is a desired width
                val desiredWidth =
                    calculateTotalWidth(maxStarSize, numberOfStars, starsSeparation, true)
                desiredWidth
            } else {
                // using defaults
                val desiredWidth =
                    calculateTotalWidth(defaultStarSize, numberOfStars, starsSeparation, true)
                desiredWidth
            }
        }
        val tentativeStarSize =
            (width - paddingLeft - paddingRight - starsSeparation * (numberOfStars - 1)) / numberOfStars

        //Measure Height
        height = if (heightMode == MeasureSpec.EXACTLY) {
            //Must be this size
            heightSize
        } else if (heightMode == MeasureSpec.AT_MOST) {
            //Can't be bigger than...
            if (desiredStarSize != Int.MAX_VALUE.toFloat()) {
                // user specified a specific star size, so there is a desired width
                val desiredHeight =
                    calculateTotalHeight(desiredStarSize, numberOfStars, starsSeparation, true)
                Math.min(desiredHeight, heightSize)
            } else if (maxStarSize != Int.MAX_VALUE.toFloat()) {
                // user specified a max star size, so there is a desired width
                val desiredHeight =
                    calculateTotalHeight(maxStarSize, numberOfStars, starsSeparation, true)
                Math.min(desiredHeight, heightSize)
            } else {
                // using defaults
                val desiredHeight =
                    calculateTotalHeight(tentativeStarSize, numberOfStars, starsSeparation, true)
                Math.min(desiredHeight, heightSize)
            }
        } else {
            //Be whatever you want
            if (desiredStarSize != Int.MAX_VALUE.toFloat()) {
                // user specified a specific star size, so there is a desired width
                val desiredHeight =
                    calculateTotalHeight(desiredStarSize, numberOfStars, starsSeparation, true)
                desiredHeight
            } else if (maxStarSize != Int.MAX_VALUE.toFloat()) {
                // user specified a max star size, so there is a desired width
                val desiredHeight =
                    calculateTotalHeight(maxStarSize, numberOfStars, starsSeparation, true)
                desiredHeight
            } else {
                // using defaults
                val desiredHeight =
                    calculateTotalHeight(tentativeStarSize, numberOfStars, starsSeparation, true)
                desiredHeight
            }
        }

        //MUST CALL THIS
        setMeasuredDimension(width, height)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        val width = width
        val height = height
        currentStarSize = if (desiredStarSize == Int.MAX_VALUE.toFloat()) {
            calculateBestStarSize(width, height)
        } else {
            desiredStarSize
        }
        performStarSizeAssociatedCalculations(width, height)
    }

    /**
     * Calculates largest possible star size, based on chosen width and height.
     * If maxStarSize is present, it will be considered and star size will not be greater than this value.
     *
     * @param width
     * @param height
     */
    private fun calculateBestStarSize(width: Int, height: Int): Float {
        return if (maxStarSize != Int.MAX_VALUE.toFloat()) {
            val desiredTotalWidth =
                calculateTotalWidth(maxStarSize, numberOfStars, starsSeparation, true).toFloat()
            val desiredTotalHeight =
                calculateTotalHeight(maxStarSize, numberOfStars, starsSeparation, true).toFloat()
            if (desiredTotalWidth >= width || desiredTotalHeight >= height) {
                // we need to shrink the size of the stars
                val sizeBasedOnWidth =
                    (width - paddingLeft - paddingRight - starsSeparation * (numberOfStars - 1)) / numberOfStars
                val sizeBasedOnHeight = (height - paddingTop - paddingBottom).toFloat()
                Math.min(sizeBasedOnWidth, sizeBasedOnHeight)
            } else {
                maxStarSize
            }
        } else {
            // expand the most we can
            val sizeBasedOnWidth =
                (width - paddingLeft - paddingRight - starsSeparation * (numberOfStars - 1)) / numberOfStars
            val sizeBasedOnHeight = (height - paddingTop - paddingBottom).toFloat()
            Math.min(sizeBasedOnWidth, sizeBasedOnHeight)
        }
    }

    /**
     * Performs auxiliary calculations to later speed up drawing phase.
     *
     * @param width
     * @param height
     */
    private fun performStarSizeAssociatedCalculations(width: Int, height: Int) {
        val totalStarsWidth =
            calculateTotalWidth(currentStarSize, numberOfStars, starsSeparation, false).toFloat()
        val totalStarsHeight =
            calculateTotalHeight(currentStarSize, numberOfStars, starsSeparation, false).toFloat()
        val startingX = (width - paddingLeft - paddingRight) / 2 - totalStarsWidth / 2 + paddingLeft
        val startingY =
            (height - paddingTop - paddingBottom) / 2 - totalStarsHeight / 2 + paddingTop
        starsDrawingSpace =
            RectF(startingX, startingY, startingX + totalStarsWidth, startingY + totalStarsHeight)
        val aux = starsDrawingSpace!!.width() * 0.05f
        starsTouchSpace = RectF(
            starsDrawingSpace!!.left - aux,
            starsDrawingSpace!!.top,
            starsDrawingSpace!!.right + aux,
            starsDrawingSpace!!.bottom
        )
        val bottomFromMargin = currentStarSize * 0.2f
        val triangleSide = currentStarSize * 0.35f
        val half = currentStarSize * 0.5f
        val tipVerticalMargin = currentStarSize * 0.05f
        val tipHorizontalMargin = currentStarSize * 0.03f
        val innerUpHorizontalMargin = currentStarSize * 0.38f
        val innerBottomHorizontalMargin = currentStarSize * 0.32f
        val innerBottomVerticalMargin = currentStarSize * 0.6f
        val innerCenterVerticalMargin = currentStarSize * 0.27f
        starVertex = floatArrayOf(
            tipHorizontalMargin,
            innerUpHorizontalMargin,  // top left
            tipHorizontalMargin + triangleSide,
            innerUpHorizontalMargin,
            half,
            tipVerticalMargin,  // top tip
            currentStarSize - tipHorizontalMargin - triangleSide,
            innerUpHorizontalMargin,
            currentStarSize - tipHorizontalMargin,
            innerUpHorizontalMargin,  // top right
            currentStarSize - innerBottomHorizontalMargin,
            innerBottomVerticalMargin,
            currentStarSize - bottomFromMargin,
            currentStarSize - tipVerticalMargin,  // bottom right
            half,
            currentStarSize - innerCenterVerticalMargin,
            bottomFromMargin,
            currentStarSize - tipVerticalMargin,  // bottom left
            innerBottomHorizontalMargin,
            innerBottomVerticalMargin
        )
    }

    /**
     * Calculates total width to occupy based on several parameters
     *
     * @param starSize
     * @param numberOfStars
     * @param starsSeparation
     * @param padding
     * @return
     */
    private fun calculateTotalWidth(
        starSize: Float,
        numberOfStars: Int,
        starsSeparation: Float,
        padding: Boolean
    ): Int {
        return (Math.round(starSize * numberOfStars + starsSeparation * (numberOfStars - 1))
                + if (padding) paddingLeft + paddingRight else 0)
    }

    /**
     * Calculates total height to occupy based on several parameters
     *
     * @param starSize
     * @param numberOfStars
     * @param starsSeparation
     * @param padding
     * @return
     */
    private fun calculateTotalHeight(
        starSize: Float,
        numberOfStars: Int,
        starsSeparation: Float,
        padding: Boolean
    ): Int {
        return Math.round(starSize) + if (padding) paddingTop + paddingBottom else 0
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        generateInternalCanvas(w, h)
    }

    /**
     * Generates internal canvas on which the ratingbar will be drawn.
     *
     * @param w
     * @param h
     */
    private fun generateInternalCanvas(w: Int, h: Int) {
        if (internalBitmap != null) {
            // avoid leaking memory after losing the reference
            internalBitmap!!.recycle()
        }
        if (w > 0 && h > 0) {
            // if width == 0 or height == 0 we don't need internal bitmap, cause view won't be drawn anyway.
            internalBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
            internalBitmap!!.eraseColor(Color.TRANSPARENT)
            internalCanvas = Canvas(internalBitmap!!)
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val height = height
        val width = width
        if (width == 0 || height == 0) {
            // don't draw view with width or height equal zero.
            return
        }

        // clean internal canvas
        internalCanvas!!.drawColor(0, PorterDuff.Mode.CLEAR)

        // choose colors
        setupColorsInPaint()

        // draw stars
        if (gravity == Gravity.Left) {
            drawFromLeftToRight(internalCanvas)
        } else {
            drawFromRightToLeft(internalCanvas)
        }

        // draw view background color
        if (touchInProgress) {
            canvas.drawColor(pressedBackgroundColor)
        } else {
            canvas.drawColor(srbBackgroundColor)
        }

        // draw internal bitmap to definite canvas
        canvas.drawBitmap(internalBitmap!!, 0f, 0f, null)
    }

    /**
     * Sets the color for the different paints depending on whether current state is pressed or normal.
     */
    private fun setupColorsInPaint() {
        if (touchInProgress) {
            paintStarBorder!!.color = pressedBorderColor
            paintStarFill!!.color = pressedFillColor
            if (pressedFillColor != Color.TRANSPARENT) {
                paintStarFill!!.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP)
            } else {
                paintStarFill!!.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
            }
            paintStarBackground!!.color = pressedStarBackgroundColor
            if (pressedStarBackgroundColor != Color.TRANSPARENT) {
                paintStarBackground!!.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP)
            } else {
                paintStarBackground!!.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
            }
        } else {
            paintStarBorder!!.color = borderColor
            paintStarFill!!.color = fillColor
            if (fillColor != Color.TRANSPARENT) {
                paintStarFill!!.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP)
            } else {
                paintStarFill!!.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
            }
            paintStarBackground!!.color = starBackgroundColor
            if (starBackgroundColor != Color.TRANSPARENT) {
                paintStarBackground!!.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP)
            } else {
                paintStarBackground!!.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
            }
        }
    }

    /**
     * Draws the view when gravity is Left
     *
     * @param internalCanvas
     */
    private fun drawFromLeftToRight(internalCanvas: Canvas?) {
        var remainingTotalRating = rating
        var startingX = starsDrawingSpace!!.left
        val startingY = starsDrawingSpace!!.top
        for (i in 0 until numberOfStars) {
            if (remainingTotalRating >= 1) {
                drawStar(internalCanvas, startingX, startingY, 1f, Gravity.Left)
                remainingTotalRating -= 1f
            } else {
                drawStar(internalCanvas, startingX, startingY, remainingTotalRating, Gravity.Left)
                remainingTotalRating = 0f
            }
            startingX += starsSeparation + currentStarSize
        }
    }

    /**
     * Draws the view when gravity is Right
     *
     * @param internalCanvas
     */
    private fun drawFromRightToLeft(internalCanvas: Canvas?) {
        var remainingTotalRating = rating
        var startingX = starsDrawingSpace!!.right - currentStarSize
        val startingY = starsDrawingSpace!!.top
        for (i in 0 until numberOfStars) {
            if (remainingTotalRating >= 1) {
                drawStar(internalCanvas, startingX, startingY, 1f, Gravity.Right)
                remainingTotalRating -= 1f
            } else {
                drawStar(internalCanvas, startingX, startingY, remainingTotalRating, Gravity.Right)
                remainingTotalRating = 0f
            }
            startingX -= starsSeparation + currentStarSize
        }
    }

    /**
     * Draws a star in the provided canvas.
     *
     * @param canvas
     * @param x       left of the star
     * @param y       top of the star
     * @param filled  between 0 and 1
     * @param gravity Left or Right
     */
    private fun drawStar(canvas: Canvas?, x: Float, y: Float, filled: Float, gravity: Gravity) {
        // calculate fill in pixels
        val fill = currentStarSize * filled

        // prepare path for star
        starPath!!.reset()
        starPath!!.moveTo(x + starVertex[0], y + starVertex[1])
        var i = 2
        while (i < starVertex.size) {
            starPath!!.lineTo(x + starVertex[i], y + starVertex[i + 1])
            i = i + 2
        }
        starPath!!.close()

        // draw star outline
        canvas!!.drawPath(starPath!!, paintStarOutline!!)

        // Note: below, currentStarSize*0.02f is a minor correction so the user won't see a vertical black line in between the fill and empty color
        if (gravity == Gravity.Left) {
            // color star fill
            canvas.drawRect(
                x,
                y,
                x + fill + currentStarSize * 0.02f,
                y + currentStarSize,
                paintStarFill!!
            )
            // draw star background
            canvas.drawRect(
                x + fill,
                y,
                x + currentStarSize,
                y + currentStarSize,
                paintStarBackground!!
            )
        } else {
            // color star fill
            canvas.drawRect(
                x + currentStarSize - (fill + currentStarSize * 0.02f),
                y,
                x + currentStarSize,
                y + currentStarSize,
                paintStarFill!!
            )
            // draw star background
            canvas.drawRect(
                x,
                y,
                x + currentStarSize - fill,
                y + currentStarSize,
                paintStarBackground!!
            )
        }

        // draw star border on top
        if (drawBorderEnabled) {
            canvas.drawPath(starPath!!, paintStarBorder!!)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (isIndicator || ratingAnimator != null && ratingAnimator!!.isRunning) {
            return false
        }
        val action = event.action and MotionEvent.ACTION_MASK
        when (action) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE ->                 // check if action is performed on stars
                if (starsTouchSpace!!.contains(event.x, event.y)) {
                    touchInProgress = true
                    setNewRatingFromTouch(event.x, event.y)
                } else {
                    if (touchInProgress && ratingListener != null) {
                        ratingListener!!.onRatingChanged(this, rating, true)
                    }
                    touchInProgress = false
                    return false
                }
            MotionEvent.ACTION_UP -> {
                setNewRatingFromTouch(event.x, event.y)
                if (clickListener != null) {
                    clickListener!!.onClick(this)
                }
                if (ratingListener != null) {
                    ratingListener!!.onRatingChanged(this, rating, true)
                }
                touchInProgress = false
            }
            MotionEvent.ACTION_CANCEL -> {
                if (ratingListener != null) {
                    ratingListener!!.onRatingChanged(this, rating, true)
                }
                touchInProgress = false
            }
        }
        invalidate()
        return true
    }

    /**
     * Assigns a rating to the touch event.
     *
     * @param x
     * @param y
     */
    private fun setNewRatingFromTouch(x: Float, y: Float) {
        // normalize x to inside starsDrawinSpace
        var x = x
        if (gravity != Gravity.Left) {
            x = width - x
        }

        // we know that touch was inside starsTouchSpace, but it might be outside starsDrawingSpace
        if (x < starsDrawingSpace!!.left) {
            rating = 0f
            return
        } else if (x > starsDrawingSpace!!.right) {
            rating = numberOfStars.toFloat()
            return
        }
        x = x - starsDrawingSpace!!.left
        // reduce the width to allow the user reach the top and bottom values of rating (0 and numberOfStars)
        rating = numberOfStars.toFloat() / starsDrawingSpace!!.width() * x

        // correct rating in case step size is present
        val mod = rating % stepSize
        if (mod < stepSize / 4) {
            rating = rating - mod
            rating = Math.max(0f, rating)
        } else {
            rating = rating - mod + stepSize
            rating = Math.min(numberOfStars.toFloat(), rating)
        }
    }

    override fun onSaveInstanceState(): Parcelable? {
        val superState = super.onSaveInstanceState()
        val savedState = SavedState(superState)
        savedState.rating = getRating()
        return savedState
    }

    override fun onRestoreInstanceState(state: Parcelable) {
        val savedState = state as SavedState
        super.onRestoreInstanceState(savedState.superState)
        setRating(savedState.rating)
    }

    private class SavedState : BaseSavedState {
        var rating = 0.0f

        protected constructor(source: Parcel) : super(source) {
            rating = source.readFloat()
        }

        @TargetApi(Build.VERSION_CODES.N)
        protected constructor(source: Parcel?, loader: ClassLoader?) : super(source, loader)

        constructor(superState: Parcelable?) : super(superState)

        override fun writeToParcel(out: Parcel, flags: Int) {
            super.writeToParcel(out, flags)
            out.writeFloat(rating)
        }

        companion object {
            @JvmField
            val CREATOR: Parcelable.Creator<SavedState?> =
                object : Parcelable.Creator<SavedState?> {
                    override fun createFromParcel(parcel: Parcel): SavedState? {
                        return SavedState(parcel)
                    }

                    override fun newArray(size: Int): Array<SavedState?> {
                        return arrayOfNulls(size)
                    }
                }
        }
    }

    /* ----------- GETTERS AND SETTERS ----------- */
    fun getRating(): Float {
        return rating
    }

    /**
     * Sets rating.
     * If provided value is less than 0, rating will be set to 0.
     * * If provided value is greater than numberOfStars, rating will be set to numberOfStars.
     *
     * @param rating
     */
    fun setRating(rating: Float) {
        this.rating = normalizeRating(rating)
        // request redraw of the view
        invalidate()
        if (ratingListener != null && (ratingAnimator == null || !ratingAnimator!!.isRunning)) {
            ratingListener!!.onRatingChanged(this, rating, false)
        }
    }

    fun getStepSize(): Float {
        return stepSize
    }

    /**
     * Sets step size of rating.
     * Throws IllegalArgumentException if provided value is less or equal than zero.
     *
     * @param stepSize
     */
    fun setStepSize(stepSize: Float) {
        this.stepSize = stepSize
        require(stepSize > 0) {
            String.format(
                "SimpleRatingBar initialized with invalid value for stepSize. Found %f, but should be greater than 0",
                stepSize
            )
        }
        // request redraw of the view
        invalidate()
    }

    fun isIndicator(): Boolean {
        return isIndicator
    }

    /**
     * Sets indicator property.
     * If provided value is true, touch events will be deactivated, and thus user interaction will be deactivated.
     *
     * @param indicator
     */
    fun setIndicator(indicator: Boolean) {
        isIndicator = indicator
        touchInProgress = false
    }

    /**
     * Returns max star size in pixels.
     *
     * @return
     */
    fun getMaxStarSize(): Float {
        return maxStarSize
    }

    /**
     * Returns max star size in the requested dimension.
     *
     * @param dimen
     * @return
     */
    fun getMaxStarSize(@Dimension dimen: Int): Float {
        return valueFromPixels(maxStarSize, dimen)
    }

    /**
     * Sets maximum star size in pixels.
     * If current star size is less than provided value, this has no effect on the view.
     *
     * @param maxStarSize
     */
    fun setMaxStarSize(maxStarSize: Float) {
        this.maxStarSize = maxStarSize
        if (currentStarSize > maxStarSize) {
            // force re-calculating the layout dimension
            requestLayout()
            generateInternalCanvas(width, height)
            // request redraw of the view
            invalidate()
        }
    }

    /**
     * Sets maximum star size using the given dimension.
     * If current star size is less than provided value, this has no effect on the view.
     *
     * @param maxStarSize
     */
    fun setMaxStarSize(maxStarSize: Float, @Dimension dimen: Int) {
        setMaxStarSize(valueToPixels(maxStarSize, dimen))
    }
    /**
     * Return star size in pixels.
     *
     * @return
     */// force re-calculating the layout dimension
    // request redraw of the view
    /**
     * Sets exact star size in pixels.
     *
     * @param starSize
     */
    var starSize: Float
        get() = currentStarSize
        set(starSize) {
            desiredStarSize = starSize
            if (starSize != Int.MAX_VALUE.toFloat() && maxStarSize != Int.MAX_VALUE.toFloat() && starSize > maxStarSize) {
                Log.w(
                    "SimpleRatingBar",
                    String.format(
                        "Initialized with conflicting values: starSize is greater than maxStarSize (%f > %f). I will ignore maxStarSize",
                        starSize,
                        maxStarSize
                    )
                )
            }
            // force re-calculating the layout dimension
            requestLayout()
            generateInternalCanvas(width, height)
            // request redraw of the view
            invalidate()
        }

    /**
     * Return star size in the requested dimension.
     *
     * @param dimen
     * @return
     */
    fun getStarSize(@Dimension dimen: Int): Float {
        return valueFromPixels(currentStarSize, dimen)
    }

    /**
     * Sets exact star size using the given dimension.
     *
     * @param starSize
     * @param dimen
     */
    fun setStarSize(starSize: Float, @Dimension dimen: Int) {
        this.starSize = valueToPixels(starSize, dimen)
    }

    /**
     * Returns stars separation in pixels.
     *
     * @return
     */
    fun getStarsSeparation(): Float {
        return starsSeparation
    }

    /**
     * Returns stars separation in the requested dimension.
     *
     * @param dimen
     * @return
     */
    fun getStarsSeparation(@Dimension dimen: Int): Float {
        return valueFromPixels(starsSeparation, dimen)
    }

    /**
     * Sets separation between stars in pixels.
     *
     * @param starsSeparation
     */
    fun setStarsSeparation(starsSeparation: Float) {
        this.starsSeparation = starsSeparation
        // force re-calculating the layout dimension
        requestLayout()
        generateInternalCanvas(width, height)
        // request redraw of the view
        invalidate()
    }

    /**
     * Sets separation between stars using the given dimension.
     *
     * @param starsSeparation
     */
    fun setStarsSeparation(starsSeparation: Float, @Dimension dimen: Int) {
        setStarsSeparation(valueToPixels(starsSeparation, dimen))
    }

    fun getNumberOfStars(): Int {
        return numberOfStars
    }

    /**
     * Sets number of stars.
     * It also sets the rating to zero.
     * Throws IllegalArgumentException if provided value is less or equal than zero.
     *
     * @param numberOfStars
     */
    fun setNumberOfStars(numberOfStars: Int) {
        this.numberOfStars = numberOfStars
        require(numberOfStars > 0) {
            String.format(
                "SimpleRatingBar initialized with invalid value for numberOfStars. Found %d, but should be greater than 0",
                numberOfStars
            )
        }
        rating = 0f
        // force re-calculating the layout dimension
        requestLayout()
        generateInternalCanvas(width, height)
        // request redraw of the view
        invalidate()
    }

    /**
     * Returns star border width in pixels.
     *
     * @return
     */
    fun getStarBorderWidth(): Float {
        return starBorderWidth
    }

    /**
     * Returns star border width in the requested dimension.
     *
     * @param dimen
     * @return
     */
    fun getStarBorderWidth(@Dimension dimen: Int): Float {
        return valueFromPixels(starBorderWidth, dimen)
    }

    /**
     * Sets border width of stars in pixels.
     * Throws IllegalArgumentException if provided value is less or equal than zero.
     *
     * @param starBorderWidth
     */
    fun setStarBorderWidth(starBorderWidth: Float) {
        this.starBorderWidth = starBorderWidth
        require(starBorderWidth > 0) {
            String.format(
                "SimpleRatingBar initialized with invalid value for starBorderWidth. Found %f, but should be greater than 0",
                starBorderWidth
            )
        }
        paintStarBorder!!.strokeWidth = starBorderWidth
        // request redraw of the view
        invalidate()
    }

    /**
     * Sets border width of stars using the given dimension.
     * Throws IllegalArgumentException if provided value is less or equal than zero.
     *
     * @param starBorderWidth
     * @param dimen
     */
    fun setStarBorderWidth(starBorderWidth: Float, @Dimension dimen: Int) {
        setStarBorderWidth(valueToPixels(starBorderWidth, dimen))
    }

    /**
     * Returns start corner radius in pixels,
     *
     * @return
     */
    fun getStarCornerRadius(): Float {
        return starCornerRadius
    }

    /**
     * Returns start corner radius in the requested dimension,
     *
     * @param dimen
     * @return
     */
    fun getStarCornerRadius(@Dimension dimen: Int): Float {
        return valueFromPixels(starCornerRadius, dimen)
    }

    /**
     * Sets radius of star corner in pixels.
     * Throws IllegalArgumentException if provided value is less than zero.
     *
     * @param starCornerRadius
     */
    fun setStarCornerRadius(starCornerRadius: Float) {
        this.starCornerRadius = starCornerRadius
        require(starCornerRadius >= 0) {
            String.format(
                "SimpleRatingBar initialized with invalid value for starCornerRadius. Found %f, but should be greater or equal than 0",
                starCornerRadius
            )
        }
        cornerPathEffect = CornerPathEffect(starCornerRadius)
        paintStarBorder!!.pathEffect = cornerPathEffect
        paintStarOutline!!.pathEffect = cornerPathEffect
        // request redraw of the view
        invalidate()
    }

    /**
     * Sets radius of star corner using the given dimension.
     * Throws IllegalArgumentException if provided value is less than zero.
     *
     * @param starCornerRadius
     * @param dimen
     */
    fun setStarCornerRadius(starCornerRadius: Float, @Dimension dimen: Int) {
        setStarCornerRadius(valueToPixels(starCornerRadius, dimen))
    }

    @ColorInt
    fun getBorderColor(): Int {
        return borderColor
    }

    /**
     * Sets border color of stars in normal state.
     *
     * @param borderColor
     */
    fun setBorderColor(@ColorInt borderColor: Int) {
        this.borderColor = borderColor
        // request redraw of the view
        invalidate()
    }

    @ColorInt
    fun getFillColor(): Int {
        return fillColor
    }

    /**
     * Sets fill color of stars in normal state.
     *
     * @param fillColor
     */
    fun setFillColor(@ColorInt fillColor: Int) {
        this.fillColor = fillColor
        // request redraw of the view
        invalidate()
    }

    @ColorInt
    fun getStarBackgroundColor(): Int {
        return starBackgroundColor
    }

    /**
     * Sets background color of stars in normal state.
     *
     * @param starBackgroundColor
     */
    fun setStarBackgroundColor(@ColorInt starBackgroundColor: Int) {
        this.starBackgroundColor = starBackgroundColor
        // request redraw of the view
        invalidate()
    }

    @ColorInt
    fun getPressedBorderColor(): Int {
        return pressedBorderColor
    }

    /**
     * Sets border color of stars in pressed state.
     *
     * @param pressedBorderColor
     */
    fun setPressedBorderColor(@ColorInt pressedBorderColor: Int) {
        this.pressedBorderColor = pressedBorderColor
        // request redraw of the view
        invalidate()
    }

    @ColorInt
    fun getPressedFillColor(): Int {
        return pressedFillColor
    }

    /**
     * Sets fill color of stars in pressed state.
     *
     * @param pressedFillColor
     */
    fun setPressedFillColor(@ColorInt pressedFillColor: Int) {
        this.pressedFillColor = pressedFillColor
        // request redraw of the view
        invalidate()
    }

    @ColorInt
    fun getPressedStarBackgroundColor(): Int {
        return pressedStarBackgroundColor
    }

    /**
     * Sets background color of stars in pressed state.
     *
     * @param pressedStarBackgroundColor
     */
    fun setPressedStarBackgroundColor(@ColorInt pressedStarBackgroundColor: Int) {
        this.pressedStarBackgroundColor = pressedStarBackgroundColor
        // request redraw of the view
        invalidate()
    }

    fun getGravity(): Gravity? {
        return gravity
    }

    /**
     * Sets gravity of fill.
     *
     * @param gravity
     */
    fun setGravity(gravity: Gravity?) {
        this.gravity = gravity
        // request redraw of the view
        invalidate()
    }

    fun isDrawBorderEnabled(): Boolean {
        return drawBorderEnabled
    }

    /**
     * Sets drawBorder property.
     * If provided value is true, border will be drawn, otherwise it will be omithed.
     *
     * @param drawBorderEnabled
     */
    fun setDrawBorderEnabled(drawBorderEnabled: Boolean) {
        this.drawBorderEnabled = drawBorderEnabled
        // request redraw of the view
        invalidate()
    }

    /**
     * Convenience method to convert a value in the given dimension to pixels.
     *
     * @param value
     * @param dimen
     * @return
     */
    private fun valueToPixels(value: Float, @Dimension dimen: Int): Float {
        return when (dimen) {
            Dimension.DP -> TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                value,
                resources.displayMetrics
            )
            Dimension.SP -> TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP,
                value,
                resources.displayMetrics
            )
            else -> value
        }
    }

    /**
     * Convenience method to convert a value from pixels to the given dimension.
     *
     * @param value
     * @param dimen
     * @return
     */
    private fun valueFromPixels(value: Float, @Dimension dimen: Int): Float {
        return when (dimen) {
            Dimension.DP -> value / resources.displayMetrics.density
            Dimension.SP -> value / resources.displayMetrics.scaledDensity
            else -> value
        }
    }

    /**
     * Sets rating with animation.
     *
     * @param builder
     */
    private fun animateRating(builder: AnimationBuilder) {
        builder.ratingTarget = normalizeRating(builder.ratingTarget)
        ratingAnimator = ValueAnimator.ofFloat(0f, builder.ratingTarget)
        ratingAnimator!!.duration = builder.duration
        ratingAnimator!!.repeatCount = builder.repeatCount
        ratingAnimator!!.repeatMode = builder.repeatMode

        // Callback that executes on animation steps.
        ratingAnimator!!.addUpdateListener(AnimatorUpdateListener { animation ->
            val value = (animation.animatedValue as Float).toFloat()
            setRating(value)
        })
        if (builder.interpolator != null) {
            ratingAnimator!!.interpolator = builder.interpolator
        }
        if (builder.animatorListener != null) {
            ratingAnimator!!.addListener(builder.animatorListener)
        }
        ratingAnimator!!.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animator: Animator) {}
            override fun onAnimationEnd(animator: Animator) {
                if (ratingListener != null) {
                    ratingListener!!.onRatingChanged(this@SimpleRatingBar, rating, false)
                }
            }

            override fun onAnimationCancel(animator: Animator) {
                if (ratingListener != null) {
                    ratingListener!!.onRatingChanged(this@SimpleRatingBar, rating, false)
                }
            }

            override fun onAnimationRepeat(animator: Animator) {
                if (ratingListener != null) {
                    ratingListener!!.onRatingChanged(this@SimpleRatingBar, rating, false)
                }
            }
        })
        ratingAnimator!!.start()
    }

    /**
     * Returns a new AnimationBuilder.
     *
     * @return
     */
    val animationBuilder: AnimationBuilder
        get() = AnimationBuilder(this)

    /**
     * Normalizes rating passed by argument between 0 and numberOfStars.
     *
     * @param rating
     * @return
     */
    private fun normalizeRating(rating: Float): Float {
        return when {
            rating < 0 -> {
                Log.w(
                    "SimpleRatingBar",
                    String.format(
                        "Assigned rating is less than 0 (%f < 0), I will set it to exactly 0",
                        rating
                    )
                )
                0f
            }
            rating > numberOfStars -> {
                Log.w(
                    "SimpleRatingBar",
                    String.format(
                        "Assigned rating is greater than numberOfStars (%f > %d), I will set it to exactly numberOfStars",
                        rating,
                        numberOfStars
                    )
                )
                numberOfStars.toFloat()
            }
            else -> {
                rating
            }
        }
    }

    /**
     * Sets OnClickListener.
     *
     * @param listener
     */
    override fun setOnClickListener(listener: OnClickListener?) {
        clickListener = listener
    }

    /**
     * Sets OnRatingBarChangeListener.
     *
     * @param listener
     */
    fun setOnRatingBarChangeListener(listener: OnRatingBarChangeListener?) {
        ratingListener = listener
    }

    interface OnRatingBarChangeListener {
        /**
         * Notification that the rating has changed. Clients can use the
         * fromUser parameter to distinguish user-initiated changes from those
         * that occurred programmatically. This will not be called continuously
         * while the user is dragging, only when the user finalizes a rating by
         * lifting the touch.
         *
         * @param simpleRatingBar The RatingBar whose rating has changed.
         * @param rating          The current rating. This will be in the range
         * 0..numStars.
         * @param fromUser        True if the rating change was initiated by a user's
         * touch gesture or arrow key/horizontal trackbell movement.
         */
        fun onRatingChanged(simpleRatingBar: SimpleRatingBar?, rating: Float, fromUser: Boolean)
    }

    /**
     * Helper class to build rating animation.
     * Provides good defaults:
     * - Target rating: numberOfStars
     * - Animation: Bounce
     * - Duration: 2s
     */
    inner class AnimationBuilder(private val ratingBar: SimpleRatingBar) {
        var duration: Long = 2000
        var interpolator: Interpolator?
        var ratingTarget: Float
        var repeatCount: Int
        var repeatMode: Int
        var animatorListener: Animator.AnimatorListener? = null

        /**
         * Sets duration of animation.
         *
         * @param duration
         * @return
         */
        fun setDuration(duration: Long): AnimationBuilder {
            this.duration = duration
            return this
        }

        /**
         * Sets interpolator for animation.
         *
         * @param interpolator
         * @return
         */
        fun setInterpolator(interpolator: Interpolator?): AnimationBuilder {
            this.interpolator = interpolator
            return this
        }

        /**
         * Sets rating after animation has ended.
         *
         * @param ratingTarget
         * @return
         */
        fun setRatingTarget(ratingTarget: Float): AnimationBuilder {
            this.ratingTarget = ratingTarget
            return this
        }

        /**
         * Sets repeat count for animation.
         *
         * @param repeatCount must be a positive value or ValueAnimator.INFINITE
         * @return
         */
        fun setRepeatCount(repeatCount: Int): AnimationBuilder {
            this.repeatCount = repeatCount
            return this
        }

        /**
         * Sets repeat mode for animation.
         *
         * @param repeatMode must be ValueAnimator.RESTART or ValueAnimator.REVERSE
         * @return
         */
        fun setRepeatMode(repeatMode: Int): AnimationBuilder {
            this.repeatMode = repeatMode
            return this
        }

        /**
         * Sets AnimatorListener.
         *
         * @param animatorListener
         * @return
         */
        fun setAnimatorListener(animatorListener: Animator.AnimatorListener?): AnimationBuilder {
            this.animatorListener = animatorListener
            return this
        }

        /**
         * Starts animation.
         */
        fun start() {
            ratingBar.animateRating(this)
        }

        init {
            interpolator = BounceInterpolator()
            ratingTarget = ratingBar.getNumberOfStars().toFloat()
            repeatCount = 1
            repeatMode = ValueAnimator.REVERSE
        }
    }
}