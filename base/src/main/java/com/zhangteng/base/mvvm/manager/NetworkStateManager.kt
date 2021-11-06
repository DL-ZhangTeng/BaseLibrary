package com.zhangteng.base.mvvm.manager

import com.zhangteng.base.mvvm.livedata.SingleLiveData

/**
 * 网络变化管理者
 */
class NetworkStateManager private constructor() {

    val mNetworkStateCallback = SingleLiveData<NetState>()

    companion object {
        val instance: NetworkStateManager by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            NetworkStateManager()
        }
    }

}