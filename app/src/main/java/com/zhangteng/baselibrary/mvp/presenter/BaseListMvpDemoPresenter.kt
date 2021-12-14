package com.zhangteng.baselibrary.mvp.presenter

import com.zhangteng.base.mvp.base.BasePresenter
import com.zhangteng.baselibrary.mvp.model.BaseListMvpDemoModel
import com.zhangteng.baselibrary.mvp.model.imodel.IBaseListMvpDemoModel
import com.zhangteng.baselibrary.mvp.presenter.ipresenter.IBaseListMvpDemoPresenter
import com.zhangteng.baselibrary.mvp.view.IBaseListMvpDemoView

class BaseListMvpDemoPresenter : BasePresenter<IBaseListMvpDemoView, IBaseListMvpDemoModel>(),
    IBaseListMvpDemoPresenter {
    init {
        this.mModel = BaseListMvpDemoModel()
    }
}