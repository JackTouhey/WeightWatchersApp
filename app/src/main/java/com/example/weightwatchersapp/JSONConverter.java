package com.example.weightwatchersapp;

import androidx.room.TypeConverter;
import com.google.gson.Gson;
public class JSONConverter {
    @TypeConverter
    public static String fromDay(Day day){
        Gson gson = new Gson();
        return gson.toJson(day);
    }
    @TypeConverter
    public static Day toDay(String dayString){
        Gson gson = new Gson();
        return gson.fromJson(dayString, Day.class);
    }
}
