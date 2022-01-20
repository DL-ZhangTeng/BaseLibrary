package com.zhangteng.utils

import android.view.View
import androidx.annotation.IntRange

private const val INTERNAL_TIME: Long = 800

/**
 * Whether this click event is invalid.
 *
 * @return true, invalid click event.
 * @see .isInvalidClick
 */
fun View.isInvalidClick(): Boolean {
    return isInvalidClick(INTERNAL_TIME)
}

/**
 * Whether this click event is invalid.
 *
 * @param internalTime the internal time. The unit is millisecond.
 * @return true, invalid click event.
 */
fun View.isInvalidClick(@IntRange(from = 0) internalTime: Long): Boolean {
    val curTimeStamp = System.currentTimeMillis()
    var lastClickTimeStamp: Long = 0
    val o = getTag(R.id.last_click_time)
    if (o == null) {
        setTag(R.id.last_click_time, curTimeStamp)
        return false
    }
    lastClickTimeStamp = o as Long
    val isInvalid = curTimeStamp - lastClickTimeStamp < internalTime
    if (!isInvalid) setTag(R.id.last_click_time, curTimeStamp)
    return isInvalid
}