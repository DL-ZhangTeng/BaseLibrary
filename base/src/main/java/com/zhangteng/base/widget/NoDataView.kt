package com.zhangteng.base.widget

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.zhangteng.base.R

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

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initView(context)
        setAttrs(context, attrs)
    }

    private fun setAttrs(context: Context, attrs: AttributeSet?) {
        val a = context.obtainStyledAttributes(attrs,
                R.styleable.NoDataView)
        val indexCount = a.indexCount
        for (i in 0 until indexCount) {
            val attr = a.getIndex(i)
            if (attr == R.styleable.NoDataView_nodatatext) {
                val str = a.getString(attr)
                setNoDataText(str)
            } else if (attr == R.styleable.NoDataView_nodataimage) {
                val id = a.getResourceId(attr, R.mipmap.wangluowu)
                setNoDataImageResource(id)
            } else if (attr == R.styleable.NoDataView_nodatavisibility) {
                val visibility = a.getInt(attr, VISIBLE)
                setNoDataVisibility(visibility)
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

    fun setNoDataVisibility(visibility: Int) {
        llNoData?.visibility = visibility
    }

    fun setNoDataText(noDataText: String?) {
        tvNoData?.text = noDataText
    }

    fun setNoDataText(resourceId: Int) {
        tvNoData?.setText(resourceId)
    }

    fun setNoDataDrawable(dataDrawable: Drawable?) {
        ivNoData?.setImageDrawable(dataDrawable)
    }

    fun setNoDataImageResource(resourceId: Int) {
        ivNoData?.setImageResource(resourceId)
    }

    fun isNoDataViewShow(): Boolean {
        return isNoDataViewShow
    }

    fun setNoDataViewShow(noDataViewShow: Boolean) {
        isNoDataViewShow = noDataViewShow
    }

    fun setNoDataAgainText(noDataAgainText: String?) {
        btnNoData?.setText(noDataAgainText)
    }

    fun setNoDataAgainVisivility(visivility: Int) {
        btnNoData?.setVisibility(visivility)
    }

    fun setAgainRequestListener(againRequestListener: AgainRequestListener?) {
        btnNoData?.setOnClickListener { againRequestListener?.request() }
    }

    interface AgainRequestListener {
        fun request()
    }
}