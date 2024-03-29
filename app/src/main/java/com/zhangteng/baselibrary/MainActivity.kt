package com.zhangteng.baselibrary

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import com.zhangteng.base.base.BaseActivity
import com.zhangteng.base.widget.ImageRatingBar
import com.zhangteng.baselibrary.activity.NineImageActivity
import com.zhangteng.baselibrary.activity.TabLayout2Activity
import com.zhangteng.baselibrary.activity.TabLayoutActivity
import com.zhangteng.baselibrary.activity.TreeActivity
import com.zhangteng.utils.StateViewHelper
import com.zhangteng.utils.jumpToActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : BaseActivity() {

    @Inject
    lateinit var mInjectStateViewHelper: StateViewHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun initView() {
        findViewById<ImageRatingBar>(R.id.imageRatingBar).setRating(2)
    }

    override fun initData() {

    }

    fun onClickTabLayout(v: View) {
        jumpToActivity<TabLayoutActivity>()
    }

    fun onClickTabLayout2(v: View) {
        jumpToActivity<TabLayout2Activity>()
    }

    fun onClickTree(v: View) {
        jumpToActivity<TreeActivity>()
    }

    fun onClickNineImage(v: View) {
        jumpToActivity<NineImageActivity>()
    }

    override fun createStateViewHelper(): StateViewHelper {
        return mInjectStateViewHelper.apply {
            againRequestListeners.add(object : StateViewHelper.AgainRequestListener {
                override fun request(view: View) {
                    againRequestByStateViewHelper(view)
                }
            })
            cancelRequestListeners.add(object : StateViewHelper.CancelRequestListener {
                override fun cancel(dialog: DialogInterface) {
                    cancelRequestByStateViewHelper(dialog)
                }
            })
        }
    }

    override fun showProgressDialog(mLoadingText: String?) {
        mStateViewHelper.showProgressDialog(this, R.drawable.loading5, mLoadingText)
    }

    override fun againRequestByStateViewHelper(view: View) {
        super.againRequestByStateViewHelper(view)

    }

    override fun cancelRequestByStateViewHelper(dialog: DialogInterface) {
        super.cancelRequestByStateViewHelper(dialog)

    }
}