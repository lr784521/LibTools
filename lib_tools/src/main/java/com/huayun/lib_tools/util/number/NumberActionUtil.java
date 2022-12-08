package com.huayun.lib_tools.util.number;

import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import com.huayun.lib_tools.util.StringUtil;
import com.huayun.lib_tools.util.softkeyboard.SoftKeyboardStateHelper;

import java.math.BigDecimal;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 数量点击+长按 加减操作
 */
public class NumberActionUtil {
    private static NumberActionUtil instance;
    private ScheduledExecutorService scheduledExecutor;  //定时加减价格任务
    private EditText editText;//输入框控件
    private View add;//增加控件
    private View reduce;//减小控件
    private String defNum;//默认数值
    private String maxNum;//最大数值
    private String minNum;//最小数值
    private String addNum;//每次累加的数值
    private String reduceNum;//每次减小的数值

    public static NumberActionUtil getInstance() {
        if (instance == null) {
            synchronized (NumberActionUtil.class) {
                if (instance == null) {
                    instance = new NumberActionUtil();
                }
            }
        }
        return instance;
    }

    /**
     * 初始化参数
     *
     * @param editText  输入框控件
     * @param add       增加控件
     * @param reduce    减小控件
     * @param defNum    默认数值
     * @param maxNum    最大数值
     * @param minNum    最小数值
     * @param actionNum 每次加减的数值
     */
    public void initEditAction(EditText editText, View add, View reduce, String defNum, String maxNum, String minNum, String actionNum) {
        this.editText = editText;
        this.add = add;
        this.reduce = reduce;
        this.defNum = defNum;
        this.maxNum = maxNum;
        this.minNum = minNum;
        this.addNum = actionNum;
        this.reduceNum = actionNum;
        initListener();
    }

    /**
     * 初始化参数
     *
     * @param editText  输入框控件
     * @param add       增加控件
     * @param reduce    减小控件
     * @param defNum    默认数值
     * @param maxNum    最大数值
     * @param minNum    最小数值
     * @param addNum    每次累加的数值
     * @param reduceNum 每次减小的数值
     */
    public void initEditAction(EditText editText, View add, View reduce, String defNum, String maxNum, String minNum, String addNum, String reduceNum) {
        this.editText = editText;
        this.add = add;
        this.reduce = reduce;
        this.defNum = defNum;
        this.maxNum = maxNum;
        this.minNum = minNum;
        this.addNum = addNum;
        this.reduceNum = reduceNum;
        initListener();
    }

    /**
     * 初始化监听
     */
    private void initListener() {
        //监听输入框软键盘打开/关闭
        SoftKeyboardStateHelper softKeyboardStateHelper = new SoftKeyboardStateHelper(editText);
        softKeyboardStateHelper.addSoftKeyboardStateListener(new SoftKeyboardStateHelper.SoftKeyboardStateListener() {
            @Override
            public void onSoftKeyboardOpened(int keyboardHeightInPx) {
            }

            @Override
            public void onSoftKeyboardClosed() {
                String num = editText.getText().toString();
                if (StringUtil.isBlank(num) || BigDecimalUtil.numberIsEqualZero(new BigDecimal(num))) {
                    editText.setText(defNum);
                } else if (BigDecimalUtil.numberIsBigNum(new BigDecimal(num), new BigDecimal(maxNum))) {
                    editText.setText(maxNum);
                }
                editText.clearFocus();
            }
        });
        //设置减号触摸事件
        reduce.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    String num = editText.getText().toString();
                    if (!StringUtil.isBlank(num) && BigDecimalUtil.numberIsBigNum(new BigDecimal(num), new BigDecimal(minNum))) {
                        updateAddOrSubtract(v.getId());    //手指按下时触发不停的发送消息
                    }
                } else if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
                    stopAddOrSubtract();    //手指抬起时停止发送
                }
                return true;
            }
        });
        //设置减号点击事件
        reduce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                numAction(false);
            }
        });
        //设置加号触摸事件
        add.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    String num = editText.getText().toString();
                    if (!StringUtil.isBlank(num) && BigDecimalUtil.numberIsSmallNum(new BigDecimal(num), new BigDecimal(maxNum))) {
                        updateAddOrSubtract(v.getId());    //手指按下时触发不停的发送消息
                    }
                } else if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
                    stopAddOrSubtract();    //手指抬起时停止发送
                }
                return true;
            }
        });
        //设置加号点击事件
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                numAction(true);
            }
        });
    }

    /**
     * 数量加减
     *
     * @param type true +  false -
     */
    private void numAction(boolean type) {
        String number = editText.getText().toString();
        if (!StringUtil.isBlank(number)) {
            BigDecimal num = new BigDecimal(number);
            if (BigDecimalUtil.numberIsBigZero(num)) {
                String result;
                if (type) {
                    if (BigDecimalUtil.numberIsSmallNum(num, new BigDecimal(maxNum))) {
                        result = BigDecimalUtil.bigdecimalAdd(num, new BigDecimal(addNum), 0);
                    } else {
                        result = maxNum;
                        stopAddOrSubtract();    //停止发送
                    }
                } else {
                    if (BigDecimalUtil.numberIsBigNum(num, new BigDecimal(minNum))) {
                        result = BigDecimalUtil.bigdecimalSubtract(num, new BigDecimal(reduceNum), 0);
                    } else {
                        result = minNum;
                        stopAddOrSubtract();    //停止发送
                    }
                }
                editText.setText(result);
            } else {
                editText.setText(minNum);
            }
        } else {
            editText.setText(minNum);
        }
    }

    /**
     * 发送定时任务  加减价格
     *
     * @param viewId
     */
    private void updateAddOrSubtract(int viewId) {
        final int vid = viewId;
        scheduledExecutor = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutor.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                Message msg = new Message();
                msg.what = vid;
                handler.sendMessage(msg);
            }
        }, 0, 100, TimeUnit.MILLISECONDS);    //每间隔100ms发送Message
    }

    /**
     * 定时任务修改价格数量
     */
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int viewId = msg.what;
            if (viewId == reduce.getId()) {
                numAction(false);    //减小操作
            } else if (viewId == add.getId()) {
                numAction(true);    //增大操作
            }
        }
    };

    /**
     * 取消定时任务
     */
    private void stopAddOrSubtract() {
        if (scheduledExecutor != null) {
            scheduledExecutor.shutdownNow();
            scheduledExecutor = null;
        }
    }
}
