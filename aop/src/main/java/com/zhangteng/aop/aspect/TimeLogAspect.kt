package com.zhangteng.aop.aspect;

import android.content.Context;
import android.util.Log;

import com.zhangteng.aop.annotation.TimeLog;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;

@Aspect // 定义切面类
public class TimeLogAspect {

    // 1、应用中用到了哪些注解，放到当前的切入点进行处理（找到需要处理的切入点）
    // execution，以方法执行时作为切点，触发Aspect类
    // * *(..)) 可以处理ClickBehavior这个类所有的方法
    @Pointcut("execution(@com.vincent.aop.project.annotation.TimeLog * *(..))")
    public void pointCut() {
    }

    // 2、对切入点如何处理
    @Around("pointCut()")
    public Object joinPoint(ProceedingJoinPoint joinPoint) throws Throwable {

        StringBuilder stringBuilder = new StringBuilder();
        //1.获取切入点所在目标对象
        Object targetObj = joinPoint.getTarget();
        stringBuilder.append("\n");
        stringBuilder.append("类名：").append(targetObj.getClass().getName());
        // 2.获取切入点方法的名字
        String methodName = joinPoint.getSignature().getName();
        stringBuilder.append("\n");
        stringBuilder.append("方法名：").append(methodName);
        // 3. 获取方法上的注解
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();

        if (method != null) {
            TimeLog apiLog = method.getAnnotation(TimeLog.class);
            stringBuilder.append("\n");
            stringBuilder.append("value：").append(apiLog != null ? apiLog.value() : null);
        }

        //4. 获取方法的参数
        Object[] args = joinPoint.getArgs();
        for (Object o : args) {
            stringBuilder.append("\n");
            stringBuilder.append("参数：").append(o);
        }

        Context context = (Context) joinPoint.getThis();

        long startTime = System.currentTimeMillis();
        Object response = joinPoint.proceed();
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        stringBuilder.append("\n");
        stringBuilder.append("耗时：").append(duration);
        Log.d("TimeLog", stringBuilder.toString());
        return response;
    }
}
