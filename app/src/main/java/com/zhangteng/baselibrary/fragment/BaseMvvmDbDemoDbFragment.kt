package com.zhangteng.baselibrary.fragment

import android.os.Bundle
import android.view.View
import com.zhangteng.mvvm.mvvm.BaseMvvmDbFragment
import com.zhangteng.baselibrary.R
import com.zhangteng.baselibrary.databinding.FragmentBaseMvvmDbDemoDbBinding
import com.zhangteng.baselibrary.mvvm.vm.BaseMvvmDbDemoDbFragmentViewModel

class BaseMvvmDbDemoDbFragment :
    com.zhangteng.mvvm.mvvm.BaseMvvmDbFragment<BaseMvvmDbDemoDbFragmentViewModel, FragmentBaseMvvmDbDemoDbBinding>() {

    companion object {
        fun newInstance() = BaseMvvmDbDemoDbFragment()
    }

    override fun initView(view: View, savedInstanceState: Bundle?) {

    }

    override fun initData(savedInstanceState: Bundle?) {
        super.initData(savedInstanceState)

    }

    override fun layoutId(): Int {
        return R.layout.fragment_base_mvvm_db_demo_db
    }
}