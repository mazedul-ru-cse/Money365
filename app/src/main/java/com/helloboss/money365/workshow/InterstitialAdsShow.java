package com.helloboss.money365.workshow;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.helloboss.money365.BreakTimer;
import com.helloboss.money365.Dashboard;
import com.helloboss.money365.R;
import com.helloboss.money365.StoreUserID;
import com.helloboss.money365.adsLoader.AdsLoader;
import com.helloboss.money365.task.Task;
import java.text.SimpleDateFormat;
import java.util.Date;

public class InterstitialAdsShow extends AppCompatActivity {

    TextView tvTaskCounter1 ,tvTaskCounter2;

    StoreUserID storeUserID;
    SimpleDateFormat simpleDateFormat;
    int currentTaskCounter = 0;
    BreakTimer breakTimer;

    AdsLoader adsLoader;

    String taskRewardPoint = Task.taskNo+"reward";

    public static long rewardPoint = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interstitial_ads_show);

        getSupportActionBar().hide();
        storeUserID = new StoreUserID(this);
        breakTimer = new BreakTimer(this);

        tvTaskCounter1 = findViewById(R.id.task_counter1);
        tvTaskCounter2 = findViewById(R.id.task_counter2);

        tvTaskCounter1.setText(Task.taskRange+"");

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        //custom ads loader
        adsLoader = new AdsLoader(this);

    }

    public void play(View view) {

        tvTaskCounter2.setText(storeUserID.getTaskCount(Task.taskNo+"")+"");

        currentTaskCounter = Integer.parseInt(tvTaskCounter2.getText().toString());
        ++currentTaskCounter;

        //set current ada counter
        storeUserID.setTaskCount(Task.taskNo+"",currentTaskCounter);

        if(!Task.taskAds.equals("fb")){


        }else{

            if(adsLoader.setAdmobInterAdsID("ca-app-pub-3940256099942544/5224354917") != null){
                adsLoader.showAdmobInterstitialAdsID(storeUserID,taskRewardPoint);

            }
            
        }

        //Get current reward point
        int myPoint = storeUserID.getCurrentRewardPoint(taskRewardPoint);
        Toast.makeText(this, myPoint, Toast.LENGTH_SHORT).show();

        if(Task.taskRange == currentTaskCounter){

            try {
                tvTaskCounter2.setText("0");
                storeUserID.setTaskCount(Task.taskNo + "", 0);
                simpleDateFormat = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
                Date date = new Date();
                //Reward point set to zero
                storeUserID.setCurrentRewardPoint(taskRewardPoint,0);

                //Set break time
                storeUserID.setBreakTime(simpleDateFormat.format(date) + "");
                if (breakTimer.isBreakTime(storeUserID.getBreakTime())) {
                    breakTimer.showCounterDownTimer();
                }

            }catch (Exception e){
                e.getMessage();
            }
        }


    }
}