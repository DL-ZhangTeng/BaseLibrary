package com.zhangteng.utils

import android.graphics.Color

/**
 * 颜色加深处理
 *
 * RGB的值，由alpha（透明度）、red（红）、green（绿）、blue（蓝）构成，
 * Android中我们一般使用它的16进制，
 * 例如："#FFAABBCC",最左边到最右每两个字母就是代表alpha（透明度）、
 * red（红）、green（绿）、blue（蓝）。每种颜色值占一个字节(8位)，值域0~255
 * 所以下面使用移位的方法可以得到每种颜色的值，然后每种颜色值减小一下，在合成RGB颜色，颜色就会看起来深一些了
 */
fun Int.colorBurn(): Int {
    val alpha = this shr 24
    var red = this shr 16 and 0xFF
    var green = this shr 8 and 0xFF
    var blue = this and 0xFF
    red = Math.floor(red * (1 - 0.1)).toInt()
    green = Math.floor(green * (1 - 0.1)).toInt()
    blue = Math.floor(blue * (1 - 0.1)).toInt()
    return Color.rgb(red, green, blue)
}