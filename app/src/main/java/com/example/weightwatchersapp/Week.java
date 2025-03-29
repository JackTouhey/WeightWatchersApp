package com.example.weightwatchersapp;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.ArrayList;
@Entity(tableName = "Weeks")
public class Week implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    private int wId;
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
    private int weeklyPoints = 40;
    private int dailyLimit = 28;
    public Week(){
        monday = new Day("Monday");
    }

    protected Week(Parcel in) {
        monday = in.readParcelable(Day.class.getClassLoader());
        tuesday = in.readParcelable(Day.class.getClassLoader());
        wednesday = in.readParcelable(Day.class.getClassLoader());
        thursday = in.readParcelable(Day.class.getClassLoader());
        friday = in.readParcelable(Day.class.getClassLoader());
        saturday = in.readParcelable(Day.class.getClassLoader());
        sunday = in.readParcelable(Day.class.getClassLoader());
        weeklyPoints = in.readInt();
        dailyLimit = in.readInt();
    }

    public static final Creator<Week> CREATOR = new Creator<Week>() {
        @Override
        public Week createFromParcel(Parcel in) {
            return new Week(in);
        }

        @Override
        public Week[] newArray(int size) {
            return new Week[size];
        }
    };
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
            return monday;
        }
    }
    public int getWeeklyPoints(){
        return this.weeklyPoints;
    }
    private void createNextDay(){
        Day currentDay = getCurrentDay();
        if(currentDay == monday){
            tuesday = new Day("Tuesday");
        }else if(currentDay == tuesday){
            wednesday = new Day("Wednesday");
        }else if(currentDay == wednesday){
            thursday = new Day("Thursday");
        }else if(currentDay == thursday){
            friday = new Day("Friday");
        }else if(currentDay == friday){
            saturday = new Day("Saturday");
        }else if(currentDay == saturday){
            sunday = new Day("Sunday");
        }
    }
    public void completeDay(){
        int dayPointDifference = dailyLimit - getCurrentDay().getTotalPoints();
        if(dayPointDifference < 0){
            weeklyPoints += dayPointDifference;
        }else if(dayPointDifference >= 4){
            weeklyPoints += 4;
        }else if(dayPointDifference > 0){
            weeklyPoints += dayPointDifference;
        }
        createNextDay();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeParcelable(monday, flags);
        dest.writeParcelable(tuesday, flags);
        dest.writeParcelable(wednesday, flags);
        dest.writeParcelable(thursday, flags);
        dest.writeParcelable(friday, flags);
        dest.writeParcelable(saturday, flags);
        dest.writeParcelable(sunday, flags);
        dest.writeInt(weeklyPoints);
        dest.writeInt(dailyLimit);
    }
}
