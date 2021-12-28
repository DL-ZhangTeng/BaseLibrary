package com.zhangteng.baselibrary.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.zhangteng.base.base.BaseAdapter
import com.zhangteng.baselibrary.R

import com.zhangteng.baselibrary.bean.BaseListMvpDemoBean

class BaseListMvpDemoAdapter : BaseAdapter<BaseListMvpDemoBean, BaseAdapter.DefaultViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DefaultViewHolder {
        return DefaultViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_base_list_mvp_demo, parent, false)
        )
    }

    override fun onBindViewHolder(
        holder: DefaultViewHolder,
        item: BaseListMvpDemoBean?,
        position: Int
    ) {

    }
}