package com.helloboss.money365;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.helloboss.money365.requesthandler.RequestHandler;

import org.json.JSONObject;

import java.util.HashMap;

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
            return;
        }


        if(password.isEmpty()){
            editPassword.setError("Password is required");

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

        String phone = editPhone.getText().toString();
        String password = editPassword.getText().toString();


        @Override
        protected String doInBackground(String... strings) {

            final String LOGIN_URL = "https://helloboss365.com/money365/user_login.php";

            try {
                RequestHandler requestHandler = new RequestHandler();

                //creating request parameters
                HashMap<String, String> params = new HashMap<>();
                params.put("phone", phone);

                //returning the response
                return requestHandler.sendPostRequest(LOGIN_URL, params);

            }catch (Exception e){

                progressDialogM.hideDialog();
               // Log.i("Profile exception",e.getMessage());
                return "no";

            }

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            progressDialogM.hideDialog();

            try{
                //Converting response to JSON Object
                JSONObject obj = new JSONObject(s);

                //if no error in response
                if (!obj.getBoolean("error")){

                    if(obj.getString("password").equals(password)){
                        Toast.makeText(UserLogin.this, obj.getString("message"), Toast.LENGTH_LONG).show();
                        userID = phone;
                        storeUserID.setUserIDPassword(phone,password, true);
                        startActivity(new Intent(UserLogin.this , Dashboard.class));
                        finish();

                    }else{
                        editPhone.setError("Wrong");
                        editPassword.setError("Wrong");
                    }
                }
            } catch (Exception e ){
                Toast.makeText(UserLogin.this, "Exception: "+e, Toast.LENGTH_SHORT).show();
            }

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialogM.showDialog("Login");
        }
    }

}