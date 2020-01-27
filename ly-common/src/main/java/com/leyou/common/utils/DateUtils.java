package com.leyou.common.utils;

import org.joda.time.DateTime;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Auther: tianchao
 * @Date: 2019/12/12 20:24
 * @Description:
 */
public class DateUtils {

    public static final String FORMAT_TIME = "yyyy-MM-dd HH:mm:ss";

    /**
     * 获取当前时间
     * @return
     */
    public static String getCurrentTime(){
        DateTime dt = new DateTime();
        return dt.toString(FORMAT_TIME);
    }

    /**
     * 获取系统当前时间按照指定格式返回
     * @param pattern
     * @return
     */
    public static String getCurrentTimePattern(String pattern) {
        DateTime dt = new DateTime();
        String time = dt.toString(pattern);
        return time;
    }

    /**
     * 解析日期 yyyy-MM-dd HH:mm:ss
     * @param timestamp
     * @return
     */
    public static String format(Long timestamp, String pattern) {
        String dateStr = "";
        if (null == timestamp || timestamp.longValue() < 0) {
            return dateStr;
        }
        try {
            Date date = new Date(timestamp);
            SimpleDateFormat format = new SimpleDateFormat(pattern);
            dateStr = format.format(date);
        } catch (Exception e) {
            // ignore
        }

        return dateStr;
    }
    public static void main(String[] args) {
        System.out.println(DateUtils.getCurrentTime());
    }
}
