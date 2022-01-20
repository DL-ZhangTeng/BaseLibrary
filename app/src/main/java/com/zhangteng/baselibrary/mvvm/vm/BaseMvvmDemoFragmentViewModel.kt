package com.zhangteng.baselibrary.mvvm.vm

import com.zhangteng.mvvm.base.BaseViewModel
import com.zhangteng.baselibrary.mvvm.repository.BaseMvvmDemoFragmentRepository

class BaseMvvmDemoFragmentViewModel : BaseViewModel() {
    private val mRepository by lazy { BaseMvvmDemoFragmentRepository() }
}