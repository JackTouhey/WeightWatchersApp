package com.example.weightwatchersapp;

public class Day {

    private int breakfastPoints;
    private int lunchPoints;
    private int dinnerPoints;
    private int otherPoints;

    public Day(){
        this.otherPoints = 0;
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

