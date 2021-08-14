package com.zhangteng.base.base

import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import com.zhangteng.base.utils.LoadViewHelper
import com.zhangteng.base.utils.ToastUtils

/**
 * Created by swing on 2017/11/30.
 */
abstract class BaseDialogFragment : DialogFragment() {

    protected var mLoadViewHelper: LoadViewHelper? = null

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
        if (mLoadViewHelper == null) {
            mLoadViewHelper = LoadViewHelper()
        }
        mLoadViewHelper?.showProgressDialog(context, "")
    }

    protected open fun showProgressDialog(mLoadingText: String?) {
        if (mLoadViewHelper == null) {
            mLoadViewHelper = LoadViewHelper()
        }
        mLoadViewHelper?.showProgressDialog(context, mLoadingText)
    }

    protected open fun dismissProgressDialog() {
        mLoadViewHelper?.dismissProgressDialog()
    }

    protected open fun showNoNetView(currentView: View?) {
        if (mLoadViewHelper == null) {
            mLoadViewHelper = LoadViewHelper()
        }
        mLoadViewHelper?.showNoNetView(currentView)
    }

    protected open fun hiddenNoNetView(currentView: View?) {
        mLoadViewHelper?.hiddenNoNetView(currentView)
    }

    protected open fun showNoContentView(currentView: View?) {
        if (mLoadViewHelper == null) {
            mLoadViewHelper = LoadViewHelper()
        }
        mLoadViewHelper?.showNoContentView(currentView)
    }

    protected open fun hiddenNoContentView(currentView: View?) {
        mLoadViewHelper?.hiddenNoContentView(currentView)
    }

    protected open fun showToast(message: String?) {
        ToastUtils.showShort(context, message)
    }

    protected open fun showToast(messageId: Int) {
        ToastUtils.showShort(context, messageId)
    }
}