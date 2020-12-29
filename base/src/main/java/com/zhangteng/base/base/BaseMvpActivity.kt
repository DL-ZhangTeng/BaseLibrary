package com.zhangteng.base.base

import com.zhangteng.base.mvp.base.BasePresenter
import com.zhangteng.base.mvp.base.BaseView

/**
 * 使用Mvp模式Activity基类
 * Created by swing on 2017/11/23.
 */
abstract class BaseMvpActivity<V : BaseView?, P : BasePresenter<V?>?> : BaseActivity() {
    protected var mPresenter: P? = null
    override fun initView() {
        mPresenter = createPresenter()
        mPresenter?.attachView(this as V)
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter?.detachView()
    }

    /**
     * 子类提供实现
     * 创建对应页面的presenter
     */
    abstract fun createPresenter(): P?
}