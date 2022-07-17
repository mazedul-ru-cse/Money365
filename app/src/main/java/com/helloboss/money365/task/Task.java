package com.helloboss.money365.task;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.helloboss.money365.R;
import com.helloboss.money365.workshow.InterstitialAdsShow;

public class Task extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        getSupportActionBar().hide();
    }

    public void task1(View view) {

        startActivity(new Intent(Task.this , InterstitialAdsShow.class));
    }
    public void task2(View view) {
        startActivity(new Intent(Task.this , InterstitialAdsShow.class));
    }
    public void task3(View view) {
        startActivity(new Intent(Task.this , InterstitialAdsShow.class));
    }
    public void task4(View view) {
        startActivity(new Intent(Task.this , InterstitialAdsShow.class));
    }


}