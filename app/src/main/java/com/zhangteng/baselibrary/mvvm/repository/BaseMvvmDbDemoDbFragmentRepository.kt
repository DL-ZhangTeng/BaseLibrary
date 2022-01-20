package com.zhangteng.baselibrary.mvvm.repository

import com.zhangteng.mvvm.base.BaseNetRepository

class BaseMvvmDbDemoDbFragmentRepository : BaseNetRepository() {

    private val mService by lazy {
        //HttpUtils.getInstance().ConfigGlobalHttpUtils().createService(Api::class.java)
    }
}