package com.huayun.lib_tools.util.database;

import android.content.Context;
import android.os.Parcelable;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.huayun.lib_tools.util.GsonUtil;
import com.tencent.mmkv.MMKV;

import java.util.ArrayList;
import java.util.List;

/**
 * 本地持久化
 */
public class MMkvHelper {

    private static MMKV mmkv;
    private static final MMkvHelper INSTANCE = new MMkvHelper();

    private MMkvHelper() {

    }

    public static void init(Context context) {
        MMKV.initialize(context);
        if (mmkv == null) {
            mmkv = MMKV.defaultMMKV();
        }
    }

    public static MMkvHelper getInstance() {
        return INSTANCE;
    }


    public MMKV getMmkv() {
        return mmkv;
    }

    /**
     * 存储实体
     * @param key
     * @param tClass
     * @param <T>
     */
    public <T extends Parcelable> void saveBean(String key, T tClass){
        mmkv.encode(key, tClass);
    }

    /**
     * 获取实体
     * @param key
     * @param tClass
     * @param <T>
     * @return
     */
    public <T extends Parcelable> T getBean(String key, Class<T> tClass) {
        return mmkv.decodeParcelable(key, tClass);
    }


    /**
     * 保存list
     */
    public <T> void saveList(String tag, List<T> dataList) {
        if (null == dataList || dataList.size() <= 0)
            return;
        //转换成json数据，再保存
        String strJson = GsonUtil.getGson().toJson(dataList);
        mmkv.encode(tag, strJson);
    }

    /**
     * 获取list
     */
    public <T> List<T> getTList(String tag, Class<T> cls) {
        List<T> dataList = new ArrayList<>();
        String strJson = mmkv.decodeString(tag, null);
        if (null == strJson) {
            return dataList;
        }
        try {
            Gson gson = new Gson();
            JsonArray arry = new JsonParser().parse(strJson).getAsJsonArray();
            for (JsonElement jsonElement : arry) {
                dataList.add(gson.fromJson(jsonElement, cls));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataList;
    }

    public void remove(String key) {
        mmkv.remove(key);
    }

    public void saveString(String key, String value) {
        mmkv.encode(key, value);
    }

    public void saveDouble(String key, Double value) {
        mmkv.encode(key, value);
    }

    public void saveInt(String key, Integer value) {
        mmkv.encode(key, value);
    }

    public void saveBoolean(String key, Boolean value) {
        mmkv.encode(key, value);
    }

    public String getString(String key, String value) {
        return mmkv.decodeString(key, value);
    }

    public double getDouble(String key, Double value) {
        return mmkv.decodeDouble(key, value);
    }

    public int getInt(String key, Integer value) {
        return mmkv.decodeInt(key, value);
    }

    public boolean getBoolean(String key, Boolean value) {
        return mmkv.decodeBool(key, value);
    }


}
