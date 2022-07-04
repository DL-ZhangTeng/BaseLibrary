package com.zhangteng.utils

import android.content.Context
import android.widget.ImageView

/**
 * Created by Swing on 2018/4/18.
 */
interface ImageLoader {
    fun loadImage(context: Context?, imageView: ImageView?, uri: String?)
}