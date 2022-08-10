package com.zhangteng.utils

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.ContextWrapper
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import android.widget.TextView

/**
 * 将某个视图替换为正在加载、无网络、超时、无数据、数据错误、未登录等视图(保证每一个页面一个实例，不可单例使用会造成内存泄露或闪退)
 * Created by Swing on 2018/10/8.
 */
open class StateViewHelper {
    protected open val contentViews: HashMap<View, StateView> = HashMap()
    protected open var mProgressDialog: Dialog? = null
    protected open var mLoadTextView: TextView? = null
    protected open var mAnimation: Animation? = null
    protected open var mLoadImageView: ImageView? = null
    open var againRequestListener: AgainRequestListener? = null
    open var cancelRequestListener: CancelRequestListener? = null

    /**
     * @description: 解决连续进度弹窗问题
     */
    @Volatile
    protected open var showCount: Int = 0

    /**
     * 无网络view
     *
     * @param contentView 需要替换的view
     */
    open fun showNoNetView(contentView: View?) {
        showStateView(contentView, R.mipmap.icon_default_nonet, "无网络", "点击重试")
    }

    /**
     * 超时view
     *
     * @param contentView 需要替换的view
     */
    open fun showTimeOutView(contentView: View?) {
        showStateView(contentView, R.mipmap.icon_default_timeout, "请求超时", "点击重试")
    }

    /**
     * 无内容view
     *
     * @param contentView 需要替换的view
     */
    open fun showEmptyView(contentView: View?) {
        showStateView(contentView, R.mipmap.icon_default_empty, "暂无内容~")
    }

    /**
     * 数据错误view
     *
     * @param contentView 需要替换的view
     */
    open fun showErrorView(contentView: View?) {
        showStateView(contentView, R.mipmap.icon_default_unknown, "数据错误")
    }

    /**
     * 未登录view
     *
     * @param contentView 需要替换的view
     */
    open fun showNoLoginView(contentView: View?) {
        showStateView(contentView, R.mipmap.icon_default_nologin, "未登录", "去登录")
    }

    /**
     * 显示无数据view
     *
     * @param contentView 需要替换的view
     */
    open fun showStateView(
        contentView: View?,
        drawableRes: Int = R.mipmap.icon_default,
        stateText: String? = "",
        stateAgainText: String? = ""
    ) {
        if (contentView == null) return
        if (contentViews[contentView] == null) {
            contentViews[contentView] = StateView(contentView.context)
        }
        val mStateView = contentViews[contentView] ?: return
        mStateView.setStateImageResource(drawableRes)
        mStateView.setStateText(stateText)
        if (null == stateAgainText || "" == stateAgainText) {
            mStateView.setStateAgainVisibility(View.GONE)
        } else {
            mStateView.setStateAgainText(stateAgainText)
        }
        mStateView.setAgainRequestListener(object : StateView.AgainRequestListener {
            override fun request() {
                againRequestListener?.request()
            }
        })
        if (mStateView.isStateViewShow()) {
            return
        }
        val viewGroup = contentView.parent
        if (viewGroup != null) {
            viewGroup as ViewGroup
            viewGroup.removeView(contentView)
            viewGroup.addView(mStateView, contentView.layoutParams)
        }
        mStateView.setStateViewShow(true)
    }

    /**
     * 显示数据view
     *
     * @param contentView 需要替换的view
     */
    open fun showContentView(contentView: View?) {
        val mStateView = contentViews[contentView]
        if (mStateView?.isStateViewShow() == false) {
            return
        }
        val viewGroup = mStateView?.parent
        if (viewGroup != null) {
            viewGroup as ViewGroup
            viewGroup.removeView(mStateView)
            viewGroup.addView(contentView)
        }
        mStateView?.setStateViewShow(false)
    }

    /**
     * 显示dialog
     *
     * @param mContext     dialog上下文
     * @param mLoadingImage dialog动画
     * @param mLoadingText dialog文本
     * @param layoutRes    dialog布局文件
     */
    @Synchronized
    open fun showProgressDialog(
        mContext: Context?,
        mLoadingImage: Int = R.drawable.loading1,
        mLoadingText: String? = "加载中...",
        layoutRes: Int = R.layout.layout_dialog_progress
    ) {
        if (mContext == null) {
            return
        }

        if (mProgressDialog == null) {
            mProgressDialog = Dialog(mContext, R.style.progress_dialog)
            val view = View.inflate(mContext, layoutRes, null)
            mLoadTextView = view.findViewById(R.id.loadView)
            mLoadImageView = view.findViewById(R.id.progress_bar)
            mLoadImageView?.setImageResource(mLoadingImage)
            mAnimation = AnimationUtils.loadAnimation(mContext, R.anim.loadings)
                .apply { interpolator = LinearInterpolator() }
            mLoadImageView?.startAnimation(mAnimation)

            if (mLoadingText != null) {
                mLoadTextView?.text = mLoadingText
            }
            mProgressDialog?.setContentView(view)
            mProgressDialog?.setCancelable(true)
            mProgressDialog?.setCanceledOnTouchOutside(false)
            mProgressDialog?.setOnDismissListener {
                cancelRequestListener?.cancel()
            }
            val activity = findActivity(mContext)
            if (activity == null || activity.isDestroyed || activity.isFinishing) {
                if (mProgressDialog?.isShowing == true)
                    mProgressDialog?.dismiss()
                mProgressDialog = null
                return
            } else {
                if (mProgressDialog?.ownerActivity == null)
                    mProgressDialog?.setOwnerActivity(activity)
            }
        } else {
            if (mLoadingText != null && mLoadTextView != null) {
                mLoadTextView?.text = mLoadingText
            }
            if (mLoadImageView != null) {
                if (mAnimation == null) {
                    mAnimation = AnimationUtils.loadAnimation(mContext, R.anim.loadings)
                        .apply { interpolator = LinearInterpolator() }
                }
                mLoadImageView?.startAnimation(mAnimation)
            }
        }
        val activity1 = mProgressDialog?.ownerActivity
        if (activity1 == null || activity1.isDestroyed || activity1.isFinishing) {
            if (mProgressDialog?.isShowing == true)
                mProgressDialog?.dismiss()
            mProgressDialog = null
            return
        }
        showCount++
        if (mProgressDialog?.isShowing == false)
            mProgressDialog?.show()
    }

    /**
     * 完成dialog
     */
    @Synchronized
    open fun dismissProgressDialog() {
        showCount--
        if (mProgressDialog?.isShowing == true && showCount <= 0) {
            showCount = 0
            mLoadImageView?.clearAnimation()
            mProgressDialog?.dismiss()
        }
    }

    fun findActivity(context: Context?): Activity? {
        if (context is Activity) {
            return context
        }
        return if (context is ContextWrapper) {
            val wrapper = context as ContextWrapper?
            findActivity(wrapper?.baseContext)
        } else {
            null
        }
    }

    interface CancelRequestListener {
        fun cancel()
    }

    interface AgainRequestListener {
        fun request()
    }
}