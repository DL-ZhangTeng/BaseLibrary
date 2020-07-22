package com.zhangteng.base.adapter;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.collection.SparseArrayCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.zhangteng.base.base.BaseAdapter;

/**
 * 在已有recyclerview基础上无需修改adapter情况下添加头脚视图
 * Created by swing on 2018/5/4.
 */
public abstract class HeaderOrFooterAdapter<T> extends BaseAdapter<T> {

    public static final int BASE_ITEM_TYPE_HEADER = 100000;
    public static final int BASE_ITEM_TYPE_FOOTER = 200000;

    private SparseArrayCompat<Integer> mHeaderViewInts = new SparseArrayCompat<>();
    private SparseArrayCompat<Integer> mFootViewInts = new SparseArrayCompat<>();

    private SparseArrayCompat<View> mHeaderViews = new SparseArrayCompat<>();
    private SparseArrayCompat<View> mFootViews = new SparseArrayCompat<>();

    private RecyclerView.Adapter mInnerAdapter;

    public HeaderOrFooterAdapter(BaseAdapter mInnerAdapter) {
        super(mInnerAdapter.data);
        this.mInnerAdapter = mInnerAdapter;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (mHeaderViewInts.get(viewType) != null) {

            RecyclerView.ViewHolder holder = createHeaderOrFooterViewHolder(parent, mHeaderViewInts.get(viewType));
            mHeaderViews.put(viewType, holder.itemView);
            return holder;

        } else if (mFootViewInts.get(viewType) != null) {
            RecyclerView.ViewHolder holder = createHeaderOrFooterViewHolder(parent, mFootViewInts.get(viewType));
            mFootViews.put(viewType, holder.itemView);
            return holder;
        }
        return mInnerAdapter.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (isHeaderViewPos(position)) {
            onBindHeaderOrFooterViewHolder(holder, getItemViewType(position));
            return;
        }
        if (isFooterViewPos(position)) {
            onBindHeaderOrFooterViewHolder(holder, getItemViewType(position));
            return;
        }
        mInnerAdapter.onBindViewHolder(holder, position - getHeadersCount());
    }

    @Override
    public int getItemCount() {
        return getHeadersCount() + getFootersCount() + getRealItemCount();
    }

    @Override
    public int getItemViewType(int position) {

        if (isHeaderViewPos(position)) {
            return mHeaderViewInts.keyAt(position);
        } else if (isFooterViewPos(position)) {
            return mFootViewInts.keyAt(position - getHeadersCount() - getRealItemCount());
        }
        return mInnerAdapter.getItemViewType(position - getHeadersCount());
    }

    public void notifyHFAdpterItemChanged(int position) {
        notifyItemChanged(position + getHeadersCount());
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        mInnerAdapter.onAttachedToRecyclerView(recyclerView);

        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            final GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            final GridLayoutManager.SpanSizeLookup spanSizeLookup = gridLayoutManager.getSpanSizeLookup();

            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    int viewType = getItemViewType(position);
                    if (mHeaderViewInts.get(viewType) != null) {
                        return gridLayoutManager.getSpanCount();
                    } else if (mFootViewInts.get(viewType) != null) {
                        return gridLayoutManager.getSpanCount();
                    }
                    if (spanSizeLookup != null) {
                        return spanSizeLookup.getSpanSize(position);
                    }
                    return 1;
                }
            });
            gridLayoutManager.setSpanCount(gridLayoutManager.getSpanCount());
        }
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        mInnerAdapter.onViewAttachedToWindow(holder);
        int position = holder.getLayoutPosition();
        if (isHeaderViewPos(position) || isFooterViewPos(position)) {
            ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();

            if (lp != null
                    && lp instanceof StaggeredGridLayoutManager.LayoutParams) {

                StaggeredGridLayoutManager.LayoutParams p =
                        (StaggeredGridLayoutManager.LayoutParams) lp;

                p.setFullSpan(true);
            }
        }
    }

    private boolean isHeaderViewPos(int position) {
        return position < getHeadersCount();
    }

    private boolean isFooterViewPos(int position) {
        return position >= getHeadersCount() + getRealItemCount();
    }

    public void addHeaderView(@LayoutRes int view) {
        hasHeaderOrFooter = true;
        mHeaderViewInts.put(mHeaderViewInts.size() + BASE_ITEM_TYPE_HEADER, view);
    }

    public void addFootView(@LayoutRes int view) {
        hasHeaderOrFooter = true;
        mFootViewInts.put(mFootViewInts.size() + BASE_ITEM_TYPE_FOOTER, view);
    }

    public int getHeadersCount() {
        return mHeaderViewInts.size();
    }

    public int getFootersCount() {
        return mFootViewInts.size();
    }

    private int getRealItemCount() {
        return mInnerAdapter.getItemCount();
    }

    public abstract RecyclerView.ViewHolder createHeaderOrFooterViewHolder(ViewGroup parent, Integer viewInt);

    public abstract void onBindHeaderOrFooterViewHolder(@NonNull RecyclerView.ViewHolder holder, int viewType);

    public View getHeaderViewByType(int viewType) {
        return mHeaderViews.get(viewType);
    }

    public View getFooterViewByType(int viewType) {
        return mFootViews.get(viewType);
    }

    public View getHeaderViewByPos(int position) {
        return mHeaderViews.get(getItemViewType(position));
    }

    public View getFooterViewByPos(int position) {
        return mFootViews.get(getItemViewType(position));
    }
}
