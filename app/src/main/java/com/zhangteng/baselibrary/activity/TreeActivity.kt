package com.zhangteng.baselibrary.activity

import androidx.recyclerview.widget.LinearLayoutManager
import com.zhangteng.base.base.BaseListActivity
import com.zhangteng.baselibrary.adapter.TreeAdapter
import com.zhangteng.baselibrary.bean.TreeBean

class TreeActivity : BaseListActivity<TreeBean, TreeAdapter>() {
    private var data = ArrayList<TreeBean?>()
    override fun initData() {
        super.initData()
        loadData(1)
    }

    override fun setLayoutManager() {
        mRecyclerView?.layoutManager = LinearLayoutManager(this)
    }

    override fun createAdapter(): TreeAdapter {
        val rootTree = TreeBean().apply {
            id = "1"
            label = "1"
            children = ArrayList()
            parent = null
        }
        val oneTree = TreeBean().apply {
            id = "2"
            label = "2"
            children = ArrayList()
            parent = null
        }
        rootTree.children?.add(oneTree)

        val twoTree = TreeBean().apply {
            id = "3"
            label = "3"
            children = ArrayList()
            parent = null
        }
        oneTree.children?.add(twoTree)

        data.add(rootTree)
        return TreeAdapter(data, 1)
    }

    override fun loadData(page: Int) {
        showDataSuccess(1, data)
    }
}