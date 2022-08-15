package com.helloboss.money365.resetpasword;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.helloboss.money365.ProgressDialogM;
import com.helloboss.money365.R;
import com.helloboss.money365.UserLogin;
import com.helloboss.money365.requesthandler.RequestHandler;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Random;

public class ForgotPassword extends AppCompatActivity {

    EditText fPhone, fEmail;
    private ProgressDialogM progressDialogM;
    public static String otpCode,otpEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        getSupportActionBar().hide();

        fPhone = findViewById(R.id.f_phone);
        fEmail = findViewById(R.id.f_email);

        progressDialogM = new ProgressDialogM(this);
    }

    public void reset_password(View view) {

        String phone;

        otpCode = getOTP();
        otpEmail = fEmail.getText().toString();
        phone = fPhone.getText().toString();

        if(otpEmail.isEmpty()){
            return;
        }

        if(phone.isEmpty()){
            return;
        }

        UserLogin.userID = phone;

        class resetPasswordTask extends AsyncTask<String,Void , String> {


            final String RESET_PASSWORD_URL = "https://helloboss365.com/money365/reset_password.php";
            @Override
            protected String doInBackground(String... task) {

                try {

                    RequestHandler requestHandler = new RequestHandler();

                   // Log.i("OTP code", otpCode);
                    //creating request parameters
                    HashMap<String, String> params = new HashMap<>();
                    params.put("email", otpEmail);
                    params.put("phone", phone);
                    params.put("otp", otpCode);

                    //returning the response
                    return requestHandler.sendPostRequest(RESET_PASSWORD_URL, params);


                }catch (Exception e) {

                    progressDialogM.hideDialog();
                    return null;

                }
            }

            @Override
            protected void onPostExecute(String s) {

                super.onPostExecute(s);
                progressDialogM.hideDialog();



                try {
                    JSONObject obj = new JSONObject(s);

                    if(!obj.getBoolean("error")){

                        startActivity(new Intent(ForgotPassword.this , Verification.class));
                        finish();
                    }else{
                        fPhone.setError("Not found");
                        fEmail.setError("Not found");
                        Toast.makeText(ForgotPassword.this,obj.getString("message"), Toast.LENGTH_SHORT).show();
                    }


                }catch (Exception e){

                    Toast.makeText(ForgotPassword.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                }
            }


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialogM.showDialog("Please wait..");
            }
        }

        new resetPasswordTask().execute();
    }

    private String getOTP() {
        return new DecimalFormat("0000").format(new Random().nextInt(9999));
    }
}