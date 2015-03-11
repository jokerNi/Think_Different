package com.think_different.utility;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

/**
 * Created by oceancx on 15/3/7.
 * 时间帮助类 用于对时间进行转换
 */
public class TimeUtility {


    public static Calendar cal = Calendar.getInstance();
    public static final String TIME_PATTERN = "HH:mm:ss";
    public static final String DATE_TIME_MS_PATTERN = "yyyy-MM-dd HH:mm:ss.S";
    public static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_YYYYMMDD_PATTERN = "yyyyMMdd";
    public static final String TIME_HHMM_PATTERN = "HH:mm";
    public static final String TIME_HHMM_PATTERN2 = "HHmm";
    public static final String DATE_TIME_NO_HORI_PATTERN = "yyyyMMdd HH:mm:ss";
    public static final String DATE_TIME_NO_SPACE_PATTERN = "yyyyMMddHHmmss";
    public static final String DATE_TIME_MILLISECOND_PATTERN = "yyyyMMddHHmmssS";
    public static final String DATE_TIME_PLAYBILL_PATTERN = "yyyyMMdd HH:mm";
    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_FORMAT2 = "yyyy-MM-dd";
    public static final String DATE_ENGLISH_FORMAT = "EEE MMM dd HH:mm:ss zzz yyyy";

    public static final SimpleDateFormat timeFormat = new SimpleDateFormat(
            DATE_TIME_MS_PATTERN);
    public static final SimpleDateFormat dateFormat = new SimpleDateFormat(
            DATE_TIME_PATTERN);
    public static final SimpleDateFormat yyyyMMdd = new SimpleDateFormat(
            DATE_YYYYMMDD_PATTERN);
    public static final SimpleDateFormat HHmm = new SimpleDateFormat(
            TIME_HHMM_PATTERN);
    public static final SimpleDateFormat HHmm2 = new SimpleDateFormat(
            TIME_HHMM_PATTERN2);
    public static final SimpleDateFormat yyyyMMddHHmmss = new SimpleDateFormat(
            DATE_TIME_NO_HORI_PATTERN);
    public static final SimpleDateFormat yyyyMMddHHmmssFile = new SimpleDateFormat(
            DATE_TIME_NO_SPACE_PATTERN);
    public static final SimpleDateFormat PLAYBILL_TIME_PATTERN = new SimpleDateFormat(
            DATE_TIME_PLAYBILL_PATTERN);
    public static final SimpleDateFormat ENGLISH_SDF = new SimpleDateFormat(
            DATE_ENGLISH_FORMAT, Locale.ENGLISH);

    private static Map<String, SimpleDateFormat> patternFormatMap;

//    @SuppressWarnings("deprecation")
//    public static String getHourAndMinute(String datetime) {
//        Date date;
//        String dateString = "";
//        try {
//            date = convertStringToDate(DATE_TIME_PATTERN, datetime);
//            dateString = String.format("%02d", date.getHours()) + ":"
//                    + String.format("%02d", date.getMinutes());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return dateString;
//    }
//
//    public static Date GetDateFromDeadline(String deadline) {
//        Date date = null;
//        try {
//            date = convertStringToDate(DATE_FORMAT2, deadline);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return date;
//    }

    public static Long toLong(String created_at) {
        // 根据TimeStr 将其转化为Long Wed Mar 11 00:43
        String dateStr =  created_at;
        DateFormat formatter = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy");
        Date date = null;
        try {
            date = (Date)formatter.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        System.out.println(date);

//        Calendar cal = Calendar.getInstance();
//        cal.setTime(date);
//        String formatedDate = cal.get(Calendar.DATE) + "/" + (cal.get(Calendar.MONTH) + 1) + "/" +         cal.get(Calendar.YEAR);
//        System.out.println("formatedDate : " + formatedDate);
        return date.getTime();
    }


    public static String Description(String created_at) {
        //0- 59 分钟前

        //1-23小时前

        //1-3天前

        //绝对时间

        DateFormat formatter = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy");
        Date date = null;
        String time="";
        try {
            date = formatter.parse(created_at);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            time= cal.get( Calendar.YEAR) +"/" +(cal.get(Calendar.MONTH)+1) +"/" + cal.get(Calendar.DATE);

        } catch (ParseException e) {
            e.printStackTrace();
        }


        return time;
    }
}
