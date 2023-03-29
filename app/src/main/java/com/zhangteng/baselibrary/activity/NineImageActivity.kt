package com.zhangteng.baselibrary.activity

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.TextUtils
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.zhangteng.base.adapter.NineGridViewClickAdapter
import com.zhangteng.base.base.BaseActivity
import com.zhangteng.base.bean.PreviewImageInfo
import com.zhangteng.base.widget.NineGridView
import com.zhangteng.baselibrary.R


class NineImageActivity : BaseActivity() {
    private var nineGrid: NineGridView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nineimage)
    }

    override fun initView() {
        NineGridView.imageLoader = object : NineGridView.ImageLoader {
            override fun onDisplayImage(
                context: Context?,
                imageView: ImageView?,
                thumbnailUrl: String?,
                bigImageUrl: String?,
                onProgressListener: NineGridView.OnProgressListener?,
            ) {
                context?.let {
                    imageView?.let { it1 ->
                        if (TextUtils.isEmpty(bigImageUrl)) {
                            //大图图片地址,九宫格显示时为null
                            Glide.with(it)
                                .load(thumbnailUrl)
                                .apply(
                                    RequestOptions()
                                        .placeholder(R.mipmap.ic_launcher)
                                        .centerCrop()
                                )
                                .listener(object : RequestListener<Drawable> {
                                    override fun onLoadFailed(
                                        e: GlideException?,
                                        model: Any?,
                                        target: Target<Drawable>?,
                                        isFirstResource: Boolean
                                    ): Boolean {
                                        onProgressListener?.onProgress(false, 1, 0, 0)
                                        return false
                                    }

                                    override fun onResourceReady(
                                        resource: Drawable?,
                                        model: Any?,
                                        target: Target<Drawable>?,
                                        dataSource: DataSource?,
                                        isFirstResource: Boolean
                                    ): Boolean {
                                        onProgressListener?.onProgress(true, 100, 0, 0)
                                        return false
                                    }
                                })
                                .into(it1)
                        } else {
                            //预览时先加载缩略图在加载大图
                            Glide.with(context)
                                .load(bigImageUrl)
                                .thumbnail(
                                    Glide.with(context)
                                        .load(thumbnailUrl)
                                )
                                .into(it1)
                        }
                    }
                }
            }

            override fun getCacheImage(context: Context?, url: String?): Bitmap? {
                return null
            }

        }

        nineGrid = findViewById(R.id.nineGrid)
        val imageInfo: ArrayList<PreviewImageInfo> = ArrayList()
        imageInfo.add(PreviewImageInfo().apply {
            bigImageUrl =
                "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fbpic.588ku.com%2Felement_origin_min_pic%2F16%2F08%2F26%2F1657bffac4e3795.jpg&refer=http%3A%2F%2Fbpic.588ku.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1633421267&t=d59b36f659f72d06989c79f3cee54bb7"
            thumbnailUrl = bigImageUrl
        })
        imageInfo.add(PreviewImageInfo().apply {
            bigImageUrl =
                "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fbpic.588ku.com%2Felement_origin_min_pic%2F16%2F08%2F26%2F1657bffac4e3795.jpg&refer=http%3A%2F%2Fbpic.588ku.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1633421267&t=d59b36f659f72d06989c79f3cee54bb7"
            thumbnailUrl = bigImageUrl
        })
        imageInfo.add(PreviewImageInfo().apply {
            bigImageUrl =
                "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fbpic.588ku.com%2Felement_origin_min_pic%2F16%2F08%2F26%2F1657bffac4e3795.jpg&refer=http%3A%2F%2Fbpic.588ku.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1633421267&t=d59b36f659f72d06989c79f3cee54bb7"
            thumbnailUrl = bigImageUrl
        })
//        if (imageInfo.size == 1) {
//            nineGrid?.setSingleImageRatio()
//        }
        nineGrid?.setAdapter(NineGridViewClickAdapter(this, imageInfo))

    }

    override fun initData() {

    }
}