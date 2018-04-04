package com.baisi.spedometer.utiles;

import android.content.Context;
import android.content.Intent;

import com.baisi.spedometer.service.ServiceTimer;

/**
 * Created by hanwenmao on 2018/3/16.
 */

public class ServiceUtils {


    public static void startServiceTimer(Context context){

        Intent intent =new Intent(context , ServiceTimer.class);
        context.startService(intent);
    }
}
