package com.helloboss.money365.adsLoader;
import android.app.Activity;
import android.content.Context;
import android.hardware.display.DisplayManager;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.helloboss.money365.Dashboard;
import com.helloboss.money365.R;
import com.helloboss.money365.StoreUserID;

import com.helloboss.money365.workshow.InterstitialAdsShow;
import com.startapp.sdk.adsbase.Ad;
import com.startapp.sdk.adsbase.StartAppAd;
import com.startapp.sdk.adsbase.adlisteners.AdDisplayListener;
import com.startapp.sdk.adsbase.adlisteners.AdEventListener;
import com.startapp.sdk.adsbase.adlisteners.VideoListener;
import com.unity3d.ads.IUnityAdsLoadListener;
import com.unity3d.ads.IUnityAdsShowListener;
import com.unity3d.ads.UnityAds;
import com.unity3d.ads.UnityAdsShowOptions;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.concurrent.ExecutionException;

public class UnityAdsLoader {

    Context context;
    String interstitial;
    String rewardedAd;
    TextView btnPlay;
    StartAppAd interstitialStartIO;
    StartAppAd rewardedStartIO;

    StartAppAd ads;

    public UnityAdsLoader(Context context,TextView btnPlay) {
        this.context = context;
        this.btnPlay = btnPlay;

    }

    public boolean setInterstitialAds(String adsID,StoreUserID storeUserID, String taskNo){

        try {

            interstitial = adsID;
            interstitialStartIO = new StartAppAd(context);

            interstitialStartIO.setVideoListener(new VideoListener() {
                @Override
                public void onVideoCompleted() {
                   // showUnityInterstitialAdsID(storeUserID, taskNo);
                }

            });

            interstitialStartIO.loadAd(StartAppAd.AdMode.FULLPAGE, new AdEventListener() {
                @Override
                public void onReceiveAd(@NonNull Ad ad) {
                    setUnityInterstitialAdsID();
                }

                @Override
                public void onFailedToReceiveAd(@Nullable Ad ad) {
                    setUnityInterstitialAdsID();
                }
            });

        }catch (Exception e){
            setUnityInterstitialAdsID();
            e.printStackTrace();
            return true;
        }
        return true;
    }

    public void showInAds(StoreUserID storeUserID, String taskNo){

        interstitialStartIO.showAd(new AdDisplayListener() {
            @Override
            public void adHidden(Ad ad) {
               showUnityInterstitialAdsID(storeUserID, taskNo);
            }

            @Override
            public void adDisplayed(Ad ad) {

            }

            @Override
            public void adClicked(Ad ad) {

            }

            @Override
            public void adNotDisplayed(Ad ad) {
                showUnityInterstitialAdsID(storeUserID, taskNo);
            }
        });
    }

    public void setUnityInterstitialAdsID() {

        UnityAds.initialize(context.getApplicationContext(),context.getString(R.string.unity_game_id));

        try {
            UnityAds.load(interstitial);

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void showUnityInterstitialAdsID(StoreUserID storeUserID, String taskNo) {

        ads = new StartAppAd(context);

        IUnityAdsShowListener showListener = new IUnityAdsShowListener(){

            @Override
            public void onUnityAdsShowFailure(String placementId, UnityAds.UnityAdsShowError error, String message) {
                bounceAd(storeUserID, taskNo);
            }

            @Override
            public void onUnityAdsShowStart(String placementId) {

               // Toast.makeText(context, "onUnityAdsShowStart", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onUnityAdsShowClick(String placementId) {

               // Toast.makeText(context, "onUnityAdsShowClick", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onUnityAdsShowComplete(String placementId, UnityAds.UnityAdsShowCompletionState state) {
                storeUserID.setCurrentRewardPoint(taskNo,storeUserID.getCurrentRewardPoint(taskNo)+Dashboard.rPoint);
               // Toast.makeText(context, "onUnityAdsShowComplete", Toast.LENGTH_SHORT).show();
                bounceAd(storeUserID, taskNo);
            }
        };

        try {
            UnityAds.show((Activity)context, interstitial, new UnityAdsShowOptions(), showListener);

        }catch (Exception e){
            bounceAd(storeUserID, taskNo);
            e.printStackTrace();
        }

    }


    public void bounceAd(StoreUserID storeUserID, String taskNo){

        try {
           // StartAppAd.showAd(context);

            ads.setVideoListener(new VideoListener() {
                @Override
                public void onVideoCompleted() {

                }
            });

            ads.showAd(new AdDisplayListener() {

                @Override
                public void adHidden(Ad ad) {

                }

                @Override
                public void adDisplayed(Ad ad) {
                    storeUserID.setCurrentRewardPoint(taskNo,storeUserID.getCurrentRewardPoint(taskNo)+ Dashboard.bPoint);
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


    public boolean setRewardAds(String adsID,StoreUserID storeUserID, String taskNo){

        try {
            rewardedAd = adsID;

            rewardedStartIO = new StartAppAd(context);

            rewardedStartIO.setVideoListener(new VideoListener() {
                @Override
                public void onVideoCompleted() {
                    showUnityRewardedAdsID(storeUserID, taskNo);
                }

            });

            rewardedStartIO.loadAd(StartAppAd.AdMode.FULLPAGE, new AdEventListener() {
                @Override
                public void onReceiveAd(@NonNull Ad ad) {

                }

                @Override
                public void onFailedToReceiveAd(@Nullable Ad ad) {

                }
            });

        }catch (Exception e){
            setUnityRewardedAdID();
            e.printStackTrace();
            return true;
        }
        return true;
    }

    public void showRewardAds(StoreUserID storeUserID, String taskNo){

        rewardedStartIO.showAd(new AdDisplayListener() {
            @Override
            public void adHidden(Ad ad) {

            }

            @Override
            public void adDisplayed(Ad ad) {
                setUnityRewardedAdID();
            }

            @Override
            public void adClicked(Ad ad) {

            }

            @Override
            public void adNotDisplayed(Ad ad) {
                showUnityRewardedAdsID(storeUserID, taskNo);
            }
        });
    }

    public void setUnityRewardedAdID() {

        UnityAds.initialize(context.getApplicationContext(),context.getString(R.string.unity_game_id));

        try {
            UnityAds.load(rewardedAd);
        }catch (Exception e){
            e.printStackTrace();

        }

    }

    public void showUnityRewardedAdsID(StoreUserID storeUserID, String taskNo) {

        ads = new StartAppAd(context);

        IUnityAdsShowListener showListener = new IUnityAdsShowListener(){

            @Override
            public void onUnityAdsShowFailure(String placementId, UnityAds.UnityAdsShowError error, String message) {
                bounceAd(storeUserID, taskNo);
            }

            @Override
            public void onUnityAdsShowStart(String placementId) {


            }

            @Override
            public void onUnityAdsShowClick(String placementId) {


            }

            @Override
            public void onUnityAdsShowComplete(String placementId, UnityAds.UnityAdsShowCompletionState state) {
                storeUserID.setCurrentRewardPoint(taskNo,storeUserID.getCurrentRewardPoint(taskNo)+ Dashboard.rPoint);
                bounceAd(storeUserID, taskNo);
            }
        };

        try {
            UnityAds.show((Activity)context, rewardedAd, new UnityAdsShowOptions(), showListener);

        }catch (Exception e){
            e.printStackTrace();
        }

    }

}
