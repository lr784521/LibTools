package com.huayun.lib_tools.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.telephony.SmsManager;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import androidx.annotation.RequiresApi;

import com.huayun.lib_tools.util.toast.ToastEnum;
import com.huayun.lib_tools.util.toast.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 发送短信
 * boolean hasSimCard = SendMessage.isHasSimCard(context);
 * LogUtils.e("hasSimCard==>"+hasSimCard);
 * if (hasSimCard) {
 * SendMessage.sendSms(context, 0, "手机号", "内容");
 * }
 */
public class SendMessage {

    /**
     * 编辑内容，发短息
     *
     * @param phoneNumber
     * @param message
     */
    public static void sendSMS2(String phoneNumber, String message,
                                Context context) {
        // 获取短信管理器
        Uri uri = Uri.parse("smsto:" + phoneNumber);
        Intent it = new Intent(Intent.ACTION_SENDTO, uri);
        it.putExtra("sms_body", message);
        context.startActivity(it);
    }

    /**
     * 发送短信
     *
     * @param mContext
     * @param which    卡1：0   卡2：1
     * @param phone    手机号
     * @param context  内容
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    public static void sendSms(Context mContext, final int which, String phone, String context) {
        SubscriptionInfo sInfo = null;
        final SubscriptionManager sManager = (SubscriptionManager) mContext.getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE);
        @SuppressLint("MissingPermission") List<SubscriptionInfo> list = sManager.getActiveSubscriptionInfoList();
        if (list.size() == 2) {
            // 双卡
            sInfo = list.get(which);
        } else {
            // 单卡
            sInfo = list.get(0);
        }
        if (sInfo != null) {
            int subId = sInfo.getSubscriptionId();
            SmsManager manager = SmsManager.getSmsManagerForSubscriptionId(subId);
            if (!TextUtils.isEmpty(phone)) {
                ArrayList<String> messageList = manager.divideMessage(context);
                for (String text : messageList) {
                    manager.sendTextMessage(phone, null, text, null, null);
                }
                ToastUtil.getInstance().showToast(ToastEnum.SUCCESS,"信息正在发送，请稍候");
            } else {
                ToastUtil.getInstance().showToast(ToastEnum.ERROR,"无法正确的获取SIM卡信息，请稍候重试");
            }
        }
    }

    /**
     * 判断是否包含SIM卡
     *
     * @return 状态
     */
    public static boolean isHasSimCard(Context context) {
        TelephonyManager telMgr = (TelephonyManager)
                context.getSystemService(Context.TELEPHONY_SERVICE);
        int simState = telMgr.getSimState();
        boolean result = true;
        switch (simState) {
            case TelephonyManager.SIM_STATE_ABSENT:
                result = false; // 没有SIM卡
                break;
            case TelephonyManager.SIM_STATE_UNKNOWN:
                result = false;
                break;
        }
        return result;
    }

}
