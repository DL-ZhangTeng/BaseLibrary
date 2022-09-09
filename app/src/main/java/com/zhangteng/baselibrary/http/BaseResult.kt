package com.zhangteng.baselibrary.http

import com.zhangteng.utils.IResponse

data class BaseResult<T>(
    val errorMsg: String,
    val errorCode: Int,
    val data: T
) : IResponse<T> {

    override fun getCode() = errorCode

    override fun getMsg() = errorMsg

    override fun getResult() = data

    override fun isSuccess() = errorCode == 0

}