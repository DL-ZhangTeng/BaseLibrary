package com.zhangteng.base.base

import com.zhangteng.base.mvp.base.IModel
import com.zhangteng.base.mvp.base.IPresenter
import com.zhangteng.base.mvp.base.IView

/**
 * 使用Mvp模式Activity基类
 * Created by swing on 2017/11/23.
 */
abstract class BaseListMvpActivity<V : IView, M : IModel, P : IPresenter<V, M>,
        D, A : BaseAdapter<D, BaseAdapter.DefaultViewHolder>> : BaseListActivity<D, A>() {

    protected var mPresenter: P? = null

    override fun initView() {
        super.initView()
        mPresenter = createPresenter()
        mPresenter?.attachView(this as V)
        mPresenter?.onStart()
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter?.detachView()
        mPresenter?.onDestroy()
        mPresenter = null
    }

    /**
     * 子类提供实现
     * 创建对应页面的presenter
     */
    abstract fun createPresenter(): P?
}