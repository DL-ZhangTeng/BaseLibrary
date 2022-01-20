package com.zhangteng.utils

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import java.io.File
import java.io.FileOutputStream
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.min
import kotlin.math.sqrt

/**
 * description drawable转Bitmap
 * @param
 * @return Bitmap
 */
fun Drawable?.drawableToBitmap(): Bitmap? {
    if (this == null) return null
    val bitmap = Bitmap.createBitmap(
        intrinsicWidth,
        intrinsicHeight,
        if (opacity != PixelFormat.OPAQUE) Bitmap.Config.ARGB_8888 else Bitmap.Config.RGB_565
    )
    val canvas = Canvas(bitmap)
    setBounds(0, 0, intrinsicWidth, intrinsicHeight)
    draw(canvas)
    return bitmap
}

/**
 * description 保存bitmap
 * @param dir 文件路径
 * @param name 文件名
 * @return 保存是否成功
 */
fun Bitmap?.saveBitmap(dir: String?, name: String?): Boolean {
    if (this == null) return false
    val path = File(dir ?: File.separator)
    if (!path.exists()) {
        path.mkdirs()
    }
    val file = File("$path${File.separator}$name")
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
        compress(
            Bitmap.CompressFormat.PNG, 100,
            fileOutputStream
        )
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

/**
 * description 通过字节码创建bitmap
 * @param maxNumOfPixels 最大像素
 * @return bitmap
 */
fun ByteArray.makeBitmap(maxNumOfPixels: Int): Bitmap? {
    return try {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeByteArray(
            this, 0, size,
            options
        )
        if (options.mCancel || options.outWidth == -1 || options.outHeight == -1) {
            return null
        }
        options.inSampleSize = options.computeSampleSize(-1, maxNumOfPixels)
        options.inJustDecodeBounds = false
        options.inDither = false
        options.inPreferredConfig = Bitmap.Config.ARGB_8888
        BitmapFactory.decodeByteArray(
            this, 0, size,
            options
        )
    } catch (ex: NullPointerException) {
        null
    } catch (ex: OutOfMemoryError) {
        null
    }
}

/**
 * description 计算样品大小
 * @param minSideLength 最小像素
 * @param maxNumOfPixels 最大像素
 * @return
 */
fun BitmapFactory.Options.computeSampleSize(minSideLength: Int, maxNumOfPixels: Int): Int {
    val initialSize = computeInitialSampleSize(minSideLength, maxNumOfPixels)
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

/**
 * description 计算样品大小
 * @param minSideLength 最小像素
 * @param maxNumOfPixels 最大像素
 * @return
 */
fun BitmapFactory.Options.computeInitialSampleSize(minSideLength: Int, maxNumOfPixels: Int): Int {
    val w = outWidth.toDouble()
    val h = outHeight.toDouble()
    val lowerBound = if (maxNumOfPixels < 0) 1 else ceil(sqrt(w * h / maxNumOfPixels)).toInt()
    val upperBound = if (minSideLength < 0) 128 else min(
        floor(w / minSideLength),
        floor(h / minSideLength)
    ).toInt()
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
 * @param watermark
 * @param paddingLeft
 * @param paddingTop
 * @return
 */
fun Bitmap?.createWaterMaskLeftTop(
    context: Context?,
    watermark: Bitmap?,
    paddingLeft: Int,
    paddingTop: Int
): Bitmap? {
    if (this == null) return null
    return if (watermark == null) this else watermark.createWaterMaskBitmap(
        this,
        context.dp2px(paddingLeft.toFloat()),
        context.dp2px(paddingTop.toFloat())
    )
}

/**
 * 设置水印图片在右下角
 *
 * @param context       上下文
 * @param watermark
 * @param paddingRight
 * @param paddingBottom
 * @return
 */
fun Bitmap?.createWaterMaskRightBottom(
    context: Context?,
    watermark: Bitmap?,
    paddingRight: Int,
    paddingBottom: Int
): Bitmap? {
    if (this == null) return null
    return if (watermark == null) this else createWaterMaskBitmap(
        watermark,
        width - watermark.width - context.dp2px(paddingRight.toFloat()),
        height - watermark.height - context.dp2px(paddingBottom.toFloat())
    )
}

/**
 * 设置水印图片到右上角
 *
 * @param context
 * @param watermark
 * @param paddingRight
 * @param paddingTop
 * @return
 */
fun Bitmap?.createWaterMaskRightTop(
    context: Context?,
    watermark: Bitmap?,
    paddingRight: Int,
    paddingTop: Int
): Bitmap? {
    if (this == null) return null
    return if (watermark == null) this else createWaterMaskBitmap(
        watermark,
        width - watermark.width - context.dp2px(paddingRight.toFloat()),
        context.dp2px(paddingTop.toFloat())
    )
}

/**
 * 设置水印图片到左下角
 *
 * @param context
 * @param watermark
 * @param paddingLeft
 * @param paddingBottom
 * @return
 */
fun Bitmap?.createWaterMaskLeftBottom(
    context: Context?,
    watermark: Bitmap?,
    paddingLeft: Int,
    paddingBottom: Int
): Bitmap? {
    if (this == null) return null
    return if (watermark == null) this else createWaterMaskBitmap(
        watermark, context.dp2px(paddingLeft.toFloat()),
        height - watermark.height - context.dp2px(paddingBottom.toFloat())
    )
}

/**
 * 设置水印图片到中下角
 *
 * @param context
 * @param watermark
 * @return
 */
fun Bitmap?.createWaterMaskCenterBottom(context: Context?, watermark: Bitmap?): Bitmap? {
    if (this == null) return null
    return if (watermark == null) this else createWaterMaskBitmap(
        watermark, 0,
        height * 2 / 3
    )
}

/**
 * 设置水印图片到中间
 *
 * @param watermark
 * @return
 */
fun Bitmap?.createWaterMaskCenter(watermark: Bitmap?): Bitmap? {
    if (this == null) return null
    return if (watermark == null) this else createWaterMaskBitmap(
        watermark,
        (width - watermark.width) / 2,
        (height - watermark.height) / 2
    )
}

/**
 * description 创建新的bitmap
 * @param newWidth
 * @param newHeight
 * @return
 */
fun Bitmap?.getNewBitmap(newWidth: Int, newHeight: Int): Bitmap? {
    if (this == null) return null
    // 获得图片的宽高.
    val width = width
    val height = height
    // 计算缩放比例.
    val scaleWidth = newWidth.toFloat() / width
    val scaleHeight = newHeight.toFloat() / height
    // 取得想要缩放的matrix参数.
    val matrix = Matrix()
    matrix.postScale(scaleWidth, scaleHeight)
    // 得到新的图片.
    return Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
}

/**
 * description 添加水印
 * @param watermark 水印图片
 * @param paddingLeft 距左px
 * @param paddingTop 距顶px
 * @return
 */
fun Bitmap?.createWaterMaskBitmap(
    watermark: Bitmap?,
    paddingLeft: Int = 0,
    paddingTop: Int = 0
): Bitmap? {
    if (this == null) return null
    if (watermark == null) return this
    val width = width
    val height = height
    val bitmaptwo = watermark.getNewBitmap(width, height / 3)
    //创建一个bitmap
    val newb =
        Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888) // 创建一个新的和SRC长度宽度一样的位图
    //将该图片作为画布
    val canvas = Canvas(newb)
    //在画布 0，0坐标上开始绘制原始图片
    canvas.drawBitmap(this, 0f, 0f, null)
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
 * 给图片添加文字到左上角
 *
 * @param context
 * @param text
 * @return
 */
fun Bitmap?.drawTextToLeftTop(
    context: Context?,
    text: String?,
    size: Int,
    color: Int,
    paddingLeft: Int,
    paddingTop: Int
): Bitmap? {
    if (this == null || text == null) return null
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    paint.color = color
    paint.textSize = context.dp2px(size.toFloat()).toFloat()
    val bounds = Rect()
    paint.getTextBounds(text, 0, text.length, bounds)
    return drawTextToBitmap(
        text,
        paint,
        context.dp2px(paddingLeft.toFloat()),
        context.dp2px(paddingTop.toFloat()) + bounds.height()
    )
}

/**
 * 绘制文字到右下角
 *
 * @param context
 * @param text
 * @param size
 * @param color
 * @return
 */
fun Bitmap?.drawTextToRightBottom(
    context: Context?,
    text: String?,
    size: Int,
    color: Int,
    paddingRight: Int,
    paddingBottom: Int
): Bitmap? {
    if (this == null || text == null) return null
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    paint.color = color
    paint.textSize = context.dp2px(size.toFloat()).toFloat()
    val bounds = Rect()
    paint.getTextBounds(text, 0, text.length, bounds)
    return drawTextToBitmap(
        text,
        paint,
        width - bounds.width() - context.dp2px(paddingRight.toFloat()),
        height - context.dp2px(paddingBottom.toFloat())
    )
}

/**
 * 绘制文字到右上方
 *
 * @param context
 * @param text
 * @param size
 * @param color
 * @param paddingRight
 * @param paddingTop
 * @return
 */
fun Bitmap?.drawTextToRightTop(
    context: Context?,
    text: String?,
    size: Int,
    color: Int,
    paddingRight: Int,
    paddingTop: Int
): Bitmap? {
    if (this == null || text == null) return null
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    paint.color = color
    paint.textSize = context.dp2px(size.toFloat()).toFloat()
    val bounds = Rect()
    paint.getTextBounds(text, 0, text.length, bounds)
    return drawTextToBitmap(
        text,
        paint,
        width - bounds.width() - context.dp2px(paddingRight.toFloat()),
        context.dp2px(paddingTop.toFloat()) + bounds.height()
    )
}

/**
 * 绘制文字到左下方
 *
 * @param context
 * @param text
 * @param size
 * @param color
 * @param paddingLeft
 * @param paddingBottom
 * @return
 */
fun Bitmap?.drawTextToLeftBottom(
    context: Context?,
    text: String?,
    size: Int,
    color: Int,
    paddingLeft: Int,
    paddingBottom: Int
): Bitmap? {
    if (this == null || text == null) return null
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    paint.color = color
    paint.textSize = context.dp2px(size.toFloat()).toFloat()
    val bounds = Rect()
    paint.getTextBounds(text, 0, text.length, bounds)
    return drawTextToBitmap(
        text,
        paint,
        context.dp2px(paddingLeft.toFloat()),
        height - context.dp2px(paddingBottom.toFloat())
    )
}

/**
 * 绘制文字到中间
 *
 * @param context
 * @param text
 * @param size
 * @param color
 * @return
 */
fun Bitmap?.drawTextToCenter(
    context: Context?,
    text: String?,
    size: Int,
    color: Int
): Bitmap? {
    if (this == null || text == null) return null
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    paint.color = color
    paint.textSize = context.dp2px(size.toFloat()).toFloat()
    val bounds = Rect()
    paint.getTextBounds(text, 0, text.length, bounds)
    return drawTextToBitmap(
        text,
        paint,
        (width - bounds.width()) / 2,
        (height + bounds.height()) / 2
    )
}

/**
 * description 图片上绘制文字
 * @param
 * @return
 */
fun Bitmap?.drawTextToBitmap(
    text: String?,
    paint: Paint?,
    paddingLeft: Int,
    paddingTop: Int
): Bitmap? {
    if (this == null || text == null || paint == null) return null
    var bitmap: Bitmap = this
    var bitmapConfig = bitmap.config
    paint.isDither = true // 获取跟清晰的图像采样
    paint.isFilterBitmap = true // 过滤一些
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
 * @param w
 * @param h
 * @return
 */
fun Bitmap?.scaleWithWH(w: Double, h: Double): Bitmap? {
    return if (w == 0.0 || h == 0.0 || this == null) {
        this
    } else {
        // 记录src的宽高
        val width = width
        val height = height
        // 创建一个matrix容器
        val matrix = Matrix()
        // 计算缩放比例
        val scaleWidth = (w / width).toFloat()
        val scaleHeight = (h / height).toFloat()
        // 开始缩放
        matrix.postScale(scaleWidth, scaleHeight)
        // 创建缩放后的图片
        Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
    }
}