package com.zhangteng.baselibrary.ui.mvvmdb

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.zhangteng.base.base.BaseAdapter
import com.zhangteng.baselibrary.R
import com.zhangteng.baselibrary.databinding.ItemUsedwebBinding
import com.zhangteng.baselibrary.http.entity.ArticlesBean

class MeWebAdapter : BaseAdapter<ArticlesBean, BaseDataBindingHolder<ItemUsedwebBinding>>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseDataBindingHolder<ItemUsedwebBinding> {
        return BaseDataBindingHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_usedweb, parent,false)
        )
    }

    override fun onBindViewHolder(
        holder: BaseDataBindingHolder<ItemUsedwebBinding>,
        position: Int
    ) {
        holder.dataBinding?.itemData = data?.get(position)
        holder.dataBinding?.executePendingBindings()
    }
}

open class BaseDataBindingHolder<BD : ViewDataBinding>(view: View) :
    BaseAdapter.DefaultViewHolder(view) {

    val dataBinding = DataBindingUtil.bind<BD>(view)
}