package com.zhangteng.base.mvp.base;

/**
 * model到presenter的回调
 * Created by swing on 2019/7/2 0002.
 */
public abstract class BaseHttpEntity<T> {
    private BaseView baseView;

    public BaseHttpEntity() {
    }

    public BaseHttpEntity(BaseView baseView) {
        this.baseView = baseView;
    }

    public abstract void onSuccess(T data);

    /**
     * 无网络
     */
    public void onNoNetworkError() {
        if (baseView != null && baseView instanceof BaseNoNetworkView)
            ((BaseNoNetworkView) baseView).showNoNetwork();
    }

    /**
     * 业务异常处理
     */
    public void onError(int code, String error) {

    }

    /**
     * http异常处理
     */
    public void onHttpError(int code, String error) {

    }

    /**
     * 请求完成
     */
    public void onFinish() {
        if (baseView != null && baseView instanceof BaseNoNetworkView)
            ((BaseNoNetworkView) baseView).hideNoNetwork();
        if (baseView != null && baseView instanceof BaseRefreshView)
            ((BaseRefreshView) baseView).finishRefreshOrLoadMore();
    }
}