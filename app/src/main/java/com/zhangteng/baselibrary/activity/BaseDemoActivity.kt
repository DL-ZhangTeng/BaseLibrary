package com.zhangteng.baselibrary.activity

import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zhangteng.base.base.BaseActivity
import com.zhangteng.base.widget.GridSpacingItemDecoration
import com.zhangteng.baselibrary.R
import com.zhangteng.baselibrary.adapter.ImageSelectorAdapter
import com.zhangteng.imagepicker.bean.ImageInfo
import com.zhangteng.imagepicker.imageloader.GlideImageLoader
import com.zhangteng.utils.ImageLoader

class BaseDemoActivity : BaseActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var imageSelectorAdapter: ImageSelectorAdapter
    private var data: MutableList<ImageInfo?>? = null
    private lateinit var imageLoader: ImageLoader
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base_demo)
    }

    override fun initView() {
        imageLoader = GlideImageLoader()
        recyclerView = findViewById(R.id.recyclerView)
        data = ArrayList()
        imageSelectorAdapter = ImageSelectorAdapter(this, data, imageLoader = imageLoader)
        recyclerView.adapter = imageSelectorAdapter
        recyclerView.layoutManager = GridLayoutManager(this, 3)
        recyclerView.addItemDecoration(GridSpacingItemDecoration(3, 10, false))
    }

    override fun initData() {

    }
}