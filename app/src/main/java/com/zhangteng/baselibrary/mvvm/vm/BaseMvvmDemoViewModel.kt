package com.zhangteng.baselibrary.mvvm.vm

import com.zhangteng.base.mvvm.base.BaseViewModel
import com.zhangteng.baselibrary.mvvm.repository.BaseMvvmDemoRepository

class BaseMvvmDemoViewModel : BaseViewModel() {
    private val mRepository by lazy { BaseMvvmDemoRepository() }
}