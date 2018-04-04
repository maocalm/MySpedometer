package com.baisi.spedometer.fcm;

/**
 * Created by hanwenmao on 2018/3/15.
 */

public class FirebaseMessagingHelper {

   public static class Type {
        public static final String pedometer_tools_notification = "pedometer_tools_notification";   //发送特定的通知
        static final  String   pedometer_tools_notification_timer = "pedometer_tools_notification_timer";  //后台定时发送唤醒应用

        static final String  pedometer_tools_dialog_morning = "pedometer_tools_dialog_morning";
        static final String  pedometer_tools_dialog_morning_time = "pedometer_tools_dialog_morning_time";   // 改变弹窗的弹出时间
     
        static final String  pedometer_tools_dialog_noon = "pedometer_tools_dialog_noon";
        static final String  pedometer_tools_dialog_noon_time = "pedometer_tools_dialog_noon_time";

        static final String  pedometer_tools_dialog_night = "pedometer_tools_dialog_night";
        static final String  pedometer_tools_dialog_night_time = "pedometer_tools_dialog_night_time";

    }
}
