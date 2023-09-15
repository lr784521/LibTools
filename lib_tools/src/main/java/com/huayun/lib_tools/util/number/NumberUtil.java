package com.huayun.lib_tools.util.number;

import android.text.TextUtils;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 数据处理
 */
public class NumberUtil {

    /**
     * 数组合并
     * @param first
     * @param rest
     * @param <T>
     * @return
     */
    public static <T> T[] concatAll(T[] first, T[]... rest) {
        int totalLength = first.length;
        for (T[] array : rest) {
            totalLength += array.length;
        }
        T[] result = Arrays.copyOf(first, totalLength);
        int offset = first.length;
        for (T[] array : rest) {
            System.arraycopy(array, 0, result, offset, array.length);
            offset += array.length;
        }
        return result;
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
     *  处理BigDecimal接收的数据展示出多余的8个0
     * @param bigDecimal
     * @param defVal 默认值
     * @return
     */
    public static String subZeroAndDot(BigDecimal bigDecimal,String defVal) {
        if (bigDecimal == null) {
            return defVal;
        }
        String s = bigDecimal.toPlainString();
        if (s.indexOf(".") > 0) {
            s = s.replaceAll("0+?$", "");//去掉多余的0
            s = s.replaceAll("[.]$", "");//如最后一位是.则去掉
        }
        return s;
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



}
