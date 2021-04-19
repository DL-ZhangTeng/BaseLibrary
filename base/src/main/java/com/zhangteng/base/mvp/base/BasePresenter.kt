package com.zhangteng.base.mvp.base

import java.lang.ref.WeakReference

open class BasePresenter<V : IView?> : IPresenter<V> {
    protected var mView: WeakReference<V?>? = null
    protected var mModel: IModel? = null

    override fun attachView(view: V?) {
        if (!isAttach()) {
            this.mView = WeakReference(view)
        }
    }

    override fun detachView() {
        if (isAttach()) {
            mView?.clear()
            mView = null
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mModel?.onDestroy()
        mModel = null
    }

    override fun isAttach(): Boolean {
        return mView != null &&
                mView!!.get() != null
    }
}