package com.zhangteng.base.base

import android.app.Dialog
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONArray
import com.zhangteng.utils.getFromSP
import com.zhangteng.utils.putToSP
import com.zhangteng.base.widget.CommonDialog
import com.zhangteng.base.widget.CommonTitleBar
import org.json.JSONObject
import java.util.*

/**
 * @ClassName: MemoryActivity
 * @Description: 带缓存的Activity
 * @Author: Swing 763263311@qq.com
 * @Date: 2020/10/28 0028 下午 14:02
 */
abstract class MemoryActivity : TitleBarActivity() {
    /**
     * 是否永久缓存页面数据
     * false 页面不缓存数据
     * true 页面可能缓存数据（具体是否缓存根据子类重写的isSaveStateForEver()方法判断）
     */
    private var isSaveStateForEver = true

    /**
     * 缓存数据，永久层存储在sp中引用类型转为json存储
     */
    private var outState: MutableMap<String?, Any?>? = null
    override fun initView() {
        super.initView()
        mTitleBar?.setListener(object : CommonTitleBar.OnTitleBarListener {
            override fun onClicked(v: View?, action: Int, extra: String?) {
                if (action == CommonTitleBar.ACTION_LEFT_BUTTON || action == CommonTitleBar.ACTION_LEFT_TEXT) {
                    if (isSaveStateForEver()) {
                        CommonDialog(this@MemoryActivity, "是否保存已编辑的数据？")
                            .setPositiveButton("保存")
                            .setNegativeButton("取消")
                            .setDialogCancelable(false)
                            .setDialogCanceledOnTouchOutside(false)
                            .setListener(object : CommonDialog.OnCloseListener {
                                override fun onClick(dialog: Dialog?, confirm: Boolean) {
                                    isSaveStateForEver = confirm
                                    dialog?.dismiss()
                                    finish()
                                }
                            }).show()
                    } else {
                        finish()
                    }
                }
            }
        })
    }

    override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)
        if (isSaveStateForEver) {
            val state = getFromSP("share_data", javaClass.name, null) as String
            if (!TextUtils.isEmpty(state)) {
                outState = jsonToMap(state)
                restoreStateForEver(outState)
            }
        }
    }

    override fun setContentView(view: View?) {
        super.setContentView(view)
        if (isSaveStateForEver) {
            val state = getFromSP("share_data", javaClass.name, null) as String
            if (!TextUtils.isEmpty(state)) {
                outState = jsonToMap(state)
                restoreStateForEver(outState)
            }
        }
    }

    override fun setContentView(view: View?, params: ViewGroup.LayoutParams?) {
        super.setContentView(view, params)
        if (isSaveStateForEver) {
            val state = getFromSP("share_data", javaClass.name, null) as String
            if (!TextUtils.isEmpty(state)) {
                outState = jsonToMap(state)
                restoreStateForEver(outState)
            }
        }
    }

    /**
     * 判断是否需要缓存
     * 子类可以重写本方法增加和业务相关的判断
     *
     * @return isSaveStateForEver 是否永久缓存
     */
    protected open fun isSaveStateForEver(): Boolean {
        return isSaveStateForEver
    }

    open fun setSaveStateForEver(saveStateForEver: Boolean) {
        isSaveStateForEver = saveStateForEver
    }

    /**
     * 子类实现本方法保持页面数据
     * outState.put();
     *
     * @param outState 内存中缓存暂存对象
     */
    protected abstract fun saveStateForEver(outState: MutableMap<String?, Any?>?)

    /**
     * 子类实现本方法读取缓存数据
     * 页面重新进入后自动读取永久层的缓存到内存中
     * savedInstanceState.get();
     *
     * @param savedInstanceState 内存中缓存暂存对象
     */
    protected abstract fun restoreStateForEver(savedInstanceState: MutableMap<String?, Any?>?)
    override fun onDestroy() {
        super.onDestroy()
        if (isSaveStateForEver()) {
            if (outState == null) outState = HashMap()
            saveStateForEver(outState)
            if (outState != null && outState!!.isNotEmpty()) {
                putToSP("share_data", javaClass.name, JSON.toJSONString(outState))
            } else {
                putToSP("share_data", javaClass.name, null)
            }
        } else {
            putToSP("share_data", javaClass.name, null)
        }
    }

    override fun onBackPressed() {
        if (isSaveStateForEver()) {
            CommonDialog(this, "是否保存已编辑的数据？")
                .setPositiveButton("保存")
                .setNegativeButton("取消")
                .setDialogCancelable(false)
                .setDialogCanceledOnTouchOutside(false)
                .setListener(object : CommonDialog.OnCloseListener {
                    override fun onClick(dialog: Dialog?, confirm: Boolean) {
                        isSaveStateForEver = confirm
                        dialog?.dismiss()
                        super@MemoryActivity.onBackPressed()
                    }
                }).show()
        } else {
            super.onBackPressed()
        }
    }

    /**
     * 将json转为Map
     */
    private fun jsonToMap(jsonStr: String?): HashMap<String?, Any?>? {
        val returnMap = HashMap<String?, Any?>()
        if (TextUtils.isEmpty(jsonStr)) return returnMap
        val map: MutableMap<String?, Any?>? = JSON.parseObject(jsonStr)
        if (map == null || map.isEmpty()) return returnMap
        for (key in map.keys) {
            val value = map[key] ?: continue
            if (value is JSONObject) {
                returnMap[key] = jsonToMap((value as JSONObject).toString())
            } else if (value is JSONArray) {
                returnMap[key] = jsonToList((value as JSONArray).toJSONString())
            } else {
                returnMap[key] = value
            }
        }
        return returnMap
    }

    /**
     * 将json转为list
     */
    private fun jsonToList(jsonStr: String?): MutableList<Any?>? {
        val returnList = ArrayList<Any?>()
        if (TextUtils.isEmpty(jsonStr)) return returnList
        val list: MutableList<Any?>? = JSON.parseArray(jsonStr)
        if (list == null || list.isEmpty()) return returnList
        for (i in list.indices) {
            val value = list[i] ?: continue
            if (value is JSONObject) {
                returnList.add(i, jsonToMap((value as JSONObject).toString()))
            } else if (value is JSONArray) {
                returnList.add(i, jsonToList((value as JSONArray).toJSONString()))
            } else {
                returnList.add(i, value)
            }
        }
        return returnList
    }
}