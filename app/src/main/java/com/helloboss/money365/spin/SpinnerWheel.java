package com.helloboss.money365.spin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
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
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.helloboss.money365.Dashboard;
import com.helloboss.money365.ProgressDialogM;
import com.helloboss.money365.R;
import com.helloboss.money365.StoreUserID;
import com.helloboss.money365.UserLogin;
import com.helloboss.money365.requesthandler.RequestHandler;
import com.helloboss.money365.task.Task;
import com.helloboss.money365.task.TaskList;
import com.helloboss.money365.workshow.InterstitialAdsShow;
import com.startapp.sdk.adsbase.Ad;
import com.startapp.sdk.adsbase.StartAppAd;
import com.startapp.sdk.adsbase.adlisteners.AdDisplayListener;
import com.startapp.sdk.adsbase.adlisteners.AdEventListener;
import com.startapp.sdk.adsbase.adlisteners.VideoListener;
import com.unity3d.ads.IUnityAdsLoadListener;
import com.unity3d.ads.IUnityAdsShowListener;
import com.unity3d.ads.UnityAds;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.ExecutionException;

public class SpinnerWheel extends AppCompatActivity {

    ImageView wheelNumber;
    TextView spinner,adsClickNotice, spinnerCounter1,spinnerCounter2,spinPointTv;
    

    public static final String[] sectors = {"4","5","6","7","8","9","1","2","3"};
    public static final int[] sectorDegrees = new int[sectors.length];
    public static final Random random = new Random();
    int degree = 0;
    private boolean isSpinning = false;
    int adsClickCounter = 1,spinClickCounter = 0;

    StoreUserID storeUserID;
    String spinRewardPoint = "spinRewardPoint";
    private int updateCoin = 0;
    private ProgressDialogM progressDialogM;
    StartAppAd rewardedAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spinner_wheel);

        getSupportActionBar().hide();

        spinner = findViewById(R.id.btn_spinner);
        adsClickNotice = findViewById(R.id.ads_click_notice);
        wheelNumber = findViewById(R.id.wheel_number);
        spinnerCounter1 = findViewById(R.id.spinner_counter1);
        spinnerCounter2 = findViewById(R.id.spinner_counter2);
        spinPointTv = findViewById(R.id.spin_point_tv);

        storeUserID = new StoreUserID(this);
        progressDialogM = new ProgressDialogM(this);

        try {
            spinnerCounter1.setText(Dashboard.sRange + "");
            spinnerCounter2.setText(storeUserID.getSpinCount("spinCount") + "");

            spinPointTv.setText("Earned point " + storeUserID.getCurrentSpinRewardPoint(spinRewardPoint));

            adsClickNotice.setText(Dashboard.sAdsClick + " টি স্পিন করার পর অবশ্যই যেকোনো একটি বিজ্ঞাপনের উপর ক্লিক করবেন।");

            getDegreeForSectors();
        }catch (Exception e){
            e.printStackTrace();
        }

        spinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    StartAppAd.showAd(SpinnerWheel.this);

                    spinClickCounter = Integer.parseInt(spinnerCounter2.getText().toString());
                    rewardedAd = new StartAppAd(SpinnerWheel.this);

                    if (!isSpinning) {

                        if (Dashboard.sRange <= spinClickCounter) {
                            //Reward point set to zero
                            updateCoin = Integer.parseInt(Dashboard.pCoin) + storeUserID.getCurrentSpinRewardPoint(spinRewardPoint);

                            //Update the point
                            new SpinUpdateCoin().execute();
                        } else {
                            spin();
                            isSpinning = true;
                        }
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    private void spin(){

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        spinClickCounter++;
        adsClickCounter++;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){

            Network network = connectivityManager.getActiveNetwork();
            NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(network);
            boolean vpnConnection = networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_VPN);

            if(!vpnConnection){
                startActivity(new Intent(SpinnerWheel.this, Dashboard.class ));
                finish();
                return;
            }
        }


        try {
            degree = random.nextInt(sectors.length);
            RotateAnimation rotateAnimation = new RotateAnimation(0, (360 * sectors.length) + sectorDegrees[degree],
                    RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);

            rotateAnimation.setDuration(5000);
            rotateAnimation.setFillAfter(true);
            rotateAnimation.setInterpolator(new DecelerateInterpolator());

            rotateAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                    rewardedAd.setVideoListener(new VideoListener() {
                        @Override
                        public void onVideoCompleted() {

                        }
                    });

                    rewardedAd.loadAd(StartAppAd.AdMode.FULLPAGE, new AdEventListener() {
                        @Override
                        public void onReceiveAd(@NonNull Ad ad) {

                        }

                        @Override
                        public void onFailedToReceiveAd(@Nullable Ad ad) {

                        }
                    });
                }

                @Override
                public void onAnimationEnd(Animation animation) {

                    //  Toast.makeText(SpinnerWheel.this, "You've got "+sectors[sectors.length - (degree + 1)]+" points", Toast.LENGTH_SHORT).show();
                    isSpinning = false;

                    try {
                        rewardedAd.showAd(new AdDisplayListener() {
                            @Override

                            public void adHidden(Ad ad) {
                                // Toast.makeText(SpinnerWheel.this, "Got 10 points", Toast.LENGTH_SHORT).show();
                                spinner.setEnabled(false);
                                StartAppAd.showAd(SpinnerWheel.this);

                                new CountDownTimer(5000, 1000) {
                                    @Override
                                    public void onTick(long l) {

                                        NumberFormat f = new DecimalFormat("00");

                                        long sec = (l / 1000) % 60;

                                        spinner.setText("  "+f.format(sec)+"  ");
                                    }

                                    @Override
                                    public void onFinish() {
                                        spinner.setText("  Spin  ");
                                        spinner.setEnabled(true);
                                        StartAppAd.showAd(SpinnerWheel.this);
                                    }
                                }.start();

                            }

                            @Override
                            public void adDisplayed(Ad ad) {

                                //set current ada counter
                                storeUserID.setSpinCount("spinCount", spinClickCounter);

                                //set current spin points
                                storeUserID.setCurrentSpinRewardPoint(spinRewardPoint, storeUserID.getCurrentSpinRewardPoint(spinRewardPoint) + Dashboard.sPoint);

                                //set spin counter
                                spinnerCounter2.setText(spinClickCounter + "");

                                //get current spin reward points
                                spinPointTv.setText("Earned point " + storeUserID.getCurrentSpinRewardPoint(spinRewardPoint));

                                if (adsClickCounter >= Dashboard.sAdsClick) {
                                    AlertDialog.Builder dialog = new AlertDialog.Builder(SpinnerWheel.this);

                                    dialog.setCancelable(false);
                                    dialog.setTitle("বিজ্ঞাপন ক্লিক");
                                    dialog.setMessage("পরবর্তী বিজ্ঞাপনে অবশ্যই ক্লিক করবেন। তানাহলে আপনি আর কোন পয়েন্ট পাবেন না।");
                                    adsClickCounter = 1;


                                    dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.dismiss();
                                            StartAppAd.showAd(SpinnerWheel.this);
                                        }
                                    });

                                    dialog.create();
                                    dialog.show();
                                }
                            }

                            @Override
                            public void adClicked(Ad ad) {

                            }

                            @Override
                            public void adNotDisplayed(Ad ad) {
                            }
                        });
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });

            wheelNumber.startAnimation(rotateAnimation);

        }catch (Exception e){
            e.printStackTrace();
        }


    }

    private void getDegreeForSectors(){

        try {
            int sectorDegree = 360 / sectors.length;

            for (int i = 0; i < sectors.length; i++) {
                sectorDegrees[i] = (i + 1) * sectorDegree;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    class SpinUpdateCoin extends AsyncTask<String,Void , String> {

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
            StartAppAd.showAd(SpinnerWheel.this);
            try {
                JSONObject obj = new JSONObject(s);

                //get current time
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
                Date date = new Date();
                //Set break time
                storeUserID.setBreakTime(simpleDateFormat.format(date) + "");

                int lastPoint = storeUserID.getCurrentSpinRewardPoint(spinRewardPoint);

               //set spin task count 0
                storeUserID.setSpinCount("spinCount", 0);

                if(!obj.getBoolean("error")){
                    storeUserID.setCurrentSpinRewardPoint(spinRewardPoint, 0);
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

                    StartAppAd.showAd(SpinnerWheel.this);

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

                    StartAppAd.showAd(SpinnerWheel.this);

                    try {
                        dialog.dismiss();
                        //  dialogInterface.dismiss();
                        startActivity(new Intent(SpinnerWheel.this , Dashboard.class));
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(SpinnerWheel.this, TaskList.class));
        finish();
    }

}