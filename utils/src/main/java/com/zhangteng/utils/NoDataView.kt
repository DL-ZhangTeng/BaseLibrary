package com.zhangteng.utils

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
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
open class NoDataView : LinearLayout {
    private var llNoData: ConstraintLayout? = null
    private var tvNoData: TextView? = null
    private var ivNoData: ImageView? = null
    private var btnNoData: Button? = null
    private var isNoDataViewShow = false

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
            R.styleable.NoDataView
        )
        val indexCount = a.indexCount
        for (i in 0 until indexCount) {
            when (val attr = a.getIndex(i)) {
                R.styleable.NoDataView_nodatatext -> {
                    val str = a.getString(attr)
                    setNoDataText(str)
                }
                R.styleable.NoDataView_nodataimage -> {
                    val id = a.getResourceId(attr, R.mipmap.icon_default)
                    setNoDataImageResource(id)
                }
                R.styleable.NoDataView_nodatavisibility -> {
                    val visibility = a.getInt(attr, VISIBLE)
                    setNoDataVisibility(visibility)
                }
            }
        }
        a.recycle()
    }

    private fun initView(context: Context) {
        LayoutInflater.from(context).inflate(R.layout.layout_no_data_view, this, true)
        llNoData = findViewById(R.id.ll_no_data)
        tvNoData = findViewById(R.id.tv_no_data)
        ivNoData = findViewById(R.id.iv_no_data)
        btnNoData = findViewById(R.id.btn_no_data)
    }

    open fun setNoDataVisibility(visibility: Int) {
        llNoData?.visibility = visibility
    }

    open fun setNoDataText(noDataText: String?) {
        tvNoData?.text = noDataText
    }

    open fun setNoDataText(resourceId: Int) {
        tvNoData?.setText(resourceId)
    }

    open fun setNoDataDrawable(dataDrawable: Drawable?) {
        ivNoData?.setImageDrawable(dataDrawable)
    }

    open fun setNoDataImageResource(resourceId: Int) {
        ivNoData?.setImageResource(resourceId)
    }

    open fun isNoDataViewShow(): Boolean {
        return isNoDataViewShow
    }

    open fun setNoDataViewShow(noDataViewShow: Boolean) {
        isNoDataViewShow = noDataViewShow
    }

    open fun setNoDataAgainText(noDataAgainText: String?) {
        btnNoData?.text = noDataAgainText
    }

    open fun setNoDataAgainVisibility(visibility: Int) {
        btnNoData?.visibility = visibility
    }

    open fun setAgainRequestListener(againRequestListener: AgainRequestListener?) {
        btnNoData?.setOnClickListener { againRequestListener?.request() }
    }

    interface AgainRequestListener {
        fun request()
    }
}