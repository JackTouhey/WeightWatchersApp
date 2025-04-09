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
    @ColumnInfo(name="mondayWP")
    private int mondayWP;
    @ColumnInfo(name = "tuesdayWP")
    private int tuesdayWP;
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
    public Week(int weeklyPointStart, Long mondayId){
        this.weeklyPoints = weeklyPointStart;
        this.weeklyPointStart = weeklyPointStart;
    }
    public int getWeeklyPointStart(){return this.weeklyPointStart;}
    public void setWeeklyPointStart(int weeklyPointStart){this.weeklyPointStart = weeklyPointStart;}
    public long getWId(){return this.wId;}
    public Long getMondayId(){return (wId * 7) - 6;}
    public Long getTuesdayId(){return (wId * 7) - 5;}
    public Long getWednesdayId(){return (wId * 7) - 4;}
    public Long getThursdayId(){return (wId * 7) - 3;}
    public Long getFridayId(){return (wId * 7) - 2;}
    public Long getSaturdayId(){return (wId * 7) - 1;}
    public Long getSundayId(){return (wId * 7);}
    public int getDailyLimit(){return this.dailyLimit;}
    public void setWId(long wId){this.wId = wId;}
    public void setMondayId(Long mondayId){
        Log.d("DEBUG", "Setting MondayId: " + mondayId);
        this.mondayId = mondayId;}
    public void setTuesdayId(Long tuesdayId){this.tuesdayId = tuesdayId;}
    public void setWednesdayId(Long wednesdayId){this.wednesdayId = wednesdayId;}
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
