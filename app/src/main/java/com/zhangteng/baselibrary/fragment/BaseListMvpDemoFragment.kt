package com.zhangteng.baselibrary.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.zhangteng.baselibrary.R
import com.zhangteng.baselibrary.adapter.BaseListMvpDemoAdapter
import com.zhangteng.baselibrary.bean.BaseListMvpDemoBean
import com.zhangteng.baselibrary.mvp.model.imodel.IBaseListMvpDemoFragmentModel
import com.zhangteng.baselibrary.mvp.presenter.BaseListMvpDemoFragmentPresenter
import com.zhangteng.baselibrary.mvp.presenter.ipresenter.IBaseListMvpDemoFragmentPresenter
import com.zhangteng.baselibrary.mvp.view.IBaseListMvpDemoFragmentView
import com.zhangteng.mvp.mvp.BaseListMvpFragment

class BaseListMvpDemoFragment :
    BaseListMvpFragment<IBaseListMvpDemoFragmentView, IBaseListMvpDemoFragmentModel, IBaseListMvpDemoFragmentPresenter, BaseListMvpDemoBean, BaseListMvpDemoAdapter>(),
    IBaseListMvpDemoFragmentView {

    companion object {
        fun newInstance() = BaseListMvpDemoFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return LayoutInflater.from(context)
            .inflate(R.layout.fragment_base_list_mvp_demo, container, false)
    }

    /**
     *return Proxy.newProxyInstance(BaseListMvpDemoFragmentPresenter::class.java.classLoader, arrayOf(IBaseListMvpDemoFragmentPresenter::class.java), LoadingPresenterHandler(BaseListMvpDemoFragmentPresenter())) as IBaseListMvpDemoFragmentPresenter
     */
    override fun createPresenter(): IBaseListMvpDemoFragmentPresenter? {
        return BaseListMvpDemoFragmentPresenter()
    }

    override fun initView(view: View, savedInstanceState: Bundle?) {
        super.initView(view, savedInstanceState)

    }

    override fun initData(savedInstanceState: Bundle?) {

    }

    override fun createAdapter(): BaseListMvpDemoAdapter {
        return BaseListMvpDemoAdapter(mList)
    }

    override fun getRecyclerView(): RecyclerView? {
        return view?.findViewById(R.id.recyclerView)
    }

    override fun getSmartRefreshLayout(): SmartRefreshLayout? {
        return view?.findViewById(R.id.smartRefreshLayout)
    }

    override fun loadData(i: Int) {}
    override fun setLayoutManager() {
        setLinearLayoutManager(LinearLayoutManager.VERTICAL)
    }
}