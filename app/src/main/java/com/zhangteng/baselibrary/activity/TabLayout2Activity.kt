package com.zhangteng.baselibrary.activity

import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.zhangteng.base.base.BaseActivity
import com.zhangteng.base.widget.MyTabLayout
import com.zhangteng.base.widget.MyTabLayoutMediator
import com.zhangteng.baselibrary.R
import com.zhangteng.baselibrary.fragment.BaseDemoFragment


class TabLayout2Activity : BaseActivity() {
    private var tab_layout: MyTabLayout? = null
    private var vp: ViewPager2? = null
    private val titleList: Array<String?> =
        arrayOf("111111", "111111", "111111", "111111", "111111")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tablayout2)
    }

    override fun initView() {
        tab_layout = findViewById(R.id.tab_layout)
        vp = findViewById(R.id.vp)

        vp?.adapter = object : FragmentStateAdapter(supportFragmentManager, lifecycle) {
            override fun getItemCount(): Int {
                return titleList.size
            }

            override fun createFragment(position: Int): Fragment {
                return BaseDemoFragment()
            }
        }

        //使用MyTabLayoutMediator自定义Tab
        MyTabLayoutMediator(tab_layout!!, vp!!,
            object : MyTabLayoutMediator.TabConfigurationStrategy {
                override fun onConfigureTab(tab: MyTabLayout.Tab, position: Int) {
                    val imageView = ImageView(this@TabLayout2Activity)
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
            }).attach2()
        tab_layout?.addOnTabSelectedListener(object : MyTabLayout.OnTabSelectedListener {
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