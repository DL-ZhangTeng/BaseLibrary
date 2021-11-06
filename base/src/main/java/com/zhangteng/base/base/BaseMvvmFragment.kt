package com.zhangteng.base.base

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.zhangteng.base.mvvm.base.BaseLoadingViewModel
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
    private var isFirst: Boolean = true

    lateinit var mViewModel: VM

    lateinit var mActivity: AppCompatActivity

    /**
     * 当前Fragment绑定的视图布局
     */
    abstract fun layoutId(): Int

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(layoutId(), container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        isMvvmModel = true
        mActivity = context as AppCompatActivity
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isFirst = true
        mViewModel = createViewModel()
        initView(view, savedInstanceState)
        createObserver()
        registerDefUIChange()
    }

    /**
     * 网络变化监听 子类重写
     */
    open fun onNetworkStateChanged(netState: NetState) {}

    /**
     * 创建viewModel
     */
    private fun createViewModel(): VM {
        return ViewModelProvider(this).get(getVmClazz(this))
    }

    override fun initView(view: View, savedInstanceState: Bundle?) {

    }

    /**
     * 懒加载
     */
    abstract fun lazyLoadData()

    /**
     * 创建观察者
     */
    abstract fun createObserver()

    override fun onResume() {
        super.onResume()
        onVisible()
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
                    Observer {
                        //不是首次订阅时调用方法，防止数据第一次监听错误
                        if (!isFirst) {
                            onNetworkStateChanged(it)
                        }
                    })
                isFirst = false
            }, lazyLoadTime())
        }
    }

    abstract fun showLoading(message: String = "请求网络中...")

    abstract fun dismissLoading()

    /**
     * 注册 UI 事件
     */
    private fun registerDefUIChange() {
        if (mViewModel is BaseLoadingViewModel) {
            (mViewModel as BaseLoadingViewModel).loadingChange.showLoadingView.observe(
                this,
                Observer {
                    showLoading(it)
                })
            (mViewModel as BaseLoadingViewModel).loadingChange.dismissLoadingView.observe(
                this,
                Observer {
                    dismissLoading()
                })
        }
    }

    /**
     * 将非该Fragment绑定的ViewModel添加 loading回调 防止出现请求时不显示 loading 弹窗bug
     * @param viewModels Array<out BaseViewModel>
     */
    protected fun addLoadingObserve(vararg viewModels: BaseLoadingViewModel) {
        viewModels.forEach { viewModel ->
            //显示弹窗
            viewModel.loadingChange.showLoadingView.observe(this, Observer {
                showLoading(it)
            })
            //关闭弹窗
            viewModel.loadingChange.dismissLoadingView.observe(this, Observer {
                dismissLoading()
            })
        }
    }

    /**
     * 延迟加载 防止 切换动画还没执行完毕时数据就已经加载好了，这时页面会有渲染卡顿  bug
     * 这里传入你想要延迟的时间，延迟时间可以设置比转场动画时间长一点 单位： 毫秒
     * 不传默认 300毫秒
     * @return Long
     */
    open fun lazyLoadTime(): Long {
        return 300
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
    }
}