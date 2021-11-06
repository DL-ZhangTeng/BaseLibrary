package com.zhangteng.base.base

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.zhangteng.base.mvvm.base.BaseLoadingViewModel
import com.zhangteng.base.mvvm.base.BaseViewModel
import com.zhangteng.base.mvvm.manager.NetState
import com.zhangteng.base.mvvm.manager.NetworkStateManager
import com.zhangteng.base.mvvm.utils.getVmClazz

/**
 * ViewModelActivity基类，把ViewModel注入进来了
 */
abstract class BaseMvvmActivity<VM : BaseViewModel> : BaseActivity() {

    /**
     * 是否需要使用DataBinding 供子类BaseVmDbActivity修改，用户请慎动
     */
    private var isUserDb = false

    lateinit var mViewModel: VM

    abstract fun layoutId(): Int

    abstract fun showLoading(message: String = "请求网络中...")

    abstract fun dismissLoading()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isMvvmModel = true
        if (!isUserDb) {
            setContentView(layoutId())
        } else {
            initDataBind()
        }
        mViewModel = createViewModel()
        registerUiChange()
        initView()
        createObserver()
        NetworkStateManager.instance.mNetworkStateCallback.observe(this, Observer {
            onNetworkStateChanged(it)
        })
        initData()
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

    /**
     * 创建LiveData数据观察者
     */
    abstract fun createObserver()

    /**
     * 注册UI 事件
     */
    private fun registerUiChange() {
        if (mViewModel is BaseLoadingViewModel) {
            //显示弹窗
            (mViewModel as BaseLoadingViewModel).loadingChange.showLoadingView.observe(
                this,
                Observer {
                    showLoading(it)
                })
            //关闭弹窗
            (mViewModel as BaseLoadingViewModel).loadingChange.dismissLoadingView.observe(
                this,
                Observer {
                    dismissLoading()
                })
        }
    }

    /**
     * 将非该Activity绑定的ViewModel添加 loading回调 防止出现请求时不显示 loading 弹窗bug
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

    fun userDataBinding(isUserDb: Boolean) {
        this.isUserDb = isUserDb
    }

    /**
     * 供子类BaseVmDbActivity 初始化Databinding操作
     */
    open fun initDataBind() {}
}