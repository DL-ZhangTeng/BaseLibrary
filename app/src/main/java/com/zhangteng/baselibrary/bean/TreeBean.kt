package com.zhangteng.baselibrary.bean

import com.zhangteng.base.tree.TreeNodeChildren
import com.zhangteng.base.tree.TreeNodeId
import com.zhangteng.base.tree.TreeNodeLabel
import com.zhangteng.base.tree.TreeNodeParent

class TreeBean {
    @TreeNodeId
    var id: String? = null

    @TreeNodeLabel
    var label: String? = null

    @TreeNodeChildren
    var children: ArrayList<TreeBean?>? = null

    @TreeNodeParent
    var parent: TreeBean? = null
}