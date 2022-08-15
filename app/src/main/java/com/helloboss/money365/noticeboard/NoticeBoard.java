package com.helloboss.money365.noticeboard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.helloboss.money365.ProgressDialogM;
import com.helloboss.money365.R;
import com.helloboss.money365.requesthandler.RequestHandler;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class NoticeBoard extends AppCompatActivity {

    RecyclerView noticeView;
    NoticeAdapter noticeAdapter;
    ArrayList<NoticeModel> noticeModelArrayList;
    ProgressDialogM progressDialogM;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_board);
        getSupportActionBar().hide();

        noticeView = findViewById(R.id.notice_list);
        // Initialize Recycler View
        noticeView.setLayoutManager(new LinearLayoutManager(this));

        //Progress bar
        progressDialogM = new ProgressDialogM(this);
        //Show notice
        getNotice();
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