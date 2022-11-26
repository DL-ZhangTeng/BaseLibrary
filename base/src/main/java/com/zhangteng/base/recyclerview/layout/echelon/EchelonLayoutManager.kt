package com.zhangteng.base.recyclerview.layout.echelon

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler
import kotlin.math.floor
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow

/**
 * description:梯形LayoutManager
 * author: Swing
 * date: 2022/11/26
 */
class EchelonLayoutManager : RecyclerView.LayoutManager() {
    private var mItemViewWidth: Int
    private var mItemViewHeight: Int
    private var mItemCount = 0
    private var mScrollOffset = Int.MAX_VALUE
    private val mScale = 0.9f
    /**
     * 获取RecyclerView的显示高度
     */
    private val verticalSpace: Int
        get() = height - paddingTop - paddingBottom

    /**
     * 获取RecyclerView的显示宽度
     */
    private val horizontalSpace: Int
        get() = width - paddingLeft - paddingRight

    init {
        mItemViewWidth = (horizontalSpace * 0.87f).toInt() //item的宽
        mItemViewHeight = (mItemViewWidth * 1.46f).toInt() //item的高
    }

    override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams {
        return RecyclerView.LayoutParams(
            RecyclerView.LayoutParams.WRAP_CONTENT,
            RecyclerView.LayoutParams.WRAP_CONTENT
        )
    }

    override fun onLayoutChildren(recycler: Recycler, state: RecyclerView.State) {
        if (state.itemCount == 0 || state.isPreLayout) return
        removeAndRecycleAllViews(recycler)
        mItemViewWidth = (horizontalSpace * 0.87f).toInt()
        mItemViewHeight = (mItemViewWidth * 1.46f).toInt()
        mItemCount = itemCount
        mScrollOffset =
            min(max(mItemViewHeight, mScrollOffset), mItemCount * mItemViewHeight)
        layoutChild(recycler)
    }

    override fun scrollVerticallyBy(dy: Int, recycler: Recycler, state: RecyclerView.State): Int {
        val pendingScrollOffset = mScrollOffset + dy
        mScrollOffset =
            min(max(mItemViewHeight, mScrollOffset + dy), mItemCount * mItemViewHeight)
        layoutChild(recycler)
        return mScrollOffset - pendingScrollOffset + dy
    }

    override fun canScrollVertically(): Boolean {
        return true
    }

    private fun layoutChild(recycler: Recycler) {
        if (itemCount == 0) return
        var bottomItemPosition = floor((mScrollOffset / mItemViewHeight).toDouble()).toInt()
        var remainSpace = verticalSpace - mItemViewHeight
        val bottomItemVisibleHeight = mScrollOffset % mItemViewHeight
        val offsetPercentRelativeToItemView = bottomItemVisibleHeight * 1.0f / mItemViewHeight
        val layoutInfos = ArrayList<EchelonBean?>()
        run {
            var i = bottomItemPosition - 1
            var j = 1
            while (i >= 0) {
                val maxOffset =
                    (this.verticalSpace - mItemViewHeight) / 2 * 0.8.pow(j.toDouble())
                val start = (remainSpace - offsetPercentRelativeToItemView * maxOffset).toInt()
                val scaleXY = (mScale.toDouble().pow((j - 1).toDouble()) * (1 - offsetPercentRelativeToItemView * (1 - mScale))).toFloat()
                val layoutPercent = start * 1.0f / this.verticalSpace
                val info =
                    EchelonBean(start, scaleXY, offsetPercentRelativeToItemView, layoutPercent)
                layoutInfos.add(0, info)
                remainSpace = (remainSpace - maxOffset).toInt()
                if (remainSpace <= 0) {
                    info.top = (remainSpace + maxOffset).toInt()
                    info.positionOffset = 0f
                    info.layoutPercent = (info.top / this.verticalSpace).toFloat()
                    info.scaleXY = mScale.toDouble().pow((j - 1).toDouble()).toFloat()
                    break
                }
                i--
                j++
            }
        }
        if (bottomItemPosition < mItemCount) {
            val start = verticalSpace - bottomItemVisibleHeight
            layoutInfos.add(
                EchelonBean(
                    start,
                    1.0f,
                    bottomItemVisibleHeight * 1.0f / mItemViewHeight,
                    start * 1.0f / verticalSpace,
                    true
                )
            )
        } else {
            bottomItemPosition -= 1 //99
        }
        val layoutCount = layoutInfos.size
        val startPos = bottomItemPosition - (layoutCount - 1)
        val endPos = bottomItemPosition
        val childCount = childCount
        for (i in childCount - 1 downTo 0) {
            val childView = getChildAt(i)
            val pos = getPosition(childView!!)
            if (pos > endPos || pos < startPos) {
                removeAndRecycleView(childView, recycler)
            }
        }
        detachAndScrapAttachedViews(recycler)
        for (i in 0 until layoutCount) {
            val view = recycler.getViewForPosition(startPos + i)
            val layoutInfo = layoutInfos[i]
            addView(view)
            measureChildWithExactlySize(view)
            val left = (horizontalSpace - mItemViewWidth) / 2
            layoutDecoratedWithMargins(
                view,
                left,
                layoutInfo?.top ?: 0,
                left + mItemViewWidth,
                (layoutInfo?.top ?: 0) + mItemViewHeight
            )
            view.pivotX = (view.width / 2).toFloat()
            view.pivotY = 0f
            view.scaleX = layoutInfo?.scaleXY ?: 1f
            view.scaleY = layoutInfo?.scaleXY ?: 1f
        }
    }

    /**
     * 测量itemview的确切大小
     */
    private fun measureChildWithExactlySize(child: View) {
        val widthSpec = View.MeasureSpec.makeMeasureSpec(mItemViewWidth, View.MeasureSpec.EXACTLY)
        val heightSpec = View.MeasureSpec.makeMeasureSpec(mItemViewHeight, View.MeasureSpec.EXACTLY)
        child.measure(widthSpec, heightSpec)
    }
}