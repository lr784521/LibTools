package com.huayun.lib_tools.util.time;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

/**
 * 倒计时 天/时/分/秒
 */
public class TimerView extends LinearLayout {
    private static final int START_TIME = 0X001;
    private static final String ZERO_FILL = "0";

    private final long M_FOR_S = 30 * 24 * 60 * 60;
    private final long DAY_FOR_S = 24 * 60 * 60;
    private final long HOUR_FOR_S = 60 * 60;
    private final long MIN_FOR_S = 60;

    //计时器开关
    private boolean isStart = false;
    //view 对象池
    private Map<Byte, TimeItemView> viewPool = new HashMap<>();
    private final byte DAY_TAG = 0x04;
    private final byte HOUR_TAG = 0x03;
    private final byte MIN_TAG = 0x02;
    private final byte SECOND_TAG = 0x01;
    private final byte NULL_TAG = 0x05;

    private long mSeconds;
    private Context context;
    private int numTvTextColor;
    private int unitTvTextColor;

    public void setOnTimerListener(OnTimerListener onTimerListener) {
        this.onTimerListener = onTimerListener;
    }

    private OnTimerListener onTimerListener;

    public TimerView(Context context) {
        super(context);
        init(context);
    }

    public TimerView(Context context, AttributeSet attr) {
        super(context, attr);
        init(context);
    }

    public void init(Context context) {
        this.context = context;
        setOrientation(HORIZONTAL);
    }

    private void show(long seconds) {
        long countTimer = seconds;
//        if (seconds > M_FOR_S) {
//            LogUtils.e("暂不支持大于月的计时");
//            return;
//        }
        if (seconds > DAY_FOR_S) {
            //存在天
            if (viewPool.containsKey(DAY_TAG)) {
                TimeItemView timeItemView = viewPool.get(DAY_TAG);
                timeItemView.setNumText((countTimer / DAY_FOR_S));
            } else {
                TimeItemView timeItemView = new TimeItemView(context);
                timeItemView.setNumText((countTimer / DAY_FOR_S));
                timeItemView.setUnitText((DAY_TAG));
                addView(timeItemView);
                viewPool.put(DAY_TAG, timeItemView);
            }
            countTimer -= (countTimer / DAY_FOR_S) * DAY_FOR_S;
        } else {
            if (viewPool.get(DAY_TAG) != null && seconds == DAY_FOR_S) {
                removeView(viewPool.get(DAY_TAG));
            }
        }
        if (seconds > HOUR_FOR_S) {
            //存在时
            if (viewPool.containsKey(HOUR_TAG)) {
                TimeItemView timeItemView = viewPool.get(HOUR_TAG);
                timeItemView.setNumText((countTimer / HOUR_FOR_S));
            } else {
                TimeItemView timeItemView = new TimeItemView(context);
                timeItemView.setNumText((countTimer / HOUR_FOR_S));
                timeItemView.setUnitText((HOUR_TAG));
                this.addView(timeItemView);
                viewPool.put(HOUR_TAG, timeItemView);
            }
            countTimer -= (countTimer / HOUR_FOR_S) * HOUR_FOR_S;
        } else {
            if (viewPool.get(HOUR_TAG) != null && seconds == HOUR_FOR_S) {
                removeView(viewPool.get(HOUR_TAG));
            }
        }
        if (seconds > MIN_FOR_S) {
            //存在分
            if (viewPool.containsKey(MIN_TAG)) {
                TimeItemView timeItemView = viewPool.get(MIN_TAG);
                timeItemView.setNumText((countTimer / MIN_FOR_S));
            } else {
                TimeItemView timeItemView = new TimeItemView(context);
                timeItemView.setNumText((countTimer / MIN_FOR_S));
                timeItemView.setUnitText((MIN_TAG));
                this.addView(timeItemView);
                viewPool.put(MIN_TAG, timeItemView);
            }
            countTimer -= (countTimer / MIN_FOR_S) * MIN_FOR_S;
        } else {
            if (viewPool.get(MIN_TAG) != null && seconds == MIN_FOR_S) {
                removeView(viewPool.get(MIN_TAG));
            }
        }
        if (seconds > 0) {
            //存在S
//            if (viewPool.containsKey(SECOND_TAG)) {
//                TimeItemView timeItemView = viewPool.get(SECOND_TAG);
//                timeItemView.setNumText((countTimer));
//            } else {
//                TimeItemView timeItemView = new TimeItemView(context);
//                timeItemView.setNumText((countTimer));
//                timeItemView.setUnitText((SECOND_TAG));
//                this.addView(timeItemView);
//                viewPool.put(SECOND_TAG, timeItemView);
//            }
        } else {
            if (viewPool.get(SECOND_TAG) != null && seconds == 0) {
                removeView(viewPool.get(MIN_TAG));
                //回调
                if (onTimerListener != null) {
                    onTimerListener.timeOver();
                }
            }
        }

    }


    /**
     * 开始计时
     */
    public void startTiming(long seconds) {
        this.mSeconds = seconds;
        isStart = true;
        Message message=new Message();
        message.what=START_TIME;
        handler.sendMessageDelayed(message,1000);
    }

    /**
     * 结束计时
     */
    public void shopTiming() {
        isStart = false;
        handler.removeMessages(START_TIME);
        if (viewPool.containsKey(DAY_TAG)) {
            viewPool.get(DAY_TAG).setNumText("");
            viewPool.get(DAY_TAG).setUnitText((NULL_TAG));
        }
        if (viewPool.containsKey(HOUR_TAG)) {
            viewPool.get(HOUR_TAG).setNumText("");
            viewPool.get(HOUR_TAG).setUnitText((NULL_TAG));
        }
        if (viewPool.containsKey(MIN_TAG)) {
            viewPool.get(MIN_TAG).setNumText("");
            viewPool.get(MIN_TAG).setUnitText((NULL_TAG));
        }
        if (viewPool.containsKey(SECOND_TAG)) {
            viewPool.get(SECOND_TAG).setNumText("");
            viewPool.get(SECOND_TAG).setUnitText((NULL_TAG));
        }
        if (viewPool != null) {
            if (viewPool.size() > 0) {
                viewPool.clear();
            }
        }
    }



    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == START_TIME) {
                if (mSeconds == 0) {
                    isStart = false;
                }
                show(mSeconds--);
                if (isStart) {
                    handler.sendEmptyMessageDelayed(START_TIME, 1000);
                }
            }
        }
    };

    class TimeItemView extends LinearLayout {
        private Context context;
        private TextView numTv;
        private TextView unitTv;

        TimeItemView(Context context) {
            super(context);
            this.context = context;
            setOrientation(HORIZONTAL);
            addView(getNumTv());
            addView(getUnitTv());
        }

        public void setNumText(long num) {
            if (num < 10) {
                getNumTv().setText(ZERO_FILL + num);
            } else {
                getNumTv().setText(String.valueOf(num));
            }
        }

        public void setNumText(String str) {
            getNumTv().setText(str);
        }

        public void setUnitText(byte tag) {
            switch (tag) {
                case DAY_TAG:
                    getUnitTv().setText("天");
                    break;
                case HOUR_TAG:
                    getUnitTv().setText("时");
                    break;
                case MIN_TAG:
                    getUnitTv().setText("分");
                    break;
                case SECOND_TAG:
//                    getUnitTv().setText("秒");
                    break;
                case NULL_TAG:
                    getUnitTv().setText("");
                    break;
            }
        }

        private TextView getNumTv() {
            if (numTv == null) {
                numTv = new TextView(context);
                numTv.setTextColor(context.getResources().getColor(numTvTextColor));
            }
            return numTv;
        }

        private TextView getUnitTv() {
            if (unitTv == null) {
                unitTv = new TextView(context);
                unitTv.setTextColor(context.getResources().getColor(unitTvTextColor));
            }
            return unitTv;
        }
    }

    public interface OnTimerListener {
        void timeOver();
    }

    public void setNumTvTextColor(int numTvTextColor) {
        this.numTvTextColor = numTvTextColor;
    }

    public void setUnitTvTextColor(int unitTvTextColor) {
        this.unitTvTextColor = unitTvTextColor;
    }

}
