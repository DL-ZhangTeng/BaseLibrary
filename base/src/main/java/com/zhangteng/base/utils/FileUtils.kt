package com.zhangteng.base.utils

import android.content.Context
import android.graphics.Bitmap
import android.os.Environment
import android.text.TextUtils
import android.util.Log
import com.zhangteng.base.base.BaseApplication
import java.io.*
import java.net.URLConnection
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

/**
 * 文件操作工具类
 */
object FileUtils {
    const val SIZETYPE_B = 1 //获取文件大小单位为B的double值
    const val SIZETYPE_KB = 2 //获取文件大小单位为KB的double值
    const val SIZETYPE_MB = 3 //获取文件大小单位为MB的double值
    const val SIZETYPE_GB = 4 //获取文件大小单位为GB的double值

    /**
     * 在指定的位置创建文件夹
     *
     * @param dirPath 文件夹路径
     * @return 若创建成功，则返回True；反之，则返回False
     */
    fun mkDir(dirPath: String?): Boolean {
        return File(dirPath).mkdirs()
    }

    /**
     * 删除指定的文件
     *
     * @param filePath 文件路径
     * @return 若删除成功，则返回True；反之，则返回False
     */
    fun delFile(filePath: String?): Boolean {
        var result = false
        val file = File(filePath)
        if (file.exists()) {
            result = file.delete()
        }
        return result
    }

    /**
     * 删除指定的文件夹
     *
     * @param dirPath 文件夹路径
     * @param delFile 文件夹中是否包含文件
     * @return 若删除成功，则返回True；反之，则返回False
     */
    fun delDir(dirPath: String?, delFile: Boolean): Boolean {
        val file = File(dirPath)
        if (!file.exists()) return true
        return if (delFile) {
            if (file.isFile) {
                file.delete()
            } else if (file.isDirectory) {
                if (file.listFiles().size == 0) {
                    file.delete()
                } else {
                    val zfiles = file.listFiles().size
                    val delfile = file.listFiles()
                    for (i in 0 until zfiles) {
                        if (delfile[i].isDirectory) {
                            delDir(delfile[i].absolutePath, true)
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
     * @param source   源文件（夹）
     * @param target   目标文件（夹）
     * @param isFolder 若进行文件夹复制，则为True；反之为False
     * @throws IOException
     */
    @Throws(IOException::class)
    fun copy(source: String?, target: String?, isFolder: Boolean) {
        if (source == null || target == null) return
        if (isFolder) {
            File(target).mkdirs()
            val a = File(source)
            val file = a.list()
            var temp: File? = null
            for (i in file.indices) {
                temp = if (source.endsWith(File.separator)) {
                    File(source + file[i])
                } else {
                    File(source + File.separator + file[i])
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
                    copy(source + "/" + file[i], target + "/" + file[i], true)
                }
            }
        } else {
            var byteread = 0
            val oldfile = File(source)
            if (oldfile.exists()) {
                val inStream: InputStream = FileInputStream(source)
                val file = File(target)
                file.parentFile.mkdirs()
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
     * @param source   源文件（夹）
     * @param target   目标文件（夹）
     * @param isFolder 若为文件夹，则为True；反之为False
     * @return
     * @throws IOException
     */
    @Throws(IOException::class)
    fun move(source: String?, target: String?, isFolder: Boolean): Boolean {
        copy(source, target, isFolder)
        return if (isFolder) {
            delDir(source, true)
        } else {
            delFile(source)
        }
    }

    /**
     * 获取缓存文件夹
     *
     * @param context
     * @return
     */
    fun getDiskCacheDir(context: Context?): String? {
        if (context == null) return BaseApplication.instance?.externalCacheDir?.absolutePath
        //isExternalStorageEmulated()设备的外存是否是用内存模拟的，是则返回true
        return if ((Environment.MEDIA_MOUNTED == Environment.getExternalStorageState() || !Environment.isExternalStorageEmulated()) && context.externalCacheDir != null) {
            context.externalCacheDir!!.absolutePath
        } else {
            context.cacheDir.absolutePath
        }
    }

    /**
     * 获取媒体文件夹
     *
     * @param context
     * @return
     */
    fun getFilesDir(context: Context?): String? {
        if (context == null) return BaseApplication.instance?.filesDir?.absolutePath
        //isExternalStorageEmulated()设备的外存是否是用内存模拟的，是则返回true
        return if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState() || !Environment.isExternalStorageEmulated()) {
            Environment.getExternalStorageDirectory().absolutePath
        } else {
            context.filesDir.absolutePath
        }
    }

    private val PATTERN: String? = "yyyyMMddHHmmss" // 时间戳命名

    /**
     * 在缓存路径里创建文件
     *
     * @param context  context
     * @param filePath 文件路径
     * @return file
     */
    fun createTmpFile(context: Context?, filePath: String?): File? {
        val timeStamp = SimpleDateFormat(PATTERN, Locale.CHINA).format(Date())
        val dir = File(getFilesDir(context) + filePath)
        if (!dir.exists()) {
            dir.mkdirs()
        }
        return File(dir, "$timeStamp.jpg")
    }

    /**
     * 在缓存路径里创建初始文件夹。保存拍摄图片和剪裁后的图片
     *
     * @param filePath 文件夹路径
     */
    fun createFile(context: Context?, filePath: String?) {
        val dir = File(getFilesDir(context) + filePath)
        val cropFile = File(getFilesDir(context) + filePath + "/crop")
        if (!dir.exists()) {
            if (!dir.isDirectory) {
                dir.delete()
            }
            dir.mkdirs()
        }
        if (!cropFile.exists()) {
            if (!cropFile.parentFile.isDirectory) {
                cropFile.parentFile.delete()
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
     * 在缓存路径里保存拍摄图片和剪裁后的图片
     *
     * @param dir 文件夹路径
     * @return 图片绝对路径
     */
    fun saveBitmap(context: Context?, dir: String?, b: Bitmap?): String? {
        val dataTake = System.currentTimeMillis()
        val jpegName = "picture_$dataTake.jpg"
        return saveBitmap(context, dir, jpegName, b)
    }

    fun saveBitmap(context: Context?, dir: String?, name: String?, b: Bitmap?): String? {
        val f = File(getFilesDir(context) + dir)
        if (!f.exists()) {
            if (!f.isDirectory) {
                f.delete()
            }
            f.mkdirs()
        }
        val jpegPath = getFilesDir(context) + dir + File.separator + name
        return try {
            val fout = FileOutputStream(jpegPath)
            val bos = BufferedOutputStream(fout)
            b!!.compress(Bitmap.CompressFormat.JPEG, 100, bos)
            bos.flush()
            bos.close()
            jpegPath
        } catch (e: IOException) {
            e.printStackTrace()
            ""
        } catch (e: NullPointerException) {
            e.printStackTrace()
            ""
        }
    }

    /**
     * 获取缓存路径里的剪裁文件夹
     *
     * @param path 相对路径
     */
    fun getCropDir(context: Context?, path: String?): File? {
        val cropFile = File(getFilesDir(context) + path + "/crop")
        if (!cropFile.exists()) {
            if (!cropFile.parentFile.isDirectory) {
                cropFile.parentFile.delete()
            }
            cropFile.mkdirs()
        }
        return cropFile
    }

    /**
     * 获取文件指定文件的指定单位的大小
     *
     * @param filePath 文件路径
     * @param sizeType 获取大小的类型1为B、2为KB、3为MB、4为GB
     * @return double值的大小
     */
    fun getFileOrFilesSize(filePath: String?, sizeType: Int): Double {
        val file = File(filePath)
        var blockSize: Long = 0
        try {
            blockSize = if (file.isDirectory) {
                getFileSizes(file)
            } else {
                getFileSize(file)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("获取文件大小", "获取失败!")
        }
        return FormetFileSize(blockSize, sizeType)
    }

    /**
     * 调用此方法自动计算指定文件或指定文件夹的大小
     *
     * @param filePath 文件路径
     * @return 计算好的带B、KB、MB、GB的字符串
     */
    fun getAutoFileOrFilesSize(filePath: String?): String? {
        val file = File(filePath)
        var blockSize: Long = 0
        try {
            blockSize = if (file.isDirectory) {
                getFileSizes(file)
            } else {
                getFileSize(file)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("获取文件大小", "获取失败!")
        }
        return FormetFileSize(blockSize)
    }

    /**
     * 获取指定文件大小
     *
     * @param file
     * @return
     */
    @Throws(Exception::class)
    fun getFileSize(file: File?): Long {
        var size: Long = 0
        if (file != null && file.exists()) {
            val fis = FileInputStream(file)
            size = fis.available().toLong()
        } else {
            Log.e("获取文件大小", "文件不存在!")
        }
        return size
    }

    /**
     * 获取指定文件夹大小
     *
     * @param f
     * @return
     */
    @Throws(Exception::class)
    fun getFileSizes(f: File?): Long {
        var size: Long = 0
        if (f != null && f.exists() && f.isDirectory) {
            val flist = f.listFiles()
            for (file in flist) {
                size = if (file.isDirectory) {
                    size + getFileSizes(file)
                } else {
                    size + getFileSize(file)
                }
            }
        }
        return size
    }

    /**
     * 转换文件大小
     *
     * @param fileS
     * @return
     */
    fun FormetFileSize(fileS: Long): String? {
        val df = DecimalFormat("#.00")
        var fileSizeString = ""
        val wrongSize = "0B"
        if (fileS == 0L) {
            return wrongSize
        }
        fileSizeString = if (fileS < 1024) {
            df.format(fileS as Double) + "B"
        } else if (fileS < 1048576) {
            df.format(fileS as Double / 1024) + "KB"
        } else if (fileS < 1073741824) {
            df.format(fileS as Double / 1048576) + "MB"
        } else {
            df.format(fileS as Double / 1073741824) + "GB"
        }
        return fileSizeString
    }

    /**
     * 转换文件大小,指定转换的类型
     *
     * @param fileS
     * @param sizeType
     * @return
     */
    fun FormetFileSize(fileS: Long, sizeType: Int): Double {
        val df = DecimalFormat("#.00")
        var fileSizeLong = 0.0
        when (sizeType) {
            SIZETYPE_B -> fileSizeLong = df.format(fileS as Double).toDouble()
            SIZETYPE_KB -> fileSizeLong = df.format(fileS as Double / 1024).toDouble()
            SIZETYPE_MB -> fileSizeLong = df.format(fileS as Double / 1048576).toDouble()
            SIZETYPE_GB -> fileSizeLong = df.format(fileS as Double / 1073741824).toDouble()
            else -> {
            }
        }
        return fileSizeLong
    }

    /**
     * Get the Mime Type from a File
     *
     * @param fileName 文件名
     * @return 返回MIME类型
     */
    private fun getMimeType(fileName: String?): String? {
        val fileNameMap = URLConnection.getFileNameMap()
        var type: String? = null
        try {
            type = fileNameMap.getContentTypeFor(fileName)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return type
    }

    /**
     * 根据文件后缀名判断 文件是否是视频文件
     *
     * @param fileName 文件名
     * @return 是否是视频文件
     */
    fun isVideoFile(fileName: String?): Boolean {
        if (TextUtils.isEmpty(fileName)) return false
        val index = fileName!!.lastIndexOf(".")
        if (index == -1) return false
        val mimeType = getMimeType(fileName.substring(index))
        return if (TextUtils.isEmpty(mimeType)) false else mimeType!!.contains("video/")
    }

    fun isVideoFileAsMime(mimeType: String?): Boolean {
        return !TextUtils.isEmpty(mimeType) && mimeType!!.contains("video/")
    }

    /**
     * @param mContext 上下文
     * @param fileName 目标文件路径
     * @description 将assets文件转成json
     * @author: Swing 763263311@qq.com
     * @date: 2020/12/28 0028 上午 10:49
     */
    fun getJsonFromAssets(mContext: Context?, fileName: String?): String? {
        if (mContext == null || fileName == null) return null
        val sb = StringBuilder()
        val am = mContext.assets
        try {
            val br = BufferedReader(InputStreamReader(
                    am.open(fileName)))
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
}