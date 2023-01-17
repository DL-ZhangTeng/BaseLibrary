package com.zhangteng.base.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import com.zhangteng.base.R
import kotlin.math.max
import kotlin.math.min

/**
 * Created by Swing on 2019/6/05.
 */
open class ShapedImageView : AppCompatImageView {
    private lateinit var mShaderMatrix: Matrix
    private lateinit var mViewRect: RectF // ImageView的矩形区域
    private lateinit var mBorderRect: RectF  // 边框的矩形区域
    private lateinit var mBorderPaint: Paint
    private lateinit var mBitmapPaint: Paint
    private lateinit var mPath: Path
    private var isInit: Boolean = false
    private var mBorderSize = 0f // 边框大小,默认为0，即无边框
    private var mBorderColor = Color.WHITE // 边框颜色，默认为白色
    private var mShape = ShapeType.SHAPE_REC.type // 形状，默认为直接矩形
    private var mRoundRadius = 0f // 矩形的圆角半径,默认为0，即直角矩形
    private var mRoundRadiusLeftTop = 0f
    private var mRoundRadiusLeftBottom = 0f
    private var mRoundRadiusRightTop = 0f
    private var mRoundRadiusRightBottom = 0f
    private var mBitmapShader: BitmapShader? = null
    private var mBitmap: Bitmap? = null

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.ShapedImageView, defStyleAttr, 0)

        mShape = a.getInt(R.styleable.ShapedImageView_siv_shape, ShapeType.SHAPE_REC.type)
        mRoundRadius = a.getDimension(R.styleable.ShapedImageView_siv_corner_radius, 0f)
        mRoundRadiusLeftBottom =
            a.getDimension(R.styleable.ShapedImageView_siv_round_radius_leftBottom, mRoundRadius)
        mRoundRadiusLeftTop =
            a.getDimension(R.styleable.ShapedImageView_siv_round_radius_leftTop, mRoundRadius)
        mRoundRadiusRightBottom =
            a.getDimension(R.styleable.ShapedImageView_siv_round_radius_rightBottom, mRoundRadius)
        mRoundRadiusRightTop =
            a.getDimension(R.styleable.ShapedImageView_siv_round_radius_rightTop, mRoundRadius)
        mBorderSize = a.getDimension(R.styleable.ShapedImageView_siv_border_size, 0f)
        mBorderColor = a.getColor(R.styleable.ShapedImageView_siv_border_color, Color.WHITE)
        a.recycle()

        init()
    }

    private fun init() {
        if (!isInit) {
            isInit = true
            mShaderMatrix = Matrix()
            // ImageView的矩形区域
            mViewRect = RectF()
            // 边框的矩形区域
            mBorderRect = RectF()
            mBorderPaint = Paint(Paint.ANTI_ALIAS_FLAG)
            mBitmapPaint = Paint()
            mPath = Path()
            mBorderPaint.style = Paint.Style.STROKE
            mBorderPaint.strokeWidth = mBorderSize
            mBorderPaint.color = mBorderColor
            mBorderPaint.isAntiAlias = true
            mBitmapPaint.isAntiAlias = true
        }
    }

    /**
     * 对于普通的view,在执行到onDraw()时，背景图已绘制完成
     *
     *
     * 对于ViewGroup,当它没有背景时直接调用的是dispatchDraw()方法, 而绕过了draw()方法，
     * 当它有背景的时候就调用draw()方法，而draw()方法里包含了dispatchDraw()方法的调用，
     */
    @SuppressLint("DrawAllocation", "UseCompatLoadingForDrawables")
    public override fun onDraw(canvas: Canvas?) {
        if (isInEditMode) {
            if (mBitmap == null) {
                mBitmap =
                    getBitmapFromDrawable(
                        resources.getDrawable(
                            R.drawable.self_dialog_round_bg,
                            null
                        )
                    )
            }
        }

        if (mBitmap != null) {
            when (mShape) {
                ShapeType.SHAPE_CIRCLE.type -> {
                    canvas?.drawCircle(
                        mViewRect.right / 2, mViewRect.bottom / 2,
                        min(mViewRect.right, mViewRect.bottom) / 2, mBitmapPaint
                    )
                }
                ShapeType.SHAPE_OVAL.type -> {
                    canvas?.drawOval(mViewRect, mBitmapPaint)
                }
                else -> {
                    mPath.reset()
                    mPath.addRoundRect(
                        mViewRect, floatArrayOf(
                            mRoundRadiusLeftTop, mRoundRadiusLeftTop,
                            mRoundRadiusRightTop, mRoundRadiusRightTop,
                            mRoundRadiusRightBottom, mRoundRadiusRightBottom,
                            mRoundRadiusLeftBottom, mRoundRadiusLeftBottom
                        ), Path.Direction.CW
                    )
                    canvas?.drawPath(mPath, mBitmapPaint)
                }
            }
        }
        if (mBorderSize > 0) { // 绘制边框
            when (mShape) {
                ShapeType.SHAPE_CIRCLE.type -> {
                    canvas?.drawCircle(
                        mViewRect.right / 2, mViewRect.bottom / 2,
                        min(mViewRect.right, mViewRect.bottom) / 2 - mBorderSize / 2, mBorderPaint
                    )
                }
                ShapeType.SHAPE_OVAL.type -> {
                    canvas?.drawOval(mBorderRect, mBorderPaint)
                }
                else -> {
                    mPath.reset()
                    mPath.addRoundRect(
                        mBorderRect, floatArrayOf(
                            mRoundRadiusLeftTop, mRoundRadiusLeftTop,
                            mRoundRadiusRightTop, mRoundRadiusRightTop,
                            mRoundRadiusRightBottom, mRoundRadiusRightBottom,
                            mRoundRadiusLeftBottom, mRoundRadiusLeftBottom
                        ), Path.Direction.CW
                    )
                    canvas?.drawPath(mPath, mBorderPaint)
                }
            }
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        initRect()
        setupBitmapShader()
    }

    override fun setPadding(left: Int, top: Int, right: Int, bottom: Int) {
        super.setPadding(left, top, right, bottom)
        initRect()
        setupBitmapShader()
    }

    override fun setPaddingRelative(start: Int, top: Int, end: Int, bottom: Int) {
        super.setPaddingRelative(start, top, end, bottom)
        initRect()
        setupBitmapShader()
    }

    override fun setImageDrawable(drawable: Drawable?) {
        super.setImageDrawable(drawable)
        mBitmap = getBitmapFromDrawable(drawable)
        setupBitmapShader()
    }

    open fun getShape(): Int {
        return mShape
    }

    open fun setShape(shape: Int) {
        mShape = shape
    }

    open fun getBorderSize(): Float {
        return mBorderSize
    }

    open fun setBorderSize(mBorderSize: Int) {
        this.mBorderSize = mBorderSize.toFloat()
        mBorderPaint.strokeWidth = mBorderSize.toFloat()
        initRect()
        invalidate()
    }

    open fun getBorderColor(): Int {
        return mBorderColor
    }

    open fun setBorderColor(mBorderColor: Int) {
        this.mBorderColor = mBorderColor
        mBorderPaint.color = mBorderColor
        invalidate()
    }

    open fun getRoundRadius(): Float {
        return mRoundRadius
    }

    open fun setRoundRadius(mRoundRadius: Float) {
        this.mRoundRadius = mRoundRadius
        invalidate()
    }

    open fun setRoundRadius(
        roundRadiusLeftBottom: Float,
        roundRadiusLeftTop: Float,
        roundRadiusRightBottom: Float,
        roundRadiusRightTop: Float
    ) {
        mRoundRadiusLeftBottom = roundRadiusLeftBottom
        mRoundRadiusLeftTop = roundRadiusLeftTop
        mRoundRadiusRightBottom = roundRadiusRightBottom
        mRoundRadiusRightTop = roundRadiusRightTop
        invalidate()
    }

    open fun getRoundRadiusArray(): FloatArray {
        return floatArrayOf(
            mRoundRadiusLeftBottom,
            mRoundRadiusLeftTop,
            mRoundRadiusRightBottom,
            mRoundRadiusRightTop
        )
    }

    private fun getBitmapFromDrawable(drawable: Drawable?): Bitmap? {
        if (drawable == null) {
            return null
        }
        return if (drawable is BitmapDrawable) {
            (drawable as BitmapDrawable?)?.bitmap
        } else try {
            val bitmap: Bitmap = if (drawable is ColorDrawable) {
                Bitmap.createBitmap(2, 2, Bitmap.Config.ARGB_8888)
            } else {
                Bitmap.createBitmap(
                    drawable.intrinsicWidth,
                    drawable.intrinsicHeight,
                    Bitmap.Config.ARGB_8888
                )
            }
            val canvas = Canvas(bitmap)
            drawable.setBounds(0, 0, canvas.width, canvas.height)
            drawable.draw(canvas)
            bitmap
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun setupBitmapShader() {
        // super(context, attrs, defStyle)调用setImageDrawable时,成员变量还未被正确初始化
        if (mBitmap == null) {
            invalidate()
            return
        }
        if (!isInit) {
            init()
        }
        mBitmapShader = BitmapShader(mBitmap!!, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        mBitmapPaint.shader = mBitmapShader

        // 固定为CENTER_CROP,使图片在view中居中并裁剪
        mShaderMatrix.set(null)
        // 缩放到高或宽　与view的高或宽　匹配
        val scale = max(width * 1f / mBitmap!!.width, height * 1f / mBitmap!!.height)
        // 由于BitmapShader默认是从画布的左上角开始绘制，所以把其平移到画布中间，即居中
        val dx = (width - mBitmap!!.width * scale) / 2
        val dy = (height - mBitmap!!.height * scale) / 2
        mShaderMatrix.setScale(scale, scale)
        mShaderMatrix.postTranslate(dx, dy)
        mBitmapShader?.setLocalMatrix(mShaderMatrix)
        invalidate()
    }

    //　设置图片的绘制区域
    private fun initRect() {
        mViewRect.top = 0f
        mViewRect.left = 0f
        mViewRect.right = width.toFloat() // 宽度
        mViewRect.bottom = height.toFloat() // 高度

        // 边框的矩形区域不能等于ImageView的矩形区域，否则边框的宽度只显示了一半
        mBorderRect.top = mBorderSize / 2
        mBorderRect.left = mBorderSize / 2
        mBorderRect.right = width - mBorderSize / 2
        mBorderRect.bottom = height - mBorderSize / 2
    }

    enum class ShapeType(val type: Int) {
        SHAPE_REC(1), // 矩形
        SHAPE_CIRCLE(2), // 圆形
        SHAPE_OVAL(3) // 椭圆
    }
}