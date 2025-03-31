package com.example.weightwatchersapp;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Day.class, Week.class}, version = 4)
@TypeConverters({JSONConverter.class, weekJSONConverter.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract DayDao dayDao();
    public abstract WeekDao weekDao();
    private static final ExecutorService databaseExecutor = Executors.newFixedThreadPool(4);
    public static ExecutorService getDatabaseExecutor() {
        return databaseExecutor;
    }
    private static volatile AppDatabase INSTANCE;
    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    AppDatabase.class, "app_database")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
