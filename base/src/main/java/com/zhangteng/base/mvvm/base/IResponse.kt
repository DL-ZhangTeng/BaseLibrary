package com.zhangteng.base.mvvm.base


/**
 * 如果需要框架帮你做脱壳处理请实现它
 */
interface IResponse<T> {

    //抽象方法，用户的基类继承该类时，需要重写该方法
    fun isSuccess(): Boolean

    fun getResult(): T

    fun getCode(): Int

    fun getMsg(): String

}