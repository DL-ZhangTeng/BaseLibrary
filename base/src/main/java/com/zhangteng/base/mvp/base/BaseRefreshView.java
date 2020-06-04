package com.zhangteng.base.mvp.base;

/**
 * 无网络/无数据
 * Created by swing on 2019/7/25 0025.
 */
public interface BaseRefreshView<T> extends BaseNoNetworkView<T> {
    void finishRefreshOrLoadMore();
}
