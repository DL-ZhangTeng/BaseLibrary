package com.zhangteng.mvvm.mvvm

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.zhangteng.base.base.BaseFragment
import com.zhangteng.mvvm.base.BaseLoadingViewModel
import com.zhangteng.mvvm.base.BaseNoNetworkViewModel
import com.zhangteng.mvvm.base.BaseRefreshViewModel
import com.zhangteng.mvvm.base.BaseViewModel
import com.zhangteng.mvvm.manager.NetState
import com.zhangteng.mvvm.manager.NetworkStateManager
import com.zhangteng.mvvm.utils.getVmClazz

/**
 * ViewModelFragment基类，自动把ViewModel注入Fragment
 */

abstract class BaseMvvmFragment<VM : BaseViewModel> : BaseFragment() {

    lateinit var mViewModel: VM

    lateinit var mActivity: AppCompatActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mActivity = context as AppCompatActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel = createViewModel()
        registerDefUIChange()
    }

    override fun lazyLoadData() {
        super.lazyLoadData()
        //在Fragment中，只有懒加载过了才能开启网络变化监听
        NetworkStateManager.instance.mNetworkStateCallback.observe(
            this
        ) {
            //不是首次订阅时调用方法，防止数据第一次监听错误
            if (!isFirst) {
                onNetworkStateChanged(it)
            }
        }
    }

    /**
     * 创建viewModel
     */
    protected fun createViewModel(): VM {
        return ViewModelProvider(this).get(getVmClazz(this))
    }

    /**
     * 注册 UI 事件
     */
    protected fun registerDefUIChange() {
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
        if (mViewModel is BaseNoNetworkViewModel) {
            //显示
            (mViewModel as BaseNoNetworkViewModel).networkChange.showNoNetwork.observe(
                this
            ) {
                showNoNetView(it)
            }
            //关闭
            (mViewModel as BaseNoNetworkViewModel).networkChange.hideNoNetwork.observe(
                this
            ) {
                hiddenNoNetView(it)
            }
            //显示
            (mViewModel as BaseNoNetworkViewModel).networkChange.showNoDataView.observe(
                this
            ) {
                showEmptyView(it)
            }
            //关闭
            (mViewModel as BaseNoNetworkViewModel).networkChange.hideNoDataView.observe(
                this
            ) {
                hiddenEmptyView(it)
            }
        }
        if (mViewModel is BaseRefreshViewModel) {
            (mViewModel as BaseRefreshViewModel).listChange.finishRefreshOrLoadMore.observe(
                this
            ) {
                finishRefreshOrLoadMore()
            }
        }
    }

    /**
     * 网络变化监听 子类重写
     */
    protected open fun onNetworkStateChanged(netState: NetState) {}

    /**
     * 完成加载刷新动画
     */
    protected open fun finishRefreshOrLoadMore() {}

}