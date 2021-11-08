package com.zhangteng.base.mvvm.base

import android.view.View
import com.zhangteng.base.mvvm.livedata.SingleLiveData

/**
 * ViewModel的带网络状态基类
 */
open class BaseNoNetworkViewModel : BaseLoadingViewModel() {

    val networkChange: UiNoNetworkChange by lazy { UiNoNetworkChange() }

    inner class UiNoNetworkChange {
        //显示（View是将要被无网络视图替换的视图）
        val showNoNetwork by lazy { SingleLiveData<View>() }

        //隐藏（View是将要被无网络视图替换的视图）
        val hideNoNetwork by lazy { SingleLiveData<View>() }

        //显示（View是将要被无数据视图替换的视图）
        val showNoDataView by lazy { SingleLiveData<View>() }

        //隐藏（View是将要被无数据视图替换的视图）
        val hideNoDataView by lazy { SingleLiveData<View>() }
    }

}