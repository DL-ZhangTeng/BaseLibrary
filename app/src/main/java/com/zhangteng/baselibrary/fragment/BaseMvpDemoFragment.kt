package com.zhangteng.baselibrary.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.zhangteng.mvp.mvp.BaseMvpFragment
import com.zhangteng.baselibrary.mvp.model.imodel.IBaseMvpDemoFragmentModel
import com.zhangteng.baselibrary.mvp.presenter.BaseMvpDemoFragmentPresenter
import com.zhangteng.baselibrary.mvp.presenter.ipresenter.IBaseMvpDemoFragmentPresenter
import com.zhangteng.baselibrary.mvp.view.IBaseMvpDemoFragmentView
import com.zhangteng.baselibrary.R

class BaseMvpDemoFragment :
    com.zhangteng.mvp.mvp.BaseMvpFragment<IBaseMvpDemoFragmentView, IBaseMvpDemoFragmentModel, IBaseMvpDemoFragmentPresenter>(),
    IBaseMvpDemoFragmentView {

    companion object {
        fun newInstance() = BaseMvpDemoFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return LayoutInflater.from(context)
            .inflate(R.layout.fragment_base_mvp_demo, container, false)
    }

    /**
     *return Proxy.newProxyInstance(BaseMvpDemoFragmentPresenter::class.java.classLoader, arrayOf(IBaseMvpDemoFragmentPresenter::class.java), LoadingPresenterHandler(BaseMvpDemoFragmentPresenter())) as IBaseMvpDemoFragmentPresenter
     */
    override fun createPresenter(): IBaseMvpDemoFragmentPresenter? {
        return BaseMvpDemoFragmentPresenter()
    }

    override fun initView(view: View, savedInstanceState: Bundle?) {
        super.initView(view, savedInstanceState)

    }

    override fun initData(savedInstanceState: Bundle?) {

    }
}