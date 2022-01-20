package com.zhangteng.mvp.utils

import com.zhangteng.mvp.base.BaseLoadingPresenter
import com.zhangteng.mvp.base.BaseLoadingView
import com.zhangteng.mvp.base.IModel
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method

/**
 * 使用代理的方式自动调用加载动画开启与关闭方法，同步执行方法时才有意义
 * Proxy.newProxyInstance(
 * MainPresenter::class.java.classLoader,
 * arrayOf(IMainPresenter::class.java),
 * LoadingPresenterHandler(MainPresenter())
 * ) as IMainPresenter
 */
class LoadingPresenterHandler<V : BaseLoadingView<*>, M : IModel, T : BaseLoadingPresenter<V, M>>(
    private val presenter: T?
) :
    InvocationHandler {
    @Throws(Throwable::class)
    override fun invoke(proxy: Any?, method: Method?, args: Array<Any?>?): Any? {
        if ("attachView" == method?.name ||
            "onStart" == method?.name ||
            "detachView" == method?.name ||
            "onDestroy" == method?.name ||
            "isAttach" == method?.name
        ) {
            return method.invoke(presenter, *args.orEmpty())
        }
        presenter?.getBaseLoadingView()?.showLoadingView()
        val result = method?.invoke(presenter, *args.orEmpty())
        presenter?.getBaseLoadingView()?.dismissLoadingView()
        return result
    }
}