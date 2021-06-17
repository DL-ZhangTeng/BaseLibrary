package com.zhangteng.base.tree

import java.util.*

/**
 * Created by swing on 2018/6/29.
 */
open class Node {
    private var id = 0
    private var name: String? = null

    /**
     * 当前的级别
     */
    private var level = 0

    /**
     * 是否展开
     */
    private var isExpand = false
    private var icon: Int = 0

    /**
     * 下一级的子Node
     */
    private var children: MutableList<Node?>? = ArrayList()

    /**
     * 父Node
     */
    private var parent: Node? = null

    constructor() {}
    constructor(id: Int, name: String?) : super() {
        this.id = id
        this.name = name
    }

    constructor(id: String?, name: String?) : super() {
        this.id = id?.toInt() ?: 0
        this.name = name
    }

    open fun getIcon(): Int {
        return icon
    }

    open fun setIcon(icon: Int) {
        this.icon = icon
    }

    open fun getId(): Int {
        return id
    }

    open fun setId(id: Int) {
        this.id = id
    }

    open fun getName(): String? {
        return name
    }

    open fun setName(name: String?) {
        this.name = name
    }

    open fun isExpand(): Boolean {
        return isExpand
    }

    /**
     * 设置展开
     *
     * @param isExpand
     */
    open fun setExpand(isExpand: Boolean) {
        this.isExpand = isExpand
        if (!isExpand && children != null) {
            for (node in children!!) {
                node?.setExpand(isExpand)
            }
        }
    }

    open fun getChildren(): MutableList<Node?>? {
        return children
    }

    open fun setChildren(children: MutableList<Node?>?) {
        this.children = children
    }

    open fun getParent(): Node? {
        return parent
    }

    open fun setParent(parent: Node?) {
        this.parent = parent
    }

    /**
     * 是否为跟节点
     *
     * @return
     */
    open fun isRoot(): Boolean {
        return parent == null
    }

    /**
     * 判断父节点是否展开
     *
     * @return
     */
    open fun isParentExpand(): Boolean {
        return parent?.isExpand() ?: false
    }

    /**
     * 是否是叶子界点
     *
     * @return
     */
    open fun isLeaf(): Boolean {
        return children?.size == 0
    }

    /**
     * 获取level
     */
    open fun getLevel(): Int {
        return if (parent == null) 0 else parent!!.getLevel() + 1
    }

    open fun setLevel(level: Int) {
        this.level = level
    }
}