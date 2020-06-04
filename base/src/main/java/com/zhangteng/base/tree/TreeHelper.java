package com.zhangteng.base.tree;

import com.zhangteng.base.R;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by swing on 2018/6/29.
 */
public class TreeHelper {
    /**
     * 传入我们的普通bean，转化为排序后的Node
     *
     * @param datas
     * @param defaultExpandLevel
     * @return
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    public static <T> List<Node> getSortedNodes(List<T> datas, int defaultExpandLevel) throws IllegalArgumentException, IllegalAccessException {
        List<Node> result = new ArrayList<Node>();
        // 将用户数据转化为List<Node>
        List<Node> nodes = convetData2Node(datas);
        // 拿到根节点
        List<Node> rootNodes = getRootNodes(nodes);
        // 排序以及设置Node间关系
        for (Node node : rootNodes) {
            addNode(result, node, defaultExpandLevel, 1);
        }
        return result;
    }

    /**
     * 过滤出所有可见的Node
     *
     * @param nodes
     * @return
     */
    public static List<Node> filterVisibleNode(List<Node> nodes) {
        List<Node> result = new ArrayList<Node>();
        for (Node node : nodes) {
            // 如果为跟节点，或者上层目录为展开状态
            if (node.isRoot() || node.isParentExpand()) {
                setNodeIcon(node);
                result.add(node);
            }
        }
        return result;
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
    private static <T> List<Node> convetData2Node(List<T> datas) throws IllegalArgumentException, IllegalAccessException {
        List<Node> nodes = new ArrayList<Node>();
        List<Node> nodeChildren = new ArrayList<Node>();
        Node nodeParent = null;
        Node node = null;

        for (T t : datas) {
            String id = null;
            String label = null;
            T parent = null;
            List<T> children = new ArrayList<>();
            Class<? extends Object> clazz = t.getClass();
            Field[] declaredFields = clazz.getDeclaredFields();
            for (Field f : declaredFields) {
                if (f.getAnnotation(TreeNodeId.class) != null) {
                    f.setAccessible(true);
                    id = (String) f.get(t);
                }
                if (f.getAnnotation(TreeNodeParent.class) != null) {
                    f.setAccessible(true);
                    parent = (T) f.get(t);
                    if (parent != null) {
                        nodeParent = convetData2Node(parent);
                    } else {
                        nodeParent = null;
                    }
                }
                if (f.getAnnotation(TreeNodeLabel.class) != null) {
                    f.setAccessible(true);
                    label = (String) f.get(t);
                }
                if (f.getAnnotation(TreeNodeChildren.class) != null) {
                    f.setAccessible(true);
                    children = (List<T>) f.get(t);
                    if (children != null) {
                        nodeChildren = convetData2Node(children);
                    } else {
                        nodeChildren.clear();
                    }
                }
            }
            node = new Node(id, label);
            if (nodeParent != null) {
                if (node.getParent() == null) {
                    node.setParent(nodeParent);
                }
                if (!nodeParent.getChildren().contains(node)) {
                    nodeParent.getChildren().add(node);
                }
            }
            for (Node child : nodeChildren) {
                if (child.getParent() == null) {
                    child.setParent(node);
                }
                if (!node.getChildren().contains(child)) {
                    node.getChildren().add(child);
                }
            }
            if (!nodes.contains(node)) {
                nodes.add(node);
            }
        }

        // 设置图片
        for (Node n : nodes) {
            setNodeIcon(n);
        }
        return nodes;
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
    private static <T> Node convetData2Node(T data) throws IllegalArgumentException, IllegalAccessException {
        List<Node> nodeChildren = new ArrayList<Node>();
        Node nodeParent = null;
        Node node = null;
        String id = null;
        String label = null;
        T parent = null;
        List<T> children = new ArrayList<>();
        Class<? extends Object> clazz = data.getClass();
        Field[] declaredFields = clazz.getDeclaredFields();
        for (Field f : declaredFields) {
            if (f.getAnnotation(TreeNodeId.class) != null) {
                f.setAccessible(true);
                id = (String) f.get(data);
            }
            if (f.getAnnotation(TreeNodeParent.class) != null) {
                f.setAccessible(true);
                parent = (T) f.get(data);
                if (parent != null)
                    nodeParent = convetData2Node(parent);
            }
            if (f.getAnnotation(TreeNodeLabel.class) != null) {
                f.setAccessible(true);
                label = (String) f.get(data);
            }
            if (f.getAnnotation(TreeNodeChildren.class) != null) {
                f.setAccessible(true);
                children = (List<T>) f.get(data);
                if (children != null) {
                    nodeChildren = convetData2Node(children);
                }
            }
        }
        node = new Node(id, label);
        if (nodeParent != null) {
            if (node.getParent() == null) {
                node.setParent(nodeParent);
            }
            if (!nodeParent.getChildren().contains(node)) {
                nodeParent.getChildren().add(node);
            }
        }
        for (Node child : nodeChildren) {
            if (child.getParent() == null) {
                child.setParent(node);
            }
            if (!node.getChildren().contains(child)) {
                node.getChildren().add(child);
            }
        }
        // 设置图片
        setNodeIcon(node);
        return node;
    }

    private static List<Node> getRootNodes(List<Node> nodes) {
        List<Node> root = new ArrayList<Node>();
        for (Node node : nodes) {
            if (node.isRoot()) {
                root.add(node);
            }
        }
        return root;
    }

    /**
     * 把一个节点上的所有的内容都挂上去
     */
    private static void addNode(List<Node> nodes, Node node,
                                int defaultExpandLeval, int currentLevel) {

        nodes.add(node);
        //设置默认展开
        /*if (defaultExpandLeval >= currentLevel) {
            node.setExpand(true);
        }*/

        if (node.isLeaf()) {
            return;
        }
        for (int i = 0; i < node.getChildren().size(); i++) {
            addNode(nodes, (Node) node.getChildren().get(i), defaultExpandLeval,
                    currentLevel + 1);
        }
    }

    /**
     * 设置节点的图标
     *
     * @param node
     */
    private static void setNodeIcon(Node node) {
        if (node.getChildren().size() > 0 && node.isExpand()) {
            node.setIcon(R.mipmap.ic_more_bottom_gray);
        } else if (node.getChildren().size() > 0 && !node.isExpand()) {
            node.setIcon(R.mipmap.ic_more_right_gray);
        } else {
            node.setIcon(-1);
        }

    }

}
