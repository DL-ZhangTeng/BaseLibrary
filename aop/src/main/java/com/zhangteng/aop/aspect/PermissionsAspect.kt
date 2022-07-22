package com.zhangteng.aop.aspect

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.ContextWrapper
import com.zhangteng.androidpermission.AndroidPermission
import com.zhangteng.androidpermission.callback.Callback
import com.zhangteng.aop.annotation.Permissions
import com.zhangteng.base.widget.CommonDialog
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut
import kotlin.system.exitProcess

/**
 * description: 权限申请切面
 * author: Swing
 * date: 2022/7/22
 */
@Aspect
class PermissionsAspect {
    /**
     * 方法切入点
     */
    @Pointcut("execution(@com.zhangteng.aop.annotation.Permissions * *(..))")
    fun pointCut() {
    }

    /**
     * 在连接点进行方法替换
     */
    @Around("pointCut() && @annotation(permissions)")
    fun joinPoint(joinPoint: ProceedingJoinPoint, permissions: Permissions) {
        val context = joinPoint.getThis() as Context
        val activity = findActivity(context)
        if (activity == null || activity.isFinishing || activity.isDestroyed) {
            return
        }
        AndroidPermission.Buidler()
            .with(activity)
            .permission(*permissions.value)
            .callback(object : Callback {
                override fun success(permissionActivity: Activity?) {
                    try {
                        // 获得权限，执行原方法
                        joinPoint.proceed()
                    } catch (e: Throwable) {
                        e.printStackTrace()
                    }
                }

                override fun failure(permissionActivity: Activity?) {
                    if (permissions.value.contains("android.permission.MANAGE_EXTERNAL_STORAGE")) {
                        CommonDialog(context)
                            .setTitle("权限说明")
                            .setContent("拒绝授权,将会影响您正常使用APP")
                            .setNegativeButton("")
                            .setPositiveButton("退出应用")
                            .setListener(
                                object : CommonDialog.OnCloseListener {
                                    override fun onClick(dialog: Dialog?, confirm: Boolean) {
                                        if (confirm) {
                                            exitProcess(0)
                                        }
                                    }
                                })
                            .show()
                    } else {
                        CommonDialog(context)
                            .setTitle("权限说明")
                            .setContent("拒绝授权,将会影响您正常使用APP")
                            .setNegativeButton("取消")
                            .setPositiveButton("重新获取")
                            .setListener(
                                object : CommonDialog.OnCloseListener {
                                    override fun onClick(dialog: Dialog?, confirm: Boolean) {
                                        joinPoint(joinPoint, permissions)
                                    }
                                })
                            .show()
                    }
                }

                override fun nonExecution(permissionActivity: Activity?) {
                    try {
                        // 获得权限，执行原方法
                        joinPoint.proceed()
                    } catch (e: Throwable) {
                        e.printStackTrace()
                    }
                }

            })
            .build()
            .execute()
    }

    private fun findActivity(context: Context?): Activity? {
        if (context is Activity) {
            return context
        }
        return if (context is ContextWrapper) {
            val wrapper = context as ContextWrapper?
            findActivity(wrapper?.baseContext)
        } else {
            null
        }
    }
}