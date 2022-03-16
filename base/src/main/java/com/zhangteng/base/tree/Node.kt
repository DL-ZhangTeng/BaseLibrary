package com.zhangteng.base.tree

/**
 * Created by swing on 2018/6/29.
 */
open class Node<T> {
    private var id: String? = null
    private var name: String? = null

    /**
     * 父Node
     */
    private var parent: Node<T?>? = null

    /**
     * 下一级的子Node
     */
    private var children: MutableList<Node<T?>?>? = ArrayList()

    /**
     * 当前的级别
     */
    private var level = 0

    /**
     * 是否展开
     */
    private var isExpand = false

    /**
     * 展开折叠时显示的图标
     */
    private var icon: Int = 0

    /**
     * 实际值
     */
    private var actualValue: T? = null

    constructor() {}

    constructor(id: String?, name: String?) : super() {
        this.id = id
        this.name = name
    }

    open fun getId(): String? {
        return id
    }

    open fun setId(id: String?) {
        this.id = id
    }

    open fun getName(): String? {
        return name
    }

    open fun setName(name: String?) {
        this.name = name
    }

    open fun getIcon(): Int {
        return icon
    }

    open fun setIcon(icon: Int) {
        this.icon = icon
    }

    open fun getActualValue(): T? {
        return actualValue
    }

    open fun setActualValue(actualValue: T?) {
        this.actualValue = actualValue
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

    open fun getChildren(): MutableList<Node<T?>?>? {
        return children
    }

    open fun setChildren(children: MutableList<Node<T?>?>?) {
        this.children = children
    }

    open fun getParent(): Node<T?>? {
        return parent
    }

    open fun setParent(parent: Node<T?>?) {
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