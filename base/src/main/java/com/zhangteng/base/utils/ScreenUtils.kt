package com.zhangteng.base.utils

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.Point
import android.graphics.Rect
import android.os.Build
import android.util.DisplayMetrics
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.annotation.RequiresApi

/**
 * 屏幕相关辅助类
 */
open class ScreenUtils private constructor() {
    companion object {
        /**
         * 获得屏幕宽度
         *
         * @param context
         * @return
         */
        fun getScreenWidth(context: Context?): Int {
            context ?: return -1
            val wm = context
                    .getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val outMetrics = DisplayMetrics()
            wm.defaultDisplay.getMetrics(outMetrics)
            return outMetrics.widthPixels
        }

        /**
         * 获得屏幕高度
         *
         * @param context
         * @return
         */
        fun getScreenHeight(context: Context?): Int {
            context ?: return -1
            val wm = context
                    .getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val outMetrics = DisplayMetrics()
            wm.defaultDisplay.getMetrics(outMetrics)
            return outMetrics.heightPixels
        }

        /**
         * 获得屏幕大小
         *
         * @param context
         * @return
         */
        fun getScreenSize(context: Context?): Point? {
            context ?: return null
            val point = Point()
            val wm = context
                    .getSystemService(Context.WINDOW_SERVICE) as WindowManager
            wm.defaultDisplay.getSize(point)
            return point
        }

        /**
         * 获得状态栏的高度
         *
         * @param context
         * @return
         */
        fun getStatusHeight(context: Context?): Int {
            var statusHeight = -1
            context ?: return statusHeight
            try {
                val clazz = Class.forName("com.android.internal.R\$dimen")
                val `object` = clazz.newInstance()
                val height = clazz.getField("status_bar_height")[`object`].toString().toInt()
                statusHeight = context.getResources().getDimensionPixelSize(height)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return statusHeight
        }

        /**
         * 获取当前屏幕截图，包含状态栏
         *
         * @param activity
         * @return
         */
        fun snapShotWithStatusBar(activity: Activity?): Bitmap? {
            activity ?: return null
            val view = activity.window.decorView
            view.isDrawingCacheEnabled = true
            view.buildDrawingCache()
            val bmp = view.drawingCache
            val width = getScreenWidth(activity)
            val height = getScreenHeight(activity)
            var bp: Bitmap? = null
            bp = Bitmap.createBitmap(bmp, 0, 0, width, height)
            view.destroyDrawingCache()
            return bp
        }

        /**
         * 获取当前屏幕截图，不包含状态栏
         *
         * @param activity
         * @return
         */
        fun snapShotWithoutStatusBar(activity: Activity?): Bitmap? {
            activity ?: return null
            val view = activity.window.decorView
            view.isDrawingCacheEnabled = true
            view.buildDrawingCache()
            val bmp = view.drawingCache
            val frame = Rect()
            activity.window.decorView.getWindowVisibleDisplayFrame(frame)
            val statusBarHeight = frame.top
            val width = getScreenWidth(activity)
            val height = getScreenHeight(activity)
            var bp: Bitmap? = null
            bp = Bitmap.createBitmap(bmp, 0, statusBarHeight, width, height
                    - statusBarHeight)
            view.destroyDrawingCache()
            return bp
        }

        /**
         * 判断虚拟按键栏是否重写
         *
         * @return
         */
        private fun getNavBarOverride(): String? {
            var sNavBarOverride: String? = null
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                try {
                    val c = Class.forName("android.os.SystemProperties")
                    val m = c.getDeclaredMethod("get", String::class.java)
                    m.isAccessible = true
                    sNavBarOverride = m.invoke(null, "qemu.hw.mainkeys") as String
                } catch (e: Throwable) {
                }
            }
            return sNavBarOverride
        }

        /**
         * 获取虚拟按键的高度
         */
        fun getVirtualBarHeight(context: Context?): Int {
            var vh = 0
            context ?: return vh
            val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val display = windowManager.defaultDisplay
            val dm = DisplayMetrics()
            vh = try {
                val c = Class.forName("android.view.Display")
                val method = c.getMethod("getRealMetrics", DisplayMetrics::class.java)
                method.invoke(display, dm)
                dm.heightPixels - display.height
            } catch (e: Exception) {
                return 0
            }
            return vh
        }

        /**
         * 判断虚拟导航栏是否显示
         *
         * @param context 上下文对象
         * @param window  当前窗口
         * @return true(显示虚拟导航栏)，false(不显示或不支持虚拟导航栏)
         */
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
        fun checkNavigationBarShow(context: Context, window: Window): Boolean {
            val show: Boolean
            val display = window.windowManager.defaultDisplay
            val point = Point()
            display.getRealSize(point)
            val decorView = window.decorView
            val conf = context.resources.configuration
            show = if (Configuration.ORIENTATION_LANDSCAPE == conf.orientation) {
                val contentView = decorView.findViewById<View?>(android.R.id.content)
                point.x != contentView?.width
            } else {
                val rect = Rect()
                decorView.getWindowVisibleDisplayFrame(rect)
                rect.bottom != point.y
            }
            return show
        }
    }

    init {
        /*cannot be instantiated*/
        throw UnsupportedOperationException("cannot be instantiated")
    }
}