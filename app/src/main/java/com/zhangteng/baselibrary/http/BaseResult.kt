package com.zhangteng.baselibrary.http

import com.zhangteng.base.mvvm.base.BaseLoadingViewModel

data class BaseResult<T>(
    val errorMsg: String,
    val errorCode: Int,
    val data: T
) : BaseLoadingViewModel.IResponse<T> {

    override fun getCode() = errorCode

    override fun getMsg() = errorMsg

    override fun getResult() = data

    override fun isSuccess() = errorCode == 0

}