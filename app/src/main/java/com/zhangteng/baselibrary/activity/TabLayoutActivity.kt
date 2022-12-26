package com.zhangteng.baselibrary.activity

import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.zhangteng.base.adapter.CommonFragmentAdapter
import com.zhangteng.base.base.BaseActivity
import com.zhangteng.base.widget.MyTabLayout
import com.zhangteng.base.widget.MyTabLayoutMediator
import com.zhangteng.baselibrary.R
import com.zhangteng.baselibrary.fragment.BaseDemoFragment


class TabLayoutActivity : BaseActivity() {
    private var tab_layout1: MyTabLayout? = null
    private var tab_layout2: MyTabLayout? = null
    private var tab_layout3: MyTabLayout? = null
    private var tab_layout4: MyTabLayout? = null
    private var vp: ViewPager? = null
    private val titleList: Array<String?> =
        arrayOf("111111", "111111", "111111", "111111", "111111")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tablayout)
    }

    override fun initView() {
        tab_layout1 = findViewById(R.id.tab_layout1)
        tab_layout2 = findViewById(R.id.tab_layout2)
        tab_layout3 = findViewById(R.id.tab_layout3)
        tab_layout4 = findViewById(R.id.tab_layout4)
        vp = findViewById(R.id.vp)

        val fragments = ArrayList<Fragment>()
        fragments.add(BaseDemoFragment())
        fragments.add(BaseDemoFragment())
        fragments.add(BaseDemoFragment())
        fragments.add(BaseDemoFragment())
        fragments.add(BaseDemoFragment())
        vp?.adapter =
            CommonFragmentAdapter(
                supportFragmentManager,
                titleList,
                fragments
            )

        tab_layout1?.setupWithViewPager(vp)
        tab_layout2?.setupWithViewPager(vp)
        tab_layout3?.setupWithViewPager(vp)
        tab_layout4?.setupWithViewPager(vp)

        //使用MyTabLayoutMediator自定义Tab
        MyTabLayoutMediator(
            tab_layout4!!,
            vp!!,
            object : MyTabLayoutMediator.TabConfigurationStrategy {
                override fun onConfigureTab(tab: MyTabLayout.Tab, position: Int) {
                    val imageView = ImageView(this@TabLayoutActivity)
                    setAnimation(imageView, position)
                    tab.setCustomView(imageView)
                    if (position == 0) {
                        val animationDrawable =
                            (tab.getCustomView() as ImageView).drawable as AnimationDrawable?
                        if (animationDrawable != null && !animationDrawable.isRunning) {
                            animationDrawable.start()
                        }
                    }
                }
            })
            .attach()

        tab_layout4?.addOnTabSelectedListener(object : MyTabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: MyTabLayout.Tab?) {
                tab?.let {
                    val animationDrawable =
                        (tab.getCustomView() as ImageView).drawable as AnimationDrawable?
                    if (animationDrawable != null && !animationDrawable.isRunning) {
                        animationDrawable.start()
                    }
                }
            }

            override fun onTabUnselected(tab: MyTabLayout.Tab?) {
                tab?.let {
                    setAnimation(tab.getCustomView() as ImageView, tab.getPosition())
                }
            }

            override fun onTabReselected(tab: MyTabLayout.Tab?) {

            }
        })

        //        //使用newTab()自定义Tab
//        for (position in titleList.indices) {
//            val tab = tab_layout4?.newTab()
//            tab?.let {
//                val imageView = ImageView(this@TabLayoutActivity)
//                setAnimation(imageView, position)
//                tab.setCustomView(imageView)
//                tab_layout4?.addTab(tab)
//                if (position == 0) {
//                    val animationDrawable =
//                        (tab.getCustomView() as ImageView).drawable as AnimationDrawable?
//                    if (animationDrawable != null && !animationDrawable.isRunning) {
//                        animationDrawable.start()
//                    }
//                }
//            }
//        }
//        vp?.addOnPageChangeListener(MyTabLayout.TabLayoutOnPageChangeListener(tab_layout4))
//
//        tab_layout4?.addOnTabSelectedListener(object : MyTabLayout.OnTabSelectedListener {
//            override fun onTabSelected(tab: MyTabLayout.Tab?) {
//                tab?.let {
//                    vp?.currentItem = tab.getPosition()
//
//                    val animationDrawable =
//                        (tab.getCustomView() as ImageView).drawable as AnimationDrawable?
//                    if (animationDrawable != null && !animationDrawable.isRunning) {
//                        animationDrawable.start()
//                    }
//                }
//            }
//
//            override fun onTabUnselected(tab: MyTabLayout.Tab?) {
//                tab?.let {
//                    setAnimation(tab.getCustomView() as ImageView, tab.getPosition())
//                }
//            }
//
//            override fun onTabReselected(tab: MyTabLayout.Tab?) {
//
//            }
//        })
    }

    override fun initData() {

    }

    private fun setAnimation(imageView: ImageView, position: Int) {
        when (position) {
            0 -> {
                imageView.setImageResource(R.drawable.tab_animation_1)
            }
            1 -> {
                imageView.setImageResource(R.drawable.tab_animation_2)
            }
            2 -> {
                imageView.setImageResource(R.drawable.tab_animation_3)
            }
            3 -> {
                imageView.setImageResource(R.drawable.tab_animation_4)
            }
            4 -> {
                imageView.setImageResource(R.drawable.tab_animation_5)
            }
        }
    }
}