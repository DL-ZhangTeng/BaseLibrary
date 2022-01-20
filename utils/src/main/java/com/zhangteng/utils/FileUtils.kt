package com.zhangteng.utils

import android.content.Context
import android.os.Environment
import android.text.TextUtils
import android.util.Log
import java.io.*
import java.net.URLConnection
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

/**
 * 在指定的位置创建文件夹
 *
 * @return 若创建成功，则返回True；反之，则返回False
 */
fun String.mkDir(): Boolean {
    return File(this).mkdirs()
}

/**
 * 删除指定的文件
 *
 * @return 若删除成功，则返回True；反之，则返回False
 */
fun String.deleteFile(): Boolean {
    var result = false
    val file = File(this)
    if (file.exists()) {
        result = file.delete()
    }
    return result
}

/**
 * 删除指定的文件夹
 *
 * @param delFile 文件夹中是否包含文件
 * @return 若删除成功，则返回True；反之，则返回False
 */
fun String.deleteDir(delFile: Boolean): Boolean {
    val file = File(this)
    if (!file.exists()) return true
    return if (delFile) {
        if (file.isFile) {
            file.delete()
        } else if (file.isDirectory) {
            if (file.listFiles() == null || file.listFiles()!!.isEmpty()) {
                file.delete()
            } else {
                val zfiles = file.listFiles()!!.size
                val delfile = file.listFiles()
                for (i in 0 until zfiles) {
                    if (delfile!![i].isDirectory) {
                        delfile[i].absolutePath.deleteDir(true)
                    }
                    delfile[i].delete()
                }
                file.delete()
            }
        } else {
            false
        }
    } else {
        file.delete()
    }
}

/**
 * 复制文件/文件夹 若要进行文件夹复制，请勿将目标文件夹置于源文件夹中
 *
 * @param target   目标文件（夹）
 * @param isFolder 若进行文件夹复制，则为True；反之为False
 * @throws IOException
 */
@Throws(IOException::class)
fun String?.copy(target: String?, isFolder: Boolean) {
    if (this == null || target == null) return
    if (isFolder) {
        File(target).mkdirs()
        val a = File(this)
        val file = a.list() ?: return
        var temp: File? = null
        for (i in file.indices) {
            temp = if (this.endsWith(File.separator)) {
                File(this + file[i])
            } else {
                File(this + File.separator + file[i])
            }
            if (temp.isFile) {
                val input = FileInputStream(temp)
                val output = FileOutputStream(target + "/" + temp.name.toString())
                val b = ByteArray(1024)
                var len: Int
                while (input.read(b).also { len = it } != -1) {
                    output.write(b, 0, len)
                }
                output.flush()
                output.close()
                input.close()
            }
            if (temp.isDirectory) {
                (this + "/" + file[i]).copy(target + "/" + file[i], true)
            }
        }
    } else {
        var byteread = 0
        val oldfile = File(this)
        if (oldfile.exists()) {
            val inStream: InputStream = FileInputStream(this)
            val file = File(target)
            file.parentFile?.mkdirs()
            file.createNewFile()
            val fs = FileOutputStream(file)
            val buffer = ByteArray(1024)
            while (inStream.read(buffer).also { byteread = it } != -1) {
                fs.write(buffer, 0, byteread)
            }
            inStream.close()
            fs.close()
        }
    }
}

/**
 * 移动指定的文件（夹）到目标文件（夹）
 *
 * @param target   目标文件（夹）
 * @param isFolder 若为文件夹，则为True；反之为False
 * @return
 * @throws IOException
 */
@Throws(IOException::class)
fun String.move(target: String?, isFolder: Boolean): Boolean {
    copy(target, isFolder)
    return if (isFolder) {
        deleteDir(true)
    } else {
        deleteFile()
    }
}

/**
 * 获取文件指定文件的指定单位的大小
 *
 * @param sizeType 获取大小的类型1为B、2为KB、3为MB、4为GB
 * @return double值的大小
 */
fun String?.getFileOrFilesSize(sizeType: Int): Double {
    if (TextUtils.isEmpty(this)) return 0.0
    val file = File(this!!)
    var blockSize: Long = 0
    try {
        blockSize = if (file.isDirectory) {
            file.getFilesSize()
        } else {
            file.getFileSize()
        }
    } catch (e: Exception) {
        e.printStackTrace()
        Log.e("获取文件大小", "获取失败!")
    }
    return blockSize.formatFileSize(sizeType)
}

/**
 * 调用此方法自动计算指定文件或指定文件夹的大小
 *
 * @return 计算好的带B、KB、MB、GB的字符串
 */
fun String?.getFileOrFilesSize(): String? {
    if (TextUtils.isEmpty(this)) return null
    val file = File(this!!)
    var blockSize: Long = 0
    try {
        blockSize = if (file.isDirectory) {
            file.getFilesSize()
        } else {
            file.getFileSize()
        }
    } catch (e: Exception) {
        e.printStackTrace()
        Log.e("获取文件大小", "获取失败!")
    }
    return blockSize.formatFileSize()
}

/**
 * 根据文件后缀名判断 文件是否是视频文件
 *
 * @return 是否是视频文件
 */
fun String?.isVideoFile(): Boolean {
    if (TextUtils.isEmpty(this)) return false
    val index = this!!.lastIndexOf(".")
    if (index == -1) return false
    val mimeType = substring(index).getMimeType()
    return if (TextUtils.isEmpty(mimeType)) false else mimeType!!.contains("video/")
}

/**
 * 根据文件mimeTyep判断 文件是否是视频文件
 *
 * @return 是否是视频文件
 */
fun String?.isVideoFileAsMime(): Boolean {
    return !TextUtils.isEmpty(this) && this!!.contains("video/")
}

/**
 * Get the Mime Type from a File
 *
 * @return 返回MIME类型
 */
fun String?.getMimeType(): String? {
    val fileNameMap = URLConnection.getFileNameMap()
    var type: String? = null
    try {
        type = fileNameMap.getContentTypeFor(this)
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return type
}

/**
 * 获取指定文件大小
 *
 * @return
 */
@Throws(Exception::class)
fun File?.getFileSize(): Long {
    var size: Long = 0
    if (this != null && exists()) {
        val fis = FileInputStream(this)
        size = fis.available().toLong()
    } else {
        Log.e("获取文件大小", "文件不存在!")
    }
    return size
}

/**
 * 获取指定文件夹大小
 *
 * @return
 */
@Throws(Exception::class)
fun File?.getFilesSize(): Long {
    var size: Long = 0
    if (this != null && exists() && isDirectory) {
        val flist = listFiles()
        for (file in flist) {
            size = if (file.isDirectory) {
                size + file.getFilesSize()
            } else {
                size + file.getFileSize()
            }
        }
    }
    return size
}

/**
 * 转换文件大小
 *
 * @return
 */
fun Long.formatFileSize(): String {
    val df = DecimalFormat("#.00")
    var fileSizeString = ""
    val wrongSize = "0B"
    if (this == 0L) {
        return wrongSize
    }
    fileSizeString = when {
        this < 1024 -> {
            df.format(toDouble()) + "B"
        }
        this < 1048576 -> {
            df.format(toDouble() / 1024) + "KB"
        }
        this < 1073741824 -> {
            df.format(toDouble() / 1048576) + "MB"
        }
        else -> {
            df.format(toDouble() / 1073741824) + "GB"
        }
    }
    return fileSizeString
}

/**
 * 转换文件大小,指定转换的类型
 *
 * @param sizeType 1:b 2:kb 3:mb 4:gb
 * @return
 */
fun Long.formatFileSize(sizeType: Int = 1): Double {
    val df = DecimalFormat("#.00")
    var fileSizeLong = 0.0
    when (sizeType) {
        1 -> fileSizeLong = df.format(toDouble()).toDouble()
        2 -> fileSizeLong = df.format(toDouble() / 1024).toDouble()
        3 -> fileSizeLong = df.format(toDouble() / 1048576).toDouble()
        4 -> fileSizeLong = df.format(toDouble() / 1073741824).toDouble()
    }
    return fileSizeLong
}

/**
 * 获取缓存文件夹
 *
 * @return
 */
fun Context?.getDiskCacheDir(): String? {
    if (this == null) return this?.externalCacheDir?.absolutePath
    //isExternalStorageEmulated()设备的外存是否是用内存模拟的，是则返回true
    return if ((Environment.MEDIA_MOUNTED == Environment.getExternalStorageState() || !Environment.isExternalStorageEmulated()) && externalCacheDir != null) {
        externalCacheDir!!.absolutePath
    } else {
        cacheDir.absolutePath
    }
}

/**
 * 获取图片文件夹
 *
 * @return 文件夹路径
 */
val pictureDir: String
    get() = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).absolutePath

/**
 * 获取视频文件夹
 *
 * @return 文件夹路径
 */
val videoDir: String
    get() = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES).absolutePath

/**
 * 获取媒体文件夹
 *
 * @return
 */
fun Context?.getFilesDir(): String? {
    if (this == null) return this?.filesDir?.absolutePath
    //isExternalStorageEmulated()设备的外存是否是用内存模拟的，是则返回true
    return if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState() || !Environment.isExternalStorageEmulated()) {
        Environment.getExternalStorageDirectory().absolutePath
    } else {
        filesDir.absolutePath
    }
}

/**
 * 在缓存路径里创建文件
 *
 * @param rootPath 文件夹根路径
 * @param filePath 相对路径
 * @return file
 */
fun Context?.createImageFile(rootPath: String, filePath: String): File {
    val timeStamp = SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA).format(Date())
    val dir = File(rootPath + filePath)
    if (!dir.exists()) {
        dir.mkdirs()
    }
    return File(dir, "$timeStamp.jpg")
}

/**
 * 在缓存路径里创建初始文件夹。保存拍摄图片和剪裁后的图片
 *
 * @param rootPath 文件夹根路径
 * @param filePath 相对路径
 */
fun Context?.createFile(rootPath: String, filePath: String) {
    val dir = File(rootPath + filePath)
    val cropFile = File("$rootPath$filePath/crop")
    if (!dir.exists()) {
        if (!dir.isDirectory) {
            dir.delete()
        }
        dir.mkdirs()
    }
    if (!cropFile.exists()) {
        if (cropFile.parentFile != null && !cropFile.parentFile!!.isDirectory) {
            cropFile.parentFile!!.delete()
        }
        cropFile.mkdirs()
    }
    val file = File(cropFile, ".nomedia") // 创建忽视文件。   有该文件，系统将检索不到此文件夹下的图片。
    if (!file.exists()) {
        try {
            file.createNewFile()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}

/**
 * 获取缓存路径里的剪裁文件夹
 *
 * @param rootPath 文件夹根路径
 * @param path 相对路径
 */
fun Context?.getCropDir(rootPath: String, path: String): File {
    val cropFile = File("$rootPath$path/crop")
    if (!cropFile.exists()) {
        if (cropFile.parentFile != null && !cropFile.parentFile!!.isDirectory) {
            cropFile.parentFile!!.delete()
        }
        cropFile.mkdirs()
    }
    return cropFile
}

/**
 * @param fileName 目标文件路径
 * @description 将assets文件转成json
 * @author: Swing 763263311@qq.com
 * @date: 2020/12/28 0028 上午 10:49
 */
fun Context?.getJsonFromAssets(fileName: String?): String? {
    if (this == null || fileName == null) return null
    val sb = StringBuilder()
    val am = assets
    try {
        val br = BufferedReader(
            InputStreamReader(
                am.open(fileName)
            )
        )
        var next: String?
        while (null != br.readLine().also { next = it }) {
            sb.append(next)
        }
    } catch (e: IOException) {
        e.printStackTrace()
        sb.delete(0, sb.length)
    }
    return sb.toString().trim { it <= ' ' }
}