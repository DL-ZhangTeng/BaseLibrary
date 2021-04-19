package com.zhangteng.base.widget

import android.graphics.Color
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.zhangteng.base.adapter.HeaderOrFooterAdapter
import com.zhangteng.base.base.BaseAdapter
import java.util.*

/**
 * Created by swing on 2018/5/7.
 */
open class ItemMoveTouchHelper
/**
 * Creates an ItemTouchHelper that will work with the given Callback.
 *
 *
 * You can attach ItemTouchHelper to a RecyclerView via
 * [.attachToRecyclerView]. Upon attaching, it will add an item decoration,
 * an onItemTouchListener and a Child attach / detach listener to the RecyclerView.
 *
 * @param callback The Callback which controls the behavior of this touch helper.
 */
(callback: Callback) : ItemTouchHelper(callback) {
    override fun attachToRecyclerView(recyclerView: RecyclerView?) {
        super.attachToRecyclerView(recyclerView)
    }

    class MoveTouchCallback : Callback() {
        /**
         * 如果是列表布局的话则拖拽方向为DOWN和UP，如果是网格布局的话则是DOWN和UP和LEFT和RIGHT
         */
        override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
            return if (recyclerView.getLayoutManager() is GridLayoutManager) {
                val dragFlags = UP or DOWN or
                        LEFT or RIGHT
                val swipeFlags = 0
                makeMovementFlags(dragFlags, swipeFlags)
            } else {
                val dragFlags = UP or DOWN
                val swipeFlags = 0
                makeMovementFlags(dragFlags, swipeFlags)
            }
        }

        /**
         * 将正在拖拽的item和集合的item进行交换元素
         */
        override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
            val baseAdapter = recyclerView.adapter as BaseAdapter<*, *>? ?: return true
            val data = baseAdapter.data
            //得到当拖拽的viewHolder的Position
            val fromPosition = viewHolder.adapterPosition
            //拿到当前拖拽到的item的viewHolder
            val toPosition = target.adapterPosition
            val headerCount = if (baseAdapter.hasHeaderOrFooter) (baseAdapter as HeaderOrFooterAdapter<*>?)!!.getHeadersCount() else 0
            if (fromPosition < toPosition) {
                for (i in fromPosition - headerCount until toPosition - headerCount) {
                    Collections.swap(data, i, i + 1)
                }
            } else {
                for (i in fromPosition - headerCount downTo toPosition - headerCount + 1) {
                    Collections.swap(data, i, i - 1)
                }
            }
            baseAdapter.notifyItemMoved(fromPosition, toPosition)
            return true
        }

        /**
         * 拖曳处理后的回调
         */
        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {}

        /**
         * 长按选中Item的时候开始调用
         *
         * @param viewHolder
         * @param actionState
         */
        override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
            if (actionState != ACTION_STATE_IDLE) {
                viewHolder?.itemView?.setBackgroundColor(Color.LTGRAY)
            }
            super.onSelectedChanged(viewHolder, actionState)
        }

        /**
         * 手指松开的时候还原
         *
         * @param recyclerView
         * @param viewHolder
         */
        override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
            super.clearView(recyclerView, viewHolder)
            viewHolder.itemView.setBackgroundColor(0)
        }
    }
}