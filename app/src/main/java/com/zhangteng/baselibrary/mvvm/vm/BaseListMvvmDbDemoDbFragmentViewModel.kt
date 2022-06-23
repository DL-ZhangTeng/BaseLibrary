package com.zhangteng.baselibrary.mvvm.vm

import com.zhangteng.baselibrary.mvvm.repository.BaseListMvvmDbDemoDbFragmentRepository
import com.zhangteng.mvvm.base.BaseViewModel

class BaseListMvvmDbDemoDbFragmentViewModel : BaseViewModel() {
    private val mRepository by lazy { BaseListMvvmDbDemoDbFragmentRepository() }
}