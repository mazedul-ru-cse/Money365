package com.helloboss.money365.tips;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.helloboss.money365.ProgressDialogM;
import com.helloboss.money365.R;
import com.helloboss.money365.requesthandler.RequestHandler;
import com.helloboss.money365.userguide.UserGuideAdapter;
import com.helloboss.money365.userguide.UserGuideModel;
import com.unity3d.ads.UnityAds;
import com.unity3d.ads.UnityAdsShowOptions;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Tips extends AppCompatActivity {

    RecyclerView tipsView;
    TipsAdapter tipsAdapter;
    ArrayList<TipsModel> tipsModelArrayList;
    ProgressDialogM progressDialogM;
    AdView adView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tips);

        getSupportActionBar().hide();

        tipsView = findViewById(R.id.tips_list);
        // Initialize Recycler View
        tipsView.setLayoutManager(new LinearLayoutManager(this));

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


        //Progress bar
        progressDialogM = new ProgressDialogM(this);
        //Show notice
        getTips();
    }

    private void getAds() {
        try {
            UnityAds.show(this, "interstitialAds1", new UnityAdsShowOptions());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void getTips() {

        tipsModelArrayList = new ArrayList<>();
        tipsAdapter = new TipsAdapter(this, tipsModelArrayList);
        tipsView.setAdapter(tipsAdapter);

        class TipsTask extends AsyncTask<String ,Void , String> {

            final String TIPS_URL = "https://helloboss365.com/money365/tips.php";

            @Override
            protected String doInBackground(String... strings) {
                try {

                    RequestHandler requestHandler = new RequestHandler();

                    //creating request parameters
                    HashMap<String, String> params = new HashMap<>();
                    params.put("phone","");

                    //returning the response
                    return requestHandler.sendPostRequest(TIPS_URL, params);

                }catch (Exception e){

                    progressDialogM.hideDialog();
                    Log.i("Tips exception",e.getMessage());
                }

                return null;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                progressDialogM.hideDialog();

                try{
                    //Converting response to JSON Object
                    JSONObject obj = new JSONObject(s);
                    String row = "";
                    //if no error in response
                    if (!obj.getBoolean("error")){

                        for(int i = 0 ; i < obj.length() -1 ; i++){

                            //Get each json row
                            row = obj.getString(""+i);

                            JSONObject data = new JSONObject(row);
                            //set notice to recycler view
                            TipsModel tipsModel = new TipsModel();
                            //Data
                            tipsModel.setTitle(data.getString("title"));
                            tipsModel.setIconId(data.getString("icon"));
                            tipsModel.setLink(data.getString("link"));
                            //set adapter
                            tipsModelArrayList.add(tipsModel);

                        }
                        tipsAdapter.notifyDataSetChanged();

                        getAds();
                    }
                }catch (Exception e ){

                }
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialogM.showDialog("Please wait");
            }
        }
        new TipsTask().execute();

    }
}