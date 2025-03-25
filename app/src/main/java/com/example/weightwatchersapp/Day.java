package com.example.weightwatchersapp;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Day implements Parcelable {

    private int breakfastPoints;
    private int lunchPoints;
    private int dinnerPoints;
    private int otherPoints = 0;
    private String name;

    public Day(){
    }
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
    public int getBreakfastPoints(){
        return this.breakfastPoints;
    }
    public void setLunchPoints(int lunchPoints){
        this.lunchPoints = lunchPoints;
    }
    public int getLunchPoints(){
        return this.lunchPoints;
    }
    public void setDinnerPoints(int dinnerPoints){
        this.dinnerPoints = dinnerPoints;
    }
    public int getDinnerPoints(){
        return this.dinnerPoints;
    }
    public void addOtherPoints(int otherPoints){
        this.otherPoints += otherPoints;
    }
    public int getOtherPoints(){
        return this.otherPoints;
    }
    public int getTotalPoints(){
        return breakfastPoints + lunchPoints + dinnerPoints + otherPoints;
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

