package com.zhangteng.utils

import java.lang.reflect.Field
import java.lang.reflect.Modifier
import java.util.*

/**
 * Any转Map
 */
fun Any?.toMap(): MutableMap<String?, Any?>? {
    if (this == null) return null
    val map: MutableMap<String?, Any?> = HashMap()
    val clazz: Class<*> = javaClass
    println(clazz)
    for (field in clazz.declaredFields) {
        field.isAccessible = true
        try {
            val fieldName = field.name
            var value = field[this]
            if (value == null) {
                value = ""
            }
            map[fieldName] = value
        } catch (ignore: Exception) {
        }
    }
    return map
}

/**
 * Map转Any
 */
fun MutableMap<*, *>?.toAny(beanClass: Class<*>?): Any? {
    return try {
        if (this == null || beanClass == null) return null
        val obj = beanClass.newInstance()
        val fields = obj.getAllFields()
        for (field in fields!!) {
            val mod = field!!.modifiers
            if (Modifier.isStatic(mod) || Modifier.isFinal(mod)) {
                continue
            }
            field.isAccessible = true
            if (containsKey(field.name)) {
                field.set(obj, this[field.name])
            }
        }
        obj
    } catch (ignore: Exception) {
        null
    }
}

/**
 * 获取Any的所有字段
 */
fun Any?.getAllFields(): MutableList<Field?>? {
    if (this == null) return null
    var clazz: Class<*>? = javaClass
    val fieldList: MutableList<Field?> = ArrayList()
    while (clazz != null) {
        fieldList.addAll(listOf(*clazz.declaredFields))
        clazz = clazz.superclass
    }
    return fieldList
}