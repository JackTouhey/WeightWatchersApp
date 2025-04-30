package com.example.weightwatchersapp;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface QuickAddDao {
    @Query("SELECT * FROM QuickAdd WHERE quickAddID = :quickAddID")
    QuickAdd getQuickAddById(long quickAddID);    @Insert
    long insert(QuickAdd quickAdd);
    @Update
    void update(QuickAdd quickAdd);
}
