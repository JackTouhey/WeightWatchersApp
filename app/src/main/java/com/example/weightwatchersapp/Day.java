package com.example.weightwatchersapp;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Day implements Parcelable {

    private Integer breakfastPoints;
    private Integer lunchPoints;
    private Integer dinnerPoints;
    private Integer otherPoints;
    private final String name;
    private int beerCount = 0;
    private final int dailyPoints = 28;

    public Day(String name){
        this.name = name;
    }

    protected Day(Parcel in) {
        breakfastPoints = in.readInt();
        lunchPoints = in.readInt();
        dinnerPoints = in.readInt();
        otherPoints = in.readInt();
        name = in.readString();
    }

    public static final Creator<Day> CREATOR = new Creator<Day>() {
        @Override
        public Day createFromParcel(Parcel in) {
            return new Day(in);
        }
        @Override
        public Day[] newArray(int size) {
            return new Day[size];
        }
    };

    public String getName(){
        return this.name;
    }
    public void setBreakfastPoints(int breakfastPoints) {
        this.breakfastPoints = breakfastPoints;
    }
    public Integer getBreakfastPoints(){
        return this.breakfastPoints;
    }
    public void setLunchPoints(int lunchPoints){
        this.lunchPoints = lunchPoints;
    }
    public Integer getLunchPoints(){
        return this.lunchPoints;
    }
    public void setDinnerPoints(int dinnerPoints){
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(breakfastPoints);
        dest.writeInt(lunchPoints);
        dest.writeInt(dinnerPoints);
        dest.writeInt(otherPoints);
        dest.writeString(name);
    }
}

