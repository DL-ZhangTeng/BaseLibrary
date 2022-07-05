package com.zhangteng.baselibrary.adapter

import androidx.fragment.app.FragmentActivity
import com.zhangteng.base.adapter.PublishAdapter
import com.zhangteng.baselibrary.R
import com.zhangteng.imagepicker.bean.ImageInfo
import com.zhangteng.imagepicker.config.ImagePickerConfig
import com.zhangteng.imagepicker.config.ImagePickerEnum
import com.zhangteng.imagepicker.config.ImagePickerOpen
import com.zhangteng.utils.IHandlerCallBack
import com.zhangteng.utils.ImageLoader

class ImageSelectorAdapter(
    activity: FragmentActivity,
    data: MutableList<ImageInfo?>?,
    addButtonNum: Int = 1,
    onlyImage: Boolean = false,
    imageLoader: ImageLoader? = null
) : PublishAdapter<ImageInfo>(activity, data, addButtonNum, onlyImage, imageLoader) {

    override fun initPicker(
        activity: FragmentActivity?,
        iHandlerCallBack: IHandlerCallBack<ImageInfo>?,
        imageLoader: ImageLoader?,
        maxImage: Int,
        isSelectVideo: Boolean,
        isSelectImage: Boolean,
        isCrop: Boolean,
        cropAspectRatio: Float
    ) {
        val imagePickerConfig = ImagePickerConfig.Builder()
            .pickerThemeColorRes(R.color.titlebar_bg)
            .pickerTitleColorRes(R.color.titlebar_text_color)
            .pickerBackRes(R.mipmap.image_picker_back_black)
            .pickerFolderRes(R.mipmap.image_picker_folder_black)
            .cropThemeColorRes(R.color.base_theme_color)
            .cropTitleColorRes(R.color.titlebar_text_light_color)
            .imageLoader(imageLoader) //图片加载器
            .iHandlerCallBack(iHandlerCallBack) //图片选择器生命周期监听（直接打开摄像头时无效）
            .multiSelect(true) //是否开启多选
            .isVideoPicker(isSelectVideo) //是否选择视频 默认false
            .isShowCamera(!isSelectVideo)
            .isImagePicker(isSelectImage)
            .imagePickerType(ImagePickerEnum.PHOTO_PICKER) //选择器打开类型
            .isMirror(false) //是否旋转镜头
            .maxImageSelectable(maxImage) //图片可选择数
            .maxVideoSelectable(1)
            .maxHeight(1920) //图片最大高度
            .maxWidth(1080) //图片最大宽度
            .maxImageSize(15) //图片最大大小Mb
            .maxVideoLength(10 * 60 * 1000)
            .maxVideoSize(50)
            .isCrop(isCrop, cropAspectRatio)
            .build()
        ImagePickerOpen.getInstance().setImagePickerConfig(imagePickerConfig).open(activity)
    }
}