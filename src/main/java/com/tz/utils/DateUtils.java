package com.tz.utils;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Date;

public class DateUtils {
    private static  final String STANDARD_FORMATE="yyyy-MM-dd HH:mm:ss";

    /**
     * 将时间转为字符串,使用joda-time依赖插件完成
     */
    public static  String dateTostr(Date date,String formate){
        DateTime dateTime=new DateTime(date);
        return  dateTime.toString(formate);
    }

    public static  String dateTostr(Date date){
        DateTime dateTime=new DateTime(date);
        return  dateTime.toString(STANDARD_FORMATE);
    }

    /**
     * 字符串转时间
     */
    public static  Date strTodate(String str){
        DateTimeFormatter dateTimeFormatter=DateTimeFormat.forPattern(STANDARD_FORMATE);
        DateTime dateTime=dateTimeFormatter.parseDateTime(str);
        return dateTime.toDate();
    }
    public static  Date strTodate(String str,String format){
        DateTimeFormatter dateTimeFormatter=DateTimeFormat.forPattern(format);
        DateTime dateTime=dateTimeFormatter.parseDateTime(str);
        return dateTime.toDate();
    }
}
