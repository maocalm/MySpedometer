package com.baisi.spedometer.fragment;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.baisi.spedometer.R;
import com.baisi.spedometer.activity.WeekReportActivity;
import com.baisi.spedometer.app.PedometerApplication;
import com.baisi.spedometer.base.BaseFragment;
import com.baisi.spedometer.evenbus.Event;
import com.baisi.spedometer.evenbus.EventBusUtil;
import com.baisi.spedometer.fragment.fragmentlistenner.PedometerFragmentListener;
import com.baisi.spedometer.greendao.gen.DaoSession;
import com.baisi.spedometer.greendao.gen.StepDataDao;
import com.baisi.spedometer.step.UpdateUiCallBack;
import com.baisi.spedometer.step.bean.StepData;
import com.baisi.spedometer.step.service.StepService;
import com.baisi.spedometer.step.utils.MyDbUtils;
import com.baisi.spedometer.step.utils.SharedPreferencesUtils;
import com.baisi.spedometer.step.utils.StepConversion;
import com.baisi.spedometer.utiles.DensityUtil;
import com.baisi.spedometer.utiles.Firebase;
import com.baisi.spedometer.view.ProgressRing;
import com.baisi.spedometer.view.TextViewDrawable;
import com.baisi.spedometer.view.chatview.CircleScaleView;
import com.baisi.spedometer.view.floataction.FloatingActionButton;
import com.baisi.spedometer.view.floataction.FloatingActionMenu;
import com.bestgo.adsplugin.ads.activity.ShowAdFilter;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.fitness.FitnessOptions;
import com.google.android.gms.fitness.data.DataType;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by MnyZhao on 2017/11/14.
 */

public class PedometerFragment extends BaseFragment implements View.OnClickListener, ShowAdFilter {

    private View view;
    private boolean isBind = false;
    private TextView step_in_progress;
    private ProgressRing progressRing;
    private TextView setp_goal;
    private TextViewDrawable pause_resume;
    private SharedPreferencesUtils mSpUtils;
    private String mStepGoal;  //目标步数
    private int stepCount; // 当天步数；
    private TextView pause_in_progress;
    private StepService stepService;

    private String TAG = "pedometer";

    /*体重*/
    private Float weght;
    private TextView kcal_tv;
    private TextView km_tv;
    private TextView time_tv;
    private String gender;
    private int height;
    private FloatingActionMenu menuDown;
    private MyDbUtils myDbUtils;
    private FloatingActionButton timeline_float;
    private FloatingActionButton reset_float;
    private DaoSession daoSession;
    private CircleScaleView circleScaleView;

    @Override
    protected View initViews(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.pedometer_fragment, null);
        Log.d(TAG, "  screen____ height_dip_" + DensityUtil.getDmDensityDpi());

        //if (!judGmentGoogleFit()) {
         /*开启计步服务*/
        startStepService();
        //}
        initViewAndBaseData();
        initProgress();
        initKmKcTime();
        Log.d(TAG, "initView >>>>>");
        return view;
    }


    private void initViewAndBaseData() {
        step_in_progress = (TextView) view.findViewById(R.id.steps_in_progress);
        //progressRing = (ProgressRing) view.findViewById(R.id.ProgressRing);
        pause_resume = (TextViewDrawable) view.findViewById(R.id.pause_resume);
        pause_resume.setSelected(true);
        pause_in_progress = (TextView) view.findViewById(R.id.pause_in_progress);
        setp_goal = (TextView) view.findViewById(R.id.setp_goal_in_progress);
        kcal_tv = (TextView) view.findViewById(R.id.kcal);
        km_tv = (TextView) view.findViewById(R.id.km);
        time_tv = (TextView) view.findViewById(R.id.time);
        circleScaleView = view.findViewById(R.id.circleScaleView);
        kcal_tv.setOnClickListener(this);
        km_tv.setOnClickListener(this);
        time_tv.setOnClickListener(this);
        pause_resume.setOnClickListener(this);

        mSpUtils = new SharedPreferencesUtils(PedometerApplication.getApplicationContextPedometer());
        mStepGoal = mSpUtils.getParam(SharedPreferencesUtils.STEP_GOAL, SharedPreferencesUtils.STEPGOAL_DEFAULT).toString();
        weght = (Float) mSpUtils.getParam(SharedPreferencesUtils.WEIGHT, SharedPreferencesUtils.WEIGHT_DEFAULT);
        gender = (String) mSpUtils.getParam(SharedPreferencesUtils.GENDER, SharedPreferencesUtils.GENDER_DEFAULT);
        height = (int) mSpUtils.getParam(SharedPreferencesUtils.HEIGHT, SharedPreferencesUtils.Height_DEFAULT);

        myDbUtils = new MyDbUtils(getActivity());
        daoSession = myDbUtils.getDaoManager().getDaoSession();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        //super.setUserVisibleHint(isVisibleToUser);
        if (view != null && isVisibleToUser == true) {
            Log.d(TAG, "onvisible ");
            mStepGoal = (String) mSpUtils.getParam(SharedPreferencesUtils.STEP_GOAL, SharedPreferencesUtils.STEPGOAL_DEFAULT);
            //setp_goal.setText(String.format(getString(R.string.goal_with_value), mStepGoal));
            initProgress();
            //circleScaleView.setStepNum(Integer.valueOf(mStepGoal), stepCount);
        }
    }

    private boolean judGmentGoogleFit() {

        final FitnessOptions fitnessOptions =
                FitnessOptions.builder()
                        .addDataType(DataType.TYPE_STEP_COUNT_CUMULATIVE)
                        .addDataType(DataType.TYPE_STEP_COUNT_DELTA)
                        .addDataType(DataType.TYPE_CALORIES_EXPENDED)
                        .addDataType(DataType.TYPE_DISTANCE_CUMULATIVE)
                        .addDataType(DataType.TYPE_DISTANCE_DELTA)
                        .addDataType(DataType.TYPE_ACTIVITY_SEGMENT)

                        .build();
        if (GoogleSignIn.hasPermissions(GoogleSignIn.getLastSignedInAccount(getActivity()), fitnessOptions)) {
            //readDat
            return true;
        } else {
            return false;
        }

    }

    @Override
    protected void initData() {

    }

    private void initKmKcTime() {
        /*根据步数计算distance  kcal */
        String kcal = StepConversion.getCalorie(stepCount);
        kcal_tv.setText(String.valueOf(kcal));
        String distance = StepConversion.getDistance(gender, stepCount, height);
        km_tv.setText(distance);
        /*时长-- 分钟*/
       int stepTimeCountSecond = (int) mSpUtils.getParam(SharedPreferencesUtils.STEP_SECOND ,0);
        time_tv.setText(String.valueOf(stepTimeCountSecond / 60));
    }

    private void initProgress() {
        /*第一次进去默认开始*/
        stepCount =  (int)mSpUtils.getParam(SharedPreferencesUtils.STEP_NUMBER , 0);
        circleScaleView.setStepNum(Integer.valueOf(mStepGoal), stepCount);
    }

    private int getStepSecond() {
        int stepSecond = 0;
        List<StepData> stepDataList = myDbUtils.getDaoManager()
                .getDaoSession().getStepDataDao().queryBuilder()
                .where(StepDataDao.Properties.StepToday.eq(getTodaySimpleDate()), StepDataDao.Properties.Reset.eq("false"))
                .list();
        if (stepDataList.size() == 0 || stepDataList.isEmpty()) {
            stepSecond = 0;
        } else if (stepDataList.size() == 1) {
            Log.v(TAG, "StepData=" + stepDataList.get(0).toString());
            stepSecond = stepDataList.get(0).getStepSecond();
        }
        return stepSecond;
    }

    public int getStepCount() {
        Observable.create(new ObservableOnSubscribe<List<StepData>>() {
            @Override
            public void subscribe(ObservableEmitter<List<StepData>> e) throws Exception {
                List<StepData> stepDataList = daoSession.getStepDataDao().
                        queryBuilder().where(StepDataDao.Properties.StepToday.eq(getTodaySimpleDate()), StepDataDao.Properties.Reset.eq("false")).list();
                e.onNext(stepDataList);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<StepData>>() {
                    @Override
                    public void accept(List<StepData> stepDataList) throws Exception {
                        if (stepDataList.size() == 0 || stepDataList.isEmpty()) {
                            stepCount = 0;
                        }
                        stepCount=0;
                        for (StepData stepdata : stepDataList) {
                            stepCount += stepdata.getStep();
                        }
                        Log.d(TAG, "accept:  stepcount"+stepCount);
                        circleScaleView.setStepNum(Integer.valueOf(mStepGoal), stepCount);
                    }
                });
        return stepCount;
    }

    /**
     * 获取当天日期
     *
     * @return
     */
    private String getTodaySimpleDate() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }

    @Override
    protected void setDefaultFragmentTitle(String title) {
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getActivity(), WeekReportActivity.class);
        String type = "TYPE";
        switch (v.getId()) {
            case R.id.pause_resume:
                setPause_resume(!pause_resume.isSelected());
                Firebase.getInstance(getApplicationContext()).logEvent("pedometerfragment", "暂停 按钮点击");
                break;
            case R.id.kcal:
                intent.putExtra(type, "kcal");
                Firebase.getInstance(getApplicationContext()).logEvent("pedometerfragment", "kcal 按钮点击");
                startActivity(intent);
                break;
            case R.id.km:
                intent.putExtra(type, "distance");
                startActivity(intent);
                Firebase.getInstance(getApplicationContext()).logEvent("pedometerfragment", "km 按钮点击");
                EventBusUtil.sendEvent(new Event(EventBusUtil.EventCode.GOWEEKACTIVITY, "distance"));
                break;
            case R.id.time:
                intent.putExtra(type, "time");
                startActivity(intent);
                Firebase.getInstance(getApplicationContext()).logEvent("pedometerfragment", "time 按钮点击");
                EventBusUtil.sendEvent(new Event(EventBusUtil.EventCode.GOWEEKACTIVITY, "time"));
                break;
        }
    }

    private static PedometerFragmentListener pedometerFragmentListener;
    public void registerPedometerFragmentListener(PedometerFragmentListener pedometerFragmentListener) {
        this.pedometerFragmentListener = pedometerFragmentListener;
    }


    /*停止 ，开始计步*/
    private void setPause_resume(boolean isOrNo) {
        pause_resume.setSelected(isOrNo);
        pause_in_progress.setVisibility(View.GONE);
        pause_resume.setText(R.string.pause);
        if (stepService != null) {  //  stepservice  只有再回调传回来的时候才不为空；
            if (isOrNo) {
                Log.d(TAG, "setPause_resume:  true ");
                pause_in_progress.setVisibility(View.GONE);
                pause_resume.setText(R.string.pause);
                stepService.registerReceiver();

            } else {
            /*停止计步*/
                pause_in_progress.setVisibility(View.VISIBLE);
                pause_resume.setText(R.string.resume);
                //new StepCount().setOffSteps(false);
                stepService.unRegisterReceiver();
            }
        }
    }
    private void startStepService() {
        Intent intent = new Intent(getActivity(), StepService.class);
        getActivity().startService(intent);
        isBind = getActivity().bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
        getActivity().startService(intent);
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            stepService = ((StepService.StepBinder) service).getService();
            Log.d(TAG, "pedometer  onserviceconnected ");
            //设置步数监听回调
            stepService.registerCallback(new UpdateUiCallBack() {
                @Override
                public void updateUi(int stepCount, int stepTimeCountSecond) {
                    Log.d(TAG, "pedometer  updateUi");
                    circleScaleView.setStepNum(Integer.valueOf(mStepGoal), stepCount);
                    setDistancekcalTime(stepCount, stepTimeCountSecond);
                }
            });
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };


    /**
     * @param stepTimeCountSecond 回调的时长；
     */
    private void setDistancekcalTime(int stepCount, int stepTimeCountSecond) {
        /*根据步数计算distance  kcal */
        String kcal = StepConversion.getCalorie(stepCount);
        kcal_tv.setText(String.valueOf(kcal));
        String distance = StepConversion.getDistance(gender, stepCount, height);
        km_tv.setText(distance);
        /*时长-- 分钟*/
        time_tv.setText(String.valueOf(stepTimeCountSecond / 60));
    }


    /**
     * 设置进度条
     *
     * @param mStepGoal
     * @param stepCount
     */
    private void setProgressRing(String mStepGoal, int stepCount) {
        if (mStepGoal != "0") {
            progressRing.setProgress(stepCount * 100 / Integer.parseInt(mStepGoal));
        } else {
            progressRing.setProgress(100);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unbindService(serviceConnection);
    }


    @Override
    public boolean allowShowAd() {
        return false;
    }
}
