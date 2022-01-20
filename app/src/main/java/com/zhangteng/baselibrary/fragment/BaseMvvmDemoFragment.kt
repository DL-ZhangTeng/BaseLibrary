package com.zhangteng.baselibrary.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.zhangteng.mvvm.mvvm.BaseMvvmFragment
import com.zhangteng.baselibrary.R
import com.zhangteng.baselibrary.mvvm.vm.BaseMvvmDemoFragmentViewModel

class BaseMvvmDemoFragment : com.zhangteng.mvvm.mvvm.BaseMvvmFragment<BaseMvvmDemoFragmentViewModel>() {

    companion object {
        fun newInstance() = BaseMvvmDemoFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_base_mvvm_demo, container, false)
    }

    override fun initView(view: View, savedInstanceState: Bundle?) {

    }

    override fun initData(savedInstanceState: Bundle?) {
        super.initData(savedInstanceState)

    }
}