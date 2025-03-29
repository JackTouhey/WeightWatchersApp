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
    private int dId;
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
    private int dailyPoints = 28;

    public Day(String name){
        this.name = name;
    }
    public String getName(){
        return this.name;
    }
    public int getDId(){
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
    public int getRemainingPoints(){
        return dailyPoints - getTotalPoints();
    }
    public int getBeerCount(){
        return beerCount;
    }
    public void addBeer(){
        if(otherPoints != null){
            otherPoints += 4;
        } else{
            otherPoints = 4;
        }
        beerCount++;
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

