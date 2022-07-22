package com.helloboss.money365.workshow;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.helloboss.money365.BreakTimer;
import com.helloboss.money365.R;
import com.helloboss.money365.StoreUserID;
import com.helloboss.money365.task.Task;

import java.text.SimpleDateFormat;
import java.util.Date;

public class InterstitialAdsShow extends AppCompatActivity {

    TextView tvTaskCounter1 ,tvTaskCounter2;

    StoreUserID storeUserID;
    SimpleDateFormat simpleDateFormat;
    int currentTaskCounter = 0;
    BreakTimer breakTimer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interstitial_ads_show);

        getSupportActionBar().hide();
        storeUserID = new StoreUserID(this);

        tvTaskCounter1 = findViewById(R.id.task_counter1);
        tvTaskCounter2 = findViewById(R.id.task_counter2);

        tvTaskCounter1.setText(Task.taskRange+"");


        breakTimer = new BreakTimer(this);


        if(breakTimer.isBreakTime(storeUserID.getBreakTime())){
            String timerDuration = breakTimer.getTimerDuration();
            breakTimer.showCounterDownTimer(timerDuration );

        }


    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void play(View view) {

        if(Task.taskRange <= currentTaskCounter){

            tvTaskCounter2.setText("0");
            storeUserID.setTaskCount(Task.taskNo+"",0);
            simpleDateFormat = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
            Date date = new Date();
            storeUserID.setBreakTime(simpleDateFormat.format(date)+"");


        }

        tvTaskCounter2.setText(storeUserID.getTaskCount(Task.taskNo+"")+"");
        currentTaskCounter = Integer.parseInt(tvTaskCounter2.getText().toString());
        storeUserID.setTaskCount(Task.taskNo+"",++currentTaskCounter);


    }
}