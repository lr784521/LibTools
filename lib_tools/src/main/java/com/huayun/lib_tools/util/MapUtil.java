package com.huayun.lib_tools.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class MapUtil {

    /**
     * 根据Map 键值获取Map对象
     * @param map
     * @param key
     * @return
     */
    public static Map<String, Object> getMap(Map<String, Object> map, String key) {
        if (map == null || key == null) {
            return null;
        }
        Object value = map.get(key);
        if (value instanceof Map) {
            return (Map) value;
        }
        return null;
    }

    /**
     * 判断Map value 是否是Number类型  并获取value值
     * @param map
     * @param key
     * @return
     */
    public static Long getLong(Map<String, Object> map, String key) {
        if (map == null || key == null) {
            return null;
        }
        Object value = map.get(key);
        if (value == null) {
            return null;
        }
        if (value instanceof Number) {
            return ((Number) value).longValue();
        }
        try {
            return Long.parseLong(value.toString());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * 盘点Map value值是否是List类型 并获取value
     * @param map
     * @param key
     * @return
     */
    public static List<Long> getLongList(Map<String, Object> map, String key) {
        if (map == null || key == null) {
            return Collections.EMPTY_LIST;
        }
        Object value = map.get(key);
        if (value == null) {
            return Collections.EMPTY_LIST;
        }
        if (value instanceof List) {
            List<Object> list = (List) value;
            List<Long> longValues = new ArrayList<Long>();
            for (Object i : list) {
                if (i instanceof Number) {
                    longValues.add(((Number) i).longValue());
                }
            }
            return longValues;
        }
        return Collections.EMPTY_LIST;
    }
}
