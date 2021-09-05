package com.zhangteng.base.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.zhangteng.base.base.ImagePreviewActivity
import com.zhangteng.base.bean.PreviewImageInfo
import com.zhangteng.base.utils.ScreenUtils
import com.zhangteng.base.widget.NineGridView
import java.io.Serializable

/**
 * @description: 九宫格图片预览
 * @author: Swing
 * @date: 2021/9/5
 */
class NineGridViewClickAdapter(context: Context, imageInfo: List<PreviewImageInfo?>) :
    NineGridViewAdapter(context, imageInfo) {
    private val statusHeight: Int
    override fun onImageItemClick(
        context: Context,
        nineGridView: NineGridView,
        index: Int,
        imageInfo: List<PreviewImageInfo?>?
    ) {
        for (i in imageInfo!!.indices) {
            val info = imageInfo[i]
            var imageView: View
            imageView = if (i < nineGridView.maxSize) {
                nineGridView.getChildAt(i)
            } else {
                //如果图片的数量大于显示的数量，则超过部分的返回动画统一退回到最后一个图片的位置
                nineGridView.getChildAt(nineGridView.maxSize - 1)
            }
            info!!.imageViewWidth = imageView.width
            info.imageViewHeight = imageView.height
            val points = IntArray(2)
            imageView.getLocationInWindow(points)
            info.imageViewX = points[0]
            info.imageViewY = points[1] - statusHeight
        }
        val intent = Intent(context, ImagePreviewActivity::class.java)
        val bundle = Bundle()
        bundle.putSerializable(
            ImagePreviewActivity.Companion.IMAGE_INFO,
            imageInfo as Serializable?
        )
        bundle.putInt(ImagePreviewActivity.Companion.CURRENT_ITEM, index)
        intent.putExtras(bundle)
        context.startActivity(intent)
        (context as Activity).overridePendingTransition(0, 0)
    }


    init {
        statusHeight = ScreenUtils.getStatusHeight(context)
    }
}