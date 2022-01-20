package com.zhangteng.baselibrary.activity

import com.zhangteng.mvvm.mvvm.BaseMvvmDbActivity
import com.zhangteng.baselibrary.R
import com.zhangteng.baselibrary.databinding.ActivityBaseMvvmDbDemoDbBinding
import com.zhangteng.baselibrary.mvvm.vm.BaseMvvmDbDemoDbViewModel

class BaseMvvmDbDemoDbActivity : BaseMvvmDbActivity<BaseMvvmDbDemoDbViewModel, ActivityBaseMvvmDbDemoDbBinding>() {

    override fun initView() {

    }

    override fun initData() {

    }

    override fun layoutId(): Int {
        return R.layout.activity_base_mvvm_db_demo_db
    }
}