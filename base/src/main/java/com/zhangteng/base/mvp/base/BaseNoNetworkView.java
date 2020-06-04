package com.zhangteng.base.mvp.base;

/**
 * 无网络/无数据
 * Created by swing on 2019/7/25 0025.
 */
public interface BaseNoNetworkView<T> extends BaseLoadingView<T> {
    void showNoNetwork();

    void hideNoNetwork();

    void showNoDataView();

    void hideNoDataView();
}
