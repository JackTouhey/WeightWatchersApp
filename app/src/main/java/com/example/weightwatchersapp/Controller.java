package com.example.weightwatchersapp;

import java.util.ArrayList;
import java.util.List;
import android.app.Activity;

public class Controller {
    Activity activity;
    private ArrayList<Week> history = new ArrayList<Week>();
    private Day currentDay;
    private Week currentWeek;

    public Controller(Activity activity){
        this.activity = activity;
        currentWeek = new Week();
        currentDay = currentWeek.getCurrentDay();
    }
    public ArrayList<Week> getHistory(){
        return this.history;
    }
    public Day getCurrentDay(){
        return this.currentDay;
    }
    public Week getCurrentWeek() {
        return currentWeek;
    }

    public void nextDay(){
        currentWeek.completeDay();
        if(isSunday()){
            history.add(currentWeek);
            currentWeek = new Week();
        }
        currentDay = currentWeek.getCurrentDay();
    }
    public boolean isSunday(){
        return currentDay.getName().equals("Sunday");
    }
}
