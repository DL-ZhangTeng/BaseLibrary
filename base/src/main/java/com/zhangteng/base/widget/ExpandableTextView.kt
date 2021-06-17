package com.zhangteng.base.widget

import android.animation.Animator
import android.animation.ValueAnimator
import android.annotation.TargetApi
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.text.TextUtils
import android.util.AttributeSet
import android.util.SparseBooleanArray
import android.view.*
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.zhangteng.base.R
import com.zhangteng.base.utils.DensityUtil

/**
 * des:可伸展textview
 * Created by xsf
 * on 2016.08.24
 */
open class ExpandableTextView : LinearLayout, View.OnClickListener {
    /*内容textview*/
    protected var mTvContent: TextView? = null

    /*展开收起textview*/
    protected var mTvExpandCollapse: TextView? = null

    /*是否有重新绘制*/
    private var mRelayout = false

    /*默认收起*/
    private var mCollapsed = true

    /*展开图片*/
    private var mExpandDrawable: Drawable? = null

    /*收起图片*/
    private var mCollapseDrawable: Drawable? = null

    /*动画执行时间*/
    private var mAnimationDuration = 0

    /*是否正在执行动画*/
    private var mAnimating = false

    /* 展开收起状态回调 */
    private var mListener: OnExpandStateChangeListener? = null

    /* listview等列表情况下保存每个item的收起/展开状态 */
    private var mCollapsedStatus: SparseBooleanArray? = null

    /* 列表位置 */
    private var mPosition = 0

    /*设置内容最大行数，超过隐藏*/
    private var mMaxCollapsedLines = 0

    /*这个linerlayout容器的高度*/
    private var mCollapsedHeight = 0

    /*内容tv真实高度（含padding）*/
    private var mTextHeightWithMaxLines = 0

    /*内容tvMarginTopAmndBottom高度*/
    private var mMarginBetweenTxtAndBottom = 0

    /*内容颜色*/
    private var contentTextColor = 0

    /*收起展开颜色*/
    private var collapseExpandTextColor = 0

    /*内容字体大小*/
    private var contentTextSize = 0f

    /*收起展字体大小*/
    private var collapseExpandTextSize = 0f

    /*收起文字*/
    private var textCollapse: String? = null

    /*展开文字*/
    private var textExpand: String? = null

    /*收起展开位置，默认左边*/
    private var grarity = 0

    /*收起展开图标位置，默认在右边*/
    private var drawableGrarity = 0

    @JvmOverloads
    constructor(context: Context?, attrs: AttributeSet? = null) : super(context, attrs) {
        init(attrs)
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    constructor(context: Context?, attrs: AttributeSet?, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        init(attrs)
    }

    override fun setOrientation(orientation: Int) {
        require(HORIZONTAL != orientation) { "ExpandableTextView only supports Vertical Orientation." }
        super.setOrientation(orientation)
    }

    /**
     * 初始化属性
     *
     * @param attrs
     */
    private fun init(attrs: AttributeSet?) {
        mCollapsedStatus = SparseBooleanArray()
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ExpandableTextView)
        mMaxCollapsedLines =
            typedArray.getInt(R.styleable.ExpandableTextView_maxCollapsedLines, MAX_COLLAPSED_LINES)
        mAnimationDuration =
            typedArray.getInt(R.styleable.ExpandableTextView_animDuration, DEFAULT_ANIM_DURATION)
        mExpandDrawable = typedArray.getDrawable(R.styleable.ExpandableTextView_expandDrawable)
        mCollapseDrawable = typedArray.getDrawable(R.styleable.ExpandableTextView_collapseDrawable)
        textCollapse = typedArray.getString(R.styleable.ExpandableTextView_textCollapse)
        textExpand = typedArray.getString(R.styleable.ExpandableTextView_textExpand)

//        if (mExpandDrawable == null) {
//            mExpandDrawable = ContextCompat.getDrawable(getContext(), R.drawable.icon_green_arrow_up);
//        }
//        if (mCollapseDrawable == null) {
//            mCollapseDrawable = ContextCompat.getDrawable(getContext(), R.drawable.icon_green_arrow_down);
//        }
        if (TextUtils.isEmpty(textCollapse)) {
            textCollapse = context.getString(R.string.collapse)
        }
        if (TextUtils.isEmpty(textExpand)) {
            textExpand = context.getString(R.string.expand)
        }
        contentTextColor = typedArray.getColor(
            R.styleable.ExpandableTextView_android_textColor,
            ContextCompat.getColor(context, R.color.default_text_grey_color)
        )
        contentTextSize = typedArray.getDimension(
            R.styleable.ExpandableTextView_android_textSize,
            DensityUtil.Companion.sp2px(context, 14f).toFloat()
        )
        collapseExpandTextColor = typedArray.getColor(
            R.styleable.ExpandableTextView_collapseExpandTextColor,
            ContextCompat.getColor(context, R.color.default_text_green_color)
        )
        collapseExpandTextSize = typedArray.getDimension(
            R.styleable.ExpandableTextView_collapseExpandTextSize,
            DensityUtil.Companion.sp2px(context, 14f).toFloat()
        )
        grarity =
            typedArray.getInt(R.styleable.ExpandableTextView_collapseExpandGrarity, Gravity.LEFT)
        drawableGrarity =
            typedArray.getInt(R.styleable.ExpandableTextView_drawableGrarity, Gravity.RIGHT)
        typedArray.recycle()
        // enforces vertical orientation
        orientation = VERTICAL
        // default visibility is gone
        visibility = GONE
    }

    /**
     * 渲染完成时初始化view
     */
    override fun onFinishInflate() {
        super.onFinishInflate()
        findViews()
    }

    /**
     * 初始化viwe
     */
    private fun findViews() {
        val inflater = context
            .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.item_expand_collapse, this)
        mTvContent = findViewById<View?>(R.id.expandable_text) as TextView
        mTvContent?.setOnClickListener(this)
        mTvExpandCollapse = findViewById<View?>(R.id.expand_collapse) as TextView
        setDrawbleAndText()
        mTvExpandCollapse?.setOnClickListener(this)
        mTvContent?.setTextColor(contentTextColor)
        mTvContent?.paint?.textSize = contentTextSize
        mTvExpandCollapse?.setTextColor(collapseExpandTextColor)
        mTvExpandCollapse?.paint?.textSize = collapseExpandTextSize

        //设置收起展开位置：左或者右
        val lp = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        lp.gravity = grarity
        mTvExpandCollapse?.layoutParams = lp
    }

    /**
     * 点击事件
     *
     * @param view
     */
    override fun onClick(view: View?) {
        if (mTvExpandCollapse?.visibility != VISIBLE) {
            return
        }
        mCollapsed = !mCollapsed
        //修改收起/展开图标、文字
        setDrawbleAndText()
        //保存位置状态
        mCollapsedStatus?.put(mPosition, mCollapsed)
        // 执行展开/收起动画
        mAnimating = true
        val valueAnimator: ValueAnimator? = if (mCollapsed) {
            ValueAnimator.ofInt(height, mCollapsedHeight)
        } else {
            ValueAnimator.ofInt(
                height, height +
                        mTextHeightWithMaxLines - mTvContent!!.height
            )
        }
        valueAnimator?.addUpdateListener { animator ->
            val animatedValue = animator.animatedValue as Int
            mTvContent?.maxHeight = animatedValue - mMarginBetweenTxtAndBottom
            layoutParams.height = animatedValue
            requestLayout()
        }
        valueAnimator?.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animator: Animator?) {}
            override fun onAnimationEnd(animator: Animator?) {
                // 动画结束后发送结束的信号
                /// clear the animation flag
                mAnimating = false
                // notify the listener
                mListener?.onExpandStateChanged(mTvContent, !mCollapsed)
            }

            override fun onAnimationCancel(animator: Animator?) {}
            override fun onAnimationRepeat(animator: Animator?) {}
        })
        valueAnimator?.duration = mAnimationDuration.toLong()
        valueAnimator?.start()
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        // 当动画还在执行状态时，拦截事件，不让child处理
        return mAnimating
    }

    /**
     * 重新测量
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        // If no change, measure and return
        if (!mRelayout || visibility == GONE) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
            return
        }
        mRelayout = false

        // Setup with optimistic case
        // i.e. Everything fits. No button needed
        mTvExpandCollapse?.visibility = GONE
        mTvContent?.maxLines = Int.MAX_VALUE

        // Measure
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        //如果内容真实行数小于等于最大行数，不处理
        if (mTvContent == null || mTvContent!!.lineCount <= mMaxCollapsedLines) {
            return
        }
        // 获取内容tv真实高度（含padding）
        mTextHeightWithMaxLines = getRealTextViewHeight(mTvContent!!)

        // 如果是收起状态，重新设置最大行数
        if (mCollapsed) {
            mTvContent?.maxLines = mMaxCollapsedLines
        }
        mTvExpandCollapse?.visibility = VISIBLE

        // Re-measure with new setup
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if (mCollapsed) {
            // Gets the margin between the TextView's bottom and the ViewGroup's bottom
            if (mTvContent != null)
                mTvContent!!.post { mMarginBetweenTxtAndBottom = height - mTvContent!!.height }
            // 保存这个容器的测量高度
            mCollapsedHeight = measuredHeight
        }
    }

    /**
     * 设置收起展开图标位置和文字
     */
    private fun setDrawbleAndText() {
        if (Gravity.START == drawableGrarity) {
            mTvExpandCollapse?.setCompoundDrawablesWithIntrinsicBounds(
                if (mCollapsed) mCollapseDrawable else mExpandDrawable,
                null,
                null,
                null
            )
        } else {
            mTvExpandCollapse?.setCompoundDrawablesWithIntrinsicBounds(
                null,
                null,
                if (mCollapsed) mCollapseDrawable else mExpandDrawable,
                null
            )
        }
        mTvExpandCollapse?.text =
            if (mCollapsed) resources.getString(R.string.expand) else resources.getString(R.string.collapse)
    }
    /*********暴露给外部调用方法 */
    /**
     * 设置收起/展开监听
     *
     * @param listener
     */
    open fun setOnExpandStateChangeListener(listener: OnExpandStateChangeListener?) {
        mListener = listener
    }

    /**
     * 设置内容，列表情况下，带有保存位置收起/展开状态
     *
     * @param text
     * @param position
     */
    open fun setText(text: CharSequence?, position: Int) {
        mPosition = position
        //获取状态，如无，默认是true:收起
        mCollapsed = mCollapsedStatus?.get(position, true) == true
        clearAnimation()
        //设置收起/展开图标和文字
        setDrawbleAndText()
        mTvExpandCollapse?.text =
            if (mCollapsed) resources.getString(R.string.expand) else resources.getString(R.string.collapse)
        setText(text)
        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
        requestLayout()
    }

    /**
     * 获取内容
     *
     * @return
     */
    open fun getText(): CharSequence? {
        return if (mTvContent == null) {
            ""
        } else mTvContent!!.text
    }

    /**
     * 设置内容
     *
     * @param text
     */
    open fun setText(text: CharSequence?) {
        mRelayout = true
        mTvContent?.text = text
        visibility = if (TextUtils.isEmpty(text)) GONE else VISIBLE
    }

    /**
     * 定义状态改变接口
     */
    interface OnExpandStateChangeListener {
        /**
         * @param textView   - TextView being expanded/collapsed
         * @param isExpanded - true if the TextView has been expanded
         */
        open fun onExpandStateChanged(textView: TextView?, isExpanded: Boolean)
    }

    companion object {
        /* 默认最高行数 */
        private const val MAX_COLLAPSED_LINES = 5

        /* 默认动画执行时间 */
        private const val DEFAULT_ANIM_DURATION = 200

        /**
         * 获取内容tv真实高度（含padding）
         *
         * @param textView
         * @return
         */
        private fun getRealTextViewHeight(textView: TextView): Int {
            val textHeight = textView.getLayout().getLineTop(textView.getLineCount())
            val padding = textView.getCompoundPaddingTop() + textView.getCompoundPaddingBottom()
            return textHeight + padding
        }
    }
}