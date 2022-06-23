package com.zhangteng.baselibrary.mvvm.vm

import com.zhangteng.baselibrary.mvvm.repository.BaseListMvvmDemoFragmentRepository
import com.zhangteng.mvvm.base.BaseViewModel

class BaseListMvvmDemoFragmentViewModel : BaseViewModel() {
    private val mRepository by lazy { BaseListMvvmDemoFragmentRepository() }
}