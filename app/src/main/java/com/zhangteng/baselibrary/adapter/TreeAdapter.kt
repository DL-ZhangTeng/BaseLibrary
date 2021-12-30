package com.zhangteng.baselibrary.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import com.zhangteng.base.adapter.TreeRecyclerViewAdapter
import com.zhangteng.base.tree.Node
import com.zhangteng.baselibrary.R
import com.zhangteng.baselibrary.bean.TreeBean

class TreeAdapter(
    data: MutableList<TreeBean?>?,
    defaultExpandLevel: Int
) : TreeRecyclerViewAdapter<TreeBean>(data, defaultExpandLevel) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DefaultViewHolder {
        return DefaultViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_tree_layout, parent, false)
        )
    }

    override fun onBindViewHolder(
        holder: DefaultViewHolder?,
        item: TreeBean?,
        position: Int,
        node: Node?
    ) {
        holder?.getView<TextView>(R.id.tv_name)?.text = node?.getName()
    }
}