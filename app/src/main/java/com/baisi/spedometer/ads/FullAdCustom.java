package com.baisi.spedometer.ads;

import com.bestgo.adsplugin.ads.AdAppHelper;
import static com.facebook.FacebookSdk.getApplicationContext;
/**
 * Created by hanwenmao on 2018/3/16.
 */

public class FullAdCustom {

    private static String  defaultValue =  "0" ;
    /*
    0 关闭 1 打开
     */
    private static void showFullOrNo(String show_default_full, String fromActivity) {
        if (show_default_full.equals("1")) {
            GoogleAd.LoadFullAd(fromActivity);
        } else if (show_default_full.equals("0")) {

        }
    }


    public static void showMorningWindowFull (String fromActivity) {
        String show_dialogFull = AdAppHelper.getInstance(getApplicationContext()).getCustomCtrlValue("show_morning_window_full" ,defaultValue);
        showFullOrNo(show_dialogFull , fromActivity);
    }

    public static void  showNoonWindowFull (String fromActivity){
        String show_dialogFull = AdAppHelper.getInstance(getApplicationContext()).getCustomCtrlValue("show_noon_window_full" ,defaultValue);
        showFullOrNo(show_dialogFull , fromActivity);
    }

    public static void showNightWindowFull(String fromActivity){
        String show_dialogFull = AdAppHelper.getInstance(getApplicationContext()).getCustomCtrlValue("show_night_window_full" ,defaultValue);
        showFullOrNo(show_dialogFull , fromActivity);
    }
}
