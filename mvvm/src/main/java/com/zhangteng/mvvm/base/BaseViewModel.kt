package com.zhangteng.mvvm.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

/**
 * ViewModel的基类（建议定义*UiState保存页面状态）
 *
 * data class NewsUiState(
 *  val isSignedIn: Boolean = false,
 *  val isPremium: Boolean = false,
 *  val newsItems: List<NewsItemUiState> = listOf(),
 *  val userMessages: List<Message> = listOf()
 * )
 *
 * data class NewsItemUiState(
 *  val title: String,
 *  val body: String,
 *  val bookmarked: Boolean = false,
 *  ...
 * )
 */
open class BaseViewModel : ViewModel() {

    /**
     * 所有网络请求都在 viewModelScope 域中启动，当页面销毁时会自动
     * 调用ViewModel的  #onCleared 方法取消所有协程
     */
    fun launchUI(block: suspend CoroutineScope.() -> Unit) = viewModelScope.launch { block() }

    /**
     * 用流的方式进行网络请求
     */
    fun <T> launchFlow(block: suspend () -> T): Flow<T> {
        return flow {
            emit(block())
        }
    }
}