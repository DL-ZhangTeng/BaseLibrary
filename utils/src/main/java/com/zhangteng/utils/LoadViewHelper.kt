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
 * 将某个视图替换为正在加载、无数据、加载失败等视图(保证每一个页面一个实例，不可单例使用会造成内存泄露或闪退)
 * Created by Swing on 2018/10/8.
 */
open class LoadViewHelper {
    protected open val contentViews: HashMap<View, NoDataView> = HashMap()
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
     * @param currentView 需要替换的view
     */
    open fun showNoNetView(currentView: View?) {
        showNoDataView(currentView, R.mipmap.wangluowu, "无网络", "点击重试")
    }

    /**
     * 无内容view
     *
     * @param currentView 需要替换的view
     */
    open fun showNoContentView(currentView: View?) {
        showNoDataView(currentView, R.mipmap.neirongwu, "暂无内容~", "")
    }

    /**
     * 显示无数据view
     *
     * @param currentView 需要替换的view
     */
    open fun showNoDataView(
        currentView: View?,
        drawableRes: Int,
        noDataText: String?,
        noDataAgainText: String?
    ) {
        if (currentView == null) return
        if (contentViews[currentView] == null) {
            contentViews[currentView] = NoDataView(currentView.context)
        }
        val mNoDataView = contentViews[currentView] ?: return
        mNoDataView.setNoDataImageResource(drawableRes)
        mNoDataView.setNoDataText(noDataText)
        if (null == noDataAgainText || "" == noDataAgainText) {
            mNoDataView.setNoDataAgainVisivility(View.GONE)
        } else {
            mNoDataView.setNoDataAgainText(noDataAgainText)
        }
        mNoDataView.setAgainRequestListener(object : NoDataView.AgainRequestListener {
            override fun request() {
                againRequestListener?.request()
            }
        })
        if (mNoDataView.isNoDataViewShow()) {
            return
        }
        val viewGroup = currentView.parent
        if (viewGroup != null) {
            viewGroup as ViewGroup
            viewGroup.removeView(currentView)
            viewGroup.addView(mNoDataView, currentView.layoutParams)
        }
        mNoDataView.setNoDataViewShow(true)
    }

    /**
     * 显示dialog
     *
     * @param mContext     dialog上下文
     * @param mLoadingText dialog文本
     */
    @JvmOverloads
    open fun showProgressDialog(mContext: Context?, mLoadingText: String? = "加载中...") {
        if (mContext == null) {
            return
        }
        showProgressDialog(
            mContext,
            R.drawable.loading1,
            mLoadingText
        )
    }

    /**
     * 显示dialog
     *
     * @param mContext     dialog上下文
     * @param mLoadingImage dialog动画
     * @param mLoadingText dialog文本
     */
    open fun showProgressDialog(
        mContext: Context?,
        mLoadingImage: Int,
        mLoadingText: String?
    ) {
        if (mContext == null) {
            return
        }
        showProgressDialog(
            mContext,
            mLoadingImage,
            mLoadingText,
            R.layout.layout_dialog_progress
        )
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
        mLoadingImage: Int,
        mLoadingText: String?,
        layoutRes: Int
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

    /**
     * 隐藏无网络view
     *
     * @param currentView 需要替换的view
     */
    open fun hiddenNoNetView(currentView: View?) {
        hiddenNoDataView(currentView)
    }

    /**
     * 隐藏无内容view
     *
     * @param currentView 需要替换的view
     */
    open fun hiddenNoContentView(currentView: View?) {
        hiddenNoDataView(currentView)
    }

    /**
     * 隐藏无数据view
     *
     * @param currentView 需要替换的view
     */
    open fun hiddenNoDataView(currentView: View?) {
        val mNoDataView = contentViews[currentView]
        if (mNoDataView?.isNoDataViewShow() == false) {
            return
        }
        val viewGroup = mNoDataView?.parent
        if (viewGroup != null) {
            viewGroup as ViewGroup
            viewGroup.removeView(mNoDataView)
            viewGroup.addView(currentView)
        }
        mNoDataView?.setNoDataViewShow(false)
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