package com.helloboss.money365;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.helloboss.money365.databaseconnection.DBConnection;

public class UserRegistration extends AppCompatActivity {

    EditText editName, editEmail, editPhone, editPassword1, editPasswordConfirm , editRefer;
    ProgressBar progressBar;
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
      progressBar = findViewById(R.id.registration_progress);


    }

    public void user_register(View view) {

        String name = editName.getText().toString();
        String email = editEmail.getText().toString();
        String phone = editPhone.getText().toString();
        String password1 = editPassword1.getText().toString();
        String password2 = editPasswordConfirm.getText().toString();
        String referCode = editRefer.getText().toString();

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
        if(password1.isEmpty()){
            editPassword1.setError("Password is required");
            return;
        }
        if(password2.isEmpty()){
            editPasswordConfirm.setError("Password is required");
            return;
        }
        if(referCode.isEmpty()){
            referCode = "#";

        }

        try{
            progressBar.setActivated(true);

            AsyncTask<String, Void, String> message  =  new DBConnection().execute("New Account", name , email,password1, phone, referCode);

            progressBar.setActivated(false);

            if(message.get().equals("created")){

                Toast.makeText(this, "Account created successfully", Toast.LENGTH_SHORT).show();

            }else{
                editPhone.setError("Phone number is already used");
                return;
            }

        }catch (Exception e){
            Toast.makeText(this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
        }

    }

    public void already_registered(View view) {
        startActivity(new Intent(UserRegistration.this, UserLogin.class));
    }
}