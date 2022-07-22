package com.zhangteng.aop.annotation

/**
 * description: 网络检测注解
 * author: Swing
 * date: 2022/7/22
 */
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
@Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER
)
annotation class CheckNet 