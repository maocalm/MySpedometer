package com.baisi.spedometer.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.baisi.spedometer.R;
import com.baisi.spedometer.ads.FullAdCustom;
import com.baisi.spedometer.step.utils.SharedPreferencesUtils;
import com.baisi.spedometer.utiles.Firebase;
import com.baisi.spedometer.utiles.Timeutils;

public class StepGoalDialogActivity extends AppCompatActivity implements View.OnClickListener {
    private LinearLayout content;
    private ImageView iv_close;
    private TextView title;
    private Button button;
    private  String  className = getClass().getSimpleName();
    private  SharedPreferencesUtils sp ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_goal_dialog);
        initViewAndData();
        FullAdCustom.showNoonWindowFull(className);

        Firebase.getInstance(getApplicationContext()).logEvent("StepGoalDialogActivity" ,"进入" );
    }

    private  void  initViewAndData (){
        sp = new SharedPreferencesUtils(this);
        sp.setParam(SharedPreferencesUtils.NIGHT_DIALOG  , Timeutils.getTodayDate());
        iv_close = findViewById(R.id.iv_close);
        title = findViewById(R.id.title);
        content = findViewById(R.id.content);
        button = findViewById(R.id.button);
        button.setOnClickListener(this);
        iv_close.setOnClickListener(this);
        content.setOnClickListener(this);

        int stepCount  =  (int)sp.getParam(SharedPreferencesUtils.STEP_NUMBER ,2000);
        String  step = getResources().getString(R.string.stepgoal_dialog_content) ;
        String sss =  String.format(step ,String.valueOf(stepCount));
        int a  = "Walking".length();
        SpannableStringBuilder spannableStringBuilder =new SpannableStringBuilder(sss);
        spannableStringBuilder.setSpan(new ForegroundColorSpan(Color.RED) ,a+1 ,String.valueOf(stepCount).length()+a+1 , Spannable.SPAN_EXCLUSIVE_INCLUSIVE	);
        /*int  stepintstart = step.indexOf("2");
        StringBuilder stringBuilder = new StringBuilder(step);
        stringBuilder.replace(stepintstart ,stepintstart+4 ,String.valueOf(stepCount));
        */
        title.setText(spannableStringBuilder);

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_close:
                Firebase.getInstance(getApplicationContext()).logEvent("StepGoalDialogActivity弹框" ,"点击关闭" );
                finish();
                break;
            case R.id.content:

            case R.id.button:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                Firebase.getInstance(getApplicationContext()).logEvent("StepGoalDialogActivity弹框" ,"点击进入" );
                finish();
                break;
        }
    }

    public static void startMe(Context context) {
        Intent intent = new Intent(context, StepGoalDialogActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }
}
