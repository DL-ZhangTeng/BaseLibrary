package com.zhangteng.base.base

import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment

/**
 * Created by swing on 2017/11/30.
 */
abstract class BaseDialogFragment : DialogFragment() {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initData()
    }

    protected abstract fun initView(view: View?)
    protected open fun initData() {}
}