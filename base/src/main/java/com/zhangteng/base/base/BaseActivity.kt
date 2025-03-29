package com.zhangteng.base.base

import android.content.ActivityNotFoundException
import android.content.DialogInterface
import android.content.Intent
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.gyf.immersionbar.ImmersionBar
import com.zhangteng.utils.IStateView
import com.zhangteng.utils.StateViewHelper
import com.zhangteng.utils.showShortToast

/**
 * Created by swing on 2017/11/23.
 */
abstract class BaseActivity : AppCompatActivity(), IStateView {

    protected val mImmersionBar by lazy { createStatusBarConfig() }
    val mStateViewHelper by lazy { createStateViewHelper() }

    override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)
        if (isImmersionBarEnabled()) {
            mImmersionBar.init()
        }
        initView()
        initData()
    }

    override fun setContentView(view: View?) {
        super.setContentView(view)
        if (isImmersionBarEnabled()) {
            mImmersionBar.init()
        }
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
     * description 是否使用沉浸式状态栏
     */
    protected open fun isImmersionBarEnabled(): Boolean {
        return true
    }

    /**
     * description 状态栏字体深色模式
     */
    protected open fun isStatusBarDarkFont(): Boolean {
        return true
    }

    /**
     * description 初始化沉浸式状态栏
     * ImmersionBar.with(this)
     * //设置状态栏View
     * .statusBarView(statusBar)
     * //使用titleBar后不需要在layout_toolbar布局中添加statusBar节点
     * //设置标题栏View，如果设置了statusBarView则不需要设置标题栏View（设置标题栏View会自动在顶部给titleBar增加一个状态栏的高度Padding、Height）
     * .titleBar(titleBar)
     * //深色状态栏字体
     * .statusBarDarkFont(true)
     * //导航栏白色
     * .navigationBarColor(R.color.white)
     * //自动改吧状态栏导航栏颜色
     * .autoDarkModeEnable(true, 0.2f)
     * //隐藏状态栏与标题栏
     * .hideBar(BarHide.FLAG_HIDE_BAR)
     */
    protected open fun createStatusBarConfig(): ImmersionBar {
        return ImmersionBar.with(this)
            .statusBarDarkFont(isStatusBarDarkFont())
            .navigationBarColor(android.R.color.white)
            .autoDarkModeEnable(true, 0.2f)
    }

    /**
     * description 创建 StateViewHelper类，并回调重试请求、取消请求监听
     */
    override fun createStateViewHelper(): StateViewHelper {
        return StateViewHelper().apply {
            againRequestListeners.add(object : StateViewHelper.AgainRequestListener {
                override fun request(view: View) {
                    againRequestByStateViewHelper(view)
                }
            })
            cancelRequestListeners.add(object : StateViewHelper.CancelRequestListener {
                override fun cancel(dialog: DialogInterface) {
                    cancelRequestByStateViewHelper(dialog)
                }
            })
        }
    }

    /**
     * description 无网络视图
     * @param contentView 被替换的View
     */
    override fun showNoNetView(contentView: View?) {
        mStateViewHelper.showNoNetView(contentView)
    }

    /**
     * description 超时视图
     * @param contentView 被替换的View
     */
    override fun showTimeOutView(contentView: View?) {
        mStateViewHelper.showTimeOutView(contentView)
    }

    /**
     * description 无数据视图
     * @param contentView 被替换的View
     */
    override fun showEmptyView(contentView: View?) {
        mStateViewHelper.showEmptyView(contentView)
    }

    /**
     * description 错误视图
     * @param contentView 被替换的View
     */
    override fun showErrorView(contentView: View?) {
        mStateViewHelper.showErrorView(contentView)
    }

    /**
     * description 未登录视图
     * @param contentView 被替换的View
     */
    override fun showNoLoginView(contentView: View?) {
        mStateViewHelper.showNoLoginView(contentView)
    }

    /**
     * description 业务视图
     * @param contentView 要展示的View
     */
    override fun showContentView(contentView: View?) {
        mStateViewHelper.showContentView(contentView)
    }

    /**
     * description 加载中弹窗
     * @param mLoadingText 加载中...
     */
    override fun showProgressDialog(mLoadingText: String?) {
        mStateViewHelper.showProgressDialog(this, mLoadingText = mLoadingText)
    }

    /**
     * description 关闭加载中弹窗
     */
    override fun dismissProgressDialog() {
        mStateViewHelper.dismissProgressDialog()
    }

    /**
     * description 状态View重新请求回调
     * @param view 重试按钮
     */
    override fun againRequestByStateViewHelper(view: View) {

    }

    /**
     * description 加载中取消回调
     * @param dialog 加载中弹窗
     */
    override fun cancelRequestByStateViewHelper(dialog: DialogInterface) {

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
    override fun startActivityForResult(intent: Intent, requestCode: Int) {
        try {
            super.startActivityForResult(intent, requestCode)
        } catch (e: ActivityNotFoundException) {
            showToast("未找到相应应用")
        }
    }
}