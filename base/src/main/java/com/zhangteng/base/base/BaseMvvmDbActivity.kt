package com.zhangteng.base.base

import android.os.Bundle
import androidx.databinding.ViewDataBinding
import com.zhangteng.base.mvvm.base.BaseViewModel

/**
 * 包含ViewModel 和Databind ViewModelActivity基类，把ViewModel 和Databind注入进来了
 * 需要使用Databind的清继承它
 */
abstract class BaseMvvmDbActivity<VM : BaseViewModel, DB : ViewDataBinding> :
    BaseMvvmActivity<VM>() {

    lateinit var mDatabind: DB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mDatabind = createDataBind()
        mDatabind.lifecycleOwner = this
    }

    /**
     * 创建DataBinding, 使用DataBindingUtil.setContentView(this, layoutId)
     */
    abstract fun createDataBind(): DB
}