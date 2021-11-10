package com.zhangteng.base.mvvm.base

import com.zhangteng.base.mvvm.livedata.SingleLiveData
import com.zhangteng.rxhttputils.exception.ApiException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext

/**
 * ViewModel的带加载中基类
 */
open class BaseLoadingViewModel : BaseViewModel() {

    val loadingChange: UiLoadingChange by lazy { UiLoadingChange() }

    /**
     *  不过滤请求结果
     * @param block 请求体
     * @param error 失败回调
     * @param complete  完成回调（无论成功失败都会调用）
     * @param isShowDialog 是否显示加载框
     */
    fun launchGo(
        block: suspend CoroutineScope.() -> Unit,
        error: suspend CoroutineScope.(ApiException) -> Unit,
        complete: suspend CoroutineScope.() -> Unit = {},
        isShowDialog: Boolean = true
    ) {
        if (isShowDialog) loadingChange.showLoadingView.call()
        launchUI {
            handleException(
                withContext(Dispatchers.IO) { block },
                { error(it) },
                {
                    loadingChange.dismissLoadingView.call()
                    complete()
                }
            )
        }
    }

    /**
     * 过滤请求结果，其他全抛异常
     * @param block 请求体
     * @param success 成功回调
     * @param error 失败回调
     * @param complete  完成回调（无论成功失败都会调用）
     * @param isShowDialog 是否显示加载框
     */
    fun <T> launchOnlyResult(
        block: suspend CoroutineScope.() -> IResponse<T>,
        success: (T) -> Unit,
        error: (ApiException) -> Unit,
        complete: () -> Unit = {},
        isShowDialog: Boolean = true
    ) {
        if (isShowDialog) loadingChange.showLoadingView.call()
        launchUI {
            handleException(
                {
                    withContext(Dispatchers.IO) {
                        block().let {
                            if (it.isSuccess()) it.getResult()
                            else throw ApiException(Throwable(it.getMsg()), it.getCode())
                        }
                    }.also { success(it) }
                },
                { error(it) },
                {
                    loadingChange.dismissLoadingView.call()
                    complete()
                }
            )
        }
    }


    /**
     * 异常统一处理
     */
    private suspend fun handleException(
        block: suspend CoroutineScope.() -> Unit,
        error: suspend CoroutineScope.(ApiException) -> Unit,
        complete: suspend CoroutineScope.() -> Unit
    ) {
        coroutineScope {
            try {
                block()
            } catch (e: Throwable) {
                error(ApiException.handleException(e))
            } finally {
                complete()
            }
        }
    }

    inner class UiLoadingChange {
        //显示加载框
        val showLoadingView by lazy { SingleLiveData<String>() }

        //隐藏
        val dismissLoadingView by lazy { SingleLiveData<Boolean>() }
    }

    /**
     * 如果需要框架帮你做脱壳处理请实现它
     */
    interface IResponse<T> {

        //抽象方法，用户的基类继承该类时，需要重写该方法
        fun isSuccess(): Boolean

        fun getResult(): T

        fun getCode(): Int

        fun getMsg(): String

    }
}