package com.zhangteng.aop.aspect

import com.zhangteng.aop.annotation.SingleClick
import com.zhangteng.utils.i
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut
import org.aspectj.lang.reflect.CodeSignature

/**
 * description: 防重复点击切面
 * author: Swing
 * date: 2022/7/22
 */
@Aspect
class SingleClickAspect {
    /**
     * 最近一次点击的时间
     */
    private var mLastTime: Long = 0

    /**
     * 最近一次点击的标记
     */
    private var mLastTag: String? = null

    /**
     * 方法切入点
     */
    @Pointcut("execution(@com.zhangteng.aop.annotation.SingleClick * *(..))")
    fun pointCut() {
    }

    /**
     * 在连接点进行方法替换
     */
    @Around("pointCut() && @annotation(singleClick)")
    @Throws(Throwable::class)
    fun joinPoint(joinPoint: ProceedingJoinPoint, singleClick: SingleClick) {
        val codeSignature = joinPoint.signature as CodeSignature
        // 方法所在类
        val className = codeSignature.declaringType.name
        // 方法名
        val methodName = codeSignature.name
        // 构建方法 TAG
        val builder = StringBuilder("$className.$methodName")
        builder.append("(")
        val parameterValues = joinPoint.args
        for (i in parameterValues.indices) {
            val arg = parameterValues[i]
            if (i == 0) {
                builder.append(arg)
            } else {
                builder.append(", ")
                    .append(arg)
            }
        }
        builder.append(")")
        val tag = builder.toString()
        val currentTimeMillis = System.currentTimeMillis()
        if (currentTimeMillis - mLastTime < singleClick.value && tag == mLastTag) {
            String.format("SingleClick %s 毫秒内发生快速点击：%s", singleClick.value, tag).i()
            return
        }
        mLastTime = currentTimeMillis
        mLastTag = tag
        // 执行原方法
        joinPoint.proceed()
    }
}