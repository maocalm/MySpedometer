package com.baisi.spedometer.step.bean;

import org.greenrobot.greendao.annotation.Entity;

import java.util.Date;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * Created by hanwenmao on 2018/1/11.
 */
@Entity
public class SportRecordData {

    @Id(autoincrement = true)
    private Long id ;
    private Date SprotDate;
    private String simpleDate;
    private float  Miles ; // zou 的距离
    private long sportLong ;  //  时钟时间
    private String  milesNeedTime ; //  走一miles  所需的时间  （s）
    private  int steps ;
    @Generated(hash = 1367859063)
    public SportRecordData(Long id, Date SprotDate, String simpleDate, float Miles,
            long sportLong, String milesNeedTime, int steps) {
        this.id = id;
        this.SprotDate = SprotDate;
        this.simpleDate = simpleDate;
        this.Miles = Miles;
        this.sportLong = sportLong;
        this.milesNeedTime = milesNeedTime;
        this.steps = steps;
    }
    @Generated(hash = 163707897)
    public SportRecordData() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Date getSprotDate() {
        return this.SprotDate;
    }
    public void setSprotDate(Date SprotDate) {
        this.SprotDate = SprotDate;
    }
    public String getSimpleDate() {
        return this.simpleDate;
    }
    public void setSimpleDate(String simpleDate) {
        this.simpleDate = simpleDate;
    }
    public float getMiles() {
        return this.Miles;
    }
    public void setMiles(float Miles) {
        this.Miles = Miles;
    }
    public long getSportLong() {
        return this.sportLong;
    }
    public void setSportLong(long sportLong) {
        this.sportLong = sportLong;
    }
    public String getMilesNeedTime() {
        return this.milesNeedTime;
    }
    public void setMilesNeedTime(String milesNeedTime) {
        this.milesNeedTime = milesNeedTime;
    }
    public int getSteps() {
        return this.steps;
    }
    public void setSteps(int steps) {
        this.steps = steps;
    }

    @Override
    public String toString() {
        return "SportRecordData{" +
                "id=" + id +
                ", SprotDate=" + SprotDate +
                ", simpleDate='" + simpleDate + '\'' +
                ", Miles=" + Miles +
                ", sportLong=" + sportLong +
                ", milesNeedTime='" + milesNeedTime + '\'' +
                ", steps=" + steps +
                '}';
    }
}
