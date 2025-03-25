package com.example.weightwatchersapp;

public class Day {

    private int breakfastPoints;
    private int lunchPoints;
    private int dinnerPoints;
    private int otherPoints = 0;
    private String name;

    public Day(){
    }
    public Day(String name){
        this.name = name;
    }
    public String getName(){
        return this.name;
    }
    public void setBreakfastPoints(int breakfastPoints) {
        this.breakfastPoints = breakfastPoints;
    }
    public int getBreakfastPoints(){
        return this.breakfastPoints;
    }
    public void setLunchPoints(int lunchPoints){
        this.lunchPoints = lunchPoints;
    }
    public int getLunchPoints(){
        return this.lunchPoints;
    }
    public void setDinnerPoints(int dinnerPoints){
        this.dinnerPoints = dinnerPoints;
    }
    public int getDinnerPoints(){
        return this.dinnerPoints;
    }
    public void addOtherPoints(int otherPoints){
        this.otherPoints += otherPoints;
    }
    public int getOtherPoints(){
        return this.otherPoints;
    }
    public int getTotalPoints(){
        return breakfastPoints + lunchPoints + dinnerPoints + otherPoints;
    }
}

