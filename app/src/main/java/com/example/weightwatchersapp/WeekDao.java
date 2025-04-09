package com.example.weightwatchersapp;

import android.util.Log;

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
    default int getWeeklyPointsAtDay(Long weekId, String name){
        switch (name){
            case "Monday":
                return getMondayWPFromWeekId(weekId);
            case "Tuesday":
                return getTuesdayWPFromWeekId(weekId);
            case "Wednesday":
                return getWednesdayWPFromWeekId(weekId);
            case "Thursday":
                return getThursdayWPFromWeekId(weekId);
            case "Friday":
                return getFridayWPFromWeekId(weekId);
            case "Saturday":
                return getSaturdayWPFromWeekId(weekId);
            default:
                return getSundayWPFromWeekId(weekId);
        }
    }
    @Query("SELECT * FROM Weeks WHERE wId = :weekId")
    Week getWeekById(long weekId);
    @Query("SELECT MAX(wId) FROM Weeks")
    long getCurrentWId();
    @Query("SELECT DISTINCT mondayWP FROM Weeks WHERE wId = :weekId")
    int getMondayWPFromWeekId(Long weekId);
    @Query("SELECT DISTINCT tuesdayWP FROM Weeks WHERE wId = :weekId")
    int getTuesdayWPFromWeekId(Long weekId);
    @Query("SELECT DISTINCT wednesdayWP FROM Weeks WHERE wId = :weekId")
    int getWednesdayWPFromWeekId(Long weekId);
    @Query("SELECT DISTINCT thursdayWP FROM Weeks WHERE wId = :weekId")
    int getThursdayWPFromWeekId(Long weekId);
    @Query("SELECT DISTINCT fridayWP FROM Weeks WHERE wId = :weekId")
    int getFridayWPFromWeekId(Long weekId);
    @Query("SELECT DISTINCT saturdayWP FROM Weeks WHERE wId = :weekId")
    int getSaturdayWPFromWeekId(Long weekId);
    @Query("SELECT DISTINCT sundayWP FROM Weeks WHERE wId = :weekId")
    int getSundayWPFromWeekId(Long weekId);
    @Insert
    long insert(Week week);
    @Update
    void update(Week week);
    @Delete
    void delete(Week week);
}
