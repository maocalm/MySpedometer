package com.baisi.spedometer.service;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;

import com.baisi.spedometer.activity.MainActivity;

/**
 * Created by hanwenmao on 2018/1/18.
 */

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class MyJobService extends JobService {
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            //Toast.makeText(MyJobService.this, "MyJobService", Toast.LENGTH_SHORT).show();
            JobParameters param = (JobParameters) msg.obj;
            jobFinished(param, true);
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //startActivity(intent);
            return true;
        }
    });

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public boolean onStartJob(JobParameters params) {
        Message m = Message.obtain();
        m.obj = params;
        handler.sendMessage(m);
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        handler.removeCallbacksAndMessages(null);
        return false;
    }
}
