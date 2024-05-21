package com.zhangteng.base.adapter

import android.annotation.SuppressLint
import android.content.*
import com.zhangteng.base.base.BaseAdapter
import com.zhangteng.base.tree.*

/**
 * 树结构的列表适配器
 * Created by swing on 2018/6/29.
 */
abstract class TreeRecyclerViewAdapter<T>(data: MutableList<T?>?, defaultExpandLevel: Int = 1) :
    BaseAdapter<T, BaseAdapter.DefaultViewHolder>() {

    /**
     * 存储所有可见的Node
     */
    protected var mNodes: MutableList<Node<T?>?>?

    /**
     * 存储所有的Node
     */
    protected var mAllNodes: MutableList<Node<T?>?>?

    /**
     * 点击的回调接口
     */
    var onTreeNodeClickListener: OnTreeNodeClickListener? = null

    override fun onBindViewHolder(holder: DefaultViewHolder, item: T?, position: Int) {
        val node = mNodes?.get(position)
        onBindViewHolder(holder, node, position)

        /**
         * 设置节点点击时，可以展开以及关闭；并且将ItemClick事件继续往外公布
         */
        holder.itemView.setOnClickListener {
            expandOrCollapse(position)
            if (onTreeNodeClickListener != null) {
                onTreeNodeClickListener!!.onClick(mNodes?.get(position), position)
            }
        }
    }

    override fun getItemCount(): Int {
        return mNodes?.size ?: 0
    }

    abstract fun onBindViewHolder(
        holder: DefaultViewHolder?,
        node: Node<T?>?,
        position: Int
    )

    /**
     * 相应ListView的点击事件 展开或关闭某节点
     *
     * @param position
     */
    @SuppressLint("NotifyDataSetChanged")
    open fun expandOrCollapse(position: Int) {
        val n = mNodes?.get(position)
        if (n != null) { // 排除传入参数错误异常
            if (!n.isLeaf()) {
                n.setExpand(!n.isExpand())
                mNodes = TreeHelper.filterVisibleNode(mAllNodes)
                notifyDataSetChanged() // 刷新视图
            }
        }
    }

    /**
     * 获取显示的节点
     *
     * @param position
     */
    fun getVisibleNode(position: Int): Node<T?>? {
        return mNodes?.get(position)
    }

    interface OnTreeNodeClickListener {
        open fun <T> onClick(node: Node<T?>?, position: Int)
    }

    /**
     * @param data 原始数据
     * @param defaultExpandLevel 默认展开几级树
     */
    init {
        /**
         * 对所有的Node进行排序
         */
        mAllNodes = TreeHelper.getSortedNodes(data, defaultExpandLevel)
        /**
         * 过滤出可见的Node
         */
        mNodes = TreeHelper.filterVisibleNode(mAllNodes)
    }
}