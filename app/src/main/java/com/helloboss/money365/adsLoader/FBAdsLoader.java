package com.helloboss.money365.adsLoader;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdSettings;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;
import com.facebook.ads.RewardedVideoAd;
import com.facebook.ads.RewardedVideoAdListener;
import com.helloboss.money365.Dashboard;
import com.helloboss.money365.StoreUserID;
import com.helloboss.money365.workshow.InterstitialAdsShow;

public class FBAdsLoader {

    Context context;

    InterstitialAd interstitialAd;
    RewardedVideoAd rewardedVideoAd;

    public FBAdsLoader(Context context) {
        this.context = context;
    }

    public void getFBInterstitialAd() {

        try {
            if (interstitialAd.isAdLoaded() && interstitialAd != null && !interstitialAd.isAdInvalidated()) {
                interstitialAd.show();
                interstitialAd = null;
            }
        }catch (Exception e){
            Log.i("Exception", e.getMessage());
        }
    }

    public InterstitialAd setFBInterstitialAdsID(String adsID, StoreUserID storeUserID, String taskNo){


//        AdSettings.setTestAdType(AdSettings.TestAdType.IMG_16_9_APP_INSTALL);
//        AdSettings.getTestAdType();

        interstitialAd = new InterstitialAd((InterstitialAdsShow)context, adsID);

        InterstitialAdListener adListener = new InterstitialAdListener() {

            @Override
            public void onError(Ad ad, AdError adError) {
               // Toast.makeText(context, "adError", Toast.LENGTH_SHORT).show();
                interstitialAd = null;


            }

            @Override
            public void onAdLoaded(Ad ad) {
                //interstitialAd.show();
               // Toast.makeText(context, "onAdLoaded", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onAdClicked(Ad ad) {
                //Toast.makeText(context, "onAdClicked", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLoggingImpression(Ad ad) {

            }

            @Override
            public void onInterstitialDisplayed(Ad ad) {
                //Toast.makeText(context, "onInterstitialDisplayed", Toast.LENGTH_SHORT).show();
                storeUserID.setCurrentRewardPoint(taskNo,storeUserID.getCurrentRewardPoint(taskNo)+10);
            }

            @Override
            public void onInterstitialDismissed(Ad ad) {
                //Toast.makeText(context, "onInterstitialDismissed", Toast.LENGTH_SHORT).show();
                interstitialAd = null;

            }
        };

        interstitialAd.loadAd(
                interstitialAd.buildLoadAdConfig()
                        .withAdListener(adListener)
                        .build());


        return interstitialAd;
    }



    public void getFBRewardedVideoAd() {

        try {
            if (rewardedVideoAd != null && rewardedVideoAd.isAdLoaded() && !rewardedVideoAd.isAdInvalidated()) {
                rewardedVideoAd.show();
                rewardedVideoAd = null;
            }
        }catch (Exception e){
            Log.i("Exception", e.getMessage());
        }

    }

    public RewardedVideoAd setFBRewardedVideoAdsID(String adsID, StoreUserID storeUserID, String taskNo){

//        AdSettings.setTestAdType(AdSettings.TestAdType.IMG_16_9_APP_INSTALL);
//        AdSettings.getTestAdType();

        rewardedVideoAd = new RewardedVideoAd((InterstitialAdsShow)context,adsID);
        RewardedVideoAdListener rewardedVideoAdListener = new RewardedVideoAdListener() {
            @Override
            public void onError(Ad ad, AdError error) {
               // rewardedVideoAd = null;
                Toast.makeText(context, "Sorry, error on loading the ad. Try again!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdLoaded(Ad ad) {
            }

            @Override
            public void onAdClicked(Ad ad) {
            }

            @Override
            public void onLoggingImpression(Ad ad) {
            }

            @Override
            public void onRewardedVideoCompleted() {
                Toast.makeText(context, "Ad completed, now give reward to user", Toast.LENGTH_SHORT).show();
                storeUserID.setCurrentRewardPoint(taskNo,storeUserID.getCurrentRewardPoint(taskNo)+10);

            }

            @Override
            public void onRewardedVideoClosed() {

            }
        };
        rewardedVideoAd.loadAd(
                rewardedVideoAd.buildLoadAdConfig()
                        .withAdListener(rewardedVideoAdListener)
                        .build());

        return rewardedVideoAd;
    }
}
