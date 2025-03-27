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
                    } catch (NumberFormatException nfe){
                        Log.d("BREAKFASTPOINTINPUT", "NFE on setting current day breakfast points");
                    }
                    breakfastPointsDisplay.setText(String.valueOf(currentDay.getBreakfastPoints()));
                    return true;
                }
                return false;
            }
        });

    }
}
