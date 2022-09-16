package com.huayun.lib_tools.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;

import com.huayun.lib_tools.R;

import java.util.HashMap;
import java.util.Map;

/**
 * 通知栏工具
 */
public class NoticeMessageUtils {

    public static Context context;
    private static Map<Integer, NotificationCompat.Builder> mMap; //保存所有通知实体
    private static NotificationManager mManager;

    public static void init(Context context, NotificationManager mManager) {
        NoticeMessageUtils.context = context;
        NoticeMessageUtils.mManager = mManager;
        mMap = new HashMap<>();
    }

    /**
     * 经典通知栏
     *
     * @param tag         标记
     * @param title       标题
     * @param content     内容
     * @param aClass      跳转界面
     * @param autoCancel  点击通知后自动消失
     * @param isDisappear 点击是否不消失
     */
    public static void showNotification(int tag, String title, String content, Class aClass,
                                        boolean autoCancel, boolean isDisappear,int logo) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "channel_id_1");
        builder
                .setContentTitle(title)
                .setContentText(content)
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(autoCancel)// 点击通知后自动消失
                .setSmallIcon(logo)// 通知图标
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), logo))// 小图标只能用alpha图层进行绘制，不能用rgb图层
                .setOngoing(false);  //是否是正在进行的通知   正在进行的通知在“常规”通知的上方排序通知面板  并且不受全部清理按钮影响
//                .setSound(null); //默认系统提示音

//         playSound();  //自定义提示音 (与系统提示音2选1)

        // ======================  点击通知后进入的活动  ======================
        Intent resultIntent = new Intent(context, aClass);
        // 这两句非常重要，使之前的活动不出栈
        resultIntent.setAction(Intent.ACTION_MAIN);
        resultIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        //允许更新
        PendingIntent resultPendingIntent = PendingIntent.getActivity(context, 0, resultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(resultPendingIntent)
                // 浮动通知
                .setFullScreenIntent(resultPendingIntent, true);

        //通知点击是否消失
        if (isDisappear) {
            // 通知点击不消失
            Notification notification = builder.build();
            notification.flags = Notification.FLAG_ONGOING_EVENT;
            //保存当前通知 (不消失时保存)
            mMap.put(tag, builder);
        }

        //发出通知
        mManager.notify(tag, builder.build());
    }

    //播放自定义的声音
    public static void playSound(String uri) {
//        String uri = "android.resource://" + context.getPackageName() + "/" + R.raw.tips;
        Uri no = Uri.parse(uri);
        Ringtone r = RingtoneManager.getRingtone(context, no);
        r.play();
    }

    /**
     * 设置对应通知是否 点击是否自动清除
     *
     * @param tag
     * @param autoCancel
     */
    public static void setNoticeAutoCancel(int tag, boolean autoCancel) {
        NotificationCompat.Builder builder = mMap.get(tag);
        if (builder != null) {
            builder.setAutoCancel(autoCancel);
        }
    }


    /**
     * 自定义通知栏
     *
     * @param tag         标记
     * @param aClass      跳转界面
     * @param isDisappear 点击是否不消失
     */
    public static void customNotification(int tag, Class aClass, boolean isDisappear,int logo) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "channel_id_1");
        builder.setSmallIcon(logo)// 通知图标
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), logo)); // 小图标只能用alpha图层进行绘制，不能用rgb图层

        // ======================  点击通知后进入的活动  ======================
        Intent resultIntent = new Intent(context, aClass);
        // 这两句非常重要，使之前的活动不出栈
        resultIntent.setAction(Intent.ACTION_MAIN);
        resultIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        //允许更新
        PendingIntent resultPendingIntent = PendingIntent.getActivity(context, 0, resultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(resultPendingIntent);

        // 设置通知的显示视图
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.notifit_bar);
        builder.setCustomContentView(remoteViews);
        Notification notification = builder.build();

        //通知点击是否消失
        if (isDisappear) {
            // 通知点击不消失
            notification.flags = Notification.FLAG_ONGOING_EVENT;
            //保存当前通知 (不消失时保存)
            mMap.put(tag, builder);
        }
        mManager.notify(tag, builder.build());
    }

    /**
     * 更新自定义布局
     *
     * @param tag
     */
    public static void updateCustom(int tag) {
        NotificationCompat.Builder builder = mMap.get(tag);
        if (null != builder) {
            // 修改进度条
//            @SuppressLint("RestrictedApi")
//            RemoteViews contentView = builder.getContentView();
//            contentView.setProgressBar(R.id.pBar, 100, progress, false);
//            builder.setCustomContentView(contentView);
//            mManager.notify(tag, builder.build());
        }
    }

}
