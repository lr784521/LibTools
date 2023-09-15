package com.huayun.lib_tools.util;

import android.app.Activity;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.hjq.permissions.OnPermissionCallback;
import com.hjq.permissions.OnPermissionPageCallback;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;

import java.util.List;

/**
 * 权限请求工具
 */
public class PermissionsUtil {

    /**
     * 权限请求
     *
     * @param context
     * @param callback    结果回调
     * @param permissions 权限集合
     */
    public static void requestPermissions(Context context, OnPermissionCallback callback, String... permissions) {
        XXPermissions.with(context)
                .permission(permissions)
                .request(callback);
    }


    /**
     * 获取权限操作对象
     * // 设置不触发错误检测机制（全局设置）
     * XXPermissions.setCheckMode(false);
     * // 设置权限申请拦截器（全局设置）
     * XXPermissions.setInterceptor(new IPermissionInterceptor() {});
     *
     * @return
     */
    public static XXPermissions getPermissions(Context context) {
        return XXPermissions.with(context);
    }

    /**
     * 判断一个或多个权限是否全部授予了
     *
     * @param context
     * @param permissions
     * @return
     */
    public static boolean isGranted(Context context, String... permissions) {
        return XXPermissions.isGranted(context, permissions);
    }

    /**
     * 获取没有授予的权限
     *
     * @param context
     * @param permissions
     * @return
     */
    public static List<String> getDenied(Context context, String... permissions) {
        return XXPermissions.getDenied(context, permissions);
    }

    /**
     * 判断某个权限是否为特殊权限
     *
     * @param permissions
     * @return
     */
    public static boolean isSpecial(String permissions) {
        return XXPermissions.isSpecial(permissions);
    }

    /**
     * 判断一个或多个权限是否被永久拒绝了（一定要在权限申请的回调方法中调用才有效果）
     *
     * @param permissions
     * @return
     */
    public static boolean isPermanentDenied(Activity activity, String... permissions) {
        return XXPermissions.isPermanentDenied(activity, permissions);
    }

    /**
     * 跳转到应用权限设置页
     *
     * @param activity
     * @param callback
     * @param permission
     */
    public static void startPermissionActivity(Activity activity, OnPermissionPageCallback callback, String... permission) {
        if (callback == null) {
            XXPermissions.startPermissionActivity(activity, permission);
        } else {
            XXPermissions.startPermissionActivity(activity, permission, callback);
        }
    }

    /**
     * 跳转到应用权限设置页
     *
     * @param fragment
     * @param callback
     * @param permission
     */
    public static void startPermissionActivity(Fragment fragment, OnPermissionPageCallback callback, String... permission) {
        if (callback == null) {
            XXPermissions.startPermissionActivity(fragment, permission);
        } else {
            XXPermissions.startPermissionActivity(fragment, permission, callback);
        }
    }

//    XXPermissions.with(this)
//            // 申请单个权限
//            .permission(Permission.RECORD_AUDIO)
//    // 申请多个权限
//        .permission(Permission.Group.CALENDAR)
//    // 设置权限请求拦截器（局部设置）
//    //.interceptor(new PermissionInterceptor())
//    // 设置不触发错误检测机制（局部设置）
//    //.unchecked()
//        .request(new OnPermissionCallback() {
//
//        @Override
//        public void onGranted(@NonNull List<String> permissions, boolean allGranted) {
//            if (!allGranted) {
//                toast("获取部分权限成功，但部分权限未正常授予");
//                return;
//            }
//            toast("获取录音和日历权限成功");
//        }
//
//        @Override
//        public void onDenied(@NonNull List<String> permissions, boolean doNotAskAgain) {
//            if (doNotAskAgain) {
//                toast("被永久拒绝授权，请手动授予录音和日历权限");
//                // 如果是被永久拒绝就跳转到应用权限系统设置页面
//                XXPermissions.startPermissionActivity(context, permissions);
//            } else {
//                toast("获取录音和日历权限失败");
//            }
//        }
//    });
}
