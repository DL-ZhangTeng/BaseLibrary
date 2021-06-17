package com.zhangteng.base.adapter

import android.annotation.SuppressLint
import android.content.*
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.zhangteng.base.tree.*

/**
 * 树结构的列表适配器
 * Created by swing on 2018/6/29.
 */
abstract class TreeRecyclerViewAdapter<T>(
    mTree: RecyclerView?,
    protected var mContext: Context?,
    datas: MutableList<T?>?,
    defaultExpandLevel: Int
) : RecyclerView.Adapter<RecyclerView.ViewHolder?>() {
    /**
     * 存储所有可见的Node
     */
    protected var mNodes: MutableList<Node?>?
    protected var mInflater: LayoutInflater?

    /**
     * 存储所有的Node
     */
    protected var mAllNodes: MutableList<Node?>?

    /**
     * 点击的回调接口
     */
    private var onTreeNodeClickListener: OnTreeNodeClickListener? = null
    open fun setOnTreeNodeClickListener(onTreeNodeClickListener: OnTreeNodeClickListener?) {
        this.onTreeNodeClickListener = onTreeNodeClickListener
    }

    /**
     * 相应ListView的点击事件 展开或关闭某节点
     *
     * @param position
     */
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return getCreateViewHolder(parent, viewType)
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        @SuppressLint("RecyclerView") position: Int
    ) {
        val node = mNodes?.get(position)
        getBindViewHolder(node, position, holder)
        // 设置内边距
        holder.itemView.setPadding((node?.getLevel() ?: 0) * 40, 3, 3, 3)
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

    abstract fun getCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder
    abstract fun getBindViewHolder(node: Node?, position: Int, holder: RecyclerView.ViewHolder?)
    interface OnTreeNodeClickListener {
        open fun onClick(node: Node?, position: Int)
    }

    /**
     * @param mTree
     * @param context
     * @param datas
     * @param defaultExpandLevel 默认展开几级树
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    init {
        /**
         * 对所有的Node进行排序
         */
        mAllNodes = TreeHelper.getSortedNodes(datas, defaultExpandLevel)
        /**
         * 过滤出可见的Node
         */
        mNodes = TreeHelper.filterVisibleNode(mAllNodes)
        mInflater = LayoutInflater.from(mContext)
    }
}