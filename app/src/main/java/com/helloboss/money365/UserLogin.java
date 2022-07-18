package com.helloboss.money365;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.helloboss.money365.databaseconnection.DBConnection;

public class UserLogin extends AppCompatActivity {

    int backPressedCount = 0;

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

    @Override
    public void onBackPressed() {

            if (backPressedCount >= 1) {
                super.onBackPressed();
                backPressedCount = 0;
                return;

            } else {
                backPressedCount = backPressedCount + 1;
                Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT).show();
            }


    }
}