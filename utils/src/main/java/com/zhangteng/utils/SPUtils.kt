package com.zhangteng.utils

import android.content.Context
import android.content.SharedPreferences
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method

/**
 * 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
 *
 * @param key
 * @param defaultValue
 */
fun Context?.putToSP(
    spName: String? = "currentUser",
    key: String?,
    defaultValue: Any?
) {
    if (this == null || spName == null || key == null) return
    val sp = getSharedPreferences(
        spName,
        Context.MODE_PRIVATE
    )
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
 * @param key
 * @param defaultObject
 * @return
 */
fun Context?.getFromSP(
    spName: String? = "currentUser",
    key: String?,
    defaultObject: Any?
): Any? {
    if (this == null || spName == null || key == null) return null
    val sp = getSharedPreferences(
        spName,
        Context.MODE_PRIVATE
    )
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

fun Context?.getFromSPToSet(
    spName: String? = "currentUser",
    key: String?,
    defaultSet: Set<String?>?
): Set<String>? {
    if (this == null || spName == null || key == null) return null
    val sp = getSharedPreferences(
        spName,
        Context.MODE_PRIVATE
    )
    return sp.getStringSet(key, defaultSet)
}

/**
 * 返回所有的键值对
 *
 * @return
 */
fun Context?.getFromSPForAll(spName: String? = "currentUser"): MutableMap<String?, *>? {
    if (this == null || spName == null) return null
    val sp = getSharedPreferences(
        spName,
        Context.MODE_PRIVATE
    )
    return sp.all
}

/**
 * 移除某个key值已经对应的值
 *
 * @param key
 */
fun Context?.removeFromSP(spName: String? = "currentUser", key: String?) {
    if (this == null || spName == null || key == null) return
    val sp = getSharedPreferences(
        spName,
        Context.MODE_PRIVATE
    )
    val editor = sp.edit()
    editor.remove(key)
    SharedPreferencesCompat.apply(editor)
}

/**
 * 清除所有数据
 *
 */
fun Context?.clearFromSP(spName: String? = "currentUser") {
    if (this == null || spName == null) return
    val sp = getSharedPreferences(
        spName,
        Context.MODE_PRIVATE
    )
    val editor = sp.edit()
    editor.clear()
    SharedPreferencesCompat.apply(editor)
}

/**
 * 查询某个key是否已经存在
 *
 * @param key
 * @return
 */
fun Context?.containsInSP(spName: String? = "currentUser", key: String?): Boolean? {
    if (this == null || spName == null || key == null) return null
    val sp = getSharedPreferences(
        spName,
        Context.MODE_PRIVATE
    )
    return sp.contains(key)
}

/**
 * 创建一个解决SharedPreferencesCompat.apply方法的一个兼容类
 *
 */
object SharedPreferencesCompat {
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