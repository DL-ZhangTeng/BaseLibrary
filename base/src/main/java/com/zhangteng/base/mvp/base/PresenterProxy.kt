package com.zhangteng.base.mvp.base

import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method

/**
 * 使用代理的方式自动调用加载动画开启与关闭方法
 * IPresenter mIPresenter = Proxy.newProxyInstance(
 * TestPresenter.class.getClassLoader(),
 * TestPresenter.class.getInterfaces(),
 * new PresenterProxy<>(mPresenter));
 */
class PresenterProxy<V : BaseLoadingView<*>?, T : BaseLoadingPresenter<V?>?>(private val presenter: T) : InvocationHandler {
    @Throws(Throwable::class)
    override fun invoke(proxy: Any?, method: Method?, args: Array<Any?>?): Any? {
        presenter?.getBaseLoadingView()?.showLoadingView()
        val result = method?.invoke(presenter, args)
        presenter?.getBaseLoadingView()?.dismissLoadingView()
        return result
    }
}