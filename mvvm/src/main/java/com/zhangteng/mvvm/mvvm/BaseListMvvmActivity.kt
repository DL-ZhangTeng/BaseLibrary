package com.zhangteng.mvvm.mvvm

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.zhangteng.base.base.BaseAdapter
import com.zhangteng.base.base.BaseListActivity
import com.zhangteng.mvvm.base.BaseLoadingViewModel
import com.zhangteng.mvvm.base.BaseRefreshViewModel
import com.zhangteng.mvvm.base.BaseStateViewModel
import com.zhangteng.mvvm.base.BaseViewModel
import com.zhangteng.mvvm.manager.NetState
import com.zhangteng.mvvm.manager.NetworkStateManager
import com.zhangteng.mvvm.utils.getVmClazz

/**
 * ViewModelActivity基类，把ViewModel注入进来了
 */
abstract class BaseListMvvmActivity<VM : BaseViewModel, D, A : BaseAdapter<D, BaseAdapter.DefaultViewHolder>> :
    BaseListActivity<D, A>() {
    lateinit var mViewModel: VM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel = createViewModel()
        registerUiChange()
        NetworkStateManager.instance.mNetworkStateCallback.observe(this) {
            onNetworkStateChanged(it)
        }
    }

    /**
     * 创建viewModel
     */
    protected fun createViewModel(): VM {
        return ViewModelProvider(this).get(getVmClazz(this))
    }

    /**
     * 注册UI 事件(处理了加载中，无网络，无数据，完成刷新等)
     */
    protected fun registerUiChange() {
        if (mViewModel is BaseLoadingViewModel) {
            //显示弹窗
            (mViewModel as BaseLoadingViewModel).loadingChange.showLoadingView.observe(
                this,
                this::showProgressDialog
            )
            //关闭弹窗
            (mViewModel as BaseLoadingViewModel).loadingChange.dismissLoadingView.observe(
                this
            ) {
                dismissProgressDialog()
            }
        }
        if (mViewModel is BaseStateViewModel) {
            (mViewModel as BaseStateViewModel).stateChange.showNoNetView.observe(
                this
            ) {
                showNoNetView(it)
            }
            (mViewModel as BaseStateViewModel).stateChange.showTimeOutView.observe(
                this
            ) {
                showTimeOutView(it)
            }
            (mViewModel as BaseStateViewModel).stateChange.showEmptyView.observe(
                this
            ) {
                showEmptyView(it)
            }
            (mViewModel as BaseStateViewModel).stateChange.showErrorView.observe(
                this
            ) {
                showErrorView(it)
            }
            (mViewModel as BaseStateViewModel).stateChange.showNoLoginView.observe(
                this
            ) {
                showNoLoginView(it)
            }
            (mViewModel as BaseStateViewModel).stateChange.showContentView.observe(
                this
            ) {
                showContentView(it)
            }
        }
        if (mViewModel is BaseRefreshViewModel) {
            (mViewModel as BaseRefreshViewModel).refreshChange.finishRefreshOrLoadMore.observe(
                this
            ) {
                finishRefreshOrLoadMore()
            }
        }
    }

    /**
     * 网络变化监听(通过广播获取变化，需要注册广播接收者[com.zhangteng.base.mvvm.manager.NetworkStateReceive]) 子类重写
     */
    protected open fun onNetworkStateChanged(netState: NetState) {}

    /**
     * 完成加载刷新动画
     */
    protected open fun finishRefreshOrLoadMore() {}
}