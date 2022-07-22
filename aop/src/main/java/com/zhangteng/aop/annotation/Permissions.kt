package com.zhangteng.aop.annotation

/**
 * description: 权限申请注解
 * author: Swing
 * date: 2022/7/22
 */
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
@Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER
)
annotation class Permissions(
    /**
     * 需要申请权限的集合
     */
    val value: Array<String>
)