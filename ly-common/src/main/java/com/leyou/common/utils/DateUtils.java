package com.leyou.common.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Auther: tianchao
 * @Date: 2019/12/12 20:24
 * @Description:
 */
public class DateUtils {
    public static final String YMDHMS = "yyyy-MM-dd HH:mm:ss";
    public static String getDateStr(Date date,String pattern){
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(date);
    }
}
