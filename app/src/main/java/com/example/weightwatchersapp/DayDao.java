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
    @Insert
    long insert(Day day);
    @Update
    void update(Day day);
}
