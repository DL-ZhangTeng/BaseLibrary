package com.zhangteng.baselibrary.ui.mvvmdb

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.zhangteng.base.base.BaseAdapter
import com.zhangteng.baselibrary.MainActivity
import com.zhangteng.baselibrary.R
import com.zhangteng.baselibrary.databinding.MvvmDbFragmentBinding
import com.zhangteng.mvvm.mvvm.BaseMvvmDbFragment

/**
 * description: 有databinding的mvvm
 * author: Swing
 * date: 2021/11/11
 */
class MvvmDbFragment : BaseMvvmDbFragment<MvvmDbViewModel, MvvmDbFragmentBinding>() {
    private val mAdapter by lazy { MeWebAdapter() }

    companion object {
        fun newInstance() = MvvmDbFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.mvvm_db_fragment, container, false)
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
}