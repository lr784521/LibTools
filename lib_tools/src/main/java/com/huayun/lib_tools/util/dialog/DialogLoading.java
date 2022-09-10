package com.huayun.lib_tools.util.dialog;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.huayun.lib_tools.R;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.impl.LoadingPopupView;


/**
 * 提示弹窗
 */
public class DialogLoading {
    private ProgressBar progressBar;
    private ImageView ivImage;
    private TextView tvMsg;
    //弹窗
    private LoadingPopupView loadingPopup;

    private Context context;

    public DialogLoading(Context context) {
        this.context = context;
    }

    /**
     * 显示弹窗
     */
    public void show() {
        show(context.getString(R.string.loading_in));
    }

    /**
     * 显示弹窗
     *
     * @param msg 提示内容
     */
    public void show(String msg) {
        if (loadingPopup == null) {
            loadingPopup = new XPopup.Builder(context)
                    .dismissOnBackPressed(true) //按返回键是否关闭弹窗，默认为true
                    .isLightNavigationBar(true)
                    .dismissOnTouchOutside(false)//点击外部是否关闭弹窗，默认为true
                    .asLoading(null, R.layout.dialog_loading, LoadingPopupView.Style.ProgressBar);
        }
        progressBar = loadingPopup.findViewById(R.id.progress_bar);
        ivImage = loadingPopup.findViewById(R.id.iv_image);
        tvMsg = loadingPopup.findViewById(R.id.tv_msg);
        progressBar.setVisibility(View.VISIBLE);
        ivImage.setVisibility(View.GONE);
        tvMsg.setText(msg);
        loadingPopup.show();
    }

    /**
     * 直接关闭弹窗
     */
    public void dismiss() {
        if (loadingPopup != null) {
            loadingPopup.delayDismiss(0);
            loadingPopup = null;
        }
    }

    /**
     * 显示结果 并关闭弹窗
     *
     * @param type 提示类型
     * @param msg  内容
     */
    public void dismissShowMsg(DialogLoadingResult type, String msg) {
        if (loadingPopup != null) {
            loadingPopup.postDelayed(new Runnable() {
                @Override
                public void run() {
                    progressBar.setVisibility(View.GONE);
                    ivImage.setVisibility(View.VISIBLE);
                    switch (type) {
                        case SUCCESS:
                            ivImage.setImageResource(R.drawable.base_iv_loading_tips_success);
                            break;
                        case ERROR:
                            ivImage.setImageResource(R.drawable.base_iv_loading_tips_error);
                            break;
                        case WARN:
                            ivImage.setImageResource(R.drawable.base_iv_loading_tips_warn);
                            break;
                    }
                    tvMsg.setText(msg);
                }
            }, 50);
            loadingPopup.delayDismissWith(1000, new Runnable() {
                @Override
                public void run() {
                }
            });
        }
    }

}
