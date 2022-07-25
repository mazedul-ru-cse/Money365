package com.helloboss.money365.adsLoader;
import android.content.Context;
import androidx.annotation.NonNull;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.helloboss.money365.StoreUserID;
import com.helloboss.money365.workshow.InterstitialAdsShow;

public class AdsLoader {

    InterstitialAd mInterstitialAd;
    Context context;
    String fbInterstitialAdsID;

    String admobInterstitialAdsID;
    String fbRewardAdsID;
    String admobRewardAdsID;

    public AdsLoader(Context context) {
        this.context = context;
    }

    public InterstitialAd setAdmobInterAdsID(String adsID) {

        admobInterstitialAdsID = adsID;
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

                    storeUserID.setCurrentRewardPoint(taskNo,storeUserID.getCurrentRewardPoint(taskNo)+15);
                }
            });
        }catch (Exception e){

        }

    }


}
