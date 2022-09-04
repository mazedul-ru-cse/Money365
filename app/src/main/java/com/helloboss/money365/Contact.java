package com.helloboss.money365;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.startapp.sdk.adsbase.StartAppAd;
import com.unity3d.ads.UnityAds;
import com.unity3d.ads.UnityAdsShowOptions;

public class Contact extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        getSupportActionBar().hide();

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        try {
            UnityAds.initialize(getApplicationContext(), getString(R.string.unity_game_id), false);
            UnityAds.load("interstitialAds1");
        }catch (Exception e){
            e.printStackTrace();
        }

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                getAds();
            }
        },1500);

        StartAppAd.showAd(this);
    }

    private void getAds() {

        StartAppAd.showAd(this);

        try {
            UnityAds.show(this, "interstitialAds1", new UnityAdsShowOptions());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void call_now(View view) {

        StartAppAd.showAd(this);

        TextView phone = findViewById(R.id.c_phone);
        try {
            phone.setMovementMethod(LinkMovementMethod.getInstance());
        }catch (Exception e){
            e.getMessage();
        }
    }

    public void join_telegram(View view) {

        StartAppAd.showAd(this);

        TextView telegram = findViewById(R.id.c_telegram);
        try {
            telegram.setMovementMethod(LinkMovementMethod.getInstance());
        }catch (Exception e){
            e.getMessage();
        }
    }

    public void send_mail(View view) {

        StartAppAd.showAd(this);

       TextView email = findViewById(R.id.c_email);
        try {
            email.setMovementMethod(LinkMovementMethod.getInstance());
        }catch (Exception e){
            e.getMessage();
        }
    }


    public void join_facebook(View view) {

        StartAppAd.showAd(this);

        TextView facebook = findViewById(R.id.c_facebook);
        try {
            facebook.setMovementMethod(LinkMovementMethod.getInstance());
        }catch (Exception e){
            e.getMessage();
        }
    }
}