package com.baisi.spedometer.fcm;

import android.util.Log;

import com.baisi.spedometer.utiles.Firebase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.firebase.messaging.FirebaseMessaging;


/**
 * 订阅GCM
 *
 * @author magic
 */
public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    private static final String TAG = "MyFirebaseIIDService";

    @Override
    public void onCreate() {
        super.onCreate();
        //订阅事件  主题


        // 云通知；
        FirebaseMessaging.getInstance().subscribeToTopic("pedometer_tools_notification");

        //云通知唤醒应用的
        FirebaseMessaging.getInstance().subscribeToTopic("pedometer_tools_notification_timer");

        //云弹窗---早上的弹窗
        FirebaseMessaging.getInstance().subscribeToTopic("pedometer_tools_dialog_morning");
        // 云弹窗---早上的弹窗时间
        FirebaseMessaging.getInstance().subscribeToTopic("pedometer_tools_dialog_morning_time");

        //云弹窗 -- 中午的
        FirebaseMessaging.getInstance().subscribeToTopic("pedometer_tools_dialog_noon");
        FirebaseMessaging.getInstance().subscribeToTopic("pedometer_tools_dialog_noon_time");

        //云弹窗---晚上的
        FirebaseMessaging.getInstance().subscribeToTopic("pedometer_tools_dialog_night");
        FirebaseMessaging.getInstance().subscribeToTopic("pedometer_tools_dialog_night_time");


        FirebaseMessaging.getInstance().subscribeToTopic("pedometer_tools_dialog_timer");

        // TODO: 2018/3/8 测试id
        //FirebaseMessaging.getInstance().subscribeToTopic("MagicTestHello");
    }

    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.e(TAG, "Refreshedtoken:" + refreshedToken);
        sendRegistrationToServer(refreshedToken);
    }

    private void sendRegistrationToServer(String token) {
        // TODO: Implement this method to send token to your app server.
        Firebase.getInstance(this).logEvent("user_id", token);
    }

}
