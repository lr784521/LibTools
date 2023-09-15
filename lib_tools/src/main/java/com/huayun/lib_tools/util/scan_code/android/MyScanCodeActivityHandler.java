package com.huayun.lib_tools.util.scan_code.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import com.google.zxing.Result;
import com.huayun.lib_tools.util.scan_code.camera.CameraManager;
import com.huayun.lib_tools.util.scan_code.common.Constant;
import com.huayun.lib_tools.util.scan_code.decode.DecodeThread;
import com.huayun.lib_tools.util.scan_code.listener.WidScanCodeListener;
import com.huayun.lib_tools.util.scan_code.view.ViewfinderResultPointCallback;

/**
 * This class handles all the messaging which comprises the state machine for
 * capture. 该类用于处理有关拍摄状态的所有信息
 *
 * @author dswitkin@google.com (Daniel Switkin)
 */
public final class MyScanCodeActivityHandler extends Handler {

    private final WidScanCodeListener activity;
    private final DecodeThread decodeThread;
    private State state;
    private final CameraManager cameraManager;

    private enum State {
        PREVIEW, SUCCESS, DONE
    }

    public MyScanCodeActivityHandler(WidScanCodeListener activity, CameraManager cameraManager) {
        this.activity = activity;
        decodeThread = new DecodeThread(activity,  new ViewfinderResultPointCallback(
                activity.getViewfinderView()));
        decodeThread.start();
        state = State.SUCCESS;

        // Start ourselves capturing previews and decoding.
        // 开始拍摄预览和解码
        this.cameraManager = cameraManager;
        cameraManager.startPreview();
        restartPreviewAndDecode();
    }

    @Override
    public void handleMessage(Message message) {
        switch (message.what) {
            case Constant.RESTART_PREVIEW:
                // 重新预览
                restartPreviewAndDecode();
                break;
            case Constant.DECODE_SUCCEEDED:
                // 解码成功
                state = State.SUCCESS;
                activity.handleDecode((Result) message.obj);
                break;
            case Constant.DECODE_FAILED:
                // 尽可能快的解码，以便可以在解码失败时，开始另一次解码
                state = State.PREVIEW;
                cameraManager.requestPreviewFrame(decodeThread.getHandler(),
                        Constant.DECODE);
                break;
            case Constant.RETURN_SCAN_RESULT:

                activity.scanSetResult(Activity.RESULT_OK, (Intent) message.obj);
                activity.scanFinish();
                break;
            case Constant.FLASH_OPEN:
                activity.switchFlashImg(Constant.FLASH_OPEN);
                break;
            case Constant.FLASH_CLOSE:
                activity.switchFlashImg(Constant.FLASH_CLOSE);
                break;
        }
    }

    /**
     * 完全退出
     */
    public void quitSynchronously() {
        state = State.DONE;
        cameraManager.stopPreview();
        Message quit = Message.obtain(decodeThread.getHandler(), Constant.QUIT);
        quit.sendToTarget();
        try {
            // Wait at most half a second; should be enough time, and onPause()
            // will timeout quickly
            decodeThread.join(500L);
        } catch (InterruptedException e) {
            // continue
        }

        // Be absolutely sure we don't send any queued up messages
        //确保不会发送任何队列消息
        removeMessages(Constant.DECODE_SUCCEEDED);
        removeMessages(Constant.DECODE_FAILED);
    }

    public void restartPreviewAndDecode() {
        if (state == State.SUCCESS) {
            state = State.PREVIEW;
            cameraManager.requestPreviewFrame(decodeThread.getHandler(),
                    Constant.DECODE);
            activity.drawViewfinder();
        }
    }

}
