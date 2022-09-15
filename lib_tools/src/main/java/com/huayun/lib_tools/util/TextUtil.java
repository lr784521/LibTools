package com.huayun.lib_tools.util;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.TextAppearanceSpan;
import android.widget.TextView;

public class TextUtil {

    /**
     * 给TextView设置左边的图片
     *
     * @param context
     * @param view
     * @param rsd
     */
    public static void setDrawbleLeft(Context context, TextView view, int rsd) {
        Drawable drawable = context.getResources().getDrawable(rsd);
        // / 这一步必须要做,否则不会显示.
        drawable.setBounds(0, 0, drawable.getMinimumWidth(),
                drawable.getMinimumHeight());
        view.setCompoundDrawables(drawable, null, null, null);
    }

    /**
     * 给TextView设置右边的图片
     *
     * @param context
     * @param view
     * @param rsd
     */
    public static void setDrawbleRight(Context context, TextView view, int rsd) {
        Drawable drawable = context.getResources().getDrawable(rsd);
        // / 这一步必须要做,否则不会显示.
        drawable.setBounds(0, 0, drawable.getMinimumWidth(),
                drawable.getMinimumHeight());
        view.setCompoundDrawables(null, null, drawable, null);
    }

    /**
     * 给TextView设置顶部的图片
     *
     * @param context
     * @param view
     * @param rsd
     */
    public static void setDrawbleTop(Context context, TextView view, int rsd) {
        Drawable drawable = context.getResources().getDrawable(rsd);
        // / 这一步必须要做,否则不会显示.
        drawable.setBounds(0, 0, drawable.getMinimumWidth(),
                drawable.getMinimumHeight());
        view.setCompoundDrawables(null, drawable, null, null);
    }

    /**
     * 给TextView设置底部的图片
     *
     * @param context
     * @param view
     * @param rsd
     */
    public static void setDrawbleBottom(Context context, TextView view, int rsd) {
        Drawable drawable = context.getResources().getDrawable(rsd);
        // / 这一步必须要做,否则不会显示.
        drawable.setBounds(0, 0, drawable.getMinimumWidth(),
                drawable.getMinimumHeight());
        view.setCompoundDrawables(null, null, null, drawable);
    }

    /**
     * 修改金额文字部分样式
     *
     * @param textView
     * @param oneSize
     * @param twoSize
     * @param oneStyle
     * @param twoStyle
     * @param oneColorStateList
     * @param twoColorStateList 示例： TextUtil.updateMoneyTextStyle(tvLockedAssets, 32, 24,
     *                          Typeface.BOLD, Typeface.BOLD,
     *                          ColorStateList.valueOf(0XFF333333),
     *                          ColorStateList.valueOf(0XFF333333));
     */
    public static void updateMoneyTextStyle(TextView textView, int oneSize, int twoSize,
                                            int oneStyle, int twoStyle, ColorStateList oneColorStateList, ColorStateList twoColorStateList) {
        String str = textView.getText().toString();
        if (str.contains(".")) {
            SpannableStringBuilder builder = new SpannableStringBuilder(str);
            TextAppearanceSpan mainSpan = new TextAppearanceSpan(null, oneStyle, oneSize, oneColorStateList, null);
            TextAppearanceSpan graySpan = new TextAppearanceSpan(null, twoStyle, twoSize, twoColorStateList, null);

            builder.setSpan(mainSpan, 0, str.indexOf("."), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            builder.setSpan(graySpan, str.indexOf("."), str.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            textView.setText(builder);
        }
    }

    /**
     * 设置删除样式
     *
     * @param textView
     */
    public static void setDeleteLineStyle(TextView textView) {
        textView.setPaintFlags(textView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
    }

}
