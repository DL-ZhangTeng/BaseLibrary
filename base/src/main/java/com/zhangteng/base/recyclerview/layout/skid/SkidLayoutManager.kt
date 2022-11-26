package com.zhangteng.base.recyclerview.layout.skid

import android.view.View
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler
import com.zhangteng.base.recyclerview.layout.echelon.EchelonBean
import kotlin.math.floor
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow

/**
 * description: 堆叠左梯形LayoutManager
 *              SkidLayoutManager(1.5f, 0.85f)
 * author: Swing
 * date: 2022/11/26
 */
class SkidLayoutManager(private val mItemHeightWidthRatio: Float, private val mScale: Float) :
    RecyclerView.LayoutManager() {
    private var mHasChild = false
    private var mItemViewWidth = 0
    private var mItemViewHeight = 0
    private var mScrollOffset = Int.MAX_VALUE
    private var mItemCount = 0
    private val mSkidSnapHelper: SkidSnapHelper = SkidSnapHelper()

    /**
     * Sets true to scroll layout in left direction.
     */
    var isReverseDirection = false

    override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams {
        return RecyclerView.LayoutParams(
            RecyclerView.LayoutParams.WRAP_CONTENT,
            RecyclerView.LayoutParams.WRAP_CONTENT
        )
    }

    override fun onAttachedToWindow(view: RecyclerView) {
        super.onAttachedToWindow(view)
        mSkidSnapHelper.attachToRecyclerView(view)
    }

    fun getFixedScrollPosition(direction: Int, fixValue: Float): Int {
        if (mHasChild) {
            if (mScrollOffset % mItemViewWidth == 0) {
                return RecyclerView.NO_POSITION
            }
            val itemPosition = position()
            val layoutPosition =
                (if (direction > 0) itemPosition + fixValue else itemPosition + (1 - fixValue)).toInt() - 1
            return convert2AdapterPosition(layoutPosition)
        }
        return RecyclerView.NO_POSITION
    }

    override fun onLayoutChildren(recycler: Recycler, state: RecyclerView.State) {
        if (state.itemCount == 0 || state.isPreLayout) return
        removeAndRecycleAllViews(recycler)
        if (!mHasChild) {
            mItemViewHeight = verticalSpace
            mItemViewWidth = (mItemViewHeight / mItemHeightWidthRatio).toInt()
            mHasChild = true
        }
        mItemCount = itemCount
        mScrollOffset = makeScrollOffsetWithinRange(mScrollOffset)
        fill(recycler)
    }

    private fun position(): Float {
        return if (isReverseDirection) (mScrollOffset + mItemCount * mItemViewWidth) * 1.0f / mItemViewWidth else mScrollOffset * 1.0f / mItemViewWidth
    }

    fun fill(recycler: Recycler) {
        var bottomItemPosition = floor(position().toDouble()).toInt()
        val space = horizontalSpace
        val bottomItemVisibleSize =
            if (isReverseDirection) ((mItemCount - 1) * mItemViewWidth + mScrollOffset) % mItemViewWidth else mScrollOffset % mItemViewWidth
        val offsetPercent = bottomItemVisibleSize * 1.0f / mItemViewWidth
        var remainSpace = if (isReverseDirection) 0 else space - mItemViewWidth
        val baseOffsetSpace =
            if (isReverseDirection) mItemViewWidth else horizontalSpace - mItemViewWidth
        val layoutInfos = ArrayList<EchelonBean?>()
        run {
            var i = bottomItemPosition - 1
            var j = 1
            while (i >= 0) {
                val maxOffset = baseOffsetSpace / 2 * mScale.toDouble().pow(j.toDouble())
                val adjustedPercent = if (isReverseDirection) -offsetPercent else +offsetPercent
                val start = (remainSpace - adjustedPercent * maxOffset).toInt()
                val scaleXY = (mScale.toDouble()
                    .pow((j - 1).toDouble()) * (1 - offsetPercent * (1 - mScale))).toFloat()
                val percent = start * 1.0f / space
                val info = EchelonBean(start, scaleXY, offsetPercent, percent)
                layoutInfos.add(0, info)
                val delta = if (isReverseDirection) maxOffset else -maxOffset
                remainSpace += delta.toInt()
                val isOutOfSpace =
                    if (isReverseDirection) remainSpace > this.horizontalSpace else remainSpace <= 0
                if (isOutOfSpace) {
                    info.top = (remainSpace - delta).toInt()
                    info.positionOffset = 0f
                    info.layoutPercent = (info.top / space).toFloat()
                    info.scaleXY = mScale.toDouble().pow((j - 1).toDouble()).toFloat()
                    break
                }
                i--
                j++
            }
        }
        if (bottomItemPosition < mItemCount) {
            val start =
                if (isReverseDirection) bottomItemVisibleSize - mItemViewWidth else space - bottomItemVisibleSize
            layoutInfos.add(
                EchelonBean(
                    start,
                    1.0f,
                    offsetPercent,
                    start * 1.0f / space,
                    true
                )
            )
        } else {
            bottomItemPosition -= 1
        }
        val layoutCount = layoutInfos.size
        val startPos = bottomItemPosition - (layoutCount - 1)
        val endPos = bottomItemPosition
        val childCount = childCount
        for (i in childCount - 1 downTo 0) {
            val childView = getChildAt(i)
            val pos = convert2LayoutPosition(getPosition(childView!!))
            if (pos > endPos || pos < startPos) {
                removeAndRecycleView(childView, recycler)
            }
        }
        detachAndScrapAttachedViews(recycler)
        for (i in 0 until layoutCount) {
            val position = convert2AdapterPosition(startPos + i)
            fillChild(recycler.getViewForPosition(position), layoutInfos[i])
        }
    }

    private fun fillChild(view: View, layoutInfo: EchelonBean?) {
        addView(view)
        measureChildWithExactlySize(view)
        val scaleFix = (mItemViewWidth * (1 - (layoutInfo?.scaleXY ?: 1f)) / 2).toInt()
        val left = (layoutInfo?.top ?: 0) - scaleFix
        val top = paddingTop
        val right = (layoutInfo?.top ?: 0) + mItemViewWidth - scaleFix
        val bottom = top + mItemViewHeight
        layoutDecoratedWithMargins(view, left, top, right, bottom)
        ViewCompat.setScaleX(view, layoutInfo?.scaleXY ?: 1f)
        ViewCompat.setScaleY(view, layoutInfo?.scaleXY ?: 1f)
    }

    private fun measureChildWithExactlySize(child: View) {
        val lp = child.layoutParams as RecyclerView.LayoutParams
        val widthSpec = View.MeasureSpec.makeMeasureSpec(
            mItemViewWidth - lp.leftMargin - lp.rightMargin, View.MeasureSpec.EXACTLY
        )
        val heightSpec = View.MeasureSpec.makeMeasureSpec(
            mItemViewHeight - lp.topMargin - lp.bottomMargin, View.MeasureSpec.EXACTLY
        )
        child.measure(widthSpec, heightSpec)
    }

    private fun makeScrollOffsetWithinRange(scrollOffset: Int): Int {
        return if (isReverseDirection) {
            max(min(0, scrollOffset), -(mItemCount - 1) * mItemViewWidth)
        } else {
            min(max(mItemViewWidth, scrollOffset), mItemCount * mItemViewWidth)
        }
    }

    override fun scrollHorizontallyBy(dx: Int, recycler: Recycler, state: RecyclerView.State): Int {
        val delta = if (isReverseDirection) -dx else dx
        val pendingScrollOffset = mScrollOffset + delta
        mScrollOffset = makeScrollOffsetWithinRange(pendingScrollOffset)
        fill(recycler)
        return mScrollOffset - pendingScrollOffset + delta
    }

    fun calculateDistanceToPosition(targetPos: Int): Int {
        if (isReverseDirection) {
            return mItemViewWidth * targetPos + mScrollOffset
        }
        val pendingScrollOffset = mItemViewWidth * (convert2LayoutPosition(targetPos) + 1)
        return pendingScrollOffset - mScrollOffset
    }

    override fun scrollToPosition(position: Int) {
        if (position > 0 && position < mItemCount) {
            mScrollOffset = mItemViewWidth * (convert2LayoutPosition(position) + 1)
            requestLayout()
        }
    }

    override fun canScrollHorizontally(): Boolean {
        return true
    }

    fun convert2AdapterPosition(layoutPosition: Int): Int {
        return mItemCount - 1 - layoutPosition
    }

    fun convert2LayoutPosition(adapterPosition: Int): Int {
        return mItemCount - 1 - adapterPosition
    }

    val verticalSpace: Int
        get() = height - paddingTop - paddingBottom
    val horizontalSpace: Int
        get() = width - paddingLeft - paddingRight
}