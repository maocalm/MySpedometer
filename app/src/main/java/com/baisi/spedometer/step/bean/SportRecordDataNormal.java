package com.baisi.spedometer.step.bean;

import java.util.Date;

/**
 * Created by hanwenmao on 2018/1/12.
 */

public class SportRecordDataNormal {
    private Date SprotDate;
    private String simpleDate;
    private float  Miles ; // zou 的距离
    private long sportLong ;  //  时钟时间
    private String  milesNeedTime ; //  走一miles  所需的时间  （s）
    private  int steps ;
    private  boolean istitle ;
    private  int sport_times;  //  运动的次数
    public SportRecordDataNormal() {
    }

    public SportRecordDataNormal(Date sprotDate, String simpleDate, float miles, long sportLong, String milesNeedTime, int steps, boolean istitle, int sport_times) {
        SprotDate = sprotDate;
        this.simpleDate = simpleDate;
        Miles = miles;
        this.sportLong = sportLong;
        this.milesNeedTime = milesNeedTime;
        this.steps = steps;
        this.istitle = istitle;
        this.sport_times = sport_times;
    }

    public Date getSprotDate() {
        return SprotDate;
    }

    public void setSprotDate(Date sprotDate) {
        SprotDate = sprotDate;
    }

    public String getSimpleDate() {
        return simpleDate;
    }

    public void setSimpleDate(String simpleDate) {
        this.simpleDate = simpleDate;
    }

    public float getMiles() {
        return Miles;
    }

    public int getSport_times() {
        return sport_times;
    }

    public void setSport_times(int sport_times) {
        this.sport_times = sport_times;
    }

    public void setMiles(float miles) {
        Miles = miles;
    }

    public long getSportLong() {
        return sportLong;
    }

    public void setSportLong(long sportLong) {
        this.sportLong = sportLong;
    }

    public String getMilesNeedTime() {
        return milesNeedTime;
    }

    public void setMilesNeedTime(String milesNeedTime) {
        this.milesNeedTime = milesNeedTime;
    }

    public int getSteps() {
        return steps;
    }

    public void setSteps(int steps) {
        this.steps = steps;
    }

    public boolean isIstitle() {
        return istitle;
    }

    public void setIstitle(boolean istitle) {
        this.istitle = istitle;
    }

    @Override
    public String toString() {
        return "SportRecordDataNormal{" +
                "SprotDate=" + SprotDate +
                ", simpleDate='" + simpleDate + '\'' +
                ", Miles=" + Miles +
                ", sportLong=" + sportLong +
                ", milesNeedTime='" + milesNeedTime + '\'' +
                ", steps=" + steps +
                ", istitle=" + istitle +
                ", sport_times=" + sport_times +
                '}';
    }
}
