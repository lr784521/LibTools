package com.huayun.lib_tools.util;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GsonUtil {

    private static Gson gson = null;

    static {
        if (gson == null) {
            gson = new Gson();
        }
    }

    private GsonUtil() {
    }

    public static Gson getGson() {
        return gson;
    }

    /**
     * 转成json 字符串
     *
     * @param object
     * @return
     */
    public static String toJson(Object object) {
        String gsonString = null;
        if (gson != null && object != null) {
            gsonString = gson.toJson(object);
        }
        return gsonString;
    }

    /**
     * Json字符串转成实体类
     *
     * @param content
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T gsonToBean(String content, Class<T> clazz) {
        if (StringUtil.isBlank(content) || clazz == null) {
            return null;
        }
        try {
            return gson.fromJson(content, clazz);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Json字符串转成实体类
     *
     * @param content
     * @param token
     * @param <T>
     * @return
     */
    public static <T> T gsonToBean(String content, TypeToken<T> token) {
        if (StringUtil.isBlank(content) || token == null) {
            return null;
        }
        try {
            return gson.fromJson(content, token.getType());
        } catch (JsonSyntaxException e) {
            return null;
        }
    }

    /**
     * 把json转成对应的类型。适合用于自定义数据类型，如ArrayList<Foo>等
     *
     * @param content json
     * @param type    自定义类型的token。使用方法如下
     *                Type listType = new TypeToken<ArrayList<Foo>>(){}.getType();
     * @param <T>
     * @return 对应类型的对象
     */
    public static <T> T gsonToBean(String content, Type type) {
        if ((content != null && content != "") && type != null) {
            try {
                return gson.fromJson(content, type);
            } catch (JsonSyntaxException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 转成带list的bean
     *
     * @param gsonString
     * @param cls
     * @return
     */
    public static <T> T gsonToListBean(String gsonString, Class<T> cls) {
        T t = null;
        if (gson != null) {
            t = gson.fromJson(gsonString, cls);
        }
        return t;
    }

    /**
     * JSon转成list
     *
     * @param json
     * @param cls
     * @return
     */
    public static <T> List<T> gsonToList(String json, Class<T> cls) {
        List<T> dataList = new ArrayList<>();
        if (StringUtil.isBlank(json)) {
            return dataList;
        }
        try {
            JsonArray array = new JsonParser().parse(json).getAsJsonArray();
            for (JsonElement jsonElement : array) {
                dataList.add(gson.fromJson(jsonElement, cls));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataList;
    }

    /**
     * 转成list中有map的
     *
     * @param gsonString
     * @return
     */
    public static <T> List<Map<String, T>> gsonToListMaps(String gsonString) {
        List<Map<String, T>> list = null;
        if (gson != null) {
            list = gson.fromJson(gsonString,
                    new TypeToken<List<Map<String, T>>>() {
                    }.getType());
        }
        return list;
    }

    /**
     * Json字符串转Map
     *
     * @param gsonString
     * @return
     */
    public static <T> Map<String, T> gsonToMaps(String gsonString) {
        Map<String, T> map = null;
        if (gson != null) {
            map = gson.fromJson(gsonString, new TypeToken<Map<String, T>>() {
            }.getType());
        }
        return map;
    }

    /**
     * 对象转Map
     *
     * @param obj
     * @return
     */
    public static Map<String, Object> objToMap(Object obj) {
        JsonElement element = gson.toJsonTree(obj);
        return gson.fromJson(element, Map.class);
    }

    /**
     * Obj转实体
     *
     * @param obj
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T fromObject(Object obj, Class<T> clazz) {
        JsonElement element = gson.toJsonTree(obj);
        return gson.fromJson(element, clazz);
    }

    /**
     * Obj转实体
     *
     * @param obj
     * @param token
     * @param <T>
     * @return
     */
    public static <T> T fromObject(Object obj, TypeToken<T> token) {
        JsonElement element = gson.toJsonTree(obj);
        return gson.fromJson(element, token.getType());
    }

    /**
     * 从json中搜索，根据键的名字，返回值。
     * @param json
     * @param name json中的键名
     * @return Object
     */
    public static Object findObject(String json , String name){
        Object object = null;
        if(StringUtil.isBlank(json) || StringUtil.isBlank(name)){
            return null;
        }
        try {
            JSONObject jsonobject = new JSONObject(json);
            if(!jsonobject.has(name)){
                return null;
            } else {
                object = jsonobject.get(name);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object;
    }

    /**
     * 判断字符串是否是Json
     * @param content
     * @return
     */
    public static boolean isJson(String content) {
        try {
            JSONObject jsonStr = new JSONObject(content);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 是否是JSON数组
     * @param content
     * @return
     */
    public static boolean isJsonList(String content) {
        if (StringUtil.isBlank(content)) {
            return false;
        }
        try {
            JsonArray arry = new JsonParser().parse(content).getAsJsonArray();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}