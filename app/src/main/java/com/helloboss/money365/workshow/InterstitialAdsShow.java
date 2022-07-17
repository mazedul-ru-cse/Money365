package com.helloboss.money365.workshow;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.helloboss.money365.R;

public class InterstitialAdsShow extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interstitial_ads_show);

        getSupportActionBar().hide();
    }
}