package com.zhangteng.base.utils

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import kotlin.math.*

/**
 * bitmap处理工具类
 */
object BitmapUtils {
    private val TAG: String? = "BitmapUtils"
    fun drawableToBitmap(drawable: Drawable?): Bitmap? {
        if (drawable == null) return null
        val bitmap = Bitmap.createBitmap(
                drawable.intrinsicWidth,
                drawable.intrinsicHeight,
                if (drawable.opacity != PixelFormat.OPAQUE) Bitmap.Config.ARGB_8888 else Bitmap.Config.RGB_565)
        val canvas = Canvas(bitmap)

        //canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
        drawable.draw(canvas)
        return bitmap
    }

    fun saveBitmap(bitmap: Bitmap?, dir: String?, name: String?, isShowPhotos: Boolean): Boolean {
        val path = File(dir)
        if (!path.exists()) {
            path.mkdirs()
        }
        val file = File("$path/$name")
        if (file.exists()) {
            file.delete()
        }
        if (!file.exists()) {
            try {
                file.createNewFile()
            } catch (e: Exception) {
                e.printStackTrace()
                return false
            }
        } else {
            return true
        }
        var fileOutputStream: FileOutputStream? = null
        try {
            fileOutputStream = FileOutputStream(file)
            bitmap!!.compress(Bitmap.CompressFormat.PNG, 100,
                    fileOutputStream)
            fileOutputStream.flush()
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        } finally {
            try {
                fileOutputStream?.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return true
    }

    fun makeBitmap(jpegData: ByteArray?, maxNumOfPixels: Int): Bitmap? {
        return try {
            val options = BitmapFactory.Options()
            options.inJustDecodeBounds = true
            BitmapFactory.decodeByteArray(jpegData, 0, jpegData!!.size,
                    options)
            if (options.mCancel || options.outWidth == -1 || options.outHeight == -1) {
                return null
            }
            options.inSampleSize = computeSampleSize(
                    options, -1, maxNumOfPixels)
            options.inJustDecodeBounds = false
            options.inDither = false
            options.inPreferredConfig = Bitmap.Config.ARGB_8888
            BitmapFactory.decodeByteArray(jpegData, 0, jpegData.size,
                    options)
        } catch (ex: NullPointerException) {
            null
        } catch (ex: OutOfMemoryError) {
            Log.e(TAG, "Got oom exception ", ex)
            null
        }
    }

    fun computeSampleSize(options: BitmapFactory.Options, minSideLength: Int, maxNumOfPixels: Int): Int {
        val initialSize = computeInitialSampleSize(options, minSideLength,
                maxNumOfPixels)
        var roundedSize: Int
        if (initialSize <= 8) {
            roundedSize = 1
            while (roundedSize < initialSize) {
                roundedSize = roundedSize shl 1
            }
        } else {
            roundedSize = (initialSize + 7) / 8 * 8
        }
        return roundedSize
    }

    private fun computeInitialSampleSize(options: BitmapFactory.Options, minSideLength: Int, maxNumOfPixels: Int): Int {
        val w = options.outWidth.toDouble()
        val h = options.outHeight.toDouble()
        val lowerBound = if (maxNumOfPixels < 0) 1 else ceil(sqrt(w * h / maxNumOfPixels)).toInt()
        val upperBound = if (minSideLength < 0) 128 else min(floor(w / minSideLength),
                floor(h / minSideLength)).toInt()
        if (upperBound < lowerBound) {
            // return the larger one when there is no overlapping zone.
            return lowerBound
        }
        return if (maxNumOfPixels < 0 && minSideLength < 0) {
            1
        } else if (minSideLength < 0) {
            lowerBound
        } else {
            upperBound
        }
    }

    /**
     * 设置水印图片在左上角
     *
     * @param context     上下文
     * @param src
     * @param watermark
     * @param paddingLeft
     * @param paddingTop
     * @return
     */
    fun createWaterMaskLeftTop(context: Context?, src: Bitmap?, watermark: Bitmap?, paddingLeft: Int, paddingTop: Int): Bitmap? {
        if (src == null) return null
        return if (watermark == null) src else createWaterMaskBitmap(src, watermark,
                dp2px(context, paddingLeft.toFloat()), dp2px(context, paddingTop.toFloat()))
    }

    fun getNewBitmap(bitmap: Bitmap?, newWidth: Int, newHeight: Int): Bitmap? {
        if (bitmap == null) return null
        // 获得图片的宽高.
        val width = bitmap.width
        val height = bitmap.height
        // 计算缩放比例.
        val scaleWidth = newWidth.toFloat() / width
        val scaleHeight = newHeight.toFloat() / height
        // 取得想要缩放的matrix参数.
        val matrix = Matrix()
        matrix.postScale(scaleWidth, scaleHeight)
        // 得到新的图片.
        return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true)
    }

    private fun createWaterMaskBitmap(src: Bitmap?, watermark: Bitmap?, paddingLeft: Int, paddingTop: Int): Bitmap? {
        if (src == null) return null
        if (watermark == null) return src
        val width = src.width
        val height = src.height
        val bitmaptwo = getNewBitmap(watermark, width, height / 3)
        //创建一个bitmap
        val newb = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888) // 创建一个新的和SRC长度宽度一样的位图
        //将该图片作为画布
        val canvas = Canvas(newb)
        //在画布 0，0坐标上开始绘制原始图片
        canvas.drawBitmap(src, 0f, 0f, null)
        //在画布上绘制水印图片
        if (bitmaptwo != null) {
            canvas.drawBitmap(bitmaptwo, paddingLeft.toFloat(), paddingTop.toFloat(), null)
        }
        // 保存
        canvas.save()
        // 存储
        canvas.restore()
        return newb
    }

    /**
     * 设置水印图片在右下角
     *
     * @param context       上下文
     * @param src
     * @param watermark
     * @param paddingRight
     * @param paddingBottom
     * @return
     */
    fun createWaterMaskRightBottom(context: Context?, src: Bitmap?, watermark: Bitmap?, paddingRight: Int, paddingBottom: Int): Bitmap? {
        if (src == null) return null
        return if (watermark == null) src else createWaterMaskBitmap(src, watermark,
                src.width - watermark.width - dp2px(context, paddingRight.toFloat()),
                src.height - watermark.height - dp2px(context, paddingBottom.toFloat()))
    }

    /**
     * 设置水印图片到右上角
     *
     * @param context
     * @param src
     * @param watermark
     * @param paddingRight
     * @param paddingTop
     * @return
     */
    fun createWaterMaskRightTop(context: Context?, src: Bitmap?, watermark: Bitmap?, paddingRight: Int, paddingTop: Int): Bitmap? {
        if (src == null) return null
        return if (watermark == null) src else createWaterMaskBitmap(src, watermark,
                src.width - watermark.width - dp2px(context, paddingRight.toFloat()),
                dp2px(context, paddingTop.toFloat()))
    }

    /**
     * 设置水印图片到左下角
     *
     * @param context
     * @param src
     * @param watermark
     * @param paddingLeft
     * @param paddingBottom
     * @return
     */
    fun createWaterMaskLeftBottom(context: Context?, src: Bitmap?, watermark: Bitmap?, paddingLeft: Int, paddingBottom: Int): Bitmap? {
        if (src == null) return null
        return if (watermark == null) src else createWaterMaskBitmap(src, watermark, dp2px(context, paddingLeft.toFloat()),
                src.height - watermark.height - dp2px(context, paddingBottom.toFloat()))
    }

    /**
     * 设置水印图片到中下角
     *
     * @param context
     * @param src
     * @param watermark
     * @return
     */
    fun createWaterMaskCenterBottom(context: Context?, src: Bitmap?, watermark: Bitmap?): Bitmap? {
        if (src == null) return null
        return if (watermark == null) src else createWaterMaskBitmap(src, watermark, 0,
                src.height * 2 / 3)
    }

    /**
     * 设置水印图片到中间
     *
     * @param src
     * @param watermark
     * @return
     */
    fun createWaterMaskCenter(src: Bitmap?, watermark: Bitmap?): Bitmap? {
        if (src == null) return null
        return if (watermark == null) src else createWaterMaskBitmap(src, watermark,
                (src.width - watermark.width) / 2,
                (src.height - watermark.height) / 2)
    }

    /**
     * 给图片添加文字到左上角
     *
     * @param context
     * @param bitmap
     * @param text
     * @return
     */
    fun drawTextToLeftTop(context: Context?, bitmap: Bitmap?, text: String?, size: Int, color: Int, paddingLeft: Int, paddingTop: Int): Bitmap? {
        if (bitmap == null || text == null) return null
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.color = color
        paint.textSize = dp2px(context, size.toFloat()).toFloat()
        val bounds = Rect()
        paint.getTextBounds(text, 0, text.length, bounds)
        return drawTextToBitmap(context, bitmap, text, paint, bounds,
                dp2px(context, paddingLeft.toFloat()),
                dp2px(context, paddingTop.toFloat()) + bounds.height())
    }

    /**
     * 绘制文字到右下角
     *
     * @param context
     * @param bitmap
     * @param text
     * @param size
     * @param color
     * @return
     */
    fun drawTextToRightBottom(context: Context?, bitmap: Bitmap?, text: String?, size: Int, color: Int, paddingRight: Int, paddingBottom: Int): Bitmap? {
        if (bitmap == null || text == null) return null
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.color = color
        paint.textSize = dp2px(context, size.toFloat()).toFloat()
        val bounds = Rect()
        paint.getTextBounds(text, 0, text.length, bounds)
        return drawTextToBitmap(context, bitmap, text, paint, bounds,
                bitmap.width - bounds.width() - dp2px(context, paddingRight.toFloat()),
                bitmap.height - dp2px(context, paddingBottom.toFloat()))
    }

    /**
     * 绘制文字到右上方
     *
     * @param context
     * @param bitmap
     * @param text
     * @param size
     * @param color
     * @param paddingRight
     * @param paddingTop
     * @return
     */
    fun drawTextToRightTop(context: Context?, bitmap: Bitmap?, text: String?, size: Int, color: Int, paddingRight: Int, paddingTop: Int): Bitmap? {
        if (bitmap == null || text == null) return null
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.color = color
        paint.textSize = dp2px(context, size.toFloat()).toFloat()
        val bounds = Rect()
        paint.getTextBounds(text, 0, text.length, bounds)
        return drawTextToBitmap(context, bitmap, text, paint, bounds,
                bitmap.width - bounds.width() - dp2px(context, paddingRight.toFloat()),
                dp2px(context, paddingTop.toFloat()) + bounds.height())
    }

    /**
     * 绘制文字到左下方
     *
     * @param context
     * @param bitmap
     * @param text
     * @param size
     * @param color
     * @param paddingLeft
     * @param paddingBottom
     * @return
     */
    fun drawTextToLeftBottom(context: Context?, bitmap: Bitmap?, text: String?, size: Int, color: Int, paddingLeft: Int, paddingBottom: Int): Bitmap? {
        if (bitmap == null || text == null) return null
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.color = color
        paint.textSize = dp2px(context, size.toFloat()).toFloat()
        val bounds = Rect()
        paint.getTextBounds(text, 0, text.length, bounds)
        return drawTextToBitmap(context, bitmap, text, paint, bounds,
                dp2px(context, paddingLeft.toFloat()),
                bitmap.height - dp2px(context, paddingBottom.toFloat()))
    }

    /**
     * 绘制文字到中间
     *
     * @param context
     * @param bitmap
     * @param text
     * @param size
     * @param color
     * @return
     */
    fun drawTextToCenter(context: Context?, bitmap: Bitmap?, text: String?, size: Int, color: Int): Bitmap? {
        if (bitmap == null || text == null) return null
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.color = color
        paint.textSize = dp2px(context, size.toFloat()).toFloat()
        val bounds = Rect()
        paint.getTextBounds(text, 0, text.length, bounds)
        return drawTextToBitmap(context, bitmap, text, paint, bounds,
                (bitmap.width - bounds.width()) / 2,
                (bitmap.height + bounds.height()) / 2)
    }

    //图片上绘制文字
    private fun drawTextToBitmap(context: Context?, bitmap: Bitmap?, text: String?, paint: Paint?, bounds: Rect?, paddingLeft: Int, paddingTop: Int): Bitmap? {
        if (bitmap == null || text == null || paint == null) return null
        var bitmap: Bitmap = bitmap
        var bitmapConfig = bitmap.config
        paint.setDither(true) // 获取跟清晰的图像采样
        paint.setFilterBitmap(true) // 过滤一些
        if (bitmapConfig == null) {
            bitmapConfig = Bitmap.Config.ARGB_8888
        }
        bitmap = bitmap.copy(bitmapConfig, true)
        val canvas = Canvas(bitmap)
        canvas.drawText(text, paddingLeft.toFloat(), paddingTop.toFloat(), paint)
        return bitmap
    }

    /**
     * 缩放图片
     *
     * @param src
     * @param w
     * @param h
     * @return
     */
    fun scaleWithWH(src: Bitmap?, w: Double, h: Double): Bitmap? {
        return if (w == 0.0 || h == 0.0 || src == null) {
            src
        } else {
            // 记录src的宽高
            val width = src.width
            val height = src.height
            // 创建一个matrix容器
            val matrix = Matrix()
            // 计算缩放比例
            val scaleWidth = (w / width).toFloat()
            val scaleHeight = (h / height).toFloat()
            // 开始缩放
            matrix.postScale(scaleWidth, scaleHeight)
            // 创建缩放后的图片
            Bitmap.createBitmap(src, 0, 0, width, height, matrix, true)
        }
    }

    /**
     * dip转pix
     *
     * @param context
     * @param dp
     * @return
     */
    fun dp2px(context: Context?, dp: Float): Int {
        if (context == null) return dp.roundToInt()
        val scale = context.getResources().displayMetrics.density
        return (dp * scale + 0.5f).toInt()
    }
}