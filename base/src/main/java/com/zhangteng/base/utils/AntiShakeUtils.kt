package com.zhangteng.base.utils

import android.view.View
import androidx.annotation.IntRange
import com.zhangteng.base.R

object AntiShakeUtils {
    private const val INTERNAL_TIME: Long = 800
    private var lastZanClick: Long = 0

    /**
     * Whether this click event is invalid.
     *
     * @param target target view
     * @return true, invalid click event.
     * @see .isInvalidClick
     */
    fun isInvalidClick(target: View): Boolean {
        return isInvalidClick(target, INTERNAL_TIME)
    }

    /**
     * Whether this click event is invalid.
     *
     * @param target       target view
     * @param internalTime the internal time. The unit is millisecond.
     * @return true, invalid click event.
     */
    fun isInvalidClick(target: View, @IntRange(from = 0) internalTime: Long): Boolean {
        val curTimeStamp = System.currentTimeMillis()
        var lastClickTimeStamp: Long = 0
        val o = target.getTag(R.id.last_click_time)
        if (o == null) {
            target.setTag(R.id.last_click_time, curTimeStamp)
            return false
        }
        lastClickTimeStamp = o as Long
        val isInvalid = curTimeStamp - lastClickTimeStamp < internalTime
        if (!isInvalid) target.setTag(R.id.last_click_time, curTimeStamp)
        return isInvalid
    }

    fun isZanClick(): Boolean {
        val now = System.currentTimeMillis()
        if (now - lastZanClick >= 1000) {
            lastZanClick = now
            return false
        } else {
            lastZanClick = now
        }
        return true
    }
}