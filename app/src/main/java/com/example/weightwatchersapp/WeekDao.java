package com.example.weightwatchersapp;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.ArrayList;

@Dao
public interface WeekDao {
    @Query("SELECT * FROM Weeks")
    ArrayList<Week> getAll();
    @Query("SELECT * FROM Weeks WHERE wId = :weekId")
    Week getWeekById(int weekId);
    @Insert
    void insert(Week week);
    @Update
    void update(Week week);
    @Delete
    void delete(Week week);
}
