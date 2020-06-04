package com.zhangteng.base.widget;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.zhangteng.base.bean.GroupInfo;

/**
 * Created by swing on 2018/4/12.
 */
public class ItemStickyDecoration extends RecyclerView.ItemDecoration {
    private final Paint.FontMetrics mFontMetrics;
    private int mStickyHeight = 60;
    private int textSize = 16;
    private int padding = 16;
    private Paint mTextPaint;
    private Paint mPaint;
    private GroupInfoInterface groupInfoInterface;

    public ItemStickyDecoration(GroupInfoInterface groupInfoInterface) {
        super();
        this.groupInfoInterface = groupInfoInterface;
        mTextPaint = new TextPaint();
        mTextPaint.setColor(Color.parseColor("#989898"));
        mTextPaint.setTextSize(textSize);
        mFontMetrics = mTextPaint.getFontMetrics();

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.parseColor("#e8e8e8"));
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View view = parent.getChildAt(i);
            int index = parent.getChildAdapterPosition(view);
            if (groupInfoInterface != null) {
                GroupInfo groupinfo = groupInfoInterface.getGroupInfo(index);
                int left = parent.getPaddingLeft();
                int right = parent.getWidth() - parent.getPaddingRight();
                //屏幕上第一个可见的 ItemView 时，i == 0;
                if (i != 0) {
                    //只有组内的第一个ItemView之上才绘制
                    if (groupinfo.isFirst()) {
                        int top = view.getTop() - mStickyHeight;
                        int bottom = view.getTop();
                        drawStickyHeader(c, groupinfo, left, top, right, bottom);
                    } else {
                        int top = view.getTop() - 1;
                        int bottom = view.getTop();
                        c.drawRect(left, top, right, bottom, mPaint);
                    }
                } else {
                    //当 ItemView 是屏幕上第一个可见的View 时，不管它是不是组内第一个View
                    //它都需要绘制它对应的 StickyHeader。
                    // 还要判断当前的 ItemView 是不是它组内的最后一个 View
                    int top = parent.getPaddingTop();
                    if (groupinfo.isLast()) {
                        int suggestTop = view.getBottom() - mStickyHeight;
                        // 当 ItemView 与 Header 底部平齐的时候，判断 Header 的顶部是否小于
                        // parent 顶部内容开始的位置，如果小于则对 Header.top 进行位置更新，
                        //否则将继续保持吸附在 parent 的顶部
                        if (suggestTop < top) {
                            top = suggestTop;
                        }
                    }
                    int bottom = top + mStickyHeight;
                    drawStickyHeader(c, groupinfo, left, top, right, bottom);
                }

            }
        }
    }

    private void drawStickyHeader(Canvas c, GroupInfo groupinfo, int left, int top, int right, int bottom) {
        //绘制Header
        c.drawRect(left, top, right, bottom, mPaint);
        float titleX = left + padding;
        float titleY = bottom - mFontMetrics.descent - padding / 2;
        //绘制Title
        c.drawText(TextUtils.isEmpty(groupinfo.getTitle()) ? "" : groupinfo.getTitle(), titleX, titleY, mTextPaint);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int position = parent.getChildAdapterPosition(view);
        GroupInfo groupInfo = groupInfoInterface.getGroupInfo(position);
        if (groupInfo != null && groupInfo.isFirst()) {
            outRect.top = mStickyHeight;
        } else {
            outRect.top = 0;
        }
        Log.e("top", String.valueOf(outRect.top) + position);
    }

    public void setmStickyHeight(int mStickyHeight) {
        this.mStickyHeight = mStickyHeight;
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
        mTextPaint.setTextSize(textSize);
    }

    public void setTextPadding(int padding) {
        this.padding = padding;
    }

    public interface GroupInfoInterface {
        GroupInfo getGroupInfo(int position);
    }

}
