package com.lokiy.cloud.common.util.time;


import com.lokiy.cloud.common.base.constant.GlobalConstant;
import com.lokiy.cloud.common.base.constant.NumberConsts;
import com.lokiy.cloud.common.base.constant.StrNumberConsts;
import com.lokiy.cloud.common.base.constant.SymbolConsts;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.Date;

/**
 * @author Lokiy
 * @date 2019/7/17 15:51
 * @description: 日期处理类
 */
public class DateUtil {

    public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

    public static final String YYYY_MM_DD = "yyyy-MM-dd";

    public static final String HH_MM_SS = "HH:mm:ss";

    public static final String YYYY_MM = "yyyy-MM";


    /**
     * 获取当前时间戳
     * @return 当前时间戳
     */
    public static long timestamp(){
        return Instant.now().getEpochSecond();
    }

    /**
     * 根据表达式 str转date
     * @param dateStr
     * @param pattern
     * @return
     */
    public static Date str2DatePattern(String dateStr, String pattern){
        DateTimeFormatter df = DateTimeFormatter.ofPattern(pattern);
        if(YYYY_MM_DD_HH_MM_SS.equals(pattern)){
            LocalDateTime localDateTime = LocalDateTime.parse(dateStr, df);
            return localDateTime2Date(localDateTime);
        }
        if(YYYY_MM_DD.equals(pattern)){
            LocalDate localDateTime = LocalDate.parse(dateStr, df);
            return localDate2Date(localDateTime);
        }
        if(YYYY_MM.equals(pattern)){
            dateStr = dateStr + SymbolConsts.SHORT_LINE + StrNumberConsts.ZERO_ONE;
            df = DateTimeFormatter.ofPattern(YYYY_MM_DD);
            LocalDate localDateTime = LocalDate.parse(dateStr, df);
            return localDate2Date(localDateTime);
        }
        if(HH_MM_SS.equals(pattern)){
            LocalTime localDateTime = LocalTime.parse(dateStr, df);
            return localTime2Date(localDateTime);
        }
        return null;
    }

    /**
     * 根据表达式 date转str
     * @param date
     * @return
     */
    public static String date2StrPattern(Date date, String pattern){
        DateTimeFormatter df = DateTimeFormatter.ofPattern(pattern);
        return df.format(date2LocalDateTime(date));
    }

    /**
     * date转str
     * @param date
     * @return
     */
    public static String date2Str(Date date){
        DateTimeFormatter df = DateTimeFormatter.ofPattern(YYYY_MM_DD_HH_MM_SS);
        return df.format(date2LocalDateTime(date));
    }

    /**
     * str转date
     * @param dateStr
     * @return
     */
    public static Date str2Date(String dateStr){
        DateTimeFormatter df = DateTimeFormatter.ofPattern(YYYY_MM_DD_HH_MM_SS);
        LocalDateTime localDateTime = LocalDateTime.parse(dateStr, df);
        return localDateTime2Date(localDateTime);
    }

    /**
     * date转localDateTime
     * @param date
     * @return
     */
    public static LocalDateTime date2LocalDateTime(Date date){
        Instant instant = date.toInstant();
        ZoneId zoneId = ZoneId.systemDefault();
        return LocalDateTime.ofInstant(instant, zoneId);
    }

    /**
     * localDateTime转date
     * @param localDateTime
     * @return
     */
    public static Date localDateTime2Date(LocalDateTime localDateTime){
        ZoneId zoneId = ZoneId.systemDefault();
        Instant instant = localDateTime.atZone(zoneId).toInstant();
        return Date.from(instant);
    }


    /**
     * date转localDate
     * @param date
     * @return
     */
    public static LocalDate date2LocalDate(Date date){
        return date2LocalDateTime(date).toLocalDate();
    }

    /**
     * localDate转date
     * @param localDate
     * @return
     */
    public static Date localDate2Date(LocalDate localDate){
        ZoneId zoneId = ZoneId.systemDefault();
        Instant instant = localDate.atStartOfDay().atZone(zoneId).toInstant();
        return Date.from(instant);
    }


    /**
     * date转localTime
     * @param date
     * @return
     */
    public static LocalTime date2LocalTime(Date date){
        return date2LocalDateTime(date).toLocalTime();
    }

    /**
     * localTime转date
     * @param localTime
     * @return
     */
    public static Date localTime2Date(LocalTime localTime){
        LocalDate localDate = LocalDate.now();
        LocalDateTime localDateTime = LocalDateTime.of(localDate, localTime);
        ZoneId zoneId = ZoneId.systemDefault();
        Instant instant = localDateTime.atZone(zoneId).toInstant();
        return Date.from(instant);
    }


    /**
     * 获取日期的年份
     * @param date 传入日期
     * @return 获取年份
     */
    public static int year(Date date){
        return date2LocalDateTime(date).getYear();
    }


    /**
     * 获取日期的月份
     * @param date 传入日期
     * @return 获取月
     */
    public static int month(Date date){
        return date2LocalDateTime(date).getMonthValue();
    }

    /**
     * 获取日
     * @param date 传入日期
     * @return 获取日
     */
    public static int day(Date date){
        return date2LocalDateTime(date).getDayOfMonth();
    }


    /**
     * 获取年月字符串
     * @param year
     * @param month
     * @return
     */
    public static String getYearAndMonth(int year, int month){
        return year + SymbolConsts.SHORT_LINE + (month > NumberConsts.NINE_INT?
                month : StrNumberConsts.ZERO + month);
    }

    /**
     * 获取年月字符串
     * @param date
     * @return
     */
    public static String getYearAndMonth(Date date){
        LocalDateTime localDateTime = date2LocalDateTime(date);
        int year = localDateTime.getYear();
        int month = localDateTime.getMonthValue();
        return year + SymbolConsts.SHORT_LINE + (month > NumberConsts.NINE_INT?
                month : StrNumberConsts.ZERO + month);
    }


    public static Date offsetDate(Date date, long amountToAdd, TemporalUnit unit){
        return localDateTime2Date(date2LocalDateTime(date).plus(amountToAdd, unit));
    }


    /**
     * 前一小时
     * @param date 传入日期
     * @return 前 一小时
     */
    public static Date prevHour(Date date){
        return offsetDate(date, -1 , ChronoUnit.HOURS);
    }

    /**
     * 后一小时
     * @param date 传入日期
     * @return 后一小时
     */
    public static Date nextHour(Date date){
        return offsetDate(date, 1, ChronoUnit.HOURS);
    }




    /**
     * 前一天
     * @param date 传入日期
     * @return 前一天
     */
    public static Date prevDay(Date date){
        return offsetDate(date, -1 , ChronoUnit.DAYS);
    }

    /**
     * 后一天
     * @param date 传入日期
     * @return 后一天
     */
    public static Date nextDay(Date date){
        return offsetDate(date, 1, ChronoUnit.DAYS);
    }


    /**
     * 前一月
     * @param date 传入日期
     * @return 前一月
     */
    public static Date prevMonth(Date date){
        return offsetDate(date, -1, ChronoUnit.MONTHS);
    }

    /**
     * 后一月
     * @param date 传入日期
     * @return 后一月
     */
    public static Date nextMonth(Date date){
        return offsetDate(date, 1, ChronoUnit.MONTHS);
    }


    /**
     * 前一年
     * @param date 传入日期
     * @return 前一年
     */
    public static Date prevYear(Date date){
        return offsetDate(date, -1, ChronoUnit.YEARS);
    }

    /**
     * 后一年
     * @param date 传入日期
     * @return  后一年
     */
    public static Date nextYear(Date date){
        return offsetDate(date, 1, ChronoUnit.YEARS);
    }


    /**
     * 两日期之间的不同维度差值
     * @param sDate 之前的时间
     * @param bDate 后的时间
     * @param chronoUnit 时间类型
     * @return 时间间隔
     */
    public static long diff(Date sDate, Date bDate, ChronoUnit chronoUnit){
        switch (chronoUnit){
            case SECONDS:
                return ChronoUnit.SECONDS.between(date2LocalDateTime(sDate), date2LocalDateTime(bDate));
            case MINUTES:
                return ChronoUnit.MINUTES.between(date2LocalDateTime(sDate), date2LocalDateTime(bDate));
            case HOURS:
                return ChronoUnit.HOURS.between(date2LocalDateTime(sDate), date2LocalDateTime(bDate));
            case DAYS:
                return ChronoUnit.DAYS.between(date2LocalDate(sDate), date2LocalDate(bDate));
            case MONTHS:
                return ChronoUnit.MONTHS.between(date2LocalDate(sDate), date2LocalDate(bDate));
            case YEARS:
                return ChronoUnit.YEARS.between(date2LocalDate(sDate), date2LocalDate(bDate));

            default:
                return 0;
        }
    }
}
