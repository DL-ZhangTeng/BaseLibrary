package com.zhangteng.base.base

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import com.zhangteng.base.mvvm.base.BaseLoadingViewModel
import com.zhangteng.base.mvvm.base.BaseNoNetworkViewModel
import com.zhangteng.base.mvvm.base.BaseRefreshViewModel
import com.zhangteng.base.mvvm.base.BaseViewModel
import com.zhangteng.base.mvvm.manager.NetState
import com.zhangteng.base.mvvm.manager.NetworkStateManager
import com.zhangteng.base.mvvm.utils.getVmClazz

/**
 * ViewModelFragment基类，自动把ViewModel注入Fragment
 */

abstract class BaseMvvmFragment<VM : BaseViewModel> : BaseFragment() {

    private val handler = Handler()

    //是否第一次加载
    protected var isFirst: Boolean = true

    lateinit var mViewModel: VM

    lateinit var mActivity: AppCompatActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mActivity = context as AppCompatActivity
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        isFirst = true
        mViewModel = createViewModel()
        initView(view, savedInstanceState)
        createObserver()
        registerDefUIChange()
    }

    override fun onResume() {
        super.onResume()
        onVisible()
    }

    /**
     * 创建viewModel
     */
    protected fun createViewModel(): VM {
        return ViewModelProvider(this).get(getVmClazz(this))
    }

    /**
     * 是否需要懒加载
     */
    private fun onVisible() {
        if (lifecycle.currentState == Lifecycle.State.STARTED && isFirst) {
            // 延迟加载 防止 切换动画还没执行完毕时数据就已经加载好了，这时页面会有渲染卡顿
            handler.postDelayed({
                lazyLoadData()
                //在Fragment中，只有懒加载过了才能开启网络变化监听
                NetworkStateManager.instance.mNetworkStateCallback.observe(
                    this,
                    {
                        //不是首次订阅时调用方法，防止数据第一次监听错误
                        if (!isFirst) {
                            onNetworkStateChanged(it)
                        }
                    })
                isFirst = false
            }, lazyLoadTime())
        }
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
     * 懒加载
     */
    protected abstract fun lazyLoadData()

    /**
     * 创建观察者
     */
    protected abstract fun createObserver()

    /**
     * 网络变化监听 子类重写
     */
    protected open fun onNetworkStateChanged(netState: NetState) {}

    /**
     * 完成加载刷新动画
     */
    protected open fun finishRefreshOrLoadMore() {}

    /**
     * 延迟加载 防止 切换动画还没执行完毕时数据就已经加载好了，这时页面会有渲染卡顿  bug
     * 这里传入你想要延迟的时间，延迟时间可以设置比转场动画时间长一点 单位： 毫秒
     * 不传默认 300毫秒
     * @return Long
     */
    protected open fun lazyLoadTime(): Long {
        return 300
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
    }
}