package com.zhangteng.base.utils

import android.content.Context
import android.util.TypedValue

/**
 * 单位转换类
 */
open class DensityUtil private constructor() {
    companion object {
        private var density = -1f
        private var widthPixels = -1
        private var heightPixels = -1

        /**
         * dp转px
         *
         * @param context
         * @param dpVal
         * @return
         */
        fun dp2px(context: Context?, dpVal: Float): Int {
            return if (context != null) {
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpVal, context.resources.displayMetrics).toInt()
            } else -1
        }

        /**
         * sp转px
         *
         * @param context
         * @param spVal
         * @return
         */
        fun sp2px(context: Context?, spVal: Float): Int {
            return if (context != null) {
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spVal, context.resources.displayMetrics).toInt()
            } else -1
        }

        /**
         * px转dp
         *
         * @param context
         * @param pxVal
         * @return
         */
        fun px2dp(context: Context?, pxVal: Float): Float {
            if (context == null) return -1f
            val scale = context.resources.displayMetrics.density
            return pxVal / scale
        }

        /**
         * px转sp
         *
         * @param context
         * @param pxVal
         * @return
         */
        fun px2sp(context: Context?, pxVal: Float): Float {
            if (context == null) return -1f
            return pxVal / context.resources.displayMetrics.scaledDensity
        }

        fun getDensity(context: Context?): Float {
            if (density <= 0f) {
                if (context == null) return -1f
                density = context.resources.displayMetrics.density
            }
            return density
        }

        fun getScreenWidth(context: Context?): Int {
            if (widthPixels <= 0) {
                if (context == null) return -1
                widthPixels = context.resources.displayMetrics.widthPixels
            }
            return widthPixels
        }

        fun getScreenHeight(context: Context?): Int {
            if (heightPixels <= 0) {
                if (context == null) return -1
                heightPixels = context.resources.displayMetrics.heightPixels
            }
            return heightPixels
        }
    }

    /**
     * cannot be instantiated
     */
    init {
        throw UnsupportedOperationException("cannot be instantiated")
    }
}