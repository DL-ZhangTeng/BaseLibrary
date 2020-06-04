package com.zhangteng.base.mvp.base;

import java.lang.ref.WeakReference;

public class BasePresenter<V extends BaseView> {
    protected WeakReference<V> view;

    public void attachView(V view) {
        if (!isAttach()) {
            this.view = new WeakReference<>(view);
        }
    }

    public void detachView() {
        if (isAttach()) {
            view.clear();
            view = null;
        }
    }

    boolean isAttach() {
        return view != null &&
                view.get() != null;
    }
}
