package com.helloboss.money365;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.startapp.sdk.adsbase.StartAppAd;
import com.unity3d.ads.UnityAds;
import com.unity3d.ads.UnityAdsShowOptions;

public class Profile extends AppCompatActivity {

    TextView tvName, tvEmail , tvTk, tvPointBalance, tvTotalRefer, tvReferCode;
    AdView adView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getSupportActionBar().hide();

        tvName = findViewById(R.id.p_user_name);
        tvEmail = findViewById(R.id.p_user_email);
        tvTk = findViewById(R.id.p_tk_balance);
        tvPointBalance = findViewById(R.id.p_point_balance);
        tvTotalRefer = findViewById(R.id.p_refer_count);
        tvReferCode = findViewById(R.id.p_refer_code);

        tvName.setText(Dashboard.pName);
        tvEmail.setText(Dashboard.pEmail);
        tvTk.setText("TK : "+Dashboard.pTaka);
        tvPointBalance.setText(Dashboard.pCoin);
        tvTotalRefer.setText(Dashboard.pReferCount);
        tvReferCode.setText(Dashboard.pReferCode);

        StartAppAd.showAd(this);

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


    }

    private void getAds() {
        try {
            UnityAds.show(this, "interstitialAds1", new UnityAdsShowOptions());
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}