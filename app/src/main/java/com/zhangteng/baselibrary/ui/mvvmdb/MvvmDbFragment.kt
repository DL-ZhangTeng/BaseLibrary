package com.zhangteng.baselibrary.ui.mvvmdb

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.zhangteng.base.base.BaseAdapter
import com.zhangteng.mvvm.mvvm.BaseMvvmDbFragment
import com.zhangteng.baselibrary.MainActivity
import com.zhangteng.baselibrary.R
import com.zhangteng.baselibrary.databinding.MvvmDbFragmentBinding
/**
 * description: 有databinding的mvvm
 * author: Swing
 * date: 2021/11/11
 */
class MvvmDbFragment : com.zhangteng.mvvm.mvvm.BaseMvvmDbFragment<MvvmDbViewModel, MvvmDbFragmentBinding>() {
    private val mAdapter by lazy { MeWebAdapter() }

    companion object {
        fun newInstance() = MvvmDbFragment()
    }

    override fun initView(view: View, savedInstanceState: Bundle?) {
        mDatabind.viewModel = mViewModel
        mAdapter.apply {
            mOnItemClickListener = object : BaseAdapter.OnItemClickListener {
                override fun onItemClick(view: View?, position: Int) {
                    startActivity(Intent(activity, MainActivity::class.java))
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun initData(savedInstanceState: Bundle?) {
        super.initData(savedInstanceState)
        with(mDatabind.rvProject) {
            adapter = mAdapter
        }

        mViewModel.items.observe(this, {
            mAdapter.data = it
            mAdapter.notifyDataSetChanged()
        })
        mViewModel.getData()
    }

    override fun layoutId(): Int {
        return R.layout.mvvm_db_fragment
    }
}