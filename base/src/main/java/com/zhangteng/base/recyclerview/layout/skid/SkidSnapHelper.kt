package com.zhangteng.base.recyclerview.layout.skid

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper

/**
 * description:堆叠左梯形SnapHelper
 * author: Swing
 * date: 2022/11/26
 */
class SkidSnapHelper : SnapHelper() {
    private var mDirection = 0
    override fun calculateDistanceToFinalSnap(
        layoutManager: RecyclerView.LayoutManager, targetView: View
    ): IntArray? {
        if (layoutManager is SkidLayoutManager) {
            val out = IntArray(2)
            if (layoutManager.canScrollHorizontally()) {
                out[0] = layoutManager.calculateDistanceToPosition(
                    layoutManager.getPosition(targetView)
                )
            } else {
                out[1] = layoutManager.calculateDistanceToPosition(
                    layoutManager.getPosition(targetView)
                )
            }
            return out
        }
        return null
    }

    override fun findTargetSnapPosition(
        layoutManager: RecyclerView.LayoutManager, velocityX: Int,
        velocityY: Int
    ): Int {
        mDirection = if (layoutManager.canScrollHorizontally()) {
            velocityX
        } else {
            velocityY
        }
        return RecyclerView.NO_POSITION
    }

    override fun findSnapView(layoutManager: RecyclerView.LayoutManager): View? {
        if (layoutManager is SkidLayoutManager) {
            val pos = layoutManager.getFixedScrollPosition(
                mDirection, if (mDirection != 0) 0.8f else 0.5f
            )
            mDirection = 0
            if (pos != RecyclerView.NO_POSITION) {
                return layoutManager.findViewByPosition(pos)
            }
        }
        return null
    }
}