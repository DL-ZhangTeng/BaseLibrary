package com.zhangteng.base.adapter

import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.collection.SparseArrayCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.zhangteng.base.base.BaseAdapter
import com.zhangteng.base.base.BaseAdapter.DefaultViewHolder

/**
 * 在已有recyclerview基础上无需修改adapter情况下添加头脚视图
 * Created by swing on 2018/5/4.
 */
abstract class HeaderOrFooterAdapter<T>(private val mInnerAdapter: BaseAdapter<T, DefaultViewHolder>) : BaseAdapter<T, DefaultViewHolder>(mInnerAdapter.data) {
    private val mHeaderViewInts: SparseArrayCompat<Int?> = SparseArrayCompat()
    private val mFootViewInts: SparseArrayCompat<Int?> = SparseArrayCompat()
    private val mHeaderViews: SparseArrayCompat<View?> = SparseArrayCompat()
    private val mFootViews: SparseArrayCompat<View?> = SparseArrayCompat()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DefaultViewHolder {
        if (mHeaderViewInts.get(viewType) != null) {
            val holder = createHeaderOrFooterViewHolder(parent, mHeaderViewInts.get(viewType))
            mHeaderViews.put(viewType, holder.itemView)
            return holder
        } else if (mFootViewInts.get(viewType) != null) {
            val holder = createHeaderOrFooterViewHolder(parent, mFootViewInts.get(viewType))
            mFootViews.put(viewType, holder.itemView)
            return holder
        }
        return mInnerAdapter.onCreateViewHolder(parent, viewType)
    }

    override fun onBindViewHolder(holder: DefaultViewHolder, position: Int) {
        if (isHeaderViewPos(position)) {
            onBindHeaderOrFooterViewHolder(holder, getItemViewType(position))
            return
        }
        if (isFooterViewPos(position)) {
            onBindHeaderOrFooterViewHolder(holder, getItemViewType(position))
            return
        }
        mInnerAdapter.onBindViewHolder(holder, position - getHeadersCount())
    }

    override fun getItemCount(): Int {
        return getHeadersCount() + getFootersCount() + getRealItemCount()
    }

    override fun getItemViewType(position: Int): Int {
        if (isHeaderViewPos(position)) {
            return mHeaderViewInts.keyAt(position)
        } else if (isFooterViewPos(position)) {
            return mFootViewInts.keyAt(position - getHeadersCount() - getRealItemCount())
        }
        return mInnerAdapter.getItemViewType(position - getHeadersCount())
    }

    fun notifyHFAdpterItemChanged(position: Int) {
        notifyItemChanged(position + getHeadersCount())
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        mInnerAdapter.onAttachedToRecyclerView(recyclerView)
        val layoutManager = recyclerView.layoutManager
        if (layoutManager is GridLayoutManager) {
            val gridLayoutManager = layoutManager as GridLayoutManager?
            gridLayoutManager?.let {
                val spanSizeLookup = it.spanSizeLookup
                it.spanSizeLookup = object : SpanSizeLookup() {
                    override fun getSpanSize(position: Int): Int {
                        val viewType = getItemViewType(position)
                        if (mHeaderViewInts.get(viewType) != null) {
                            return it.spanCount
                        } else if (mFootViewInts.get(viewType) != null) {
                            return it.spanCount
                        }
                        return spanSizeLookup?.getSpanSize(position) ?: 1
                    }
                }
                it.spanCount = it.spanCount
            }
        }
    }

    override fun onViewAttachedToWindow(holder: DefaultViewHolder) {
        mInnerAdapter.onViewAttachedToWindow(holder)
        val position = holder.layoutPosition
        if (isHeaderViewPos(position) || isFooterViewPos(position)) {
            val lp = holder.itemView.layoutParams
            if (lp != null
                    && lp is StaggeredGridLayoutManager.LayoutParams) {
                lp.isFullSpan = true
            }
        }
    }

    private fun isHeaderViewPos(position: Int): Boolean {
        return position < getHeadersCount()
    }

    private fun isFooterViewPos(position: Int): Boolean {
        return position >= getHeadersCount() + getRealItemCount()
    }

    fun addHeaderView(@LayoutRes view: Int) {
        hasHeaderOrFooter = true
        mHeaderViewInts.put(mHeaderViewInts.size() + BASE_ITEM_TYPE_HEADER, view)
    }

    fun addFootView(@LayoutRes view: Int) {
        hasHeaderOrFooter = true
        mFootViewInts.put(mFootViewInts.size() + BASE_ITEM_TYPE_FOOTER, view)
    }

    fun getHeadersCount(): Int {
        return mHeaderViewInts.size()
    }

    fun getFootersCount(): Int {
        return mFootViewInts.size()
    }

    private fun getRealItemCount(): Int {
        return mInnerAdapter.getItemCount()
    }

    abstract fun createHeaderOrFooterViewHolder(parent: ViewGroup?, viewInt: Int?): DefaultViewHolder
    abstract fun onBindHeaderOrFooterViewHolder(holder: DefaultViewHolder, viewType: Int)
    fun getHeaderViewByType(viewType: Int): View? {
        return mHeaderViews.get(viewType)
    }

    fun getFooterViewByType(viewType: Int): View? {
        return mFootViews.get(viewType)
    }

    fun getHeaderViewByPos(position: Int): View? {
        return mHeaderViews.get(getItemViewType(position))
    }

    fun getFooterViewByPos(position: Int): View? {
        return mFootViews.get(getItemViewType(position))
    }

    companion object {
        const val BASE_ITEM_TYPE_HEADER = 100000
        const val BASE_ITEM_TYPE_FOOTER = 200000
    }
}