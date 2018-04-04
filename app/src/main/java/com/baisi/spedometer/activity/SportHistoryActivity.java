package com.baisi.spedometer.activity;

import android.content.Intent;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.baisi.spedometer.R;
import com.baisi.spedometer.adapter.SportDecoration;
import com.baisi.spedometer.adapter.SportHistoryAdapter;
import com.baisi.spedometer.ads.GoogleAd;
import com.baisi.spedometer.greendao.gen.SportRecordDataDao;
import com.baisi.spedometer.step.bean.SportRecordData;
import com.baisi.spedometer.step.bean.SportRecordDataNormal;
import com.baisi.spedometer.step.utils.MyDbUtils;
import com.baisi.spedometer.step.utils.StepConversion;
import com.baisi.spedometer.utiles.Firebase;
import com.bestgo.adsplugin.ads.AdAppHelper;
import com.bestgo.adsplugin.ads.activity.ShowAdFilter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class SportHistoryActivity extends AppCompatActivity implements ShowAdFilter, View.OnClickListener {

    private ContentLoadingProgressBar contentLoadingProgressBar;
    private RecyclerView recyclerView;
    private RelativeLayout relativeLayoutTop;
    private List<SportRecordDataNormal> sportRecordAdapterDataList = new ArrayList<>();
    private SportRecordDataDao daoSessionSportRecord;

    //填充adapter de 数据源；
    //private List<SportRecordDataNormal> sportRecordDataNormalsList;
    private SportHistoryAdapter sportHistoryAdapter;
    private RelativeLayout progress_rl;
    private FrameLayout frameLayoutAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sport_history);
        inintView();
        initDb();
        setRecycleViewData();
        showAd();
        Firebase.getInstance(getApplicationContext()).logEvent("SportHistoryActivity", "进入");
    }

    private void showAd() {
        /*加载广告*/
        GoogleAd.loadAd(frameLayoutAd ,1 ,"sportHistory");
    }
    private void inintView() {
        frameLayoutAd = findViewById(R.id.ad_frame);
        relativeLayoutTop = findViewById(R.id.top);
        relativeLayoutTop.setOnClickListener(this);
        progress_rl = findViewById(R.id.progress_rl);
        recyclerView = findViewById(R.id.historyrecycleview);
        contentLoadingProgressBar = findViewById(R.id.progress_bar);
        showOrHideProgress(true);
        sportHistoryAdapter = new SportHistoryAdapter(this, sportRecordAdapterDataList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(sportHistoryAdapter);
        recyclerView.addItemDecoration(new SportDecoration(this  , DividerItemDecoration.VERTICAL));
    }

    private void showOrHideProgress(boolean trueorfalse){
        if (trueorfalse==true){
            progress_rl.setVisibility(View.VISIBLE);
            contentLoadingProgressBar.show();
        }else {
            progress_rl.setVisibility(View.INVISIBLE);
            contentLoadingProgressBar.hide();
        }
    }
    private void initDb() {
        MyDbUtils dbUtils = new MyDbUtils(this);
        daoSessionSportRecord = dbUtils.getDaoManager().getDaoSession().getSportRecordDataDao();
    }

    private void setRecycleViewData() {
        Observable.create(new ObservableOnSubscribe<List<SportRecordDataNormal>>() {
            @Override
            public void subscribe(ObservableEmitter<List<SportRecordDataNormal>> e) throws Exception {
                List<SportRecordDataNormal> sportRecordDataNormals = getDaoData();
                e.onNext(sportRecordDataNormals);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<SportRecordDataNormal>>() {
                    @Override
                    public void accept(List<SportRecordDataNormal> sportRecordDataNormals) throws Exception {
                        showOrHideProgress(false);
                        sportHistoryAdapter.notifyDataSetChanged();
                    }
                });

    }

    private List<SportRecordDataNormal> getDaoData() {
        Long countid = daoSessionSportRecord.queryBuilder().count();
        SportRecordData sportRecordData = daoSessionSportRecord.queryBuilder().where(SportRecordDataDao.Properties.Id.eq(countid)).unique();
        //sportRecordDataNormalsList = new ArrayList<>();
        for (int i = 11; i >= 0; i--) {

            Date date = StepConversion.getTimes1Monthmorning(new Date(), i);

            //获取当月的第一天和最后一天；
            Date datemorning = StepConversion.getTimesMonthmorning(date);
            Date datenight = StepConversion.getTimesMonthnight(date);

            List<SportRecordData> sportRecordDataList = daoSessionSportRecord.queryBuilder()
                    .where(SportRecordDataDao.Properties.SprotDate.between(datemorning, datenight))
                    .orderAsc(SportRecordDataDao.Properties.SprotDate)
                    .list();
            if (sportRecordDataList.size()==0){
                continue;
            }
            Float milesAllTop = 0f;
            for (int k = 0; k < sportRecordDataList.size(); k++) {
                milesAllTop += sportRecordDataList.get(k).getMiles();
            }

            //tian jia  toptitle   ;
            if (sportRecordDataList != null) { // 本月没有数据
                SportRecordDataNormal sportRecordDataNormaltitle = new SportRecordDataNormal();
                sportRecordDataNormaltitle.setSprotDate(date);
                BigDecimal b = new BigDecimal(milesAllTop);
                Float milesAllTopF = b.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
                sportRecordDataNormaltitle.setMiles(milesAllTopF);
                sportRecordDataNormaltitle.setSport_times(sportRecordDataList.size());
                sportRecordDataNormaltitle.setIstitle(true);
                sportRecordAdapterDataList.add(sportRecordDataNormaltitle);
            }

            int countDao = sportRecordDataList.size();


            for (int j = 0; j < countDao; j++) {
                SportRecordDataNormal sportRecordDataNormal = new SportRecordDataNormal();
                SportRecordData sportRecordData1 = sportRecordDataList.get(j);
                if (j == countDao) {
                    sportRecordDataNormal.setIstitle(true);
                } else {
                    sportRecordDataNormal.setIstitle(false);
                }
                sportRecordDataNormal.setMiles(sportRecordData1.getMiles());
                sportRecordDataNormal.setMilesNeedTime(sportRecordData1.getMilesNeedTime());
                sportRecordDataNormal.setSimpleDate(sportRecordData1.getSimpleDate());
                sportRecordDataNormal.setSportLong(sportRecordData1.getSportLong());
                sportRecordDataNormal.setSprotDate(sportRecordData1.getSprotDate());
                sportRecordDataNormal.setSteps(sportRecordData1.getSteps());

                sportRecordAdapterDataList.add(sportRecordDataNormal);

            }
        }
        //数据库最后一天的日期；
        Date date = sportRecordData.getSprotDate();

        //数据库中第一天的日期 ；
        Date daoFirstDate = daoSessionSportRecord.loadByRowId(1).getSprotDate();


        return sportRecordAdapterDataList;
    }

    @Override
    public boolean allowShowAd() {
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.top:
                finish();
                break;

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(0);

//        Intent intent = new Intent(this ,MainActivity.class);
//        startActivity(intent);
//        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        setResult(0);
        finish();
    }
}
