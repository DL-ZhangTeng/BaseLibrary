package com.zhangteng.aop.aspect

import android.content.Context
import android.util.Log
import com.zhangteng.aop.annotation.TimeLog
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut
import org.aspectj.lang.reflect.MethodSignature

@Aspect // 定义切面类
class TimeLogAspect {
    // 1、应用中用到了哪些注解，放到当前的切入点进行处理（找到需要处理的切入点）
    // execution，以方法执行时作为切点，触发Aspect类
    // * *(..)) 可以处理ClickBehavior这个类所有的方法
    @Pointcut("execution(@com.zhangteng.aop.annotation.TimeLog * *(..))")
    fun pointCut() {
    }

    // 2、对切入点如何处理
    @Around("pointCut()")
    @Throws(Throwable::class)
    fun joinPoint(joinPoint: ProceedingJoinPoint): Any? {
        val stringBuilder = StringBuilder()
        //1.获取切入点所在目标对象
        val targetObj = joinPoint.target
        stringBuilder.append(" \n")
        stringBuilder.append("类名：").append(targetObj.javaClass.name)
        // 2.获取切入点方法的名字
        val methodName = joinPoint.signature.name
        stringBuilder.append(" \n")
        stringBuilder.append("方法名：").append(methodName)
        // 3. 获取方法上的注解
        val signature = joinPoint.signature
        val methodSignature = signature as MethodSignature
        val method = methodSignature.method
        if (method != null) {
            val apiLog = method.getAnnotation(TimeLog::class.java)
            stringBuilder.append(" \n")
            stringBuilder.append("value：").append(apiLog?.value)
        }

        //4. 获取方法的参数
        val args = joinPoint.args
        for (o in args) {
            stringBuilder.append(" \n")
            stringBuilder.append("参数：").append(o)
        }
        val context = joinPoint.getThis() as Context?
        val startTime = System.currentTimeMillis()
        val response = joinPoint.proceed()
        val endTime = System.currentTimeMillis()
        val duration = endTime - startTime
        stringBuilder.append(" \n")
        stringBuilder.append("耗时：").append(duration).append("ms")
        Log.d("TimeLog", stringBuilder.toString())
        return response
    }
}