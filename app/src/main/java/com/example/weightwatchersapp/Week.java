package com.example.weightwatchersapp;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.ArrayList;
@Entity(tableName = "Weeks")
public class Week {
    @PrimaryKey(autoGenerate = true)
    private long wId;
    @ColumnInfo(name="mondayId")
    private Long mondayId;
    @ColumnInfo(name = "tuesdayId")
    private Long tuesdayId;
    @ColumnInfo(name = "wednesdayId")
    private Long wednesdayId;
    @ColumnInfo(name = "thursdayId")
    private Long thursdayId;
    @ColumnInfo(name = "fridayId")
    private Long fridayId;
    @ColumnInfo(name = "saturdayId")
    private Long saturdayId;
    @ColumnInfo(name = "sundayId")
    private Long sundayId;
    @ColumnInfo(name = "weekly_points")
    private int weeklyPoints;
    @ColumnInfo(name = "weekly_point_start")
    private int weeklyPointStart;
    private int dailyLimit = 28;
    public Week(int weeklyPointStart){
        this.weeklyPoints = weeklyPointStart;
        this.weeklyPointStart = weeklyPointStart;
    }
    public int getWeeklyPointStart(){return this.weeklyPointStart;}
    public void setWeeklyPointStart(int weeklyPointStart){this.weeklyPointStart = weeklyPointStart;}
    public long getWId(){return this.wId;}
    public Long getMondayId(){return this.mondayId;}
    public Long getTuesdayId(){return this.tuesdayId;}
    public Long getWednesdayId(){return this.wednesdayId;}
    public Long getThursdayId(){return this.thursdayId;}
    public Long getFridayId(){return this.fridayId;}
    public Long getSaturdayId(){return this.saturdayId;}
    public Long getSundayId(){return this.sundayId;}
    public int getDailyLimit(){return this.dailyLimit;}
    public void setWId(long wId){this.wId = wId;}
    public void setMondayId(Long monday){this.mondayId = monday;}
    public void setTuesdayId(Long tuesday){this.tuesdayId = tuesday;}
    public void setWednesdayId(Long wednesday){this.wednesdayId = wednesday;}
    public void setThursdayId(Long thursday){this.thursdayId = thursday;}
    public void setFridayId(Long friday){this.fridayId = friday;}
    public void setSaturdayId(Long saturday){this.saturdayId = saturday;}
    public void setSundayId(Long sunday){this.sundayId = sunday;}
    public void setDailyLimit(int dailyLimit){this.dailyLimit = dailyLimit;}
    public void setWeeklyPoints(int weeklyPoints){this.weeklyPoints = weeklyPoints;}
    public int getWeeklyPoints(){
        return this.weeklyPoints;
    }
}
