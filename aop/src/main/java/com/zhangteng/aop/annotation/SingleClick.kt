package com.zhangteng.aop.annotation

/**
 * description: 防重复点击注解
 * author: Swing
 * date: 2022/7/22
 */
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
@Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER
)
annotation class SingleClick(
    /**
     * 快速点击的间隔
     */
    val value: Long = 1000
)