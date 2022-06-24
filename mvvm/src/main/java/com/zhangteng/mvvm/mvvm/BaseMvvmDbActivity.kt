package com.zhangteng.mvvm.mvvm

import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.zhangteng.mvvm.BR
import com.zhangteng.mvvm.base.BaseViewModel


/**
 * 包含ViewModel 和Databind ViewModelActivity基类，把ViewModel 和Databind注入进来了
 * 需要使用Databind的请继承它
 */
abstract class BaseMvvmDbActivity<VM : BaseViewModel, DB : ViewDataBinding> :
    BaseMvvmActivity<VM>() {

    lateinit var mDatabind: DB

    override fun setContentView(layoutResID: Int) {
        mDatabind = DataBindingUtil.bind(layoutInflater.inflate(layoutResID, null))!!
        mDatabind.setVariable(BR.viewModel, mViewModel)
        mDatabind.lifecycleOwner = this
        super.setContentView(mDatabind.root)
    }

    override fun setContentView(view: View?) {
        mDatabind = DataBindingUtil.bind(view!!)!!
        mDatabind.setVariable(BR.viewModel, mViewModel)
        mDatabind.lifecycleOwner = this
        super.setContentView(mDatabind.root)
    }

    override fun setContentView(view: View?, params: ViewGroup.LayoutParams?) {
        mDatabind = DataBindingUtil.bind(view!!)!!
        mDatabind.setVariable(BR.viewModel, mViewModel)
        mDatabind.lifecycleOwner = this
        super.setContentView(mDatabind.root, params)
    }
}