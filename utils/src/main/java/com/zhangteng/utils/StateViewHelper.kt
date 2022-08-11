package com.zhangteng.utils

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.ContextWrapper
import android.content.DialogInterface
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
    /**
     * description 状态View集合
     */
    protected open val contentViews: HashMap<View, StateView> = HashMap()

    /**
     * description: 状态View重试回调
     */
    open var againRequestListener: AgainRequestListener? = null

    /**
     * description: 加载中弹窗
     */
    protected open var mProgressDialog: Dialog? = null
    protected open var mLoadTextView: TextView? = null
    protected open var mAnimation: Animation? = null
    protected open var mLoadImageView: ImageView? = null

    /**
     * description: 解决连续加载中弹窗问题
     */
    @Volatile
    protected open var showCount: Int = 0

    /**
     * description: 加载中取消回调
     */
    open var cancelRequestListener: CancelRequestListener? = null

    /**
     * 无网络view
     *
     * @param contentView 需要替换的view
     */
    open fun showNoNetView(contentView: View?) {
        showStateView(contentView, noNetIcon, noNetText, noNetAgainText)
    }

    /**
     * 超时view
     *
     * @param contentView 需要替换的view
     */
    open fun showTimeOutView(contentView: View?) {
        showStateView(contentView, timeOutIcon, timeOutText, timeOutAgainText)
    }

    /**
     * 无内容view
     *
     * @param contentView 需要替换的view
     */
    open fun showEmptyView(contentView: View?) {
        showStateView(contentView, emptyIcon, emptyText, emptyAgainText)
    }

    /**
     * 数据错误view
     *
     * @param contentView 需要替换的view
     */
    open fun showErrorView(contentView: View?) {
        showStateView(contentView, unknownIcon, unknownText, unknownAgainText)
    }

    /**
     * 未登录view
     *
     * @param contentView 需要替换的view
     */
    open fun showNoLoginView(contentView: View?) {
        showStateView(contentView, noLoginIcon, noLoginText, noLoginAgainText)
    }

    /**
     * 显示无数据view
     *
     * @param contentView 需要替换的view
     */
    open fun showStateView(
        contentView: View?,
        drawableRes: Int = defaultIcon,
        stateText: String? = defaultText,
        stateAgainText: String? = defaultAgainText
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
            override fun request(view: View) {
                againRequestListener?.request(view)
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
        mLoadingImage: Int = loadingImage,
        mLoadingText: String? = loadingText,
        layoutRes: Int = loadingLayout
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
                cancelRequestListener?.cancel(it)
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
        fun cancel(dialog: DialogInterface)
    }

    interface AgainRequestListener {
        fun request(view: View)
    }

    companion object {
        //状态View图标
        var defaultIcon: Int = R.mipmap.icon_default
        var noNetIcon: Int = R.mipmap.icon_default_nonet
        var timeOutIcon: Int = R.mipmap.icon_default_timeout
        var emptyIcon: Int = R.mipmap.icon_default_empty
        var unknownIcon: Int = R.mipmap.icon_default_unknown
        var noLoginIcon: Int = R.mipmap.icon_default_nologin

        //状态View提示文本
        var defaultText: String = ""
        var noNetText: String = "无网络"
        var timeOutText: String = "请求超时"
        var emptyText: String = "暂无内容~"
        var unknownText: String = "数据错误"
        var noLoginText: String = "未登录"

        //状态View重试文本
        var defaultAgainText: String = ""
        var noNetAgainText: String = "点击重试"
        var timeOutAgainText: String = "点击重试"
        var emptyAgainText: String = ""
        var unknownAgainText: String = ""
        var noLoginAgainText: String = "去登录"

        //加载中图标
        var loadingImage: Int = R.drawable.loading1

        //加载中文本
        var loadingText: String = "加载中..."

        //加载中布局
        var loadingLayout: Int = R.layout.layout_dialog_progress
    }
}