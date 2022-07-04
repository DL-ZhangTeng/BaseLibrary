package com.zhangteng.utils

import android.os.Build
import androidx.annotation.RequiresApi
import com.google.gson.JsonParseException
import com.google.gson.JsonSerializer
import org.apache.http.conn.ConnectTimeoutException
import org.json.JSONException
import retrofit2.HttpException
import java.io.IOException
import java.io.NotSerializableException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.text.ParseException
import javax.net.ssl.SSLHandshakeException
import kotlin.properties.Delegates

/**
 * description: 网络异常处理
 * author: Swing
 * date: 2022/7/4
 */
open class IException : Exception {
    var code by Delegates.notNull<Int>()

    constructor(message: String?, code: Int) : super(message) {
        this.code = code
    }

    constructor(message: String?, cause: Throwable?, code: Int) : super(message, cause) {
        this.code = code
    }

    constructor(cause: Throwable?, code: Int) : super(cause) {
        this.code = code
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    constructor(
        message: String?, cause: Throwable?,
        enableSuppression: Boolean,
        writableStackTrace: Boolean,
        code: Int
    ) : super(message, cause, enableSuppression, writableStackTrace) {
        this.code = code
    }

    /**
     * 约定异常
     */
    object ERROR {
        /**
         * 未知错误
         */
        const val UNKNOWN = 1000

        /**
         * 连接超时
         */
        const val TIMEOUT_ERROR = 1001

        /**
         * 空指针错误
         */
        const val NULL_POINTER_EXCEPTION = 1002

        /**
         * 证书出错
         */
        const val SSL_ERROR = 1003

        /**
         * 类转换错误
         */
        const val CAST_ERROR = 1004

        /**
         * 解析错误
         */
        const val PARSE_ERROR = 1005

        /**
         * 非法数据异常
         */
        const val ILLEGAL_STATE_ERROR = 1006
    }

    companion object {
        fun handleException(e: Throwable): IException {
            return if (e is HttpException) {
                try {
                    val message = e.response()!!.errorBody()!!.string()
                    IException(message, e, e.code())
                } catch (ioException: IOException) {
                    ioException.printStackTrace()
                    val message = ioException.message!!
                    IException(message, e, e.code())
                }
            } else if (e is SocketTimeoutException) {
                IException("网络连接超时，请检查您的网络状态后重试！", e, ERROR.TIMEOUT_ERROR)
            } else if (e is ConnectException) {
                IException("网络连接异常，请检查您的网络状态后重试！", e, ERROR.TIMEOUT_ERROR)
            } else if (e is ConnectTimeoutException) {
                IException("网络连接超时，请检查您的网络状态后重试！", e, ERROR.TIMEOUT_ERROR)
            } else if (e is UnknownHostException) {
                IException("网络连接异常，请检查您的网络状态后重试！", e, ERROR.TIMEOUT_ERROR)
            } else if (e is NullPointerException) {
                IException("空指针", e, ERROR.NULL_POINTER_EXCEPTION)
            } else if (e is SSLHandshakeException) {
                IException("证书验证失败", e, ERROR.SSL_ERROR)
            } else if (e is ClassCastException) {
                IException("类型转换失败", e, ERROR.CAST_ERROR)
            } else if (e is JsonParseException
                || e is JSONException
                || e is JsonSerializer<*>
                || e is NotSerializableException
                || e is ParseException
            ) {
                IException("解析失败", e, ERROR.PARSE_ERROR)
            } else if (e is IllegalStateException) {
                IException("非法状态", e, ERROR.ILLEGAL_STATE_ERROR)
            } else {
                IException("未知错误", e, ERROR.UNKNOWN)
            }
        }
    }

}