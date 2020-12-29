package com.zhangteng.base.utils

import java.lang.reflect.Field
import java.lang.reflect.Modifier
import java.util.*

object BeanUtils {
    /**
     * Object转Map
     */
    fun getObjectToMap(obj: Any?): MutableMap<String?, Any?>? {
        if (obj == null) return null
        val map: MutableMap<String?, Any?> = HashMap()
        val clazz: Class<*> = obj.javaClass
        println(clazz)
        for (field in clazz.declaredFields) {
            field.isAccessible = true
            try {
                val fieldName = field.name
                var value = field[obj]
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
     * Map转Object
     */
    fun mapToObject(map: MutableMap<*, *>?, beanClass: Class<*>?): Any? {
        return try {
            if (map == null || beanClass == null) return null
            val obj = beanClass.newInstance()
            val fields = getAllFields(obj)
            for (field in fields!!) {
                val mod = field!!.modifiers
                if (Modifier.isStatic(mod) || Modifier.isFinal(mod)) {
                    continue
                }
                field.isAccessible = true
                if (map.containsKey(field.name)) {
                    field.set(obj, map[field.name])
                }
            }
            obj
        } catch (ignore: Exception) {
            null
        }
    }

    private fun getAllFields(`object`: Any?): MutableList<Field?>? {
        if (`object` == null) return null
        var clazz: Class<*>? = `object`.javaClass
        val fieldList: MutableList<Field?> = ArrayList()
        while (clazz != null) {
            fieldList.addAll(ArrayList(listOf(*clazz.declaredFields)))
            clazz = clazz.superclass
        }
        return fieldList
    }
}