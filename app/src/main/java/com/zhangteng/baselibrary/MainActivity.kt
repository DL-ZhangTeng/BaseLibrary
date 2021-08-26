package com.zhangteng.baselibrary

import android.os.Bundle
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.zhangteng.base.adapter.CommonFragmentAdapter
import com.zhangteng.base.base.BaseMvpActivity
import com.zhangteng.base.mvp.base.LoadingPresenterHandler
import com.zhangteng.base.utils.LoadViewHelper
import com.zhangteng.base.utils.LogUtils
import com.zhangteng.base.utils.ToastUtils
import com.zhangteng.base.widget.MyTabLayout
import com.zhangteng.baselibrary.mvp.model.imodel.IMainModel
import com.zhangteng.baselibrary.mvp.presenter.MainPresenter
import com.zhangteng.baselibrary.mvp.presenter.ipresenter.IMainPresenter
import com.zhangteng.baselibrary.mvp.view.IMainView
import java.lang.reflect.Proxy

class MainActivity : BaseMvpActivity<IMainView, IMainModel, IMainPresenter>(), IMainView {
    private var tv_TextView: TextView? = null
    private var tab_layout: MyTabLayout? = null
    private var vp: ViewPager? = null
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
        tab_layout = findViewById(R.id.tab_layout)
        vp = findViewById(R.id.vp)
        tab_layout?.setupWithViewPager(vp)
        val fragments = ArrayList<Fragment>()
        fragments.add(Fragment())
        fragments.add(Fragment())
        fragments.add(Fragment())
        vp?.adapter = CommonFragmentAdapter(supportFragmentManager, arrayOf("1", "2", "3"), fragments)
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
}