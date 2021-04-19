package com.zhangteng.base.widget

import android.content.*
import android.graphics.*
import android.os.Build
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.*
import android.view.*
import android.view.View.OnFocusChangeListener
import android.view.inputmethod.EditorInfo
import android.widget.*
import android.widget.ImageView.ScaleType
import android.widget.TextView.OnEditorActionListener
import com.zhangteng.base.R
import com.zhangteng.base.utils.DensityUtil

/**
 * 通用标题栏
 *
 *
 * <declare-styleable name="GCommonTitleBar">
 * <attr name="titleBarBg" format="reference"></attr>
 * <attr name="titleBarHeight" format="dimension"></attr>
 * <attr name="showBottomLine" format="boolean"></attr>
 * <attr name="bottomLineColor" format="color"></attr>
 * <attr name="bottomShadowHeight" format="dimension"></attr>
 *
 * <attr name="statusBarMode" format="enum">
 * <enum name="dark" value="0"></enum>
 * <enum name="light" value="1"></enum>
</attr> *
 *
 *
 *
 * <attr name="leftType">
 * <enum name="none" value="0"></enum>
 * <enum name="textView" value="1"></enum>
 * <enum name="imageButton" value="2"></enum>
 * <enum name="customView" value="3"></enum>
</attr> *
 *
 *
 * <attr name="leftText" format="string"></attr>
 * <attr name="leftTextColor" format="color"></attr>
 * <attr name="leftTextSize" format="dimension"></attr>
 * <attr name="leftDrawable" format="reference"></attr>
 * <attr name="leftDrawablePadding" format="dimension"></attr>
 * <attr name="leftImageResource" format="reference"></attr>
 * <attr name="leftCustomView" format="reference"></attr>
 *
 *
 *
 * <attr name="rightType">
 * <enum name="none" value="0"></enum>
 * <enum name="textView" value="1"></enum>
 * <enum name="imageButton" value="2"></enum>
 * <enum name="customView" value="3"></enum>
</attr> *
 *
 *
 * <attr name="rightText" format="string"></attr>
 * <attr name="rightTextColor" format="color"></attr>
 * <attr name="rightTextSize" format="dimension"></attr>
 * <attr name="rightImageResource" format="reference"></attr>
 * <attr name="rightCustomView" format="reference"></attr>
 *
 *
 *
 * <enum name="none" value="0"></enum>
 * <enum name="textView" value="1"></enum>
 * <enum name="searchView" value="2"></enum>
 * <enum name="customView" value="3"></enum>
 *
 *
 *
 * <attr name="centerText" format="string"></attr>
 * <attr name="centerTextColor" format="color"></attr>
 * <attr name="centerTextSize" format="dimension"></attr>
 * <attr name="centerTextMarquee" format="boolean"></attr>
 * <attr name="centerSubText" format="string"></attr>
 * <attr name="centerSubTextColor" format="color"></attr>
 * <attr name="centerSubTextSize" format="dimension"></attr>
 * <attr name="centerSearchEdiable" format="boolean"></attr>
 * <attr name="centerCustomView" format="reference"></attr>
</declare-styleable> *
 *
 *
 */
open class CommonTitleBar(context: Context, attrs: AttributeSet?) : RelativeLayout(context, attrs), View.OnClickListener {
    private var rlMain // 主视图
            : RelativeLayout? = null
    private var tvLeft // 左边TextView
            : TextView? = null
    private var btnLeft // 左边ImageButton
            : ImageButton? = null
    private var viewCustomLeft: View? = null
    private var tvRight // 右边TextView
            : TextView? = null
    private var btnRight // 右边ImageButton
            : ImageButton? = null
    private var viewCustomRight: View? = null
    private var llMainCenter: LinearLayout? = null
    private var tvCenter // 标题栏文字
            : TextView? = null
    private val tvCenterSub // 副标题栏文字
            : TextView? = null

    //    private ProgressBar progressCenter;                 // 中间进度条,默认隐藏
    private var rlMainCenterSearch // 中间搜索框布局
            : RelativeLayout? = null
    private var etSearchHint: EditText? = null
    private var ivSearch: ImageView? = null
    private var ivVoice: ImageView? = null
    private var centerCustomView // 中间自定义视图
            : View? = null
    private var titleBarColor // 标题栏背景颜色
            = 0
    private var titleBarHeight // 标题栏高度
            = 0

    //    private boolean showBottomLine;                     // 是否显示底部分割线
    //    private int bottomLineColor;                        // 分割线颜色
    //    private float bottomShadowHeight;                   // 底部阴影高度
    private var leftType // 左边视图类型
            = 0
    private var leftText // 左边TextView文字
            : String? = null
    private var leftTextColor // 左边TextView颜色
            = 0
    private var leftTextSize // 左边TextView文字大小
            = 0f
    private var leftDrawable // 左边TextView drawableLeft资源
            = 0
    private var leftDrawablePadding // 左边TextView drawablePadding
            = 0f
    private var leftImageResource // 左边图片资源
            = 0
    private var leftCustomViewRes // 左边自定义视图布局资源
            = 0
    private var rightType // 右边视图类型
            = 0
    private var rightText // 右边TextView文字
            : String? = null
    private var rightTextColor // 右边TextView颜色
            = 0
    private var rightTextSize // 右边TextView文字大小
            = 0f
    private var rightImageResource // 右边图片资源
            = 0
    private var rightCustomViewRes // 右边自定义视图布局资源
            = 0
    private var centerType // 中间视图类型
            = 0
    private var centerText // 中间TextView文字
            : String? = null
    private var centerTextColor // 中间TextView字体颜色
            = 0
    private var centerTextSize // 中间TextView字体大小
            = 0f
    private var centerTextMarquee // 中间TextView字体是否显示跑马灯效果
            = false
    private var centerSubText // 中间subTextView文字
            : String? = null
    private var centerSubTextColor // 中间subTextView字体颜色
            = 0
    private var centerSubTextSize // 中间subTextView字体大小
            = 0f
    private var centerSearchEditable // 搜索框是否可输入
            = false
    private var centerSearchBgResource // 搜索框背景图片
            = 0
    private var centerSearchRightType // 搜索框右边按钮类型  0: voice 1: delete
            = 0
    private var centerCustomViewRes // 中间自定义布局资源
            = 0
    private var PADDING_10 = 0
    private var PADDING_16 = 0
    private var listener: OnTitleBarListener? = null
    private var doubleClickListener: OnTitleBarDoubleClickListener? = null
    private fun loadAttributes(context: Context, attrs: AttributeSet?) {
        PADDING_10 = DensityUtil.Companion.dp2px(context, 10f)
        PADDING_16 = DensityUtil.Companion.dp2px(context, 16f)
        val array = context.obtainStyledAttributes(attrs, R.styleable.CommonTitleBar)
        titleBarColor = array.getColor(R.styleable.CommonTitleBar_titleBarColor, resources.getColor(R.color.titlebar_bg))
        titleBarHeight = array.getDimension(R.styleable.CommonTitleBar_titleBarHeight, resources.getDimensionPixelSize(R.dimen.titlebar_height).toFloat()).toInt()

//        showBottomLine = array.getBoolean(R.styleable.CommonTitleBar_showBottomLine, true);
//        bottomLineColor = array.getColor(R.styleable.CommonTitleBar_bottomLineColor, Color.parseColor("#dddddd"));
//        bottomShadowHeight = array.getDimension(R.styleable.CommonTitleBar_bottomShadowHeight, DensityUtil.dip2px(0,context));
        leftType = array.getInt(R.styleable.CommonTitleBar_leftType, TYPE_LEFT_NONE)
        if (leftType == TYPE_LEFT_TEXTVIEW) {
            leftText = array.getString(R.styleable.CommonTitleBar_leftText)
            leftTextColor = array.getColor(R.styleable.CommonTitleBar_leftTextColor, resources.getColor(R.color.titlebar_text_color))
            leftTextSize = array.getDimension(R.styleable.CommonTitleBar_leftTextSize, DensityUtil.Companion.dp2px(context, 14f).toFloat())
            leftDrawable = array.getResourceId(R.styleable.CommonTitleBar_leftDrawable, 0)
            leftDrawablePadding = array.getDimension(R.styleable.CommonTitleBar_leftDrawablePadding, 0f)
        } else if (leftType == TYPE_LEFT_IMAGEBUTTON) {
            leftImageResource = array.getResourceId(R.styleable.CommonTitleBar_leftImageResource, R.drawable.comm_titlebar_reback_selector)
        } else if (leftType == TYPE_LEFT_CUSTOM_VIEW) {
            leftCustomViewRes = array.getResourceId(R.styleable.CommonTitleBar_leftCustomView, 0)
        }
        rightType = array.getInt(R.styleable.CommonTitleBar_rightType, TYPE_RIGHT_NONE)
        if (rightType == TYPE_RIGHT_TEXTVIEW) {
            rightText = array.getString(R.styleable.CommonTitleBar_rightText)
            rightTextColor = array.getColor(R.styleable.CommonTitleBar_rightTextColor, resources.getColor(R.color.titlebar_text_color))
            rightTextSize = array.getDimension(R.styleable.CommonTitleBar_rightTextSize, resources.getDimensionPixelSize(R.dimen.titlebar_text_size).toFloat())
        } else if (rightType == TYPE_RIGHT_IMAGEBUTTON) {
            rightImageResource = array.getResourceId(R.styleable.CommonTitleBar_rightImageResource, 0)
        } else if (rightType == TYPE_RIGHT_CUSTOM_VIEW) {
            rightCustomViewRes = array.getResourceId(R.styleable.CommonTitleBar_rightCustomView, 0)
        }
        centerType = array.getInt(R.styleable.CommonTitleBar_centerType, TYPE_CENTER_NONE)
        if (centerType == TYPE_CENTER_TEXTVIEW) {
            centerText = array.getString(R.styleable.CommonTitleBar_centerText)
            centerTextColor = array.getColor(R.styleable.CommonTitleBar_centerTextColor, resources.getColor(R.color.titlebar_text_color))
            centerTextSize = array.getDimension(R.styleable.CommonTitleBar_centerTextSize, resources.getDimensionPixelSize(R.dimen.titlebar_title_size).toFloat())
            centerTextMarquee = array.getBoolean(R.styleable.CommonTitleBar_centerTextMarquee, false)
            centerSubText = array.getString(R.styleable.CommonTitleBar_centerSubText)
            centerSubTextColor = array.getColor(R.styleable.CommonTitleBar_centerSubTextColor, Color.parseColor("#666666"))
            centerSubTextSize = array.getDimension(R.styleable.CommonTitleBar_centerSubTextSize, DensityUtil.Companion.dp2px(context, 11f).toFloat())
        } else if (centerType == TYPE_CENTER_SEARCHVIEW) {
            centerSearchEditable = array.getBoolean(R.styleable.CommonTitleBar_centerSearchEditable, true)
            centerSearchBgResource = array.getResourceId(R.styleable.CommonTitleBar_centerSearchBg, R.drawable.comm_titlebar_search_gray_shape)
            centerSearchRightType = array.getInt(R.styleable.CommonTitleBar_centerSearchRightType, TYPE_CENTER_SEARCH_RIGHT_VOICE)
        } else if (centerType == TYPE_CENTER_CUSTOM_VIEW) {
            centerCustomViewRes = array.getResourceId(R.styleable.CommonTitleBar_centerCustomView, 0)
        }
        array.recycle()
    }

    private val MATCH_PARENT = ViewGroup.LayoutParams.MATCH_PARENT
    private val WRAP_CONTENT = ViewGroup.LayoutParams.WRAP_CONTENT

    /**
     * 初始化全局视图
     *
     * @param context
     */
    private fun initGlobalViews(context: Context?) {
        setBackgroundColor(titleBarColor)
        val globalParams = ViewGroup.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
        layoutParams = globalParams

        // 构建主视图
        rlMain = RelativeLayout(context)
        rlMain?.setBackgroundColor(titleBarColor)
        val mainParams = LayoutParams(MATCH_PARENT, titleBarHeight)
        mainParams.addRule(ALIGN_PARENT_TOP)
        // 计算主布局高度
//        if (showBottomLine) {
//            mainParams.height = titleBarHeight - Math.max(1, DensityUtil.dip2px(0.4,context));
//        } else {
        mainParams.height = titleBarHeight
        //        }
        addView(rlMain, mainParams)
    }

    /**
     * 初始化主视图
     *
     * @param context
     */
    private fun initMainViews(context: Context?) {
        if (leftType != TYPE_LEFT_NONE) {
            initMainLeftViews(context)
        }
        if (rightType != TYPE_RIGHT_NONE) {
            initMainRightViews(context)
        }
        if (centerType != TYPE_CENTER_NONE) {
            initMainCenterViews(context)
        }
    }

    /**
     * 初始化主视图左边部分
     *
     * @param context
     */
    private fun initMainLeftViews(context: Context?) {
        val leftInnerParams = LayoutParams(WRAP_CONTENT, MATCH_PARENT)
        leftInnerParams.addRule(ALIGN_PARENT_LEFT)
        leftInnerParams.addRule(CENTER_VERTICAL)
        if (leftType == TYPE_LEFT_TEXTVIEW) {
            // 初始化左边TextView
            tvLeft = TextView(context)
            tvLeft?.apply {
                text = leftText
                setTextColor(leftTextColor)
                setTextSize(TypedValue.COMPLEX_UNIT_PX, leftTextSize)
                gravity = Gravity.START or Gravity.CENTER_VERTICAL
                isSingleLine = true
                setOnClickListener(this@CommonTitleBar)
                // 设置DrawableLeft及DrawablePadding
                if (leftDrawable != 0) {
                    compoundDrawablePadding = leftDrawablePadding.toInt()
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                        setCompoundDrawablesRelativeWithIntrinsicBounds(leftDrawable, 0, 0, 0)
                    } else {
                        setCompoundDrawablesWithIntrinsicBounds(leftDrawable, 0, 0, 0)
                    }
                }
                setPadding(PADDING_16, 0, PADDING_16, 0)
            }
            rlMain?.addView(tvLeft, leftInnerParams)
        } else if (leftType == TYPE_LEFT_IMAGEBUTTON) {
            // 初始化左边ImageButton
            btnLeft = ImageButton(context)
            btnLeft?.apply {
                setBackgroundColor(Color.TRANSPARENT)
                setImageResource(leftImageResource)
                setPadding(PADDING_10, 0, PADDING_10, 0)
                setOnClickListener(this@CommonTitleBar)
            }
            rlMain?.addView(btnLeft, leftInnerParams)
        } else if (leftType == TYPE_LEFT_CUSTOM_VIEW) {
            // 初始化自定义View
            viewCustomLeft = LayoutInflater.from(context).inflate(leftCustomViewRes, rlMain, false)
            rlMain?.addView(viewCustomLeft, leftInnerParams)
        }
    }

    /**
     * 初始化主视图右边部分
     *
     * @param context
     */
    private fun initMainRightViews(context: Context?) {
        val rightInnerParams = LayoutParams(WRAP_CONTENT, MATCH_PARENT)
        rightInnerParams.addRule(ALIGN_PARENT_RIGHT)
        rightInnerParams.addRule(CENTER_VERTICAL)
        if (rightType == TYPE_RIGHT_TEXTVIEW) {
            // 初始化右边TextView
            tvRight = TextView(context)
            tvRight?.apply {
                text = rightText
                setTextColor(rightTextColor)
                setTextSize(TypedValue.COMPLEX_UNIT_PX, rightTextSize)
                gravity = Gravity.END or Gravity.CENTER_VERTICAL
                isSingleLine = true
                setPadding(PADDING_16, 0, PADDING_16, 0)
                setOnClickListener(this@CommonTitleBar)
            }
            rlMain?.addView(tvRight, rightInnerParams)
        } else if (rightType == TYPE_RIGHT_IMAGEBUTTON) {
            // 初始化右边ImageBtn
            btnRight = ImageButton(context)
            btnRight?.apply {
                setImageResource(rightImageResource)
                setBackgroundColor(Color.TRANSPARENT)
                scaleType = ScaleType.CENTER_INSIDE
                setPadding(PADDING_10, 0, PADDING_10, 0)
                setOnClickListener(this@CommonTitleBar)
            }
            rlMain?.addView(btnRight, rightInnerParams)
        } else if (rightType == TYPE_RIGHT_CUSTOM_VIEW) {
            // 初始化自定义view
            viewCustomRight = LayoutInflater.from(context).inflate(rightCustomViewRes, rlMain, false)
            rlMain?.addView(viewCustomRight, rightInnerParams)
        }
    }

    /**
     * 初始化主视图中间部分
     *
     * @param context
     */
    private fun initMainCenterViews(context: Context?) {
        if (centerType == TYPE_CENTER_TEXTVIEW) {
            // 初始化中间子布局
            llMainCenter = LinearLayout(context)
            llMainCenter?.apply {
                gravity = Gravity.CENTER
                orientation = LinearLayout.VERTICAL
                setOnClickListener(this@CommonTitleBar)
            }
            val centerParams = LayoutParams(WRAP_CONTENT, MATCH_PARENT)
            centerParams.addRule(CENTER_IN_PARENT)
            rlMain?.addView(llMainCenter, centerParams)

            // 初始化标题栏TextView
            tvCenter = TextView(context)
            tvCenter?.apply {
                text = centerText
                setTextColor(centerTextColor)
                setTextSize(TypedValue.COMPLEX_UNIT_PX, centerTextSize)
                gravity = Gravity.CENTER
                isSingleLine = true
                setTypeface(Typeface.defaultFromStyle(Typeface.BOLD))
                // 设置跑马灯效果
                maxWidth = (DensityUtil.Companion.getScreenWidth(context) * 3 / 5.0).toInt()
                if (centerTextMarquee) {
                    ellipsize = TextUtils.TruncateAt.MARQUEE
                    marqueeRepeatLimit = -1
                    requestFocus()
                    isSelected = true
                }
            }
            val centerTextParams = LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
            llMainCenter?.addView(tvCenter, centerTextParams)
        } else if (centerType == TYPE_CENTER_SEARCHVIEW) {
            // 初始化通用搜索框
            rlMainCenterSearch = RelativeLayout(context)
            rlMainCenterSearch?.setBackgroundResource(centerSearchBgResource)
            val centerParams = LayoutParams(MATCH_PARENT, MATCH_PARENT)
            // 设置边距
            centerParams.topMargin = DensityUtil.Companion.dp2px(context, 7f)
            centerParams.bottomMargin = DensityUtil.Companion.dp2px(context, 7f)
            // 根据左边的布局类型来设置边距,布局依赖规则
            if (leftType == TYPE_LEFT_TEXTVIEW) {
                if (tvLeft != null)
                    centerParams.addRule(RIGHT_OF, tvLeft!!.getId())
            } else if (leftType == TYPE_LEFT_IMAGEBUTTON) {
                if (btnLeft != null)
                    centerParams.addRule(RIGHT_OF, btnLeft!!.getId())
            } else if (leftType == TYPE_LEFT_CUSTOM_VIEW) {
                if (viewCustomLeft != null)
                    centerParams.addRule(RIGHT_OF, viewCustomLeft!!.getId())
            }
            // 根据右边的布局类型来设置边距,布局依赖规则
            if (rightType == TYPE_RIGHT_TEXTVIEW) {
                if (tvRight != null)
                    centerParams.addRule(LEFT_OF, tvRight!!.getId())
            } else if (rightType == TYPE_RIGHT_IMAGEBUTTON) {
                if (btnRight != null)
                    centerParams.addRule(LEFT_OF, btnRight!!.getId())
            } else if (rightType == TYPE_RIGHT_CUSTOM_VIEW) {
                if (viewCustomRight != null)
                    centerParams.addRule(LEFT_OF, viewCustomRight!!.getId())
            }
            rlMain?.addView(rlMainCenterSearch, centerParams)

            // 初始化搜索框搜索ImageView
            ivSearch = ImageView(context)
            ivSearch?.setOnClickListener(this)
            val searchIconWidth: Int = DensityUtil.Companion.dp2px(context, 15f)
            val searchParams = LayoutParams(searchIconWidth, searchIconWidth)
            searchParams.addRule(CENTER_VERTICAL)
            searchParams.addRule(ALIGN_PARENT_LEFT)
            //            searchParams.leftMargin = PADDING_12;
            rlMainCenterSearch?.addView(ivSearch, searchParams)
            ivSearch?.setImageResource(R.drawable.comm_titlebar_search_normal)

            // 初始化搜索框语音ImageView
            ivVoice = ImageView(context)
            ivVoice?.setOnClickListener(this)
            val voiceParams = LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
            voiceParams.addRule(CENTER_VERTICAL)
            voiceParams.addRule(ALIGN_PARENT_RIGHT)
            //            voiceParams.rightMargin = PADDING_12;
            rlMainCenterSearch?.addView(ivVoice, voiceParams)
            if (centerSearchRightType == TYPE_CENTER_SEARCH_RIGHT_VOICE) {
                ivVoice?.setImageResource(R.drawable.comm_titlebar_voice)
            } else {
                ivVoice?.setImageResource(R.drawable.comm_titlebar_delete_normal)
                ivVoice?.setVisibility(GONE)
            }

            // 初始化文字输入框
            etSearchHint = EditText(context)
            etSearchHint?.apply {
                setBackgroundColor(Color.TRANSPARENT)
                gravity = Gravity.LEFT or Gravity.CENTER_VERTICAL
                hint = resources.getString(R.string.titlebar_search_hint)
                setTextColor(Color.parseColor("#666666"))
                setHintTextColor(Color.parseColor("#999999"))
                setTextSize(TypedValue.COMPLEX_UNIT_PX, DensityUtil.Companion.dp2px(context, 14f).toFloat())
                //            etSearchHint.setPadding(PADDING_5, 0, PADDING_5, 0);
                if (!centerSearchEditable) {
                    isCursorVisible = false
                    clearFocus()
                    isFocusable = false
                    setOnClickListener(this@CommonTitleBar)
                }
                isCursorVisible = false
                isSingleLine = true
                ellipsize = TextUtils.TruncateAt.END
                imeOptions = EditorInfo.IME_ACTION_SEARCH
                addTextChangedListener(centerSearchWatcher)
                onFocusChangeListener = focusChangeListener
                setOnEditorActionListener(editorActionListener)
                setOnClickListener { isCursorVisible = true }
            }
            val searchHintParams = LayoutParams(MATCH_PARENT, MATCH_PARENT)
            if (ivSearch != null)
                searchHintParams.addRule(RIGHT_OF, ivSearch!!.id)
            if (ivVoice != null)
                searchHintParams.addRule(LEFT_OF, ivVoice!!.id)
            searchHintParams.addRule(CENTER_VERTICAL)
            rlMainCenterSearch?.addView(etSearchHint, searchHintParams)
        } else if (centerType == TYPE_CENTER_CUSTOM_VIEW) {
            // 初始化中间自定义布局
            centerCustomView = LayoutInflater.from(context).inflate(centerCustomViewRes, rlMain, false)
            val centerCustomParams = LayoutParams(WRAP_CONTENT, MATCH_PARENT)
            centerCustomParams.addRule(CENTER_IN_PARENT)
            rlMain?.addView(centerCustomView, centerCustomParams)
        }
    }

    private val centerSearchWatcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        override fun afterTextChanged(s: Editable?) {
            if (centerSearchRightType == TYPE_CENTER_SEARCH_RIGHT_VOICE) {
                if (TextUtils.isEmpty(s)) {
                    ivVoice?.setImageResource(R.drawable.comm_titlebar_voice)
                } else {
                    ivVoice?.setImageResource(R.drawable.comm_titlebar_delete_normal)
                }
            } else {
                if (TextUtils.isEmpty(s)) {
                    ivVoice?.visibility = GONE
                } else {
                    ivVoice?.visibility = VISIBLE
                }
            }
        }
    }
    private val focusChangeListener: OnFocusChangeListener = OnFocusChangeListener { _, hasFocus ->
        if (centerSearchRightType == TYPE_CENTER_SEARCH_RIGHT_DELETE) {
            val input = etSearchHint?.text.toString()
            if (hasFocus && !TextUtils.isEmpty(input)) {
                ivVoice?.setVisibility(VISIBLE)
            } else {
                ivVoice?.setVisibility(GONE)
            }
        }
    }
    private val editorActionListener: OnEditorActionListener = OnEditorActionListener { v, actionId, event ->
        if (listener != null && actionId == EditorInfo.IME_ACTION_SEARCH) {
            listener!!.onClicked(v, ACTION_SEARCH_SUBMIT, etSearchHint?.text.toString())
        }
        false
    }
    private var lastClickMillis: Long = 0 // 双击事件中，上次被点击时间
    override fun onClick(v: View?) {
        if (listener == null) return
        if (v == llMainCenter && doubleClickListener != null) {
            val currentClickMillis = System.currentTimeMillis()
            if (currentClickMillis - lastClickMillis < 500) {
                doubleClickListener?.onClicked(v)
            }
            lastClickMillis = currentClickMillis
        } else if (v == tvLeft) {
            listener!!.onClicked(v, ACTION_LEFT_TEXT, null)
        } else if (v == btnLeft) {
            listener!!.onClicked(v, ACTION_LEFT_BUTTON, null)
        } else if (v == tvRight) {
            listener!!.onClicked(v, ACTION_RIGHT_TEXT, null)
        } else if (v == btnRight) {
            listener!!.onClicked(v, ACTION_RIGHT_BUTTON, null)
        } else if (v == etSearchHint || v == ivSearch) {
            listener!!.onClicked(v, ACTION_SEARCH, null)
        } else if (v == ivVoice) {
            etSearchHint?.setText("")
            if (centerSearchRightType == TYPE_CENTER_SEARCH_RIGHT_VOICE) {
                // 语音按钮被点击
                listener!!.onClicked(v, ACTION_SEARCH_VOICE, null)
            } else {
                listener!!.onClicked(v, ACTION_SEARCH_DELETE, null)
            }
        } else if (v == tvCenter) {
            listener!!.onClicked(v, ACTION_CENTER_TEXT, null)
        }
    }

    /**
     * 设置背景图片
     *
     * @param resource
     */
    override fun setBackgroundResource(resource: Int) {
        setBackgroundColor(Color.TRANSPARENT)
        super.setBackgroundResource(resource)
    }

    /**
     * 获取标题栏左边TextView，对应leftType = textView
     *
     * @return
     */
    fun getLeftTextView(): TextView? {
        return tvLeft
    }

    /**
     * 获取标题栏左边ImageButton，对应leftType = imageButton
     *
     * @return
     */
    fun getLeftImageButton(): ImageButton? {
        return btnLeft
    }

    /**
     * 获取标题栏右边TextView，对应rightType = textView
     *
     * @return
     */
    fun getRightTextView(): TextView? {
        return tvRight
    }

    /**
     * 获取标题栏右边ImageButton，对应rightType = imageButton
     *
     * @return
     */
    fun getRightImageButton(): ImageButton? {
        return btnRight
    }

    fun getCenterLayout(): LinearLayout? {
        return llMainCenter
    }

    /**
     * 获取标题栏中间TextView，对应centerType = textView
     *
     * @return
     */
    fun getCenterTextView(): TextView? {
        return tvCenter
    }

    fun getCenterSubTextView(): TextView? {
        return tvCenterSub
    }

    /**
     * 获取搜索框布局，对应centerType = searchView
     *
     * @return
     */
    fun getCenterSearchView(): RelativeLayout? {
        return rlMainCenterSearch
    }

    /**
     * 获取搜索框内部输入框，对应centerType = searchView
     *
     * @return
     */
    fun getCenterSearchEditText(): EditText? {
        return etSearchHint
    }

    /**
     * 获取搜索框右边图标ImageView，对应centerType = searchView
     *
     * @return
     */
    fun getCenterSearchRightImageView(): ImageView? {
        return ivVoice
    }

    fun getCenterSearchLeftImageView(): ImageView? {
        return ivSearch
    }

    /**
     * 获取左边自定义布局
     *
     * @return
     */
    fun getLeftCustomView(): View? {
        return viewCustomLeft
    }

    /**
     * 获取右边自定义布局
     *
     * @return
     */
    fun getRightCustomView(): View? {
        return viewCustomRight
    }

    /**
     * 获取中间自定义布局视图
     *
     * @return
     */
    fun getCenterCustomView(): View? {
        return centerCustomView
    }

    fun setTitleText(title: String?) {
        tvCenter?.text = title
    }

    /**
     * @param leftView
     */
    fun setLeftView(leftView: View?) {
        val leftInnerParams = LayoutParams(WRAP_CONTENT, MATCH_PARENT)
        leftInnerParams.addRule(ALIGN_PARENT_LEFT)
        leftInnerParams.addRule(CENTER_VERTICAL)
        rlMain?.addView(leftView, leftInnerParams)
    }

    /**
     * @param centerView
     */
    fun setCenterView(centerView: View?) {
        val centerInnerParams = LayoutParams(WRAP_CONTENT, MATCH_PARENT)
        centerInnerParams.addRule(CENTER_IN_PARENT)
        centerInnerParams.addRule(CENTER_VERTICAL)
        rlMain?.addView(centerView, centerInnerParams)
    }

    /**
     * @param rightView
     */
    fun setRightView(rightView: View?) {
        val rightInnerParams = LayoutParams(WRAP_CONTENT, MATCH_PARENT)
        rightInnerParams.addRule(ALIGN_PARENT_RIGHT)
        rightInnerParams.addRule(CENTER_VERTICAL)
        rlMain?.addView(rightView, rightInnerParams)
    }

    /**
     * 设置搜索框右边图标
     *
     * @param res
     */
    fun setSearchRightImageResource(res: Int) {
        ivVoice?.setImageResource(res)
    }

    /**
     * 获取SearchView输入结果
     */
    fun getSearchKey(): String {
        return etSearchHint?.text?.toString() ?: ""
    }

    /**
     * 设置点击事件监听
     *
     * @param listener
     */
    fun setListener(listener: OnTitleBarListener) {
        this.listener = listener
    }

    fun setDoubleClickListener(doubleClickListener: OnTitleBarDoubleClickListener?) {
        this.doubleClickListener = doubleClickListener
    }

    /**
     * 点击事件
     */
    interface OnTitleBarListener {
        /**
         * @param v
         * @param action 对应ACTION_XXX, 如ACTION_LEFT_TEXT
         * @param extra  中间为搜索框时,如果可输入,点击键盘的搜索按钮,会返回输入关键词
         */
        open fun onClicked(v: View?, action: Int, extra: String?)
    }

    /**
     * 标题栏双击事件监听
     */
    interface OnTitleBarDoubleClickListener {
        open fun onClicked(v: View?)
    }

    companion object {
        private const val TYPE_LEFT_NONE = 0
        private const val TYPE_LEFT_TEXTVIEW = 1
        private const val TYPE_LEFT_IMAGEBUTTON = 2
        private const val TYPE_LEFT_CUSTOM_VIEW = 3
        private const val TYPE_RIGHT_NONE = 0
        private const val TYPE_RIGHT_TEXTVIEW = 1
        private const val TYPE_RIGHT_IMAGEBUTTON = 2
        private const val TYPE_RIGHT_CUSTOM_VIEW = 3
        private const val TYPE_CENTER_NONE = 0
        private const val TYPE_CENTER_TEXTVIEW = 1
        private const val TYPE_CENTER_SEARCHVIEW = 2
        private const val TYPE_CENTER_CUSTOM_VIEW = 3
        private const val TYPE_CENTER_SEARCH_RIGHT_VOICE = 0
        private const val TYPE_CENTER_SEARCH_RIGHT_DELETE = 1

        /**
         * 设置双击监听
         */
        const val ACTION_LEFT_TEXT = 1 // 左边TextView被点击
        const val ACTION_LEFT_BUTTON = 2 // 左边ImageBtn被点击
        const val ACTION_RIGHT_TEXT = 3 // 右边TextView被点击
        const val ACTION_RIGHT_BUTTON = 4 // 右边ImageBtn被点击
        const val ACTION_SEARCH = 5 // 搜索框被点击,搜索框不可输入的状态下会被触发
        const val ACTION_SEARCH_SUBMIT = 6 // 搜索框输入状态下,键盘提交触发
        const val ACTION_SEARCH_VOICE = 7 // 语音按钮被点击
        const val ACTION_SEARCH_DELETE = 8 // 搜索删除按钮被点击
        const val ACTION_CENTER_TEXT = 9 // 中间文字点击
    }

    init {
        loadAttributes(context, attrs)
        initGlobalViews(context)
        initMainViews(context)
    }
}