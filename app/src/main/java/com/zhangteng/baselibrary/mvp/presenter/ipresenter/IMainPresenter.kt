package com.zhangteng.baselibrary.mvp.presenter.ipresenter

import com.zhangteng.mvp.base.IPresenter
import com.zhangteng.baselibrary.mvp.model.imodel.IMainModel
import com.zhangteng.baselibrary.mvp.view.IMainView

interface IMainPresenter : IPresenter<IMainView, IMainModel> {
    fun testString(): String?
}