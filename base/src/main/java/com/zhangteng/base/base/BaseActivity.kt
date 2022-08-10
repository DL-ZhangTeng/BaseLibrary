package com.zhangteng.base.base

import android.content.ActivityNotFoundException
import android.content.Intent
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.zhangteng.utils.StateViewHelper
import com.zhangteng.utils.showShortToast

/**
 * Created by swing on 2017/11/23.
 */
abstract class BaseActivity : AppCompatActivity() {

    protected var mStateViewHelper: StateViewHelper? = null

    override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)
        initView()
        initData()
    }

    override fun setContentView(view: View?) {
        super.setContentView(view)
        initView()
        initData()
    }

    override fun setContentView(view: View?, params: ViewGroup.LayoutParams?) {
        super.setContentView(view, params)
        initView()
        initData()
    }

    protected abstract fun initView()
    protected abstract fun initData()

    protected open fun showProgressDialog() {
        if (mStateViewHelper == null) {
            mStateViewHelper = StateViewHelper()
        }
        mStateViewHelper?.showProgressDialog(this, mLoadingText = "")
    }

    protected open fun showProgressDialog(mLoadingText: String?) {
        if (mStateViewHelper == null) {
            mStateViewHelper = StateViewHelper()
        }
        mStateViewHelper?.showProgressDialog(this, mLoadingText = mLoadingText)
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
        showShortToast(message)
    }

    protected open fun showToast(messageId: Int) {
        showShortToast(messageId)
    }

    override fun startActivity(intent: Intent?) {
        try {
            super.startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            showToast("未找到相应应用")
        }
    }

    override fun startActivityForResult(intent: Intent?, requestCode: Int) {
        try {
            super.startActivityForResult(intent, requestCode)
        } catch (e: ActivityNotFoundException) {
            showToast("未找到相应应用")
        }
    }
}