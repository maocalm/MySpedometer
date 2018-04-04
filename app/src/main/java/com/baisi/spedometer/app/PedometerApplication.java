package com.baisi.spedometer.app;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;

import com.baisi.spedometer.greendao.gen.DaoSession;
import com.baisi.spedometer.step.utils.MyDbUtils;
import com.baisi.spedometer.utiles.FacebookAnalytics;
import com.baisi.spedometer.utiles.Firebase;
import com.bestgo.adsplugin.ads.AdAppHelper;
import com.crashlytics.android.Crashlytics;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

import io.fabric.sdk.android.Fabric;

public class PedometerApplication extends Application {

    private static volatile PedometerApplication singleton;
    public static Context context;
    private static DaoSession daoSession;

    public PedometerApplication() {
    }

    public static PedometerApplication getInstance() {
        if (singleton == null) {
            synchronized (PedometerApplication.class) {
                if (singleton == null) {
                    singleton = new PedometerApplication();
                }
            }
        }
        return singleton;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        FacebookSdk.sdkInitialize(getApplicationContext());  //初始化facebook sdk
        AppEventsLogger.activateApp(this);   //开启facebook应用分析
        Fabric.with(this, new Crashlytics());
        AdAppHelper.FACEBOOK = FacebookAnalytics.getInstance(getApplicationContext());
        AdAppHelper.FIREBASE = Firebase.getInstance(this);
        AdAppHelper.getInstance(getApplicationContext()).init(null);

        new NullAsyTask().execute();
    }

    class NullAsyTask extends AsyncTask<Void ,Void , Void >{
        @Override
        protected Void doInBackground(Void... voids) {
            MyDbUtils myDbUtils = new MyDbUtils(getApplicationContext());
            daoSession = myDbUtils.getDaoManager().getDaoSession();
            return null;
        }
    }
    public static Context getApplicationContextPedometer() {
        return context;
    }

    public static  DaoSession getDaoSession() {
        return daoSession;
    }

}