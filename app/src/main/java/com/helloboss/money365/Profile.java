package com.helloboss.money365;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class Profile extends AppCompatActivity {

    TextView tvName, tvEmail , tvTk, tvPointBalance, tvTotalRefer, tvReferCode;

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

        tvName.setText(Dashboard.pName);
        tvEmail.setText(Dashboard.pEmail);
        tvTk.setText("TK : "+Dashboard.pTaka);
        tvPointBalance.setText(Dashboard.pCoin);
        tvTotalRefer.setText(Dashboard.pReferCount);
        tvReferCode.setText(Dashboard.pReferCode);
    }

}