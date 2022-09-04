package com.helloboss.money365.workshow;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.ads.AudienceNetworkAds;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.helloboss.money365.Dashboard;
import com.helloboss.money365.ProgressDialogM;
import com.helloboss.money365.R;
import com.helloboss.money365.StoreUserID;
import com.helloboss.money365.UserLogin;
import com.helloboss.money365.adsLoader.UnityAdsLoader;
import com.helloboss.money365.adsLoader.FBAdsLoader;
import com.helloboss.money365.requesthandler.RequestHandler;
import com.helloboss.money365.task.Task;
import com.startapp.sdk.adsbase.StartAppAd;
import com.unity3d.ads.IUnityAdsInitializationListener;
import com.unity3d.ads.UnityAds;
import com.unity3d.services.banners.IUnityBannerListener;
import com.unity3d.services.banners.UnityBanners;

import org.json.JSONException;
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
    UnityAdsLoader unityAdsLoader;
    FBAdsLoader fbAdsLoader;
    int updateCoin = 0;
    int questionCounter = 0;
    String taskRewardPoint = "taskRewardPoint";
    ProgressDialogM progressDialogM;

    CheckBox optionA, optionB;
    TextView question;
    Questions questions;
    TextView btnPlay;

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
        btnPlay = findViewById(R.id.get_question);

        optionA = findViewById(R.id.option_a);
        optionB = findViewById(R.id.option_b);
        question = findViewById(R.id.question);

        tvTaskCounter1.setText(Task.taskRange+"");
        tvTaskCounter2.setText(storeUserID.getTaskCount(Task.taskNo+"")+"");
        coinCounter.setText("Earned point "+storeUserID.getCurrentRewardPoint(taskRewardPoint)+"");

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });


        UnityAds.initialize(getApplicationContext(),getString(R.string.unity_game_id));


        //custom ads loader
        unityAdsLoader = new UnityAdsLoader(InterstitialAdsShow.this, btnPlay);
        fbAdsLoader = new FBAdsLoader(this);
        questions = new Questions();

        optionA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(optionB.isChecked())
                    optionB.setChecked(false);
            }
        });

        optionB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(optionA.isChecked())
                    optionA.setChecked(false);
            }
        });

        StartAppAd.showAd(this);

    }

    public void play(View view) {

        //StartAppAd.showAd(this);

        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){

                Network network = connectivityManager.getActiveNetwork();
                NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(network);
                boolean vpnConnection = networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_VPN);

                if(!vpnConnection){
                    startActivity(new Intent(InterstitialAdsShow.this, Dashboard.class ));
                    finish();
                    return;
                }
            }

            // set questions
            questionCounter++;
            if(questionCounter > 11){
                questionCounter = 1;
            }
            questions.setQuestion(questionCounter);


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

                    //Reward point set to zero
                    updateCoin = Integer.parseInt(Dashboard.pCoin) + storeUserID.getCurrentRewardPoint(taskRewardPoint);

                    //Update the point
                    new TaskUpdateCoin().execute();


                }else{
                    startActivity(new Intent(InterstitialAdsShow.this, Task.class ));
                    finish();
                }


            }else {

                currentTaskCounter++;
                tvTaskCounter2.setText(currentTaskCounter + "");
                view.setEnabled(false);

                new CountDownTimer(5000, 1000) {
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

                        if (fbAdsLoader.setFBInterstitialAdsID(Task.placementId,storeUserID, taskRewardPoint) != null) {
                            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                                @Override
                                public void run(){
                                    //fbAdsLoader.showFBInterstitialAdsID(storeUserID, taskRewardPoint);
                                    fbAdsLoader.getFBInterstitialAd();
                                }
                            }, 3500);
                        }
                    }else{
                        if (fbAdsLoader.setFBRewardedVideoAdsID(Task.placementId, storeUserID, taskRewardPoint) != null) {
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

                        if (unityAdsLoader.setInterstitialAds(Task.placementId, storeUserID, taskRewardPoint)){
                            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    unityAdsLoader.showInAds(storeUserID, taskRewardPoint);
                                }
                            }, 3000);
                        }

                    }else {
                        if (unityAdsLoader.setRewardAds(Task.placementId, storeUserID, taskRewardPoint)) {
                            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    unityAdsLoader.showRewardAds(storeUserID, taskRewardPoint);
                                }
                            }, 3000);
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
            CountDownTimer countDownTimer = new CountDownTimer(Dashboard.breakTime*60*1000, 1000) {
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

            try {
                JSONObject obj = new JSONObject(s);

                //get current time
                simpleDateFormat = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
                Date date = new Date();
                //Set break time
                storeUserID.setBreakTime(simpleDateFormat.format(date) + "");

                int lastPoint = storeUserID.getCurrentRewardPoint(taskRewardPoint);

                if(!obj.getBoolean("error")){
                    storeUserID.setCurrentRewardPoint(taskRewardPoint, 0);
                }
                earnedCoinMessage(lastPoint);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialogM.showDialog("Please wait");
        }
    }

    private class Questions {

        public void setQuestion(int index){

            switch (index){

                case 1 :
                    question.setText(getString(R.string.q1));
                    optionA.setText(getString(R.string.q1a));
                    optionB.setText(getString(R.string.q1b));
                    break;

                case 2 :
                    question.setText(getString(R.string.q2));
                    optionA.setText(getString(R.string.q2a));
                    optionB.setText(getString(R.string.q2b));
                    break;

                case 3 :
                    question.setText(getString(R.string.q3));
                    optionA.setText(getString(R.string.q3a));
                    optionB.setText(getString(R.string.q3b));
                    break;

                case 4 :
                    question.setText(getString(R.string.q4));
                    optionA.setText(getString(R.string.q4a));
                    optionB.setText(getString(R.string.q4b));
                    break;

                case 5 :
                    question.setText(getString(R.string.q5));
                    optionA.setText(getString(R.string.q5a));
                    optionB.setText(getString(R.string.q5b));
                    break;

                case 6 :
                    question.setText(getString(R.string.q6));
                    optionA.setText(getString(R.string.q6a));
                    optionB.setText(getString(R.string.q6b));
                    break;

                case 7 :
                    question.setText(getString(R.string.q7));
                    optionA.setText(getString(R.string.q7a));
                    optionB.setText(getString(R.string.q7b));
                    break;

                case 9 :
                    question.setText(getString(R.string.q9));
                    optionA.setText(getString(R.string.q9a));
                    optionB.setText(getString(R.string.q9b));
                    break;

                case 10 :
                    question.setText(getString(R.string.q10));
                    optionA.setText(getString(R.string.q10a));
                    optionB.setText(getString(R.string.q10b));
                    break;

                case 11 :
                    question.setText(getString(R.string.q11));
                    optionA.setText(getString(R.string.q11a));
                    optionB.setText(getString(R.string.q11b));
                    break;

                default:
                    question.setText(getString(R.string.q8));
                    optionA.setText(getString(R.string.q8a));
                    optionB.setText(getString(R.string.q8b));
                    break;

            }
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(InterstitialAdsShow.this, Task.class));
        finish();
    }
}