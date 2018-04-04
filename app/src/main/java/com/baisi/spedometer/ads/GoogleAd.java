package com.baisi.spedometer.ads;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.baisi.spedometer.app.PedometerApplication;
import com.baisi.spedometer.utiles.Firebase;
import com.bestgo.adsplugin.ads.AdAppHelper;
import com.bestgo.adsplugin.ads.AdNetwork;


import static com.facebook.FacebookSdk.getApplicationContext;


/**
 * Created by liuan on 2017/5/11.
 */

public class GoogleAd {
    public static final int DEFAULT_180 = 0;
    public static final int LOCK_NOMAR = 1;
    public static final int LOCK_PASSWORD = 2;
    public static final int SCAN_RESULT = 3;
    public static final int SMALL_NATIVE = 4;

    public static void loadAd(final FrameLayout frameLayout, int id, String name) {
        loadAd(frameLayout, name, id);
    }

    public static void loadAd(final FrameLayout frameLayout, String Name, int id) {

        if (TextUtils.isEmpty(Name)) {
            Name = PedometerApplication.getInstance().getClass().getSimpleName();
        }
        //此句应该也放在onCreate中加载
//        if (!AdAppHelper.getInstance(AppContext).isNativeLoaded(GoogleAd.DEFAULT_180)) {
//            AdAppHelper.getInstance(AppContext).loadNewNative(GoogleAd.DEFAULT_180);
//        }
        if (!AdAppHelper.getInstance(PedometerApplication.getInstance()).isNativeLoaded(id)) {
            AdAppHelper.getInstance(PedometerApplication.getInstance()).loadNewNative(id);
        }


        ObjectAnimator anim = ObjectAnimator.ofFloat(frameLayout, "translationX", -20, 20, -10, 10, -5, 5, 0);
        anim.setDuration(1000);
        anim.start();
        if (!Constant.isAdShow) {
            return;
        }

//        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGro
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER);
        if (AdAppHelper.getInstance(PedometerApplication.getInstance()).isNativeLoaded(id)) {
            Firebase.getInstance(PedometerApplication.getInstance()).logEvent("广告加载成功", Name);
        } else {
            Firebase.getInstance(PedometerApplication.getInstance()).logEvent("广告加载失败", Name);
        }
        AdAppHelper.getInstance(PedometerApplication.getInstance()).getNative(id, frameLayout, params, Name);
        Firebase.getInstance(PedometerApplication.getInstance()).logEvent("广告展示", Name);


    }

    public static void loadAd_no_anim(final FrameLayout frameLayout, String Name, int id) {
        //此句应该也放在onCreate中加载
//       AdAppHelper.getInstance(AppContext).loadNewNative(AdInnerType.DEFAULT);
        if (!Constant.isAdShow) {
            return;
        }
        AdAppHelper.getInstance(PedometerApplication.getInstance()).loadNewNative(id);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER);
        if (AdAppHelper.getInstance(PedometerApplication.getInstance()).isNativeLoaded(id)) {
            Firebase.getInstance(PedometerApplication.getInstance()).logEvent("广告加载成功", Name);
        } else {
            Firebase.getInstance(PedometerApplication.getInstance()).logEvent("广告加载失败", Name);
        }
        AdAppHelper.getInstance(PedometerApplication.getInstance()).getNative(id, frameLayout, params, Name);
        Firebase.getInstance(PedometerApplication.getInstance()).logEvent("广告展示", Name);


    }


    public static boolean isNetworkAvailable(Activity activity) {
        Context context = activity.getApplicationContext();
        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager == null) {
            return false;
        } else {
            // 获取NetworkInfo对象
            NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();

            if (networkInfo != null && networkInfo.length > 0) {
                for (int i = 0; i < networkInfo.length; i++) {
                    System.out.println(i + "===状态===" + networkInfo[i].getState());
                    System.out.println(i + "===类型===" + networkInfo[i].getTypeName());
                    // 判断当前网络状态是否为连接状态
                    if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static void LoadFullAd(String fromActivity) {

        //          AdAppHelper.getInstance(AppContext).loadNewInterstitial(0);

        if (!Constant.isAdShow) {
            return;
        }
        if (!AdAppHelper.getInstance(getApplicationContext()).isFullAdLoaded()) {
            Firebase.getInstance(PedometerApplication.getInstance()).logEvent("全屏广告没准备好", fromActivity);
            AdAppHelper.getInstance(PedometerApplication.getInstance()).loadNewInterstitial();
        } else {
            Firebase.getInstance(PedometerApplication.getInstance()).logEvent("全屏广告准备好", fromActivity);

        }
        AdAppHelper.getInstance(PedometerApplication.getInstance()).showFullAd(fromActivity);
    }


    public static void loadBannerAd(String type, FrameLayout frameLayout) {

        //请求新的banner
        AdAppHelper.getInstance(getApplicationContext()).loadNewBanner();
        //banner和native返回的是一个view，需要加载自己需要的位置上
//获取banner
        View banner = AdAppHelper.getInstance(getApplicationContext()).getBanner();

        frameLayout.removeAllViews();
//        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) frameLayout.getLayoutParams();
//        layoutParams.gravity = Gravity.CENTER;
//        frameLayout.setLayoutParams(layoutParams);
        frameLayout.addView(banner);


    }


    public static void LoadFullAdAdmob(String fromActivity, int full_ad_count) {

        //          AdAppHelper.getInstance(AppContext).loadNewInterstitial(0);

        if (!Constant.isAdShow) {
            return;
        }
        if (!AdAppHelper.getInstance(getApplicationContext()).isFullAdLoaded(AdNetwork.Admob)) {
            Firebase.getInstance(PedometerApplication.getInstance()).logEvent("全屏广告没准备好", fromActivity);
            Firebase.getInstance(PedometerApplication.getInstance()).logEvent("Admob全屏广告没准备好", fromActivity);
            AdAppHelper.getInstance(PedometerApplication.getInstance()).loadNewInterstitial();
        } else {
            Firebase.getInstance(PedometerApplication.getInstance()).logEvent("全屏广告准备好", fromActivity);
            Firebase.getInstance(PedometerApplication.getInstance()).logEvent("Admob全屏广告准备好", fromActivity);

        }
        AdAppHelper.getInstance(PedometerApplication.getInstance()).showFullAd(AdNetwork.Admob, fromActivity);
    }


}


