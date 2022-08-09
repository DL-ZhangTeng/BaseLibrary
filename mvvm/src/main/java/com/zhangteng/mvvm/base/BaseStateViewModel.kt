package com.zhangteng.mvvm.base

import android.view.View
import com.zhangteng.mvvm.livedata.SingleLiveData

/**
 * ViewModel的带状态基类
 */
open class BaseStateViewModel : BaseLoadingViewModel() {

    val stateChange: UiStateChange by lazy { UiStateChange() }

    inner class UiStateChange {
        //显示（View是将要被无网络视图替换的视图）
        val showNoNetView by lazy { SingleLiveData<View>() }

        //隐藏（View是将要被无网络视图替换的视图）
        val hideNoNetView by lazy { SingleLiveData<View>() }

        //显示（View是将要被超时视图替换的视图）
        val showTimeOutView by lazy { SingleLiveData<View>() }

        //隐藏（View是将要被超时视图替换的视图）
        val hideTimeOutView by lazy { SingleLiveData<View>() }

        //显示（View是将要被无数据视图替换的视图）
        val showEmptyView by lazy { SingleLiveData<View>() }

        //隐藏（View是将要被无数据视图替换的视图）
        val hideEmptyView by lazy { SingleLiveData<View>() }

        //显示（View是将要被数据异常视图替换的视图）
        val showErrorView by lazy { SingleLiveData<View>() }

        //隐藏（View是将要被数据异常视图替换的视图）
        val hideErrorView by lazy { SingleLiveData<View>() }

        //显示（View是将要被未登录视图替换的视图）
        val showNoLoginView by lazy { SingleLiveData<View>() }

        //隐藏（View是将要被未登录视图替换的视图）
        val hideNoLoginView by lazy { SingleLiveData<View>() }
    }

}