package com.zhangteng.baselibrary.activity

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.zhangteng.base.base.BaseListMvpActivity
import com.zhangteng.base.base.BaseMvpActivity
import com.zhangteng.baselibrary.mvp.model.imodel.IBaseListMvpDemoModel
import com.zhangteng.baselibrary.mvp.presenter.BaseListMvpDemoPresenter
import com.zhangteng.baselibrary.mvp.presenter.ipresenter.IBaseListMvpDemoPresenter
import com.zhangteng.baselibrary.mvp.view.IBaseListMvpDemoView
import com.zhangteng.baselibrary.R
import com.zhangteng.baselibrary.adapter.BaseListMvpDemoAdapter
import com.zhangteng.baselibrary.bean.BaseListMvpDemoBean

class BaseListMvpDemoActivity :
    BaseListMvpActivity<IBaseListMvpDemoView, IBaseListMvpDemoModel, IBaseListMvpDemoPresenter, BaseListMvpDemoBean, BaseListMvpDemoAdapter>(),
    IBaseListMvpDemoView {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base_list_mvp_demo)
    }

    override fun createPresenter(): IBaseListMvpDemoPresenter? {
        return BaseListMvpDemoPresenter()
//        return Proxy.newProxyInstance(
//            BaseListMvpDemoPresenter::class.java.classLoader,
//            arrayOf(IBaseListMvpDemoPresenter::class.java),
//            LoadingPresenterHandler(BaseListMvpDemoPresenter())
//        ) as IBaseListMvpDemoPresenter
    }

    override fun initView() {
        super.initView()

    }

    override fun initData() {}

    override fun createAdapter(): BaseListMvpDemoAdapter {
        return BaseListMvpDemoAdapter()
    }

    override fun getRecyclerView(): RecyclerView {
        return findViewById(R.id.recyclerView)
    }

    override fun getSmartRefreshLayout(): SmartRefreshLayout {
        return findViewById(R.id.smartRefreshLayout)
    }

    override fun loadData(i: Int) {}
    override fun setLayoutManager() {
        setLinearLayoutManager(LinearLayoutManager.VERTICAL)
    }
}