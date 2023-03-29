package com.zhangteng.baselibrary.utils

import android.text.TextUtils
import com.zhangteng.base.widget.NineGridView.OnProgressListener
import com.zhangteng.baselibrary.utils.ProgressResponseBody.InternalProgressListener
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import java.util.Collections

object ProgressManager {
    private val listenersMap = Collections.synchronizedMap(HashMap<String, OnProgressListener>())
    var okHttpClient: OkHttpClient? = null
        get() {
            if (field == null) {
                field = OkHttpClient.Builder()
                    .addNetworkInterceptor { chain: Interceptor.Chain ->
                        val request = chain.request()
                        val response = chain.proceed(request)
                        response.newBuilder()
                            .body(
                                ProgressResponseBody(
                                    request.url().toString(),
                                    LISTENER,
                                    response.body()
                                )
                            )
                            .build()
                    }
                    .build()
            }
            return field
        }
        private set

    private val LISTENER =
        object : InternalProgressListener {
            override fun onProgress(url: String?, bytesRead: Long, totalBytes: Long) {
                val onProgressListener = getProgressListener(url)
                if (onProgressListener != null) {
                    val percentage = (bytesRead * 1f / totalBytes * 100f).toInt()
                    val isComplete = percentage >= 100
                    onProgressListener.onProgress(isComplete, percentage, bytesRead, totalBytes)
                    if (isComplete) {
                        removeListener(url)
                    }
                }
            }
        }

    fun addListener(url: String?, listener: OnProgressListener?) {
        if (!TextUtils.isEmpty(url) && listener != null) {
            listenersMap[url] = listener
            listener.onProgress(false, 1, 0, 0)
        }
    }

    fun removeListener(url: String?) {
        if (!TextUtils.isEmpty(url)) {
            listenersMap.remove(url)
        }
    }

    fun getProgressListener(url: String?): OnProgressListener? {
        return if (TextUtils.isEmpty(url) || listenersMap.isEmpty()) {
            null
        } else listenersMap[url]
    }
}