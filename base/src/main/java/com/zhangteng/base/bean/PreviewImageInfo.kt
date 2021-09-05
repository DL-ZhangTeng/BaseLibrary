package com.zhangteng.base.bean

import java.io.Serializable

class PreviewImageInfo : Serializable {
    var thumbnailUrl: String? = null
    var bigImageUrl: String? = null
    var imageViewHeight = 0
    var imageViewWidth = 0
    var imageViewX = 0
    var imageViewY = 0
}