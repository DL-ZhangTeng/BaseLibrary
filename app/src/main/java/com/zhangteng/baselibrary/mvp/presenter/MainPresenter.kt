package com.zhangteng.baselibrary.mvp.presenter

import com.zhangteng.mvp.base.BaseLoadingPresenter
import com.zhangteng.baselibrary.mvp.model.MainModel
import com.zhangteng.baselibrary.mvp.model.imodel.IMainModel
import com.zhangteng.baselibrary.mvp.presenter.ipresenter.IMainPresenter
import com.zhangteng.baselibrary.mvp.view.IMainView

class MainPresenter : BaseLoadingPresenter<IMainView, IMainModel>(), IMainPresenter {
    init {
        this.mModel = MainModel()
    }

    override fun testString(): String? {
        return mModel?.testString()
    }
}