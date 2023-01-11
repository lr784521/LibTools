package com.huayun.lib_tools.util.time;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * 时间转换工具
 */
public class TimeUtil {
    public static final long DAY_MILLSECONDS = 24 * 3600 * 1000;

    private TimeUtil() {
    }


    /**
     * 根据毫秒值返回字符串.e.g."0天0小时0分钟0秒"
     *
     * @param millisSecond
     * @return
     */
    public static String millisToString(long millisSecond) {
       return millisToString(millisSecond,"天","时","分","秒");
    }

    public static String millisToString(long millisSecond,String DUnit,String HUnit,String MUnit,String SUnit) {
        int s = 1000;
        int m = 60 * s;
        int h = 60 * m;
        int d = 24 * h;
        StringBuffer sb = new StringBuffer();
        if (millisSecond / d > 0) {
            sb.append(millisSecond / d);
            sb.append(DUnit);
        }
        sb.append(millisSecond % d / h);
        sb.append(HUnit);
        sb.append(millisSecond % d % h / m);
        sb.append(MUnit);
        sb.append(millisSecond % d % h % m / s);
        sb.append(SUnit);
        return sb + "";
    }

    public static String showMessageTime(long MessageTime) {
        SimpleDateFormat format;
        String showtimeString;
        long startTime = MessageTime * 1000;
        //获取当前年份
        int current_year = Integer.parseInt(getYear(System.currentTimeMillis()));
        //获取发消息的年份
        int year = Integer.parseInt(getYear(startTime));
        if (current_year > year) {
            //不在同一年 显示完整的年月日
            showtimeString = getDateEN(startTime);
        } else {
            if (newchajitian(startTime) == 0) {
                //同一天 显示时分
                format = new SimpleDateFormat("HH:mm");
                showtimeString = format.format(new Date(startTime));
            } else if (newchajitian(startTime) == 1) {
                //相差一天 显示昨天
                showtimeString = "昨天";
            } else if (newchajitian(startTime) > 1 && newchajitian(startTime) < 7) {
                //相差一天以上 七天之内 显示星期几
                showtimeString = getWeek(startTime);
            } else {
                format = new SimpleDateFormat("MM-dd");
                showtimeString = format.format(new Date(startTime));
            }

        }
        return showtimeString;
    }

    public static String getLastM(int total, int value) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM");
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date); // 设置为当前时间
        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - (total - value));
        date = calendar.getTime();
        return dateFormat.format(date);
    }

    /**
     * 根据用户生日计算年龄
     */
    public static String getAgeByBirthday(Date birthday) {
        Calendar cal = Calendar.getInstance();

        if (cal.before(birthday)) {
            throw new IllegalArgumentException(
                    "The birthDay is before Now.It's unbelievable!");
        }

        int yearNow = cal.get(Calendar.YEAR);
        int monthNow = cal.get(Calendar.MONTH) + 1;
        int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);

        cal.setTime(birthday);
        int yearBirth = cal.get(Calendar.YEAR);
        int monthBirth = cal.get(Calendar.MONTH) + 1;
        int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);

        int age = yearNow - yearBirth;

        if (monthNow <= monthBirth) {
            if (monthNow == monthBirth) {
                // monthNow==monthBirth
                if (dayOfMonthNow < dayOfMonthBirth) {
                    age--;
                }
            } else {
                // monthNow>monthBirth
                age--;
            }
        }
        return age + "岁";
    }

    //显示完整的年月日 时分秒
    public static String getTime(long time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(new Date(time));// 2012-10-03 23:41:31
    }

    //显示完整的年月日 时分秒
    public static String getTimeDes(long time) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        return format.format(new Date(time));// 2012-10-03 23:41:31
    }

    //显示完整的年月日 时分秒
    public static String getMinute(long time) {
        SimpleDateFormat format = new SimpleDateFormat("mm:ss");
        return format.format(new Date(time));// 2012-10-03 23:41:31
    }

    //显示完整的月日 时分
    public static String getComTime(long time) {
        SimpleDateFormat format = new SimpleDateFormat("MM-dd HH:mm");
        return format.format(new Date(time));// 2012-10-03 23:41:31
    }

    //显示完整的年月日
    public static String getDateEN(long time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(new Date(time));// 2012-10-03 23:41:31
    }

    //显示完整的年月日
    public static String getDateMon(long time) {
        SimpleDateFormat format = new SimpleDateFormat("MM月dd日");
        return format.format(new Date(time));// 2012-10-03 23:41:31
    }

    // 获取当前年份
    public static String getYear(Long time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy");
        return format.format(new Date(time));
    }

    public static long getDate(String timeStr) {
        long time = 0;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = format.parse(timeStr);
            time = date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return time / 1000;
    }

    public static long getDateByString(String timeStr) {
        long time = 0;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = format.parse(timeStr);
            time = date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return time;
    }

    public static String getTimeByString(String timeStr) {

        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMddhhmmss");
        Date date = null;
        try {
            date = sdf1.parse(timeStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return sdf2.format(date);
    }

    public static String getTimeByStr(String timeStr) {

        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMddhhmmss");
        Date date = null;
        try {
            date = sdf1.parse(timeStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
        return sdf2.format(date);
    }


    public static String setTimeByString(String timeStr) {

        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date = null;
        try {
            date = sdf1.parse(timeStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMddhhmmss");
        return sdf2.format(date);
    }

    // 获取上午、下午时间
    public static String getHour(Long time) {
        String str;
        final Calendar mCalendar = Calendar.getInstance();
        mCalendar.setTimeInMillis(time);
        int apm = mCalendar.get(Calendar.AM_PM);
        SimpleDateFormat format = new SimpleDateFormat("hh:mm");
        String hour = format.format(time);

        if (apm == 0) {
            str = "上午 " + hour;
        } else {
            str = "下午 " + hour;
        }
        return str;
    }

    public static long newchajitian(Long lstartTime) {
        Calendar c1 = Calendar.getInstance();
        int year = c1.get(Calendar.YEAR);
        int yue = c1.get(Calendar.MONTH);
        int ri = c1.get(Calendar.DATE);
        c1.set(year, yue + 1, ri, 0, 0);
        Calendar c2 = Calendar.getInstance();
        int year_ = Integer.parseInt((getYear(lstartTime)));
        int yue_ = getYue(lstartTime);
        int ri_ = getDay(lstartTime);
        c2.set(year_, yue_, ri_, 0, 0);
        return getAbsDayDiff(c2, c1);
    }

    public static int getDay(long time) {
        SimpleDateFormat format1 = new SimpleDateFormat("dd");
        String date1 = format1.format(new Date(time));
        return Integer.parseInt(date1);// 2012-10-03 23:41:31
    }

    public static int getYue(long time) {
        SimpleDateFormat format1 = new SimpleDateFormat("MM");
        String date1 = format1.format(new Date(time));
        return Integer.parseInt(date1);// 2012-10-03 23:41:31
    }

    public static int getAbsDayDiff(Calendar calStart, Calendar calEnd) {
        Calendar start = (Calendar) calStart.clone();
        Calendar end = (Calendar) calEnd.clone();

        start.set(start.get(Calendar.YEAR), start.get(Calendar.MONTH),
                start.get(Calendar.DATE), 0, 0, 0);
        start.set(Calendar.MILLISECOND, 0);

        end.set(end.get(Calendar.YEAR), end.get(Calendar.MONTH),
                end.get(Calendar.DATE), 0, 0, 0);
        end.set(Calendar.MILLISECOND, 0);

        return (int) ((end.getTimeInMillis() - start.getTimeInMillis()) / DAY_MILLSECONDS);
    }

    //获取星期几
    public static String getWeek(long timeStamp) {
        int myDate;
        String week = null;
        Calendar cd = Calendar.getInstance();
        cd.setTime(new Date(timeStamp));
        myDate = cd.get(Calendar.DAY_OF_WEEK);
        // 获取指定日期转换成星期几
        if (myDate == 1) {
            week = "星期日";
        } else if (myDate == 2) {
            week = "星期一";
        } else if (myDate == 3) {
            week = "星期二";
        } else if (myDate == 4) {
            week = "星期三";
        } else if (myDate == 5) {
            week = "星期四";
        } else if (myDate == 6) {
            week = "星期五";
        } else if (myDate == 7) {
            week = "星期六";
        }
        return week;
    }

    /**
     * 时间转化为显示字符串
     *
     * @param timeStamp 单位为秒
     */
    public static String getTimeStr(long timeStamp) {
        if (timeStamp == 0) return "";
        Calendar inputTime = Calendar.getInstance();
        inputTime.setTimeInMillis(timeStamp * 1000);
        Date currenTimeZone = inputTime.getTime();
        Calendar calendar = Calendar.getInstance();
        if (calendar.before(inputTime)) {
            //当前时间在输入时间之前
            SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");
            return sdf.format(currenTimeZone);
        }
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        if (calendar.before(inputTime)) {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            return sdf.format(currenTimeZone);
        }
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        if (calendar.before(inputTime)) {
            return "昨天";
        } else {
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            calendar.set(Calendar.MONTH, Calendar.JANUARY);
            if (calendar.before(inputTime)) {
                SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");
                return sdf.format(currenTimeZone);
            } else {
                SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");
                return sdf.format(currenTimeZone);
            }
        }
    }

    /**
     * 时间转化为聊天界面显示字符串
     *
     * @param timeStamp 单位为秒
     */
    public static String getChatTimeStr(long timeStamp) {
        if (timeStamp == 0) return "";
        Calendar inputTime = Calendar.getInstance();
        inputTime.setTimeInMillis(timeStamp * 1000);
        Date currenTimeZone = inputTime.getTime();
        Calendar calendar = Calendar.getInstance();
        if (calendar.before(inputTime)) {
            //当前时间在输入时间之前
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
            return sdf.format(currenTimeZone);
        }
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        if (calendar.before(inputTime)) {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            return sdf.format(currenTimeZone);
        }
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        if (calendar.before(inputTime)) {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            return "昨天 " + sdf.format(currenTimeZone);
        } else {
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            calendar.set(Calendar.MONTH, Calendar.JANUARY);
            if (calendar.before(inputTime)) {
                SimpleDateFormat sdf = new SimpleDateFormat("M月d日" + " HH:mm");
                return sdf.format(currenTimeZone);
            } else {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日" + " HH:mm");
                return sdf.format(currenTimeZone);
            }
        }
    }

    public static int getYear() {
        return Calendar.getInstance().get(Calendar.YEAR);
    }

    public static int getMonth() {
        return Calendar.getInstance().get(Calendar.MONTH) + 1;
    }

    public static int getCurrentMonthDay() {
        return Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 将字符串转为时间戳
     */
    public static long getStringToDate(String time) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日");
        Date date = new Date();
        try {
            date = simpleDateFormat.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date.getTime();
    }

    //获得当前日期与本周一相差的天数
    public static int getMondayPlus() {
        Calendar cd = Calendar.getInstance();
        // 获得今天是一周的第几天，星期日是第一天，星期二是第二天......
        int dayOfWeek = cd.get(Calendar.DAY_OF_WEEK);
        if (dayOfWeek == 1) {
            return -6;
        } else {
            return 2 - dayOfWeek;
        }
    }

    //获得本周星期一的日期
    public static Calendar getCurrentMonday() {
        int mondayPlus = getMondayPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, mondayPlus);
        Date monday = currentDate.getTime();
        Calendar c = Calendar.getInstance();
        c.setTime(monday);
        return c;
    }

    // 上/下 一月
    public static String getLastOrNextMonth(String timeStr, int value) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月");
        Date date = new Date();
        if (!timeStr.equals("本月")) {
            try {
                date = dateFormat.parse(timeStr);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date); // 设置为当前时间
        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - value);
        date = calendar.getTime();
        return dateFormat.format(date);
    }

    // 上/下 一周
    public static String getLastOrNextWeek(String timeStr, int value) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年");
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        if (!timeStr.equals("本周")) {
            try {
                date = dateFormat.parse(timeStr.substring(0, timeStr.indexOf("年") + 1));
                calendar.setTime(date); // 设置为当前时间
                calendar.set(Calendar.WEEK_OF_YEAR, Integer.parseInt(timeStr
                        .substring(timeStr.indexOf("年") + 1, timeStr.indexOf("周"))) - value);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            calendar.setTime(date); // 设置为当前时间
            calendar.set(Calendar.WEEK_OF_YEAR, calendar.get(Calendar.WEEK_OF_YEAR) - value);
        }
        date = calendar.getTime();
        if (calendar.get(Calendar.WEEK_OF_YEAR) > 9) {
            return dateFormat.format(date) + calendar.get(Calendar.WEEK_OF_YEAR) + "周";
        } else {
            return dateFormat.format(date) + "0" + calendar.get(Calendar.WEEK_OF_YEAR) + "周";
        }
    }

    // 上/下 一日
    public static String getLastOrNextDay(String timeStr, int value) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        if (!timeStr.equals("今日")) {
            try {
                date = dateFormat.parse(timeStr);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date); // 设置为当前时间
        calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) - value);
        date = calendar.getTime();
        return dateFormat.format(date);
    }

    // 是否是今日
    public static Boolean isToday(String timeStr) {
        return getDateEN(System.currentTimeMillis()).equals(timeStr);
    }
//
//    // 是否是本周
//    public static Boolean isTswk(String timeStr) {
//
//        Calendar calendar = Calendar.getInstance();
//        int year = Integer.parseInt(timeStr.substring(0, timeStr.indexOf("年")));
//        int week = Integer.parseInt(timeStr
//                .substring(timeStr.indexOf("年") + 1, timeStr.indexOf("周")));
//
//        return calendar.get(Calendar.YEAR) == year && calendar.get(Calendar.WEEK_OF_YEAR) == week;
//    }

    // 是否是本月
    public static Boolean isTsM(String timeStr) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月");
        String nowTime = dateFormat.format(new Date(System.currentTimeMillis()));

        return timeStr.equals(nowTime);
    }


    public static String getDisparity(String timeStamp) {
        Long target = getDateByString(timeStamp);
        Long now = System.currentTimeMillis();
        if (target < now) {
            return "已截止";
        }
        Long l = target - now;
        long Minute = (l / (1000 * 60));
        long day = (l / (1000 * 60 * 60 * 24));
        long min = Minute - (day * 60 * 24);
        long hour = min / 60;
        long minute = min - (hour * 60);
        return day + "天" + hour + ":" + minute + "";
    }

}
