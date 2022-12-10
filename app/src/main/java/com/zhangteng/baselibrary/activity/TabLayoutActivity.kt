package com.zhangteng.baselibrary.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.zhangteng.base.adapter.CommonFragmentAdapter

import com.zhangteng.base.base.BaseActivity
import com.zhangteng.base.widget.MyTabLayout
import com.zhangteng.baselibrary.R
import com.zhangteng.baselibrary.fragment.BaseDemoFragment

class TabLayoutActivity : BaseActivity() {
    private var tab_layout: MyTabLayout? = null
    private var tab_layout1: MyTabLayout? = null
    private var tab_layout2: MyTabLayout? = null
    private var tab_layout3: MyTabLayout? = null
    private var tab_layout4: MyTabLayout? = null
    private var tab_layout5: MyTabLayout? = null
    private var vp: ViewPager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tablayout)
    }

    override fun initView() {
        tab_layout = findViewById(R.id.tab_layout)
        tab_layout1 = findViewById(R.id.tab_layout1)
        tab_layout2 = findViewById(R.id.tab_layout2)
        tab_layout3 = findViewById(R.id.tab_layout3)
        tab_layout4 = findViewById(R.id.tab_layout4)
        tab_layout5 = findViewById(R.id.tab_layout5)
        vp = findViewById(R.id.vp)
        tab_layout?.setupWithViewPager(vp)
        tab_layout1?.setupWithViewPager(vp)
        tab_layout2?.setupWithViewPager(vp)
        tab_layout3?.setupWithViewPager(vp)
        tab_layout4?.setupWithViewPager(vp)
        tab_layout5?.setupWithViewPager(vp)
        val fragments = ArrayList<Fragment>()
        fragments.add(BaseDemoFragment())
        fragments.add(BaseDemoFragment())
        fragments.add(BaseDemoFragment())
        fragments.add(BaseDemoFragment())
        fragments.add(BaseDemoFragment())
        vp?.adapter =
            CommonFragmentAdapter(
                supportFragmentManager,
                arrayOf("111111", "111111", "111111", "111111", "111111"),
                fragments
            )
    }

    override fun initData() {

    }
}