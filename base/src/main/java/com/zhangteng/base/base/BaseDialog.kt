package com.zhangteng.base.base

import android.app.Dialog
import android.content.*
import android.view.*
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import com.zhangteng.base.R

/**
 * Created by swing on 2018/9/6.
 */
abstract class BaseDialog constructor(context: Context, themeResId: Int = R.style.SelfDialog) :
    Dialog(context, themeResId) {
    protected var clTitle: LinearLayout? = null
    protected var clContent: ConstraintLayout? = null
    protected var clButton: LinearLayout? = null
    protected var divider: View? = null
    protected var onCancelClickListener: OnCancelClickListener? = null
        /**
         * 设置取消按钮点击监听器
         * 默认左边第一个为取消按钮
         */
        set(value) {
            field = value
            if (clButton != null && clButton!!.getChildCount() > 0 && clButton!!.getChildAt(0) is ViewGroup) {
                if ((clButton!!.getChildAt(0) as ViewGroup).getChildAt(0) != null) {
                    (clButton!!.getChildAt(0) as ViewGroup).getChildAt(0)
                        .setOnClickListener { v: View? ->
                            if (this@BaseDialog.onCancelClickListener != null) {
                                this@BaseDialog.onCancelClickListener!!.onCancel(v)
                            }
                        }
                }
            }
        }
    protected var onConfirmClickListener: OnConfirmClickListener? = null
        /**
         * 设置确认按钮点击监听器
         * 默认右边第一个为确认按钮
         */
        set(value) {
            field = value
            if (clButton != null && clButton!!.getChildCount() > 0 && clButton!!.getChildAt(0) is ViewGroup) {
                if ((clButton!!.getChildAt(0) as ViewGroup).getChildAt((clButton!!.getChildAt(0) as ViewGroup).childCount - 1) != null) {
                    (clButton!!.getChildAt(0) as ViewGroup).getChildAt((clButton!!.getChildAt(0) as ViewGroup).childCount - 1)
                        .setOnClickListener { v: View? ->
                            if (this@BaseDialog.onConfirmClickListener != null) {
                                this@BaseDialog.onConfirmClickListener!!.onConfirm(v)
                            }
                        }
                }
            }
        }

    private fun init(context: Context?) {
        val view = LayoutInflater.from(context).inflate(R.layout.self_base_dialog, null)
        clTitle = view.findViewById(R.id.self_base_dialog_title)
        clContent = view.findViewById(R.id.self_base_dialog_content)
        clButton = view.findViewById(R.id.self_base_dialog_button)
        divider = view.findViewById(R.id.self_base_dialog_content_divider)
        if (getSelfTitleView() != 0) {
            LayoutInflater.from(context).inflate(getSelfTitleView(), clTitle, true)
        }
        if (getSelfContentView() != 0) {
            LayoutInflater.from(context).inflate(getSelfContentView(), clContent, true)
        } else {
            divider?.visibility = View.GONE
        }
        if (getSelfButtonView() != 0) {
            LayoutInflater.from(context).inflate(getSelfButtonView(), clButton, true)
        } else {
            divider?.visibility = View.GONE
        }
        initView(view)
        setContentView(view)
    }

    abstract fun getSelfTitleView(): Int
    abstract fun getSelfContentView(): Int
    abstract fun getSelfButtonView(): Int
    abstract fun initView(view: View?)
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

    interface OnCancelClickListener {
        fun onCancel(view: View?)
    }

    interface OnConfirmClickListener {
        fun onConfirm(view: View?)
    }

    init {
        init(context)
    }
}