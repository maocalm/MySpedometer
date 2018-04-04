package com.baisi.spedometer.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.baisi.spedometer.activity.ExerciseDialogActivity;
import com.baisi.spedometer.activity.MorningDialogActivity;
import com.baisi.spedometer.activity.StepGoalDialogActivity;
import com.baisi.spedometer.fcm.CommonService;
import com.baisi.spedometer.step.utils.SharedPreferencesUtils;
import com.baisi.spedometer.utiles.Timeutils;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by hanwenmao on 2018/3/16.
 */

public class ServiceTimer extends Service {

    private  Disposable rxDisposableTimeCount ;
    private int  duration = 10*60*1000 ;
    private SharedPreferencesUtils sp;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sp = new SharedPreferencesUtils(this);
        startTimer();
    }

    public void startTimer() {

        rxDisposableTimeCount = Observable.interval(0, duration, TimeUnit.MILLISECONDS).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        if (Timeutils.isInTime10To11(System.currentTimeMillis()) && !sp.getParam(SharedPreferencesUtils.MORNING_DIALOG ,"hh").equals(Timeutils.getTodayDate())) {
                            MorningDialogActivity.startMe(ServiceTimer.this);
                        } else if (Timeutils.isInTime18To19(System.currentTimeMillis())&& !sp.getParam(SharedPreferencesUtils.NOON_DIALOG ,"hh").equals(Timeutils.getTodayDate())) {
                            ExerciseDialogActivity.startMe(ServiceTimer.this);
                        } else if (Timeutils.isInTime22To23(System.currentTimeMillis()) && !sp.getParam(SharedPreferencesUtils.NIGHT_DIALOG ,"hh").equals(Timeutils.getTodayDate())) {
                            StepGoalDialogActivity.startMe(ServiceTimer.this);
                        }
                    }
                });
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (rxDisposableTimeCount!=null){
            if (!rxDisposableTimeCount.isDisposed()){
                rxDisposableTimeCount.dispose();
            }
        }
    }


}
