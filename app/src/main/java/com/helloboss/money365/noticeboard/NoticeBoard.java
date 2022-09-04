package com.helloboss.money365.noticeboard;

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
import com.unity3d.ads.UnityAds;
import com.unity3d.ads.UnityAdsShowOptions;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class NoticeBoard extends AppCompatActivity {

    RecyclerView noticeView;
    NoticeAdapter noticeAdapter;
    ArrayList<NoticeModel> noticeModelArrayList;
    ProgressDialogM progressDialogM;
    AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_board);
        getSupportActionBar().hide();

        noticeView = findViewById(R.id.notice_list);
        // Initialize Recycler View
        noticeView.setLayoutManager(new LinearLayoutManager(this));


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
        getNotice();

    }

    private void getAds() {
        try {
            UnityAds.show(this, "interstitialAds1", new UnityAdsShowOptions());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void getNotice() {
        noticeModelArrayList = new ArrayList<>();
        noticeAdapter = new NoticeAdapter(this, noticeModelArrayList);
        noticeView.setAdapter(noticeAdapter);

        class NoticeTask extends AsyncTask<String ,Void , String> {

            final String NOTICE_URL = "https://helloboss365.com/money365/notice.php";

            @Override
            protected String doInBackground(String... strings) {
                try {

                    RequestHandler requestHandler = new RequestHandler();

                    //creating request parameters
                    HashMap<String, String> params = new HashMap<>();
                    params.put("phone","");

                    //returning the response
                    return requestHandler.sendPostRequest(NOTICE_URL, params);

                }catch (Exception e){

                    progressDialogM.hideDialog();
                    Log.i("Notice exception",e.getMessage());
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
                            NoticeModel noticeModel = new NoticeModel();
                            //Data
                            noticeModel.setTitle(data.getString("title"));
                            noticeModel.setDate(data.getString("date"));
                            noticeModel.setBody(data.getString("body"));
                            //set adapter
                            noticeModelArrayList.add(noticeModel);

                        }
                        noticeAdapter.notifyDataSetChanged();
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
        new NoticeTask().execute();

    }
}