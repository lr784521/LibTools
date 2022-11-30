package com.huayun.lib_tools.util.number;

import com.huayun.lib_tools.util.StringUtil;

import java.math.BigDecimal;
import java.text.NumberFormat;

/**
 * 进行BigDecimal对象的加减乘除，四舍五入等运算的工具类<br/>
 * 由于Java的简单类型不能够精确的对浮点数进行运算，这个工具类提供精 确的浮点数运算，包括加减乘除和四舍五入。
 */
public class BigDecimalUtil {

    /**
     * 提供NumberFormat对象
     *
     * @param retainNum
     * @return
     */
    public static NumberFormat getNumberFormat(int retainNum) {
        NumberFormat format = NumberFormat.getInstance();
        format.setMaximumFractionDigits(retainNum);
        format.setGroupingUsed(false);
        return format;
    }

    /**
     * 提供精确的加法运算。
     *
     * @param b1 被加数
     * @param b2 加数
     * @return 两个参数的和
     */
    public static String bigdecimalAdd(BigDecimal b1, BigDecimal b2, int retainNum) {
        NumberFormat format = getNumberFormat(retainNum);
        return format.format(b1.add(b2).doubleValue());
    }

    /**
     * 提供精确的加法运算。
     *
     * @param b1 被加数
     * @param b2 加数
     * @return 两个参数的和
     */
    public static double bigdecimalAdd(BigDecimal b1, BigDecimal b2) {
        BigDecimal one = new BigDecimal("1");
        return b1.add(b2).doubleValue();
    }

    /**
     * 提供精确的减法运算。
     */
    public static String bigdecimalSubtract(BigDecimal b1, BigDecimal b2, int retainNum) {
        NumberFormat format = getNumberFormat(retainNum);
        return format.format(b1.subtract(b2).doubleValue());
    }

    /**
     * 提供精确的减法运算。
     */
    public static double bigdecimalSubtract(BigDecimal b1, BigDecimal b2) {
        BigDecimal one = new BigDecimal("1");
        return b1.subtract(b2).doubleValue();
    }


    /**
     * 比较两个数字大小    大：true  小false
     *
     * @param one 比较数
     * @param two 被比较数
     * @return 大于 true
     */
    public static boolean comparisonBooleanNumberSize(BigDecimal one, BigDecimal two) {
        int i = one.compareTo(two);
        if (i > 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 比较两个数字大小    大：true  小false  大于等于
     *
     * @param one 比较数
     * @param two 被比较数
     * @return 小于 true
     */
    public static boolean comparisonGreaterOrQqualTo(BigDecimal one, BigDecimal two) {
        int i = one.compareTo(two);
        if (i < 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 比较两个数字大小
     *
     * @param one 比较数
     * @param two 被比较数
     * @return -1 小于  0等于  1大于
     */
    public static int comparisonIntNumberSize(BigDecimal one, BigDecimal two) {
        int i = one.compareTo(two);
        return i;
    }


    /**
     * 判断当前数字是否大于0
     *
     * @return true 大于  false 小于
     */
    public static boolean numberIsBigZero(BigDecimal number) {
        boolean isCheck = false;
        BigDecimal zero = new BigDecimal("0");
        int i = number.compareTo(zero);
        if (i == 1) {
            isCheck = true;
        }
        return isCheck;
    }

    /**
     * 判断当前数字是否大于第二个数字
     *
     * @param number 比较数
     * @param num    被比较数
     * @return true 大于  false 小于
     */
    public static boolean numberIsBigNum(BigDecimal number, BigDecimal num) {
        boolean isCheck = false;
        int i = number.compareTo(num);
        if (i == 1) {
            isCheck = true;
        }
        return isCheck;
    }

    /**
     * 判断当前数字是否小于0
     *
     * @return true 小于  false 大于
     */
    public static boolean numberIsSmallZero(BigDecimal number) {
        boolean isCheck = false;
        BigDecimal zero = new BigDecimal("0");
        int i = number.compareTo(zero);
        if (i == -1) {
            isCheck = true;
        }
        return isCheck;
    }

    /**
     * 判断当前数字是否小于0
     *
     * @param number 比较数
     * @param num    被比较数
     * @return true 小于  false 大于
     */
    public static boolean numberIsSmallNum(BigDecimal number, BigDecimal num) {
        boolean isCheck = false;
        int i = number.compareTo(num);
        if (i == -1) {
            isCheck = true;
        }
        return isCheck;
    }

    /**
     * 判断当前数字是否等于0
     *
     * @return true 等于  false 不等于
     */
    public static boolean numberIsEqualZero(BigDecimal number) {
        boolean isCheck = false;
        BigDecimal zero = new BigDecimal("0");
        int i = number.compareTo(zero);
        if (i == 0) {
            isCheck = true;
        }
        return isCheck;
    }


    /**
     * BigDecimal   精确除法计算
     *
     * @param num1      被除数
     * @param num2      除数  (不能为0)
     * @param retainNum 保留小数位
     * @param upOrDown  true向上取整 false向下取整
     * @return
     */
    public static String bigDecimalDivide(BigDecimal num1, BigDecimal num2, int retainNum, boolean upOrDown) {
        NumberFormat format = getNumberFormat(retainNum);
        if (!checkNotZero(num1)) {
            if (!checkNotZero(num2)) {
                double v = 0.0;
                if (upOrDown) {
                    v = num1.divide(num2, retainNum, BigDecimal.ROUND_HALF_UP).doubleValue();
                } else {
                    v = num1.divide(num2, retainNum, BigDecimal.ROUND_DOWN).doubleValue();
                }
                return format.format(v);
            } else {
                return "0.0";
            }
        } else {
            return "0.0";
        }
    }

    /**
     * BigDecimal   精确乘法计算
     *
     * @param num1      被乘数
     * @param num2      乘数
     * @param retainNum 保留位数
     * @param upOrDown  true向上取整 false向下取整
     * @return
     */
    public static String bigDecimalMultiply(BigDecimal num1, BigDecimal num2, int retainNum, boolean upOrDown) {
        BigDecimal zero = new BigDecimal("0");
        BigDecimal one = new BigDecimal("1");
        NumberFormat format = getNumberFormat(retainNum);

        BigDecimal multiply = num1.multiply(num2);

        int i = multiply.compareTo(zero);
        if (i == 0) {
            return "0.0";
        } else {
            double v = 0.0;
            if (upOrDown) {
                v = multiply.divide(one, retainNum, BigDecimal.ROUND_HALF_UP).doubleValue();
            } else {
                v = multiply.divide(one, retainNum, BigDecimal.ROUND_DOWN).doubleValue();
            }
            return format.format(v);
        }
    }

    /**
     * BigDecimal   保留小数位
     *
     * @param num1      需要处理的数据
     * @param retainNum 小数位
     * @return
     */
    public static String bigDecimalReservedDecimalPlaces(BigDecimal num1, int retainNum) {
        BigDecimal one = new BigDecimal("1");
        NumberFormat format = getNumberFormat(retainNum);
        String s = NumberUtil.subZeroAndDot(num1.toPlainString());
        double v = new BigDecimal(s).divide(one, retainNum, BigDecimal.ROUND_HALF_UP).doubleValue();
        return format.format(v);
    }


    /**
     * 判断当前数字是否等于0
     *
     * @param number
     * @return
     */
    public static boolean checkNotZero(BigDecimal number) {
        BigDecimal zero = new BigDecimal("0");
        int i = number.compareTo(zero);
        if (i == 0) {
            return true;
        } else {
            return false;
        }
    }

}