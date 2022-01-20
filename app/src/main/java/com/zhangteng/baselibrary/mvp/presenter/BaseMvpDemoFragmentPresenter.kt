package com.zhangteng.baselibrary.mvp.presenter

import com.zhangteng.mvp.base.BasePresenter
import com.zhangteng.baselibrary.mvp.model.BaseMvpDemoFragmentModel
import com.zhangteng.baselibrary.mvp.model.imodel.IBaseMvpDemoFragmentModel
import com.zhangteng.baselibrary.mvp.presenter.ipresenter.IBaseMvpDemoFragmentPresenter
import com.zhangteng.baselibrary.mvp.view.IBaseMvpDemoFragmentView

class BaseMvpDemoFragmentPresenter :
    BasePresenter<IBaseMvpDemoFragmentView, IBaseMvpDemoFragmentModel>(),
    IBaseMvpDemoFragmentPresenter {
    init {
        this.mModel = BaseMvpDemoFragmentModel()
    }
}