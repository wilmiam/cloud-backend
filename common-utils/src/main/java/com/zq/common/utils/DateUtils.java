package com.zq.common.utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

/**
 * @author wilmiam
 * @since 2019-05-17
 */
public final class DateUtils {

    private DateUtils() {
    }

    public static Date now() {
        return Date.from(Instant.now());
    }

    /**
     * 当前时间是否在指定时间后面
     *
     * @param date
     * @return
     */
    public static boolean isNowAfter(Date date) {
        LocalDateTime dateTime = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
        return LocalDateTime.now().isAfter(dateTime);
    }

    /**
     * 判断当前是否为指定时间的N个小时之后
     *
     * @param beginDate 要计算的时间
     * @param hours     小时数
     * @return 若当前已在N小时之后则返回{@code true}
     */
    public static boolean isNowHoursAfter(Date beginDate, int hours) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(beginDate);
        cal.add(Calendar.HOUR_OF_DAY, hours);

        return System.currentTimeMillis() > cal.getTimeInMillis();
    }

    /**
     * 获取N天之前的时间
     *
     * @param days
     * @return
     */
    public static Date getDaysBefore(int days) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, -days);
        return cal.getTime();
    }

    /**
     * 获取今天的开始时间 00:00:00.000
     *
     * @return
     */
    public static Date todayStart() {
        Calendar cal = Calendar.getInstance();
        toDayStart(cal);
        return cal.getTime();
    }

    /**
     * 获取今天的结束时间 23:59:59.999
     *
     * @return
     */
    public static Date todayEnd() {
        Calendar cal = Calendar.getInstance();
        toDayEnd(cal);
        return cal.getTime();
    }

    /**
     * 获取指定日期的当天开始时间 00:00:00.000
     *
     * @return
     */
    public static Date dayStart(Date date) {
        return dayStart(date, 0);
    }

    /**
     * 获取指定日期的添加天数开始时间 00:00:00.000
     *
     * @param date 日期
     * @param days 添加天数
     * @return
     */
    public static Date dayStart(Date date, int days) {
        if (date == null) {
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, days);
        toDayStart(cal);
        return cal.getTime();
    }

    /**
     * 获取指定日期的当天结束时间 23:59:59.999
     *
     * @return
     */
    public static Date dayEnd(Date date) {
        if (date == null) {
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        toDayEnd(cal);
        return cal.getTime();
    }

    /**
     * 获取两个小时前的时间
     *
     * @return
     */
    public static Date twoHoursAgo() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.HOUR_OF_DAY, -2);
        return cal.getTime();
    }

    public static Date offset(Date date, int calendarType, int offset) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(calendarType, offset);
        return cal.getTime();
    }

    /**
     * 获取N分钟前的时间
     *
     * @param minutes
     * @return
     */
    public static Date minutesAgo(int minutes) {
        return Date.from(LocalDateTime.now().minusMinutes(minutes).toInstant(ZoneOffset.ofHours(8)));
    }

    /**
     * 对日期格式化
     *
     * @param date
     * @return
     */
    public static String convertDateToStr(Date date) {
        return convertDateToStr(date, "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 对日期格式化
     *
     * @param date
     * @return
     */
    public static String convertDateToStr(Date date, String format) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault()).format(formatter);
    }

    /**
     * 将字符串转换成日期对象
     *
     * @param dateStr 日期字符串
     * @param format  转换格式
     * @return
     */
    public static Date convertToDate(String dateStr, String format) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        LocalDateTime localDateTime = LocalDateTime.parse(dateStr, formatter);
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    private static void toDayStart(Calendar cal) {
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
    }

    private static void toDayEnd(Calendar cal) {
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
    }

}
