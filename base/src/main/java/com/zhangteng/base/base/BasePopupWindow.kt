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
    protected var clTitle: LinearLayout? = null
    protected var clContent: ConstraintLayout? = null
    protected var clButton: LinearLayout? = null
    protected var parent: View? = null
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
                    (clButton!!.getChildAt(0) as ViewGroup).getChildAt((clButton!!.getChildAt(0) as ViewGroup).childCount - 1).setOnClickListener { v ->
                        if (this@BasePopupWindow.onConfirmClickListener != null) {
                            this@BasePopupWindow.onConfirmClickListener!!.onConfirm(v)
                        }
                    }
                }
            }
        }

    private fun initView(context: Context?) {
        parent = LayoutInflater.from(context).inflate(R.layout.self_base_popupwindow, null)
        clTitle = parent?.findViewById(R.id.self_base_popupwindow_title)
        clContent = parent?.findViewById(R.id.self_base_popupwindow_content)
        clButton = parent?.findViewById(R.id.self_base_popupwindow_button)
        if (getSelfTitleView() != 0) {
            LayoutInflater.from(context).inflate(getSelfTitleView(), clTitle, true)
        }
        if (getSelfContentView() != 0) {
            LayoutInflater.from(context).inflate(getSelfContentView(), clContent, true)
        }
        if (getSelfButtonView() != 0) {
            LayoutInflater.from(context).inflate(getSelfButtonView(), clButton, true)
        }
        initView(parent)
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
        //设置SelectPicPopupWindow弹出窗体动画效果
        this.animationStyle = R.style.showAsDropDown
        //防止被虚拟导航栏阻挡
        this.softInputMode = WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
    }

    abstract fun getSelfTitleView(): Int
    abstract fun getSelfContentView(): Int
    abstract fun getSelfButtonView(): Int
    abstract fun initView(view: View?)

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

    fun setDropDown() {
        //设置SelectPicPopupWindow弹出窗体动画效果
        this.animationStyle = R.style.showAsDropDown
    }

    fun setDropUp() {
        this.animationStyle = R.style.showAsDropUp
    }

    override fun showAtLocation(parent: View?, gravity: Int, x: Int, y: Int) {
        super.showAtLocation(parent, gravity, x, y)
    }

    override fun showAsDropDown(anchor: View?, xoff: Int, yoff: Int) {
        super.showAsDropDown(anchor, xoff, yoff)
    }

    override fun showAsDropDown(anchor: View?, xoff: Int, yoff: Int, gravity: Int) {
        super.showAsDropDown(anchor, xoff, yoff, gravity)
    }

    /**
     * 窗口变暗
     */
    fun showBlackWindowBackground(activity: Activity?) {
        activity?.let {
            val lp = activity.window?.attributes
            lp!!.alpha = 0.4f
            activity.window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
            activity.window.attributes = lp
        }
    }

    fun dismissBlackWindowBackground(activity: Activity?) {
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