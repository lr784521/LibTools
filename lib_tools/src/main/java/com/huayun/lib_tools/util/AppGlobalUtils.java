package com.huayun.lib_tools.util;

import android.app.Application;

import java.lang.reflect.Method;

/**
 * 反射获取全局上下文
 */
public class AppGlobalUtils {
    private static Application myApp;
    public static Application getApplication() {
        if(myApp==null){
            try {
                Method currentApplication = Class.forName("android.app.ActivityThread").
                        getDeclaredMethod("currentApplication");
                myApp = (Application) currentApplication.invoke(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return myApp;
    }
}
