package com.zhangteng.base.utils

import android.content.Context
import android.content.SharedPreferences
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method

/**
 * SharedPreferences统一管理类
 */
object SPUtils {
    /**
     * 保存在手机里面的文件名
     */
    val FILE_NAME: String = "share_data"
    val CURRENTUSER: String = "curentuser"
    val USERLIST: String = "userlist"

    /**
     * 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
     *
     * @param context
     * @param key
     * @param object
     */
    fun put(context: Context?, spname: String?, key: String?, defaultValue: Any?) {
        if (context == null || spname == null || key == null) return
        val sp = context.getSharedPreferences(spname,
                Context.MODE_PRIVATE)
        val editor = sp.edit()
        when (defaultValue) {
            is String -> {
                editor.putString(key, defaultValue)
            }
            is Int -> {
                editor.putInt(key, defaultValue)
            }
            is Boolean -> {
                editor.putBoolean(key, defaultValue)
            }
            is Float -> {
                editor.putFloat(key, defaultValue)
            }
            is Long -> {
                editor.putLong(key, defaultValue)
            }
            else -> {
                editor.putString(key, defaultValue.toString())
            }
        }
        SharedPreferencesCompat.apply(editor)
    }

    /**
     * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
     *
     * @param context
     * @param key
     * @param defaultObject
     * @return
     */
    operator fun get(context: Context?, spname: String?, key: String?, defaultObject: Any?): Any? {
        if (context == null || spname == null || key == null) return null
        val sp = context.getSharedPreferences(spname,
                Context.MODE_PRIVATE)
        if (defaultObject is String) {
            return sp.getString(key, defaultObject)
        } else if (defaultObject is Int) {
            return sp.getInt(key, defaultObject)
        } else if (defaultObject is Boolean) {
            return sp.getBoolean(key, defaultObject)
        } else if (defaultObject is Float) {
            return sp.getFloat(key, defaultObject)
        } else if (defaultObject is Long) {
            return sp.getLong(key, defaultObject)
        }
        return null
    }

    /**
     * 移除某个key值已经对应的值
     *
     * @param context
     * @param key
     */
    fun remove(context: Context?, spname: String?, key: String?) {
        if (context == null || spname == null || key == null) return
        val sp = context.getSharedPreferences(spname,
                Context.MODE_PRIVATE)
        val editor = sp.edit()
        editor.remove(key)
        SharedPreferencesCompat.apply(editor)
    }

    /**
     * 清除所有数据
     *
     * @param context
     */
    fun clear(context: Context?, spname: String?) {
        if (context == null || spname == null) return
        val sp = context.getSharedPreferences(spname,
                Context.MODE_PRIVATE)
        val editor = sp.edit()
        editor.clear()
        SharedPreferencesCompat.apply(editor)
    }

    /**
     * 查询某个key是否已经存在
     *
     * @param context
     * @param key
     * @return
     */
    fun contains(context: Context?, spname: String?, key: String?): Boolean? {
        if (context == null || spname == null || key == null) return null
        val sp = context.getSharedPreferences(spname,
                Context.MODE_PRIVATE)
        return sp.contains(key)
    }

    /**
     * 返回所有的键值对
     *
     * @param context
     * @return
     */
    fun getAll(context: Context?, spname: String?): MutableMap<String?, *>? {
        if (context == null || spname == null) return null
        val sp = context.getSharedPreferences(spname,
                Context.MODE_PRIVATE)
        return sp.all
    }

    /**
     * 创建一个解决SharedPreferencesCompat.apply方法的一个兼容类
     *
     * @author zhy
     */
    private object SharedPreferencesCompat {
        private val sApplyMethod = findApplyMethod()

        /**
         * 反射查找apply的方法
         *
         * @return
         */
        private fun findApplyMethod(): Method? {
            try {
                val clz: Class<*> = SharedPreferences.Editor::class.java
                return clz.getMethod("apply")
            } catch (e: NoSuchMethodException) {
            }
            return null
        }

        /**
         * 如果找到则使用apply执行，否则使用commit
         *
         * @param editor
         */
        fun apply(editor: SharedPreferences.Editor?) {
            try {
                if (sApplyMethod != null) {
                    sApplyMethod.invoke(editor)
                    return
                }
            } catch (e: IllegalArgumentException) {
            } catch (e: IllegalAccessException) {
            } catch (e: InvocationTargetException) {
            }
            editor?.commit()
        }
    }
}