package com.zhangteng.baselibrary.mvvm.vm

import com.zhangteng.base.mvvm.base.BaseViewModel
import com.zhangteng.baselibrary.mvvm.repository.BaseMvvmDbDemoDbFragmentRepository

class BaseMvvmDbDemoDbFragmentViewModel : BaseViewModel() {
    private val mRepository by lazy { BaseMvvmDbDemoDbFragmentRepository() }
}