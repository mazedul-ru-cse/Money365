package com.helloboss.money365.resetpasword;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.helloboss.money365.R;

public class Verification extends AppCompatActivity {

    EditText otpEdit;
    TextView otpNotice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);

        getSupportActionBar().hide();

        otpNotice = findViewById(R.id.otp_notice);
        otpEdit = findViewById(R.id.otp_edit);

        otpNotice.setText(otpNotice.getText()+"\n"+ForgotPassword.otpEmail);

    }

    public void verify(View view) {

        String userOtp = otpEdit.getText().toString();

        if(userOtp.equals(ForgotPassword.otpCode)){
           startActivity(new Intent(Verification.this, NewPassword.class));
           finish();
        }else{
            otpEdit.setError("Invalid OTP");
        }
    }

    public void otp_resent(View view) {
    }
}