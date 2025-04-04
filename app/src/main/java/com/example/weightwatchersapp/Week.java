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
    public Long getMonday(){return this.mondayId;}
    public Long getTuesday(){return this.tuesdayId;}
    public Long getWednesday(){return this.wednesdayId;}
    public Long getThursday(){return this.thursdayId;}
    public Long getFriday(){return this.fridayId;}
    public Long getSaturday(){return this.saturdayId;}
    public Long getSunday(){return this.sundayId;}
    public int getDailyLimit(){return this.dailyLimit;}
    public void setWId(long wId){this.wId = wId;}
    public void setMonday(Long monday){this.mondayId = monday;}
    public void setTuesday(Long tuesday){this.tuesdayId = tuesday;}
    public void setWednesday(Long wednesday){this.wednesdayId = wednesday;}
    public void setThursday(Long thursday){this.thursdayId = thursday;}
    public void setFriday(Long friday){this.fridayId = friday;}
    public void setSaturday(Long saturday){this.saturdayId = saturday;}
    public void setSunday(Long sunday){this.sundayId = sunday;}
    public void setDailyLimit(int dailyLimit){this.dailyLimit = dailyLimit;}
    public void setWeeklyPoints(int weeklyPoints){this.weeklyPoints = weeklyPoints;}
    public int getWeeklyPoints(){
        return this.weeklyPoints;
    }
    public int getWeeklyPointsAtMonday(){return addDPtoWP(monday.getTotalPoints(), weeklyPointStart);}
    public int getWeeklyPointsAtTuesday(){return addDPtoWP(tuesday.getTotalPoints(), getWeeklyPointsAtMonday());}
    public int getWeeklyPointsAtWednesday(){return addDPtoWP(wednesday.getTotalPoints(), getWeeklyPointsAtTuesday());}
    public int getWeeklyPointsAtThursday(){ return addDPtoWP(thursday.getTotalPoints(), getWeeklyPointsAtWednesday());}
    public int getWeeklyPointsAtFriday(){return addDPtoWP(friday.getTotalPoints(), getWeeklyPointsAtThursday());}
    public int getWeeklyPointsAtSaturday(){return addDPtoWP(saturday.getTotalPoints(), getWeeklyPointsAtFriday());}
    public int getWeeklyPointsAtSunday(){return addDPtoWP(sunday.getTotalPoints(), getWeeklyPointsAtSaturday());}
    public int getWeeklyPointsAtDay(String name) {
        int weeklyPoints = weeklyPointStart;
        switch (name){
            case "Monday":
                weeklyPoints =  getWeeklyPointsAtMonday();
                break;
            case "Tuesday":
                weeklyPoints = getWeeklyPointsAtTuesday();
                break;
            case "Wednesday":
                weeklyPoints = getWeeklyPointsAtWednesday();
                break;
            case "Thursday":
                weeklyPoints = getWeeklyPointsAtThursday();
                break;
            case "Friday":
                weeklyPoints = getWeeklyPointsAtFriday();
                break;
            case "Saturday":
                weeklyPoints = getWeeklyPointsAtSaturday();
                break;
            case "Sunday":
                weeklyPoints = getWeeklyPointsAtSunday();
                break;
        }
        return weeklyPoints;
    }
    private int addDPtoWP(int dp, int wp){
        int dailyPointsDifference = dailyLimit - dp;
        if(dailyPointsDifference >= 4 ){
            wp += 4;
        }
        else{
            wp += dailyPointsDifference;
        }
        return wp;
    }
    public Integer getCurrentDayId(){
        if(sundayId != null){
            return sundayId;
        }else if(saturdayId != null){
            return saturdayId;
        }else if(fridayId != null){
            return fridayId;
        }else if(thursdayId != null){
            return thursdayId;
        }else if(wednesdayId != null){
            return wednesdayId;
        }else if(tuesdayId != null){
            return tuesdayId;
        }else{
            return this.mondayId;
        }
    }
    private void createNextDay(){
        Day currentDay = getCurrentDay();
        if(currentDay == monday){
            tuesday = new Day("Tuesday", this.wId);
            Log.d("DEBUG", "Tuesday Created");
        }else if(currentDay == tuesday){
            wednesday = new Day("Wednesday", this.wId);
        }else if(currentDay == wednesday){
            thursday = new Day("Thursday", this.wId);
        }else if(currentDay == thursday){
            friday = new Day("Friday", this.wId);
        }else if(currentDay == friday){
            saturday = new Day("Saturday", this.wId);
        }else if(currentDay == saturday){
            sunday = new Day("Sunday", this.wId);
        }
    }
    public void completeDay() {
        Log.d("DEBUG", "completeDay entered. Current WeekId: " + this.wId + "Current day: " + getCurrentDay().getName());
        int dayPointDifference = dailyLimit - getCurrentDay().getTotalPoints();
        Log.d("DEBUG", "Week: " + wId + " currentDay: " + getCurrentDay().getName() + " dayPointDifference: " + dayPointDifference);
        if (dayPointDifference < 0) {
            weeklyPoints += dayPointDifference;
        } else if (dayPointDifference >= 4) {
            weeklyPoints += 4;
        } else if (dayPointDifference > 0) {
            weeklyPoints += dayPointDifference;
        }
        createNextDay();
    }
}
