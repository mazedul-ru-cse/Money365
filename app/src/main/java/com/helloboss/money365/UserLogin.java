package com.helloboss.money365;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.InstallState;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.OnSuccessListener;
import com.google.android.play.core.tasks.Task;
import com.helloboss.money365.requesthandler.RequestHandler;
import com.helloboss.money365.resetpasword.ForgotPassword;

import org.json.JSONObject;

import java.util.HashMap;

public class UserLogin extends AppCompatActivity {

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
        startActivity(new Intent(UserLogin.this , ForgotPassword.class));

    }

    public void create_an_account(View view) {

       startActivity(new Intent(UserLogin.this , UserRegistration.class));
    }

    @Override
    public void onBackPressed() {

        AlertDialog.Builder showNotice = new AlertDialog.Builder(this);
        showNotice.setTitle("Exit");
        showNotice.setMessage("Do you want to exit..?");
        showNotice.setCancelable(false);

        showNotice.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        exitApp();
                    }
                });
        showNotice.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

        showNotice.create();
        showNotice.show();


    }

    private void exitApp() {
        super.onBackPressed();
    }

    public void whatsapp(View view) {

        try {
            Uri uri = Uri.parse("https://chat.whatsapp.com/LV164EqgRGj6FPo7j8Zs29");
            startActivity(new Intent(Intent.ACTION_VIEW, uri));
        }catch (Exception e){

        }
    }

    public void linkedin(View view) {

        try {
            Uri uri = Uri.parse("https://linkedin.com/groups/14105858");
            startActivity(new Intent(Intent.ACTION_VIEW, uri));
        }catch (Exception e){

        }
    }

    public void telegram(View view) {

        try {
            Uri uri = Uri.parse("https://t.me/+a5an0Ui9z88zZDY9");
            startActivity(new Intent(Intent.ACTION_VIEW, uri));
        }catch (Exception e){

        }
    }

    public void facebook(View view) {

        try {
            Uri uri = Uri.parse("https://m.me/j/AbaezHyPgAWZ2z03/");
            startActivity(new Intent(Intent.ACTION_VIEW, uri));
        }catch (Exception e){

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