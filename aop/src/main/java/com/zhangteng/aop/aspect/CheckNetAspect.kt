package com.zhangteng.aop.aspect

import android.content.Context
import com.zhangteng.aop.R
import com.zhangteng.aop.annotation.CheckNet
import com.zhangteng.utils.isConnected
import com.zhangteng.utils.showShortToast
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut

/**
 * description: 网络检测切面
 * author: Swing
 * date: 2022/7/22
 */
@Aspect
class CheckNetAspect {
    /**
     * 方法切入点
     */
    @Pointcut("execution(@com.zhangteng.aop.annotation.CheckNet * *(..))")
    fun pointCut() {
    }

    /**
     * 在连接点进行方法替换
     */
    @Around("pointCut() && @annotation(checkNet)")
    @Throws(Throwable::class)
    fun joinPoint(joinPoint: ProceedingJoinPoint, checkNet: CheckNet?) {
        val context = joinPoint.getThis() as Context
        if (!context.isConnected()) {
            context.showShortToast(context.resources.getString(R.string.aop_network_hint))
            return
        }
        //执行原方法
        joinPoint.proceed()
    }
}