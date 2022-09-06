package com.zhangteng.base.base

import android.view.View
import com.zhangteng.base.R
import com.zhangteng.base.widget.CommonTitleBar
import com.zhangteng.base.widget.CommonTitleBar.OnTitleBarListener

/**
 * Created by swing on 2017/11/23.
 */
abstract class TitleBarActivity : BaseActivity() {
    protected var mTitleBar: CommonTitleBar? = null
    override fun initView() {
        mTitleBar = findViewById(R.id.title_bar)
        checkNotNull(mTitleBar) { "The subclass of TitleBarActivity must contain a TitleBar." }
        mTitleBar?.setListener(object : OnTitleBarListener {
            override fun onClicked(v: View?, action: Int, extra: String?) {
                if (action == CommonTitleBar.ACTION_LEFT_BUTTON || action == CommonTitleBar.ACTION_LEFT_TEXT) {
                    finish()
                }
            }
        })
        if (isImmersionBarEnabled()) {
            //使用titleBar后不需要在layout_toolbar布局中添加statusBar节点
            //设置标题栏View，如果设置了statusBarView则不需要设置标题栏View（设置标题栏View会自动在顶部给titleBar增加一个状态栏的高度Padding、Height）
            mImmersionBar.titleBar(mTitleBar)
        }
    }
}