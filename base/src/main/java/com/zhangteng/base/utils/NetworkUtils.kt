package com.zhangteng.base.utils

import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.util.Log

object NetworkUtils {
    private val TAG = NetworkUtils::class.java.simpleName

    /**
     * 获取ConnectivityManager
     */
    fun getConnManager(context: Context?): ConnectivityManager? {
        return if (context == null) null else context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    /**
     * 判断网络连接是否有效（此时可传输数据）。
     *
     * @param context
     * @return boolean 不管wifi，还是mobile net，只有当前在连接状态（可有效传输数据）才返回true,反之false。
     */
    fun isConnected(context: Context?): Boolean {
        val net = getConnManager(context)?.activeNetworkInfo
        return net != null && net.isConnected
    }

    /**
     * 判断有无网络正在连接中（查找网络、校验、获取IP等）。
     *
     * @param context
     * @return boolean 不管wifi，还是mobile net，只有当前在连接状态（可有效传输数据）才返回true,反之false。
     */
    fun isConnectedOrConnecting(context: Context?): Boolean {
        val nets = getConnManager(context)?.allNetworkInfo
        if (nets != null) {
            for (net in nets) {
                if (net.isConnectedOrConnecting) {
                    return true
                }
            }
        }
        return false
    }

    fun getConnectedType(context: Context?): NetType {
        val net = getConnManager(context)?.activeNetworkInfo
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
    fun isWifiConnected(context: Context?): Boolean {
        val net = getConnManager(context)?.activeNetworkInfo
        return net != null && net.type == ConnectivityManager.TYPE_WIFI && net.isConnected
    }

    /**
     * 是否存在有效的移动连接
     *
     * @param context
     * @return boolean
     */
    fun isMobileConnected(context: Context?): Boolean {
        val net = getConnManager(context)?.activeNetworkInfo
        return net != null && net.type == ConnectivityManager.TYPE_MOBILE && net.isConnected
    }

    /**
     * 检测网络是否为可用状态
     */
    fun isAvailable(context: Context?): Boolean {
        return isWifiAvailable(context) || isMobileAvailable(context) && isMobileEnabled(context)
    }

    /**
     * 判断是否有可用状态的Wifi，以下情况返回false：
     * 1. 设备wifi开关关掉;
     * 2. 已经打开飞行模式；
     * 3. 设备所在区域没有信号覆盖；
     * 4. 设备在漫游区域，且关闭了网络漫游。
     *
     * @param context
     * @return boolean wifi为可用状态（不一定成功连接，即Connected）即返回ture
     */
    fun isWifiAvailable(context: Context?): Boolean {
        val nets = getConnManager(context)?.allNetworkInfo
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
     * @param context
     * @return boolean
     */
    fun isMobileAvailable(context: Context?): Boolean {
        val nets = getConnManager(context)?.allNetworkInfo
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
     * @param context
     * @return boolean 打开移动网络返回true，反之false
     */
    fun isMobileEnabled(context: Context?): Boolean {
        try {
            val getMobileDataEnabledMethod = ConnectivityManager::class.java.getDeclaredMethod("getMobileDataEnabled")
            getMobileDataEnabledMethod.isAccessible = true
            return getMobileDataEnabledMethod.invoke(getConnManager(context)) as Boolean
        } catch (e: Exception) {
            e.printStackTrace()
        }
        // 反射失败，默认开启
        return true
    }

    /**
     * 判断是否是WIFI连接
     *
     * @param context
     * @return
     */
    fun isWIFI(context: Context?): Boolean {
        context ?: return false
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE)
        connectivityManager ?: return false
        connectivityManager as ConnectivityManager
        return connectivityManager.activeNetworkInfo?.type == ConnectivityManager.TYPE_WIFI
    }

    /**
     * 打开网络设置界面
     */
    fun openSetting(activity: Activity?) {
        activity ?: return
        val intent = Intent("/")
        val cm = ComponentName("com.android.settings",
                "com.android.settings.WirelessSettings")
        intent.component = cm
        intent.action = "android.intent.action.VIEW"
        activity.startActivityForResult(intent, 0)
    }

    /**
     * 打印当前各种网络状态
     *
     * @param context
     * @return boolean
     */
    fun printNetworkInfo(context: Context?): Boolean {
        context ?: return false
        val connectivity = context.getSystemService(Context.CONNECTIVITY_SERVICE)
        connectivity ?: return false
        connectivity as ConnectivityManager
        val `in` = connectivity.activeNetworkInfo
        Log.i(TAG, "getActiveNetworkInfo: $`in`")
        val info = connectivity.allNetworkInfo
        for (i in info.indices) {
            // if (info[i].getType() == ConnectivityManager.TYPE_WIFI) {
            Log.i(TAG, "NetworkInfo[" + i + "]isAvailable : " + info[i].isAvailable)
            Log.i(TAG, "NetworkInfo[" + i + "]isConnected : " + info[i].isConnected)
            Log.i(TAG, "NetworkInfo[" + i + "]isConnectedOrConnecting : " + info[i].isConnectedOrConnecting)
            Log.i(TAG, "NetworkInfo[" + i + "]: " + info[i])
            // }
        }
        Log.i(TAG, "\n")
        return false
    }

    enum class NetType(var value: Int) {
        None(1), Mobile(2), Wifi(4), Other(8);
    }
}