package com.helloboss.money365;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.helloboss.money365.requesthandler.RequestHandler;

import org.json.JSONObject;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    ProgressBar progressBar;
    private int currentProg = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();

        progressBar = findViewById(R.id.start_up_progress);

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                currentProg += 20;
                progressBar.setProgress(currentProg);
                progressBar.setMax(100);
            }
        },1000);



        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                currentProg += 20;
                progressBar.setProgress(currentProg);
                progressBar.setMax(100);
            }
        },2000);

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {

                currentProg += 20;
                progressBar.setProgress(currentProg);
                progressBar.setMax(100);
            }
        },3000);

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                currentProg += 20;
                progressBar.setProgress(currentProg);
                progressBar.setMax(100);
            }
        },4000);

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                currentProg += 20;
                progressBar.setProgress(currentProg);
                progressBar.setMax(100);
            }
        },5000);

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {

                startActivity(new Intent(MainActivity.this , UserLogin.class));
                finish();
            }
        },6000);

    }

}