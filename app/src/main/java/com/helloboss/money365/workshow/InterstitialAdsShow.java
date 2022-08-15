package com.helloboss.money365.workshow;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
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

    TextView tvTaskCounter1 ,tvTaskCounter2,coinCounter;

    StoreUserID storeUserID;
    SimpleDateFormat simpleDateFormat;
    int currentTaskCounter = 0;
    AdmobAdsLoader adsLoader;
    FBAdsLoader fbAdsLoader;
    int updateCoin = 0;

    String taskRewardPoint = "taskRewardPoint";
    ProgressDialogM progressDialogM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interstitial_ads_show);

        AudienceNetworkAds.initialize(this);
        progressDialogM = new ProgressDialogM(this);

        getSupportActionBar().hide();
        storeUserID = new StoreUserID(this);

        tvTaskCounter1 = findViewById(R.id.task_counter1);
        tvTaskCounter2 = findViewById(R.id.task_counter2);
        coinCounter = findViewById(R.id.coin_counter_tv);

        tvTaskCounter1.setText(Task.taskRange+"");
        tvTaskCounter2.setText(storeUserID.getTaskCount(Task.taskNo+"")+"");
        coinCounter.setText("Earned point "+storeUserID.getCurrentRewardPoint(taskRewardPoint)+"");

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
            coinCounter.setText("Earned point "+storeUserID.getCurrentRewardPoint(taskRewardPoint)+"");

            //get previous task counter2
            currentTaskCounter = Integer.parseInt(tvTaskCounter2.getText().toString());

            if (Task.taskRange == currentTaskCounter) {

                tvTaskCounter2.setText("0");
                storeUserID.setTaskCount(Task.taskNo + "", 0);

                //Current task complete action
                storeUserID.setTaskStatus(Task.taskNo+"status", "completed");



                if(Task.taskNo == 4) {

                    storeUserID.setTaskStatus("1status", "incomplete");
                    storeUserID.setTaskStatus("2status", "incomplete");
                    storeUserID.setTaskStatus("3status", "incomplete");
                    //get current time
                    simpleDateFormat = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
                    Date date = new Date();

                    //Reward point set to zero
                    updateCoin = Integer.parseInt(Dashboard.pCoin) + storeUserID.getCurrentRewardPoint(taskRewardPoint);
                    int lastPoint = storeUserID.getCurrentRewardPoint(taskRewardPoint);
                    storeUserID.setCurrentRewardPoint(taskRewardPoint, 0);

                    //Update the point
                    new TaskUpdateCoin().execute();

                    //Set break time
                    storeUserID.setBreakTime(simpleDateFormat.format(date) + "");

                    earnedCoinMessage(lastPoint);

                }else{
                    startActivity(new Intent(InterstitialAdsShow.this, Task.class ));
                    finish();
                }


            }else {

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

                        if (fbAdsLoader.setFBInterstitialAdsID("3258418574477730_3258568021129452",storeUserID, taskRewardPoint) != null) {
                            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                                @Override
                                public void run(){
                                    //fbAdsLoader.showFBInterstitialAdsID(storeUserID, taskRewardPoint);
                                    fbAdsLoader.getFBInterstitialAd();
                                }
                            }, 3500);
                        }
                    }else{
                        if (fbAdsLoader.setFBRewardedVideoAdsID("3258418574477730_3258568577796063", storeUserID, taskRewardPoint) != null) {
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

                        if (adsLoader.setAdmobInterstitialAdsID("ca-app-pub-3940256099942544/1033173712") != null) {
                            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    adsLoader.showAdmobInterstitialAdsID(storeUserID, taskRewardPoint);
                                }
                            }, 3500);
                        }
                    }else{
                        if (adsLoader.setAdmobRewardedAdID("ca-app-pub-3940256099942544/1033173712") != null) {
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

    private void earnedCoinMessage(int point) {

        Dialog dialog;
        try {
            dialog = new Dialog(this);
            dialog.setContentView(R.layout.waring_xml);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            TextView titleTv = dialog.findViewById(R.id.waring_text);
            ImageView cross = dialog.findViewById(R.id.waring_cross);
            ImageView imageView = dialog.findViewById(R.id.waring_image);
            imageView.setBackgroundResource(R.drawable.congratulations);

            titleTv.setText("অভিনন্দন\nআপনি "+point+" পয়েন্ট পেয়েছেন!!");
            dialog.setCancelable(false);
            dialog.create();
            dialog.show();

            cross.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                        showBreak();
                    }
                }
            });

        } catch (Exception e) {
            e.getMessage();

        }
    }

    private void showBreak() {

        Dialog dialog;
        // breakTimer.showCounterDownTimer();

        try {
            dialog = new Dialog(this);
            dialog.setContentView(R.layout.count_down_timer);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setCancelable(true);

            TextView tvTimer = dialog.findViewById(R.id.timer_count);
            CountDownTimer countDownTimer = new CountDownTimer(300000, 1000) {
                @Override
                public void onTick(long l) {

                    NumberFormat f = new DecimalFormat("00");
                    long min = (l / 60000) % 60;
                    long sec = (l / 1000) % 60;

                    tvTimer.setText(f.format(min) + ":" + f.format(sec));

                }

                @Override
                public void onFinish() {

                    tvTimer.setText("00:00");
                    dialog.dismiss();

                }
            };

            dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialogInterface) {

                    try {
                        dialog.dismiss();
                      //  dialogInterface.dismiss();
                        startActivity(new Intent(InterstitialAdsShow.this , Dashboard.class));
                        finish();

                    } catch (Exception e) {
                        e.getMessage();
                    }
                }
            });

            dialog.create();
            dialog.show();
            countDownTimer.start();

        } catch (Exception e) {

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