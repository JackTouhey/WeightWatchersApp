package com.example.weightwatchersapp;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Controller controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        controller = new Controller(this);
        controller.setupDayView();
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        AppDatabase.getDatabaseExecutor().shutdown();
    }
    public void onSaveInstanceState(Bundle outstate){
        super.onSaveInstanceState(outstate);
    }
}