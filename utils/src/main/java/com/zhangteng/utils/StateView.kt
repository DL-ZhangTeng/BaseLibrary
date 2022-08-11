package com.zhangteng.utils

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout

/**
 * 请求无数据显示view
 *
 * @author swing
 * @date 2018/1/23
 */
open class StateView : LinearLayout {
    private var llState: ConstraintLayout? = null
    private var tvState: TextView? = null
    private var ivState: ImageView? = null
    private var btnState: Button? = null
    private var isStateViewShow = false

    constructor(context: Context) : super(context) {
        initView(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initView(context)
        setAttrs(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initView(context)
        setAttrs(context, attrs)
    }

    private fun setAttrs(context: Context, attrs: AttributeSet?) {
        val a = context.obtainStyledAttributes(
            attrs,
            R.styleable.StateView
        )
        val indexCount = a.indexCount
        for (i in 0 until indexCount) {
            when (val attr = a.getIndex(i)) {
                R.styleable.StateView_stateText -> {
                    val str = a.getString(attr)
                    setStateText(str)
                }
                R.styleable.StateView_stateImage -> {
                    val id = a.getResourceId(attr, R.mipmap.icon_default)
                    setStateImageResource(id)
                }
                R.styleable.StateView_stateVisibility -> {
                    val visibility = a.getInt(attr, VISIBLE)
                    setStateVisibility(visibility)
                }
            }
        }
        a.recycle()
    }

    private fun initView(context: Context) {
        LayoutInflater.from(context).inflate(R.layout.layout_no_data_view, this, true)
        llState = findViewById(R.id.ll_no_data)
        tvState = findViewById(R.id.tv_no_data)
        ivState = findViewById(R.id.iv_no_data)
        btnState = findViewById(R.id.btn_no_data)
    }

    open fun setStateVisibility(visibility: Int) {
        llState?.visibility = visibility
    }

    open fun setStateText(stateText: String?) {
        tvState?.text = stateText
    }

    open fun setStateText(resourceId: Int) {
        tvState?.setText(resourceId)
    }

    open fun setStateDrawable(dataDrawable: Drawable?) {
        ivState?.setImageDrawable(dataDrawable)
    }

    open fun setStateImageResource(resourceId: Int) {
        ivState?.setImageResource(resourceId)
    }

    open fun isStateViewShow(): Boolean {
        return isStateViewShow
    }

    open fun setStateViewShow(stateViewShow: Boolean) {
        isStateViewShow = stateViewShow
    }

    open fun setStateAgainText(stateAgainText: String?) {
        btnState?.text = stateAgainText
    }

    open fun setStateAgainVisibility(visibility: Int) {
        btnState?.visibility = visibility
    }

    open fun setAgainRequestListener(againRequestListener: AgainRequestListener?) {
        btnState?.setOnClickListener { againRequestListener?.request(it) }
    }

    interface AgainRequestListener {
        fun request(view: View)
    }
}