package com.zhangteng.baselibrary.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.zhangteng.baselibrary.R
import com.zhangteng.baselibrary.adapter.BaseListDemoAdapter
import com.zhangteng.baselibrary.bean.BaseListDemoBean
import com.zhangteng.baselibrary.mvvm.vm.BaseListMvvmDemoFragmentViewModel
import com.zhangteng.mvvm.mvvm.BaseListMvvmFragment

class BaseListMvvmDemoFragment :
    BaseListMvvmFragment<BaseListMvvmDemoFragmentViewModel, BaseListDemoBean, BaseListDemoAdapter>() {

    companion object {
        fun newInstance() = BaseListMvvmDemoFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_base_list_mvvm_demo, container, false)
    }

    override fun initView(view: View, savedInstanceState: Bundle?) {
        super.initView(view, savedInstanceState)
    }

    override fun initData(savedInstanceState: Bundle?) {
        super.initData(savedInstanceState)
    }

    override fun createAdapter(): BaseListDemoAdapter {
        return BaseListDemoAdapter()
    }

    override fun getRecyclerView(): RecyclerView? {
        return view?.findViewById(R.id.recyclerView)
    }

    override fun getSmartRefreshLayout(): SmartRefreshLayout? {
        return view?.findViewById(R.id.smartRefreshLayout)
    }

    override fun loadData(i: Int) {}
    override fun setLayoutManager() {
        setLinearLayoutManager(LinearLayoutManager.VERTICAL)
    }
}