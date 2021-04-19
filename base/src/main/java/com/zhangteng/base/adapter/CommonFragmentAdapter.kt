package com.zhangteng.base.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import java.util.*

/**
 * 公共的fragmentPagerAdapter
 * Created by Swing on 2017/11/24.
 */
open class CommonFragmentAdapter : FragmentPagerAdapter {
    private var titles: Array<String?>?
    private var fragments: ArrayList<Fragment>

    constructor(fm: FragmentManager, fragments: ArrayList<Fragment>) : super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        this.fragments = fragments
        titles = arrayOfNulls<String?>(fragments.size)
    }

    constructor(fm: FragmentManager, titles: Array<String?>?, fragments: ArrayList<Fragment>) : super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        this.titles = titles
        this.fragments = fragments
    }

    fun getFragments(): ArrayList<Fragment> {
        return fragments
    }

    override fun getItem(position: Int): Fragment {
        return fragments[position]
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return titles?.get(position)
    }

    override fun getCount(): Int {
        return fragments.size
    }
}