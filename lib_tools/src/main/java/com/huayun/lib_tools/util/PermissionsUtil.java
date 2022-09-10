package com.huayun.lib_tools.util;

import android.content.Context;

import com.hjq.permissions.OnPermissionCallback;
import com.hjq.permissions.XXPermissions;


/**
 * 权限请求工具
 */
public class PermissionsUtil {

    /**
     * 权限请求
     * @param context
     * @param callback 结果回调
     * @param permissions 权限集合
     */
    public static void requestPermissions(Context context, OnPermissionCallback callback, String... permissions) {
        XXPermissions.with(context)
                .permission(permissions)
                .request(callback);
    }
}
