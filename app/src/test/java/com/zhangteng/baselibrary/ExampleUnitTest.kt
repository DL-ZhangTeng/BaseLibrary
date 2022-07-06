package com.zhangteng.baselibrary

import org.junit.Assert
import org.junit.Test
import kotlin.math.roundToInt

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see [Testing documentation](http://d.android.com/tools/testing)
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        val packets = splitMoney(10000, 10, 300, 100)
        if (packets is IntArray) {
            var rmb = 0
            packets.forEach {
                rmb += it
                print("$it,")
            }
            println(rmb.toString())
        } else {
            println(packets.toString())
        }
        Assert.assertEquals(4, (2 + 2).toLong())
    }

    /**
     * description 随机红包
     * @param rmb 单位分
     * @param min 最小红包，单位分
     * @param max 最大红包，单位分
     * @param num 红包数量
     * @return 红包数组IntArray或异常信息String
     */
    fun splitMoney(rmb: Int, min: Int, max: Int, num: Int): Any {

        // 最小值向下取整 3.6 取 3
        val numA: Int = Math.floor(rmb / min.toDouble()).toInt()
        // 最大值向上取整 3.6 取 4
        val numB: Int = Math.floor(rmb / max.toDouble()).toInt()

        if (num > numA) {
            return "数量不可多于：${numA}"
        }
        if (num < numB) {
            return "数量不可少于：${numB}"
        }

        val packets = IntArray(num)

        // 固定金额
        if (num == 1) {
            packets[0] = max
        } else {
            // 随机金额
            if (min * num > rmb) {
                return "超出总金额"
            }
            var realMax = max
            // 验证最大红包金额是否正确
            if (num * max < rmb || max + (num - 1) * min > rmb) {
                realMax = rmb - ((num - 1) * min)
            }
            var realRmb = rmb
            var realNum = num
            while (realNum >= 1) {
                realNum--
                val kMin = Math.max(min, realRmb - realNum * realMax)
                val kMax = Math.min(realMax, realRmb - realNum * min)
                val kAvg = realRmb / (realNum + 1)

                //变大因数，rate越大则抽取红包越大，如果为1则红包是最大值的概率最高
                val rate = (1..10000).random() / 10000f
                //相对于均值的最大振幅：rate大于0.5是振幅取最大振幅，增加大红包的获取概率；rate小于0.5是取最小振幅增加小红包的获取概率
                val kDis = if (rate > 0.7) {
                    //获取最大值和最小值的距离之间的最大值
                    Math.max(kAvg - kMin, kMax - kAvg)
                } else {
                    //获取最大值和最小值的距离之间的最小值
                    Math.min(kAvg - kMin, kMax - kAvg)
                }
                //获取0到1之间的随机数与距离kDis相乘得出浮动区间，
                //这使得浮动区间不会超出范围。
                val r = Math.abs((((1..10000).random() / 10000f) - rate)) * kDis

                var k: Int = (kAvg + r).roundToInt()
                if (k > realMax) {
                    k = realMax
                }
                if (k < min) {
                    k = min
                }
                realRmb -= k

                packets[num - realNum - 1] = k
            }
        }
        return packets
    }
}