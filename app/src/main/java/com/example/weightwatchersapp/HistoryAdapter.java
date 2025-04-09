package com.example.weightwatchersapp;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.CountDownLatch;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {
    private ArrayList<Day> history;
    private AppDatabase db;
    private Activity activity;
    private Controller controller;

    public HistoryAdapter(ArrayList<Day> history, Activity activity, Controller controller) {
        this.activity = activity;
        this.controller = controller;
        db = AppDatabase.getDatabase(activity);
        this.history = new ArrayList<>(history);
        Collections.reverse(this.history);
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.history_item, parent, false);
        return new HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        AppDatabase.getDatabaseExecutor().execute(() ->{
            long dId = (long)(history.size() - position);
            Day currentDay = db.dayDao().getDayById(dId);
            Week currentWeek = db.weekDao().getWeekById(currentDay.getWeekId());
            int weeklyPoints = db.weekDao().getWeeklyPointsAtDay(currentWeek.getWId(), currentDay.getName());
            Log.d("DEBUG", "currentDayId: " + dId + " currentWeekId: " + currentWeek.getWId() + " currentWeeklyPoints: " + weeklyPoints);
            activity.runOnUiThread(() ->{
                holder.currentDayDisplay.setText(currentDay.getName());
                holder.dailyPointsDisplay.setText(String.valueOf(currentDay.getTotalPoints()));
                holder.dayIdDisplay.setText(String.valueOf(currentDay.getDId()));
                holder.weeklyPointsDisplay.setText(String.valueOf(weeklyPoints));
                if(currentDay.getBreakfastPoints() != null){
                    holder.breakfastPointsDisplay.setText(String.valueOf(currentDay.getBreakfastPoints()));
                }
                else{
                    holder.breakfastPointsDisplay.setText("Not Entered");
                }
                if(currentDay.getLunchPoints() != null){
                    holder.lunchPointsDisplay.setText(String.valueOf(currentDay.getLunchPoints()));
                }
                else{
                    holder.lunchPointsDisplay.setText("Not Entered");
                }
                if(currentDay.getDinnerPoints() != null){
                    holder.dinnerPointsDisplay.setText(String.valueOf(currentDay.getDinnerPoints()));
                }
                else{
                    holder.dinnerPointsDisplay.setText("Not Entered");
                }
                if(currentDay.getOtherPoints() != null){
                    holder.otherPointsDisplay.setText(String.valueOf(currentDay.getOtherPoints()));
                }
                else{
                    holder.otherPointsDisplay.setText("Not Entered");
                }
            });

        });
    }

    @Override
    public int getItemCount() {
        return history.size();
    }

    static class HistoryViewHolder extends RecyclerView.ViewHolder {
        TextView currentDayDisplay;
        TextView dayIdDisplay;
        TextView dailyPointsDisplay;
        TextView breakfastPointsDisplay;
        TextView lunchPointsDisplay;
        TextView dinnerPointsDisplay;
        TextView otherPointsDisplay;
        TextView weeklyPointsDisplay;

        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            currentDayDisplay = itemView.findViewById(R.id.currentDayDisplay);
            dayIdDisplay = itemView.findViewById(R.id.dayIdDisplay);
            dailyPointsDisplay = itemView.findViewById(R.id.dailyPointsDisplay);
            breakfastPointsDisplay = itemView.findViewById(R.id.breakfastPointsDisplay);
            lunchPointsDisplay = itemView.findViewById(R.id.lunchPointsDisplay);
            dinnerPointsDisplay = itemView.findViewById(R.id.dinnerPointsDisplay);
            otherPointsDisplay = itemView.findViewById(R.id.otherPointsDisplay);
            weeklyPointsDisplay = itemView.findViewById(R.id.weeklyPointsDisplay);
        }
    }
}