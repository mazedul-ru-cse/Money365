package com.helloboss.money365.leaderboard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.helloboss.money365.ProgressDialogM;
import com.helloboss.money365.R;
import com.helloboss.money365.requesthandler.RequestHandler;
import com.helloboss.noticeboard.NoticeAdapter;
import com.helloboss.noticeboard.NoticeModel;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class LeaderBoard extends AppCompatActivity {

    RecyclerView leaderBoardView;
    ProgressDialogM progressDialogM;

    LeaderBoardAdapter leaderBoardAdapter;
    ArrayList<LeaderBoardModel> leaderBoardModelArrayList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leader_board);
        getSupportActionBar().hide();

        leaderBoardView = findViewById(R.id.leader_board_list);
        // Initialize Recycler View
        leaderBoardView.setLayoutManager(new LinearLayoutManager(this));

        //Progress bar
        progressDialogM = new ProgressDialogM(this);
        //Show Notice board
        getLeaderBoard();
    }

    private void getLeaderBoard(){

        //Initialize adapter and model
        leaderBoardModelArrayList = new ArrayList<>();
        leaderBoardAdapter = new LeaderBoardAdapter(this, leaderBoardModelArrayList);
        leaderBoardView.setAdapter(leaderBoardAdapter);


        LeaderBoardModel leaderBoardModel = new LeaderBoardModel();
        //Data
        leaderBoardModel.setRank("1");
        leaderBoardModel.setName("M A Mazedul Islam");
        leaderBoardModel.setAmount("500");
        //set adapter
        leaderBoardModelArrayList.add(leaderBoardModel);

        LeaderBoardModel leaderBoardModel1 = new LeaderBoardModel();
        //Data
        leaderBoardModel1.setRank("2");
        leaderBoardModel1.setName("Helale");
        leaderBoardModel1.setAmount("100");
        //set adapter
        leaderBoardModelArrayList.add(leaderBoardModel1);

        LeaderBoardModel leaderBoardModel2 = new LeaderBoardModel();
        //Data
        leaderBoardModel2.setRank("10");
        leaderBoardModel2.setName("Rana Islam");
        leaderBoardModel2.setAmount("1500");
        //set adapter
        leaderBoardModelArrayList.add(leaderBoardModel2);

        leaderBoardAdapter.notifyDataSetChanged();

//        class LeaderBoardTask extends AsyncTask<String ,Void , String> {
//
//            final String LEADERBOARD_URL = "https://helloboss365.com/money365/leader_board.php";
//
//            @Override
//            protected String doInBackground(String... strings) {
//                try {
//
//                    RequestHandler requestHandler = new RequestHandler();
//
//                    //creating request parameters
//                    HashMap<String, String> params = new HashMap<>();
//                    params.put("phone","");
//
//                    //returning the response
//                    return requestHandler.sendPostRequest(LEADERBOARD_URL, params);
//
//                }catch (Exception e){
//
//                    progressDialogM.hideDialog();
//                    Log.i("Leader Board exception",e.getMessage());
//                }
//
//                return null;
//            }
//
//            @Override
//            protected void onPostExecute(String s) {
//                super.onPostExecute(s);
//
//                progressDialogM.hideDialog();
//
//                try{
//                    //Converting response to JSON Object
//                    JSONObject obj = new JSONObject(s);
//                    String row = "";
//                    //if no error in response
//                    if (!obj.getBoolean("error")){
//
//                        for(int i = 0 ; i < obj.length() - 1 ; i++){
//
//                            //Get each json row
//                            row = obj.getString(""+i);
//
//                            JSONObject data = new JSONObject(row);
//                            //set notice to recycler view
//                            LeaderBoardModel leaderBoardModel = new LeaderBoardModel();
//                            //Data
//                            leaderBoardModel.setRank(data.getString("rank"));
//                            leaderBoardModel.setName(data.getString("name"));
//                            leaderBoardModel.setAmount(data.getString("amount"));
//                            //set adapter
//                            leaderBoardModelArrayList.add(leaderBoardModel);
//                        }
//                        leaderBoardAdapter.notifyDataSetChanged();
//                    }
//                }catch (Exception e ){
//
//                }
//            }
//
//            @Override
//            protected void onPreExecute() {
//                super.onPreExecute();
//                progressDialogM.showDialog("Please wait");
//            }
//        }
//        new LeaderBoardTask().execute();

    }
}