package com.zhangteng.utils

import android.util.Log

// 是否需要打印log，可以在application的onCreate函数里面初始化
var isDebug = true

// 下面四个是默认tag的函数
fun String?.i() {
    if (isDebug && this != null) Log.i("BaseLibraryLog", this)
}

fun String?.d() {
    if (isDebug && this != null) Log.d("BaseLibraryLog", this)
}

fun String?.e() {
    if (isDebug && this != null) Log.e("BaseLibraryLog", this)
}

fun String?.v() {
    if (isDebug && this != null) Log.v("BaseLibraryLog", this)
}

fun String?.i(tag: String?) {
    if (isDebug && this != null) Log.i(tag, this)
}

fun String?.d(tag: String?) {
    if (isDebug && this != null) Log.d(tag, this)
}

fun String?.e(tag: String?) {
    if (isDebug && this != null) Log.e(tag, this)
}

fun String?.v(tag: String?) {
    if (isDebug && this != null) Log.v(tag, this)
}