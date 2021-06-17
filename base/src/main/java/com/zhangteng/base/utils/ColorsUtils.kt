package com.zhangteng.base.utils

import android.graphics.Color

/**
 * 颜色工具类 包括常用的色值
 */
class ColorsUtils private constructor() {
    companion object {
        /**
         * 白色
         */
        const val WHITE = -0x1

        /**
         * 白色 - 半透明
         */
        const val WHITE_TRANSLUCENT = -0x7f000001

        /**
         * 黑色
         */
        const val BLACK = -0x1000000

        /**
         * 黑色 - 半透明
         */
        const val BLACK_TRANSLUCENT = -0x80000000

        /**
         * 透明
         */
        const val TRANSPARENT = 0x00000000

        /**
         * 红色
         */
        const val RED = -0x10000

        /**
         * 红色 - 半透明
         */
        const val RED_TRANSLUCENT = -0x7f010000

        /**
         * 红色 - 深的
         */
        const val RED_DARK = -0x750000

        /**
         * 红色 - 深的 - 半透明
         */
        const val RED_DARK_TRANSLUCENT = -0x7f750000

        /**
         * 绿色
         */
        const val GREEN = -0xff0100

        /**
         * 绿色 - 半透明
         */
        const val GREEN_TRANSLUCENT = -0x7fff0100

        /**
         * 绿色 - 深的
         */
        const val GREEN_DARK = -0xffcd00

        /**
         * 绿色 - 深的 - 半透明
         */
        const val GREEN_DARK_TRANSLUCENT = -0x7fffcd00

        /**
         * 绿色 - 浅的
         */
        const val GREEN_LIGHT = -0x330034

        /**
         * 绿色 - 浅的 - 半透明
         */
        const val GREEN_LIGHT_TRANSLUCENT = -0x7f330034

        /**
         * 蓝色
         */
        const val BLUE = -0xffff01

        /**
         * 蓝色 - 半透明
         */
        const val BLUE_TRANSLUCENT = -0x7fffff01

        /**
         * 蓝色 - 深的
         */
        const val BLUE_DARK = -0xffff75

        /**
         * 蓝色 - 深的 - 半透明
         */
        const val BLUE_DARK_TRANSLUCENT = -0x7fffff75

        /**
         * 蓝色 - 浅的
         */
        const val BLUE_LIGHT = -0xc95a1d

        /**
         * 蓝色 - 浅的 - 半透明
         */
        const val BLUE_LIGHT_TRANSLUCENT = -0x7fc95a1d

        /**
         * 天蓝
         */
        const val SKYBLUE = -0x783115

        /**
         * 天蓝 - 半透明
         */
        const val SKYBLUE_TRANSLUCENT = -0x7f783115

        /**
         * 天蓝 - 深的
         */
        const val SKYBLUE_DARK = -0xff4001

        /**
         * 天蓝 - 深的 - 半透明
         */
        const val SKYBLUE_DARK_TRANSLUCENT = -0x7fff4001

        /**
         * 天蓝 - 浅的
         */
        const val SKYBLUE_LIGHT = -0x783106

        /**
         * 天蓝 - 浅的 - 半透明
         */
        const val SKYBLUE_LIGHT_TRANSLUCENT = -0x7f783106

        /**
         * 灰色
         */
        const val GRAY = -0x69696a

        /**
         * 灰色 - 半透明
         */
        const val GRAY_TRANSLUCENT = -0x7f69696a

        /**
         * 灰色 - 深的
         */
        const val GRAY_DARK = -0x565657

        /**
         * 灰色 - 深的 - 半透明
         */
        const val GRAY_DARK_TRANSLUCENT = -0x7f565657

        /**
         * 灰色 - 暗的
         */
        const val GRAY_DIM = -0x969697

        /**
         * 灰色 - 暗的 - 半透明
         */
        const val GRAY_DIM_TRANSLUCENT = -0x7f969697

        /**
         * 灰色 - 浅的
         */
        const val GRAY_LIGHT = -0x2c2c2d

        /**
         * 灰色 - 浅的 - 半透明
         */
        const val GRAY_LIGHT_TRANSLUCENT = -0x7f2c2c2d

        /**
         * 橙色
         */
        const val ORANGE = -0x5b00

        /**
         * 橙色 - 半透明
         */
        const val ORANGE_TRANSLUCENT = -0x7f005b00

        /**
         * 橙色 - 深的
         */
        const val ORANGE_DARK = -0x7800

        /**
         * 橙色 - 深的 - 半透明
         */
        const val ORANGE_DARK_TRANSLUCENT = -0x7f007800

        /**
         * 橙色 - 浅的
         */
        const val ORANGE_LIGHT = -0x44cd

        /**
         * 橙色 - 浅的 - 半透明
         */
        const val ORANGE_LIGHT_TRANSLUCENT = -0x7f0044cd

        /**
         * 金色
         */
        const val GOLD = -0x2900

        /**
         * 金色 - 半透明
         */
        const val GOLD_TRANSLUCENT = -0x7f002900

        /**
         * 粉色
         */
        const val PINK = -0x3f35

        /**
         * 粉色 - 半透明
         */
        const val PINK_TRANSLUCENT = -0x7f003f35

        /**
         * 紫红色
         */
        const val FUCHSIA = -0xff01

        /**
         * 紫红色 - 半透明
         */
        const val FUCHSIA_TRANSLUCENT = -0x7f00ff01

        /**
         * 灰白色
         */
        const val GRAYWHITE = -0xd0d0e

        /**
         * 灰白色 - 半透明
         */
        const val GRAYWHITE_TRANSLUCENT = -0x7f0d0d0e

        /**
         * 紫色
         */
        const val PURPLE = -0x7fff80

        /**
         * 紫色 - 半透明
         */
        const val PURPLE_TRANSLUCENT = -0x7f7fff80

        /**
         * 青色
         */
        const val CYAN = -0xff0001

        /**
         * 青色 - 半透明
         */
        const val CYAN_TRANSLUCENT = -0x7fff0001

        /**
         * 青色 - 深的
         */
        const val CYAN_DARK = -0xff7475

        /**
         * 青色 - 深的 - 半透明
         */
        const val CYAN_DARK_TRANSLUCENT = -0x7fff7475

        /**
         * 黄色
         */
        const val YELLOW = -0x100

        /**
         * 黄色 - 半透明
         */
        const val YELLOW_TRANSLUCENT = -0x7f000100

        /**
         * 黄色 - 浅的
         */
        const val YELLOW_LIGHT = -0x20

        /**
         * 黄色 - 浅的 - 半透明
         */
        const val YELLOW_LIGHT_TRANSLUCENT = -0x7f000020

        /**
         * 巧克力色
         */
        const val CHOCOLATE = -0x2d96e2

        /**
         * 巧克力色 - 半透明
         */
        const val CHOCOLATE_TRANSLUCENT = -0x7f2d96e2

        /**
         * 番茄色
         */
        const val TOMATO = -0x9cb9

        /**
         * 番茄色 - 半透明
         */
        const val TOMATO_TRANSLUCENT = -0x7f009cb9

        /**
         * 橙红色
         */
        const val ORANGERED = -0xbb00

        /**
         * 橙红色 - 半透明
         */
        const val ORANGERED_TRANSLUCENT = -0x7f00bb00

        /**
         * 银白色
         */
        const val SILVER = -0x3f3f40

        /**
         * 银白色 - 半透明
         */
        const val SILVER_TRANSLUCENT = -0x7f3f3f40

        /**
         * 高光
         */
        const val HIGHLIGHT = 0x33ffffff

        /**
         * 低光
         */
        const val LOWLIGHT = 0x33000000

        /**
         * 颜色加深处理
         *
         * @param RGBValues RGB的值，由alpha（透明度）、red（红）、green（绿）、blue（蓝）构成，
         * Android中我们一般使用它的16进制，
         * 例如："#FFAABBCC",最左边到最右每两个字母就是代表alpha（透明度）、
         * red（红）、green（绿）、blue（蓝）。每种颜色值占一个字节(8位)，值域0~255
         * 所以下面使用移位的方法可以得到每种颜色的值，然后每种颜色值减小一下，在合成RGB颜色，颜色就会看起来深一些了
         * @return
         */
        fun colorBurn(RGBValues: Int): Int {
            val alpha = RGBValues shr 24
            var red = RGBValues shr 16 and 0xFF
            var green = RGBValues shr 8 and 0xFF
            var blue = RGBValues and 0xFF
            red = Math.floor(red * (1 - 0.1)).toInt()
            green = Math.floor(green * (1 - 0.1)).toInt()
            blue = Math.floor(blue * (1 - 0.1)).toInt()
            return Color.rgb(red, green, blue)
        }
    }

    /**
     * Don't let anyone instantiate this class.
     */
    init {
        throw Error("Do not need instantiate!")
    }
}