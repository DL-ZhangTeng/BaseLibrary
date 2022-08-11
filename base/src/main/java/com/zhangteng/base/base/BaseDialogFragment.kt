package com.zhangteng.base.base

import android.content.DialogInterface
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
            mStateViewHelper = StateViewHelper().apply {
                againRequestListener = object : StateViewHelper.AgainRequestListener {
                    override fun request(view: View) {
                        againRequestByStateViewHelper(view)
                    }
                }
                cancelRequestListener = object : StateViewHelper.CancelRequestListener {
                    override fun cancel(dialog: DialogInterface) {
                        cancelByStateViewHelper(dialog)
                    }
                }
            }
        }
        mStateViewHelper?.showNoNetView(contentView)
    }

    protected open fun showTimeOutView(contentView: View?) {
        if (mStateViewHelper == null) {
            mStateViewHelper = StateViewHelper().apply {
                againRequestListener = object : StateViewHelper.AgainRequestListener {
                    override fun request(view: View) {
                        againRequestByStateViewHelper(view)
                    }
                }
                cancelRequestListener = object : StateViewHelper.CancelRequestListener {
                    override fun cancel(dialog: DialogInterface) {
                        cancelByStateViewHelper(dialog)
                    }
                }
            }
        }
        mStateViewHelper?.showTimeOutView(contentView)
    }

    protected open fun showEmptyView(contentView: View?) {
        if (mStateViewHelper == null) {
            mStateViewHelper = StateViewHelper().apply {
                againRequestListener = object : StateViewHelper.AgainRequestListener {
                    override fun request(view: View) {
                        againRequestByStateViewHelper(view)
                    }
                }
                cancelRequestListener = object : StateViewHelper.CancelRequestListener {
                    override fun cancel(dialog: DialogInterface) {
                        cancelByStateViewHelper(dialog)
                    }
                }
            }
        }
        mStateViewHelper?.showEmptyView(contentView)
    }

    protected open fun showErrorView(contentView: View?) {
        if (mStateViewHelper == null) {
            mStateViewHelper = StateViewHelper().apply {
                againRequestListener = object : StateViewHelper.AgainRequestListener {
                    override fun request(view: View) {
                        againRequestByStateViewHelper(view)
                    }
                }
                cancelRequestListener = object : StateViewHelper.CancelRequestListener {
                    override fun cancel(dialog: DialogInterface) {
                        cancelByStateViewHelper(dialog)
                    }
                }
            }
        }
        mStateViewHelper?.showErrorView(contentView)
    }

    protected open fun showNoLoginView(contentView: View?) {
        if (mStateViewHelper == null) {
            mStateViewHelper = StateViewHelper().apply {
                againRequestListener = object : StateViewHelper.AgainRequestListener {
                    override fun request(view: View) {
                        againRequestByStateViewHelper(view)
                    }
                }
                cancelRequestListener = object : StateViewHelper.CancelRequestListener {
                    override fun cancel(dialog: DialogInterface) {
                        cancelByStateViewHelper(dialog)
                    }
                }
            }
        }
        mStateViewHelper?.showNoLoginView(contentView)
    }

    protected open fun showContentView(contentView: View?) {
        mStateViewHelper?.showContentView(contentView)
    }

    protected open fun showProgressDialog(mLoadingText: String? = StateViewHelper.loadingText) {
        if (mStateViewHelper == null) {
            mStateViewHelper = StateViewHelper().apply {
                againRequestListener = object : StateViewHelper.AgainRequestListener {
                    override fun request(view: View) {
                        againRequestByStateViewHelper(view)
                    }
                }
                cancelRequestListener = object : StateViewHelper.CancelRequestListener {
                    override fun cancel(dialog: DialogInterface) {
                        cancelByStateViewHelper(dialog)
                    }
                }
            }
        }
        mStateViewHelper?.showProgressDialog(context, mLoadingText = mLoadingText)
    }

    protected open fun dismissProgressDialog() {
        mStateViewHelper?.dismissProgressDialog()
    }

    /**
     * description 状态View重新请求回调
     * @param view 重试按钮
     */
    protected open fun againRequestByStateViewHelper(view: View) {

    }

    /**
     * description 加载中取消回调
     * @param dialog 加载中弹窗
     */
    protected open fun cancelByStateViewHelper(dialog: DialogInterface) {

    }

    protected open fun showToast(message: String?) {
        context.showShortToast(message)
    }

    protected open fun showToast(messageId: Int) {
        context.showShortToast(messageId)
    }
}