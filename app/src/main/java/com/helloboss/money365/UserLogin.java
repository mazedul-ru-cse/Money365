package com.helloboss.money365;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class UserLogin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);

        getSupportActionBar().hide();

    }

    public void login(View view) {
        startActivity(new Intent(UserLogin.this , Dashboard.class));
        finish();

    }

    public void forget_password(View view) {
    }

    public void create_an_account(View view) {
        startActivity(new Intent(UserLogin.this , UserRegistration.class));
    }
}