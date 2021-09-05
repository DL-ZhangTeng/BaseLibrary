package com.zhangteng.base.base

import android.animation.Animator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import android.view.ViewTreeObserver
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.SimpleOnPageChangeListener
import com.zhangteng.base.R
import com.zhangteng.base.adapter.ImagePreviewAdapter
import com.zhangteng.base.bean.PreviewImageInfo

/**
 * 图片预览
 */
class ImagePreviewActivity : Activity(), ViewTreeObserver.OnPreDrawListener {
    private var rootView: RelativeLayout? = null
    private var imagePreviewAdapter: ImagePreviewAdapter? = null
    private var imageInfo: List<PreviewImageInfo>? = null
    private var currentItem = 0
    private var imageHeight = 0
    private var imageWidth = 0
    private var screenWidth = 0
    private var screenHeight = 0

    @SuppressLint("StringFormatMatches")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preview)
        val viewPager = findViewById<View>(R.id.viewPager) as ViewPager
        val tv_pager = findViewById<View>(R.id.tv_pager) as TextView
        rootView = findViewById<View>(R.id.rootView) as RelativeLayout
        val metric = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(metric)
        screenWidth = metric.widthPixels
        screenHeight = metric.heightPixels
        val intent = intent
        imageInfo = intent.getSerializableExtra(IMAGE_INFO) as List<PreviewImageInfo>?
        currentItem = intent.getIntExtra(CURRENT_ITEM, 0)
        imagePreviewAdapter = ImagePreviewAdapter(this, imageInfo!!)
        viewPager.adapter = imagePreviewAdapter
        viewPager.currentItem = currentItem
        viewPager.viewTreeObserver.addOnPreDrawListener(this)
        viewPager.addOnPageChangeListener(object : SimpleOnPageChangeListener() {
            override fun onPageSelected(position: Int) {
                currentItem = position
                tv_pager.text =
                    String.format(getString(R.string.select), currentItem + 1, imageInfo!!.size)
            }
        })
        tv_pager.text = String.format(getString(R.string.select), currentItem + 1, imageInfo!!.size)
    }

    override fun onBackPressed() {
        finishActivityAnim()
    }

    /**
     * 绘制前开始动画
     */
    override fun onPreDraw(): Boolean {
        rootView!!.viewTreeObserver.removeOnPreDrawListener(this)
        val view = imagePreviewAdapter?.primaryItem
        val imageView = imagePreviewAdapter?.primaryImageView
        computeImageWidthAndHeight(imageView)
        val imageData = imageInfo!![currentItem]
        val vx = imageData.imageViewWidth * 1.0f / imageWidth
        val vy = imageData.imageViewHeight * 1.0f / imageHeight
        val valueAnimator = ValueAnimator.ofFloat(0f, 1.0f)
        valueAnimator.addUpdateListener { animation ->
            val duration = animation.duration
            val playTime = animation.currentPlayTime
            var fraction = if (duration > 0) playTime.toFloat() / duration else 1f
            if (fraction > 1) fraction = 1f
            view!!.translationX = evaluateInt(
                fraction,
                imageData.imageViewX + imageData.imageViewWidth / 2 - imageView!!.width / 2,
                0
            ).toFloat()
            view.translationY = evaluateInt(
                fraction,
                imageData.imageViewY + imageData.imageViewHeight / 2 - imageView!!.height / 2,
                0
            ).toFloat()
            view.scaleX = evaluateFloat(fraction, vx, 1)
            view.scaleY = evaluateFloat(fraction, vy, 1)
            view.alpha = fraction
            rootView!!.setBackgroundColor(evaluateArgb(fraction, Color.TRANSPARENT, Color.BLACK))
        }
        addIntoListener(valueAnimator)
        valueAnimator.duration = ANIMATE_DURATION.toLong()
        valueAnimator.start()
        return true
    }

    /**
     * activity的退场动画
     */
    fun finishActivityAnim() {
        val view = imagePreviewAdapter?.primaryItem
        val imageView = imagePreviewAdapter?.primaryImageView
        computeImageWidthAndHeight(imageView)
        val imageData = imageInfo!![currentItem]
        val vx = imageData.imageViewWidth * 1.0f / imageWidth
        val vy = imageData.imageViewHeight * 1.0f / imageHeight
        val valueAnimator = ValueAnimator.ofFloat(0f, 1.0f)
        valueAnimator.addUpdateListener { animation ->
            val duration = animation.duration
            val playTime = animation.currentPlayTime
            var fraction = if (duration > 0) playTime.toFloat() / duration else 1f
            if (fraction > 1) fraction = 1f
            view!!.translationX = evaluateInt(
                fraction,
                0,
                imageData.imageViewX + imageData.imageViewWidth / 2 - imageView!!.width / 2
            ).toFloat()
            view.translationY = evaluateInt(
                fraction,
                0,
                imageData.imageViewY + imageData.imageViewHeight / 2 - imageView!!.height / 2
            ).toFloat()
            view.scaleX = evaluateFloat(fraction, 1, vx)
            view.scaleY = evaluateFloat(fraction, 1, vy)
            view!!.alpha = 1 - fraction
            rootView!!.setBackgroundColor(evaluateArgb(fraction, Color.BLACK, Color.TRANSPARENT))
        }
        addOutListener(valueAnimator)
        valueAnimator.duration = ANIMATE_DURATION.toLong()
        valueAnimator.start()
    }

    /**
     * 计算图片的宽高
     */
    private fun computeImageWidthAndHeight(imageView: ImageView?) {

        // 获取真实大小
        val drawable = imageView!!.drawable
        val intrinsicHeight = drawable.intrinsicHeight
        val intrinsicWidth = drawable.intrinsicWidth
        // 计算出与屏幕的比例，用于比较以宽的比例为准还是高的比例为准，因为很多时候不是高度没充满，就是宽度没充满
        var h = screenHeight * 1.0f / intrinsicHeight
        var w = screenWidth * 1.0f / intrinsicWidth
        if (h > w) h = w else w = h

        // 得出当宽高至少有一个充满的时候图片对应的宽高
        imageHeight = (intrinsicHeight * h).toInt()
        imageWidth = (intrinsicWidth * w).toInt()
    }

    /**
     * 进场动画过程监听
     */
    private fun addIntoListener(valueAnimator: ValueAnimator) {
        valueAnimator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {
                rootView!!.setBackgroundColor(0x0)
            }

            override fun onAnimationEnd(animation: Animator) {}
            override fun onAnimationCancel(animation: Animator) {}
            override fun onAnimationRepeat(animation: Animator) {}
        })
    }

    /**
     * 退场动画过程监听
     */
    private fun addOutListener(valueAnimator: ValueAnimator) {
        valueAnimator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {
                rootView!!.setBackgroundColor(0x0)
            }

            override fun onAnimationEnd(animation: Animator) {
                finish()
                overridePendingTransition(0, 0)
            }

            override fun onAnimationCancel(animation: Animator) {}
            override fun onAnimationRepeat(animation: Animator) {}
        })
    }

    /**
     * Integer 估值器
     */
    fun evaluateInt(fraction: Float, startValue: Int, endValue: Int): Int {
        return (startValue + fraction * (endValue - startValue)).toInt()
    }

    /**
     * Float 估值器
     */
    fun evaluateFloat(fraction: Float, startValue: Number, endValue: Number): Float {
        val startFloat = startValue.toFloat()
        return startFloat + fraction * (endValue.toFloat() - startFloat)
    }

    /**
     * Argb 估值器
     */
    fun evaluateArgb(fraction: Float, startValue: Int, endValue: Int): Int {
        val startA = startValue shr 24 and 0xff
        val startR = startValue shr 16 and 0xff
        val startG = startValue shr 8 and 0xff
        val startB = startValue and 0xff
        val endA = endValue shr 24 and 0xff
        val endR = endValue shr 16 and 0xff
        val endG = endValue shr 8 and 0xff
        val endB = endValue and 0xff
        return ((startA + (fraction * (endA - startA)).toInt()) shl 24) or
                ((startR + (fraction * (endR - startR)).toInt()) shl 16) or
                ((startG + (fraction * (endG - startG)).toInt()) shl 8) or
                (startB + (fraction * (endB - startB)).toInt())
    }

    companion object {
        const val IMAGE_INFO = "IMAGE_INFO"
        const val CURRENT_ITEM = "CURRENT_ITEM"
        const val ANIMATE_DURATION = 200
    }
}