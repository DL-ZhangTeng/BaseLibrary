package com.zhangteng.base.mvvm.state

import androidx.lifecycle.MutableLiveData
import com.zhangteng.rxhttputils.exception.ApiException

/**
 * 自定义结果集封装类
 */
sealed class ResultState<out T> {
    companion object {
        fun <T> onSuccess(data: T): ResultState<T> = Success(data)
        fun <T> onError(error: ApiException): ResultState<T> = Error(error)
    }

    data class Success<out T>(val data: T) : ResultState<T>()
    data class Error(val error: ApiException) : ResultState<Nothing>()
}

/**
 * 处理返回值
 * @param result 请求结果
 */
fun <T> MutableLiveData<ResultState<T>>.paresResult(result: IResponse<T>) {
    value = when {
        result.isSuccess() -> {
            ResultState.onSuccess(result.getResult())
        }
        else -> {
            ResultState.onError(ApiException(Throwable(result.getMsg()), result.getCode()))
        }
    }
}

/**
 * 不处理返回值 直接返回请求结果
 * @param result 请求结果
 */
fun <T> MutableLiveData<ResultState<T>>.paresResult(result: T) {
    value = ResultState.onSuccess(result)
}

/**
 * 异常转换异常处理
 */
fun <T> MutableLiveData<ResultState<T>>.paresException(e: Throwable) {
    this.value = ResultState.onError(ApiException.handleException(e))
}

