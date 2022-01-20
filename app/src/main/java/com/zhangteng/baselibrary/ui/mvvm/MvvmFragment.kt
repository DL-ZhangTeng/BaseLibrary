package com.zhangteng.baselibrary.ui.mvvm

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import com.zhangteng.base.base.BaseAdapter
import com.zhangteng.mvvm.mvvm.BaseMvvmFragment
import com.zhangteng.baselibrary.MainActivity
import com.zhangteng.baselibrary.R
import com.zhangteng.baselibrary.ui.mvvmdb.MeWebAdapter
/**
 * description: 无databinding的mvvm
 * author: Swing
 * date: 2021/11/11
 */
class MvvmFragment : BaseMvvmFragment<MvvmViewModel>() {
    private val mAdapter by lazy { MeWebAdapter() }
    private var tbProject: TabLayout? = null
    private var rvProject: RecyclerView? = null

    companion object {
        fun newInstance() = MvvmFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.mvvm_fragment, container, false)
    }

    override fun initView(view: View, savedInstanceState: Bundle?) {
        tbProject = view.findViewById(R.id.tb_project)
        rvProject = view.findViewById(R.id.rv_project)
        mAdapter.apply {
            mOnItemClickListener = object : BaseAdapter.OnItemClickListener {
                override fun onItemClick(view: View?, position: Int) {
                    startActivity(Intent(activity, MainActivity::class.java))
                }
            }
        }
        tbProject?.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.let {
                    mViewModel.navData.value?.get(it.position)?.let { it1 ->
                        mViewModel.getProjectList(
                            it1.id)
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

        })
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun initData(savedInstanceState: Bundle?) {
        super.initData(savedInstanceState)
        with(rvProject) {
            this?.adapter = mAdapter
        }

        mViewModel.navData.observe(this, {
            it.forEach { navTypeBean ->
                tbProject?.newTab()?.setText(navTypeBean.name)
                    ?.let { it1 -> tbProject?.addTab(it1) }
            }
        })
        mViewModel.items.observe(this, {
            mAdapter.data = it
            mAdapter.notifyDataSetChanged()
        })
        mViewModel.getData()
    }
}