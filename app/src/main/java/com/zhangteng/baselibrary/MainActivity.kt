package com.zhangteng.baselibrary

import android.os.Bundle
import com.zhangteng.base.base.BaseMvpActivity
import com.zhangteng.base.mvp.base.LoadingPresenterHandler
import com.zhangteng.base.utils.LogUtils
import com.zhangteng.base.utils.ToastUtils
import com.zhangteng.baselibrary.mvp.model.imodel.IMainModel
import com.zhangteng.baselibrary.mvp.presenter.MainPresenter
import com.zhangteng.baselibrary.mvp.presenter.ipresenter.IMainPresenter
import com.zhangteng.baselibrary.mvp.view.IMainView
import java.lang.reflect.Proxy

class MainActivity : BaseMvpActivity<IMainView, IMainModel, IMainPresenter>(), IMainView {
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

    override fun initData() {
        ToastUtils.show(this, mPresenter?.testString(), 100)
    }

    override fun showLoadingView() {
        LogUtils.i("showLoadingView")
    }

    override fun dismissLoadingView() {
        LogUtils.i("dismissLoadingView")
    }

    override fun inflateView(data: String?) {

    }
}