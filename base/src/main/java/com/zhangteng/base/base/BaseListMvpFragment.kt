package com.zhangteng.base.base

import android.os.Bundle
import android.view.View
import com.zhangteng.base.mvp.base.IModel
import com.zhangteng.base.mvp.base.IPresenter
import com.zhangteng.base.mvp.base.IView

/**
 * 使用Mvp模式Fragment基类
 * Created by swing on 2021/7/3.
 */
abstract class BaseListMvpFragment<V : IView, M : IModel, P : IPresenter<V, M>,
        D, A : BaseAdapter<D, BaseAdapter.DefaultViewHolder>> : BaseListFragment<D, A>() {

    protected var mPresenter: P? = null

    override fun initView(view: View, savedInstanceState: Bundle?) {
        super.initView(view, savedInstanceState)
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