package com.zhangteng.mvvm.manager

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import com.zhangteng.utils.isAvailable


/**
 * 网络变化接收器,使用mvvm模式时在app中或manifest中注册
 */
class NetworkStateReceive : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == ConnectivityManager.CONNECTIVITY_ACTION) {
            if (!context.isAvailable()) {
                //收到没有网络时判断之前的值是不是有网络，如果有网络才提示通知 ，防止重复通知
                NetworkStateManager.instance.mNetworkStateCallback.value?.let {
                    if (it.isAvailable) {
                        //没网
                        NetworkStateManager.instance.mNetworkStateCallback.value =
                            NetState(isAvailable = false)
                    }
                    return
                }
                NetworkStateManager.instance.mNetworkStateCallback.value =
                    NetState(isAvailable = false)
            } else {
                //收到有网络时判断之前的值是不是没有网络，如果没有网络才提示通知 ，防止重复通知
                NetworkStateManager.instance.mNetworkStateCallback.value?.let {
                    if (!it.isAvailable) {
                        //有网络了
                        NetworkStateManager.instance.mNetworkStateCallback.value =
                            NetState(isAvailable = true)
                    }
                    return
                }
                NetworkStateManager.instance.mNetworkStateCallback.value =
                    NetState(isAvailable = true)
            }
        }
    }
}