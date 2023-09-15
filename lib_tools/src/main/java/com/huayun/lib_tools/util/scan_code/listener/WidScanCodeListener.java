package com.huayun.lib_tools.util.scan_code.listener;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.view.SurfaceHolder;

import com.google.zxing.Result;
import com.huayun.lib_tools.util.scan_code.bean.ZxingConfig;
import com.huayun.lib_tools.util.scan_code.camera.CameraManager;
import com.huayun.lib_tools.util.scan_code.view.ViewfinderView;

/**
 * 二维码扫描回调
 */
public interface WidScanCodeListener {


    /**
     * 初始化相机
     */
    void initCamera(SurfaceHolder surfaceHolder);

    /**
     * 初始化相机失败
     */
    void initCameraError(String msg);

    /**
     * 扫码取景框控件
     *
     * @return
     */
     ViewfinderView getViewfinderView();

    /**
     * @param rawResult 返回的扫描结果
     */
     void handleDecode(Result rawResult);

    /**
     * 切换闪光灯图片
     * @param flashState  8打开 9关闭
     */
     void switchFlashImg(int flashState);

    /**
     * 绘制取景器
     */
     void drawViewfinder();


    /**
     * 获取相机管理
     * @return
     */
    CameraManager getCameraManager();

    /**
     * 获取Handler
     * @return
     */
    Handler getHandler();

    /**
     * @param pm
     * @return 是否有闪光灯
     */
    boolean isSupportCameraLedFlash(PackageManager pm);

    /**
     * 扫码成功
     * @param result
     */
    void scanCodeSuccess(Result result);

    /**
     * SetResult
     * @param resultCode
     * @param data
     */
    void scanSetResult(int resultCode, Intent data);

    /**
     * 关闭界面
     */
    void scanFinish();

    /**
     * 获取配置
     * @return
     */
    ZxingConfig getZxingConfig();
}
