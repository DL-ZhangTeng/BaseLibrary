package com.zhangteng.baselibrary

import android.os.Bundle
import android.view.View
import android.widget.TextView
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
//        showNoContentView(tv_TextView)
    }

    override fun dismissLoadingView() {
        dismissProgressDialog()
//        hiddenNoNetView(tv_TextView)
//        hiddenNoContentView(tv_TextView)

    }

    override fun inflateView(data: String?) {

    }

    override fun showProgressDialog() {
        if (mLoadViewHelper == null) {
            mLoadViewHelper = com.zhangteng.utils.LoadViewHelper()
        }
        mLoadViewHelper?.showProgressDialog(this, R.drawable.loading5, "")
    }

    @TimeLog
    fun onClickTabLayout(v: View) {
        jumpToActivity(TabLayoutActivity::class.java, 1)
    }

    @TimeLog
    fun onClickTree(v: View) {
        jumpToActivity(TreeActivity::class.java, 1)
    }

    @TimeLog
    fun onClickNineImage(v: View) {
        jumpToActivity(NineImageActivity::class.java, 1)
    }

    @TimeLog
    fun onClickMvvm(v: View) {
        jumpToActivity(MvvmActivity::class.java, 1)
    }

    @TimeLog
    fun onClickMvvmDb(v: View) {
        jumpToActivity(MvvmDbActivity::class.java, 1)
    }

    @TimeLog
    fun onClickListMvvm(v: View) {
        jumpToActivity(BaseListMvvmDemoActivity::class.java, 1)
    }

    @TimeLog
    fun onClickListMvvmDb(v: View) {
        jumpToActivity(BaseListMvvmDbDemoDbActivity::class.java, 1)
    }
}