package com.zhangteng.base.base

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.zhangteng.base.utils.LoadViewHelper
import com.zhangteng.base.utils.ToastUtils

/**
 * 1.viewpager+fragment时空白页面bug：设置setOffscreenPageLimit为tab数  或  onCreateView生成view前移除上一次已添加的view；
 * 2.fragment长时间后台显示时重叠bug，重写activity的onSaveInstanceState与onRestoreInstanceState避免activity重建时重新new fragment；
 * 3.fragment使用show/hide不影响生命周期可使用onHiddenChanged监听状态；viewpager中可使用getUserVisibleHint获取状态
 * Created by swing on 2017/11/23.
 */
abstract class BaseFragment : Fragment() {
    protected var mLoadViewHelper: LoadViewHelper? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initData(savedInstanceState)
    }

    protected abstract fun initView(view: View, savedInstanceState: Bundle?)
    protected open fun initData(savedInstanceState: Bundle?) {}

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