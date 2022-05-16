package com.zhangteng.utils

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.FileUtils
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.provider.OpenableColumns
import androidx.annotation.RequiresApi
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

/**
 * description 通过uri  获取文件路径
 *
 * @return 文件路径
 */
@SuppressLint("ObsoleteSdkInt")
fun Uri?.getFileAbsolutePath(context: Context?): String? {
    if (context == null || this == null) {
        return null
    }
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
        return getFileFromKitkatUri(context)
    } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q && DocumentsContract.isDocumentUri(
            context,
            this
        )
    ) {
        if (isExternalStorageDocument()) {
            val docId = DocumentsContract.getDocumentId(this)
            val split = docId.split(":").toTypedArray()
            val type = split[0]
            if ("primary".equals(type, ignoreCase = true)) {
                return Environment.getExternalStorageDirectory().toString() + "/" + split[1]
            }
        } else if (isDownloadsDocument()) {
            val id = DocumentsContract.getDocumentId(this)
            val contentUri = ContentUris.withAppendedId(
                Uri.parse("content://downloads/public_downloads"),
                id.toLong()
            )
            return contentUri.getFileFromUri(context, null, null)
        } else if (isMediaDocument()) {
            val docId = DocumentsContract.getDocumentId(this)
            val split = docId.split(":").toTypedArray()
            val type = split[0]
            var contentUri: Uri? = null
            if ("image" == type) {
                contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            } else if ("video" == type) {
                contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
            } else if ("audio" == type) {
                contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
            }
            val selection = MediaStore.Images.Media._ID + "=?"
            val selectionArgs = arrayOf(split[1])
            return contentUri.getFileFromUri(context, selection, selectionArgs)
        }
    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        return getFileFromQUri(context)
    }
    if ("content".equals(this.scheme, ignoreCase = true)) {
        // Return the remote address
        return if (isGooglePhotosUri()) {
            this.lastPathSegment
        } else getFileFromUri(context, null, null)
    } else if ("file".equals(this.scheme, ignoreCase = true)) {
        // File
        return this.path
    }
    return null
}

/**
 * description 通过uri  获取文件路径 此方法 只能用于4.4以下的版本
 *
 * @return 文件路径
 */
fun Uri?.getFileFromKitkatUri(context: Context): String? {
    if (null == this) {
        return null
    }
    val scheme = this.scheme
    var data: String? = null
    if (scheme == null) {
        data = this.path
    } else if (ContentResolver.SCHEME_FILE == scheme) {
        data = this.path
    } else if (ContentResolver.SCHEME_CONTENT == scheme) {
        val projection = arrayOf(MediaStore.Images.ImageColumns.DATA)
        val cursor = context.contentResolver.query(this, projection, null, null, null)
        if (null != cursor) {
            if (cursor.moveToFirst()) {
                val index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
                if (index > -1) {
                    data = cursor.getString(index)
                }
            }
            cursor.close()
        }
    }
    return data
}

/**
 * description 通过uri  获取文件路径 通用
 *
 * @return 文件路径
 */
fun Uri?.getFileFromUri(
    context: Context,
    selection: String?,
    selectionArgs: Array<String>?
): String? {
    if (null == this) {
        return null
    }
    var cursor: Cursor? = null
    val column = MediaStore.Images.Media.DATA
    val projection = arrayOf(column)
    try {
        cursor =
            context.contentResolver.query(this, projection, selection, selectionArgs, null)
        if (cursor != null && cursor.moveToFirst()) {
            val index = cursor.getColumnIndexOrThrow(column)
            return cursor.getString(index)
        }
    } finally {
        cursor?.close()
    }
    return null
}

/**
 * 通过uri  获取文件路径 Android 10 以上适配
 *
 * @return 文件路径
 */
@RequiresApi(api = Build.VERSION_CODES.Q)
fun Uri?.getFileFromQUri(context: Context): String? {
    if (null == this) {
        return null
    }
    var file: File? = null
    //android10以上转换
    if (this.scheme == ContentResolver.SCHEME_FILE) {
        file = File(this.path)
    } else if (this.scheme == ContentResolver.SCHEME_CONTENT) {
        //把文件复制到沙盒目录
        val contentResolver = context.contentResolver
        val cursor = contentResolver.query(this, null, null, null, null)
        if (cursor!!.moveToFirst()) {
            val displayName =
                cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
            try {
                val `is` = contentResolver.openInputStream(this)
                val cache = File(
                    context.externalCacheDir!!.absolutePath,
                    Math.round((Math.random() + 1) * 1000).toString() + displayName
                )
                val fos = FileOutputStream(cache)
                FileUtils.copy(`is`!!, fos)
                file = cache
                fos.close()
                `is`.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        cursor.close()
    }
    return file?.absolutePath
}

/**
 * @return Whether the Uri authority is MediaProvider.
 */
fun Uri.isMediaDocument(): Boolean {
    return "com.android.providers.media.documents" == authority
}

/**
 * @return Whether the Uri authority is Google Photos.
 */
fun Uri.isGooglePhotosUri(): Boolean {
    return "com.google.android.apps.photos.content" == authority
}

/**
 * @return Whether the Uri authority is ExternalStorageProvider.
 */
fun Uri.isExternalStorageDocument(): Boolean {
    return "com.android.externalstorage.documents" == authority
}

/**
 * @return Whether the Uri authority is DownloadsProvider.
 */
fun Uri.isDownloadsDocument(): Boolean {
    return "com.android.providers.downloads.documents" == authority
}