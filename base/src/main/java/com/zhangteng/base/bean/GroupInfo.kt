package com.zhangteng.base.bean

/**
 * Created by swing on 2018/4/12.
 */
class GroupInfo {
    private var groupNum = 0
    private var title: String? = null
    private var position = 0
    private var total = 0

    constructor() {}
    constructor(groupNum: Int, title: String?, position: Int, total: Int) {
        this.groupNum = groupNum
        this.title = title
        this.position = position
        this.total = total
    }

    constructor(groupNum: Long?, title: String?, position: Long?, total: Long?) {
        this.groupNum = groupNum?.toInt() ?: 0
        this.title = title
        this.position = position?.toInt() ?: 0
        this.total = total?.toInt() ?: 0
    }

    fun isFirst(): Boolean {
        return position == 0
    }

    fun isLast(): Boolean {
        return position == total - 1
    }

    fun getGroupNum(): Int {
        return groupNum
    }

    fun setGroupNum(groupNum: Int) {
        this.groupNum = groupNum
    }

    fun getTitle(): String? {
        return title
    }

    fun setTitle(title: String?) {
        this.title = title
    }

    fun getPosition(): Int {
        return position
    }

    fun setPosition(position: Int) {
        this.position = position
    }

    fun getTotal(): Int {
        return total
    }

    fun setTotal(total: Int) {
        this.total = total
    }

    companion object {
        /**
         * 按26个英文字母分组
         * 存储26个组的总数
         * 多的一个备用
         */
        var totals: IntArray = IntArray(27)
        fun initTotals() {
            for (i in totals.indices) {
                totals[i] = 0
            }
        }
    }
}