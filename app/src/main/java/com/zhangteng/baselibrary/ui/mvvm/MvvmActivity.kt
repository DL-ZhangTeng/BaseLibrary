package com.zhangteng.baselibrary.ui.mvvm

import android.content.IntentFilter
import android.os.Bundle
import com.zhangteng.mvvm.mvvm.BaseMvvmActivity
import com.zhangteng.mvvm.manager.NetworkStateReceive
import com.zhangteng.baselibrary.R


class MvvmActivity : com.zhangteng.mvvm.mvvm.BaseMvvmActivity<MvvmActivityViewModel>() {
    private var intentFilter: IntentFilter? = null
    private var netChangeReceiver: NetworkStateReceive? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.mvvm_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MvvmFragment.newInstance())
                .commitNow()
        }
    }

    override fun initView() {
        intentFilter = IntentFilter()
        // 添加action,当网络情况发生变化时，系统就是发送一条值为android.net.conn.CONNECTIVITY_CHANGE的广播
        intentFilter?.addAction("android.net.conn.CONNECTIVITY_CHANGE")
        netChangeReceiver = NetworkStateReceive()
        // 注册广播
        registerReceiver(netChangeReceiver, intentFilter)
    }

    override fun initData() {

    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(netChangeReceiver)
    }
}