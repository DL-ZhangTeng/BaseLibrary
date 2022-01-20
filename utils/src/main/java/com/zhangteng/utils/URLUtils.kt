package com.zhangteng.utils

fun String?.isHttpUrl(): Boolean {
    return (null != this && this.length > 6
            && this.substring(0, 7).equals("http://", ignoreCase = true))
}

fun String?.isHttpsUrl(): Boolean {
    return (null != this && this.length > 7
            && this.substring(0, 8).equals("https://", ignoreCase = true))
}

fun String?.isNetworkUrl(): Boolean {
    return if (this == null || this.isEmpty()) {
        false
    } else isHttpUrl() || isHttpsUrl()
}