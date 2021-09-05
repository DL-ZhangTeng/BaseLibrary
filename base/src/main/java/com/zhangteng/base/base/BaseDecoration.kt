package com.zhangteng.base.base

import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import androidx.recyclerview.widget.StaggeredGridLayoutManager

/**
 * Created by Swing on 2019/5/28 0028.
 */
open class BaseDecoration : ItemDecoration() {
    protected var dividerHeight = 0.5f
    protected var dividerTop = 8f
    protected var dividerColor = "#ffe6e6e6"

    /**
     * 获取列数
     *
     * @param parent
     * @return
     */
    protected fun getSpanCount(parent: RecyclerView): Int {
        var spanCount = -1
        val layoutManager = parent.layoutManager
        if (layoutManager is StaggeredGridLayoutManager) {
            spanCount = layoutManager
                .spanCount
        }
        return spanCount
    }

    protected fun isLastColumn(parent: RecyclerView, pos: Int, spanCount: Int): Boolean {
        val layoutManager = parent.layoutManager
        return if (layoutManager is StaggeredGridLayoutManager) {
            // 如果是最后一列，则不需要绘制右边
            (pos + 1) % spanCount == 0
        } else false
    }

    protected fun isFirstColumn(parent: RecyclerView, pos: Int, spanCount: Int): Boolean {
        val layoutManager = parent.layoutManager
        return if (layoutManager is StaggeredGridLayoutManager) {
            (pos + 1) % spanCount == 1
        } else false
    }

    protected fun isLastRaw(
        parent: RecyclerView, pos: Int, spanCount: Int,
        childCount: Int
    ): Boolean {
        var childCount = childCount
        val layoutManager = parent.layoutManager
        if (layoutManager is StaggeredGridLayoutManager) {
            // 如果是最后一行，则不需要绘制底部
            childCount -= childCount % spanCount
            return pos >= childCount
        }
        return false
    }

    protected fun isFirstRaw(parent: RecyclerView, pos: Int, spanCount: Int): Boolean {
        val layoutManager = parent.layoutManager
        return if (layoutManager is StaggeredGridLayoutManager) {
            pos < spanCount
        } else false
    }
}