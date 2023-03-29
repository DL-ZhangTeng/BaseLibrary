package com.zhangteng.base.adapter

import android.content.Context
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.viewpager.widget.PagerAdapter
import com.github.chrisbanes.photoview.OnPhotoTapListener
import com.github.chrisbanes.photoview.PhotoView
import com.zhangteng.base.R
import com.zhangteng.base.base.ImagePreviewActivity
import com.zhangteng.base.bean.PreviewImageInfo
import com.zhangteng.base.widget.NineGridView

/**
 * @description: 图片预览
 * @author: Swing
 * @date: 2021/9/5
 */
class ImagePreviewAdapter(
    private val context: Context,
    private val imageInfo: List<PreviewImageInfo>
) : PagerAdapter(), OnPhotoTapListener {
    var primaryItem: View? = null
        private set

    override fun getCount(): Int {
        return imageInfo.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun setPrimaryItem(container: ViewGroup, position: Int, `object`: Any) {
        super.setPrimaryItem(container, position, `object`)
        primaryItem = `object` as View
    }

    val primaryImageView: ImageView
        get() = primaryItem!!.findViewById<View>(R.id.pv) as ImageView

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = LayoutInflater.from(context).inflate(R.layout.item_photoview, container, false)
        val pb = view.findViewById<View>(R.id.pb) as ProgressBar
        val imageView = view.findViewById<View>(R.id.pv) as PhotoView
        val info = imageInfo[position]
        imageView.setOnPhotoTapListener(this)
        showExcessPic(info, imageView)

        //如果需要加载的loading,需要自己改写,不能使用这个方法
        NineGridView.imageLoader?.onDisplayImage(
            view.context,
            imageView,
            info.thumbnailUrl,
            info.bigImageUrl
        )

        container.addView(view)
        return view
    }

    /**
     * 展示过度图片
     */
    private fun showExcessPic(imageInfo: PreviewImageInfo, imageView: PhotoView) {
        //先获取大图的缓存图片
        var cacheImage: Bitmap? = NineGridView.imageLoader?.getCacheImage(imageInfo.bigImageUrl)
        //如果大图的缓存不存在,在获取小图的缓存
        if (cacheImage == null) cacheImage =
            NineGridView.imageLoader?.getCacheImage(imageInfo.thumbnailUrl)
        //如果没有任何缓存,使用默认图片,否者使用缓存
        if (cacheImage == null) {
            imageView.setImageResource(R.drawable.ic_default_color)
        } else {
            imageView.setImageBitmap(cacheImage)
        }
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    /**
     * 单击屏幕关闭
     */
    override fun onPhotoTap(view: ImageView, x: Float, y: Float) {
        (context as ImagePreviewActivity).finishActivityAnim()
    }
}