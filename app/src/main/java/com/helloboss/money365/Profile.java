package com.helloboss.money365;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Profile extends AppCompatActivity {

    TextView tvName, tvEmail , tvTk, tvPointBalance, tvTotalRefer, tvReferCode;
    ProgressDialogM progressDialogM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getSupportActionBar().hide();

        tvName = findViewById(R.id.p_user_name);
        tvEmail = findViewById(R.id.p_user_email);
        tvTk = findViewById(R.id.p_tk_balance);
        tvPointBalance = findViewById(R.id.p_point_balance);
        tvTotalRefer = findViewById(R.id.p_refer_count);
        tvReferCode = findViewById(R.id.p_refer_code);

        progressDialogM = new ProgressDialogM(this);

        new ProfileTask().execute();

    }


    class ProfileTask extends AsyncTask<String ,Void , String>{

        private static final String URL ="jdbc:mysql://sql6.freemysqlhosting.net:3306/sql6507108";
        private static final String USER = "sql6507108";
        private static final String PASSWORD = "pkRblaKCf5";
        String name = null, email = null, tk= null, coin= null, totalRefer= null,referCode= null;
        Connection connection;

        @Override
        protected String doInBackground(String... strings) {
            Statement st;
            ResultSet resultSet;

            try {

                Class.forName("com.mysql.jdbc.Driver");
                connection = DriverManager.getConnection(URL,USER,PASSWORD);

                String query = "SELECT a.`name`, a.`email`, b.`coin`, b.`taka`, " +
                        "b.`refer_code`, b.`refer_count` FROM `user_list` a , `account_details` b" +
                        " where a.`phone`="+ UserLogin.userID+ " and b.`phone`="+UserLogin.userID;

                st = connection.createStatement();
                resultSet = st.executeQuery(query);

                if(resultSet.next()) {
                    name = resultSet.getString("name");
                    email = resultSet.getString("email");
                    tk = resultSet.getString("taka");
                    coin = resultSet.getString("coin");
                    totalRefer = resultSet.getString("refer_count");
                    referCode = resultSet.getString("refer_code");

                    Log.i("Profile data", name+ "  "+email+"  "+tk+"  "+totalRefer);
                }

                connection.close();

            }catch (Exception e){

                progressDialogM.hideDialog();
                Log.i("Profile exception",e.getMessage());

            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            tvName.setText(name);
            tvEmail.setText(email);
            tvTk.setText("TK : "+tk);
            tvPointBalance.setText(coin);
            tvTotalRefer.setText(totalRefer);
            tvReferCode.setText(referCode);
            progressDialogM.hideDialog();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialogM.showDialog("Please wait");
        }
    }

}