package com.helloboss.money365.leaderboard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;

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
import java.util.concurrent.ExecutionException;

public class LeaderBoard extends AppCompatActivity {

    RecyclerView leaderBoardView;
    ProgressDialogM progressDialogM;

    LeaderBoardAdapter leaderBoardAdapter;
    ArrayList<LeaderBoardModel> leaderBoardModelArrayList;
    AdView adView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leader_board);
        getSupportActionBar().hide();

        leaderBoardView = findViewById(R.id.leader_board_list);
        // Initialize Recycler View
        leaderBoardView.setLayoutManager(new LinearLayoutManager(this));

        try {
            UnityAds.initialize(getApplicationContext(), getString(R.string.unity_game_id), false);
            UnityAds.load("interstitialAds1");
        }catch (Exception e){
            e.printStackTrace();
        }

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });


        //Progress bar
        progressDialogM = new ProgressDialogM(this);
        //Show Notice board
        getLeaderBoard();
    }

    private void getAds() {
        try {
            UnityAds.show(this, "interstitialAds1", new UnityAdsShowOptions());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void getLeaderBoard(){

        //Initialize adapter and model
        leaderBoardModelArrayList = new ArrayList<>();
        leaderBoardAdapter = new LeaderBoardAdapter(this, leaderBoardModelArrayList);
        leaderBoardView.setAdapter(leaderBoardAdapter);

        class LeaderBoardTask extends AsyncTask<String ,Void , String> {

            final String LEADERBOARD_URL = "https://helloboss365.com/money365/leader_board.php";

            @Override
            protected String doInBackground(String... strings) {
                try {

                    RequestHandler requestHandler = new RequestHandler();

                    //creating request parameters
                    HashMap<String, String> params = new HashMap<>();
                    params.put("phone","");

                    //returning the response
                    return requestHandler.sendPostRequest(LEADERBOARD_URL, params);

                }catch (Exception e){

                    progressDialogM.hideDialog();
                    Log.i("Leader Board exception",e.getMessage());
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

                        for(int i = 0 ; i < obj.length() - 1 ; i++){

                            //Get each json row
                            row = obj.getString(""+i);
                           // Log.i((i+1)+"",row);

                            JSONObject data = new JSONObject(row);
                            //set notice to recycler view
                            LeaderBoardModel leaderBoardModel = new LeaderBoardModel();
                            //Data
                            leaderBoardModel.setRank((i+1)+"");
                            leaderBoardModel.setName(data.getString("name"));
                            leaderBoardModel.setAmount(data.getString("amount"));
                            //set adapter
                            leaderBoardModelArrayList.add(leaderBoardModel);
                        }

                        leaderBoardAdapter.notifyDataSetChanged();
                        getAds();
                    }
                }catch (Exception e ){
                    e.getMessage();

                }
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialogM.showDialog("Please wait");
            }
        }
        new LeaderBoardTask().execute();

    }
}