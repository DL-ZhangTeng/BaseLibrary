package com.zhangteng.baselibrary.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.zhangteng.base.base.BaseFragment
import com.zhangteng.baselibrary.R

class BaseDemoFragment : BaseFragment() {

    companion object {
        fun newInstance() = BaseDemoFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_base_demo, container, false)
    }

    override fun initView(view: View, savedInstanceState: Bundle?) {

    }
}