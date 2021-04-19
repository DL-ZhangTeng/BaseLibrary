package com.zhangteng.base.mvp.base

import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method
import kotlin.jvm.Throws

/**
 * 使用代理的方式自动调用加载动画开启与关闭方法
 * IPresenter mIPresenter = Proxy.newProxyInstance(
 * TestPresenter.class.getClassLoader(),
 * TestPresenter.class.getInterfaces(),
 * new LoadingPresenterProxy<>(mPresenter));
 */
class LoadingPresenterHandler<V : BaseLoadingView<*>?, T : BaseLoadingPresenter<V?>?>(private val presenter: T) : InvocationHandler {
    @Throws(Throwable::class)
    override fun invoke(proxy: Any?, method: Method?, args: Array<Any?>?): Any? {
        if ("attachView" == method?.name ||
                "onStart" == method?.name ||
                "detachView" == method?.name ||
                "onDestroy" == method?.name ||
                "isAttach" == method?.name) {
            return method.invoke(presenter, *args.orEmpty())
        }
        presenter?.getBaseLoadingView()?.showLoadingView()
        val result = method?.invoke(presenter, *args.orEmpty())
        presenter?.getBaseLoadingView()?.dismissLoadingView()
        return result
    }
}