package com.example.weightwatchersapp;

import java.util.ArrayList;
import android.app.Activity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class Controller {
    Activity activity;
    private Day currentDay;
    private int currentDayId;
    private Week currentWeek;
    private int currentWeekId;
    TextView breakfastPointsDisplay;
    TextView lunchPointsDisplay;
    TextView dinnerPointsDisplay;
    TextView otherPointsDisplay;
    TextView currentDayDisplay;
    TextView dailyRemainingPointsDisplay;
    TextView weeklyRemainingPointsDisplay;
    EditText breakfastPointsInput;
    EditText lunchPointsInput;
    EditText dinnerPointsInput;
    EditText otherPointsInput;
    Button submitDay;
    Button addBeer;
    Button addAll;
    Button historyButton;
    Button homeButton;
    RecyclerView historyRecyclerView;
    private final String notEntered = "Not Entered";
    private AppDatabase db = AppDatabase.getDatabase(this.activity);

    public Controller(Activity activity){
        this.activity = activity;
        currentWeek = new Week();
        currentWeekId = db.weekDao().insert(currentWeek);
        currentDay = currentWeek.getCurrentDay();
        currentDayId = db.dayDao().insert(currentDay);
        setupDayView();
    }
    public Controller(Activity activity, int currentDayId, int currentWeekId){
        this.activity = activity;
        this.currentDayId = currentDayId;
        this.currentDay = db.dayDao().getDayById(currentDayId);
        this.currentWeekId = currentWeekId;
        this.currentWeek = db.weekDao().getWeekById(currentWeekId);
        setupDayView();
    }
    public ArrayList<Day> getHistory(){
        return db.dayDao().getAll();
    }
    public Day getCurrentDay(){
        return this.currentDay;
    }
    public Week getCurrentWeek() {
        return currentWeek;
    }
    private void nextDay(){
        currentWeek.completeDay();
        if(isSunday()){
            currentWeek = new Week();
            currentWeekId = db.weekDao().insert(currentWeek);
        }
        currentDay = currentWeek.getCurrentDay();
        currentDayId = db.dayDao().insert(currentDay);
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
        currentDayDisplay = this.activity.findViewById(R.id.currentDayDisplay);
        dailyRemainingPointsDisplay = this.activity.findViewById(R.id.remainingDailyPointsDisplay);
        weeklyRemainingPointsDisplay = this.activity.findViewById(R.id.remainingWeeklyPointsDisplay);

        updateDisplayValues();
        setupDayViewButtons();
    }
    private void updateDisplayValues(){
        if(currentDay.hasBreakfastPoints()){
            breakfastPointsDisplay.setText(String.valueOf(currentDay.getBreakfastPoints()));
        }else{
            breakfastPointsDisplay.setText(notEntered);
        }
        if(currentDay.hasLunchPoints()){
            lunchPointsDisplay.setText(String.valueOf(currentDay.getLunchPoints()));
        }else{
            lunchPointsDisplay.setText(notEntered);
        }
        if(currentDay.hasDinnerPoints()){
            dinnerPointsDisplay.setText(String.valueOf(currentDay.getDinnerPoints()));
        }else{
            dinnerPointsDisplay.setText(notEntered);
        }
        if(currentDay.hasOtherPoints()){
            otherPointsDisplay.setText(String.valueOf(currentDay.getOtherPoints()));
        }else{
            otherPointsDisplay.setText(notEntered);
        }
        currentDayDisplay.setText(currentDay.getName());
        dailyRemainingPointsDisplay.setText(String.valueOf(currentDay.getRemainingPoints()));
        weeklyRemainingPointsDisplay.setText(String.valueOf(currentWeek.getWeeklyPoints()));
    }
    private void setupDayViewButtons(){
        breakfastPointsInput = this.activity.findViewById(R.id.breakfastPointInput);
        lunchPointsInput = this.activity.findViewById(R.id.lunchPointInput);
        dinnerPointsInput = this.activity.findViewById(R.id.dinnerPointInput);
        otherPointsInput = this.activity.findViewById(R.id.otherPointInput);
        submitDay = this.activity.findViewById(R.id.submitDayButton);
        addBeer = this.activity.findViewById(R.id.addBeerButton);
        addAll = this.activity.findViewById(R.id.addAllButton);
        historyButton = this.activity.findViewById(R.id.historyButton);


        breakfastPointsInput.setOnKeyListener(new View.OnKeyListener(){
            public boolean onKey(View v, int keyCode, KeyEvent event){
                if((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)){
                    addBreakfast();
                    return true;
                }
                return false;
            }
        });
        lunchPointsInput.setOnKeyListener(new View.OnKeyListener(){
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)){
                    addLunch();
                    return true;
                }
                return false;
            }
        });
        dinnerPointsInput.setOnKeyListener(new View.OnKeyListener(){
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)){
                    addDinner();
                    return true;
                }
                return false;
            }
        });
        otherPointsInput.setOnKeyListener(new View.OnKeyListener(){
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)){
                    addOther();
                    return true;
                }
                return false;
            }
        });
        submitDay.setOnClickListener(e->{
            onSubmitDayPress();
        });
        addBeer.setOnClickListener(e->{
            onAddBeerClick();
        });
        addAll.setOnClickListener(e->{
            onAddAllClick();
        });
        historyButton.setOnClickListener(e->{
            onHistoryClick();
        });
    }
    private void addBreakfast(){
        try{
            currentDay.setBreakfastPoints(Integer.parseInt(breakfastPointsInput.getText().toString()));
            breakfastPointsDisplay.setText(String.valueOf(currentDay.getBreakfastPoints()));
            updateDisplayValues();
            breakfastPointsInput.setText("");
        } catch (NumberFormatException nfe){
            Log.d("POINTINPUTERROR", "NFE on setting current day breakfast points" + nfe);
        }
    }
    private void addLunch(){
        try{
            currentDay.setLunchPoints(Integer.parseInt(lunchPointsInput.getText().toString()));
            lunchPointsDisplay.setText(String.valueOf(currentDay.getLunchPoints()));
            updateDisplayValues();
            lunchPointsInput.setText("");
        } catch (NumberFormatException nfe){
            Log.d("POINTINPUTERROR", "NFE on setting current day lunch points" + nfe);
        }
    }
    private void addDinner(){
        try{
            currentDay.setDinnerPoints(Integer.parseInt(dinnerPointsInput.getText().toString()));
            dinnerPointsDisplay.setText(String.valueOf(currentDay.getDinnerPoints()));
            updateDisplayValues();
            dinnerPointsInput.setText("");
        } catch (NumberFormatException nfe){
            Log.d("POINTINPUTERROR", "NFE on setting current day dinner points" + nfe);
        }
    }
    private void addOther(){
        try{
            currentDay.addOtherPoints(Integer.parseInt(otherPointsInput.getText().toString()));
            otherPointsDisplay.setText(String.valueOf(currentDay.getOtherPoints()));
            updateDisplayValues();
            otherPointsInput.setText("");
        } catch (NumberFormatException nfe){
            Log.d("POINTINPUTERROR", "NFE on setting current day other points" + nfe);
        }
    }
    private void onSubmitDayPress(){
        nextDay();
        updateDisplayValues();
    }
    private void onAddBeerClick(){
        currentDay.addBeer();
        updateDisplayValues();
        if(currentDay.getBeerCount() > (int)(Math.floor(Math.random() * 8) + 4)){
            //Insult
        }
    }
    private void onAddAllClick(){
        addBreakfast();
        addLunch();
        addDinner();
        addOther();
    }
    private void onHistoryClick(){
        activity.setContentView(R.layout.history_view);
        historyRecyclerView = this.activity.findViewById(R.id.historyRecyclerView);
        historyRecyclerView.setLayoutManager(new LinearLayoutManager(this.activity));
        HistoryAdapter adapter = new HistoryAdapter(db.dayDao().getAll());
        historyRecyclerView.setAdapter(adapter);
        homeButton = this.activity.findViewById(R.id.homeButton);

        homeButton.setOnClickListener(e->{
            setupDayView();
        });
    }
}
