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

        if(savedInstanceState != null){
            controller = new Controller(this,
                    savedInstanceState.getParcelable("currentDay"),
                    savedInstanceState.getParcelable("currentWeek"),
                    savedInstanceState.getParcelableArrayList("history"));
        }
        else{
            controller = new Controller(this);
        }

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    public void onSaveInstanceState(Bundle outstate){
        outstate.putParcelable("currentDay", controller.getCurrentDay());
        outstate.putParcelable("currentWeek", controller.getCurrentWeek());
        outstate.putParcelableArrayList("history", controller.getHistory());
        super.onSaveInstanceState(outstate);
    }
}