package com.zhangteng.base.widget

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.zhangteng.base.R
import com.zhangteng.base.base.BaseDecoration
import com.zhangteng.base.utils.DensityUtil.Companion.dp2px

/**
 * 水平线性布局分割线
 * Created by Swing on 2019/6/6 0006.
 */
class LinearSpacingItemDecoration(context: Context) : BaseDecoration() {
    private val firstLeft: Int
    private val divider: Int
    private val lastRight: Int
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        when {
            parent.getChildAdapterPosition(view) == 0 -> {
                outRect.left = firstLeft
                outRect.right = divider
            }
            parent.getChildAdapterPosition(view) == parent.adapter!!.itemCount - 1 -> {
                outRect.right = lastRight
            }
            else -> {
                outRect.right = divider
            }
        }
    }

    init {
        firstLeft = context.resources.getDimension(R.dimen.default_list_left_right).toInt()
        divider = dp2px(context, 10f)
        lastRight = context.resources.getDimension(R.dimen.default_list_left_right).toInt()
    }
}