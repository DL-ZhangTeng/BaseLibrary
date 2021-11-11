package com.zhangteng.base.base

import android.app.Activity
import android.app.Application
import com.zhangteng.base.utils.isDebug
import java.util.*

/**
 * Created by swing on 2018/8/2.
 * 应用启动时需要初始化的操作可继承BaseApplication实现相关方法
 */
abstract class BaseApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    /**
     * Module application初始化
     */
    abstract fun initModuleApp(application: Application?)

    /**
     * Module 数据初始化
     */
    abstract fun initModuleData(application: Application?)

    companion object {
        var instance: BaseApplication? = null

        /**
         * 获取正在运行的activity
         */
        fun getActivitiesByApplication(): MutableList<Activity?>? {
            var list: MutableList<Activity?>? = ArrayList()
            try {
                val applicationClass: Class<Application> = Application::class.java
                val mLoadedApkField = applicationClass.getDeclaredField("mLoadedApk")
                mLoadedApkField.isAccessible = true
                val mLoadedApk = mLoadedApkField[instance]
                val mLoadedApkClass: Class<*> = mLoadedApk.javaClass
                val mActivityThreadField = mLoadedApkClass.getDeclaredField("mActivityThread")
                mActivityThreadField.isAccessible = true
                val mActivityThread = mActivityThreadField[mLoadedApk]
                val mActivityThreadClass: Class<*> = mActivityThread.javaClass
                val mActivitiesField = mActivityThreadClass.getDeclaredField("mActivities")
                mActivitiesField.isAccessible = true
                val mActivities = mActivitiesField[mActivityThread]
                // 注意这里一定写成Map，低版本这里用的是HashMap，高版本用的是ArrayMap
                if (mActivities is MutableMap<*, *>) {
                    for ((_, value) in mActivities) {
                        val activityClientRecordClass: Class<*> = value!!::class.java
                        val activityField = activityClientRecordClass.getDeclaredField("activity")
                        activityField.isAccessible = true
                        val o = activityField[value]
                        list?.add(o as Activity)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                list = null
            }
            return list
        }
    }
}