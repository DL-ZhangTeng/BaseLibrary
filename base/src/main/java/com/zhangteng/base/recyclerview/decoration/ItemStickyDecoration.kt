package com.zhangteng.base.recyclerview.decoration

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.text.TextPaint
import android.text.TextUtils
import android.util.Log
import android.view.View
import androidx.annotation.ColorInt
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.zhangteng.base.bean.GroupInfo

/**
 * 顶部悬停分割线
 * Created by swing on 2018/4/12.
 */
open class ItemStickyDecoration(private val groupInfoInterface: GroupInfoInterface?) :
    ItemDecoration() {
    private val mFontMetrics: Paint.FontMetrics?
    private var mStickyHeight = 60
    private var padding = 16
    private val mTextPaint: Paint?
    private val mPaint: Paint?
    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(c, parent, state)
        val childCount = parent.childCount
        for (i in 0 until childCount) {
            val view = parent.getChildAt(i)
            val index = parent.getChildAdapterPosition(view)
            if (groupInfoInterface != null) {
                val groupInfo = groupInfoInterface.getGroupInfo(index)
                val left = parent.paddingLeft
                val right = parent.width - parent.paddingRight
                //屏幕上第一个可见的 ItemView 时，i == 0;
                if (i != 0) {
                    //只有组内的第一个ItemView之上才绘制
                    if (groupInfo != null && groupInfo.isFirst()) {
                        val top = view.top - mStickyHeight
                        val bottom = view.top
                        drawStickyHeader(c, groupInfo, left, top, right, bottom)
                    } else {
                        val top = view.top - 1
                        val bottom = view.top
                        if (mPaint != null)
                            c.drawRect(
                                left.toFloat(),
                                top.toFloat(),
                                right.toFloat(),
                                bottom.toFloat(),
                                mPaint
                            )
                    }
                } else {
                    //当 ItemView 是屏幕上第一个可见的View 时，不管它是不是组内第一个View
                    //它都需要绘制它对应的 StickyHeader。
                    // 还要判断当前的 ItemView 是不是它组内的最后一个 View
                    var top = parent.paddingTop
                    if (groupInfo != null && groupInfo.isLast()) {
                        val suggestTop = view.bottom - mStickyHeight
                        // 当 ItemView 与 Header 底部平齐的时候，判断 Header 的顶部是否小于
                        // parent 顶部内容开始的位置，如果小于则对 Header.top 进行位置更新，
                        //否则将继续保持吸附在 parent 的顶部
                        if (suggestTop < top) {
                            top = suggestTop
                        }
                    }
                    val bottom = top + mStickyHeight
                    drawStickyHeader(c, groupInfo, left, top, right, bottom)
                }
            }
        }
    }

    private fun drawStickyHeader(
        c: Canvas?,
        groupInfo: GroupInfo?,
        left: Int,
        top: Int,
        right: Int,
        bottom: Int
    ) {
        if (c == null || groupInfo == null || mPaint == null || mFontMetrics == null || mTextPaint == null) return
        //绘制Header
        c.drawRect(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat(), mPaint)
        val titleX = (left + padding).toFloat()
        val titleY = bottom - mFontMetrics.descent - padding / 2
        //绘制Title
        c.drawText(
            (if (TextUtils.isEmpty(groupInfo.getTitle())) "" else groupInfo.getTitle())!!,
            titleX,
            titleY,
            mTextPaint
        )
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        val position = parent.getChildAdapterPosition(view)
        val groupInfo = groupInfoInterface?.getGroupInfo(position)
        if (groupInfo != null && groupInfo.isFirst()) {
            outRect.top = mStickyHeight
        } else {
            outRect.top = 0
        }
        Log.e("top", outRect.top.toString() + position)
    }

    open fun setStickyHeight(mStickyHeight: Int) {
        this.mStickyHeight = mStickyHeight
    }

    open fun setStickyColor(@ColorInt stickyColor: Int) {
        mPaint?.color = stickyColor
    }

    open fun setTextSize(textSize: Float) {
        mTextPaint?.textSize = textSize
    }

    open fun setTextColor(@ColorInt textColor: Int) {
        mTextPaint?.color = textColor
    }

    open fun setTextPadding(padding: Int) {
        this.padding = padding
    }

    interface GroupInfoInterface {
        fun getGroupInfo(position: Int): GroupInfo?
    }

    init {
        mTextPaint = TextPaint()
        mTextPaint.color = Color.parseColor("#989898")
        mTextPaint.textSize = 16f
        mFontMetrics = mTextPaint.getFontMetrics()
        mPaint = Paint()
        mPaint.isAntiAlias = true
        mPaint.color = Color.parseColor("#e8e8e8")
    }
}