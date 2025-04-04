package com.example.weightwatchersapp;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface WeekDao {
    @Query("SELECT * FROM Weeks")
    List<Week> getAllWeeks();

    default ArrayList<Week> getAll(){
        List<Week> weekList = getAllWeeks();
        return new ArrayList<Week>(weekList);
    }
    @Query("SELECT * FROM Weeks WHERE wId = :weekId")
    Week getWeekById(long weekId);
    @Query("SELECT MAX(wId) FROM Weeks")
    long getCurrentWId();
    @Query("SELECT DISTINCT mondayId FROM Weeks WHERE wId = :weekId")
    Long getMondayIdFromWeek(long weekId);
    @Query("SELECT DISTINCT tuesdayId FROM Weeks WHERE wId = :weekId")
    Long getTuesdayIdFromWeek(long weekId);
    @Query("SELECT DISTINCT wednesdayId FROM Weeks WHERE wId = :weekId")
    Long getWednesdayIdFromWeek(long weekId);
    @Query("SELECT DISTINCT thursdayId FROM Weeks WHERE wId = :weekId")
    Long getThursdayIdFromWeek(long weekId);
    @Query("SELECT DISTINCT fridayId FROM Weeks WHERE wId = :weekId")
    Long getFridayIdFromWeek(long weekId);
    @Query("SELECT DISTINCT saturdayId FROM Weeks WHERE wId = :weekId")
    Long getSaturdayIdFromWeek(long weekId);
    @Query("SELECT DISTINCT sundayId FROM Weeks WHERE wId = :weekId")
    Long getSundayIdFromWeek(long weekId);
    @Insert
    long insert(Week week);
    @Update
    void update(Week week);
    @Delete
    void delete(Week week);
}
