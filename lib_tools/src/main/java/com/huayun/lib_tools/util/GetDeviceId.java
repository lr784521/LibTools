package com.huayun.lib_tools.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import androidx.annotation.RequiresApi;

import com.huayun.lib_tools.util.encryption.MD5Utils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 获取设备唯一标识
 */
public class GetDeviceId {

    private static Map<String, String> infoMap = new HashMap<>();

    /**
     * 收集日志
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static String collectInfo(Context context) {
        PackageManager packageManager = context.getPackageManager();
        try {
            //获取包信息
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), PackageManager.GET_ACTIVITIES);
            String versionName = TextUtils.isEmpty(packageInfo.versionName) ? "未设置版本名" : packageInfo.versionName;
            String versionCode = packageInfo.versionCode + "";
            infoMap.put("versionName", versionName);
            infoMap.put("versionCode", versionCode);

            //获取手机系统硬件信息
            //反射Build里所有成员常量
            Field[] fields = Build.class.getFields();
            if (fields != null && fields.length > 0) {
                for (Field field : fields) {
                    field.setAccessible(true);
                    infoMap.put(field.getName(), field.get(null).toString());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        infoMap.put("DeviceId",getDeviceId(context));
        return GsonUtil.gsonString(infoMap);
    }

    public static String getDeviceId(Context context) {
        StringBuilder sbDeviceId = new StringBuilder();

        String imei = getIMEI(context);

        String androidID = getAndroidId(context);

        String serial = getSerial();

        String id = getDeviceUUID().replace("-", "");
        //追加imei
        if (imei != null && imei.length() > 0) {
            sbDeviceId.append(imei);
            sbDeviceId.append("|");
        }
        //追加androidid
        if (androidID != null && androidID.length() > 0) {
            sbDeviceId.append(androidID);
            sbDeviceId.append("|");
        }
        //追加serial
        if (serial != null && serial.length() > 0) {
            sbDeviceId.append(serial);
            sbDeviceId.append("|");
        }
        //追加硬件uuid
        if (id != null && id.length() > 0) {
            sbDeviceId.append(id);
        }

        if (sbDeviceId.length() > 0) {
            return MD5Utils.md5(sbDeviceId.toString());
        }
        return null;
    }


    // //获得硬件uuid（根据硬件相关属性，生成uuid）（无需权限）  数字  0   -10
    private static String getDeviceUUID() {
        String dev = "yida" + Build.BOARD +
                Build.BRAND +
                Build.DEVICE +
                Build.HARDWARE +
                Build.ID +
                Build.MODEL +
                Build.PRODUCT +
                Build.SERIAL;
        return new UUID(dev.hashCode(), Build.SERIAL.hashCode()).toString();
    }

    private static String getSerial() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                return Build.getSerial();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    /**
     * 获得设备的AndroidId
     *
     * @param context 上下文
     * @return 设备的AndroidId
     */
    private static String getAndroidId(Context context) {
        try {
            return Settings.Secure.getString(context.getContentResolver(),
                    Settings.Secure.ANDROID_ID);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }

    //需要获得READ_PHONE_STATE权限，>=6.0，默认返回null
    private static String getIMEI(Context context) {
        try {
            TelephonyManager tm = (TelephonyManager)
                    context.getSystemService(Context.TELEPHONY_SERVICE);
            return tm.getDeviceId();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }

}
