package com.example.weightwatchersapp;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

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
    private long currentDayId;
    private Week currentWeek;
    private long currentWeekId;
    private int weeklyPointStart = 40;
    private ArrayList<Day> history = new ArrayList<>();
    private int mondayPoints;
    private int tuesdayPoints;
    private int wednesdayPoints;
    private int thursdayPoints;
    private int fridayPoints;
    private int saturdayPoints;
    private int sundayPoints;
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
    private AppDatabase db;
    private HistoryAdapter adapter;

    public Controller(Activity activity){
        this.activity = activity;
        this.db = AppDatabase.getDatabase(this.activity);
        AppDatabase.getDatabaseExecutor().execute(() ->{
            currentDayId = db.dayDao().getCurrentDId();
            if(currentDayId == 0){
                currentDay = new Day("Monday", currentWeekId + 1);
                currentDayId = db.dayDao().insert(currentDay);
                currentWeek = new Week(weeklyPointStart, currentDayId);
                //currentWeek must be inserted before making Monday otherwise weekId won't be generated and be 0 causing NPE later on
                currentWeekId = db.weekDao().insert(currentWeek);
                currentWeek.setWId(currentWeekId);
                db.weekDao().update(currentWeek);
            }
            else{
                currentWeekId = db.weekDao().getCurrentWId();
                currentDay = db.dayDao().getDayById(currentDayId);
                currentWeek = db.weekDao().getWeekById(currentWeekId);
            }
            activity.runOnUiThread(this::setupDayView);
        });
    }
    private void completeDay(CountDownLatch outerLatch) {
        AppDatabase.getDatabaseExecutor().execute(() -> {
            try {
                Week week = db.weekDao().getWeekById(currentWeekId);
                Day day = db.dayDao().getDayById(currentDayId);

                int dayPointDifference = week.getDailyLimit() - day.getTotalPoints();
                if (dayPointDifference >= 4) {
                    week.setWeeklyPoints(week.getWeeklyPoints() + 4);
                } else {
                    week.setWeeklyPoints((week.getWeeklyPoints() + dayPointDifference));
                }
                CountDownLatch innerLatch = new CountDownLatch(1);
                createNextDay(innerLatch);
                try {
                    innerLatch.await();
                } catch (InterruptedException e) {
                    Log.e("DayFragment", "Waiting for createNextDay interrupted", e);
                    Thread.currentThread().interrupt();
                }
            } finally {
                outerLatch.countDown();
            }
        });
    }
    private void createNextDay(CountDownLatch latch) {
        AppDatabase.getDatabaseExecutor().execute(() -> {
            try {
                Day day = db.dayDao().getDayById(currentDayId);
                Week week = db.weekDao().getWeekById(currentWeekId);
                Day newDay;
                switch (day.getName()) {
                    case "Monday":
                        newDay = new Day("Tuesday", currentWeekId);
                        currentDayId = db.dayDao().insert(newDay);
                        week.setTuesdayId(currentDayId);
                        break;
                    case "Tuesday":
                        newDay = new Day("Wednesday", currentWeekId);
                        currentDayId = db.dayDao().insert(newDay);
                        week.setWednesdayId(currentDayId);
                        break;
                    case "Wednesday":
                        newDay = new Day("Thursday", currentWeekId);
                        currentDayId = db.dayDao().insert(newDay);
                        week.setThursdayId(currentDayId);
                        break;
                    case "Thursday":
                        newDay = new Day("Friday", currentWeekId);
                        currentDayId = db.dayDao().insert(newDay);
                        week.setFridayId(currentDayId);
                        break;
                    case "Friday":
                        newDay = new Day("Saturday", currentWeekId);
                        currentDayId = db.dayDao().insert(newDay);
                        week.setSaturdayId(currentDayId);
                        break;
                    case "Saturday":
                        newDay = new Day("Sunday", currentWeekId);
                        currentDayId = db.dayDao().insert(newDay);
                        week.setSundayId(currentDayId);
                        break;
                    case "Sunday":
                        newDay = new Day("Monday", currentWeekId + 1);
                        currentDayId = db.dayDao().insert(newDay);
                        week = new Week(weeklyPointStart, currentDayId);
                        currentWeekId = db.weekDao().insert(week);
                        break;
                }
                db.weekDao().update(week);
                Log.d("DEBUG", "currentDayId: " + currentDayId + " currentWeekId: " + currentWeekId);
                Log.d("DEBUG", "Current dayId from weekId: " + db.weekDao().getCurrentDayIdFromWeekId(currentWeekId));
            } finally {
                latch.countDown();
            }
        });
    }
    private int getWeeklyPointsAtMonday(Week week){
        AppDatabase.getDatabaseExecutor().execute(() ->{
            int mondayDailyPoints = db.dayDao().getTotalPoints(db.weekDao().getMondayIdFromWeekId(week.getWId()));
            mondayPoints = addDptoWp(mondayDailyPoints, week.getWeeklyPointStart(), week.getDailyLimit());
        });
        return mondayPoints;
    }
    private int getWeeklyPointsAtTuesday(Week week){
        AppDatabase.getDatabaseExecutor().execute(() ->{
            int tuesdayDailyPoints = db.dayDao().getTotalPoints(db.weekDao().getTuesdayIdFromWeekId(week.getWId()));
            tuesdayPoints = addDptoWp(tuesdayDailyPoints, getWeeklyPointsAtMonday(week), week.getDailyLimit());
        });
        return tuesdayPoints;
    }
    private int getWeeklyPointsAtWednesday(Week week){
        AppDatabase.getDatabaseExecutor().execute(() ->{
            int wednesdayDailyPoints = db.dayDao().getTotalPoints(db.weekDao().getWednesdayIdFromWeekId(week.getWId()));
            wednesdayPoints = addDptoWp(wednesdayDailyPoints, getWeeklyPointsAtTuesday(week), week.getDailyLimit());
        });
        return wednesdayPoints;
    }
    private int getWeeklyPointsAtThursday(Week week){
        AppDatabase.getDatabaseExecutor().execute(() ->{
            int thursdayDailyPoints = db.dayDao().getTotalPoints(db.weekDao().getThursdayIdFromWeekId(week.getWId()));
            thursdayPoints = addDptoWp(thursdayDailyPoints, getWeeklyPointsAtWednesday(week), week.getDailyLimit());
        });
        return thursdayPoints;
    }
    private int getWeeklyPointsAtFriday(Week week){
        AppDatabase.getDatabaseExecutor().execute(() ->{
            int fridayDailyPoints = db.dayDao().getTotalPoints(db.weekDao().getFridayIdFromWeekId(week.getWId()));
            fridayPoints = addDptoWp(fridayDailyPoints, getWeeklyPointsAtThursday(week), week.getDailyLimit());
        });
        return fridayPoints;
    }
    private int getWeeklyPointsAtSaturday(Week week){
        AppDatabase.getDatabaseExecutor().execute(() ->{
            int saturdayDailyPoints = db.dayDao().getTotalPoints(db.weekDao().getSaturdayIdFromWeekId(week.getWId()));
            saturdayPoints = addDptoWp(saturdayDailyPoints, getWeeklyPointsAtFriday(week), week.getDailyLimit());
        });
        return saturdayPoints;
    }
    private int getWeeklyPointsAtSunday(Week week){
        AppDatabase.getDatabaseExecutor().execute(() ->{
            int sundayDailyPoints = db.dayDao().getTotalPoints(db.weekDao().getSundayIdFromWeekId(week.getWId()));
            sundayPoints = addDptoWp(sundayDailyPoints, getWeeklyPointsAtSaturday(week), week.getDailyLimit());
        });
        return saturdayPoints;
    }
    public int getWeeklyPointsAtDay(String name, Week week, CountDownLatch latch){
        int weeklyPoints = week.getWeeklyPointStart();
        try{
            switch (name){
                case "Monday":
                    weeklyPoints =  getWeeklyPointsAtMonday(week);
                    break;
                case "Tuesday":
                    weeklyPoints = getWeeklyPointsAtTuesday(week);
                    break;
                case "Wednesday":
                    weeklyPoints = getWeeklyPointsAtWednesday(week);
                    break;
                case "Thursday":
                    weeklyPoints = getWeeklyPointsAtThursday(week);
                    break;
                case "Friday":
                    weeklyPoints = getWeeklyPointsAtFriday(week);
                    break;
                case "Saturday":
                    weeklyPoints = getWeeklyPointsAtSaturday(week);
                    break;
                case "Sunday":
                    weeklyPoints = getWeeklyPointsAtSunday(week);
                    break;
            }
        } finally {
            latch.countDown();
        }
        return weeklyPoints;
    }
    private int addDptoWp(int dp, int wp, int dailyLimit){
        int dailyPointsDifference = dailyLimit - dp;
        if(dailyPointsDifference >= 4 ){
            wp += 4;
        }
        else{
            wp += dailyPointsDifference;
        }
        return wp;
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
        AppDatabase.getDatabaseExecutor().execute(() ->{
            Day day = db.dayDao().getDayById(currentDayId);
            Week week = db.weekDao().getWeekById(currentWeekId);
            boolean hasBreakfast = day.hasBreakfastPoints();
            String breakfastValue = hasBreakfast ?
                    String.valueOf(day.getBreakfastPoints()) : notEntered;

            boolean hasLunch = day.hasLunchPoints();
            String lunchValue = hasLunch ?
                    String.valueOf(day.getLunchPoints()) : notEntered;

            boolean hasDinner = day.hasDinnerPoints();
            String dinnerValue = hasDinner ?
                    String.valueOf(day.getDinnerPoints()) : notEntered;

            boolean hasOther = day.hasOtherPoints();
            String otherValue = hasOther ?
                    String.valueOf(day.getOtherPoints()) : notEntered;

            String currentDayName = day.getName();
            String dailyRemaining = String.valueOf(day.getRemainingPoints());
            String weeklyRemaining = String.valueOf(week.getWeeklyPoints());

            activity.runOnUiThread(() -> {
                breakfastPointsDisplay.setText(breakfastValue);
                lunchPointsDisplay.setText(lunchValue);
                dinnerPointsDisplay.setText(dinnerValue);
                otherPointsDisplay.setText(otherValue);
                currentDayDisplay.setText(currentDayName);
                dailyRemainingPointsDisplay.setText(dailyRemaining);
                weeklyRemainingPointsDisplay.setText(weeklyRemaining);
            });
        });
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
            Integer breakfastPoints = Integer.parseInt(breakfastPointsInput.getText().toString());
            AppDatabase.getDatabaseExecutor().execute(() ->{
                Day day = db.dayDao().getDayById(currentDayId);
                day.setBreakfastPoints(breakfastPoints);
                db.dayDao().update(day);
                currentDay = day;
                updateDisplayValues();
            });
            breakfastPointsInput.setText("");
        } catch (NumberFormatException nfe){
            Log.d("POINTINPUTERROR", "NFE on setting current day breakfast points" + nfe);
        }
    }
    private void addLunch(){
        try{
            Integer lunchPoints = Integer.parseInt(lunchPointsInput.getText().toString());
            AppDatabase.getDatabaseExecutor().execute(() ->{
                Day day = db.dayDao().getDayById(currentDayId);
                day.setLunchPoints(lunchPoints);
                db.dayDao().update(day);
                currentDay = day;
                updateDisplayValues();

            });
            lunchPointsInput.setText("");
        } catch (NumberFormatException nfe){
            Log.d("POINTINPUTERROR", "NFE on setting current day lunch points" + nfe);
        }
    }
    private void addDinner(){
        try{
            Integer dinnerPoints = Integer.parseInt(dinnerPointsInput.getText().toString());
            AppDatabase.getDatabaseExecutor().execute(() ->{
                Day day = db.dayDao().getDayById(currentDayId);
                day.setDinnerPoints(dinnerPoints);
                db.dayDao().update(day);
                currentDay = day;
                updateDisplayValues();
            });
            dinnerPointsInput.setText("");
        } catch (NumberFormatException nfe){
            Log.d("POINTINPUTERROR", "NFE on setting current day dinner points" + nfe);
        }
    }
    private void addOther(){
        try{
            Integer otherPoints = Integer.parseInt(otherPointsInput.getText().toString());
            AppDatabase.getDatabaseExecutor().execute(() ->{
                Day day = db.dayDao().getDayById(currentDayId);
                day.setOtherPoints(otherPoints);
                db.dayDao().update(day);
                currentDay = day;
                updateDisplayValues();
            });
            otherPointsInput.setText("");
        } catch (NumberFormatException nfe){
            Log.d("POINTINPUTERROR", "NFE on setting current day other points" + nfe);
        }
    }
    private void onSubmitDayPress(){
        CountDownLatch latch = new CountDownLatch(1);
        completeDay(latch);
        new Thread(() -> {
            try {
                latch.await();
                updateDisplayValues();
            } catch (InterruptedException e) {
                Log.e("DayFragment", "Waiting interrupted", e);
                Thread.currentThread().interrupt();
            }
        }).start();
    }
    private void onAddBeerClick(){
        AppDatabase.getDatabaseExecutor().execute(() ->{
            Day day = db.dayDao().getDayById(currentDayId);
            day.addBeer();
            db.dayDao().update(day);
            currentDay = day;
        });
        updateDisplayValues();
        if(currentDay.getBeerCount() > (int)(Math.floor(Math.random() * 8) + 4)){
            //Insult
        }
    }
    private void onAddAllClick() {
        try {
            Integer breakfastPoints = null;
            Integer lunchPoints = null;
            Integer dinnerPoints = null;
            Integer otherPoints = null;

            if (!breakfastPointsInput.getText().toString().isEmpty()) {
                breakfastPoints = Integer.parseInt(breakfastPointsInput.getText().toString());
            }
            if (!lunchPointsInput.getText().toString().isEmpty()) {
                lunchPoints = Integer.parseInt(lunchPointsInput.getText().toString());
            }
            if (!dinnerPointsInput.getText().toString().isEmpty()) {
                dinnerPoints = Integer.parseInt(dinnerPointsInput.getText().toString());
            }
            if (!otherPointsInput.getText().toString().isEmpty()) {
                otherPoints = Integer.parseInt(otherPointsInput.getText().toString());
            }

            final Integer finalBreakfastPoints = breakfastPoints;
            final Integer finalLunchPoints = lunchPoints;
            final Integer finalDinnerPoints = dinnerPoints;
            final Integer finalOtherPoints = otherPoints;

            AppDatabase.getDatabaseExecutor().execute(() -> {
                Day day = db.dayDao().getDayById(currentDayId);

                if (finalBreakfastPoints != null) {
                    day.setBreakfastPoints(finalBreakfastPoints);
                }
                if (finalLunchPoints != null) {
                    day.setLunchPoints(finalLunchPoints);
                }
                if (finalDinnerPoints != null) {
                    day.setDinnerPoints(finalDinnerPoints);
                }
                if (finalOtherPoints != null) {
                    day.setOtherPoints(finalOtherPoints);
                }
                db.dayDao().update(day);
                currentDay = day;
                updateDisplayValues();
            });

            breakfastPointsInput.setText("");
            lunchPointsInput.setText("");
            dinnerPointsInput.setText("");
            otherPointsInput.setText("");

        } catch (NumberFormatException nfe) {
            Log.d("POINTINPUTERROR", "NFE on setting day points: " + nfe);
        }
    }
    private void onHistoryClick(){
        activity.setContentView(R.layout.history_view);
        AppDatabase.getDatabaseExecutor().execute(() ->{
            adapter = new HistoryAdapter(db.dayDao().getAll(), this.activity, this);
        });
        historyRecyclerView = this.activity.findViewById(R.id.historyRecyclerView);
        historyRecyclerView.setLayoutManager(new LinearLayoutManager(this.activity));
        historyRecyclerView.setAdapter(adapter);
        homeButton = this.activity.findViewById(R.id.homeButton);

        homeButton.setOnClickListener(e->{
            setupDayView();
        });
    }
}
