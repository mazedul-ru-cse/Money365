package com.helloboss.money365.task;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.helloboss.money365.ProgressDialogM;
import com.helloboss.money365.R;
import com.helloboss.money365.workshow.InterstitialAdsShow;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Task extends AppCompatActivity {

    public static int taskRange = 10;

    public static int taskNo = 1;


    public static String taskAds = "fb";
    public static String taskAdsTypes = "interstitial";


    ProgressDialogM progressDialogM;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        getSupportActionBar().hide();

        progressDialogM = new ProgressDialogM(this);

        new Task1().execute();

    }

    public void task1(View view) {
        new Task1().execute(1);

    }
    public void task2(View view) {
        new Task1().execute(2);
    }
    public void task3(View view) {
        new Task1().execute(3);
    }
    public void task4(View view) {
        new Task1().execute(4);
    }

    class Task1 extends AsyncTask<Integer,Void , Integer> {

        private static final String URL ="jdbc:mysql://sql6.freemysqlhosting.net:3306/sql6507108";
        private static final String USER = "sql6507108";
        private static final String PASSWORD = "pkRblaKCf5";

        Connection connection;

        @Override
        protected Integer doInBackground(Integer... task) {
            Statement st;
            ResultSet resultSet;

            try {

                Class.forName("com.mysql.jdbc.Driver");
                connection = DriverManager.getConnection(URL,USER,PASSWORD);

                String query = "SELECT * from `task` where `task_no`="+task[0];

                st = connection.createStatement();
                resultSet = st.executeQuery(query);

                if(resultSet.next()) {

                    taskNo = resultSet.getInt("task_no");
                    taskRange = resultSet.getInt("task_range");
                    taskAdsTypes = resultSet.getString("ads_type");
                    taskAds = resultSet.getString("ads");

                }
                connection.close();

            }catch (Exception e){

                progressDialogM.hideDialog();
                Log.i("Task exception",e.getMessage());
                return 0;

            }

            return 1;
        }

        @Override
        protected void onPostExecute(Integer s) {

            super.onPostExecute(s);
            progressDialogM.hideDialog();

            if(s == 1)
                startActivity(new Intent(Task.this , InterstitialAdsShow.class));
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialogM.showDialog("Task loading..!");
        }
    }


}