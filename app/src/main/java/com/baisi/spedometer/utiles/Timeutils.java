package com.baisi.spedometer.utiles;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by hanwenmao on 2018/3/16.
 */

public class Timeutils {
    /**
     * 判断是否大于某个时间点
     */
    public static boolean isOverTime(long time, int hour) {
        int currentTime = 0;
        SimpleDateFormat formatter = new SimpleDateFormat("HH");
        String dateString = formatter.format(time);
        try {
            currentTime = Integer.parseInt(dateString);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (currentTime - hour >= 0);
    }
    public  static String getTodayDate() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }

    /**
     * 判断是否大于8点小与10点
     */
    public static boolean isInTime8To10(long time ,int hour) {
        boolean overFlag = false;
        int currentTime = 0;
        SimpleDateFormat formatter = new SimpleDateFormat("HH");
        String dateString = formatter.format(time);
        try {
            currentTime = Integer.parseInt(dateString);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (currentTime >=hour && currentTime < hour+2);
    }

    /**
     * 判断是否大于19点小与22点
     */
    public static boolean isInTime16To18(long time ,int hour) {
        boolean overFlag = false;
        int currentTime = 0;
        SimpleDateFormat formatter = new SimpleDateFormat("HH");
        String dateString = formatter.format(time);
        try {
            currentTime = Integer.parseInt(dateString);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (currentTime >=hour && currentTime < hour+2);
    }

    /**
     * 判断是否大于11点小与12点
     */
    public static boolean isInTime10To11(long time) {
        boolean overFlag = false;
        int currentTime = 0;
        SimpleDateFormat formatter = new SimpleDateFormat("HH");
        String dateString = formatter.format(time);
        try {
            currentTime = Integer.parseInt(dateString);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (currentTime >=10 && currentTime < 11);
    }

    /**
     * 判断是否大于19点小与22点
     */
    public static boolean isInTime18To19(long time) {
        boolean overFlag = false;
        int currentTime = 0;
        SimpleDateFormat formatter = new SimpleDateFormat("HH" , Locale.getDefault());
        String dateString = formatter.format(time);
        try {
            currentTime = Integer.parseInt(dateString);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (currentTime >=18 && currentTime < 19);
    }

    /**
     * 判断是否大于19点小与22点
     */
    public static boolean isInTime22To23(long time) {
        boolean overFlag = false;
        int currentTime = 0;
        SimpleDateFormat formatter = new SimpleDateFormat("HH");
        String dateString = formatter.format(time);
        try {
            currentTime = Integer.parseInt(dateString);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (currentTime >=22 && currentTime < 23);
    }

    /**
     * 判断是否大于19点小与22点
     */
    public static boolean isInTime20To22(long time , int hour) {
        boolean overFlag = false;
        int currentTime = 0;
        SimpleDateFormat formatter = new SimpleDateFormat("HH");
        String dateString = formatter.format(time);
        try {
            currentTime = Integer.parseInt(dateString);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (currentTime >=hour && currentTime <hour+2);
    }
}
