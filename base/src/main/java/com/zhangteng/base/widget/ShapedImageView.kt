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

/**
 * Created by Swing on 2019/6/05.
 */
open class ShapedImageView @JvmOverloads constructor(context: Context, attrs: AttributeSet?, defStyle: Int = 0) : AppCompatImageView(context, attrs, defStyle) {
    private val mShaderMatrix: Matrix = Matrix()
    private var mBorderSize = 0f // 边框大小,默认为0，即无边框
    private var mBorderColor = Color.WHITE // 边框颜色，默认为白色
    private var mShape = SHAPE_CIRCLE // 形状，默认为直接矩形
    private var mRoundRadius = 0f // 矩形的圆角半径,默认为0，即直角矩形
    private var mRoundRadiusLeftTop = 0f
    private var mRoundRadiusLeftBottom = 0f
    private var mRoundRadiusRightTop = 0f
    private var mRoundRadiusRightBottom = 0f
    private val mBorderPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val mViewRect: RectF = RectF() // ImageView的矩形区域
    private val mBorderRect: RectF = RectF() // 边框的矩形区域
    private val mBitmapPaint: Paint = Paint()
    private var mBitmapShader: BitmapShader? = null
    private var mBitmap: Bitmap? = null
    private val mPath: Path = Path()
    override fun setImageResource(resId: Int) {
        super.setImageResource(resId)
        mBitmap = getBitmapFromDrawable(drawable)
        setupBitmapShader()
    }

    override fun setImageDrawable(drawable: Drawable?) {
        super.setImageDrawable(drawable)
        mBitmap = getBitmapFromDrawable(drawable)
        setupBitmapShader()
    }

    override fun setScaleType(scaleType: ScaleType?) {
        var scaleType = scaleType
        if (scaleType != ScaleType.CENTER_CROP) {
            scaleType = ScaleType.CENTER_CROP
        }
        super.setScaleType(scaleType)
    }

    private fun init(attrs: AttributeSet?) {
        @SuppressLint("CustomViewStyleable") val a = context.obtainStyledAttributes(attrs,
                R.styleable.ShapeImageView)
        mShape = a.getInt(R.styleable.ShapeImageView_siv_shape, mShape)
        mRoundRadius = a.getDimension(R.styleable.ShapeImageView_siv_corner_radius, mRoundRadius)
        mBorderSize = a.getDimension(R.styleable.ShapeImageView_siv_border_size, mBorderSize)
        mBorderColor = a.getColor(R.styleable.ShapeImageView_siv_border_color, mBorderColor)
        mRoundRadiusLeftBottom = a.getDimension(R.styleable.ShapeImageView_siv_round_radius_leftBottom, mRoundRadius)
        mRoundRadiusLeftTop = a.getDimension(R.styleable.ShapeImageView_siv_round_radius_leftTop, mRoundRadius)
        mRoundRadiusRightBottom = a.getDimension(R.styleable.ShapeImageView_siv_round_radius_rightBottom, mRoundRadius)
        mRoundRadiusRightTop = a.getDimension(R.styleable.ShapeImageView_siv_round_radius_rightTop, mRoundRadius)
        a.recycle()
    }

    /**
     * 对于普通的view,在执行到onDraw()时，背景图已绘制完成
     *
     *
     * 对于ViewGroup,当它没有背景时直接调用的是dispatchDraw()方法, 而绕过了draw()方法，
     * 当它有背景的时候就调用draw()方法，而draw()方法里包含了dispatchDraw()方法的调用，
     */
    @SuppressLint("DrawAllocation")
    public override fun onDraw(canvas: Canvas?) {
        if (mBitmap != null) {
            if (mShape == SHAPE_CIRCLE) {
                canvas?.drawCircle(mViewRect.right / 2, mViewRect.bottom / 2,
                        Math.min(mViewRect.right, mViewRect.bottom) / 2, mBitmapPaint)
            } else if (mShape == SHAPE_OVAL) {
                canvas?.drawOval(mViewRect, mBitmapPaint)
            } else {
//                canvas.drawRoundRect(mViewRect, mRoundRadius, mRoundRadius, mBitmapPaint);
                mPath.reset()
                mPath.addRoundRect(mViewRect, floatArrayOf(
                        mRoundRadiusLeftTop, mRoundRadiusLeftTop,
                        mRoundRadiusRightTop, mRoundRadiusRightTop,
                        mRoundRadiusRightBottom, mRoundRadiusRightBottom,
                        mRoundRadiusLeftBottom, mRoundRadiusLeftBottom), Path.Direction.CW)
                canvas?.drawPath(mPath, mBitmapPaint)
            }
        }
        if (mBorderSize > 0) { // 绘制边框
            if (mShape == SHAPE_CIRCLE) {
                canvas?.drawCircle(mViewRect.right / 2, mViewRect.bottom / 2,
                        Math.min(mViewRect.right, mViewRect.bottom) / 2 - mBorderSize / 2, mBorderPaint)
            } else if (mShape == SHAPE_OVAL) {
                canvas?.drawOval(mBorderRect, mBorderPaint)
            } else {
//                canvas.drawRoundRect(mBorderRect, mRoundRadius, mRoundRadius, mBorderPaint);
                mPath.reset()
                mPath.addRoundRect(mBorderRect, floatArrayOf(
                        mRoundRadiusLeftTop, mRoundRadiusLeftTop,
                        mRoundRadiusRightTop, mRoundRadiusRightTop,
                        mRoundRadiusRightBottom, mRoundRadiusRightBottom,
                        mRoundRadiusLeftBottom, mRoundRadiusLeftBottom), Path.Direction.CW)
                canvas?.drawPath(mPath, mBorderPaint)
            }
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        initRect()
        setupBitmapShader()
    }

    // 不能在onLayout()调用invalidate()，否则导致绘制异常。（setupBitmapShader()中调用了invalidate()）
    override fun onLayout(
            changed: Boolean, left: Int, top: Int, right: Int,
            bottom: Int,
    ) {
        super.onLayout(changed, left, top, right, bottom)
        //        initRect();
//        setupBitmapShader();
    }

    private fun setupBitmapShader() {
        // super(context, attrs, defStyle)调用setImageDrawable时,成员变量还未被正确初始化
        if (mBitmap == null) {
            invalidate()
            return
        }
        mBitmapShader = BitmapShader(mBitmap!!, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        mBitmapPaint.shader = mBitmapShader

        // 固定为CENTER_CROP,使图片在view中居中并裁剪
        mShaderMatrix.set(null)
        // 缩放到高或宽　与view的高或宽　匹配
        val scale = Math.max(width * 1f / mBitmap!!.width, height * 1f / mBitmap!!.height)
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

    fun getShape(): Int {
        return mShape
    }

    fun setShape(shape: Int) {
        mShape = shape
    }

    fun getBorderSize(): Float {
        return mBorderSize
    }

    fun setBorderSize(mBorderSize: Int) {
        this.mBorderSize = mBorderSize.toFloat()
        mBorderPaint.setStrokeWidth(mBorderSize.toFloat())
        initRect()
        invalidate()
    }

    fun getBorderColor(): Int {
        return mBorderColor
    }

    fun setBorderColor(mBorderColor: Int) {
        this.mBorderColor = mBorderColor
        mBorderPaint.setColor(mBorderColor)
        invalidate()
    }

    fun getRoundRadius(): Float {
        return mRoundRadius
    }

    fun setRoundRadius(mRoundRadius: Float) {
        this.mRoundRadius = mRoundRadius
        invalidate()
    }

    fun setRoundRadiis(roundRadiusLeftBottom: Float, roundRadiusLeftTop: Float, roundRadiusRightBottom: Float, roundRadiusRightTop: Float) {
        mRoundRadiusLeftBottom = roundRadiusLeftBottom
        mRoundRadiusLeftTop = roundRadiusLeftTop
        mRoundRadiusRightBottom = roundRadiusRightBottom
        mRoundRadiusRightTop = roundRadiusRightTop
        invalidate()
    }

    fun getRoundRadiis(): FloatArray {
        return floatArrayOf(mRoundRadiusLeftBottom, mRoundRadiusLeftTop, mRoundRadiusRightBottom, mRoundRadiusRightTop)
    }

    private fun getBitmapFromDrawable(drawable: Drawable?): Bitmap? {
        if (drawable == null) {
            return null
        }
        return if (drawable is BitmapDrawable) {
            (drawable as BitmapDrawable?)?.bitmap
        } else try {
            val bitmap: Bitmap = if (drawable is ColorDrawable) {
                Bitmap.createBitmap(COLORDRAWABLE_DIMENSION, COLORDRAWABLE_DIMENSION, BITMAP_CONFIG)
            } else {
                Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, BITMAP_CONFIG)
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

    companion object {
        private val BITMAP_CONFIG: Bitmap.Config = Bitmap.Config.ARGB_8888
        private const val COLORDRAWABLE_DIMENSION = 2
        var SHAPE_REC = 1 // 矩形
        var SHAPE_CIRCLE = 2 // 圆形
        var SHAPE_OVAL = 3 // 椭圆
    }

    init {
        init(attrs)
        mBorderPaint.setStyle(Paint.Style.STROKE)
        mBorderPaint.setStrokeWidth(mBorderSize)
        mBorderPaint.setColor(mBorderColor)
        mBorderPaint.setAntiAlias(true)
        mBitmapPaint.setAntiAlias(true)
        super.setScaleType(ScaleType.CENTER_CROP) // 固定为CENTER_CROP，其他不生效
    }
}