package com.zhangteng.baselibrary.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.zhangteng.base.base.BaseAdapter
import com.zhangteng.baselibrary.R

import com.zhangteng.baselibrary.bean.BaseListDemoBean

class BaseListDemoAdapter : BaseAdapter<BaseListDemoBean, BaseAdapter.DefaultViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DefaultViewHolder {
        return DefaultViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_base_list_demo, parent, false)
        )
    }
}