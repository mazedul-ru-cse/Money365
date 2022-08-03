package com.helloboss.money365.task;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import com.helloboss.money365.BreakTimer;
import com.helloboss.money365.Dashboard;
import com.helloboss.money365.ProgressDialogM;
import com.helloboss.money365.R;
import com.helloboss.money365.StoreUserID;
import com.helloboss.money365.UserLogin;
import com.helloboss.money365.requesthandler.RequestHandler;
import com.helloboss.money365.workshow.InterstitialAdsShow;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;

public class Task extends AppCompatActivity {

    public static int taskRange = 10;
    public static int taskNo = 1;
    public static String taskAds = "fb";
    public static String taskAdsTypes = "interstitial";


    ProgressDialogM progressDialogM;
    BreakTimer breakTimer;
    StoreUserID storeUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        getSupportActionBar().hide();

        progressDialogM = new ProgressDialogM(this);


        breakTimer = new BreakTimer(this);
        storeUserID = new StoreUserID(this);

        if(breakTimer.isBreakTime(storeUserID.getBreakTime())){

            breakTimer.showCounterDownTimer();
        }

    }

    public void task1(View view) {
        new Task1().execute("1");
    }
    public void task2(View view) {
        new Task1().execute("2");
    }
    public void task3(View view) {
        new Task1().execute("3");
    }
    public void task4(View view) {
        new Task1().execute("4");
    }

    class Task1 extends AsyncTask<String,Void , String> {

        final String TASK_URL = "https://helloboss365.com/money365/task.php";

        @Override
        protected String doInBackground(String... task) {


            try {


                RequestHandler requestHandler = new RequestHandler();

                //creating request parameters
                HashMap<String, String> params = new HashMap<>();
                params.put("task_no", task[0]);

                //returning the response
                return requestHandler.sendPostRequest(TASK_URL, params);


            }catch (Exception e) {

                progressDialogM.hideDialog();
                Log.i("Task exception", e.getMessage());
                return null;

            }
        }

        @Override
        protected void onPostExecute(String s) {

            super.onPostExecute(s);
            progressDialogM.hideDialog();

            try {
                Log.i("Json",s);
                JSONObject obj = new JSONObject(s);
                if(!obj.getBoolean("error")) {

                    taskNo = obj.getInt("task_no");
                    taskRange = obj.getInt("task_range");
                    taskAdsTypes = obj.getString("ads_type");
                    taskAds = obj.getString("ads");

                    startActivity(new Intent(Task.this, InterstitialAdsShow.class));
                    finish();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialogM.showDialog("Task loading..!");
        }
    }
}