package com.zhangteng.baselibrary.activity

import android.os.Bundle

import com.zhangteng.base.base.BaseActivity
import com.zhangteng.baselibrary.R

class BaseDemoActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base_demo)
    }

    override fun initView() {

    }

    override fun initData() {

    }
}