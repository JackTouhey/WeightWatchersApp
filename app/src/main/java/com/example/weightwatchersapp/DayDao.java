package com.example.weightwatchersapp;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.ArrayList;

@Dao
public interface DayDao {
    @Query("SELECT * FROM Days")
    ArrayList<Day> getAll();
    @Query("SELECT * FROM Days WHERE dId = :dayId")
    Day getDayById(int dayId);
    @Insert
    int insert(Day day);
    @Update
    void update(Day day);
    void delete(Day day);

}
