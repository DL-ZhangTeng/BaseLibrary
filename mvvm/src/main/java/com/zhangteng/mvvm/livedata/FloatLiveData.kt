package com.zhangteng.mvvm.livedata

import androidx.lifecycle.MutableLiveData


/**
 * 自定义的Float类型 MutableLiveData 提供了默认值，避免取值的时候还要判空
 */
class FloatLiveData() : MutableLiveData<Float>() {
    override fun getValue(): Float {
        return super.getValue() ?: 0f
    }
}