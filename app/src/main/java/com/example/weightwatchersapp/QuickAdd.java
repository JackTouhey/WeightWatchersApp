package com.example.weightwatchersapp;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "QuickAdd")
public class QuickAdd {
    @PrimaryKey(autoGenerate = true)
    private long quickAddID;
    @ColumnInfo(name = "name")
    private String name;
    @ColumnInfo(name = "breakfast_points")
    private Integer breakfastPoints;
    @ColumnInfo(name = "lunch_points")
    private Integer lunchPoints;
    @ColumnInfo(name = "dinner_points")
    private Integer dinnerPoints;
    @ColumnInfo(name = "other_points")
    private Integer otherPoints;

    public QuickAdd(){

    }
    public void setQuickAddID(long quickAddID){this.quickAddID = quickAddID;}
    public long getQuickAddID(){return this.quickAddID;}
    public void setName(String name){this.name = name;}
    public String getName(){return this.name;}
    public void setBreakfastPoints(Integer breakfastPoints){this.breakfastPoints = breakfastPoints;}
    public Integer getBreakfastPoints(){return this.breakfastPoints;}
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
    public void setOtherPoints(Integer otherPoints){this.otherPoints = otherPoints;}
    public Integer getOtherPoints(){return this.otherPoints;}
    public void removeBreakfastPoints(){this.breakfastPoints = null;}
    public void removeLunchPoints(){this.lunchPoints = null;}
    public void removeDinnerPoints(){this.dinnerPoints = null;}
    public void removeOtherPoints(){this.otherPoints = null;}
}
