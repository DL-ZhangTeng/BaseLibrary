package com.zhangteng.baselibrary.mvp.presenter

import com.zhangteng.mvp.base.BasePresenter
import com.zhangteng.baselibrary.mvp.model.BaseMvpDemoModel
import com.zhangteng.baselibrary.mvp.model.imodel.IBaseMvpDemoModel
import com.zhangteng.baselibrary.mvp.presenter.ipresenter.IBaseMvpDemoPresenter
import com.zhangteng.baselibrary.mvp.view.IBaseMvpDemoView

class BaseMvpDemoPresenter : BasePresenter<IBaseMvpDemoView, IBaseMvpDemoModel>(),
    IBaseMvpDemoPresenter {
    init {
        this.mModel = BaseMvpDemoModel()
    }
}