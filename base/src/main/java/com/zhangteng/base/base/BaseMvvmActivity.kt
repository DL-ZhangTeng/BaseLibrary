package com.zhangteng.base.base

import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.zhangteng.base.mvvm.base.BaseLoadingViewModel
import com.zhangteng.base.mvvm.base.BaseNoNetworkViewModel
import com.zhangteng.base.mvvm.base.BaseRefreshViewModel
import com.zhangteng.base.mvvm.base.BaseViewModel
import com.zhangteng.base.mvvm.manager.NetState
import com.zhangteng.base.mvvm.manager.NetworkStateManager
import com.zhangteng.base.mvvm.utils.getVmClazz

/**
 * ViewModelActivity基类，把ViewModel注入进来了
 */
abstract class BaseMvvmActivity<VM : BaseViewModel> : BaseActivity() {

    lateinit var mViewModel: VM

    override fun setContentView(layoutResID: Int) {
        delegate.setContentView(layoutResID)
        mViewModel = createViewModel()
        registerUiChange()
        initView()
        createObserver()
        NetworkStateManager.instance.mNetworkStateCallback.observe(this, {
            onNetworkStateChanged(it)
        })
        initData()
    }

    override fun setContentView(view: View?) {
        delegate.setContentView(view)
        mViewModel = createViewModel()
        registerUiChange()
        initView()
        createObserver()
        NetworkStateManager.instance.mNetworkStateCallback.observe(this, {
            onNetworkStateChanged(it)
        })
        initData()
    }

    override fun setContentView(view: View?, params: ViewGroup.LayoutParams?) {
        delegate.setContentView(view, params)
        mViewModel = createViewModel()
        registerUiChange()
        initView()
        createObserver()
        NetworkStateManager.instance.mNetworkStateCallback.observe(this, {
            onNetworkStateChanged(it)
        })
        initData()
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
                this,
                {
                    dismissProgressDialog()
                })
        }
        if (mViewModel is BaseNoNetworkViewModel) {
            //显示
            (mViewModel as BaseNoNetworkViewModel).networkChange.showNoNetwork.observe(
                this,
                {
                    showNoNetView(it)
                }
            )
            //关闭
            (mViewModel as BaseNoNetworkViewModel).networkChange.hideNoNetwork.observe(
                this,
                {
                    hiddenNoNetView(it)
                })
            //显示
            (mViewModel as BaseNoNetworkViewModel).networkChange.showNoDataView.observe(
                this,
                {
                    showNoContentView(it)
                }
            )
            //关闭
            (mViewModel as BaseNoNetworkViewModel).networkChange.hideNoDataView.observe(
                this,
                {
                    hiddenNoContentView(it)
                })
        }
        if (mViewModel is BaseRefreshViewModel) {
            (mViewModel as BaseRefreshViewModel).listChange.finishRefreshOrLoadMore.observe(
                this,
                {
                    finishRefreshOrLoadMore()
                }
            )
        }
    }

    /**
     * 创建LiveData数据观察者
     */
    protected abstract fun createObserver()

    /**
     * 网络变化监听(通过广播获取变化，需要注册广播接收者[com.zhangteng.base.mvvm.manager.NetworkStateReceive]) 子类重写
     */
    protected open fun onNetworkStateChanged(netState: NetState) {}

    /**
     * 完成加载刷新动画
     */
    protected open fun finishRefreshOrLoadMore() {}
}