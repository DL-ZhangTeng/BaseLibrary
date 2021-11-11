package com.zhangteng.baselibrary.ui.mvvmdb

import android.os.Bundle
import com.zhangteng.base.base.BaseMvvmDbActivity
import com.zhangteng.baselibrary.R
import com.zhangteng.baselibrary.databinding.MvvmDbActivityBinding

class MvvmDbActivity : BaseMvvmDbActivity<MvvmDbActivityViewModel, MvvmDbActivityBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MvvmDbFragment.newInstance())
                .commitNow()
        }
    }

    override fun initView() {

    }

    override fun initData() {

    }

    override fun layoutId(): Int {
        return R.layout.mvvm_db_activity
    }
}