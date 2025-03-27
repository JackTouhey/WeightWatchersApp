package com.example.weightwatchersapp;

import java.util.ArrayList;
import android.app.Activity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class Controller {
    Activity activity;
    private ArrayList<Week> history = new ArrayList<Week>();
    private Day currentDay;
    private Week currentWeek;
    TextView breakfastPointsDisplay;
    TextView lunchPointsDisplay;
    TextView dinnerPointsDisplay;
    TextView otherPointsDisplay;
    EditText breakfastPointsInput;
    EditText lunchPointsInput;
    EditText dinnerPointsInput;
    EditText otherPointsInput;

    public Controller(Activity activity){
        this.activity = activity;
        currentWeek = new Week();
        currentDay = currentWeek.getCurrentDay();
        setupDayView();
    }
    public Controller(Activity activity, Day currentDay, Week currentWeek, ArrayList<Week> history){
        this.activity = activity;
        this.currentDay = currentDay;
        this.currentWeek = currentWeek;
        this.history = history;
        setupDayView();
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
    public void setupDayView(){
        activity.setContentView(R.layout.activity_main);
        breakfastPointsDisplay = this.activity.findViewById(R.id.breakfastPointDisplay);
        lunchPointsDisplay = this.activity.findViewById(R.id.lunchPointDisplay);
        dinnerPointsDisplay = this.activity.findViewById(R.id.dinnerPointDisplay);
        otherPointsDisplay = this.activity.findViewById(R.id.otherPointDisplay);
        setupDayViewButtons();
        if(currentDay.hasBreakfastPoints()){
            breakfastPointsDisplay.setText(String.valueOf(currentDay.getBreakfastPoints()));
        }
        if(currentDay.hasLunchPoints()){
            lunchPointsDisplay.setText(String.valueOf(currentDay.getLunchPoints()));
        }
        if(currentDay.hasDinnerPoints()){
            dinnerPointsDisplay.setText(String.valueOf(currentDay.getDinnerPoints()));
        }
        if(currentDay.hasOtherPoints()){
            otherPointsDisplay.setText(String.valueOf(currentDay.getOtherPoints()));
        }
    }
    private void setupDayViewButtons(){
        breakfastPointsInput = this.activity.findViewById(R.id.breakfastPointInput);
        lunchPointsInput = this.activity.findViewById(R.id.lunchPointInput);
        dinnerPointsInput = this.activity.findViewById(R.id.dinnerPointInput);
        otherPointsInput = this.activity.findViewById(R.id.otherPointInput);

        breakfastPointsInput.setOnKeyListener(new View.OnKeyListener(){
            public boolean onKey(View v, int keyCode, KeyEvent event){
                if((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)){
                    try{
                        currentDay.setBreakfastPoints(Integer.parseInt(breakfastPointsInput.getText().toString()));
                        breakfastPointsDisplay.setText(String.valueOf(currentDay.getBreakfastPoints()));
                    } catch (NumberFormatException nfe){
                        Log.d("POINTINPUTERROR", "NFE on setting current day breakfast points" + nfe);
                    }
                    return true;
                }
                return false;
            }
        });
        lunchPointsInput.setOnKeyListener(new View.OnKeyListener(){
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)){
                    try{
                        currentDay.setLunchPoints(Integer.parseInt(lunchPointsInput.getText().toString()));
                        lunchPointsDisplay.setText(String.valueOf(currentDay.getLunchPoints()));
                    } catch (NumberFormatException nfe){
                        Log.d("POINTINPUTERROR", "NFE on setting current day lunch points" + nfe);
                    }
                    return true;
                }
                return false;
            }
        });
        dinnerPointsInput.setOnKeyListener(new View.OnKeyListener(){
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)){
                    try{
                        currentDay.setDinnerPoints(Integer.parseInt(dinnerPointsInput.getText().toString()));
                        dinnerPointsDisplay.setText(String.valueOf(currentDay.getDinnerPoints()));
                    } catch (NumberFormatException nfe){
                        Log.d("POINTINPUTERROR", "NFE on setting current day dinner points" + nfe);
                    }
                    return true;
                }
                return false;
            }
        });
        otherPointsInput.setOnKeyListener(new View.OnKeyListener(){
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)){
                    try{
                        currentDay.addOtherPoints(Integer.parseInt(otherPointsInput.getText().toString()));
                        otherPointsDisplay.setText(String.valueOf(currentDay.getOtherPoints()));
                    } catch (NumberFormatException nfe){
                        Log.d("POINTINPUTERROR", "NFE on setting current day other points" + nfe);
                    }
                    return true;
                }
                return false;
            }
        });
    }
}
