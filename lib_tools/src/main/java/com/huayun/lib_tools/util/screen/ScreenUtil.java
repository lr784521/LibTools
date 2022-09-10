package com.huayun.lib_tools.util.screen;

import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.WindowManager;

public class ScreenUtil {

    /**
     * 设置全屏(true 全屏, false 取消全屏)
     *
     * @param pFullScreen
     */
    protected void setfullScreen(Activity activity, boolean pFullScreen) {
        WindowManager.LayoutParams attrs = activity.getWindow().getAttributes();
        if (pFullScreen) {
            attrs.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
            activity.getWindow().setAttributes(attrs);
            // 全屏设置
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        } else {
            attrs.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
            activity.getWindow().setAttributes(attrs);
            // 取消全屏设置
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
    }

    /**
     * 获取屏幕宽，单位为dip
     *
     * @return
     */
    public static int getScreenWidth(Activity activity) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    /**
     * 获取屏幕高，单位为dip
     *
     * @return
     */
    public static int getScreenHeight(Activity activity) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;
    }

}
