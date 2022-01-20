package com.zhangteng.baselibrary.activity

import android.os.Bundle
import com.zhangteng.mvp.mvp.BaseMvpActivity
import com.zhangteng.baselibrary.R
import com.zhangteng.baselibrary.mvp.model.imodel.IBaseMvpDemoModel
import com.zhangteng.baselibrary.mvp.presenter.BaseMvpDemoPresenter
import com.zhangteng.baselibrary.mvp.presenter.ipresenter.IBaseMvpDemoPresenter
import com.zhangteng.baselibrary.mvp.view.IBaseMvpDemoView

class BaseMvpDemoActivity :
    com.zhangteng.mvp.mvp.BaseMvpActivity<IBaseMvpDemoView, IBaseMvpDemoModel, IBaseMvpDemoPresenter>(),
    IBaseMvpDemoView {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base_mvp_demo)
    }

    override fun createPresenter(): IBaseMvpDemoPresenter? {
        return BaseMvpDemoPresenter()
//        return Proxy.newProxyInstance(
//            BaseMvpDemoPresenter::class.java.classLoader,
//            arrayOf(IBaseMvpDemoPresenter::class.java),
//            LoadingPresenterHandler(BaseMvpDemoPresenter())
//        ) as IBaseMvpDemoPresenter
    }

    override fun initView() {
        super.initView()

    }

    override fun initData() {

    }
}