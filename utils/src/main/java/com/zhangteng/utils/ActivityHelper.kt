package com.zhangteng.utils

import android.app.Activity
import android.content.Intent
import android.os.Bundle

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
 * 下一个页面进入动画(入栈动画)
 */
fun getNextActivityAnimShow(): Int {
    return R.anim.self_enter_anim
}

/**
 * 当前页面退出动画(入栈动画)
 */
fun getCurrentActivityAnimShow(): Int {
    return R.anim.self_exit_anim
}

/**
 * 上一个页面进入动画(出栈动画)
 */
fun getPreviousActivityAnimClose(): Int {
    return R.anim.self_pop_enter_anim
}

/**
 * 当前页面退出动画(出栈动画)
 */
fun getCurrentActivityAnimClose(): Int {
    return R.anim.self_pop_exit_anim
}