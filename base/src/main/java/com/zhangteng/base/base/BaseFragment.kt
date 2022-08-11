package com.zhangteng.base.base

import android.content.DialogInterface
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import com.zhangteng.utils.StateViewHelper
import com.zhangteng.utils.showShortToast

/**
 * 1.viewpager+fragment时空白页面bug：设置setOffscreenPageLimit为tab数  或  onCreateView生成view前移除上一次已添加的view；
 * 2.fragment长时间后台显示时重叠bug，重写activity的onSaveInstanceState与onRestoreInstanceState避免activity重建时重新new fragment；
 * 3.fragment使用show/hide不影响生命周期可使用onHiddenChanged监听状态；viewpager中可使用getUserVisibleHint获取状态
 * Created by swing on 2017/11/23.
 */
abstract class BaseFragment : Fragment() {

    //是否第一次加载
    protected var isFirst: Boolean = true

    private val handler = Handler(Looper.getMainLooper())

    protected var mStateViewHelper: StateViewHelper? = null

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