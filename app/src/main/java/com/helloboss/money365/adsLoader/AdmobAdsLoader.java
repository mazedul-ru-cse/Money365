package com.helloboss.money365.adsLoader;
import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.helloboss.money365.StoreUserID;
import com.helloboss.money365.workshow.InterstitialAdsShow;

public class AdmobAdsLoader {

    InterstitialAd mInterstitialAd;
    Context context;
    RewardedAd mRewardedAd;

    public AdmobAdsLoader(Context context) {
        this.context = context;
    }

    public InterstitialAd setAdmobInterstitialAdsID(String adsID) {

        AdRequest adRequest = new AdRequest.Builder().build();

        InterstitialAd.load(context, adsID, adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {

                        mInterstitialAd = interstitialAd;
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {

                        mInterstitialAd = null;
                    }
                });

        return mInterstitialAd;
    }

    public void showAdmobInterstitialAdsID(StoreUserID storeUserID, String taskNo) {

        try {
            mInterstitialAd.show((InterstitialAdsShow) context);
            mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {


                @Override
                public void onAdDismissedFullScreenContent() {
                    super.onAdDismissedFullScreenContent();

                    mInterstitialAd = null;

                    //setAdmobInterAdsID(admobInterstitialAdsID);

                }

                @Override
                public void onAdClicked() {
                    super.onAdClicked();
                }

                @Override
                public void onAdImpression() {
                    super.onAdImpression();
                }

                @Override
                public void onAdShowedFullScreenContent() {
                    super.onAdShowedFullScreenContent();

                    storeUserID.setCurrentRewardPoint(taskNo,storeUserID.getCurrentRewardPoint(taskNo)+10);
                }
            });
        }catch (Exception e){

        }

    }


    public RewardedAd setAdmobRewardedAdID(String adsID) {

        AdRequest adRequest = new AdRequest.Builder().build();
        RewardedAd.load(context, adsID,
                adRequest, new RewardedAdLoadCallback() {
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error.
                        // Log.d(TAG, loadAdError.toString());
                        mRewardedAd = null;
                    }

                    @Override
                    public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                        mRewardedAd = rewardedAd;
                        // Log.d(TAG, "Ad was loaded.");
                    }
                });

        return mRewardedAd;
    }

    public void showAdmobRewardedAdsID(StoreUserID storeUserID, String taskNo) {

        try {

            mRewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {

                @Override
                public void onAdDismissedFullScreenContent() {
                    // Called when ad is dismissed.
                    // Set the ad reference to null so you don't show the ad a second time.

                    mRewardedAd = null;
                }

                @Override
                public void onAdFailedToShowFullScreenContent(AdError adError) {
                    // Called when ad fails to show.

                    mRewardedAd = null;
                }

            });

            mRewardedAd.show((InterstitialAdsShow) context, new OnUserEarnedRewardListener() {
                @Override
                public void onUserEarnedReward(@NonNull RewardItem rewardItem) {

//                    int rewardAmount = rewardItem.getAmount();
//                    String rewardType = rewardItem.getType();
//

                    storeUserID.setCurrentRewardPoint(taskNo,storeUserID.getCurrentRewardPoint(taskNo)+10);
                }
            });

        }catch (Exception e){

        }

    }

}
