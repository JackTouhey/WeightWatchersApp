package com.example.weightwatchersapp;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface DayDao {
    @Query("SELECT * FROM Days")
    List<Day> getAllDays();

    default ArrayList<Day> getAll(){
        List<Day> dayList = getAllDays();
        return new ArrayList<Day>(dayList);
    }
    @Query("SELECT * FROM Days WHERE dId = :dayId")
    Day getDayById(long dayId);
    @Query("SELECT DISTINCT daily_points FROM Days WHERE dId = :dayId")
    int getDailyPoints(long dayId);
    @Query("SELECT DISTINCT breakfast_points FROM Days WHERE dId = :dayId")
    Integer getBreakfastPoints(long dayId);
    @Query("SELECT DISTINCT lunch_points FROM Days WHERE dId = :dayId")
    Integer getLunchPoints(long dayId);
    @Query("SELECT DISTINCT dinner_points FROM Days WHERE dId = :dayId")
    Integer getDinnerPoints(long dayId);
    @Query("SELECT DISTINCT other_points FROM Days WHERE dId = :dayId")
    Integer getOtherPoints(long dayId);
    @Query("SELECT DISTINCT name FROM Days WHERE dId = :dayId")
    String getName(long dayId);
    default int getTotalPoints(long dayId){
        int total = 0;
        if(getBreakfastPoints(dayId) != null){
            total += getBreakfastPoints(dayId);
        }
        if(getLunchPoints(dayId) != null){
            total += getLunchPoints(dayId);
        }
        if(getDinnerPoints(dayId) != null){
            total += getDinnerPoints(dayId);
        }
        if(getOtherPoints(dayId) != null){
            total += getOtherPoints(dayId);
        }
        return total;
    }
    default int getRemainingPoints(long dayId){
        return getDailyPoints(dayId) - getTotalPoints(dayId);
    }
    @Insert
    long insert(Day day);
    @Update
    void update(Day day);
}
