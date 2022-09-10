package com.huayun.lib_tools.util.number;

import android.text.InputFilter;
import android.text.Spanned;

/**
 * 限制输入框 输入小数位长度
 */
public class PointLengthFilter implements InputFilter {

    /**
     * 输入框小数的位数  示例保留一位小数
     */
    private static int DECIMAL_DIGITS = 2;

    public PointLengthFilter() {
    }

    public PointLengthFilter(int length) {
        DECIMAL_DIGITS = length;
    }

    public CharSequence filter(CharSequence source, int start, int end,
                               Spanned dest, int dstart, int dend) {
        // 删除等特殊字符，直接返回
        if ("".equals(source.toString())) {
            return null;
        }
        String dValue = dest.toString();
        String[] splitArray = dValue.split("\\.");
        if (splitArray.length > 1) {
            String dotValue = splitArray[1];
            int diff = dotValue.length() + 1 - DECIMAL_DIGITS;
            if (diff > 0) {
                return source.subSequence(start, end - diff);
            }
        }
        return null;
    }
}