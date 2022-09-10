package com.huayun.lib_tools.util;

import android.os.CountDownTimer;
import android.widget.TextView;


/**
 * 发送短信专用倒计时
 */
public class CountDownTimerUtils extends CountDownTimer {
    private TextView mTextView;
    private String startTips="";
    private String endTips="S";
    private String oldText;
    private int oldColor;

    public CountDownTimerUtils(TextView textView, long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
        this.mTextView = textView;
        saveOdlStyle();
    }
    public CountDownTimerUtils(TextView textView, long millisInFuture, long countDownInterval,String startTips,String endTips) {
        super(millisInFuture, countDownInterval);
        this.mTextView = textView;
        this.startTips = startTips;
        this.endTips = endTips;
        saveOdlStyle();
    }

    private void saveOdlStyle(){
        oldText=mTextView.getText().toString();
        oldColor = mTextView.getTextColors().getDefaultColor();
    }

    @Override
    public void onTick(long millisUntilFinished) {
        mTextView.setClickable(false); //设置不可点击
        mTextView.setText(startTips+millisUntilFinished / 1000+endTips);  //设置倒计时时间
    }

    @Override
    public void onFinish() {
        reSet();
    }

    public void stop(){
        cancel();
        reSet();
    }

    private void reSet(){
        mTextView.setText(oldText);
        mTextView.setTextColor(oldColor);
        mTextView.setClickable(true);//重新获得点击
    }
}