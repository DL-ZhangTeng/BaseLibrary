package com.zhangteng.baselibrary.ui.mvvmdb

import androidx.databinding.ObservableArrayList
import androidx.lifecycle.MutableLiveData
import com.google.android.material.tabs.TabLayout
import com.zhangteng.base.mvvm.base.BaseLoadingViewModel
import com.zhangteng.base.utils.d
import com.zhangteng.base.utils.e
import com.zhangteng.baselibrary.http.Api
import com.zhangteng.baselibrary.http.entity.ArticlesBean
import com.zhangteng.baselibrary.http.entity.NavTypeBean
import com.zhangteng.rxhttputils.exception.ApiException
import com.zhangteng.rxhttputils.http.HttpUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*

class MvvmDbViewModel : BaseLoadingViewModel() {
    /**
     * 使用了databinding
     * */
    var navTitle = ObservableArrayList<String>()

    var navData = ObservableArrayList<NavTypeBean>()

    var items = MutableLiveData<MutableList<ArticlesBean?>?>()


    private var page: Int = 0

    var tabOnClickListener = object : TabLayout.OnTabSelectedListener {
        override fun onTabSelected(tab: TabLayout.Tab?) {
            tab?.let {
                getProjectList(navData[it.position].id)
            }
        }

        override fun onTabUnselected(tab: TabLayout.Tab?) {
        }

        override fun onTabReselected(tab: TabLayout.Tab?) {

        }

    }

    fun getProjectList(cid: Int) {
        /**
         * 只返回结果，其他全抛自定义异常
         * */
        launchOnlyResult({
            HttpUtils.getInstance()
                .ConfigGlobalHttpUtils()
                .createService(Api::class.java)
                .getProjectList(page, cid)
        }, {
            items.value = it.datas
        }, error = {

        }, complete = {

        }, isShowDialog = true)
    }

    /**
     * 先请求tab数据
     * */
    fun getData() {
        launchOnlyResult({
            HttpUtils.getInstance()
                .ConfigGlobalHttpUtils()
                .createService(Api::class.java)
                .naviJson()
        }, success = {
            navData.addAll(it)
            it.forEach { item ->
                navTitle.add(item.name)
            }
        }, error = {
            it.message.e()
        })
    }

    /**
     * 用Flow流的方式
     * 操作符比较繁琐
     * */
    fun getFirstData() {

        launchUI {
            launchFlow {
                HttpUtils.getInstance()
                    .ConfigGlobalHttpUtils()
                    .createService(Api::class.java)
                    .naviJson()
            }
                .flatMapConcat {
                    return@flatMapConcat if (it.isSuccess()) {
                        navData.addAll(it.data)

                        it.data.forEach { item -> navTitle.add(item.name) }

                        launchFlow {
                            HttpUtils.getInstance()
                                .ConfigGlobalHttpUtils()
                                .createService(Api::class.java).getProjectList(page, it.data[0].id)
                        }
                    } else throw ApiException(Throwable(it.errorMsg), it.errorCode)
                }
                .onStart { loadingChange.showLoadingView.postValue(null) }
                .flowOn(Dispatchers.IO)
                .onCompletion { loadingChange.dismissLoadingView.call() }
                .catch {
                    // 错误处理
                    val err = ApiException.handleException(it)
                    "${err.code}: ${err.message}".d()
                }
                .collect {
                    if (it.isSuccess()) items.value = it.data.datas
                }
        }
    }
}