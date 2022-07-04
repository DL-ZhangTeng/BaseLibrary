package com.zhangteng.utils;

import java.util.List;

/**
 * Created by Swing on 2018/4/18.
 */
public interface IHandlerCallBack {

    void onStart();

    void onSuccess(List<IMediaBean> selectList);

    void onCancel();

    void onFinish(List<IMediaBean> selectList);

    void onError();

}
