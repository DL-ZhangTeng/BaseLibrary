package com.zhangteng.base.bean;

/**
 * Created by swing on 2018/4/12.
 */
public class GroupInfo {
    /**
     * 按26个英文字母分组
     * 存储26个组的总数
     * 多的一个备用
     */
    public static int[] totals = new int[27];
    private int groupNum;
    private String title;
    private int position;
    private int total;

    public GroupInfo() {
    }

    public GroupInfo(int groupNum, String title, int position, int total) {
        this.groupNum = groupNum;
        this.title = title;
        this.position = position;
        this.total = total;
    }

    public GroupInfo(Long groupNum, String title, Long position, Long total) {
        this.groupNum = groupNum.intValue();
        this.title = title;
        this.position = position.intValue();
        this.total = total.intValue();
    }

    public static void initTotals() {
        for (int i = 0; i < totals.length; i++) {
            totals[i] = 0;
        }
    }

    public boolean isFirst() {
        return position == 0;
    }

    public boolean isLast() {
        return position == total - 1;
    }

    public int getGroupNum() {
        return groupNum;
    }

    public void setGroupNum(int groupNum) {
        this.groupNum = groupNum;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
