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
    @ColumnInfo(name = "wednesdayWP")
    private int wednesdayWP;
    @ColumnInfo(name = "thursdayWP")
    private int thursdayWP;
    @ColumnInfo(name = "fridayWP")
    private int fridayWP;
    @ColumnInfo(name = "saturdayWP")
    private int saturdayWP;
    @ColumnInfo(name = "sundayWP")
    private int sundayWP;
    @ColumnInfo(name = "weekly_points")
    private int weeklyPoints;
    @ColumnInfo(name = "weekly_point_start")
    private int weeklyPointStart;
    public Week(int weeklyPointStart){
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
    public void setWId(long wId){this.wId = wId;}
    public void setMondayWP(int mondayWP){this.mondayWP = mondayWP;}
    public void setTuesdayWP(int tuesdayWP){this.tuesdayWP = tuesdayWP;}
    public void setWednesdayWP(int wednesdayWP){this.wednesdayWP = wednesdayWP;}
    public void setThursdayWP(int thursdayWP){this.thursdayWP = thursdayWP;}
    public void setFridayWP(int fridayWP){this.fridayWP = fridayWP;}
    public void setSaturdayWP(int saturdayWP){this.saturdayWP = saturdayWP;}
    public void setSundayWP(int sundayWP){this.sundayWP = sundayWP;}
    public int getMondayWP(){return this.mondayWP;}
    public int getTuesdayWP(){return this.tuesdayWP;}
    public int getWednesdayWP(){return this.wednesdayWP;}
    public int getThursdayWP(){return this.thursdayWP;}
    public int getFridayWP(){return this.fridayWP;}
    public int getSaturdayWP(){return this.saturdayWP;}
    public int getSundayWP(){return this.sundayWP;}
    public void setWeeklyPoints(int weeklyPoints){this.weeklyPoints = weeklyPoints;}
    public int getWeeklyPoints(){
        return this.weeklyPoints;
    }
    public void updateWeeklyPoints(Integer newWeeklyPoints, long currentDayId){
        int pointDifference = this.weeklyPoints - newWeeklyPoints;
        if (currentDayId >= this.getMondayId()){
            this.setMondayWP(this.getMondayWP() + pointDifference);
        }
        if (currentDayId >= this.getTuesdayId()){
            this.setTuesdayWP(this.getTuesdayWP() + pointDifference);
        }
        if (currentDayId >= this.getWednesdayId()){
            this.setWednesdayWP(this.getWednesdayWP() + pointDifference);
        }
        if (currentDayId >= this.getThursdayId()){
            this.setThursdayWP(this.getThursdayWP() + pointDifference);
        }
        if (currentDayId >= this.getFridayId()){
            this.setFridayWP(this.getFridayWP() + pointDifference);
        }
        if (currentDayId >= this.getSaturdayId()){
            this.setSaturdayWP(this.getSaturdayWP() + pointDifference);
        }
        if (currentDayId >= this.getSundayWP()){
            this.setSundayWP(this.getSundayWP() + pointDifference);
        }
    }
}
