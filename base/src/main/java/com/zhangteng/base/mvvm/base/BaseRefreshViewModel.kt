package com.zhangteng.base.mvvm.base

import com.zhangteng.base.mvvm.livedata.SingleLiveData

/**
 * ViewModel的带刷新基类
 */
open class BaseRefreshViewModel : BaseNoNetworkViewModel() {

    val listChange: UiRefreshChange by lazy { UiRefreshChange() }

    inner class UiRefreshChange {
        val finishRefreshOrLoadMore by lazy { SingleLiveData<Boolean>() }
    }

}