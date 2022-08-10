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

    protected open fun showNoNetView(contentView: View?) {
        if (mStateViewHelper == null) {
            mStateViewHelper = StateViewHelper()
        }
        mStateViewHelper?.showNoNetView(contentView)
    }

    protected open fun showTimeOutView(contentView: View?) {
        if (mStateViewHelper == null) {
            mStateViewHelper = StateViewHelper()
        }
        mStateViewHelper?.showTimeOutView(contentView)
    }

    protected open fun showEmptyView(contentView: View?) {
        if (mStateViewHelper == null) {
            mStateViewHelper = StateViewHelper()
        }
        mStateViewHelper?.showEmptyView(contentView)
    }

    protected open fun showErrorView(contentView: View?) {
        if (mStateViewHelper == null) {
            mStateViewHelper = StateViewHelper()
        }
        mStateViewHelper?.showErrorView(contentView)
    }

    protected open fun showNoLoginView(contentView: View?) {
        if (mStateViewHelper == null) {
            mStateViewHelper = StateViewHelper()
        }
        mStateViewHelper?.showNoLoginView(contentView)
    }

    protected open fun showContentView(contentView: View?) {
        mStateViewHelper?.showContentView(contentView)
    }

    protected open fun showProgressDialog(mLoadingText: String? = "") {
        if (mStateViewHelper == null) {
            mStateViewHelper = StateViewHelper()
        }
        mStateViewHelper?.showProgressDialog(context, mLoadingText = mLoadingText)
    }

    protected open fun dismissProgressDialog() {
        mStateViewHelper?.dismissProgressDialog()
    }

    protected open fun showToast(message: String?) {
        context.showShortToast(message)
    }

    protected open fun showToast(messageId: Int) {
        context.showShortToast(messageId)
    }
}