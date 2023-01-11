package com.huayun.lib_tools.util.textview;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.widget.TextView;

import com.huayun.lib_tools.util.UnitUtil;


/**
 * TextView处理工具
 */
public class TextViewUtil {

    /**
     * 用在显示标题的时候在标题的开头加上标签
     *
     * @param context        上下文
     * @param textView       需要显示的view
     * @param tip            提示文字 标签
     * @param result         标题
     * @param tipTextSize    标签的大小 (一般这个标签的大小要小于title的大小)
     * @param resultTextSize 标题的文字大小
     * @param radius         圆角半径
     */
    public static void tvLabelBg(Context context, TextView textView, String tip, String result, float tipTextSize, int resultTextSize, int color, int radius) {

        SpannableStringBuilder builder = new SpannableStringBuilder(tip + " " + result);
        //构造文字背景圆角
        RadiusBackgroundSpan span = new RadiusBackgroundSpan(color, color, radius, (int) UnitUtil.sp2px(context, resultTextSize));
        builder.setSpan(span, 0, tip.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        //构造标签文字大小
        AbsoluteSizeSpan spanSize = new AbsoluteSizeSpan((int) UnitUtil.sp2px(context, tipTextSize));
        builder.setSpan(spanSize, 0, tip.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        //构造title文字大小
        AbsoluteSizeSpan spanSizeLast = new AbsoluteSizeSpan((int) UnitUtil.sp2px(context, resultTextSize));
        builder.setSpan(spanSizeLast, tip.length(), builder.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);

        SpannableStringBuilder sb = new SpannableStringBuilder();
        sb.append(builder);
        sb.append(result);

        textView.setText(sb);
    }


}
