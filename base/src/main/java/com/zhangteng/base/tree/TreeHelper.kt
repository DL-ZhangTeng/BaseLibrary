package com.zhangteng.base.tree

import com.zhangteng.base.R
import java.util.*
import kotlin.jvm.Throws

/**
 * Created by swing on 2018/6/29.
 */
object TreeHelper {
    /**
     * 传入我们的普通bean，转化为排序后的Node
     *
     * @param datas
     * @param defaultExpandLevel
     * @return
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    @Throws(IllegalArgumentException::class, IllegalAccessException::class)
    fun <T> getSortedNodes(datas: MutableList<T?>?, defaultExpandLevel: Int): MutableList<Node?>? {
        val result: MutableList<Node?> = ArrayList()
        // 将用户数据转化为List<Node>
        val nodes = convetData2Node(datas)
        // 拿到根节点
        val rootNodes = getRootNodes(nodes)
        if (rootNodes != null) {
            // 排序以及设置Node间关系
            for (node in rootNodes) {
                addNode(result, node, defaultExpandLevel, 1)
            }
        }
        return result
    }

    /**
     * 过滤出所有可见的Node
     *
     * @param nodes
     * @return
     */
    fun filterVisibleNode(nodes: MutableList<Node?>?): MutableList<Node?>? {
        val result: MutableList<Node?> = ArrayList()
        if (nodes != null) {
            for (node in nodes) {
                // 如果为跟节点，或者上层目录为展开状态
                if (node?.isRoot() == true || node?.isParentExpand() == true) {
                    setNodeIcon(node)
                    result.add(node)
                }
            }
        }
        return result
    }

    /**
     * 将我们的数据转化为树的节点
     *
     * @param datas
     * @return
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     */
    @Throws(IllegalArgumentException::class, IllegalAccessException::class)
    private fun <T> convetData2Node(datas: MutableList<T?>?): MutableList<Node?> {
        val nodes: MutableList<Node?> = ArrayList()
        var nodeChildren: MutableList<Node?>? = ArrayList()
        var nodeParent: Node? = null
        var node: Node? = null
        if (datas != null) {
            for (t in datas) {
                if (t == null) continue
                var id: String? = null
                var label: String? = null
                var parent: T? = null
                var children: MutableList<T?> = ArrayList()
                val clazz: Class<out Any?> = t!!::class.java
                val declaredFields = clazz.declaredFields
                for (f in declaredFields) {
                    if (f.getAnnotation(TreeNodeId::class.java) != null) {
                        f.isAccessible = true
                        id = f[t] as String
                    }
                    if (f.getAnnotation(TreeNodeParent::class.java) != null) {
                        f.isAccessible = true
                        parent = f[t] as T
                        nodeParent = if (parent != null) {
                            convetData2Node(parent)
                        } else {
                            null
                        }
                    }
                    if (f.getAnnotation(TreeNodeLabel::class.java) != null) {
                        f.isAccessible = true
                        label = f[t] as String
                    }
                    if (f.getAnnotation(TreeNodeChildren::class.java) != null) {
                        f.isAccessible = true
                        children = f[t] as MutableList<T?>
                        if (children != null) {
                            nodeChildren = convetData2Node<T?>(children)
                        } else {
                            nodeChildren?.clear()
                        }
                    }
                }
                node = Node(id, label)
                if (nodeParent != null) {
                    if (node.getParent() == null) {
                        node.setParent(nodeParent)
                    }
                    if (nodeParent.getChildren()?.contains(node) == false) {
                        nodeParent.getChildren()?.add(node)
                    }
                }
                if (nodeChildren != null) {
                    for (child in nodeChildren) {
                        if (child?.getParent() == null) {
                            child?.setParent(node)
                        }
                        if (node.getChildren()?.contains(child) == false) {
                            node.getChildren()?.add(child)
                        }
                    }
                }
                if (!nodes.contains(node)) {
                    nodes.add(node)
                }

            }
        }
        // 设置图片
        for (n in nodes) {
            setNodeIcon(n)
        }
        return nodes
    }

    /**
     * 将单个数据转化为树的节点
     *
     * @param data
     * @return
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     */
    @Throws(IllegalArgumentException::class, IllegalAccessException::class)
    private fun <T> convetData2Node(data: T?): Node? {
        if (data == null) return null
        var nodeChildren: MutableList<Node?>? = ArrayList()
        var nodeParent: Node? = null
        var node: Node? = null
        var id: String? = null
        var label: String? = null
        var parent: T? = null
        var children: MutableList<T?> = ArrayList()
        val clazz: Class<out Any?> = data!!::class.java
        val declaredFields = clazz.declaredFields
        for (f in declaredFields) {
            if (f.getAnnotation(TreeNodeId::class.java) != null) {
                f.isAccessible = true
                id = f[data] as String
            }
            if (f.getAnnotation(TreeNodeParent::class.java) != null) {
                f.isAccessible = true
                parent = f[data] as T
                if (parent != null) nodeParent = convetData2Node<T?>(parent)
            }
            if (f.getAnnotation(TreeNodeLabel::class.java) != null) {
                f.isAccessible = true
                label = f[data] as String
            }
            if (f.getAnnotation(TreeNodeChildren::class.java) != null) {
                f.isAccessible = true
                children = f[data] as MutableList<T?>
                if (children != null) {
                    nodeChildren = convetData2Node(children)
                }
            }
        }
        node = Node(id, label)
        if (nodeParent != null) {
            if (node.getParent() == null) {
                node.setParent(nodeParent)
            }
            if (nodeParent.getChildren()?.contains(node) == false) {
                nodeParent.getChildren()?.add(node)
            }
        }
        if (nodeChildren != null) {
            for (child in nodeChildren) {
                if (child?.getParent() == null) {
                    child?.setParent(node)
                }
                if (node.getChildren()?.contains(child) == false) {
                    node.getChildren()?.add(child)
                }
            }
        }
        // 设置图片
        setNodeIcon(node)
        return node
    }

    private fun getRootNodes(nodes: MutableList<Node?>?): MutableList<Node?>? {
        val root: MutableList<Node?> = ArrayList()
        if (nodes != null) {
            for (node in nodes) {
                if (node != null && node.isRoot()) {
                    root.add(node)
                }
            }
        }
        return root
    }

    /**
     * 把一个节点上的所有的内容都挂上去
     */
    private fun addNode(
            nodes: MutableList<Node?>?, node: Node?,
            defaultExpandLeval: Int, currentLevel: Int,
    ) {
        nodes?.add(node)
        //设置默认展开
        /*if (defaultExpandLeval >= currentLevel) {
            node.setExpand(true);
        }*/if (node == null || node.isLeaf()) {
            return
        }
        if (node.getChildren() != null) {
            for (i in node.getChildren()!!.indices) {
                addNode(nodes, node.getChildren()!![i] as Node, defaultExpandLeval,
                        currentLevel + 1)
            }
        }
    }

    /**
     * 设置节点的图标
     *
     * @param node
     */
    private fun setNodeIcon(node: Node?) {
        node?.let {
            if (it.getChildren()?.size ?: 0 > 0 && it.isExpand()) {
                it.setIcon(R.mipmap.ic_more_bottom_gray)
            } else if (it.getChildren()?.size ?: 0 > 0 && !it.isExpand()) {
                it.setIcon(R.mipmap.ic_more_right_gray)
            } else {
                it.setIcon(-1)
            }
        }
    }
}