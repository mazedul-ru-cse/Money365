package com.helloboss.money365.workshow;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.ads.AudienceNetworkAds;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.helloboss.money365.BreakTimer;
import com.helloboss.money365.Dashboard;
import com.helloboss.money365.ProgressDialogM;
import com.helloboss.money365.R;
import com.helloboss.money365.StoreUserID;
import com.helloboss.money365.UserLogin;
import com.helloboss.money365.adsLoader.AdmobAdsLoader;
import com.helloboss.money365.adsLoader.FBAdsLoader;
import com.helloboss.money365.requesthandler.RequestHandler;
import com.helloboss.money365.task.Task;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class InterstitialAdsShow extends AppCompatActivity {

    TextView tvTaskCounter1 ,tvTaskCounter2;

    StoreUserID storeUserID;
    SimpleDateFormat simpleDateFormat;
    int currentTaskCounter = 0;
    BreakTimer breakTimer;

    AdmobAdsLoader adsLoader;
    FBAdsLoader fbAdsLoader;
    int updateCoin = 0;

    String taskRewardPoint = Task.taskNo+"reward";

    public static long rewardPoint = 0;
    ProgressDialogM progressDialogM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interstitial_ads_show);

        AudienceNetworkAds.initialize(this);
        progressDialogM = new ProgressDialogM(this);

        getSupportActionBar().hide();
        storeUserID = new StoreUserID(this);
        breakTimer = new BreakTimer(this);

        tvTaskCounter1 = findViewById(R.id.task_counter1);
        tvTaskCounter2 = findViewById(R.id.task_counter2);

        tvTaskCounter1.setText(Task.taskRange+"");
        tvTaskCounter2.setText(storeUserID.getTaskCount(Task.taskNo+"")+"");

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        //custom ads loader
        adsLoader = new AdmobAdsLoader(this);
        fbAdsLoader = new FBAdsLoader(this);



    }

    public void play(View view) {

        try {
            //get previous task counter2
            currentTaskCounter = Integer.parseInt(tvTaskCounter2.getText().toString());

            if (Task.taskRange == currentTaskCounter) {

                tvTaskCounter2.setText("0");
                storeUserID.setTaskCount(Task.taskNo + "", 0);

                //get current time
                simpleDateFormat = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
                Date date = new Date();

                //Reward point set to zero
                updateCoin = Integer.parseInt(Dashboard.pCoin) + storeUserID.getCurrentRewardPoint(taskRewardPoint);
                storeUserID.setCurrentRewardPoint(taskRewardPoint, 0);

                //Update the point
                new TaskUpdateCoin().execute();

                //Set break time
                storeUserID.setBreakTime(simpleDateFormat.format(date) + "");
                if (breakTimer.isBreakTime(storeUserID.getBreakTime())) {
                    Toast.makeText(this, "Total Coin "+updateCoin, Toast.LENGTH_SHORT).show();
                    breakTimer.showCounterDownTimer();
                }


            } else {

                currentTaskCounter++;
                tvTaskCounter2.setText(currentTaskCounter + "");
                view.setEnabled(false);

                new CountDownTimer(3000, 1000) {
                    @Override
                    public void onTick(long l) {

                        NumberFormat f = new DecimalFormat("00");

                        long sec = (l / 1000) % 60;

                        Toast.makeText(InterstitialAdsShow.this, "Ads coming : " + f.format(sec) + " sec", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onFinish() {
                        view.setEnabled(true);
                    }
                }.start();

                // tvTaskCounter2.setText(storeUserID.getTaskCount(Task.taskNo+"")+"");

                //set current ada counter
                storeUserID.setTaskCount(Task.taskNo + "", currentTaskCounter);


                if(Task.taskAds.equals("fb")) {

                    if(Task.taskAdsTypes.equals("interstitial")) {

                        if (fbAdsLoader.setFBInterstitialAdsID("ca-app-pub-3940256099942544/5224354917",storeUserID, taskRewardPoint) != null) {
                            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                                @Override
                                public void run(){
                                    //fbAdsLoader.showFBInterstitialAdsID(storeUserID, taskRewardPoint);
                                    fbAdsLoader.getFBInterstitialAd();
                                }
                            }, 3500);
                        }
                    }else{
                        if (fbAdsLoader.setFBRewardedVideoAdsID("ca-app-pub-3940256099942544/5224354917", storeUserID, taskRewardPoint) != null) {
                            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    fbAdsLoader.getFBRewardedVideoAd();
                                }
                            }, 3500);
                        }

                    }


                } else {

                    if(Task.taskAdsTypes.equals("interstitial")) {

                        if (adsLoader.setAdmobInterstitialAdsID("ca-app-pub-3940256099942544/5224354917") != null) {
                            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    adsLoader.showAdmobInterstitialAdsID(storeUserID, taskRewardPoint);
                                }
                            }, 3500);
                        }
                    }else{
                        if (adsLoader.setAdmobRewardedAdID("ca-app-pub-3940256099942544/5224354917") != null) {
                            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    adsLoader.showAdmobRewardedAdsID(storeUserID, taskRewardPoint);
                                }
                            }, 3500);
                        }

                    }
                }

            }

        }catch (Exception e){
            e.getMessage();
        }
    }

    class TaskUpdateCoin extends AsyncTask<String,Void , String> {

        final String UPDATE_COIN_URL = "https://helloboss365.com/money365/update_coin.php";

        @Override
        protected String doInBackground(String... task) {

            try {

                RequestHandler requestHandler = new RequestHandler();

                //creating request parameters
                HashMap<String, String> params = new HashMap<>();
                params.put("coin", updateCoin+"");
                params.put("phone", UserLogin.userID);

                //returning the response
                return requestHandler.sendPostRequest(UPDATE_COIN_URL, params);


            }catch (Exception e) {

                progressDialogM.hideDialog();
                Log.i("Task exception", e.getMessage());
                return null;

            }
        }

        @Override
        protected void onPostExecute(String s) {

            super.onPostExecute(s);
            progressDialogM.hideDialog();
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialogM.showDialog("Please wait");
        }
    }

}