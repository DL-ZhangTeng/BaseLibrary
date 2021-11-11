package com.zhangteng.base.utils

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.zhangteng.base.R

fun Activity?.jumpToActivity(cls: Class<*>?, code: Int) {
    if (this == null || cls == null) return
    val intent = Intent()
    intent.setClass(this, cls)
    startActivity(intent)
    anim(code)
}

fun Activity?.jumpToActivityWithBundle(cls: Class<*>?, bundle: Bundle?, code: Int) {
    if (this == null || cls == null || bundle == null) return
    val intent = Intent()
    intent.setClass(this, cls)
    intent.putExtras(bundle)
    startActivity(intent)
    anim(code)
}

fun Activity?.jumpActivityWithBundle(cls: Class<*>?, bundle: Bundle?, code: Int) {
    if (this == null || cls == null || bundle == null) return
    val intent = Intent()
    intent.setClass(this, cls)
    intent.putExtras(bundle)
    startActivity(intent)
    anim(code)
    finish()
}

fun Activity?.jumpActivity(cls: Class<*>?, code: Int) {
    if (this == null || cls == null) return
    val intent = Intent()
    intent.setClass(this, cls)
    startActivity(intent)
    anim(code)
    finish()
}

fun Activity?.jumpToActivityForParams(

    cls: Class<*>?,
    key: String?,
    value: String?,
    code: Int
) {
    if (this == null || cls == null) return
    val intent = Intent()
    intent.setClass(this, cls)
    intent.putExtra(key, value)
    startActivity(intent)
    anim(code)
}

fun Activity?.jumpActivityForParams(

    cls: Class<*>?,
    key: String?,
    value: String?,
    code: Int
) {
    if (this == null || cls == null) return
    val intent = Intent()
    intent.setClass(this, cls)
    intent.putExtra(key, value)
    startActivity(intent)
    anim(code)
    finish()
}

fun Activity?.jumpActivityResult(cls: Class<*>?, requestCode: Int, code: Int) {
    if (this == null || cls == null) return
    val intent = Intent()
    intent.setClass(this, cls)
    startActivityForResult(intent, requestCode)
    anim(code)
}

fun Activity?.anim(code: Int) {
    if (code == 1) {
        setActivityAnimShow()
    } else if (code == 2) {
        setActivityAnimClose()
    }
}

//跳转进activity时的动画
fun Activity?.setActivityAnimShow() {
    if (this == null) return
    overridePendingTransition(
        getNextActivityAnimShow(),
        getCurrentActivityAnimShow()
    )
}

//退出activity时的动画
fun Activity?.setActivityAnimClose() {
    if (this == null) return
    overridePendingTransition(
        getPreviousActivityAnimClose(),
        getCurrentActivityAnimClose()
    )
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