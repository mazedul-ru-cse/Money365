package com.helloboss.money365;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.TextView;

public class Contact extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        getSupportActionBar().hide();
    }

    public void call_now(View view) {

        TextView phone = findViewById(R.id.c_phone);
        try {
            phone.setMovementMethod(LinkMovementMethod.getInstance());
        }catch (Exception e){
            e.getMessage();
        }
    }

    public void join_telegram(View view) {

        TextView telegram = findViewById(R.id.c_telegram);
        try {
            telegram.setMovementMethod(LinkMovementMethod.getInstance());
        }catch (Exception e){
            e.getMessage();
        }
    }

    public void send_mail(View view) {
       TextView email = findViewById(R.id.c_email);
        try {
            email.setMovementMethod(LinkMovementMethod.getInstance());
        }catch (Exception e){
            e.getMessage();
        }
    }


    public void join_facebook(View view) {

        TextView facebook = findViewById(R.id.c_facebook);
        try {
            facebook.setMovementMethod(LinkMovementMethod.getInstance());
        }catch (Exception e){
            e.getMessage();
        }
    }
}