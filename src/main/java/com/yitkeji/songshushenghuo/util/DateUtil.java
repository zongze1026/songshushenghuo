package com.yitkeji.songshushenghuo.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @desc  日期操作工具类
 * @author  ysg
 * @create  2018/6/11
 **/
public class DateUtil {

    /**
     * 日期格式:MM-dd
     */
    public static final String DATEFORMAT_FIF = "MM-dd";

    /**
     * 日期格式:yyyyMM
     */
    public static final String DATEFORMAT_SIX = "yyyyMM";
    /**
     * 日期格式:yyyyMMddHHmmss
     */
    public static final String DATEFORMAT_EIGHT = "yyyyMMddHHmmss";
    /**
     * 日期格式:yyyy-MM-dd
     */
    public static final String DATEFORMAT_TEN = "yyyy-MM-dd";
    /**
     * 日期格式:yyyy-MM-dd HH:mm
     */
    public static final String DATEFORMAT_TWELVE = "yyyy-MM-dd HH:mm";
    /**
     * 日期格式:yyyy-MM-dd HH:mm:ss
     */
    public static final String DATEFORMAT_NINTEEN = "yyyy-MM-dd HH:mm:ss";

    /**
     * 计算两个日期之间相差的天数
     * @param smdate 较小的时间
     * @param bdate  较大的时间
     * @return 相差天数
     */
    public static int daysBetween(Date smdate, Date bdate){
        SimpleDateFormat sdf=new SimpleDateFormat(DateUtil.DATEFORMAT_TEN);
        try {
            smdate=sdf.parse(sdf.format(smdate));
            bdate=sdf.parse(sdf.format(bdate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(smdate);
        long time1 = cal.getTimeInMillis();
        cal.setTime(bdate);
        long time2 = cal.getTimeInMillis();
        long between_days=(time2-time1)/(1000*3600*24);
        return Integer.parseInt(String.valueOf(between_days));
    }

    /**
     * 距date前day天的时间
     * @return
     */
    public static Date getDayBeforeTime(Date date, Integer day) {
        Calendar ca = Calendar.getInstance();
        ca.setTime(date);
        ca.add(Calendar.DATE, day);
        return ca.getTime();
    }

    /**
     * Date类型转字符串
     * @return
     */
    public static String format(Date date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        String strDate;
        try {
            strDate = sdf.format(date);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return strDate;
    }

    /**
     * 字符串类型转Date
     * @return
     */
    public static Date parse(String strDate, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date date;
        try {
            date = sdf.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
        return date;
    }

    /**
     * 获取时间段内随机时间点
     * @param start 起始时间
     * @param end  结束时间
     * @return 获取到的随机时间
     */
    public static Date getMiddleTime(String start, String end){
        double middleL;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            Long startL = sdf.parse(start).getTime();
            Long endL = sdf.parse(end).getTime();
            middleL = startL + (endL - startL) * Math.random();
        }catch (Exception e){
            e.printStackTrace();
            return  null;
        }
        return new Date((long) middleL);
    }

    public static Date getTodayStartTime(){
        return getDayStartTime(Calendar.getInstance().getTime());
    }

    public static Date getDayStartTime(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    public static Date getTodayEndTime(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(getTodayStartTime());
        calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) + 1);
        return calendar.getTime();
    }

    public static Date getDayEndTime(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.setTimeInMillis(calendar.getTimeInMillis() - 1);
        return calendar.getTime();
    }

    public static Date getLastDay(Date date){
        return new Date(date.getTime() - 24 * 3600 * 1000);
    }

    public static Date getNextDay(Date date){
        return new Date(date.getTime() + 24 * 3600 * 1000);
    }

    public static boolean isTheSameDay(Date date1, Date date2){
        return getDayStartTime(date1).getTime() == getDayStartTime(date2).getTime();
    }

    public static boolean sameDay(Calendar calendar1, Calendar calendar2){
        return calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR)
                && calendar1.get(Calendar.MONTH) == calendar2.get(Calendar.MONTH)
                && calendar1.get(Calendar.DAY_OF_MONTH) == calendar2.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 获取当月某天的时间
     * @param day 具体某天
     * @return
     */
    public static String getOnTheDay(Integer day){
        Calendar calendar = Calendar.getInstance();
        String time = calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-" + day;
        Date timeDate = DateUtil.parse(time, DateUtil.DATEFORMAT_TEN);
        String strTime = DateUtil.format(timeDate, DateUtil.DATEFORMAT_TEN);
        return strTime;
    }

    /**
     * 获取上个月某天的时间
     * @param day 具体某天
     * @return
     */
    public static String getPreviousMonthDay(Integer day){
        Calendar calendar = Calendar.getInstance();
        String time = calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH)) + "-" + day;
        return time;
    }

    /**
     * 获取下个月某天的时间
     * @param day 具体某天
     * @return
     */
    public static String getNextMonthOnDay(Integer day){
        Calendar calendar = Calendar.getInstance();
        String time = calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 2) + "-" + day;
        return time;
    }
}
