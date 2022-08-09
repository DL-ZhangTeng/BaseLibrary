package com.zhangteng.mvvm.base

import com.zhangteng.mvvm.livedata.SingleLiveData

/**
 * ViewModel的带刷新基类（建议定义*UiState保存页面状态）
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
open class BaseRefreshViewModel : BaseStateViewModel() {

    val refreshChange: UiRefreshChange by lazy { UiRefreshChange() }

    inner class UiRefreshChange {
        val finishRefreshOrLoadMore by lazy { SingleLiveData<Boolean>() }
    }

}