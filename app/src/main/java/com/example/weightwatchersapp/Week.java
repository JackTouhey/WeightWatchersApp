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
    @ColumnInfo(name="monday")
    private Day monday;
    @ColumnInfo(name = "tuesday")
    private Day tuesday;
    @ColumnInfo(name = "wednesday")
    private Day wednesday;
    @ColumnInfo(name = "thursday")
    private Day thursday;
    @ColumnInfo(name = "friday")
    private Day friday;
    @ColumnInfo(name = "saturday")
    private Day saturday;
    @ColumnInfo(name = "sunday")
    private Day sunday;
    @ColumnInfo(name = "weekly_points")
    private int weeklyPoints;
    private int weeklyPointStart;
    private int dailyLimit = 28;
    public Week(int weeklyPointStart){
        this.weeklyPoints = weeklyPointStart;
        this.weeklyPointStart = weeklyPointStart;
    }
    public void makeMonday(){
        this.monday = new Day("Monday", this.wId);
        Log.d("DEBUG", "Monday Made, object: " + this.monday);
    }
    public int getWeeklyPointStart(){return this.weeklyPointStart;}
    public void setWeeklyPointStart(int weeklyPointStart){this.weeklyPointStart = weeklyPointStart;}
    public long getWId(){return this.wId;}
    public Day getMonday(){return this.monday;}
    public Day getTuesday(){return this.tuesday;}
    public Day getWednesday(){return this.wednesday;}
    public Day getThursday(){return this.thursday;}
    public Day getFriday(){return this.friday;}
    public Day getSaturday(){return this.saturday;}
    public Day getSunday(){return this.sunday;}
    public int getDailyLimit(){return this.dailyLimit;}
    public void setWId(long wId){this.wId = wId;}
    public void setMonday(Day monday){this.monday = monday;}
    public void setTuesday(Day tuesday){this.tuesday = tuesday;}
    public void setWednesday(Day wednesday){this.wednesday = wednesday;}
    public void setThursday(Day thursday){this.thursday = thursday;}
    public void setFriday(Day friday){this.friday = friday;}
    public void setSaturday(Day saturday){this.saturday = saturday;}
    public void setSunday(Day sunday){this.sunday = sunday;}
    public void setDailyLimit(int dailyLimit){this.dailyLimit = dailyLimit;}
    public void setWeeklyPoints(int weeklyPoints){this.weeklyPoints = weeklyPoints;}
    public ArrayList<Day> getHistory(){
        ArrayList<Day> history = new ArrayList<>();
        if(monday != null){
            history.add(monday);
        }
        if(tuesday != null){
            history.add(tuesday);
        }
        if(wednesday != null){
            history.add(wednesday);
        }
        if(thursday != null){
            history.add(thursday);
        }
        if(friday != null){
            history.add(friday);
        }
        if(saturday != null){
            history.add(saturday);
        }
        if(sunday != null){
            history.add(sunday);
        }
        return history;
    }
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
    public Day getCurrentDay(){
        if(sunday != null){
            return sunday;
        }else if(saturday != null){
            return saturday;
        }else if(friday != null){
            return friday;
        }else if(thursday != null){
            return thursday;
        }else if(wednesday != null){
            return wednesday;
        }else if(tuesday != null){
            return tuesday;
        }else{
            return this.monday;
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
