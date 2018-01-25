package com.example.chat.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 时间工具类
 * Created by gg on 2016/10/24.
 */
public class DateUtil {

    private DateUtil() {

    }

    /**
     * 当前时间的字符串格式
     *
     * @return
     */
    public static String now() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }

    /**
     * 当前时间到分钟的字符串格式
     *
     * @return
     */
    public static String thisMin() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date());
    }


    /**
     * 上一分钟到分钟的字符串格式
     *
     * @return
     */
    public static String lastMin() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date(System.currentTimeMillis() - 60000));
    }

    /**
     * 当前时间到小时的字符串格式
     *
     * @return
     */
    public static String thisHour() {
        return new SimpleDateFormat("yyyy-MM-dd HH").format(new Date());
    }


    /**
     * 上一小时到小时的字符串格式
     *
     * @return
     */
    public static String lastHour() {
        return new SimpleDateFormat("yyyy-MM-dd HH").format(new Date(System.currentTimeMillis() - 3600000));
    }

    /**
     * 当前时间到天的字符串格式
     *
     * @return
     */
    public static String thisDay() {
        return new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    }

    /**
     * 当前时间到月的字符串格式
     *
     * @return
     */
    public static String thisMon() {
        return new SimpleDateFormat("yyyy-MM").format(new Date());
    }

    /**
     * 时间戳的字符串标示
     *
     * @param t
     * @return
     */
    public static String format(long t) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(t));
    }

    /**
     * 时间戳的字符串标示
     *
     * @param t
     * @return
     */
    public static String format10min(long t) {
        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date(t));
        return time.substring(0, time.length() - 1);
    }

    /**
     * 字符串转日期
     *
     * @param str
     * @return
     */
    public static Date parse(String str) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new Date();
    }

    /**
     * 当前时间的字符串分值double格式
     *
     * @return
     */
    public static double formatScore(Date date) {
        String str = new SimpleDateFormat("yyyyMMddHHmmss").format(date);
        return new Double(str);
    }


    /**
     * double转日期
     *
     * @param score
     * @return
     */
    public static Date parseScore(double score) {
        try {
            return new SimpleDateFormat("yyyyMMddHHmmss").parse(String.valueOf(Double.valueOf(score).longValue()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new Date();
    }
}
