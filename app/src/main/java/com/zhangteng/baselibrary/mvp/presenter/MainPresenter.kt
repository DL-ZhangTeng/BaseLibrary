package com.zhangteng.baselibrary.mvp.presenter

import com.zhangteng.base.mvp.base.BaseLoadingPresenter
import com.zhangteng.baselibrary.mvp.presenter.ipresenter.IMainPresenter
import com.zhangteng.baselibrary.mvp.view.MainView

class MainPresenter : BaseLoadingPresenter<MainView?>() , IMainPresenter {
    override fun testString(): String? {
        return "test"
    }
}