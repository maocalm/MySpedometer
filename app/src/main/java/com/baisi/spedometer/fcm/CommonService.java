package com.baisi.spedometer.fcm;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.TimeUtils;

import com.baisi.spedometer.BuildConfig;
import com.baisi.spedometer.R;
import com.baisi.spedometer.activity.ExerciseDialogActivity;
import com.baisi.spedometer.activity.MorningDialogActivity;
import com.baisi.spedometer.activity.StepGoalDialogActivity;
import com.baisi.spedometer.step.utils.SharedPreferencesUtils;
import com.baisi.spedometer.utiles.NetUtils;
import com.baisi.spedometer.utiles.Timeutils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;


/**
 * Created by magic on 2017/12/14.通用Service
 */

public class CommonService extends Service {
    private static final String TAG = CommonService.class.getSimpleName();
    private SharedPreferencesUtils sp;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        registerFirebaseMessagingListener(intent);
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sp = new SharedPreferencesUtils(this);
        initRemoteConfig();

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        startService(new Intent(this, CommonService.class));
    }


    //==============================================================================================
    //               监听接收到云消息 GCM
    //==============================================================================================

    /**
     * 注册云消息监听
     */
    private void registerFirebaseMessagingListener(Intent intent) {

        if (null == intent || !NetUtils.isConnected(this)) {
            return;
        }

        String type = intent.getStringExtra("FirebaseMessagingType");
        String message = intent.getStringExtra("FirebaseMessagingContent");
        try {
            switch (type) {
                case FirebaseMessagingHelper.Type.pedometer_tools_notification_timer:
                    solveFcmMessage(message);
                    break;
                case FirebaseMessagingHelper.Type.pedometer_tools_notification:
                    break;
                case FirebaseMessagingHelper.Type.pedometer_tools_dialog_morning:
                    break;
                case FirebaseMessagingHelper.Type.pedometer_tools_dialog_night:
                    break;
                case FirebaseMessagingHelper.Type.pedometer_tools_dialog_noon:
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void solveFcmMessage(String message) {
        if (!NetUtils.isConnected(this)) {
            return;
        }
        String[] messagecontent = message.split(" ");
        if (messagecontent.length == 3) {
            String message1 = messagecontent[0];
            String message2 = messagecontent[1];
            String message3 = messagecontent[2];
            int hour1 = Integer.valueOf(message1);
            int hour2 = Integer.valueOf(message2);
            int hour3 = Integer.valueOf(message3);
            Log.d(TAG, "solveFcmMessage: " + hour1 + "  " + hour2 + "  " + hour3);


            if (Timeutils.isInTime8To10(System.currentTimeMillis(), hour1) && !sp.getParam(SharedPreferencesUtils.MORNING_DIALOG, "hh").equals(Timeutils.getTodayDate())) {
                MorningDialogActivity.startMe(CommonService.this);
            } else if (Timeutils.isInTime16To18(System.currentTimeMillis(), hour2) && !sp.getParam(SharedPreferencesUtils.NOON_DIALOG, "hh").equals(Timeutils.getTodayDate())) {
                ExerciseDialogActivity.startMe(CommonService.this);
            } else if (Timeutils.isInTime20To22(System.currentTimeMillis(), hour3) && !sp.getParam(SharedPreferencesUtils.NIGHT_DIALOG, "hh").equals(Timeutils.getTodayDate())) {
                StepGoalDialogActivity.startMe(CommonService.this);
            }
        }
    }

    String WELCOME_MESSAGE_KEY = "welcom";

    private void initRemoteConfig() {
        final FirebaseRemoteConfig firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();

        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(BuildConfig.DEBUG)
                .build();
        firebaseRemoteConfig.setConfigSettings(configSettings);
        firebaseRemoteConfig.setDefaults(R.xml.remote_config_defaults);


        long cachetimer = 1 * 60 * 1000;

        if (firebaseRemoteConfig.getInfo().getConfigSettings().isDeveloperModeEnabled()) {
            cachetimer = 0;
        }


        firebaseRemoteConfig.fetch(cachetimer)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            firebaseRemoteConfig.activateFetched();
                            String welcomeMessage = firebaseRemoteConfig.getString(WELCOME_MESSAGE_KEY);
                        } else {
                            Log.d(TAG, "onComplete: else ====");
                        }
                    }
                });


        String welcomeMessage = firebaseRemoteConfig.getString(WELCOME_MESSAGE_KEY);
        Log.d(TAG , "welcomeMessage"+welcomeMessage);
    }
}
