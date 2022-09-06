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

    protected val mStateViewHelper by lazy { createStateViewHelper() }

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

    /**
     * description 创建 StateViewHelper类，并回调重试请求、取消请求监听
     */
    protected open fun createStateViewHelper(): StateViewHelper {
        return StateViewHelper().apply {
            againRequestListener = object : StateViewHelper.AgainRequestListener {
                override fun request(view: View) {
                    againRequestByStateViewHelper(view)
                }
            }
            cancelRequestListener = object : StateViewHelper.CancelRequestListener {
                override fun cancel(dialog: DialogInterface) {
                    cancelRequestByStateViewHelper(dialog)
                }
            }
        }
    }

    /**
     * description 无网络视图
     * @param contentView 被替换的View
     */
    protected open fun showNoNetView(contentView: View?) {
        mStateViewHelper.showNoNetView(contentView)
    }

    /**
     * description 超时视图
     * @param contentView 被替换的View
     */
    protected open fun showTimeOutView(contentView: View?) {
        mStateViewHelper.showTimeOutView(contentView)
    }

    /**
     * description 无数据视图
     * @param contentView 被替换的View
     */
    protected open fun showEmptyView(contentView: View?) {
        mStateViewHelper.showEmptyView(contentView)
    }

    /**
     * description 错误视图
     * @param contentView 被替换的View
     */
    protected open fun showErrorView(contentView: View?) {
        mStateViewHelper.showErrorView(contentView)
    }

    /**
     * description 未登录视图
     * @param contentView 被替换的View
     */
    protected open fun showNoLoginView(contentView: View?) {
        mStateViewHelper.showNoLoginView(contentView)
    }

    /**
     * description 业务视图
     * @param contentView 要展示的View
     */
    protected open fun showContentView(contentView: View?) {
        mStateViewHelper.showContentView(contentView)
    }

    /**
     * description 加载中弹窗
     * @param mLoadingText 加载中...
     */
    protected open fun showProgressDialog(mLoadingText: String? = StateViewHelper.loadingText) {
        mStateViewHelper.showProgressDialog(context, mLoadingText = mLoadingText)
    }

    /**
     * description 关闭加载中弹窗
     */
    protected open fun dismissProgressDialog() {
        mStateViewHelper.dismissProgressDialog()
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
    protected open fun cancelRequestByStateViewHelper(dialog: DialogInterface) {

    }

    protected open fun showToast(message: String?) {
        context.showShortToast(message)
    }

    protected open fun showToast(messageId: Int) {
        context.showShortToast(messageId)
    }
}