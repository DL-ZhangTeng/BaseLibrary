package com.zhangteng.baselibrary

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.zhangteng.aop.annotation.CheckNet
import com.zhangteng.aop.annotation.Permissions
import com.zhangteng.aop.annotation.SingleClick
import com.zhangteng.aop.annotation.TimeLog
import com.zhangteng.baselibrary.activity.*
import com.zhangteng.baselibrary.mvp.model.imodel.IMainModel
import com.zhangteng.baselibrary.mvp.presenter.MainPresenter
import com.zhangteng.baselibrary.mvp.presenter.ipresenter.IMainPresenter
import com.zhangteng.baselibrary.mvp.view.IMainView
import com.zhangteng.baselibrary.ui.mvvm.MvvmActivity
import com.zhangteng.baselibrary.ui.mvvmdb.MvvmDbActivity
import com.zhangteng.mvp.mvp.BaseMvpActivity
import com.zhangteng.mvp.utils.LoadingPresenterHandler
import com.zhangteng.utils.StateViewHelper
import com.zhangteng.utils.jumpToActivity
import java.lang.reflect.Proxy

class MainActivity : BaseMvpActivity<IMainView, IMainModel, IMainPresenter>(), IMainView {
    private var tv_TextView: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun createPresenter(): IMainPresenter? {
//        return MainPresenter()
        return Proxy.newProxyInstance(
            MainPresenter::class.java.classLoader,
            arrayOf(IMainPresenter::class.java),
            LoadingPresenterHandler(MainPresenter())
        ) as IMainPresenter
    }

    override fun initView() {
        super.initView()
        tv_TextView = findViewById(R.id.tv_TextView)
    }

    override fun initData() {
        showToast(mPresenter?.testString())
    }

    override fun showLoadingView() {
        showProgressDialog()
//        showNoNetView(tv_TextView)
//        showEmptyView(tv_TextView)
    }

    override fun dismissLoadingView() {
        dismissProgressDialog()
//        showContentView(tv_TextView)
    }

    override fun inflateView(data: String?) {

    }

    @TimeLog
    @CheckNet
    @SingleClick
    @Permissions(value = ["android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.READ_EXTERNAL_STORAGE"])
    fun onClickBaseLayout(v: View) {
        jumpToActivity<BaseDemoActivity>()
    }

    @TimeLog
    fun onClickTabLayout(v: View) {
        jumpToActivity<TabLayoutActivity>()
    }

    @TimeLog
    fun onClickTree(v: View) {
        jumpToActivity<TreeActivity>()
    }

    @TimeLog
    fun onClickNineImage(v: View) {
        jumpToActivity<NineImageActivity>()
    }

    @TimeLog
    fun onClickMvvm(v: View) {
        jumpToActivity<MvvmActivity>()
    }

    @TimeLog
    fun onClickMvvmDb(v: View) {
        jumpToActivity<MvvmDbActivity>()
    }

    @TimeLog
    fun onClickListMvvm(v: View) {
        jumpToActivity<BaseListMvvmDemoActivity>()
    }

    @TimeLog
    fun onClickListMvvmDb(v: View) {
        jumpToActivity<BaseListMvvmDbDemoDbActivity>()
    }

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

    override fun showProgressDialog(mLoadingText: String?) {
        mStateViewHelper.showProgressDialog(this, R.drawable.loading5, mLoadingText)
    }

    override fun againRequestByStateViewHelper(view: View) {
        super.againRequestByStateViewHelper(view)

    }

    override fun cancelRequestByStateViewHelper(dialog: DialogInterface) {
        super.cancelRequestByStateViewHelper(dialog)

    }
}