package com.helloboss.money365;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class UserLogin extends AppCompatActivity {

    int backPressedCount = 0;
    EditText editPhone, editPassword;
    ProgressDialogM progressDialogM;
    TextView btnLogin;

    public static String userID;
    StoreUserID storeUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);

        getSupportActionBar().hide();

        editPhone = findViewById(R.id.login_phone);
        editPassword = findViewById(R.id.login_password);
        btnLogin = findViewById(R.id.bt_login);

        storeUserID = new StoreUserID(this);
        progressDialogM = new ProgressDialogM(this);

        //Check user login or not
        if(storeUserID.getLoginStatus()){

            //progressDialogM.showDialog("Login");

            userID = storeUserID.getUserID();

            startActivity(new Intent(UserLogin.this , Dashboard.class));
            finish();
        }


    }

    public void login(View view) {

        String phone = editPhone.getText().toString();
        String password = editPassword.getText().toString();

        if(phone.isEmpty()){
            editPhone.setError("Phone number is required");
            progressDialogM.hideDialog();
            return;
        }


        if(password.isEmpty()){
            editPassword.setError("Password is required");
            progressDialogM.hideDialog();

            return;
        }

        new LoginTask().execute();

    }

    public void forget_password(View view) {


    }

    public void create_an_account(View view) {

       startActivity(new Intent(UserLogin.this , UserRegistration.class));
    }

    @Override
    public void onBackPressed() {

        progressDialogM.hideDialog();
        if (backPressedCount >= 1) {
            super.onBackPressed();
            backPressedCount = 0;
            return;

        } else {
            backPressedCount = backPressedCount + 1;
            Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT).show();
        }


    }

    class LoginTask extends AsyncTask<String ,Void , String>{

        private static final String URL ="jdbc:mysql://sql6.freemysqlhosting.net:3306/sql6507108";
        private static final String USER = "sql6507108";
        private static final String PASSWORD = "pkRblaKCf5";

        String phone = editPhone.getText().toString();
        String password = editPassword.getText().toString();

        Connection connection;

        @Override
        protected String doInBackground(String... strings) {
            Statement st;
            ResultSet resultSet;

            try {

                Class.forName("com.mysql.jdbc.Driver");
                connection = DriverManager.getConnection(URL,USER,PASSWORD);

                String query = "SELECT `password` FROM `user_list` WHERE `phone`="+phone;

                st = connection.createStatement();
                resultSet = st.executeQuery(query);

                String userPassword = null;

                if(resultSet.next()) {
                    userPassword = resultSet.getString("password");
                }

                if(password.equals(userPassword)){
                    return "yes";
                }

                connection.close();

            }catch (Exception e){

                progressDialogM.hideDialog();
                Log.i("Profile exception",e.getMessage());
                return "no";

            }

            return "no";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            progressDialogM.hideDialog();

            if(s.equals("yes")){
                userID = phone;
                storeUserID.setUserIDPassword(phone,password, true);
                startActivity(new Intent(UserLogin.this , Dashboard.class));
                finish();
            }
            else{
                editPhone.setError("Wrong");
                editPassword.setError("Wrong");
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialogM.showDialog("Login");
        }
    }

}