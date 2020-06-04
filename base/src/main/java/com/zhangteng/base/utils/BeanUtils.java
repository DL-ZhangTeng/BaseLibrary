package com.zhangteng.base.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BeanUtils {
    /**
     * Object转Map
     */
    public static Map<String, Object> getObjectToMap(Object obj) {
        Map<String, Object> map = new HashMap<>();
        Class<?> clazz = obj.getClass();
        System.out.println(clazz);
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            try {
                String fieldName = field.getName();
                Object value = field.get(obj);
                if (value == null) {
                    value = "";
                }
                map.put(fieldName, value);
            } catch (Exception ignore) {
            }
        }
        return map;
    }

    /**
     * Map转Object
     */
    public static Object mapToObject(Map map, Class<?> beanClass) {
        try {
            if (map == null)
                return null;
            Object obj = beanClass.newInstance();
            Field[] fields = getAllFields(obj);
            for (Field field : fields) {
                int mod = field.getModifiers();
                if (Modifier.isStatic(mod) || Modifier.isFinal(mod)) {
                    continue;
                }
                field.setAccessible(true);
                if (map.containsKey(field.getName())) {
                    field.set(obj, map.get(field.getName()));
                }
            }

            return obj;
        } catch (Exception ignore) {
            return null;
        }
    }

    private static Field[] getAllFields(Object object) {
        Class clazz = object.getClass();
        List<Field> fieldList = new ArrayList<>();
        while (clazz != null) {
            fieldList.addAll(new ArrayList<>(Arrays.asList(clazz.getDeclaredFields())));
            clazz = clazz.getSuperclass();
        }
        Field[] fields = new Field[fieldList.size()];
        fieldList.toArray(fields);
        return fields;
    }
}
