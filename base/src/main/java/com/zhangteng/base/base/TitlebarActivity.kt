package com.zhangteng.base.base

import android.view.View
import com.zhangteng.base.R
import com.zhangteng.base.widget.CommonTitleBar
import com.zhangteng.base.widget.CommonTitleBar.OnTitleBarListener

/**
 * Created by swing on 2017/11/23.
 */
abstract class TitlebarActivity : BaseActivity() {
    protected var mTitlebar: CommonTitleBar? = null
    override fun initView() {
        mTitlebar = findViewById(R.id.titlebar)
        checkNotNull(mTitlebar) { "The subclass of TitlebarActivity must contain a titlebar." }
        mTitlebar?.setListener(object : OnTitleBarListener {
            override fun onClicked(v: View?, action: Int, extra: String?) {
                if (action == CommonTitleBar.ACTION_LEFT_BUTTON || action == CommonTitleBar.ACTION_LEFT_TEXT) {
                    finish()
                }
            }
        })
    }
}