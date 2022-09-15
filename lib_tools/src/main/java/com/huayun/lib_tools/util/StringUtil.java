package com.huayun.lib_tools.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.res.Resources;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

@SuppressWarnings("all")
public class StringUtil {

    /**
     * 判断字符串是否是數字
     *
     * @author lvliuyan
     */

    private final static Pattern emailer = Pattern
            .compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");


    /**
     * 字符串截取后几位
     *
     * @param content
     * @param substringIndex
     * @return
     */
    public static String strNumberLastFour(String content, int substringIndex) {
        String substring = content.substring(content.length() - substringIndex);
        return substring;
    }

    /**
     * 银行卡字符串截取，只展示最后几位
     *
     * @param content        银行卡号
     * @param substringIndex 需要展示的最后几位
     * @return
     */
    public static String bankNumberSubstring(String content, int substringIndex) {
        String substring = content.substring(content.length() - substringIndex);
        return "**** **** **** " + substring;
    }

    /**
     * 解析Url
     *
     * @return
     */
    public static Map<String, String> analysisUrl(String url) {
        Map<String, String> map = new HashMap<>();
        //判断链接中是否包含？
        if (url.contains("?")) {
            String[] split = url.split("[?]");
            if (split.length == 2) {
                String[] keyValue = split[1].split("[&]");
                for (String s : keyValue) {
                    if (s.contains("=")) {
                        String[] segmentation = s.split("[=]");
                        if (segmentation.length == 2) {
                            map.put(segmentation[0], segmentation[1]);
                        }
                    }
                }
            }
        }
        return map;
    }

    /**
     * 更改TextView部分字体颜色
     *
     * @param textView 需要更改部分字体颜色的控件
     * @param color    需要更改的颜色
     * @param stat     开始下标
     * @param end      结束下标
     */
    public static void udpTextPartTypeface(TextView textView, int color, int stat, int end) {
        //更改TextView部分字体大小
        Spannable WordtoSpan = new SpannableString(textView.getText().toString());
        WordtoSpan.setSpan(new ForegroundColorSpan(color), stat, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(WordtoSpan);
    }

    /**
     * 隐私信息脱敏 数据截取显示
     * @param s 内容
     * @param start 开始位置
     * @param end 结束位置
     * @param type 脱敏符号
     * @param typeNum 符号长度
     * @return
     */
    public static String replaceStar(String s, int start, int end, String type, int typeNum) {
        if (s.length() < start || s.length() < end) {
            return s;
        }
        String rep = "";
        for (int i = 0; i < typeNum; i++) {
            rep += type;
        }
        return s.substring(0, start) + rep + s.substring(s.length() - end, s.length());
    }

    /**
     * 判断字符串是否包含中文
     *
     * @author lvliuyan
     */
    public static final boolean isChinese(String strName) {
        char[] ch = strName.toCharArray();
        for (int i = 0; i < ch.length; i++) {
            char c = ch[i];
            if (isChinese(c)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断字符是否是中文
     * @param c
     * @return
     */
    private static final boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
            return true;
        }
        return false;
    }

    /**
     * 判断是否是手机号
     *
     * @param phoneNumber
     * @return
     */
    public static boolean isPhoneNumber(String phoneNumber) {
        boolean isValid = false;
        String expression = "^1[3|5|8|4|7][0-9]{9}$";
        CharSequence inputStr = phoneNumber;
        Pattern pattern = Pattern.compile(expression);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

    /**
     * 正则表达式校验邮箱
     *
     * @param email 待匹配的邮箱
     * @return 匹配成功返回true 否则返回false;
     */
    public static boolean isEmail(String email) {
        String RULE_EMAIL = "^\\w+((-\\w+)|(\\.\\w+))*\\@[A-Za-z0-9]+((\\.|-)[A-Za-z0-9]+)*\\.[A-Za-z0-9]+$";
        //正则表达式的模式
        Pattern p = Pattern.compile(RULE_EMAIL);
        //正则表达式的匹配器
        Matcher m = p.matcher(email);
        //进行正则匹配
        return m.matches();
    }

    /**
     * 根据手机分辨率从 px(像素) 单位 转成 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 根据手机分辨率从 dp 单位 转成 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 是否是身份证号
     */
    public static boolean isCard(String s_aStr) {
        String str = "\\d{17}[0-9a-zA-Z]|\\d{14}[0-9a-zA-Z]";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(s_aStr);
        return m.matches();
    }

    /**
     * 判断给定字符串是否空白串 空白串是指由空格、制表符、回车符、换行符组成的字符串 若输入字符串为null或空字符串，返回true
     */
    public static boolean isEmpty(CharSequence input) {
        if (input == null || "".equals(input) || "null".equals(input))
            return true;
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
                return false;
            }
        }
        return true;
    }

    /**
     * 专业--判断字符串是否为空
     *
     * @param str
     * @return
     */
    public static boolean isBlank(String str) {
        int strLen;
        if (str != null && (strLen = str.length()) != 0) {
            for (int i = 0; i < strLen; ++i) {
                // 判断字符是否为空格、制表符、tab
                if (!Character.isWhitespace(str.charAt(i))) {
                    return false;
                }
            }
            return true;
        } else {
            return true;
        }
    }

    /**
     * 判断是不是一个合法的手机号码
     */
    public static boolean isPhone(CharSequence phoneNum) {
        if (isEmpty(phoneNum))
            return false;
        Pattern phone = Pattern
                .compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
        return phone.matcher(phoneNum).matches();
    }

    /**
     * 字符串转整数
     *
     * @param str
     * @param defValue
     * @return
     */
    public static int toInt(String str, int defValue) {
        try {
            return Integer.parseInt(str);
        } catch (Exception e) {
        }
        return defValue;
    }

    /**
     * 对象转整
     *
     * @param obj
     * @return 转换异常返回 0
     */
    public static int objToInt(Object obj) {
        if (obj == null)
            return 0;
        return toInt(obj + "", 0);
    }

    /**
     * String转long
     *
     * @param obj
     * @return 转换异常返回 0
     */
    public static long toLong(String obj) {
        try {
            return Long.parseLong(obj);
        } catch (Exception e) {
        }
        return 0;
    }

    /**
     * String转double
     *
     * @param obj
     * @return 转换异常返回 0
     */
    public static double toDouble(String obj) {
        try {
            return Double.parseDouble(obj);
        } catch (Exception e) {
        }
        return 0D;
    }

    /**
     * 字符串转布尔
     *
     * @param b
     * @return 转换异常返回 false
     */
    public static boolean toBool(String b) {
        try {
            return Boolean.parseBoolean(b);
        } catch (Exception e) {
        }
        return false;
    }

    /**
     * 删除金额字符串的金额单位
     *
     * @param str
     * @return
     */
    public static String removeBiFuhao(String str) {
        String tmpstr = str.replace(" ", "");
        tmpstr = tmpstr.replace("￥", "");
        tmpstr = tmpstr.replace("$", "");
        tmpstr = tmpstr.replace("元", "");
        return tmpstr;
    }

    /**
     * 实现文本复制功能
     *
     * @param content 要复制的内容
     */
    @SuppressLint("NewApi")
    public static void copy(String content, Context context) {
        // 得到剪贴板管理器
        ClipboardManager cmb = (ClipboardManager) context
                .getSystemService(Context.CLIPBOARD_SERVICE);
        cmb.setText(content.trim());
    }

    /**
     * string数组转字符串
     * @param strs 内容
     * @param type 分割类型(不需要时填空字符串)
     * @return
     */
    public static String getStrToStr(String[] strs,String type) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < strs.length; i++) {
            sb.append(strs[i] + type);
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb + "";
    }

    /**
     * 检测是否有emoji表情
     *
     * @param source
     * @return
     */
    public static boolean containsEmoji(String source) {
        int len = source.length();
        for (int i = 0; i < len; i++) {
            char codePoint = source.charAt(i);
            if (!isEmojiCharacter(codePoint)) { //如果不能匹配,则该字符是Emoji表情
                return true;
            }
        }
        return false;
    }

    /**
     * 搜索字符串是否包含某内容
     * @param string
     * @return
     */
    public static boolean containsSearch(String string) {
        Pattern pattern = Pattern.compile("^[A-Za-z0-9\\s \\u4e00-\\u9fa5]+$");
        Matcher matcher = pattern.matcher(string);
        return matcher.matches();
    }

    /**
     * 判断是否是Emoji
     *
     * @param codePoint 比较的单个字符
     * @return
     */
    private static boolean isEmojiCharacter(char codePoint) {
        return (codePoint == 0x0) || (codePoint == 0x9) || (codePoint == 0xA) ||
                (codePoint == 0xD) || ((codePoint >= 0x20) && (codePoint <= 0xD7FF)) ||
                ((codePoint >= 0xE000) && (codePoint <= 0xFFFD)) || ((codePoint >= 0x10000)
                && (codePoint <= 0x10FFFF));
    }



}
