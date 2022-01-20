package com.zhangteng.baselibrary.activity

import android.content.IntentFilter
import android.os.Bundle
import com.zhangteng.mvvm.mvvm.BaseMvvmActivity
import com.zhangteng.mvvm.manager.NetworkStateReceive
import com.zhangteng.baselibrary.R
import com.zhangteng.baselibrary.mvvm.vm.BaseMvvmDemoViewModel

class BaseMvvmDemoActivity : BaseMvvmActivity<BaseMvvmDemoViewModel>() {

    private var intentFilter: IntentFilter? = null
    private var netChangeReceiver: NetworkStateReceive? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base_mvvm_demo)
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