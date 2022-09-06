package com.zhangteng.base.base

import android.R
import android.content.ActivityNotFoundException
import android.content.DialogInterface
import android.content.Intent
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.gyf.immersionbar.ImmersionBar
import com.zhangteng.utils.StateViewHelper
import com.zhangteng.utils.showShortToast

/**
 * Created by swing on 2017/11/23.
 */
abstract class BaseActivity : AppCompatActivity() {
    /** 状态栏沉浸  */
    protected val mImmersionBar by lazy { createStatusBarConfig() }
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
        if (isImmersionBarEnabled()) {
            mImmersionBar.init()
        }
        initView()
        initData()
    }

    protected abstract fun initView()
    protected abstract fun initData()

    /**
     * 是否使用沉浸式状态栏
     */
    protected open fun isImmersionBarEnabled(): Boolean {
        return true
    }

    /**
     * 状态栏字体深色模式
     */
    protected open fun isStatusBarDarkFont(): Boolean {
        return true
    }

    /**
     * 初始化沉浸式状态栏,隐藏状态栏与导航条重写此方法super.createStatusBarConfig().hideBar(BarHide.FLAG_HIDE_BAR)
     */
    protected open fun createStatusBarConfig(): ImmersionBar {
        return ImmersionBar.with(this)
            .statusBarDarkFont(isStatusBarDarkFont())
            .navigationBarColor(R.color.white)
            .autoDarkModeEnable(true, 0.2f)
    }

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
                        cancelRequestByStateViewHelper(dialog)
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
                        cancelRequestByStateViewHelper(dialog)
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
                        cancelRequestByStateViewHelper(dialog)
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
                        cancelRequestByStateViewHelper(dialog)
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
                        cancelRequestByStateViewHelper(dialog)
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
                        cancelRequestByStateViewHelper(dialog)
                    }
                }
            }
        }
        mStateViewHelper?.showProgressDialog(this, mLoadingText = mLoadingText)
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
    protected open fun cancelRequestByStateViewHelper(dialog: DialogInterface) {

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

    @Deprecated(
        message = "use {@link #registerForActivityResult(ActivityResultContract, ActivityResultCallback)} passing in a {@link StartActivityForResult} object for the {@link ActivityResultContract}.",
        level = DeprecationLevel.WARNING
    )
    override fun startActivityForResult(intent: Intent?, requestCode: Int) {
        try {
            super.startActivityForResult(intent, requestCode)
        } catch (e: ActivityNotFoundException) {
            showToast("未找到相应应用")
        }
    }
}