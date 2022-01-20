package com.zhangteng.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager


/**
 * 获取ConnectivityManager
 */
fun Context?.getConnManager(): ConnectivityManager? {
    return if (this == null) null else getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
}

/**
 * 判断网络连接是否有效（此时可传输数据）。
 *
 * @return boolean 不管wifi，还是mobile net，只有当前在连接状态（可有效传输数据）才返回true,反之false。
 */
fun Context?.isConnected(): Boolean {
    val net = getConnManager()?.activeNetworkInfo
    return net != null && net.isConnected
}

/**
 * 判断有无网络正在连接中（查找网络、校验、获取IP等）。
 *
 * @return boolean 不管wifi，还是mobile net，只有当前在连接状态（可有效传输数据）才返回true,反之false。
 */
fun Context?.isConnectedOrConnecting(): Boolean {
    val nets = getConnManager()?.allNetworkInfo
    if (nets != null) {
        for (net in nets) {
            if (net.isConnectedOrConnecting) {
                return true
            }
        }
    }
    return false
}

/**
 * 判断链接类型
 */
fun Context?.getConnectedType(): NetType {
    val net = getConnManager()?.activeNetworkInfo
    return if (net != null) {
        when (net.type) {
            ConnectivityManager.TYPE_WIFI -> NetType.Wifi
            ConnectivityManager.TYPE_MOBILE -> NetType.Mobile
            else -> NetType.Other
        }
    } else NetType.None
}

/**
 * 是否存在有效的WIFI连接
 */
fun Context?.isWifiConnected(): Boolean {
    val net = getConnManager()?.activeNetworkInfo
    return net != null && net.type == ConnectivityManager.TYPE_WIFI && net.isConnected
}

/**
 * 是否存在有效的移动连接
 *
 * @return boolean
 */
fun Context?.isMobileConnected(): Boolean {
    val net = getConnManager()?.activeNetworkInfo
    return net != null && net.type == ConnectivityManager.TYPE_MOBILE && net.isConnected
}

/**
 * 检测网络是否为可用状态
 */
fun Context?.isAvailable(): Boolean {
    return isWifiAvailable() || isMobileAvailable() && isMobileEnabled()
}

/**
 * 判断是否有可用状态的Wifi，以下情况返回false：
 * 1. 设备wifi开关关掉;
 * 2. 已经打开飞行模式；
 * 3. 设备所在区域没有信号覆盖；
 * 4. 设备在漫游区域，且关闭了网络漫游。
 *
 * @return boolean wifi为可用状态（不一定成功连接，即Connected）即返回ture
 */
fun Context?.isWifiAvailable(): Boolean {
    val nets = getConnManager()?.allNetworkInfo
    if (nets != null) {
        for (net in nets) {
            if (net.type == ConnectivityManager.TYPE_WIFI) {
                return net.isAvailable
            }
        }
    }
    return false
}

/**
 * 判断有无可用状态的移动网络，注意关掉设备移动网络直接不影响此函数。
 * 也就是即使关掉移动网络，那么移动网络也可能是可用的(彩信等服务)，即返回true。
 * 以下情况它是不可用的，将返回false：
 * 1. 设备打开飞行模式；
 * 2. 设备所在区域没有信号覆盖；
 * 3. 设备在漫游区域，且关闭了网络漫游。
 *
 * @return boolean
 */
fun Context?.isMobileAvailable(): Boolean {
    val nets = getConnManager()?.allNetworkInfo
    if (nets != null) {
        for (net in nets) {
            if (net.type == ConnectivityManager.TYPE_MOBILE) {
                return net.isAvailable
            }
        }
    }
    return false
}

/**
 * 设备是否打开移动网络开关
 *
 * @return boolean 打开移动网络返回true，反之false
 */
@SuppressLint("DiscouragedPrivateApi")
fun Context?.isMobileEnabled(): Boolean {
    try {
        val getMobileDataEnabledMethod =
            ConnectivityManager::class.java.getDeclaredMethod("getMobileDataEnabled")
        getMobileDataEnabledMethod.isAccessible = true
        return getMobileDataEnabledMethod.invoke(getConnManager()) as Boolean
    } catch (e: Exception) {
        e.printStackTrace()
    }
    // 反射失败，默认开启
    return true
}

/**
 * 判断是否是WIFI连接
 *
 * @return
 */
fun Context?.isWIFI(): Boolean {
    this ?: return false
    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE)
    connectivityManager ?: return false
    connectivityManager as ConnectivityManager
    return connectivityManager.activeNetworkInfo?.type == ConnectivityManager.TYPE_WIFI
}

/**
 * 打开网络设置界面
 */
fun Activity?.openSetting() {
    this ?: return
    val intent = Intent("/")
    val cm = ComponentName(
        "com.android.settings",
        "com.android.settings.WirelessSettings"
    )
    intent.component = cm
    intent.action = "android.intent.action.VIEW"
    startActivityForResult(intent, 0)
}

/**
 * description: 网络类型
 * author: Swing
 * date: 2021/11/11
 */
enum class NetType(var value: Int) {
    None(1), Mobile(2), Wifi(4), Other(8);
}