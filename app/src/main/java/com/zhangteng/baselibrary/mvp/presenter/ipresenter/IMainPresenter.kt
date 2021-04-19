package com.zhangteng.baselibrary.mvp.presenter.ipresenter

import com.zhangteng.base.mvp.base.IPresenter
import com.zhangteng.baselibrary.mvp.view.MainView

interface IMainPresenter : IPresenter<MainView?> {
    fun testString(): String?
}