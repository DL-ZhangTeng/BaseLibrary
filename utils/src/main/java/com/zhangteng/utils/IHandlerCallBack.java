package com.zhangteng.utils;

import java.util.List;

/**
 * Created by Swing on 2018/4/18.
 */
public interface IHandlerCallBack<T extends IMediaBean> {

    void onStart();

    void onSuccess(List<T> selectList);

    void onCancel();

    void onFinish(List<T> selectList);

    void onError();

}
