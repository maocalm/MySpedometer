package com.baisi.spedometer.fcm;

import android.content.Intent;
import android.util.Log;

import com.baisi.spedometer.R;
import com.baisi.spedometer.app.PedometerApplication;
import com.baisi.spedometer.utiles.Firebase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

/**
 * 云推送
 *
 * @author magic
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();
    private static final String pedometer_tools_notification = "/topics/" + FirebaseMessagingHelper.Type.pedometer_tools_notification;
    private static final String pedometer_tools_notification_timer = "/topics/" + FirebaseMessagingHelper.Type.pedometer_tools_notification_timer;
    private static final String pedometer_tools_dialog_morning_time = "/topics/" + FirebaseMessagingHelper.Type.pedometer_tools_dialog_morning_time;
    private static final String pedometer_tools_dialog_noon_time = "/topics/" + FirebaseMessagingHelper.Type.pedometer_tools_dialog_noon_time;
    private static final String pedometer_tools_dialog_night_time = "/topics/" + FirebaseMessagingHelper.Type.pedometer_tools_dialog_night_time;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.e(TAG, "message: " + remoteMessage.getFrom());
        String from = remoteMessage.getFrom();


        //带主题
        if (from != null && !from.equals(getResources().getString(R.string.firebase_fcm_sendid))) {
            Firebase.getInstance(getApplicationContext()).logEvent("pedometer_tools_notification_timer", from);
            Log.e(TAG, "onMessageReceived:  " + from);
            if (pedometer_tools_notification_timer.equals(from)) {
                //Log.e(TAG, "onMessageReceived:   3333 " + from + "===="+remoteMessage.getData().get("notificationMessage"));
                String  message = remoteMessage.getData().get("notificationMessage");
                startService(FirebaseMessagingHelper.Type.pedometer_tools_notification_timer  , message);

            } else if (pedometer_tools_dialog_morning_time.equals(from)) {

                startService(FirebaseMessagingHelper.Type.pedometer_tools_dialog_morning_time ,null);
            } else if (pedometer_tools_dialog_noon_time.equals(from)) {

                startService(FirebaseMessagingHelper.Type.pedometer_tools_dialog_noon_time ,null );
            } else if (pedometer_tools_dialog_night_time.equals(from)) {

                startService(FirebaseMessagingHelper.Type.pedometer_tools_dialog_night_time ,null );
                //Log.e(TAG, "onMessageReceived:   444 " + from + "====" + remoteMessage.getNotification().getBody());
            } else if (pedometer_tools_notification.equals(from)) {

                startService(FirebaseMessagingHelper.Type.pedometer_tools_notification ,null );
            }

            String token = FirebaseInstanceId.getInstance().getToken();
            Firebase.getInstance(getApplicationContext()).logEvent("user_id", token);

        } else {
            Map<String, String> data = remoteMessage.getData();
            RemoteMessage.Notification notification = remoteMessage.getNotification();
            Log.e(TAG, "onMessageReceived: 没有主题" + notification.getTitle() + notification.getBody());
            if (data != null) {
                String title = data.get("notificationTitle");
                String message = data.get("notificationMessage");
                String url = data.get("notificationUrl");

                String videoId = data.get("notificationContent");
                Log.e(TAG, "onMessageReceived: 没有主题 message" + message);
                Log.e(TAG, "onMessageReceived: 没有主题 title" + title);
                Log.e(TAG, "onMessageReceived: 没有主题 url" + url);
                Log.e(TAG, "onMessageReceived: 没有主题 videoId" + videoId);
            }
            String token = FirebaseInstanceId.getInstance().getToken();
            Firebase.getInstance(this).logEvent("user_id", token);
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    /**
     * 启动服务，MyFirebaseMessagingService服务容易挂掉
     *
     * @param type
     */
    private void startService(String type ,String message ) {
        Intent intent = new Intent(this, CommonService.class);
        intent.putExtra("FirebaseMessagingType", type);
        intent.putExtra("FirebaseMessagingContent", message);
        this.startService(intent);
    }
}
