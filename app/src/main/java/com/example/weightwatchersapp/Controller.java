package com.example.weightwatchersapp;

import java.util.ArrayList;
import java.util.List;

public class Controller {
    private ArrayList<Week> history = new ArrayList<Week>();
    private Day currentDay;
    private Week currentWeek;

    public Controller(){
        currentWeek = new Week();
        currentDay = currentWeek.getCurrentDay();
    }
}
