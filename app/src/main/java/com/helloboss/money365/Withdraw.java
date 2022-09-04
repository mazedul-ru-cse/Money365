package com.helloboss.money365;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.startapp.sdk.adsbase.StartAppAd;
import com.unity3d.ads.UnityAds;
import com.unity3d.ads.UnityAdsShowOptions;

public class Withdraw extends AppCompatActivity {

    TextView wPoint, wTaka;

    PaymentDialog paymentDialog;
    Dialog dialog;

    TextView minR, minB, minRo, minNo;

    AdView adView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw);
        getSupportActionBar().hide();

        wPoint = findViewById(R.id.w_point_balance);
        wTaka = findViewById(R.id.w_tk_balance);

        minR = findViewById(R.id.min_r);
        minB = findViewById(R.id.min_b);
        minRo = findViewById(R.id.min_ro);
        minNo = findViewById(R.id.min_no);

        minR.setText(Dashboard.minRecharge+" Tk");
        minB.setText(Dashboard.minWithdraw+" Tk");
        minRo.setText(Dashboard.minWithdraw+" Tk");
        minNo.setText(Dashboard.minWithdraw+" Tk");


        wPoint.setText(Dashboard.pCoin);
        wTaka.setText(Dashboard.pTaka);

        paymentDialog = new PaymentDialog(this);
        dialog = new Dialog(this);

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
        },1000);



    }

    private void getAds() {
        try {
            UnityAds.show(this, "interstitialAds1", new UnityAdsShowOptions());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void recharge(View view) {

        new RechargeDialog(this).showRechargeDialog(Dashboard.minRecharge);
    }

    public void bkash(View view) {
        paymentDialog.showPaymentDialog(Dashboard.minWithdraw,"bKash");
    }

    public void rocket(View view) {
        paymentDialog.showPaymentDialog(Dashboard.minWithdraw,"Rocket");
    }

    public void nogot(View view) {
        paymentDialog.showPaymentDialog(Dashboard.minWithdraw,"Nogot");
    }

    public void paypal(View view) {
        Toast.makeText(this, "Coming soon", Toast.LENGTH_SHORT).show();
    }

    public void card(View view) {
        Toast.makeText(this, "Coming soon", Toast.LENGTH_SHORT).show();
    }
}