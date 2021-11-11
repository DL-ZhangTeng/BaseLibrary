package com.zhangteng.baselibrary

import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.zhangteng.base.base.BaseMvpActivity
import com.zhangteng.base.mvp.base.LoadingPresenterHandler
import com.zhangteng.base.utils.ActivityHelper
import com.zhangteng.base.utils.LoadViewHelper
import com.zhangteng.base.utils.LogUtils
import com.zhangteng.base.utils.ToastUtils
import com.zhangteng.baselibrary.activity.NineImageActivity
import com.zhangteng.baselibrary.activity.TabLayoutActivity
import com.zhangteng.baselibrary.activity.TreeActivity
import com.zhangteng.baselibrary.mvp.model.imodel.IMainModel
import com.zhangteng.baselibrary.mvp.presenter.MainPresenter
import com.zhangteng.baselibrary.mvp.presenter.ipresenter.IMainPresenter
import com.zhangteng.baselibrary.mvp.view.IMainView
import com.zhangteng.baselibrary.ui.mvvm.MvvmActivity
import com.zhangteng.baselibrary.ui.mvvmdb.MvvmDbActivity
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
        ToastUtils.show(this, mPresenter?.testString(), 100)
    }

    override fun showLoadingView() {
        LogUtils.i("showLoadingView")
        showProgressDialog()
//        showNoNetView(tv_TextView)
//        showNoContentView(tv_TextView)
    }

    override fun dismissLoadingView() {
        LogUtils.i("dismissLoadingView")
//        dismissProgressDialog()
//        hiddenNoNetView(tv_TextView)
//        hiddenNoContentView(tv_TextView)

    }

    override fun inflateView(data: String?) {

    }

    override fun showProgressDialog() {
        if (mLoadViewHelper == null) {
            mLoadViewHelper = LoadViewHelper()
        }
        mLoadViewHelper?.showProgressDialog(this, R.drawable.loading5, "")
    }

    fun onClickTabLayout(v: View) {
        ActivityHelper.jumpToActivity(this, TabLayoutActivity::class.java, 1)
    }

    fun onClickTree(v: View) {
        ActivityHelper.jumpToActivity(this, TreeActivity::class.java, 1)
    }

    fun onClickNineImage(v: View) {
        ActivityHelper.jumpToActivity(this, NineImageActivity::class.java, 1)
    }

    fun onClickMvvm(v: View) {
        ActivityHelper.jumpToActivity(this, MvvmActivity::class.java, 1)
    }

    fun onClickMvvmDb(v: View) {
        ActivityHelper.jumpToActivity(this, MvvmDbActivity::class.java, 1)
    }
}