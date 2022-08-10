package com.zhangteng.base.base

import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import com.zhangteng.utils.StateViewHelper
import com.zhangteng.utils.showShortToast

/**
 * Created by swing on 2017/11/30.
 */
abstract class BaseDialogFragment : DialogFragment() {

    protected var mStateViewHelper: StateViewHelper? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initData()
    }

    protected abstract fun initView(view: View?)
    protected open fun initData() {}

    protected open fun showProgressDialog() {
        if (mStateViewHelper == null) {
            mStateViewHelper = StateViewHelper()
        }
        mStateViewHelper?.showProgressDialog(context, mLoadingText = "")
    }

    protected open fun showProgressDialog(mLoadingText: String?) {
        if (mStateViewHelper == null) {
            mStateViewHelper = StateViewHelper()
        }
        mStateViewHelper?.showProgressDialog(context, mLoadingText = mLoadingText)
    }

    protected open fun dismissProgressDialog() {
        mStateViewHelper?.dismissProgressDialog()
    }

    protected open fun showNoNetView(currentView: View?) {
        if (mStateViewHelper == null) {
            mStateViewHelper = StateViewHelper()
        }
        mStateViewHelper?.showNoNetView(currentView)
    }

    protected open fun showTimeOutView(currentView: View?) {
        if (mStateViewHelper == null) {
            mStateViewHelper = StateViewHelper()
        }
        mStateViewHelper?.showTimeOutView(currentView)
    }

    protected open fun showEmptyView(currentView: View?) {
        if (mStateViewHelper == null) {
            mStateViewHelper = StateViewHelper()
        }
        mStateViewHelper?.showEmptyView(currentView)
    }

    protected open fun showErrorView(currentView: View?) {
        if (mStateViewHelper == null) {
            mStateViewHelper = StateViewHelper()
        }
        mStateViewHelper?.showErrorView(currentView)
    }

    protected open fun showNoLoginView(currentView: View?) {
        if (mStateViewHelper == null) {
            mStateViewHelper = StateViewHelper()
        }
        mStateViewHelper?.showNoLoginView(currentView)
    }

    protected open fun hiddenNoNetView(currentView: View?) {
        mStateViewHelper?.hiddenNoNetView(currentView)
    }

    protected open fun hiddenTimeOutView(currentView: View?) {
        mStateViewHelper?.hiddenTimeOutView(currentView)
    }

    protected open fun hiddenEmptyView(currentView: View?) {
        mStateViewHelper?.hiddenEmptyView(currentView)
    }

    protected open fun hiddenErrorView(currentView: View?) {
        mStateViewHelper?.hiddenErrorView(currentView)
    }

    protected open fun hiddenNoLoginView(currentView: View?) {
        mStateViewHelper?.hiddenNoLoginView(currentView)
    }

    protected open fun showToast(message: String?) {
        context.showShortToast(message)
    }

    protected open fun showToast(messageId: Int) {
        context.showShortToast(messageId)
    }
}