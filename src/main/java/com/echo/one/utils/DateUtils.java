package com.echo.one.utils;


import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


public class DateUtils {

    private static final Logger logger = LoggerFactory.getLogger(DateUtils.class);

    private DateUtils() {
    }

    /**
     * 获取当前日期
     *
     * @return 返回yyyyMMdd格式日期
     */
    public static String getNowDate() {
        return format("yyyyMMdd");
    }

    /**
     * 获取当前日期
     *
     * @param pattern 日期格式
     * @return String 返回格式化后日期
     */
    public static String getNowDate(String pattern) {
        return format(pattern);
    }

    /**
     * 获取当前日期
     *
     * @param pattern 日期格式
     * @return String 返回格式化后日期
     */
    public static String format(String pattern) {
        DateFormat df = new SimpleDateFormat(pattern);
        return df.format(new Date());
    }


    /**
     * 获得上个月的会计期
     *
     * @param pattern 日期格式
     * @return String 返回格式化后会计期
     */
    public static String getPreMonth(String pattern) {
        DateFormat df = new SimpleDateFormat(pattern);
        Date now = new Date();
        Calendar cd = Calendar.getInstance();
        cd.setTime(now);
        cd.add(Calendar.MONDAY, -1);
        Date time = cd.getTime();
        return df.format(time);
    }

    /**
     * 指定日期为星期几
     *
     * @return 星期几
     */
    public static int dayForWeek(String date, String fomatStr) {
        SimpleDateFormat format = new SimpleDateFormat(fomatStr);
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(format.parse(date));
        } catch (ParseException e) {
            logger.error("日期转为星期几失败说", e);
        }
        int dayForWeek = 0;
        if (c.get(Calendar.DAY_OF_WEEK) == 1) {
            dayForWeek = 7;
        } else {
            dayForWeek = c.get(Calendar.DAY_OF_WEEK) - 1;
        }
        return dayForWeek;
    }

    /**
     * 日期转为String
     *
     * @return
     */
    public static String toString(Date date, String format) {
        DateFormat df = new SimpleDateFormat(format);
        return df.format(date);
    }


    /**
     * 将日期类型转换为字符类型日期
     *
     * @param date 传入日期
     * @return String 返回yyyy-MM-dd格式字符串
     */
    public static String format(Date date) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return df.format(date);
    }

    /**
     * 获取当前时间
     *
     * @return Date 返回yyyy-MM-dd HH:mm:ss格式日期
     */
    public static Date getNowDateTime() {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            return df.parse(getNowDate("yyyy-MM-dd HH:mm:ss"));
        } catch (ParseException e) {
            logger.error("日期格式化失败", e);
        }

        return null;
    }

    /**
     * 获取当前日期
     *
     * @return Date 返回yyyy-MM-dd格式日期
     */
    public static Date getDate() {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return df.parse(getNowDate("yyyy-MM-dd"));
        } catch (ParseException e) {
            logger.error("日期格式化失败", e);
        }

        return null;
    }

    /**
     * 字符串日期转Date类型日期
     *
     * @param date 输入日期
     * @return Date 返回yyyy-MM-dd格式日期
     */
    public static Date getDate(String date) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return df.parse(date);
        } catch (ParseException e) {
            logger.error("日期格式化失败", e);
        }

        return null;
    }

    /**
     * 获取当前时间
     *
     * @return String 返回 HHmmss 格式字符串
     */
    public static String getNowTime() {
        return format("HHmmss");
    }

    /**
     * 获取当前时间
     *
     * @param pattern 格式化类型
     * @return String 返回pattern格式化时间
     */
    public static String getNowTime(String pattern) {
        return format(pattern);
    }

    /**
     * 获取当前时间
     *
     * @return String 返回yyyyMMddHHmmssSSS格式化时间
     */
    public static String getFullTime() {
        return format("yyyyMMddHHmmssSSS");
    }

    public static String getFullTimes() {
        return format("yyyyMMddHHmmss");
    }

    public static String getFullNowTime() {
        return format("yyyy-MM-dd HH:mm:ss");
    }


    public static String getAccurateNowTime() {
        return format("yyyy-MM-dd HH:mm:ss.SSS");
    }


    /**
     * 获取当前时间
     *
     * @param pattern
     * @return String 返回pattern格式化时间
     */
    public static String getFullTime(String pattern) {
        return format(pattern);
    }

    /**
     * 将yyyy-MM-dd格式日期转换为yyyyMMdd格式日期
     *
     * @param orgi 传入yyyy-MM-dd格式日期
     * @return String 传入为空则返回空；传入日期不包括'-'字符返回空；
     */
    public static String parse(String orgi) {
        if (StringUtils.isBlank(orgi))
            return StringUtils.EMPTY;
        String dest = null;
        if (orgi.indexOf('-') < 0) {
            return orgi;
        }
        try {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            Date date = df.parse(orgi);
            df = new SimpleDateFormat("yyyyMMdd");
            dest = df.format(date);
        } catch (ParseException e) {
            logger.error("日期格式化失败", e);
        }
        return dest;
    }

    /**
     * 将yyyy-MM-dd格式日期转换为yyyyMMdd格式日期
     *
     * @param date 传入 yyyy-MM-dd格式日期
     * @return String 传入为空则返回空；传入日期不包括'-'字符返回空；
     */
    public static String getDateString(Date date) {
        try {
            if (date == null) {
                date = new Date();
            }
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            return df.format(date);
        } catch (Exception e) {
            logger.error("日期格式化失败", e);
        }
        return null;
    }

    /**
     * 将 yyyy年MM月dd日 格式日期转换为yyyy-MM-dd格式日期
     *
     * @param orgi 传入 yyyy年MM月dd日 格式日期
     * @return String String 返回yyyy-MM-dd格式字符串
     */
    public static String parse2(String orgi) {
        if (StringUtils.isBlank(orgi))
            return StringUtils.EMPTY;
        String dest = null;
        try {
            DateFormat df = new SimpleDateFormat("yyyy年MM月dd日");
            Date date = df.parse(orgi);
            df = new SimpleDateFormat("yyyy-MM-dd");
            dest = df.format(date);
        } catch (ParseException e) {
            logger.error("日期格式化失败", e);
        }
        return dest;
    }

    /**
     * 将long类型日期转换为字符串类型日期
     *
     * @param date 传入long类型日期
     * @return String 返回yyyy-MM-dd格式的字符串类型日期
     */
    public static String parseLong2Date(long date) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return df.format(new Date(date));
    }


    /**
     * 计算两个日期相差的天数
     *
     * @param source
     * @param target
     * @return
     */
    public static int getBetweenDays(String source, String target)
            throws ParseException {
        long dayNumber = 0;
        long DAY = 24L * 60L * 60L * 1000L;
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date d1 = df.parse(target);
            Date d2 = df.parse(source);
            dayNumber = (d2.getTime() - d1.getTime()) / DAY;
        } catch (Exception e) {
            logger.error("日期计算失败", e);
        }
        return (int) dayNumber;
    }

    /**
     * 计算两个日期相差的月数
     *
     * @param source string类型日期 格式yyyy-MM-dd
     * @param target string类型日期 格式yyyy-MM-dd
     * @return
     */
    public static int getBetweenMons(String source, String target)
            throws Exception {
        Calendar cal1 = new GregorianCalendar();
        cal1.setTime(getDate(source));
        Calendar cal2 = new GregorianCalendar();
        cal2.setTime(getDate(target));
        int c = (cal1.get(Calendar.YEAR) - cal2.get(Calendar.YEAR)) * 12
                + cal1.get(Calendar.MONTH) - cal2.get(Calendar.MONTH);
        return c;
    }

    /**
     * 计算两个日期相差的年数
     *
     * @param source string类型日期 格式yyyy-MM-dd
     * @param target string类型日期 格式yyyy-MM-dd
     * @return float 返回两日期相差年数
     */
    public static float getBetweenYears(String source, String target)
            throws Exception {
        Calendar cal1 = new GregorianCalendar();
        cal1.setTime(getDate(source));
        Calendar cal2 = new GregorianCalendar();
        cal2.setTime(getDate(target));
        float c = (cal1.get(Calendar.YEAR) - cal2.get(Calendar.YEAR)) * 12
                + cal1.get(Calendar.MONTH) - cal2.get(Calendar.MONTH);
        return c / 12;
    }

    /**
     * 在一个日期上加指定的月数
     *
     * @param dateStr   日期 格式yyyy-MM-dd
     * @param addMonths 加计月数
     * @return Date 返回yyyy-MM-dd格式日期
     */
    public static Date addMonths(String dateStr, int addMonths) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = dateFormat.parse(dateStr);
        } catch (ParseException e) {
            logger.error("日期格式化失败", e);
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, addMonths);
        Date nowDate = calendar.getTime();
        return getDate(dateFormat.format(nowDate));
    }


    public static int getDayOfWeek(String dateStr) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        Date date = null;
        try {
            date = dateFormat.parse(dateStr);
        } catch (ParseException e) {
            logger.error("日期格式化失败", e);
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int w = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 1)
            w = 1;

        return w;
    }

    /**
     * 一个日期上加相应的天数
     *
     * @param dateStr 日期 格式yyyy-MM-dd
     * @param days    加计天数 正数是加，负数是减
     * @return String 返回yyyy-MM-dd格式日期
     */
    public static String addDays(String dateStr, int days) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = dateFormat.parse(dateStr);
        } catch (ParseException e) {
            logger.error("日期格式化失败", e);
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, days);
        Date nowDate = calendar.getTime();
        return dateFormat.format(nowDate);
    }

    /**
     * 获取当日去年日期加上addMonths月数后的前一天
     *
     * @param dateStr   传入日期 格式yyyy-MM-dd
     * @param addMonths 加计月数
     * @return Date 返回当日去年日期加上addMonths月数后的前一天 格式yyyy-MM-dd
     */
    public static Date addMonthsEnd(String dateStr, int addMonths) {
        Date endMonth = addMonths(dateStr, addMonths);
        return getDate(addDays(format(endMonth), -1));
    }

    /**
     * 获取当前时间
     *
     * @return String 返回当前时间 格式yyyy-MM-dd
     */
    public static String getCurDate() {
        Date date = new Date();
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        String result = f.format(date);
        return result;
    }

    /**
     * 获取当前时间
     *
     * @return String 返回当前时间 格式yyyyMMdd
     */
    public static String getCurDate8() {
        Date date = new Date();
        SimpleDateFormat f = new SimpleDateFormat("yyyyMMdd");
        String result = f.format(date);
        return result;
    }

    /**
     * 获取当前时间
     *
     * @param farmat 时间格式
     * @return String 返回farmat格式时间
     */
    public static String getCurDate(String farmat) {
        Date date = new Date();
        SimpleDateFormat f = new SimpleDateFormat(farmat);
        String result = f.format(date);
        return result;
    }

    /**
     * 获取当月第一天*
     *
     * @return String 返回当月第一天 格式yyyy-MM-dd
     */
    public static String getCurMonthStart() {
        String result = getCurDate();
        result = result.substring(0, 7);
        result = result + "-01";
        return result;
    }

    /**
     * 获取当月第一天
     *
     * @return String 返回当月第一天 格式yyyyMMdd
     */
    public static String getCurMonthStart8() {
        String result = getCurDate8();
        result = result.substring(0, 6);
        result = result + "01";
        return result;
    }

    /**
     * 字符串日期类型转换为日期类型
     *
     * @param str    字符串日期
     * @param format 日期格式
     * @return Date 返回日期 格式format
     */
    public static Date stringToDate(String str, String format) {
        DateFormat df = new SimpleDateFormat(format);
        Date date = null;
        try {
            date = df.parse(str);
        } catch (ParseException e) {
            logger.error("日期格式化失败", e);
        }
        return date;
    }

    /**
     * 字符串类型日期转换为日期类型
     *
     * @param str 传入字符串 格式yyyy-MM-dd
     * @return Date 返回yyyy-MM-dd格式日期
     */
    public static Date stringToDate10(String str) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = df.parse(str);
        } catch (ParseException e) {
            logger.error("日期格式化失败", e);
        }
        return date;
    }

    /**
     * 字符串类型日期转换为日期类型
     *
     * @param str 传入字符串 格式yyyyMMdd
     * @return Date 返回yyyyMMdd格式日期
     */
    public static Date stringToDate8(String str) {
        DateFormat df = new SimpleDateFormat("yyyyMMdd");
        Date date = null;
        try {
            date = df.parse(str);
        } catch (ParseException e) {
            logger.error("日期格式化失败", e);
        }
        return date;
    }

    /**
     * 日期类型转换为字符串类型日期
     *
     * @param date 传入日期 格式yyyy-MM-dd
     * @return String 返回字符串日期 格式yyyy-MM-dd
     */
    public static String dateToString10(Date date) {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }

    public static String dateToString8(Date date) {
        DateFormat format = new SimpleDateFormat("yyyyMMdd");
        return format.format(date);
    }

    /**
     * 禁止使用
     *
     * @param date
     * @return String
     */
    private static String toString(Date date) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return df.format(date);
    }

    /**
     * 获取当前日期前一月日期
     *
     * @return Date 日期类型时间
     */
    public static Date getOneMonthAgoDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -1);
        calendar.add(Calendar.DATE, 1);

        return calendar.getTime();
    }

    /**
     * 获取当前日期前一月日期
     *
     * @return String 字符串类型时间 格式yyyy-MM-dd
     */
    public static String getOneMonthAgoDateString() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -1);
        calendar.add(Calendar.DATE, 1);

        return toString(calendar.getTime());
    }

    /**
     * 禁止使用
     *
     * @return String
     */
    public static String getd() {
        return Calendar.getInstance().get(Calendar.YEAR) + "-"
                + (Calendar.getInstance().get(Calendar.MONTH) + 1) + "-"
                + Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

    }

    /**
     * 获取当前日前七天的日期
     *
     * @return String 返回字符串类型时间 格式yyyyMMdd
     */
    public static String getAgoServerDay() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -7);
        String str = toString(calendar.getTime());
        return str.replaceAll("-", StringUtils.EMPTY);
    }

    /**
     * 输入开始日期 和 相加的月数 得到相加后日期
     *
     * @param inPrea    输入日期 格式yyyyMMdd
     * @param addMonths 加计月数
     * @return String
     */
    public static String addMonths(String inPrea, String addMonths) {
        Date date = stringToDate8(inPrea);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, +Integer.parseInt(addMonths));
        String str = toString(calendar.getTime());
        return str.replaceAll("-", StringUtils.EMPTY);
    }

    /**
     * 获取输入日期的上周五时间
     *
     * @param day 输入日期 格式yyyyMMdd
     * @return String 返回字符串类型日期 格式yyyyMMdd
     */
    public static String getLastFriday(String day) {
        Date date = stringToDate8(day);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int x = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        calendar.add(Calendar.DATE, -(x + 2));
        return toString(calendar.getTime());
    }

    /**
     * 比较两个日期的大小
     *
     * @param source source
     * @param target target
     * @return 0:source=target 1:source>target 2:source<target
     */
    public static int compare_date(Date source, Date target) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date dt1 = source;
            Date dt2 = target;
            if (dt1.getTime() > dt2.getTime()) {
                return 1;
            } else if (dt1.getTime() < dt2.getTime()) {
                return -1;
            } else {
                return 0;
            }
        } catch (Exception e) {
            logger.error("日期计算失败", e);
        }
        return 0;
    }

    /**
     * 取两个日期的最小值
     *
     * @param source source
     * @param target target
     * @return 返回最小值
     */
    public static String minDay(String source, String target) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date dt1 = df.parse(source);
            Date dt2 = df.parse(target);
            if (dt1.getTime() > dt2.getTime()) {
                return target;
            } else {
                return source;
            }
        } catch (Exception e) {
            logger.error("日期计算失败", e);
        }
        return null;
    }

    /**
     * 获取指定日期中的dd
     *
     * @param date
     * @return
     */
    public static int getDay(String date) {
        try {
            Calendar cal = Calendar.getInstance();
            cal.setTime(getDate(date));
            int day = cal.get(Calendar.DAY_OF_MONTH);
            return day;
        } catch (Exception e) {
            logger.error("获取指定日期中的day失败", e);
        }
        return 0;
    }

    /**
     * 获取当前详细时间，带毫秒
     *
     * @return
     */
    public static Timestamp getDetailNowDateTimestamp() {
        //获取当前时间
        String current = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date());
        Timestamp time = Timestamp.valueOf(current);
        return time;
    }

}
