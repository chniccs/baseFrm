package site.chniccs.basefrm.utils;

import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by chniccs on 2017/6/19 17:50.
 */

public class DateUtil {
    /**
     * @param format 要的格式
     * @param date   日期对象
     * @return 日期字符
     */
    public static String getDate(String format, Date date) {
        if (TextUtils.isEmpty(format)) {
            format = "yyyy-MM-dd HH:mm";
        }
        SimpleDateFormat myFmt1 = new SimpleDateFormat(format);
        return myFmt1.format(date);
    }

    /**
     * @param format 要的格式
     * @param date   日期时间毫秒
     * @return 日期字符
     */
    public static String getDate(String format, long date) {
        return getDate(format, new Date(date));
    }


    /**
     * 根据日期获取时间值
     *
     * @param format 日期格式 如yyyy-MM-dd
     * @param date   日期的字符
     * @return
     */
    public static long getDateToLong(String format, String date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        long longDate = 0;
        try {
            Date date1 = simpleDateFormat.parse(date);
            longDate = date1.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return longDate;
    }

    public static String getDate(long date) {
        Date date1 = new Date(date);
        return getDate(date1);
    }

    /**
     * @param date 日期对象
     * @return 日期字符
     */
    public static String getDate(Date date) {
        return getDate("", date);
    }
    //判断两个时间戳是否为同一天
    public static boolean isTwoTimeStampDayEqual(long firstTimeStamp, long secondTimeStamp){
        if(getYearByTimeStamp(firstTimeStamp) == getYearByTimeStamp(secondTimeStamp) &&
                getMonthByTimeStamp(firstTimeStamp) == getMonthByTimeStamp(secondTimeStamp)
                && getDayByTimeStamp(firstTimeStamp) == getDayByTimeStamp(secondTimeStamp)){
            return true;
        }
        return false;
    }
    public static int getYearByTimeStamp(long timeStamp){
        String date = timeStampToDate(timeStamp);
        String year = date.substring(0, 4);
        return Integer.parseInt(year);
    }

    public static int getMonthByTimeStamp(long timeStamp){
        String date = timeStampToDate(timeStamp);
        String month = date.substring(5, 7);
        return Integer.parseInt(month);
    }

    public static int getDayByTimeStamp(long timeStamp){
        String date = timeStampToDate(timeStamp);
        String day = date.substring(8, 10);
        return Integer.parseInt(day);
    }
    public static int getHourByTimeStamp(long timeStamp){
        String date = timeStampToDate(timeStamp);
        String hour = date.substring(11, 13);
        return Integer.parseInt(hour);
    }

    public static int getMinuteByTimeStamp(long timeStamp){
        String date = timeStampToDate(timeStamp);
        String minute = date.substring(14, 16);
        return Integer.parseInt(minute);
    }

    public static int getSecondByTimeStamp(long timeStamp){
        String date = timeStampToDate(timeStamp);
        String second = date.substring(17, 19);
        return Integer.parseInt(second);
    }
    public static String timeStampToDate(long timeStamp){
        Date             date = new Date(timeStamp);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateStr = simpleDateFormat.format(date);
        return dateStr;
    }
}
