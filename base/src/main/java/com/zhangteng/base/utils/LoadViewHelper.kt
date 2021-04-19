package com.zhangteng.base.utils

import android.app.Activity
import android.app.Dialog
import android.content.*
import android.graphics.drawable.AnimationDrawable
import android.util.SparseArray
import android.view.*
import android.widget.*
import com.zhangteng.base.R
import com.zhangteng.base.widget.NoDataView
import java.util.*

/**
 * 将某个视图替换为正在加载、无数据、加载失败等视图
 * Created by swing on 2018/10/8.
 */
open class LoadViewHelper {
    private val contentViews: SparseArray<View?>?
    private val noDataViews: SparseArray<NoDataView?>?
    private var mProgressDialog: Dialog? = null
    private var loadView: TextView? = null
    private var againRequestListener: AgainRequestListener? = null
    private var cancelRequestListener: CancelRequestListener? = null
    private val showQueue: ArrayDeque<Dialog?>?

    /**
     * 网络无数据view
     *
     * @param currentView 需要替换的view
     */
    fun showNetNodataView(currentView: View?) {
        showNodataView(NETWORKNO, currentView, R.mipmap.wangluowu, "无网络", "点击重试")
    }

    /**
     * 内容无数据view
     *
     * @param currentView 需要替换的view
     */
    fun showContentNodataView(currentView: View?) {
        showNodataView(CONTENTNODATA, currentView, R.mipmap.neirongwu, "暂无内容~", "")
    }

    /**
     * 显示无数据view
     *
     * @param currentView 需要替换的view
     */
    fun showNodataView(type: Int, currentView: View?, drawableRes: Int, nodataText: String?, nodataAgainText: String?) {
        if (contentViews == null || noDataViews == null) return
        if (contentViews.get(type, null) == null) {
            contentViews.put(type, currentView)
        }
        if (noDataViews.get(type, null) == null) {
            noDataViews.put(type, contentViews.get(type)?.context?.let { NoDataView(it) })
        }
        val mNoDataViews = noDataViews.get(type)
        val mContentViews = contentViews.get(type)
        if (mNoDataViews == null || mContentViews == null) return
        mNoDataViews.setNoDataImageResource(drawableRes)
        mNoDataViews.setNoDataText(nodataText)
        if (null == nodataAgainText || "" == nodataAgainText) {
            mNoDataViews.setNoDataAgainVisivility(View.GONE)
        } else {
            mNoDataViews.setNoDataAgainText(nodataAgainText)
        }
        mNoDataViews.setAgainRequestListener(object : NoDataView.AgainRequestListener {
            override fun request() {
                againRequestListener?.request()
            }
        })
        if (mNoDataViews.isNoDataViewShow()) {
            return
        }
        val viewGroup = mContentViews.parent
        if (viewGroup != null) {
            viewGroup as ViewGroup
            viewGroup.removeView(mContentViews)
            viewGroup.addView(mNoDataViews, mContentViews.layoutParams)
        }
        mNoDataViews.setNoDataViewShow(true)
    }
    /**
     * 显示dialog
     *
     * @param mContext     dialog上下文
     * @param mLoadingText dialog文本
     */
    /**
     * 显示dialog
     *
     * @param mContext dialog上下文
     */
    @JvmOverloads
    fun showProgressDialog(mContext: Context?, mLoadingText: String? = "加载中...") {
        if (mContext == null) {
            return
        }
        showProgressDialog(mContext, mLoadingText, R.layout.layout_dialog_progress)
    }

    /**
     * 显示dialog
     *
     * @param mContext     dialog上下文
     * @param mLoadingText dialog文本
     * @param layoutRes    dialog布局文件
     */
    fun showProgressDialog(mContext: Context?, mLoadingText: String?, layoutRes: Int) {
        if (mContext == null) {
            return
        }
        if (mProgressDialog == null) {
            mProgressDialog = Dialog(mContext, R.style.progress_dialog)
            val view = View.inflate(mContext, layoutRes, null)
            loadView = view.findViewById(R.id.loadView)
            val mImageView = view.findViewById<ImageView?>(R.id.progress_bar)
            (mImageView?.drawable as AnimationDrawable).start()
            if (mLoadingText != null) {
                loadView?.text = mLoadingText
            }
            mProgressDialog!!.setContentView(view)
            mProgressDialog!!.setCancelable(true)
            mProgressDialog!!.setCanceledOnTouchOutside(false)
            mProgressDialog!!.setOnDismissListener {
                if (showQueue != null && !showQueue.isEmpty()) {
                    showQueue.remove(mProgressDialog)
                }
                cancelRequestListener?.cancel()
            }
            val activity = findActivity(mContext)
            if (activity == null || activity.isDestroyed || activity.isFinishing) {
                mProgressDialog = null
                return
            } else {
                if (mProgressDialog!!.ownerActivity == null) mProgressDialog!!.setOwnerActivity(activity)
            }
            if (showQueue != null && !showQueue.isEmpty())
                showQueue.add(mProgressDialog)
        } else if (!mProgressDialog!!.isShowing) {
            if (mLoadingText != null && loadView != null) {
                loadView?.text = mLoadingText
            }
            if (showQueue != null && !showQueue.isEmpty() && !showQueue.contains(mProgressDialog)) {
                val activity = findActivity(mContext)
                if (activity == null || activity.isDestroyed || activity.isFinishing) {
                    mProgressDialog = null
                    return
                } else {
                    if (mProgressDialog!!.ownerActivity == null) mProgressDialog!!.setOwnerActivity(activity)
                }
                showQueue.add(mProgressDialog)
            }
        }
        alwaysShowProgressDialog()
    }

    /**
     * 完成dialog
     */
    fun dismissProgressDialog() {
        if (showQueue != null && !showQueue.isEmpty()) {
            val first = showQueue.pollFirst()
            alwaysShowProgressDialog()
            val activity = first?.ownerActivity
            if (activity == null || activity.isDestroyed) {
                showQueue.remove(mProgressDialog)
                dismissProgressDialog()
                return
            }
            first.dismiss()
        }
    }

    /**
     * 网络无数据view
     *
     * @param currentView 需要替换的view
     */
    fun hiddenNetNodataView(currentView: View?) {
        hiddenNodataView(NETWORKNO, currentView)
    }

    /**
     * 内容无数据view
     *
     * @param currentView 需要替换的view
     */
    fun hiddenContentNodataView(currentView: View?) {
        hiddenNodataView(CONTENTNODATA, currentView)
    }

    /**
     * 隐藏无数据view
     *
     * @param currentView 需要替换的view
     */
    fun hiddenNodataView(type: Int, currentView: View?) {
        if (contentViews == null || noDataViews == null) return
        val mNoDataViews = noDataViews.get(type, null)
        var mContentViews = contentViews.get(type, null)
        if (mContentViews == null) {
            contentViews.put(type, currentView)
            mContentViews = currentView
        }
        if (mNoDataViews == null) {
            return
        }
        if (!mNoDataViews.isNoDataViewShow()) {
            return
        }
        val viewGroup = mNoDataViews.parent
        if (viewGroup != null) {
            viewGroup as ViewGroup
            viewGroup.removeView(mNoDataViews)
            viewGroup.addView(mContentViews)
        }
        mNoDataViews.setNoDataViewShow(false)
    }

    private fun alwaysShowProgressDialog() {
        if (showQueue != null && !showQueue.isEmpty() && showQueue.first != null && !showQueue.first!!.isShowing) {
            val activity1 = showQueue.first!!.ownerActivity
            if (activity1 == null || activity1.isDestroyed || activity1.isFinishing) {
                showQueue.remove(mProgressDialog)
                alwaysShowProgressDialog()
                return
            }
            showQueue.first!!.show()
        }
    }

    fun setAgainRequestListener(againRequestListener: AgainRequestListener?) {
        this.againRequestListener = againRequestListener
    }

    fun setCancelRequestListener(cancelRequestListener: CancelRequestListener?) {
        this.cancelRequestListener = cancelRequestListener
    }

    interface CancelRequestListener {
        open fun cancel()
    }

    interface AgainRequestListener {
        open fun request()
    }

    companion object {
        const val NETWORKNO = 0
        const val CONTENTNODATA = 1
        private fun findActivity(context: Context?): Activity? {
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
    }

    init {
        contentViews = SparseArray()
        noDataViews = SparseArray()
        showQueue = ArrayDeque()
    }
}