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
    @Insert
    long insert(Week week);
    @Update
    void update(Week week);
    @Delete
    void delete(Week week);
}
