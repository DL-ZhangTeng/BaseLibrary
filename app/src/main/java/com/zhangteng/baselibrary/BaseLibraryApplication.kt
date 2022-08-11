package com.zhangteng.baselibrary

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import com.zhangteng.base.base.BaseApplication
import com.zhangteng.rxhttputils.http.HttpUtils
import com.zhangteng.utils.R
import com.zhangteng.utils.StateViewHelper

class BaseLibraryApplication : BaseApplication() {
    override fun onCreate() {
        super.onCreate()
        initModuleApp(this)
        initModuleData(this)
    }

    override fun initModuleApp(application: Application?) {
        HttpUtils.init(this)
        HttpUtils.getInstance()
            .ConfigGlobalHttpUtils() //全局的BaseUrl
            .setBaseUrl("https://www.wanandroid.com/") //开启缓存策略
            .setCache(true) //全局的请求头信息
            //.setHeaders(headerMaps)
            //全局持久话cookie,保存本地每次都会携带在header中
            .setCookie(false) //全局ssl证书认证
            //信任所有证书,不安全有风险
            .setSslSocketFactory() //使用预埋证书，校验服务端证书（自签名证书）
            //.setSslSocketFactory(getAssets().open("your.cer"))
            //使用bks证书和密码管理客户端证书（双向认证），使用预埋证书，校验服务端证书（自签名证书）
            //.setSslSocketFactory(getAssets().open("your.bks"), "123456", getAssets().open("your.cer"))
            //全局超时配置
            .setReadTimeOut(10) //全局超时配置
            .setWriteTimeOut(10) //全局超时配置
            .setConnectionTimeOut(10) //全局是否打开请求log日志
            .setLog(true)

        //状态View初始化
        StateViewHelper.apply {
            //状态View图标
            defaultIcon = R.mipmap.icon_default
            noNetIcon = R.mipmap.icon_default_nonet
            timeOutIcon = R.mipmap.icon_default_timeout
            emptyIcon = R.mipmap.icon_default_empty
            unknownIcon = R.mipmap.icon_default_unknown
            noLoginIcon = R.mipmap.icon_default_nologin

            //状态View提示文本
            defaultText = ""
            noNetText = "无网络"
            timeOutText = "请求超时"
            emptyText = "暂无内容~"
            unknownText = "数据错误"
            noLoginText = "未登录"

            //状态View重试文本
            defaultAgainText = ""
            noNetAgainText = "点击重试"
            timeOutAgainText = "点击重试"
            emptyAgainText = ""
            unknownAgainText = ""
            noLoginAgainText = "去登录"

            //加载中图标
            loadingImage = R.drawable.loading1

            //加载中文本
            loadingText = "加载中..."

            //加载中布局
            loadingLayout = R.layout.layout_dialog_progress
        }
    }

    override fun initModuleData(application: Application?) {

    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }
}