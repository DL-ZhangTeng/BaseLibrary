package com.zhangteng.mvvm.base

import com.zhangteng.mvvm.livedata.SingleLiveData

/**
 * ViewModel的带刷新基类
 */
open class BaseRefreshViewModel : BaseStateViewModel() {

    val refreshChange: UiRefreshChange by lazy { UiRefreshChange() }

    inner class UiRefreshChange {
        val finishRefreshOrLoadMore by lazy { SingleLiveData<Boolean>() }
    }

}