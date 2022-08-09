package com.zhangteng.base.base

import android.annotation.SuppressLint
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.zhangteng.base.base.BaseAdapter.DefaultViewHolder
import com.zhangteng.utils.dp2px
import java.util.*

/**
 * 基础懒加载列表Fragment
 *
 * @since Swing
 */
abstract class BaseListActivity<D, A : BaseAdapter<D, DefaultViewHolder>> : BaseActivity() {
    protected var mRecyclerView: RecyclerView? = null
    protected var mRefreshLayout: SmartRefreshLayout? = null

    /**
     * 适配器
     */
    protected var mAdapter: A? = null

    /**
     * 列表数据 实体bean
     */
    protected var mList = ArrayList<D?>()

    /**
     * 当前请求的视频列表最后数据的页码，用于查询下一页数据
     */
    protected var mPage = 1

    /**
     * 当前请求的视频列表总数
     */
    protected var mTotal = 0

    override fun initView() {
        mRefreshLayout = getSmartRefreshLayout()
        mRecyclerView = getRecyclerView()
        setLayoutManager()
        initRecyclerView()
    }

    protected fun initRecyclerView() {
        mAdapter = createAdapter()
        //设置空布局
        showEmptyView(mRecyclerView)
        // 设置上拉刷新、下拉加载更多
        mRefreshLayout?.setOnRefreshListener { _: RefreshLayout? -> refreshData(true) }
        mRefreshLayout?.setOnLoadMoreListener { _: RefreshLayout? ->
            //加载更多
            mRefreshLayout?.setNoMoreData(false)
            refreshData(false)
        }
        mRecyclerView?.adapter = mAdapter
    }

    /**
     * 创建对应适配器
     *
     * @return 适配器
     */
    protected abstract fun createAdapter(): A

    /**
     * 设置RecyclerView
     */
    protected abstract fun getRecyclerView(): RecyclerView

    /**
     * 设置SmartRefreshLayout
     */
    protected abstract fun getSmartRefreshLayout(): SmartRefreshLayout

    /**
     * 设置布局样式
     */
    protected abstract fun setLayoutManager()

    protected fun setGridLayoutManager(spanCount: Int) {
        val layoutManager = GridLayoutManager(this, spanCount)
        mRecyclerView?.layoutManager = layoutManager
    }

    protected fun setLinearLayoutManager(@RecyclerView.Orientation orientation: Int) {
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = orientation
        mRecyclerView?.layoutManager = layoutManager
    }

    fun setMargin(margin: Int) {
        val layoutParams = mRecyclerView?.layoutParams as SmartRefreshLayout.LayoutParams
        layoutParams.topMargin = dp2px(margin.toFloat())
        layoutParams.bottomMargin = dp2px(margin.toFloat())
        layoutParams.leftMargin = dp2px(margin.toFloat())
        layoutParams.rightMargin = dp2px(margin.toFloat())
        mRecyclerView?.layoutParams = layoutParams
    }

    /**
     * 获取数据 带弹窗
     *
     * @param refresh 是否刷新
     */
    protected fun refreshData(refresh: Boolean) {
        if (refresh) {
            mPage = 1
        } else {
            mPage++
        }
        loadData(mPage)
    }

    /**
     * 加载数据
     *
     * @param page 页码
     */
    protected abstract fun loadData(page: Int)

    /**
     * 数据获取成功
     *
     * @param total 总数
     * @param data  数据
     */
    @SuppressLint("NotifyDataSetChanged")
    protected fun showDataSuccess(total: Int, data: List<D?>?) {
        mRefreshLayout?.finishRefresh()
        mTotal = 0
        if (mPage == 1) {
            mList.clear()
        }
        if (data != null) {
            mTotal = total
            mList.addAll(data)
        }
        if (mList.isEmpty()) {
            showEmptyView(mRecyclerView)
        } else {
            hiddenEmptyView(mRecyclerView)
        }
        if (mList.size >= mTotal || data?.isEmpty() == true) {
            mRefreshLayout?.finishLoadMoreWithNoMoreData()
        } else {
            mRefreshLayout?.finishLoadMore()
        }
        mAdapter?.notifyDataSetChanged()
    }

    /**
     * 失败
     * 无需区分因为什么错误 因为缺省图都一样
     */
    protected fun showDataFailure() {
        mRefreshLayout?.finishRefresh()
        mRefreshLayout?.finishLoadMoreWithNoMoreData()
        showEmptyView(mRecyclerView)
    }
}