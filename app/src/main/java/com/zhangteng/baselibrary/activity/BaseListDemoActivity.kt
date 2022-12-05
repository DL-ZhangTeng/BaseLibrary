package com.zhangteng.baselibrary.activity

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.zhangteng.base.base.BaseAdapter
import com.zhangteng.base.base.BaseListActivity
import com.zhangteng.baselibrary.R
import com.zhangteng.baselibrary.adapter.BaseListDemoAdapter
import com.zhangteng.baselibrary.bean.BaseListDemoBean

class BaseListDemoActivity :
    BaseListActivity<BaseListDemoBean, BaseAdapter.DefaultViewHolder, BaseListDemoAdapter>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base_list_demo)
    }

    override fun initView() {
        super.initView()

    }

    override fun initData() {}

    override fun createAdapter(): BaseListDemoAdapter {
        return BaseListDemoAdapter()
    }

    override fun getRecyclerView(): RecyclerView {
        return findViewById(R.id.recyclerView)
    }

    override fun getSmartRefreshLayout(): SmartRefreshLayout {
        return findViewById(R.id.smartRefreshLayout)
    }

    override fun loadData(i: Int) {}
    override fun setLayoutManager() {
        setLinearLayoutManager(LinearLayoutManager.VERTICAL)
    }
}