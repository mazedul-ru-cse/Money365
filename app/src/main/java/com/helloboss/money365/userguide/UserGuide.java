package com.helloboss.money365.userguide;

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
import com.helloboss.money365.noticeboard.NoticeAdapter;
import com.helloboss.money365.noticeboard.NoticeModel;
import com.helloboss.money365.requesthandler.RequestHandler;
import com.unity3d.ads.UnityAds;
import com.unity3d.ads.UnityAdsShowOptions;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class UserGuide extends AppCompatActivity {

    RecyclerView userGuideView;
    UserGuideAdapter userGuideAdapter;
    ArrayList<UserGuideModel> userGuideModelArrayList;
    ProgressDialogM progressDialogM;
    AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_guide);
        getSupportActionBar().hide();

        userGuideView = findViewById(R.id.user_guide_list);
        // Initialize Recycler View
        userGuideView.setLayoutManager(new LinearLayoutManager(this));

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
        getUserGuide();

    }
    private void getAds() {
        try {
            UnityAds.show(this, "interstitialAds1", new UnityAdsShowOptions());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void getUserGuide() {

        userGuideModelArrayList = new ArrayList<>();
        userGuideAdapter = new UserGuideAdapter(this, userGuideModelArrayList);
        userGuideView.setAdapter(userGuideAdapter);

        class UserGuideTask extends AsyncTask<String ,Void , String> {

            final String USERGUIDE_URL = "https://helloboss365.com/money365/user_guide.php";

            @Override
            protected String doInBackground(String... strings) {
                try {

                    RequestHandler requestHandler = new RequestHandler();

                    //creating request parameters
                    HashMap<String, String> params = new HashMap<>();
                    params.put("phone","");

                    //returning the response
                    return requestHandler.sendPostRequest(USERGUIDE_URL, params);

                }catch (Exception e){

                    progressDialogM.hideDialog();
                    Log.i("User Guide exception",e.getMessage());
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
                            UserGuideModel userGuideModel = new UserGuideModel();
                            //Data
                            userGuideModel.setTitle(data.getString("title"));
                            userGuideModel.setIconId(data.getString("icon"));
                            userGuideModel.setLink(data.getString("link"));
                            //set adapter
                            userGuideModelArrayList.add(userGuideModel);

                        }
                        userGuideAdapter.notifyDataSetChanged();

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
        new UserGuideTask().execute();

    }


}