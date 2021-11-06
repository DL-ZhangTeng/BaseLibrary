package com.zhangteng.base.mvvm.base

import com.zhangteng.base.mvvm.livedata.SingleLiveData

/**
 * ViewModel的带加载中基类
 */
open class BaseLoadingViewModel : BaseViewModel() {

    val loadingChange: UiLoadingChange by lazy { UiLoadingChange() }

    inner class UiLoadingChange {
        //显示加载框
        val showLoadingView by lazy { SingleLiveData<String>() }

        //隐藏
        val dismissLoadingView by lazy { SingleLiveData<Boolean>() }
    }

}