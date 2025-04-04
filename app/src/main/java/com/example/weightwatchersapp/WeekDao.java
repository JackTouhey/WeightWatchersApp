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
    default Long getCurrentDayIdFromWeekId(Long weekId){
        if(getSundayIdFromWeekId(weekId) != null ){
            return getSundayIdFromWeekId(weekId);
        }
        else if(getSaturdayIdFromWeekId(weekId) != null){
            return getSaturdayIdFromWeekId(weekId);
        }
        else if(getFridayIdFromWeekId(weekId) != null){
            return getFridayIdFromWeekId(weekId);
        }
        else if(getThursdayIdFromWeekId(weekId) != null){
            return getThursdayIdFromWeekId(weekId);
        }
        else if(getWednesdayIdFromWeekId(weekId) != null){
            return getWednesdayIdFromWeekId(weekId);
        }
        else if(getTuesdayIdFromWeekId(weekId) != null){
            return getTuesdayIdFromWeekId(weekId);
        }
        else{
            Log.d("DEBUG", "Returning Monday ID, current value: " + getMondayIdFromWeekId(weekId) + " weekIdValue: " + weekId);
            return getMondayIdFromWeekId(weekId);
        }
    }
    @Query("SELECT * FROM Weeks WHERE wId = :weekId")
    Week getWeekById(long weekId);
    @Query("SELECT MAX(wId) FROM Weeks")
    long getCurrentWId();
    @Query("SELECT DISTINCT mondayId FROM Weeks WHERE wId = :weekId")
    Long getMondayIdFromWeekId(Long weekId);
    @Query("SELECT DISTINCT tuesdayId FROM Weeks WHERE wId = :weekId")
    Long getTuesdayIdFromWeekId(Long weekId);
    @Query("SELECT DISTINCT wednesdayId FROM Weeks WHERE wId = :weekId")
    Long getWednesdayIdFromWeekId(Long weekId);
    @Query("SELECT DISTINCT thursdayId FROM Weeks WHERE wId = :weekId")
    Long getThursdayIdFromWeekId(Long weekId);
    @Query("SELECT DISTINCT fridayId FROM Weeks WHERE wId = :weekId")
    Long getFridayIdFromWeekId(Long weekId);
    @Query("SELECT DISTINCT saturdayId FROM Weeks WHERE wId = :weekId")
    Long getSaturdayIdFromWeekId(Long weekId);
    @Query("SELECT DISTINCT sundayId FROM Weeks WHERE wId = :weekId")
    Long getSundayIdFromWeekId(Long weekId);
    @Insert
    long insert(Week week);
    @Update
    void update(Week week);
    @Delete
    void delete(Week week);
}
