package com.zhangteng.base.mvvm.base

import com.zhangteng.base.mvvm.livedata.SingleLiveData

/**
 * ViewModel的带网络状态基类
 */
open class BaseNoNetworkViewModel : BaseLoadingViewModel() {

    val networkChange: UiNoNetworkChange by lazy { UiNoNetworkChange() }

    inner class UiNoNetworkChange {
        //显示
        val showNoNetwork by lazy { SingleLiveData<String>() }

        //隐藏
        val hideNoNetwork by lazy { SingleLiveData<Boolean>() }

        //显示
        val showNoDataView by lazy { SingleLiveData<String>() }

        //隐藏
        val hideNoDataView by lazy { SingleLiveData<Boolean>() }
    }

}