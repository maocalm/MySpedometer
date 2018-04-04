package com.baisi.spedometer.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baisi.spedometer.R;
import com.baisi.spedometer.ads.FullAdCustom;
import com.baisi.spedometer.step.utils.SharedPreferencesUtils;
import com.baisi.spedometer.utiles.Firebase;
import com.baisi.spedometer.utiles.Timeutils;

// noon
public class ExerciseDialogActivity extends AppCompatActivity implements View.OnClickListener {
    private LinearLayout content;
    private ImageView iv_close;
    private TextView button;
    private  String  className = getClass().getSimpleName();
    private SharedPreferencesUtils sp ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_dialog);
        initView();
        FullAdCustom.showNightWindowFull(className);
        sp = new SharedPreferencesUtils(this) ;
        sp.setParam(SharedPreferencesUtils.NOON_DIALOG , Timeutils.getTodayDate());
        Firebase.getInstance(getApplicationContext()).logEvent("ExerciseDialogActivity", "进入");
    }

    private  void  initView (){
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
                Firebase.getInstance(getApplicationContext()).logEvent("ExerciseDialogActivity弹框", "点击关闭");
                finish();
                break;
            case R.id.button:
            case R.id.content:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                Firebase.getInstance(getApplicationContext()).logEvent("ExerciseDialogActivity弹框", "点击进入");
                finish();
                break;
        }
    }

    public static void startMe(Context context) {
        Intent intent = new Intent(context, ExerciseDialogActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }
}
