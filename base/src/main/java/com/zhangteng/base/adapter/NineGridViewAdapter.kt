package com.zhangteng.base.adapter

import android.content.Context
import android.widget.ImageView
import com.zhangteng.base.R
import com.zhangteng.base.bean.PreviewImageInfo
import com.zhangteng.base.widget.NineGridView
import com.zhangteng.base.widget.NineGridImageView
import java.io.Serializable

/**
 * @description: 九宫格图片展示
 * @author: Swing
 * @date: 2021/9/5
 */
abstract class NineGridViewAdapter(
    protected var context: Context,
    var imageInfo: List<PreviewImageInfo?>
) : Serializable {
    /**
     * 如果要实现图片点击的逻辑，重写此方法即可
     *
     * @param context      上下文
     * @param nineGridView 九宫格控件
     * @param index        当前点击图片的的索引
     * @param imageInfo    图片地址的数据集合
     */
    open fun onImageItemClick(
        context: Context,
        nineGridView: NineGridView,
        index: Int,
        imageInfo: List<PreviewImageInfo?>?
    ) {
    }

    /**
     * 生成ImageView容器的方式，默认使用NineGridImageViewWrapper类，即点击图片后，图片会有蒙板效果
     * 如果需要自定义图片展示效果，重写此方法即可
     *
     * @param context 上下文
     * @return 生成的 ImageView
     */
    fun generateImageView(context: Context?): ImageView {
        val imageView = NineGridImageView(context)
        imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        imageView.setImageResource(R.drawable.ic_default_color)
        return imageView
    }

    fun setImageInfoList(imageInfo: List<PreviewImageInfo?>) {
        this.imageInfo = imageInfo
    }
}