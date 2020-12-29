package com.zhangteng.base.base

import android.content.ActivityNotFoundException
import android.content.Intent
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.zhangteng.base.utils.ToastUtils

/**
 * Created by swing on 2017/11/23.
 */
abstract class BaseActivity : AppCompatActivity() {
    override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)
        initView()
        initData()
    }

    override fun setContentView(view: View?) {
        super.setContentView(view)
        initView()
        initData()
    }

    override fun setContentView(view: View?, params: ViewGroup.LayoutParams?) {
        super.setContentView(view, params)
        initView()
        initData()
    }

    protected abstract fun initView()
    protected abstract fun initData()
    fun showToast(message: String?) {
        ToastUtils.showShort(this, message)
    }

    fun showToast(messageId: Int) {
        ToastUtils.showShort(this, messageId)
    }

    override fun startActivity(intent: Intent?) {
        try {
            super.startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            showToast("未找到相应应用")
        }
    }

    override fun startActivityForResult(intent: Intent?, requestCode: Int) {
        try {
            super.startActivityForResult(intent, requestCode)
        } catch (e: ActivityNotFoundException) {
            showToast("未找到相应应用")
        }
    }
}