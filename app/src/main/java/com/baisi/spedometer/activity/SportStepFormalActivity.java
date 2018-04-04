package com.baisi.spedometer.activity;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baisi.spedometer.R;
import com.baisi.spedometer.ads.GoogleAd;
import com.baisi.spedometer.evenbus.Event;
import com.baisi.spedometer.evenbus.EventBusUtil;
import com.baisi.spedometer.greendao.gen.DaoSession;
import com.baisi.spedometer.greendao.gen.SportRecordDataDao;
import com.baisi.spedometer.step.bean.SportRecordData;
import com.baisi.spedometer.step.utils.MyDbUtils;
import com.baisi.spedometer.step.utils.StepConversion;
import com.baisi.spedometer.utiles.DensityUtil;
import com.baisi.spedometer.view.CustomPopWindow;
import com.baisi.spedometer.view.DampingInterpolator;
import com.baisi.spedometer.view.MixLinearLayout;
import com.bestgo.adsplugin.ads.activity.ShowAdFilter;
import com.fashare.timer_view.DigitalTimerView;
import com.fashare.timer_view.TextViewUpdater;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.greenrobot.greendao.rx.RxDao;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class SportStepFormalActivity extends AppCompatActivity implements ShowAdFilter, View.OnClickListener {
    private String TAG = getClass().getSimpleName();
    private TextView km_value;
    private TextView km_unit_tv;
    private TextView time_count;
    private MixLinearLayout calorle_mix;
    private MixLinearLayout speed_mix;
    private MixLinearLayout step_mix;

    private ImageView pause_start_img;
    private ImageView stop_img;
    private DigitalTimerView digitalTimerView;
    private RelativeLayout relativeLayout;
    private CustomPopWindow popWindow;
    private View view;
    private DaoSession daoSession;

    private int stepsSport;
    private RxDao rxDao;
    private SportRecordData sportRecordData;
    private int stepNumber;
    private Disposable rxDisposable;
    private FrameLayout frameLayoutAd;
    private RelativeLayout relativeLayoutTop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sport_step_formal);
        initView();
        initBaseData();
        initEvenbus();
        RxUpdateText(); // 时刻更新速速
        Log.d(TAG, "onCreate: asdfaf");
        showAd();
    }
    private void showAd() {
        /*加载广告*/

        GoogleAd.loadAd(frameLayoutAd ,0 ,"SportStepFormActivity");
    }
    private void initView() {
        relativeLayoutTop = findViewById(R.id.backimage_rel);
        relativeLayoutTop.setOnClickListener(this);
        frameLayoutAd = findViewById(R.id.ad_frame);
        relativeLayout = findViewById(R.id.animator_rel);
        km_value = findViewById(R.id.km_value);
        km_unit_tv = findViewById(R.id.km_unit_tv);
        //time_count = findViewById(R.id.time_count);
        calorle_mix = findViewById(R.id.calorle_tv);
        speed_mix = findViewById(R.id.speed_tv);
        speed_mix.getTextUnitView().setText(getString(R.string.time_minute) + "/" + getString(R.string.unit_km));
        step_mix = findViewById(R.id.step_tv);
        pause_start_img = findViewById(R.id.pause);
        stop_img = findViewById(R.id.stop);
        pause_start_img.setOnClickListener(this);
        digitalTimerView = findViewById(R.id.dtv_simple);
        view = LayoutInflater.from(SportStepFormalActivity.this).inflate(R.layout.pop_sport_stop, null); //  停止按钮点击弹框
        TextView cancle = view.findViewById(R.id.cancle);
        TextView ok = view.findViewById(R.id.ok);
        ok.setOnClickListener(this);
        cancle.setOnClickListener(this);
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        digitalTimerView.setSubTimeView(R.layout.item_clock, lp)
                .setSuffixView(R.layout.item_suffix, lp)
                .setViewUpdater(new TextViewUpdater(R.id.tv_time, R.id.tv_suffix));
        //开始计时
        digitalTimerView.start(0);
        EventBusUtil.sendEvent(new Event(EventBusUtil.EventCode.SPORTMODESTART));
    }

    private void initBaseData() {
        MyDbUtils dbUtils = new MyDbUtils(this);
        daoSession = dbUtils.getDaoManager().getDaoSession();
    }

    private void initEvenbus() {
        EventBusUtil.register(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public boolean allowShowAd() {
        return false;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.pause) {
            if (!pause_start_img.isSelected()) { //  pause
                digitalTimerView.pause();
                separateAnimatorSet();
                EventBusUtil.sendEvent(new Event(EventBusUtil.EventCode.SPORTMODEPAUSE));
            } else { //start
                digitalTimerView.pauseToStart(); // PAUSE TO START 计时；
                togetherAnimatorSet();
                EventBusUtil.sendStickyEvent(new Event(EventBusUtil.EventCode.SPORTMODEPAUSETOSTART));
            }

        } else if (id == R.id.stop) {
            digitalTimerView.pause();
            EventBusUtil.sendStickyEvent(new Event(EventBusUtil.EventCode.SPORTMODESTOP));
            showPopWindows(view);
        } else if (id == R.id.ok) {
            popWindow.dissmiss();
            saveSprotDataToDao();
            Intent intent = new Intent(SportStepFormalActivity.this, SportHistoryActivity.class);
            startActivityForResult(intent, 0);
            digitalTimerView.stop();
        } else if (id == R.id.cancle) {
            popWindow.dissmiss();

            //todo  王数据库插入数据；
        } else  if (id==R.id.backimage_rel){
            moveTaskToBack(true);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d(TAG, "onActivityResult: sportStepformal");
        if (resultCode == 0) {
            Log.d(TAG, "onActivityResult: sportStepformal" + "ok");
            finish();
        }
    }

    private void saveSprotDataToDao() {
        Float miles = StepConversion.getDistanceFloat( stepsSport);
        Date date = new Date();
        String simpleDate = StepConversion.getSimpleDate(date);
        Float distance = StepConversion.getDistanceFloat( stepsSport);
        String distanceString = String.valueOf(distance);
        Float distanceFloat = Float.valueOf(distanceString);


        Long need = digitalTimerView.getCurrentTime() / 1000;
        String needF;
        Float unit;
        if (need != 0) {
            needF = String.valueOf(need);
            unit = (distanceFloat / Float.valueOf(needF));
        } else {
            unit = 0f;
        }
        Float speedF =0f;

        if (unit == 0) {
            speedF = 0f;
        } else {
            speedF = 1f / unit;
        }

        String speedString = String.valueOf(Math.round(speedF));

        sportRecordData = null;
        sportRecordData = new SportRecordData();
        sportRecordData.setMiles(miles);
        sportRecordData.setMilesNeedTime(speedString);
        sportRecordData.setSimpleDate(simpleDate);
        sportRecordData.setSprotDate(date);
        sportRecordData.setSportLong(digitalTimerView.getCurrentTime()/60);
        sportRecordData.setSteps(stepsSport);
        final SportRecordDataDao sportRecordDataDao = daoSession.getSportRecordDataDao(); // 子线程获取不到 ；
        //daoSession.getSportRecordDataDao().insert(sportRecordData);
        Observable.create(new ObservableOnSubscribe<SportRecordData>() {
            @Override
            public void subscribe(ObservableEmitter<SportRecordData> e) throws Exception {
                sportRecordDataDao.insertOrReplace(sportRecordData);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<SportRecordData>() {
                    @Override
                    public void accept(SportRecordData sportRecordData) throws Exception {
                        // 主线程更新；
                    }
                });
    }

    private void separateAnimatorSet() {
        pause_start_img.setImageResource(R.mipmap.pause);
        pause_start_img.setSelected(true);
        stop_img.setVisibility(View.VISIBLE);
        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator transXLeft = ObjectAnimator.ofFloat(pause_start_img, "translationX", 0, 0 - DensityUtil.dip2px(60));
        ObjectAnimator transXRight = ObjectAnimator.ofFloat(stop_img, "translationX", 0, 0 + DensityUtil.dip2px(60));
        transXLeft.setInterpolator(new DampingInterpolator());
        transXRight.setInterpolator(new DampingInterpolator());
        transXLeft.setDuration(700);
        transXRight.setDuration(700);
        animatorSet.playTogether(transXLeft, transXRight);
        animatorSet.start();
        pause_start_img.setOnClickListener(null);
        stop_img.setOnClickListener(null);
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                pause_start_img.setOnClickListener(SportStepFormalActivity.this);
                stop_img.setOnClickListener(SportStepFormalActivity.this);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

    }

    private void togetherAnimatorSet() {
        pause_start_img.setImageResource(R.mipmap.running);
        pause_start_img.setSelected(false);

        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator transXLeft = ObjectAnimator.ofFloat(pause_start_img, "translationX", 0 - DensityUtil.dip2px(50), 0);
        ObjectAnimator transXRight = ObjectAnimator.ofFloat(stop_img, "translationX", 0 + DensityUtil.dip2px(50), 0 + DensityUtil.dip2px(7));
        transXLeft.setInterpolator(new DampingInterpolator(2, 0.2f));
        transXLeft.setDuration(700);
        transXRight.setDuration(200);
        animatorSet.playTogether(transXLeft, transXRight);
        animatorSet.start();
        pause_start_img.setOnClickListener(null);
        stop_img.setOnClickListener(null);

        transXRight.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                stop_img.setVisibility(View.INVISIBLE);
                Log.d(TAG, "onAnimationEnd:   right asdfadfaf");

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                Log.d(TAG, "onAnimationStart: " + relativeLayout.getWidth());

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                Log.d(TAG, "onAnimationEnd: " + relativeLayout.getWidth());
                Log.d(TAG, "onAnimationEnd:   pingmukuan" + DensityUtil.getScreenWidth(SportStepFormalActivity.this));
                pause_start_img.setOnClickListener(SportStepFormalActivity.this);
                stop_img.setOnClickListener(SportStepFormalActivity.this);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventReceive(Event event) {
        if (event.getCode() == EventBusUtil.EventCode.SPORTMODE_SERVICE_TO_SPORTACTIVITY) {
            stepNumber = (int) event.getData();
            stepsSport = stepNumber;
            String step = String.valueOf(stepNumber);
            String distance = StepConversion.getDistance(stepNumber);
            Float distanceFloat = Float.valueOf(distance);
            Long need = digitalTimerView.getCurrentTime() / 1000;
            String needF;
            Float unit;
            if (need != 0) {
                needF = String.valueOf(need);
                unit = (distanceFloat / Float.valueOf(needF));
            } else {
                unit = 0f;
            }
            Float speedF =0f;

            if (unit == 0) {
                speedF = null;
            } else {
                speedF = 1f / unit;
            }
            step_mix.getTextValueView().setText(step);
            calorle_mix.getTextValueView().setText(StepConversion.getCalorie( stepNumber));
            km_value.setText(distance);
        }
    }

    private void RxUpdateText() {
        rxDisposable = Observable.interval(2, 1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        Float distance = StepConversion.getDistanceFloat( stepsSport);
                        String distanceString = String.valueOf(distance);
                        Float distanceFloat = Float.valueOf(distanceString);
                        Long need = digitalTimerView.getCurrentTime() / 1000;
                        String needF;
                        Float unit;
                        if (need != 0) {
                            needF = String.valueOf(need);
                            unit = (distanceFloat / Float.valueOf(needF));
                        } else {
                            unit = 0f;
                        }
                        Float speedF =0f;

                        if (unit == 0) {
                            speedF = 0f;
                        } else {
                            speedF = 1f / unit;
                        }

                        String speedString = String.valueOf(Math.round(speedF/60));
                        speed_mix.getTextValueView().setText(speedString);
                    }
                });
    }

    private void showPopWindows(View view) {
        int a = (int) 0.5f;
        Log.d(TAG, "showPopWindows: asdafdafaasafaf" + a);
        popWindow = new CustomPopWindow.PopupWindowBuilder(this)
                .setView(view)//显示的布局，还可以通过设置一个View
                .enableBackgroundDark(true)
                .size(0, 0)
                .setBgDarkAlpha(0.5f)
                .setFocusable(true)//是否获取焦点，默认为ture
                .setOutsideTouchable(true)//是否PopupWindow 以外触摸dissmiss
                .create();
        popWindow.showAtLocation(view, Gravity.CENTER, 0, 0);//显示PopupWindow
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        moveTaskToBack(true);
        //Intent intent = new Intent(Intent.ACTION_MAIN);
        //ntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //intent.addCategory(Intent.CATEGORY_HOME);
        //startActivity(intent);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        rxDisposable.dispose();
        Log.d(TAG, "onDestroy: ");
        EventBusUtil.unregister(this);
    }


}
