package com.example.weightwatchersapp;

import androidx.room.TypeConverter;
import com.google.gson.Gson;
public class weekJSONConverter {
    @TypeConverter
    public static String fromWeek(Week week){
        Gson gson = new Gson();
        return gson.toJson(week);
    }
    @TypeConverter
    public static Week toDay(String weekString){
        Gson gson = new Gson();
        return gson.fromJson(weekString, Week.class);
    }
}
