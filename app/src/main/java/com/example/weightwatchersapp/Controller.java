package com.example.weightwatchersapp;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import android.app.Activity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class Controller {
    Activity activity;
    private Day currentDay;
    private long currentDayId;
    private Week currentWeek;
    private long currentWeekId;
    private int weeklyPointStart = 40;
    private int currentDailyPoints = 28;
    private int currentPage;
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
    EditText changeDailyPointsInput;
    EditText changeWeeklyPointsInput;
    Button submitDay;
    Button addBeer;
    Button addAll;
    Button historyButton;
    Button historyHomeButton;
    Button settingsButton;
    Button settingsHomeButton;
    TextView editHistoryBreakfastInput;
    TextView editHistoryLunchInput;
    TextView editHistoryDinnerInput;
    TextView editHistoryOtherInput;
    Button editHistorySubmitButton;
    Button quickAddButtonOne;
    Button quickAddOneSettings;
    Button quickAddButtonTwo;
    Button quickAddTwoSettings;
    Button quickAddButtonThree;
    Button quickAddThreeSettings;
    EditText editQuickAddName;
    EditText editQuickAddBreakfastPoints;
    EditText editQuickAddLunchPoints;
    EditText editQuickAddDinnerPoints;
    EditText editQuickAddOtherPoints;
    Button submitEditQuickAdd;
    Button editQuickAddHome;
    RecyclerView historyRecyclerView;
    private final String notEntered = "Not Entered";
    private final AppDatabase db;
    private HistoryAdapter adapter;
    String[] beerInsults = new String[]{
            "Whoa there big man",
            "Looks like someone needs a taxi",
            "Achievement unlocked: #1 Waitoa supporter",
            "Buy me one next time ;)",
            "I hope the brews are worth it",
            "One more and I'm telling mum",
            "Would Monty be proud of this decision?",
            "This better be homebrew",
            "Keep this up and I'll rename the app 'Waist Watchers'",
            "Is your blood type now IPA?",
            "Hydration is sexy",
            "Why not just skip the foreplay and buy a keg",
            "Bet you can't rip out 20 pushups right now",
            "Your beer to dad joke ratio is getting dangerously unbalanced",
            "Mum is so sleeping in the spare room tonight",
            "Have you broken the seal yet?",
            "If beer was sexiness you'd be Mr New Zealand",
            "I see you're maintaining your buddha like figure"};
    public int getCurrentPage(){return this.currentPage;}
    public Controller(Activity activity, int currentPage) {
        this.activity = activity;
        this.db = AppDatabase.getDatabase(this.activity);
        this.currentPage = currentPage;
        setupQuickAddObjects();
        if (!AppDatabase.getDatabaseExecutor().isShutdown()) {
            AppDatabase.getDatabaseExecutor().execute(() -> {
                try {
                    currentDayId = db.dayDao().getCurrentDId();
                    if (currentDayId == 0) {
                        currentDay = new Day("Monday", currentWeekId + 1, currentDailyPoints);
                        currentDayId = db.dayDao().insert(currentDay);
                        currentWeek = new Week(weeklyPointStart);
                        currentWeekId = db.weekDao().insert(currentWeek);
                        currentWeek.setWId(currentWeekId);
                        db.weekDao().update(currentWeek);
                    } else {
                        currentWeekId = db.weekDao().getCurrentWId();
                        currentDay = db.dayDao().getDayById(currentDayId);
                        currentWeek = db.weekDao().getWeekById(currentWeekId);
                    }
                    if (!activity.isFinishing() && !activity.isDestroyed()) {
                        activity.runOnUiThread(() -> {
                            if (this.currentPage == 1) {
                                setupHomePage();
                            } else if (this.currentPage == 2) {
                                setupHistoryPage();
                            } else if (this.currentPage == 3) {
                                setupSettingsPage();
                            }
                        });
                    }
                } catch (Exception e) {
                    Log.d("CONSTRUCTOR ERROR", e.toString());
                }
            });
        }
    }
    private void setupQuickAddObjects(){
        AppDatabase.getDatabaseExecutor().execute(()->{
            QuickAdd quickAddOne = db.quickAddDao().getQuickAddById(1);
            QuickAdd quickAddTwo = db.quickAddDao().getQuickAddById(2);
            QuickAdd quickAddThree = db.quickAddDao().getQuickAddById(3);
            if (quickAddOne == null){
                quickAddOne = new QuickAdd();
                db.quickAddDao().insert(quickAddOne);
            }
            if (quickAddTwo == null){
                quickAddTwo = new QuickAdd();
                db.quickAddDao().insert(quickAddTwo);
            }
            if (quickAddThree == null){
                quickAddThree = new QuickAdd();
                db.quickAddDao().insert(quickAddThree);
            }
        });
    }
    private void completeDay(CountDownLatch outerLatch) {
        AppDatabase.getDatabaseExecutor().execute(() -> {
            try {
                Week week = db.weekDao().getWeekById(currentWeekId);
                Day day = db.dayDao().getDayById(currentDayId);
                int dayPointDifference = day.getRemainingPoints();
                if (dayPointDifference >= 4) {
                    week.setWeeklyPoints(week.getWeeklyPoints() + 4);
                } else {
                    week.setWeeklyPoints((week.getWeeklyPoints() + dayPointDifference));
                }
                db.weekDao().update(week);
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
                int currentWeeklyPoints = week.getWeeklyPoints();
                switch (day.getName()) {
                    case "Monday":
                        newDay = new Day("Tuesday", currentWeekId, currentDailyPoints);
                        currentDayId = db.dayDao().insert(newDay);
                        week.setMondayWP(currentWeeklyPoints);
                        break;
                    case "Tuesday":
                        newDay = new Day("Wednesday", currentWeekId, currentDailyPoints);
                        currentDayId = db.dayDao().insert(newDay);
                        week.setTuesdayWP(currentWeeklyPoints);
                        break;
                    case "Wednesday":
                        newDay = new Day("Thursday", currentWeekId, currentDailyPoints);
                        currentDayId = db.dayDao().insert(newDay);
                        week.setWednesdayWP(currentWeeklyPoints);
                        break;
                    case "Thursday":
                        newDay = new Day("Friday", currentWeekId, currentDailyPoints);
                        currentDayId = db.dayDao().insert(newDay);
                        week.setThursdayWP(currentWeeklyPoints);
                        break;
                    case "Friday":
                        newDay = new Day("Saturday", currentWeekId, currentDailyPoints);
                        currentDayId = db.dayDao().insert(newDay);
                        week.setFridayWP(currentWeeklyPoints);
                        break;
                    case "Saturday":
                        newDay = new Day("Sunday", currentWeekId, currentDailyPoints);
                        currentDayId = db.dayDao().insert(newDay);
                        week.setSaturdayWP(currentWeeklyPoints);
                        break;
                    case "Sunday":
                        newDay = new Day("Monday", currentWeekId + 1, currentDailyPoints);
                        currentDayId = db.dayDao().insert(newDay);
                        week.setSundayWP(currentWeeklyPoints);
                        db.weekDao().update(week);
                        week = new Week(weeklyPointStart);
                        currentWeekId = db.weekDao().insert(week);
                        break;
                }
                db.weekDao().update(week);
            } finally {
                latch.countDown();
            }
        });
    }
    public void setupHomePage(){
        this.currentPage = 1;
        activity.setContentView(R.layout.activity_main);
        breakfastPointsDisplay = this.activity.findViewById(R.id.breakfastPointDisplay);
        lunchPointsDisplay = this.activity.findViewById(R.id.lunchPointDisplay);
        dinnerPointsDisplay = this.activity.findViewById(R.id.dinnerPointDisplay);
        otherPointsDisplay = this.activity.findViewById(R.id.otherPointDisplay);
        currentDayDisplay = this.activity.findViewById(R.id.currentDayDisplay);
        dailyRemainingPointsDisplay = this.activity.findViewById(R.id.remainingDailyPointsDisplay);
        weeklyRemainingPointsDisplay = this.activity.findViewById(R.id.remainingWeeklyPointsDisplay);
        quickAddButtonOne = this.activity.findViewById(R.id.quickAddButtonOne);
        quickAddOneSettings = this.activity.findViewById(R.id.quickAddOneSettings);
        quickAddButtonTwo = this.activity.findViewById(R.id.quickAddButtonTwo);
        quickAddTwoSettings = this.activity.findViewById(R.id.quickAddTwoSettings);
        quickAddButtonThree = this.activity.findViewById(R.id.quickAddButtonThree);
        quickAddThreeSettings = this.activity.findViewById(R.id.quickAddThreeSettings);

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
    //some change
    private void setupDayViewButtons(){
        breakfastPointsInput = this.activity.findViewById(R.id.breakfastPointInput);
        lunchPointsInput = this.activity.findViewById(R.id.lunchPointInput);
        dinnerPointsInput = this.activity.findViewById(R.id.dinnerPointInput);
        otherPointsInput = this.activity.findViewById(R.id.otherPointInput);
        submitDay = this.activity.findViewById(R.id.submitDayButton);
        addBeer = this.activity.findViewById(R.id.addBeerButton);
        addAll = this.activity.findViewById(R.id.addAllButton);
        historyButton = this.activity.findViewById(R.id.historyButton);
        settingsButton = this.activity.findViewById(R.id.settingsButton);
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
            setupHistoryPage();
        });
        settingsButton.setOnClickListener(e ->{
            setupSettingsPage();
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
                day.addOtherPoints(otherPoints);
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
        CountDownLatch latch = new CountDownLatch(1);
        AppDatabase.getDatabaseExecutor().execute(() ->{
            Day day = db.dayDao().getDayById(currentDayId);
            day.addBeer();
            db.dayDao().update(day);
            currentDay = day;
            latch.countDown();
        });
        try{
            latch.await();
        } catch (InterruptedException e) {
            Log.e("DayFragment", "Waiting interrupted", e);
            Thread.currentThread().interrupt();
        }
        updateDisplayValues();

        if(currentDay.getBeerCount() > 4){
            int randomIndex = (int)(Math.floor(Math.random() * beerInsults.length));
            Toast toast = Toast.makeText(this.activity, beerInsults[randomIndex], Toast.LENGTH_SHORT);
            toast.show();
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
                    day.addOtherPoints(finalOtherPoints);
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
    private void setupHistoryPage(){
        this.currentPage = 2;
        CountDownLatch latch = new CountDownLatch(1);
        AppDatabase.getDatabaseExecutor().execute(() ->{
            ArrayList<Day> history = new ArrayList<>(db.dayDao().getAll());
            history.remove((int)db.dayDao().getCurrentDId()-1);
            adapter = new HistoryAdapter(history, this.activity, this);
            latch.countDown();
        });
        try{
            latch.await();
            activity.setContentView(R.layout.history_view);
            historyRecyclerView = this.activity.findViewById(R.id.historyRecyclerView);
            historyRecyclerView.setLayoutManager(new LinearLayoutManager(this.activity));
            historyRecyclerView.setAdapter(adapter);
            historyHomeButton = this.activity.findViewById(R.id.homeButton);
            historyHomeButton.setOnClickListener(e->{
                setupHomePage();
            });
        } catch (InterruptedException e) {
            Log.e("DayFragment", "Waiting interrupted", e);
            Thread.currentThread().interrupt();
        }
    }
    private void setupSettingsPage(){
        this.currentPage = 3;
        activity.setContentView(R.layout.settings_page);
        changeDailyPointsInput = this.activity.findViewById(R.id.change_dp_input);
        changeWeeklyPointsInput = this.activity.findViewById(R.id.change_wp_input);
        settingsHomeButton = this.activity.findViewById(R.id.settingsHomeButton);
        changeDailyPointsInput.setOnKeyListener(new View.OnKeyListener(){
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)){
                    Integer newDailyPoints = Integer.parseInt(changeDailyPointsInput.getText().toString());
                    changeDailyPoints(newDailyPoints);
                    changeDailyPointsInput.setText("");
                    return true;
                }
                return false;
            }
        });
        changeWeeklyPointsInput.setOnKeyListener(new View.OnKeyListener(){
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)){
                    Integer newWeeklyPoints = Integer.parseInt(changeWeeklyPointsInput.getText().toString());
                    changeWeeklyPoints(newWeeklyPoints);
                    changeWeeklyPointsInput.setText("");
                    return true;
                }
                return false;
            }
        });
        settingsHomeButton.setOnClickListener(e->{
            setupHomePage();
        });
    }
    private void changeDailyPoints(Integer newDailyPoints){
        this.currentDailyPoints = newDailyPoints;
        Toast toast = Toast.makeText(this.activity, "Daily points set to " + currentDailyPoints +
                ". Please note this will not take effect until the next day", Toast.LENGTH_SHORT);
        toast.show();
    }
    private void changeWeeklyPoints(Integer newWeeklyPoints){
        this.weeklyPointStart = newWeeklyPoints;
        Toast toast = Toast.makeText(this.activity, "Weekly points set to " + weeklyPointStart +
                ". Please note this will not take effect until the next week", Toast.LENGTH_SHORT);
        toast.show();
    }
    public void setupEditHistory(long dayId){
        activity.setContentView(R.layout.edit_history);
        editHistoryBreakfastInput = this.activity.findViewById(R.id.editHistoryBreakfastInput);
        editHistoryLunchInput = this.activity.findViewById(R.id.editHistoryLunchInput);
        editHistoryDinnerInput = this.activity.findViewById(R.id.editHistoryDinnerInput);
        editHistoryOtherInput = this.activity.findViewById(R.id.editHistoryOtherInput);
        editHistorySubmitButton = this.activity.findViewById(R.id.editHistorySubmitButton);
        setEditHistoryButtons(dayId);
        CountDownLatch latch = new CountDownLatch(1);
        setEditHistoryInputTexts(dayId, latch);
        try{
            latch.await();
        } catch (InterruptedException e) {
            Log.e("DayFragment", "Waiting interrupted", e);
            Thread.currentThread().interrupt();
        }
    }
    private void setEditHistoryButtons(long dayId){
        editHistoryBreakfastInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    editHistoryBreakfastInput.setText("");
                }
            }
        });
        editHistoryLunchInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    editHistoryLunchInput.setText("");
                }
            }
        });
        editHistoryDinnerInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    editHistoryDinnerInput.setText("");
                }
            }
        });
        editHistoryOtherInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    editHistoryOtherInput.setText("");
                }
            }
        });
        editHistorySubmitButton.setOnClickListener(e->{
            submitEditHistory(dayId);
        });
    }
    private void submitEditHistory(long dayId){
        CountDownLatch latch = new CountDownLatch(1);
        Log.d("DEBUG", "Submit edit history dayId: " + dayId);
        AppDatabase.getDatabaseExecutor().execute(()->{
            Day day = db.dayDao().getDayById(dayId);
            Week week = db.weekDao().getWeekById(day.getWeekId());
            try{
                String breakfastText = editHistoryBreakfastInput.getText().toString();
                String lunchText = editHistoryLunchInput.getText().toString();
                String dinnerText = editHistoryDinnerInput.getText().toString();
                String otherText = editHistoryOtherInput.getText().toString();
                if(!breakfastText.equals("Not entered")){
                    day.setBreakfastPoints(Integer.parseInt(breakfastText));
                }
                if(!lunchText.equals("Not entered")){
                    day.setLunchPoints(Integer.parseInt(lunchText));
                }
                if(!dinnerText.equals("Not entered")){
                    day.setDinnerPoints(Integer.parseInt(dinnerText));
                }
                if(!otherText.equals("Not entered")){
                    day.setOtherPoints(Integer.parseInt(otherText));
                }
            } catch (NumberFormatException e){
                Log.d("DEBUG", "NFE: " + e);
            }
            db.dayDao().update(day);
            CountDownLatch innerLatch = new CountDownLatch(1);
            updateWeeklyPoints(dayId, week.getWId(), innerLatch);
            try{
                innerLatch.await();
            } catch (InterruptedException e) {
                Log.e("DayFragment", "Waiting interrupted", e);
                Thread.currentThread().interrupt();
            }
            latch.countDown();
        });
        try{
            latch.await();
            setupHistoryPage();
        } catch (InterruptedException e) {
            Log.e("DayFragment", "Waiting interrupted", e);
            Thread.currentThread().interrupt();
        }
    }
    private void updateWeeklyPoints(long dayId, long weekId, CountDownLatch latch){
        AppDatabase.getDatabaseExecutor().execute(()->{
            Day day = db.dayDao().getDayById(dayId);
            Week week = db.weekDao().getWeekById(weekId);
            int dayPointDifference = day.getRemainingPoints();
            int previousWP;
            Boolean isSunday = false;
            if (dayId == 1){
                previousWP = weeklyPointStart;
                if(dayPointDifference >= 4){
                    week.setMondayWP(previousWP + 4);
                }
                else{
                    week.setMondayWP(previousWP + dayPointDifference);
                }
            }
            else{
                switch (day.getName()){
                    case "Monday":
                        previousWP = db.weekDao().getSundayWPFromWeekId(weekId - 1);
                        if(dayPointDifference >= 4){
                            week.setMondayWP(previousWP + 4);
                        }
                        else{
                            week.setMondayWP(previousWP + dayPointDifference);
                        }
                        break;
                    case "Tuesday":
                        previousWP = week.getMondayWP();
                        if(dayPointDifference >= 4){
                            week.setTuesdayWP(previousWP + 4);
                        }
                        else{
                            week.setTuesdayWP(previousWP + dayPointDifference);
                        }
                        break;
                    case "Wednesday":
                        previousWP = week.getTuesdayWP();
                        if(dayPointDifference >= 4){
                            week.setWednesdayWP(previousWP + 4);
                        }
                        else{
                            week.setWednesdayWP(previousWP + dayPointDifference);
                        }
                        break;
                    case "Thursday":
                        previousWP = week.getWednesdayWP();
                        if(dayPointDifference >= 4){
                            week.setThursdayWP(previousWP + 4);
                        }
                        else{
                            week.setThursdayWP(previousWP + dayPointDifference);
                        }
                        break;
                    case "Friday":
                        previousWP = week.getThursdayWP();
                        if(dayPointDifference >= 4){
                            week.setFridayWP(previousWP + 4);
                        }
                        else{
                            week.setFridayWP(previousWP + dayPointDifference);
                        }
                        break;
                    case "Saturday":
                        previousWP = week.getFridayWP();
                        if(dayPointDifference >= 4){
                            week.setSaturdayWP(previousWP + 4);
                        }
                        else{
                            week.setSaturdayWP(previousWP + dayPointDifference);
                        }
                        break;
                    case "Sunday":
                        previousWP = week.getSaturdayWP();
                        if(dayPointDifference >= 4){
                            week.setSundayWP(previousWP + 4);
                        }
                        else{
                            week.setSundayWP(previousWP + dayPointDifference);
                        }
                        isSunday = true;
                        break;
                }
            }
            db.weekDao().update(week);
            if(!isSunday && dayId < db.dayDao().getCurrentDId()){
                CountDownLatch innerLatch = new CountDownLatch(1);
                updateWeeklyPoints(dayId + 1, weekId, innerLatch);
                try{
                    innerLatch.await();
                } catch (InterruptedException e) {
                    Log.e("DayFragment", "Waiting interrupted", e);
                    Thread.currentThread().interrupt();
                }
            }
            latch.countDown();
        });
    }
    private void setEditHistoryInputTexts(long dayId, CountDownLatch latch){
        AppDatabase.getDatabaseExecutor().execute(()->{
            Integer breakfastPoints = db.dayDao().getBreakfastPoints(dayId);
            Integer lunchPoints = db.dayDao().getLunchPoints(dayId);
            Integer dinnerPoints = db.dayDao().getDinnerPoints(dayId);
            Integer otherPoints = db.dayDao().getOtherPoints(dayId);
            if(breakfastPoints != null){
                editHistoryBreakfastInput.setText(String.valueOf(breakfastPoints));
            }
            if(lunchPoints != null){
                editHistoryLunchInput.setText(String.valueOf(lunchPoints));
            }
            if(dinnerPoints != null){
                editHistoryDinnerInput.setText(String.valueOf(dinnerPoints));
            }
            if(otherPoints != null){
                editHistoryOtherInput.setText(String.valueOf(otherPoints));
            }
            latch.countDown();
        });
    }
    private void setupEditQuickAddPage(long quickAddID){
        activity.setContentView(R.layout.edit_quick_add);
        editQuickAddName = this.activity.findViewById(R.id.editNameInput);
        editQuickAddBreakfastPoints = this.activity.findViewById(R.id.breakfastPointInput);
        editQuickAddLunchPoints = this.activity.findViewById(R.id.lunchPointInput);
        editQuickAddDinnerPoints = this.activity.findViewById(R.id.dinnerPointInput);
        editQuickAddOtherPoints = this.activity.findViewById(R.id.otherPointInput);
        submitEditQuickAdd = this.activity.findViewById(R.id.submitChangesButton);
        editQuickAddHome = this.activity.findViewById(R.id.editQuickAddHomeButton);
        submitEditQuickAdd.setOnClickListener(e->{
            submitEditQuickAdd(quickAddID);
        });
        editQuickAddHome.setOnClickListener(e->{
            setupHomePage();
        });

    }
    private void submitEditQuickAdd(long quickAddID){
        AppDatabase.getDatabaseExecutor().execute(()->{
            QuickAdd currentQuickAdd = db.quickAddDao().getQuickAddById(quickAddID);
            try{
                if(doesEditTextHaveContents(editQuickAddName)){
                    currentQuickAdd.setName(editQuickAddName.getText().toString());
                }
                if(doesEditTextHaveContents(editQuickAddBreakfastPoints)){
                    currentQuickAdd.setBreakfastPoints(Integer.parseInt(editQuickAddBreakfastPoints.getText().toString()));
                }
                if(doesEditTextHaveContents(editQuickAddLunchPoints)){
                    currentQuickAdd.setLunchPoints(Integer.parseInt(editQuickAddLunchPoints.getText().toString()));
                }
                if(doesEditTextHaveContents(editQuickAddDinnerPoints)){
                    currentQuickAdd.setDinnerPoints(Integer.parseInt(editQuickAddDinnerPoints.getText().toString()));
                }
                if(doesEditTextHaveContents(editQuickAddOtherPoints)){
                    currentQuickAdd.setOtherPoints(Integer.parseInt(editQuickAddOtherPoints.getText().toString()));
                }
                db.quickAddDao().update(currentQuickAdd);
            } catch (Exception e) {
                Log.d("EXCEPTION", "submitEditQuickAdd Exception: " + e);
            }
            setupHomePage();
        });
    }
    private boolean doesEditTextHaveContents(EditText editText){
        return !editText.getText().toString().isEmpty();
    }
}
