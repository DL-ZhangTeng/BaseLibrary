package com.zhangteng.mvvm.base

import android.view.View
import com.zhangteng.mvvm.livedata.SingleLiveData

/**
 * ViewModel的带状态基类（建议定义*UiState保存页面状态）
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
open class BaseStateViewModel : BaseLoadingViewModel() {

    val stateChange: UiStateChange by lazy { UiStateChange() }

    inner class UiStateChange {
        //显示（View是将要被无网络视图替换的视图）
        val showNoNetView by lazy { SingleLiveData<View>() }

        //显示（View是将要被超时视图替换的视图）
        val showTimeOutView by lazy { SingleLiveData<View>() }

        //显示（View是将要被无数据视图替换的视图）
        val showEmptyView by lazy { SingleLiveData<View>() }

        //显示（View是将要被数据异常视图替换的视图）
        val showErrorView by lazy { SingleLiveData<View>() }

        //显示（View是将要被未登录视图替换的视图）
        val showNoLoginView by lazy { SingleLiveData<View>() }

        //隐藏（View是将要展示的View）
        val showContentView by lazy { SingleLiveData<View>() }
    }

}