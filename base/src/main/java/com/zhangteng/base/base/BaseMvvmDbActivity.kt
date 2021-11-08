package com.zhangteng.base.base

import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.zhangteng.base.mvvm.base.BaseViewModel
import com.zhangteng.base.mvvm.manager.NetworkStateManager

/**
 * 包含ViewModel 和Databind ViewModelActivity基类，把ViewModel 和Databind注入进来了
 * 需要使用Databind的请继承它
 */
abstract class BaseMvvmDbActivity<VM : BaseViewModel, DB : ViewDataBinding> :
    BaseMvvmActivity<VM>() {

    lateinit var mDatabind: DB

    override fun setContentView(layoutResID: Int) {
        mDatabind = DataBindingUtil.setContentView(this, layoutResID)
        mDatabind.lifecycleOwner = this

        mViewModel = createViewModel()
        registerUiChange()
        initView()
        createObserver()
        NetworkStateManager.instance.mNetworkStateCallback.observe(this, { state ->
            onNetworkStateChanged(state)
        })
        initData()
    }

    override fun setContentView(view: View?) {
        view?.id?.let {
            mDatabind = DataBindingUtil.setContentView(this, it)
            mDatabind.lifecycleOwner = this

            mViewModel = createViewModel()
            registerUiChange()
            initView()
            createObserver()
            NetworkStateManager.instance.mNetworkStateCallback.observe(this, { state ->
                onNetworkStateChanged(state)
            })
            initData()
        }
    }

    override fun setContentView(view: View?, params: ViewGroup.LayoutParams?) {
        view?.id?.let {
            mDatabind = DataBindingUtil.setContentView(this, it)
            mDatabind.lifecycleOwner = this

            mViewModel = createViewModel()
            registerUiChange()
            initView()
            createObserver()
            NetworkStateManager.instance.mNetworkStateCallback.observe(this, { state ->
                onNetworkStateChanged(state)
            })
            initData()
        }
    }
}