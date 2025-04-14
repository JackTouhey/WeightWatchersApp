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
        if (savedInstanceState != null){
            controller = new Controller(this, savedInstanceState.getInt("currentPage"));
        }
        else{
            controller = new Controller(this, 1);
        }

    }
    @Override
    protected void onDestroy(){
        if (isFinishing()) {
            AppDatabase.shutdownExecutor();
        }
        super.onDestroy();
    }
    public void onSaveInstanceState(Bundle outstate){
        outstate.putInt("currentPage", controller.getCurrentPage());
        super.onSaveInstanceState(outstate);
    }
}