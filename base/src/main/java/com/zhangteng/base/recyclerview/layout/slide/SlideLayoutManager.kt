package com.zhangteng.base.recyclerview.layout.slide

import android.annotation.SuppressLint
import android.view.MotionEvent
import android.view.View.OnTouchListener
import android.view.ViewGroup
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler

/**
 * description: 侧滑倒梯形LayoutManager
 *              mCallback = SlideTouchCallback(mRecyclerView.adapter, mList)
 *              mItemTouchHelper = ItemTouchHelper(mCallback)
 *              mSlideLayoutManager = SlideLayoutManager(mRecyclerView, mItemTouchHelper)
 *              mItemTouchHelper.attachToRecyclerView(mRecyclerView)
 *              mRecyclerView.setLayoutManager(mSlideLayoutManager)
 * author: Swing
 * date: 2022/11/26
 */
@SuppressLint("ClickableViewAccessibility")
class SlideLayoutManager(recyclerView: RecyclerView, itemTouchHelper: ItemTouchHelper) :
    RecyclerView.LayoutManager() {
    private val mRecyclerView: RecyclerView
    private val mItemTouchHelper: ItemTouchHelper
    private val mOnTouchListener: OnTouchListener

    override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams {
        return RecyclerView.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    override fun onLayoutChildren(recycler: Recycler, state: RecyclerView.State) {
        detachAndScrapAttachedViews(recycler)
        val itemCount = itemCount
        if (itemCount > Constants.DEFAULT_SHOW_ITEM) {
            for (position in Constants.DEFAULT_SHOW_ITEM downTo 0) {
                val view = recycler.getViewForPosition(position)
                addView(view)
                measureChildWithMargins(view, 0, 0)
                val widthSpace = width - getDecoratedMeasuredWidth(view)
                val heightSpace = height - getDecoratedMeasuredHeight(view)
                layoutDecoratedWithMargins(
                    view, widthSpace / 2, heightSpace / 5,
                    widthSpace / 2 + getDecoratedMeasuredWidth(view),
                    heightSpace / 5 + getDecoratedMeasuredHeight(view)
                )
                if (position == Constants.DEFAULT_SHOW_ITEM) {
                    view.scaleX =
                        1 - (position - 1) * Constants.DEFAULT_SCALE
                    view.scaleY =
                        1 - (position - 1) * Constants.DEFAULT_SCALE
                    view.translationY =
                        ((position - 1) * view.measuredHeight / Constants.DEFAULT_TRANSLATE_Y).toFloat()
                } else if (position > 0) {
                    view.scaleX =
                        1 - position * Constants.DEFAULT_SCALE
                    view.scaleY = 1 - position * Constants.DEFAULT_SCALE
                    view.translationY =
                        (position * view.measuredHeight / Constants.DEFAULT_TRANSLATE_Y).toFloat()
                } else {
                    view.setOnTouchListener(mOnTouchListener)
                }
            }
        } else {
            for (position in itemCount - 1 downTo 0) {
                val view = recycler.getViewForPosition(position)
                addView(view)
                measureChildWithMargins(view, 0, 0)
                val widthSpace = width - getDecoratedMeasuredWidth(view)
                val heightSpace = height - getDecoratedMeasuredHeight(view)
                layoutDecoratedWithMargins(
                    view, widthSpace / 2, heightSpace / 5,
                    widthSpace / 2 + getDecoratedMeasuredWidth(view),
                    heightSpace / 5 + getDecoratedMeasuredHeight(view)
                )
                if (position > 0) {
                    view.scaleX =
                        1 - position * Constants.DEFAULT_SCALE
                    view.scaleY =
                        1 - position * Constants.DEFAULT_SCALE
                    view.translationY =
                        (position * view.measuredHeight / Constants.DEFAULT_TRANSLATE_Y).toFloat()
                } else {
                    view.setOnTouchListener(mOnTouchListener)
                }
            }
        }
    }

    init {
        mRecyclerView = recyclerView
        mItemTouchHelper = itemTouchHelper
        mOnTouchListener = OnTouchListener { v, event ->
            val childViewHolder = mRecyclerView.getChildViewHolder(v)
            if (event.actionMasked == MotionEvent.ACTION_DOWN) {
                mItemTouchHelper.startSwipe(childViewHolder)
            }
            false
        }
    }
}