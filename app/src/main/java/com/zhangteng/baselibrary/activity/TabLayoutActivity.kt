package com.zhangteng.baselibrary.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.zhangteng.base.adapter.CommonFragmentAdapter

import com.zhangteng.base.base.BaseActivity
import com.zhangteng.base.widget.MyTabLayout
import com.zhangteng.baselibrary.R

class TabLayoutActivity : BaseActivity() {
    private var tab_layout: MyTabLayout? = null
    private var vp: ViewPager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tablayout)
    }

    override fun initView() {
        tab_layout = findViewById(R.id.tab_layout)
        vp = findViewById(R.id.vp)
        tab_layout?.setupWithViewPager(vp)
        val fragments = ArrayList<Fragment>()
        fragments.add(Fragment())
        fragments.add(Fragment())
        fragments.add(Fragment())
        vp?.adapter =
            CommonFragmentAdapter(supportFragmentManager, arrayOf("1", "2", "3"), fragments)
    }

    override fun initData() {

    }
}