package com.zhangteng.utils

import android.app.Dialog
import android.content.Context
import android.view.View

/**
 * description: 支持gif动画
 * author: Swing
 * date: 2022/1/11
 */
open class GifStateViewHelper : StateViewHelper() {

    open fun showProgressDialog(
        mContext: Context?,
        mLoadingImage: Int = loadingImageGif,
        mLoadingText: String? = loadingTextGif
    ) {
        showProgressDialog(
            mContext,
            mLoadingImage,
            mLoadingText,
            loadingLayoutGif
        )
    }

    @Synchronized
    override fun showProgressDialog(
        mContext: Context?,
        mLoadingImage: Int,
        mLoadingText: String?,
        layoutRes: Int
    ) {
        if (mContext == null) {
            return
        }

        if (mProgressDialog == null) {
            mProgressDialog = Dialog(mContext, R.style.progress_dialog_gif)
            val view = View.inflate(mContext, layoutRes, null)
            mLoadTextView = view.findViewById(R.id.loadView)
            mLoadImageView = view.findViewById(R.id.progress_bar)
            mLoadImageView?.setImageResource(mLoadingImage)

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

    companion object {
        //加载中图标
        var loadingImageGif: Int = R.drawable.loading_gif1

        //加载中文本
        var loadingTextGif: String = ""

        //加载中布局
        var loadingLayoutGif: Int = R.layout.layout_dialog_progress_gif
    }
}