package com.zhangteng.base.base

import android.content.ActivityNotFoundException
import android.content.Intent
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.zhangteng.base.utils.LoadViewHelper
import com.zhangteng.base.utils.ToastUtils

/**
 * Created by swing on 2017/11/23.
 */
abstract class BaseActivity : AppCompatActivity() {

    protected var mLoadViewHelper: LoadViewHelper? = null

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
        if (mLoadViewHelper == null) {
            mLoadViewHelper = LoadViewHelper()
        }
        mLoadViewHelper?.showProgressDialog(this, "")
    }

    protected open fun showProgressDialog(mLoadingText: String?) {
        if (mLoadViewHelper == null) {
            mLoadViewHelper = LoadViewHelper()
        }
        mLoadViewHelper?.showProgressDialog(this, mLoadingText)
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
        ToastUtils.showShort(this, message)
    }

    protected open fun showToast(messageId: Int) {
        ToastUtils.showShort(this, messageId)
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