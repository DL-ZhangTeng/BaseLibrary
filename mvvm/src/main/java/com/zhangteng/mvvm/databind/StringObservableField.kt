package com.zhangteng.mvvm.databind

import androidx.databinding.ObservableField

/**
 * 自定义的String类型 ObservableField  提供了默认值，避免取值的时候还要判空
 */
open class StringObservableField(value: String = "") : ObservableField<String>(value) {

    override fun get(): String {
        return super.get()!!
    }

}