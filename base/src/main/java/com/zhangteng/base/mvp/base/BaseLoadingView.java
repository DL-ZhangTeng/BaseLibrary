package com.zhangteng.base.mvp.base;

/**
 * Created by swing on 2019/7/16 0016.
 */
public interface BaseLoadingView<T> extends BaseView {
    void showLoadingView();

    void dismissLoadingView();

    void inflateView(T data);
}
