package com.baisi.spedometer.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.baisi.spedometer.R;
import com.baisi.spedometer.ads.FullAdCustom;
import com.baisi.spedometer.app.PedometerApplication;
import com.baisi.spedometer.step.utils.SharedPreferencesUtils;
import com.baisi.spedometer.utiles.Firebase;
import com.baisi.spedometer.utiles.SpUtils;
import com.baisi.spedometer.utiles.Timeutils;

public class MorningDialogActivity extends AppCompatActivity implements View.OnClickListener {
    private LinearLayout content;
    private ImageView iv_close;
    private String className = getClass().getSimpleName();
    private SharedPreferencesUtils sp ;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_morning_dialog);
        initView();
        FullAdCustom.showMorningWindowFull(className);
        sp = new SharedPreferencesUtils(this);
        sp.setParam(SharedPreferencesUtils.MORNING_DIALOG  , Timeutils.getTodayDate());
        Firebase.getInstance(getApplicationContext()).logEvent("MorningDialogActivity" ,"进入");
    }

    private void initView() {
        sp = new SharedPreferencesUtils(getApplicationContext());
        iv_close = findViewById(R.id.iv_close);
        content = findViewById(R.id.content);
        button = findViewById(R.id.button);

        button.setOnClickListener(this);
        iv_close.setOnClickListener(this);
        content.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_close:
                Firebase.getInstance(getApplicationContext()).logEvent("MorningDialogActivity弹框" ,"点击关闭");
                finish();
                break;
            case R.id.button:
            case R.id.content:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                Firebase.getInstance(getApplicationContext()).logEvent("MorningDialogActivity弹框" ,"点击进入");
                finish();
                break;
        }
    }

    public static void startMe(Context context) {
        Intent intent = new Intent(context, MorningDialogActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }
}
