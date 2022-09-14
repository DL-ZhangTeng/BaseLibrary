package com.zhangteng.base.base

import android.content.DialogInterface
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import com.zhangteng.utils.IStateView
import com.zhangteng.utils.StateViewHelper
import com.zhangteng.utils.showShortToast

/**
 * 1.viewpager+fragment时空白页面bug：设置setOffscreenPageLimit为tab数  或  onCreateView生成view前移除上一次已添加的view；
 * 2.fragment长时间后台显示时重叠bug，重写activity的onSaveInstanceState与onRestoreInstanceState避免activity重建时重新new fragment；
 * 3.fragment使用show/hide不影响生命周期可使用onHiddenChanged监听状态；viewpager中可使用getUserVisibleHint获取状态
 * Created by swing on 2017/11/23.
 */
abstract class BaseFragment : Fragment(), IStateView {

    //是否第一次加载
    protected var isFirst: Boolean = true

    private val handler = Handler(Looper.getMainLooper())

    protected val mStateViewHelper by lazy { createStateViewHelper() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isFirst = true
        initView(view, savedInstanceState)
        initData(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        onVisible()
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
    }

    protected abstract fun initView(view: View, savedInstanceState: Bundle?)

    protected open fun initData(savedInstanceState: Bundle?) {}

    /**
     * 是否需要懒加载
     */
    private fun onVisible() {
        if (lifecycle.currentState == Lifecycle.State.STARTED && isFirst) {
            // 延迟加载 防止 切换动画还没执行完毕时数据就已经加载好了，这时页面会有渲染卡顿
            handler.postDelayed({
                lazyLoadData()
                isFirst = false
            }, lazyLoadTime())
        }
    }

    /**
     * 懒加载
     */
    protected open fun lazyLoadData() {}

    /**
     * 延迟加载 防止 切换动画还没执行完毕时数据就已经加载好了，这时页面会有渲染卡顿  bug
     * 这里传入你想要延迟的时间，延迟时间可以设置比转场动画时间长一点 单位： 毫秒
     * 不传默认 300毫秒
     * @return Long
     */
    protected open fun lazyLoadTime(): Long {
        return 300
    }

    /**
     * description 创建 StateViewHelper类，并回调重试请求、取消请求监听
     */
    override fun createStateViewHelper(): StateViewHelper {
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
        mStateViewHelper.showProgressDialog(context, mLoadingText = mLoadingText)
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
        context.showShortToast(message)
    }

    protected open fun showToast(messageId: Int) {
        context.showShortToast(messageId)
    }
}