package com.huayun.lib_tools.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
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
    private final static Pattern phone = Pattern
            .compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");

    /**
     * 判断当前字符串是否是数字
     *
     * @param str
     * @return
     */
    public static boolean isNumber(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();
    }


    /**
     * 获取count个随机数
     *
     * @param count 随机数个数
     * @return
     */
    public static String getRandom(int count) {
        StringBuffer sb = new StringBuffer();
        String str = "0123456789";
        Random r = new Random();
        for (int i = 0; i < count; i++) {
            int num = r.nextInt(str.length());
            sb.append(str.charAt(num));
            str = str.replace((str.charAt(num) + ""), "");
        }
        return sb.toString();
    }


    /**
     * 银行卡字符串截取，获取银行卡最后几位数
     *
     * @param content        银行卡号
     * @param substringIndex 需要获取的最后几位
     * @return
     */
    public static String bankNumberLastFour(String content, int substringIndex) {
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
     * 判断是否是钱包地址
     *
     * @param address
     * @return
     */
    public static boolean isCheckWalletAddress(String address) {
        boolean isValid = false;
        String expression = "0[xX][A-Fa-f0-9]{40}";
        CharSequence inputStr = address;
        Pattern pattern = Pattern.compile(expression);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

    /**
     * 判断是否是数字  包含小数
     *
     * @param number
     * @return
     */
    public static boolean isCheckNumber(String number) {
        boolean isValid = false;
        String expression = "^[0-9]+(.[0-9]+)?$";
        CharSequence inputStr = number;
        Pattern pattern = Pattern.compile(expression);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

    /**
     * 处理BigDecimal接收的数据展示出多余的8个0
     */
    public static String subZeroAndDot(String s) {
        if (s == null) {
            return "";
        }
        if (s.equals("0E-8")) {
            return "0";
        }
        return subZeroAndDot(new BigDecimal(s));
    }

    /**
     * 处理BigDecimal接收的数据展示出多余的8个0
     */
    public static String subZeroAndDot(double number) {
        return subZeroAndDot(new BigDecimal(String.valueOf(number)));
    }

    /**
     * 处理BigDecimal接收的数据展示出多余的8个0
     */
    public static String subZeroAndDot(BigDecimal bigDecimal) {
        if (bigDecimal == null) {
            return "";
        }
        String s = bigDecimal.toPlainString();
        if (s.indexOf(".") > 0) {
            s = s.replaceAll("0+?$", "");//去掉多余的0
            s = s.replaceAll("[.]$", "");//如最后一位是.则去掉
        }
        return s;
    }

    /**
     * 字符中间换成*
     */
    public static String replaceStar(String s, int i, int j) {
        if (s.length() < i || s.length() < j) {
            return s;
        }
        return s.substring(0, i) + " **** " + s.substring(s.length() - j, s.length());
    }

    /**
     * 显示4位尾数
     */
    public static String showMantissa(String s) {
        if (s.length() < 4) {
            return s;
        }
        return s.substring(s.length() - 4, s.length());
    }

    /**
     * 省略字符串  一般用于地址、手机号等
     *
     * @param s
     * @param i
     * @return
     */
    public static String ellipsisString(String s, int i) {
        return s.substring(0, i) + "..." + s.
                substring(s.length() - i, s.length());
    }

    /**
     * 省略字符串  一般用于地址、手机号等
     *
     * @param s
     * @param i
     * @return
     */
    public static String ellipsisString(String s, String ellipsis) {
        if (s.length() >= 7) {
            return s.substring(0, 3) + ellipsis + ellipsis + ellipsis + s.
                    substring(s.length() - 4, s.length());
        } else {
            return s;
        }
    }

    /**
     * 省略字符串  一般用于地址、手机号等
     *
     * @param s
     * @param i
     * @return
     */
    public static String ellipsisString(String s, int start, int end, String ellipsis) {
        return s.substring(0, start) + ellipsis + ellipsis + ellipsis + s.
                substring(s.length() - end, s.length());
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
     * String-->Int
     *
     * @param number
     * @return
     */
    public static int strToInt(String number) {
        int n = 0;
        try {
            n = Integer.valueOf(number);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return n;
    }

    /**
     * String-->Long
     *
     * @param number
     * @return
     */
    public static long strToLong(String number) {
        long n = 0;
        try {
            n = Long.valueOf(number);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return n;
    }

    /**
     * String-->BigDecimal
     *
     * @param number
     * @return
     */
    public static BigDecimal strToBigDecimal(String number) {
        BigDecimal n = null;
        try {
            n = new BigDecimal(number);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return n;
    }

    /**
     * String-->Double
     *
     * @param number
     * @return
     */
    public static double strToDouble(String number) {
        double n = 0l;
        try {
            n = Double.valueOf(number);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return n;
    }

    /**
     * 根据毫秒值返回字符串.e.g."0天0小时0分钟0秒"
     *
     * @param millisSecond
     * @return
     */
    public static String millisToString(long millisSecond) {
        int s = 1000;
        int m = 60 * s;
        int h = 60 * m;
        int d = 24 * h;
        StringBuffer sb = new StringBuffer();
        if (millisSecond / d > 0) {
            sb.append(millisSecond / d);
            sb.append("天");
        }
        sb.append(millisSecond % d / h);
        sb.append("小时");
        sb.append(millisSecond % d % h / m);
        sb.append("分钟");
        sb.append(millisSecond % d % h % m / s);
        sb.append("秒");
        return sb + "";
    }

    /***
     * 将输入金额num转换为汉字大写格式
     *
     * @param number 输入金额（小于10000000）
     * @return 金额的大写格式
     */
    public static String transferPriceToHanzi(String number) {
        BigDecimal num;
        if (TextUtils.isEmpty(number.trim())) {
            return "零";
        } else if (number.startsWith("-")) {
            return "输入金额不能为负数";
        } else {
            num = new BigDecimal(number.trim());
        }
        String title = "人民币:";
        String[] upChinese = {"零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌",
                "玖",};
        String[] upChinese2 = {"分", "角", "圆", "拾", "佰", "仟", "萬", "拾", "佰",
                "仟", "亿", "拾", "佰", "仟", "兆"};
        StringBuffer result = new StringBuffer();
        int count = 0;
        int zeroflag = 0;
        boolean mantissa = false;
        if (num.compareTo(BigDecimal.ZERO) < 0) {
            // 输入值小于零
            return "输入金额不能为负数";
        }
        if (num.compareTo(BigDecimal.ZERO) == 0) {
            // 输入值等于零
            return "零";
        }
        if (String.valueOf(num).length() > 12) { // 输入值过大转为科学计数法本方法无法转换
            return "您输入的金额过大";
        }
        BigDecimal temp = num.multiply(BigDecimal.TEN.pow(2));
        BigDecimal[] divideAndRemainder = temp
                .divideAndRemainder(BigDecimal.TEN.pow(2));
        if (divideAndRemainder[1].compareTo(BigDecimal.ZERO) == 0) {
            // 金额为整时
            if (temp.compareTo(BigDecimal.ZERO) == 0)
                return "您输入的金额过小";
            // 输入额为e:0.0012小于分计量单位时
            result.insert(0, "整");
            temp = temp.divide(BigDecimal.TEN.pow(2));
            count = 2;
            mantissa = true;
        }
        while (temp.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal[] divideAndRemainder2 = temp
                    .divideAndRemainder(BigDecimal.TEN);
            BigDecimal t = divideAndRemainder2[1];
            // 取得最后一位
            if (t.compareTo(BigDecimal.ZERO) != 0) {
                // 最后一位不为零时
                if (zeroflag >= 1) {
                    // 对该位前的单个或多个零位进行处理
                    if (((!mantissa) && count == 1)) {
                        // 不是整数金额且分为为零
                    } else if (count > 2 && count - zeroflag < 2) {
                        // 输入金额为400.04小数点前后都有零

                        result.insert(1, "零");
                    } else if (count > 6 && count - zeroflag < 6 && count < 10) {
                        // 万位后为零且万位为零
                        if (count - zeroflag == 2) {
                            // 输入值如400000
                            result.insert(0, "萬");
                        } else {
                            result.insert(0, "萬零");
                            // 输入值如400101
                        }
                    } else if (count > 10 && count - zeroflag < 10) {
                        if (count - zeroflag == 2) {
                            result.insert(0, "亿");
                        } else {
                            result.insert(0, "亿零");
                        }
                    } else if (((count - zeroflag) == 2)) {
                        // 个位为零
                    } else if (count > 6 && count - zeroflag == 6 && count < 10) { // 以万位开始出现零如4001000
                        result.insert(0, "萬");
                    } else if (count == 11 && zeroflag == 1) {
                        result.insert(0, "亿");
                    } else {
                        result.insert(0, "零");
                    }
                }
                result.insert(0, upChinese[t.intValue()] + upChinese2[count]);
                zeroflag = 0;
            } else {
                if (count == 2) {
                    result.insert(0, upChinese2[count]);
                    // 个位为零补上"圆"字
                }
                zeroflag++;
            }
            BigDecimal[] divideAndRemainder3 = temp
                    .divideAndRemainder(BigDecimal.TEN);
            temp = divideAndRemainder3[0];
            System.out.println("count=" + count + "---zero=" + zeroflag
                    + "----" + result + "");
            count++;
            if (count > 14)
                break;
        }
        return title + result + "";
    }

    /**
     * 判断字符串是否为手机号
     *
     * @param phoneNumber 判断的字符串
     */
    public static boolean isPhoneNumberValid(String phoneNumber) {

        boolean isValid = false;
        /*
         * 可接受的电话格式有：
         */
        String expression = "^\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{5})$";
        /*
         * 可接受的电话格式有：
         */
        String expression2 = "^\\(?(\\d{3})\\)?[- ]?(\\d{4})[- ]?(\\d{4})$";
        CharSequence inputStr = phoneNumber;
        Pattern pattern = Pattern.compile(expression);
        Matcher matcher = pattern.matcher(inputStr);

        Pattern pattern2 = Pattern.compile(expression2);
        Matcher matcher2 = pattern2.matcher(inputStr);
        if (matcher.matches() || matcher2.matches()) {
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
     * 判断当前是否有网络
     *
     * @param context
     * @return
     * @author lvliuyan
     */
    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager
                    .getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
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

    public static float sp2px(float spValue) {
        return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP,
                spValue,
                Resources.getSystem().getDisplayMetrics()
        );
    }

    public static float dp2px(float dpValue) {
        return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dpValue,
                Resources.getSystem().getDisplayMetrics()
        );
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
    public static int toInt(Object obj) {
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
     * 判断一个字符串是不是数字
     */
    public static boolean isNumber(CharSequence str) {
        try {
            Integer.parseInt(str + "");
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * byte[]数组转换为16进制的字符串。
     *
     * @param data 要转换的字节数组。
     * @return 转换后的结果。
     */
    public static final String byteArrayToHexString(byte[] data) {
        StringBuilder sb = new StringBuilder(data.length * 2);
        for (byte b : data) {
            int v = b & 0xff;
            if (v < 16) {
                sb.append('0');
            }
            sb.append(Integer.toHexString(v));
        }
        return sb + "".toUpperCase(Locale.getDefault());
    }

    /**
     * 16进制表示的字符串转换为字节数组。
     *
     * @param s 16进制表示的字符串
     * @return byte[] 字节数组
     */
    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] d = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            // 两位一组，表示一个字节,把这样表示的16进制字符串，还原成一个进制字节
            d[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character
                    .digit(s.charAt(i + 1), 16));
        }
        return d;
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
     * 把图片资源保存到本地
     *
     * @param context
     * @param filename 文件名
     * @param dra      本地图片资源id
     * @return
     */
    public static String initImagePath(Activity context, String filename,
                                       int dra) {
        String imagepath;
        try {
            if (Environment.MEDIA_MOUNTED.equals(Environment
                    .getExternalStorageState())
                    && Environment.getExternalStorageDirectory().exists()) {
                imagepath = Environment.getExternalStorageDirectory()
                        .getAbsolutePath() + filename;
            } else {
                imagepath = context.getApplication().getFilesDir()
                        .getAbsolutePath()
                        + filename;
            }
            File file = new File(imagepath);
            if (!file.exists()) {
                file.createNewFile();
                Bitmap pic = BitmapFactory.decodeResource(
                        context.getResources(), dra);
                FileOutputStream fos = new FileOutputStream(file);
                pic.compress(CompressFormat.JPEG, 100, fos);
                fos.flush();
                fos.close();
            }
        } catch (Throwable t) {
            t.printStackTrace();
            imagepath = null;
        }
        return imagepath;
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
     * sha256加密2
     *
     * @param value
     * @param key
     */
    public static String hmac_sha256(String value, String key) {
        try {
            // Get an hmac_sha256 key from the raw key bytes
            byte[] keyBytes = key.getBytes();
            SecretKeySpec signingKey = new SecretKeySpec(keyBytes, "HmacSHA256");

            // Get an hmac_sha256 Mac instance and initialize with the signing
            // key
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(signingKey);

            // Compute the hmac on input data bytes
            byte[] rawHmac = mac.doFinal(value.getBytes());

            // Convert raw bytes to Hex
            String hexBytes = byte2hex(rawHmac);
            return hexBytes;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static String byte2hex(final byte[] b) {
        String hs = "";
        String stmp = "";
        for (int n = 0; n < b.length; n++) {
            stmp = (Integer.toHexString(b[n] & 0xFF));
            if (stmp.length() == 1)
                hs = hs + "0" + stmp;
            else
                hs = hs + stmp;
        }
        return hs;
    }


    public static String getStrToStr(String[] strs) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < strs.length; i++) {
            sb.append(strs[i] + ",");
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb + "";
    }

    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    public static int dp2px(Context context, int dp) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return (int) ((dp * displayMetrics.density) + 0.5);
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


    public static SpannableStringBuilder setText(CharSequence text) {
        SpannableStringBuilder style = new SpannableStringBuilder("： " + text);
        style.setSpan(new ForegroundColorSpan(0XFF999999), 0, 1, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        return style;
    }

    /**
     * 获取屏幕的宽度
     */
    public final static int getWindowsWidth(Activity activity) {
        Resources resources = activity.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        float density = dm.density;
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        return width;
    }


}
