package com.zhangteng.base.base

import android.app.Activity
import android.content.*
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.view.*
import android.widget.LinearLayout
import android.widget.PopupWindow
import androidx.constraintlayout.widget.ConstraintLayout
import com.zhangteng.base.R

/**
 * Created by swing on 2018/9/6.
 */
abstract class BasePopupWindow(context: Context?) : PopupWindow(context) {
    protected lateinit var parent: View
    protected var clTitle: LinearLayout? = null
    protected var clContent: ConstraintLayout? = null
    protected var clButton: LinearLayout? = null
    var onCancelClickListener: OnCancelClickListener? = null
        set(value) {
            field = value
            if (clButton != null && clButton!!.childCount > 0 && clButton!!.getChildAt(0) is ViewGroup) {
                if ((clButton?.getChildAt(0) as ViewGroup).getChildAt(0) != null) {
                    (clButton?.getChildAt(0) as ViewGroup).getChildAt(0).setOnClickListener { v ->
                        if (this@BasePopupWindow.onCancelClickListener != null) {
                            this@BasePopupWindow.onCancelClickListener!!.onCancel(v)
                        }
                    }
                }
            }
        }
    var onConfirmClickListener: OnConfirmClickListener? = null
        set(value) {
            field = value
            if (clButton != null && clButton!!.childCount > 0 && clButton!!.getChildAt(0) is ViewGroup) {
                if ((clButton!!.getChildAt(0) as ViewGroup).getChildAt((clButton!!.getChildAt(0) as ViewGroup).childCount - 1) != null) {
                    (clButton!!.getChildAt(0) as ViewGroup).getChildAt((clButton!!.getChildAt(0) as ViewGroup).childCount - 1)
                        .setOnClickListener { v ->
                            if (this@BasePopupWindow.onConfirmClickListener != null) {
                                this@BasePopupWindow.onConfirmClickListener!!.onConfirm(v)
                            }
                        }
                }
            }
        }

    protected fun initView(context: Context?) {
        parent = LayoutInflater.from(context).inflate(R.layout.self_base_popupwindow, null)
        clTitle = parent.findViewById(R.id.self_base_popupwindow_title)
        clContent = parent.findViewById(R.id.self_base_popupwindow_content)
        clButton = parent.findViewById(R.id.self_base_popupwindow_button)
        if (getSelfTitleView() != 0) {
            LayoutInflater.from(context).inflate(getSelfTitleView(), clTitle, true)
        }
        if (getSelfContentView() != 0) {
            LayoutInflater.from(context).inflate(getSelfContentView(), clContent, true)
        }
        if (getSelfButtonView() != 0) {
            LayoutInflater.from(context).inflate(getSelfButtonView(), clButton, true)
        }
        this.contentView = parent

        //设置高
        this.height = WindowManager.LayoutParams.WRAP_CONTENT
        //设置宽
        this.width = WindowManager.LayoutParams.MATCH_PARENT
        //设置SelectPicPopupWindow弹出窗体可点击
        this.isFocusable = true
        //设置PopupWindow可触摸
        this.isTouchable = true
        //设置非PopupWindow区域是否可触摸
        this.isOutsideTouchable = true
        //实例化一个ColorDrawable颜色为半透明
        val dw = ColorDrawable(0x00000000)
        //设置SelectPicPopupWindow弹出窗体的背景
        setBackgroundDrawable(dw)
        //默认向下弹出
        this.animationStyle = R.style.showAsDropDown
        //防止被虚拟导航栏阻挡
        this.softInputMode = WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE

        initView(parent)
    }

    abstract fun getSelfTitleView(): Int
    abstract fun getSelfContentView(): Int
    abstract fun getSelfButtonView(): Int
    abstract fun initView(parent: View)

    /**
     * description 数据渲染，放在子类构造方法的super()之后执行，避免构造方法传参无法获取的问题
     */
    abstract fun initData()

    open fun setClTitle(view: View?) {
        clTitle?.removeAllViews()
        if (view != null) {
            clTitle?.addView(view)
        }
    }

    open fun setClContent(view: View?) {
        clContent?.removeAllViews()
        if (view != null) {
            clContent?.addView(view)
        }
    }

    open fun setClButton(view: View?) {
        clButton?.removeAllViews()
        if (view != null) {
            clButton?.addView(view)
        }
    }

    override fun getContentView(): View? {
        return super.getContentView()
    }

    override fun setContentView(view: View?) {
        super.setContentView(view)
    }

    override fun setAnimationStyle(animationStyle: Int) {
        super.setAnimationStyle(animationStyle)
    }

    override fun setBackgroundDrawable(background: Drawable?) {
        super.setBackgroundDrawable(background)
    }

    override fun showAsDropDown(view: View?) {
        super.showAsDropDown(view)
    }

    /**
     * @description 向下弹出
     */
    open fun setDropDown() {
        //设置SelectPicPopupWindow弹出窗体动画效果
        this.animationStyle = R.style.showAsDropDown
    }

    /**
     * @description 向上弹出
     */
    open fun setDropUp() {
        this.animationStyle = R.style.showAsDropUp
    }

    /**
     * @description  向左弹出
     */
    open fun setDropLeft() {
        this.animationStyle = R.style.showAsDropLeft
    }

    /**
     * @description向右弹出
     */
    open fun setDropRight() {
        this.animationStyle = R.style.showAsDropRight
    }

    override fun showAtLocation(parent: View?, gravity: Int, x: Int, y: Int) {
        super.showAtLocation(parent, gravity, x, y)
    }

    /**
     * 窗口变暗
     */
    open fun showBlackWindowBackground(activity: Activity?) {
        activity?.let {
            val lp = activity.window?.attributes
            lp!!.alpha = 0.4f
            activity.window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
            activity.window.attributes = lp
        }
    }

    open fun dismissBlackWindowBackground(activity: Activity?) {
        activity?.let {
            val lp = activity.window.attributes
            lp.alpha = 1f
            activity.window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
            activity.window.attributes = lp
        }
    }

    /**
     * 取消监听器
     */
    interface OnCancelClickListener {
        open fun onCancel(view: View?)
    }

    /**
     * 确定监听器
     */
    interface OnConfirmClickListener {
        open fun onConfirm(view: View?)
    }

    init {
        initView(context)
    }
}