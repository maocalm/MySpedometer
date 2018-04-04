package com.baisi.spedometer.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.baisi.spedometer.R;
import com.baisi.spedometer.adapter.ViewPagerAdapter;
import com.baisi.spedometer.ads.GoogleAd;
import com.baisi.spedometer.greendao.gen.StepDataDao;
import com.baisi.spedometer.step.bean.StepData;
import com.baisi.spedometer.step.utils.MyDbUtils;
import com.baisi.spedometer.step.utils.StepConversion;
import com.baisi.spedometer.utiles.Firebase;
import com.baisi.spedometer.view.chatview.SingViewToday;
import com.bestgo.adsplugin.ads.AdAppHelper;

import java.math.BigDecimal;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static com.facebook.FacebookSdk.getApplicationContext;

public class TodayReportActivity extends AppCompatActivity implements View.OnClickListener {

    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;
    private SingViewToday todayChartView;
    private TextView totalSteps;
    private TextView date;
    private TextView step_tv;
    private TextView calorle_tv;
    private TextView time_tv;
    private TextView distance_tv;
    private static final String STEP_STR = "step", CALORIE_STR = "calorie", TIME_STR = "time", DISTANCE_STR = "distance";
    private String TAG = getClass().getSimpleName();
    //private static final int STEP_NUMBER = 0, CALORIE_NUMBER = 1, TIME_NUMBER = 2, DISTANCE_NUMBER = 3;
    private ArrayList<TextView> textViewsList = new ArrayList<>();
    private MyDbUtils myDbUtils;
    private FrameLayout adFrameLayout;
    private FrameLayout.LayoutParams params;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todayreport);
        initView();
        initData();
        Firebase.getInstance(getApplicationContext()).logEvent("TodayReportActivity", "进入");

    }

    private void initView() {
        todayChartView = findViewById(R.id.todaychartview);
        totalSteps = findViewById(R.id.total_steps);
        date = findViewById(R.id.date);
        step_tv = findViewById(R.id.step_tv);
        calorle_tv = findViewById(R.id.calorle_tv);
        time_tv = findViewById(R.id.time_tv);
        distance_tv = findViewById(R.id.distance_tv);
        totalSteps = findViewById(R.id.total_steps);
        adFrameLayout = findViewById(R.id.ad_frame);

        step_tv.setOnClickListener(this);
        calorle_tv.setOnClickListener(this);
        time_tv.setOnClickListener(this);
        distance_tv.setOnClickListener(this);

        textViewsList.add(step_tv);
        textViewsList.add(calorle_tv);
        textViewsList.add(time_tv);
        textViewsList.add(distance_tv);


        date.setText(StepConversion.getTodaySimple2Date(new Date()));
    }

    private void initData() {
        myDbUtils = new MyDbUtils(this);
        //setTodayChartView(STEP_STR);
        performClickReportTv(false, STEP_STR);
    }


    private void setTodayChartView(final String type) {
        Observable.create(new ObservableOnSubscribe<List<Float>>() {

            @Override
            public void subscribe(ObservableEmitter<List<Float>> e) throws Exception {

                e.onNext(getStepListOrStepsecondList(type));
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<List<Float>>() {
            @Override
            public void accept(List<Float> floats) throws Exception {
                if (type.equals(STEP_STR)) {
                    setChartList(floats, type);
                    setTopTotalAndAverage(type ,floats);
                } else if (type.equals(CALORIE_STR)) {
                    ArrayList calorlelist = StepConversion.getCalorieList(floats);
                    setChartList(calorlelist, type);
                    setTopTotalAndAverage(type ,calorlelist);
                }else if (type.equals(DISTANCE_STR)){
                    ArrayList distanceList = StepConversion.getDistanceList(floats);
                    setChartList(distanceList, type);
                    setTopTotalAndAverage(type ,distanceList);
                }else if (type.equals(TIME_STR)){
                    setChartList(floats, type);
                    setTopTotalAndAverage(type ,floats);
                }
            }
        });
    }

    /**
     * @param classification 0  代表是stepalist  1 代表是stepsecondList ;
     * @return
     */
    private List<Float> getStepListOrStepsecondList(String classification) {

        List<StepData> stepTodayAllDataList = myDbUtils.getDaoManager().getDaoSession().getStepDataDao()
                .queryBuilder()
                .where(StepDataDao.Properties.StepToday.eq(StepConversion.getTodaySimpleDate()), StepDataDao.Properties.Reset.eq("false"))
                .list();
        Log.d(TAG, "getStepListOrStepsecondList: "+stepTodayAllDataList.toString());
        if (classification.equals(STEP_STR) || classification.equals(CALORIE_STR) || classification.equals(DISTANCE_STR)) {

            List<Float> stepList = new ArrayList();

            ArrayList arrayListEmpty = new ArrayList();
            for (int i = 0; i < 24; i++) {
                arrayListEmpty.add(0f);
            }
            stepList.addAll(arrayListEmpty);

            if (stepTodayAllDataList != null) {
                for (int i = 0; i < stepTodayAllDataList.size(); i++) {
                    int timehour = StepConversion.getStepHour(stepTodayAllDataList.get(i).getStepTime());
                    Log.d(TAG, "index  a " + timehour);
                    stepList.add(timehour, Float.valueOf(stepTodayAllDataList.get(i).getStep()));
                }
            }

            Log.d(TAG, "getStepListOrStepsecondList:    huoqude  steplist  " +stepList);
            return stepList;

        } else {

            List<Float> stepSecondList = new ArrayList();
            ArrayList arrayListEmpty = new ArrayList();
            for (int i = 0; i < 24; i++) {
                arrayListEmpty.add(0f);
            }
            stepSecondList.addAll(arrayListEmpty);

            if (stepTodayAllDataList != null) {
                for (int i = 0; i < stepTodayAllDataList.size(); i++) {
                    int timehour = StepConversion.getStepHour(stepTodayAllDataList.get(i).getStepTime());
                    Log.d(TAG, "index  a " + timehour);
                    BigDecimal b = new BigDecimal(stepTodayAllDataList.get(i).getStepSecond()/60f);
                    Float f1 = b.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
                    stepSecondList.add(timehour, Float.valueOf(f1));
                }
            }

            return stepSecondList;
        }


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.step_tv:
                performClickReportTv(false, STEP_STR);
                Firebase.getInstance(getApplicationContext()).logEvent("TodayReportActivity", "step_tv 点击");
                break;
            case R.id.calorle_tv:
                performClickReportTv(false, CALORIE_STR);
                Firebase.getInstance(getApplicationContext()).logEvent("TodayReportActivity", "calorle_tv 点击");
                break;
            case R.id.time_tv:
                performClickReportTv(false, TIME_STR);
                Firebase.getInstance(getApplicationContext()).logEvent("TodayReportActivity", "time_tv 点击");

                break;
            case R.id.distance_tv:
                performClickReportTv(false, DISTANCE_STR);
                Firebase.getInstance(getApplicationContext()).logEvent("TodayReportActivity", "distance_tv 点击");

                break;
        }
    }

    public void performClickReportTv(boolean isFromPedometer, String classification) {
        if (this == null) {
            return;
        }
        switch (classification) {
            case STEP_STR:
                todayChartView.setChartPaintColor(getResources().getColor(R.color.stepcolor));
                setTodayChartView(STEP_STR);
                break;
            case CALORIE_STR:
                todayChartView.setChartPaintColor(getResources().getColor(R.color.caloriecolor));
                setTodayChartView(CALORIE_STR);
                break;
            case TIME_STR:
                todayChartView.setChartPaintColor(getResources().getColor(R.color.timecolor));
                setTodayChartView(TIME_STR);
                break;
            case DISTANCE_STR:
                todayChartView.setChartPaintColor(getResources().getColor(R.color.distancecolor));
               setTodayChartView(DISTANCE_STR);
                break;
        }
        setSeclectTextView(classification, isFromPedometer);

    }

    private void getTodayStepAll() {

    }

    private void setChartList(List list, String classifcation) {

        if (list != null) {
            todayChartView.setList(list, classifcation);
        } else {
            List list1 = new ArrayList();
            todayChartView.setList(list1, classifcation);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        GoogleAd.loadAd(adFrameLayout ,0 ,"TodayReportActivity");
    }

    /**
     * 设置toal 数据；
     *
     * @param classification
     */
    private void setTopTotalAndAverage(String classification ,List<Float> floatList) {
        String suffixString = null;
        String averageString = null;
        switch (classification) {
            case STEP_STR:
                float totalStep = StepConversion.getTotalFloatSumList(floatList);
                suffixString = String.format("%.0f", totalStep) + " Steps";
                averageString = String.format("%.0f", totalStep / 7);
                break;
            case CALORIE_STR:
                float totalCalor = StepConversion.getTotalFloatSumList(floatList);
                suffixString = String.format("%.2f", totalCalor) + " Kcal";
                averageString = String.format("%.2f", totalCalor / 7);
                break;
            case DISTANCE_STR:
                float totalDistance = StepConversion.getTotalFloatSumList(floatList);
                suffixString = String.format("%.2f", totalDistance) + " Km";
                averageString = String.format("%.2f", totalDistance / 7);
                break;
            case TIME_STR:
                float totalTime = StepConversion.getTotalFloatSumList(floatList);
                suffixString = String.format("%.2f", totalTime ) + " m";
                averageString = String.format("%.2f", totalTime / (7));
                break;
        }
        totalSteps.setText(suffixString);
        //average_steps.setText(getString(R.string.daily_average) + averageString);

    }

    /**
     * textview 点击显示状态改变
     *
     * @param classification
     */
    private void setSeclectTextView(String classification, boolean isFromPedometer) {
        int classificationNumber = 0;
        if (classification.equals(STEP_STR)) {
            classificationNumber = 0;
        } else if (classification.equals(CALORIE_STR)) {
            classificationNumber = 1;
        } else if (classification.equals(TIME_STR)) {
            classificationNumber = 2;
        } else if (classification.equals(DISTANCE_STR)) {
            classificationNumber = 3;
        }
        for (int i = 0; i < textViewsList.size(); i++) {
            if (classificationNumber == i) {
                if (isFromPedometer) {
                    textViewsList.get(i).setSelected(true);
                } else {
                    //textViewsList.get(i).setSelected(!textViewsList.get(i).isSelected());
                    textViewsList.get(i).setSelected(true);
                }
            } else {
                textViewsList.get(i).setSelected(false);

            }
        }
    }
}
