package com.zhangteng.base.mvp.base;

import androidx.annotation.NonNull;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * 使用代理的方式自动调用加载动画开启与关闭方法
 * IPresenter mIPresenter = Proxy.newProxyInstance(
 * TestPresenter.class.getClassLoader(),
 * TestPresenter.class.getInterfaces(),
 * new PresenterProxy<>(mPresenter));
 */
public class PresenterProxy<V extends BaseLoadingView, T extends BaseLoadingPresenter<V>> implements InvocationHandler {
    @NonNull
    private T presenter;

    public PresenterProxy(@NonNull T presenter) {
        this.presenter = presenter;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        presenter.getBaseLoadingView().showLoadingView();
        Object object = method.invoke(presenter, args);
        presenter.getBaseLoadingView().dismissLoadingView();
        return object;
    }
}
