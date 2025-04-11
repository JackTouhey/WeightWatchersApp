package com.example.weightwatchersapp;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Days")
public class Day  {

    @PrimaryKey(autoGenerate = true)
    private long dId;
    @ColumnInfo(name = "breakfast_points")
    private Integer breakfastPoints;
    @ColumnInfo(name = "lunch_points")
    private Integer lunchPoints;
    @ColumnInfo(name = "dinner_points")
    private Integer dinnerPoints;
    @ColumnInfo(name = "other_points")
    private Integer otherPoints;
    @ColumnInfo(name = "name")
    private final String name;
    @ColumnInfo(name = "beer_count")
    private int beerCount = 0;
    @ColumnInfo(name = "daily_points")
    private int dailyPoints = 28;
    @ColumnInfo(name = "weekId")
    private long weekId;

    public Day(String name, long weekId){
        this.name = name;
        this.weekId = weekId;
    }
    public void setWeekId(long weekId){this.weekId = weekId;}
    public long getWeekId(){return this.weekId;}
    public String getName(){
        return this.name;
    }
    public long getDId(){
        return this.dId;
    }
    public void setDId(int DId){this.dId = DId;}
    public void setOtherPoints(Integer otherPoints){this.otherPoints = otherPoints;}
    public void setBeerCount(int beerCount){this.beerCount = beerCount;}
    public void setDailyPoints(int dailyPoints){this.dailyPoints = dailyPoints;}

    public void setBreakfastPoints(Integer breakfastPoints) {
        this.breakfastPoints = breakfastPoints;
    }
    public int getDailyPoints(){return this.dailyPoints;}
    public Integer getBreakfastPoints(){
        return this.breakfastPoints;
    }
    public void setLunchPoints(Integer lunchPoints){
        this.lunchPoints = lunchPoints;
    }
    public Integer getLunchPoints(){
        return this.lunchPoints;
    }
    public void setDinnerPoints(Integer dinnerPoints){
        this.dinnerPoints = dinnerPoints;
    }
    public Integer getDinnerPoints(){
        return this.dinnerPoints;
    }
    public void addOtherPoints(int otherPoints){
        if(this.otherPoints == null){
            this.otherPoints = otherPoints;
        }
        else{
            this.otherPoints += otherPoints;
        }
    }
    public Integer getOtherPoints(){
        return this.otherPoints;
    }
    public int getTotalPoints(){
        int total = 0;
        if(breakfastPoints != null){
            total += breakfastPoints;
        }
        if(lunchPoints != null){
            total += lunchPoints;
        }
        if(dinnerPoints != null){
            total += dinnerPoints;
        }
        if(otherPoints != null){
            total += otherPoints;
        }
        return total;
    }
    public Integer getRemainingPoints(){
        return dailyPoints - getTotalPoints();
    }
    public int getBeerCount(){
        return beerCount;
    }
    public void addBeer(){
        if(otherPoints != null){
            this.otherPoints += 4;
        } else{
            this.otherPoints = 4;
        }
        this.beerCount++;
    }
    public boolean hasBreakfastPoints(){
        return breakfastPoints != null;
    }
    public boolean hasLunchPoints(){
        return lunchPoints != null;
    }
    public boolean hasDinnerPoints(){
        return dinnerPoints != null;
    }
    public boolean hasOtherPoints(){
        return otherPoints != null;
    }
}

