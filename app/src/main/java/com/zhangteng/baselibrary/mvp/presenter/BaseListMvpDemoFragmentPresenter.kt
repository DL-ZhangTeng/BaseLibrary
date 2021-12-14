package com.zhangteng.baselibrary.mvp.presenter

import com.zhangteng.base.mvp.base.BasePresenter
import com.zhangteng.baselibrary.mvp.model.BaseListMvpDemoFragmentModel
import com.zhangteng.baselibrary.mvp.model.imodel.IBaseListMvpDemoFragmentModel
import com.zhangteng.baselibrary.mvp.presenter.ipresenter.IBaseListMvpDemoFragmentPresenter
import com.zhangteng.baselibrary.mvp.view.IBaseListMvpDemoFragmentView

class BaseListMvpDemoFragmentPresenter :
    BasePresenter<IBaseListMvpDemoFragmentView, IBaseListMvpDemoFragmentModel>(),
    IBaseListMvpDemoFragmentPresenter {
    init {
        this.mModel = BaseListMvpDemoFragmentModel()
    }
}