package com.zhangteng.base.utils

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.zhangteng.base.R

/**
 * Created by swing on 2017/11/4.
 */
object ActivityHelper {
    fun jumpToActivity(activity: Activity?, cls: Class<*>?, code: Int) {
        if (activity == null || cls == null) return
        val intent = Intent()
        intent.setClass(activity, cls)
        activity.startActivity(intent)
        anim(activity, code)
    }

    fun jumpToActivityWithBundle(activity: Activity?, cls: Class<*>?, bundle: Bundle?, code: Int) {
        if (activity == null || cls == null || bundle == null) return
        val intent = Intent()
        intent.setClass(activity, cls)
        intent.putExtras(bundle)
        activity.startActivity(intent)
        anim(activity, code)
    }

    fun jumpActivityWithBundle(activity: Activity?, cls: Class<*>?, bundle: Bundle?, code: Int) {
        if (activity == null || cls == null || bundle == null) return
        val intent = Intent()
        intent.setClass(activity, cls)
        intent.putExtras(bundle)
        activity.startActivity(intent)
        anim(activity, code)
        activity.finish()
    }

    fun jumpActivity(activity: Activity?, cls: Class<*>?, code: Int) {
        if (activity == null || cls == null) return
        val intent = Intent()
        intent.setClass(activity, cls)
        activity.startActivity(intent)
        anim(activity, code)
        activity.finish()
    }

    fun jumpToActivityForParams(activity: Activity?, cls: Class<*>?, key: String?, value: String?, code: Int) {
        if (activity == null || cls == null) return
        val intent = Intent()
        intent.setClass(activity, cls)
        intent.putExtra(key, value)
        activity.startActivity(intent)
        anim(activity, code)
    }

    fun jumpActivityForParams(activity: Activity?, cls: Class<*>?, key: String?, value: String?, code: Int) {
        if (activity == null || cls == null) return
        val intent = Intent()
        intent.setClass(activity, cls)
        intent.putExtra(key, value)
        activity.startActivity(intent)
        anim(activity, code)
        activity.finish()
    }

    fun jumpActivityResult(activity: Activity?, cls: Class<*>?, requestCode: Int, code: Int) {
        if (activity == null || cls == null) return
        val intent = Intent()
        intent.setClass(activity, cls)
        activity.startActivityForResult(intent, requestCode)
        anim(activity, code)
    }

    fun anim(activity: Activity?, code: Int) {
        if (code == 1) {
            setActivityAnimShow(activity)
        } else if (code == 2) {
            setActivityAnimClose(activity)
        }
    }

    //跳转进activity时的动画
    fun setActivityAnimShow(activity: Activity?) {
        if (activity == null) return
        activity.overridePendingTransition(getNextActivityAnimShow(), getCurrentActivityAnimShow())
    }

    //退出activity时的动画
    fun setActivityAnimClose(activity: Activity?) {
        if (activity == null) return
        activity.overridePendingTransition(getPreviousActivityAnimClose(), getCurrentActivityAnimClose())
    }

    /**
     * 下一个活动进入动画
     */
    fun getNextActivityAnimShow(): Int {
        return R.anim.activity_show
    }

    /**
     * 当前活动退出动画（start活动时）
     */
    fun getCurrentActivityAnimShow(): Int {
        return R.anim.activity_show_1
    }

    /**
     * 上一个活动进入动画
     */
    fun getPreviousActivityAnimClose(): Int {
        return R.anim.activity_close_1
    }

    /**
     * 当前活动退出动画（finish活动时）
     */
    fun getCurrentActivityAnimClose(): Int {
        return R.anim.activity_close
    }
}