package com.zhangteng.baselibrary.activity

import android.os.Bundle
import com.zhangteng.baselibrary.R
import com.zhangteng.baselibrary.databinding.ActivityBaseMvvmDbDemoDbBinding
import com.zhangteng.baselibrary.mvvm.vm.BaseMvvmDbDemoDbViewModel
import com.zhangteng.mvvm.mvvm.BaseMvvmDbActivity

class BaseMvvmDbDemoDbActivity :
    BaseMvvmDbActivity<BaseMvvmDbDemoDbViewModel, ActivityBaseMvvmDbDemoDbBinding>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base_mvvm_db_demo_db)
    }

    override fun initView() {

    }

    override fun initData() {

    }
}