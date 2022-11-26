package com.zhangteng.base.recyclerview.layout.slide

import android.annotation.SuppressLint
import android.graphics.Canvas
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.abs

/**
 * description: 手势回调
 * author: Swing
 * date: 2022/11/26
 */
class SlideTouchCallback<T> : ItemTouchHelper.Callback {
    private val adapter: RecyclerView.Adapter<*>
    private var dataList: MutableList<T>
    private var mListener: OnSlideListener<T>? = null

    constructor(adapter: RecyclerView.Adapter<*>, dataList: MutableList<T>) {
        this.adapter = adapter
        this.dataList = dataList
    }

    constructor(
        adapter: RecyclerView.Adapter<*>,
        dataList: MutableList<T>,
        listener: OnSlideListener<T>?
    ) {
        this.adapter = adapter
        this.dataList = dataList
        mListener = listener
    }

    fun setOnSlideListener(mListener: OnSlideListener<T>?) {
        this.mListener = mListener
    }

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        val dragFlags = 0
        var slideFlags = 0
        val layoutManager = recyclerView.layoutManager
        if (layoutManager is SlideLayoutManager) {
            slideFlags = ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        }
        return makeMovementFlags(dragFlags, slideFlags)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        viewHolder.itemView.setOnTouchListener(null)
        val layoutPosition = viewHolder.layoutPosition
        val remove: T = dataList.removeAt(layoutPosition)
        adapter.notifyDataSetChanged()
        if (mListener != null) {
            mListener!!.onSlided(
                viewHolder,
                remove,
                if (direction == ItemTouchHelper.LEFT) Constants.SLIDED_LEFT else Constants.SLIDED_RIGHT
            )
        }
        if (adapter.itemCount == 0) {
            if (mListener != null) {
                mListener!!.onClear()
            }
        }
    }

    override fun isItemViewSwipeEnabled(): Boolean {
        return false
    }

    override fun onChildDraw(
        c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
        dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean
    ) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        val itemView = viewHolder.itemView
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            var ratio = dX / getThreshold(recyclerView, viewHolder)
            if (ratio > 1) {
                ratio = 1f
            } else if (ratio < -1) {
                ratio = -1f
            }
            itemView.rotation =
                ratio * Constants.DEFAULT_ROTATE_DEGREE
            val childCount = recyclerView.childCount
            if (childCount > Constants.DEFAULT_SHOW_ITEM) {
                for (position in 1 until childCount - 1) {
                    val index = childCount - position - 1
                    val view = recyclerView.getChildAt(position)
                    view.scaleX =
                        1 - index * Constants.DEFAULT_SCALE + abs(
                            ratio
                        ) * Constants.DEFAULT_SCALE
                    view.scaleY =
                        1 - index * Constants.DEFAULT_SCALE + abs(
                            ratio
                        ) * Constants.DEFAULT_SCALE
                    view.translationY =
                        (index - abs(ratio)) * itemView.measuredHeight / Constants.DEFAULT_TRANSLATE_Y
                }
            } else {
                for (position in 0 until childCount - 1) {
                    val index = childCount - position - 1
                    val view = recyclerView.getChildAt(position)
                    view.scaleX =
                        1 - index * Constants.DEFAULT_SCALE + abs(
                            ratio
                        ) * Constants.DEFAULT_SCALE
                    view.scaleY =
                        1 - index * Constants.DEFAULT_SCALE + abs(
                            ratio
                        ) * Constants.DEFAULT_SCALE
                    view.translationY =
                        (index - abs(ratio)) * itemView.measuredHeight / Constants.DEFAULT_TRANSLATE_Y
                }
            }
            if (mListener != null) {
                if (ratio != 0f) {
                    mListener!!.onSliding(
                        viewHolder,
                        ratio,
                        if (ratio < 0) Constants.SLIDING_LEFT else Constants.SLIDING_RIGHT
                    )
                } else {
                    mListener!!.onSliding(viewHolder, ratio, Constants.SLIDING_NONE)
                }
            }
        }
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        super.clearView(recyclerView, viewHolder)
        viewHolder.itemView.rotation = 0f
    }

    private fun getThreshold(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Float {
        return recyclerView.width * getSwipeThreshold(viewHolder)
    }
}