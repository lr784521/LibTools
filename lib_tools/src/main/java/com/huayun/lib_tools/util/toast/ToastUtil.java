package com.huayun.lib_tools.util.toast;

import android.app.Application;
import android.view.Gravity;

import com.hjq.toast.ToastUtils;
import com.huayun.lib_tools.R;

/**
 * Toast 提示
 */
public class ToastUtil {
    private static ToastUtil instance;

    private ToastUtil(){}

    public static ToastUtil getInstance() {
        if (instance == null) {
            synchronized (ToastUtil.class) {
                if (instance == null) {
                    instance = new ToastUtil();
                }
            }
        }
        return instance;
    }

    /**
     * 初始化 Toast 框架
     * @param app
     */
    public void init(Application app){
        ToastUtils.init(app);
    }

    public void showToast(ToastEnum type, String msg) {
        ToastUtils.setView(getLayout(type));
        ToastUtils.setGravity(Gravity.CENTER);
        ToastUtils.show(msg);
    }

    public void showToast(ToastEnum type, int msg) {
        ToastUtils.setView(getLayout(type));
        ToastUtils.setGravity(Gravity.CENTER);
        ToastUtils.show(msg);
    }

    /**
     * 获取布局
     * @param type  0 成功 1失败 2警告
     * @return
     */
    private int getLayout(ToastEnum type) {
        switch (type) {
            case SUCCESS:
                return R.layout.item_toast_suc;
            case ERROR:
                return R.layout.item_toast_err;
            case WARN:
                return R.layout.item_toast_warn;
        }
        return 0;
    }

}
