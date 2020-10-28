package com.zhangteng.base.base;

import com.zhangteng.base.mvp.base.BasePresenter;
import com.zhangteng.base.mvp.base.BaseView;

/**
 * 使用Mvp模式Activity基类
 * Created by swing on 2017/11/23.
 */
public abstract class BaseMvpActivity<V extends BaseView, P extends BasePresenter<V>> extends BaseActivity {
    protected P mPresenter;

    @Override
    protected void initView() {
        mPresenter = createPresenter();
        mPresenter.attachView((V) this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }

    /**
     * 子类提供实现
     * 创建对应页面的presenter
     */
    public abstract P createPresenter();
}

