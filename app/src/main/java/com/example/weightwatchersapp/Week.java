package com.example.weightwatchersapp;

public class Week {
    private Day monday;
    private Day tuesday;
    private Day wednesday;
    private Day thursday;
    private Day friday;
    private Day saturday;
    private Day sunday;
    private int weeklyPoints;
    private final int dailyLimit = 28;
    public Week(){
        monday = new Day();
        weeklyPoints = 40;
    }
    public Day getCurrentDay(){
        if(sunday != null){
            return sunday;
        }else if(saturday != null){
            return saturday;
        }else if(friday != null){
            return friday;
        }else if(thursday != null){
            return thursday;
        }else if(wednesday != null){
            return wednesday;
        }else if(tuesday != null){
            return tuesday;
        }else{
            return monday;
        }
    }
    public void createNextDay(){
        Day currentDay = getCurrentDay();
        if(currentDay == monday){
            tuesday = new Day();
        }else if(currentDay == tuesday){
            wednesday = new Day();
        }else if(currentDay == wednesday){
            thursday = new Day();
        }else if(currentDay == thursday){
            friday = new Day();
        }else if(currentDay == friday){
            saturday = new Day();
        }else if(currentDay == saturday){
            sunday = new Day();
        }
    }
    public void completeDay(){
        int dayPointDifference = dailyLimit - getCurrentDay().getTotalPoints();
        if(dayPointDifference < 0){
            weeklyPoints -= dayPointDifference;
        }else if(dayPointDifference >= 4){
            weeklyPoints += 4;
        }else if(dayPointDifference > 0){
            weeklyPoints += dayPointDifference;
        }
        createNextDay();
    }
}
