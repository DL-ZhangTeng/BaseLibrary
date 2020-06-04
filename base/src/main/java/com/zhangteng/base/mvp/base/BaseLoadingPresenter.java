package com.zhangteng.base.mvp.base;

public class BaseLoadingPresenter<V extends BaseLoadingView> extends BasePresenter<V> {
    V getBaseLoadingView() {
        if (isAttach()) {
            return view.get();
        } else {
            return null;
        }
    }
}
