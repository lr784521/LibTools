package com.huayun.lib_tools.util.number;

import android.text.InputFilter;
import android.text.Spanned;

/**
 * 限制输入框 输入小数位长度
 */
public class PointLengthFilter implements InputFilter {

    //输入框小数的位数 示例保留几位小数
    private int decimalDigits = 2;

    public PointLengthFilter() {
    }

    public PointLengthFilter(int decimalDigits) {
        this.decimalDigits = decimalDigits;
    }

    @Override
    public CharSequence filter(CharSequence source,
                               int start,
                               int end,
                               Spanned dest,
                               int dstart,
                               int dend) {

        int dotPos = -1;
        int len = dest.length();
        for (int i = 0; i < len; i++) {
            char c = dest.charAt(i);
            if (c == '.' || c == ',') {
                dotPos = i;
                break;
            }
        }
        if (dotPos >= 0) {
            if (source.equals(".") || source.equals(",")) {
                return "";
            }
            if (dend <= dotPos) {
                return null;
            }
            if (len - dotPos > decimalDigits) {
                return "";
            }
        }

        return null;
    }
}