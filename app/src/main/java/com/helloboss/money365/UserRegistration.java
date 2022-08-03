package com.helloboss.money365;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.helloboss.money365.requesthandler.RequestHandler;


import org.json.JSONObject;

import java.util.HashMap;

public class UserRegistration extends AppCompatActivity {

    EditText editName, editEmail, editPhone, editPassword1, editPasswordConfirm , editRefer;
    String  name , email,password,password2, phone, refer;

    ProgressDialogM progressDialogM;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_registration);

        getSupportActionBar().hide();
        editName = findViewById(R.id.reg_user_name);
        editEmail = findViewById(R.id.reg_email);
        editPassword1 = findViewById(R.id.reg_password);
        editPasswordConfirm = findViewById(R.id.reg_confirm_password);
        editPhone = findViewById(R.id.reg_phone);
        editRefer = findViewById(R.id.reg_refer_code);

        progressDialogM = new ProgressDialogM(this);

    }

    public void user_register(View view) {

        name = editName.getText().toString();
        email = editEmail.getText().toString();
        phone = editPhone.getText().toString();
        password = editPassword1.getText().toString();
        password2 = editPasswordConfirm.getText().toString();
        refer = editRefer.getText().toString();

        if(name.isEmpty()){
            editName.setError("Name is required");
            return;
        }
        if(email.isEmpty()){
            editEmail.setError("Email is required");
            return;
        }
        if(phone.isEmpty()){
            editPhone.setError("Phone number is required");
            return;
        }
        if(password.isEmpty()){
            editPassword1.setError("Password is required");
            return;
        }
        if(password2.isEmpty()){
            editPasswordConfirm.setError("Password is required");
            return;
        }

        if(!password.equals(password2)){

            editPasswordConfirm.setError("Not matched...!");
            return;
        }
        if(refer.isEmpty()){
            refer = "#";

        }

       new RegistrationTask().execute();

    }

    public void already_registered(View view) {
        startActivity(new Intent(UserRegistration.this, UserLogin.class));
    }


    class RegistrationTask extends AsyncTask<String ,Void , String>{

        final String USER_REGISTRATION = "https://helloboss365.com/money365/registration.php";

        @Override
        protected String  doInBackground(String... strings) {

            try{

                //creating request handler object
                RequestHandler requestHandler = new RequestHandler();

                //creating request parameters
                HashMap<String, String> params = new HashMap<>();
                params.put("phone", phone);
                params.put("name", name);
                params.put("email", email);
                params.put("password", password);
                params.put("refer", refer);

                //returing the response
                return requestHandler.sendPostRequest(USER_REGISTRATION, params);


            }catch (Exception e){

                Log.i("Registration Exception",e.getMessage());
                Log.i("Registration Exception",e.getStackTrace().toString());
                return "no";
            }
        }

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            progressDialogM.showDialog("Please wait");
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialogM.hideDialog();

            try {
                //converting response to json object
                JSONObject obj = new JSONObject(s);
                //if no error in response
                if (!obj.getBoolean("error")) {
                    Toast.makeText(getApplicationContext(), "Account created successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(UserRegistration.this, UserLogin.class));
                    finish();
                }else{
                    editPhone.setError("Phone number is already used");
                }
            } catch (Exception e) {
                Toast.makeText(UserRegistration.this, "Exception: "+e, Toast.LENGTH_LONG).show();
            }
        }
    }
}

