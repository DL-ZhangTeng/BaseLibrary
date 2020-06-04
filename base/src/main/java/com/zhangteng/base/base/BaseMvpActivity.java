package com.zhangteng.base.base;

import android.os.Bundle;

import com.zhangteng.base.mvp.base.BasePresenter;
import com.zhangteng.base.mvp.base.BaseView;


/**
 * Created by swing on 2017/11/23.
 */

public abstract class BaseMvpActivity<V extends BaseView, P extends BasePresenter<V>> extends BaseActivity {
    protected P mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = createPresenter();
        mPresenter.attachView((V) this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }

    public abstract P createPresenter();
}

