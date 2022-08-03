package com.helloboss.money365;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class Withdraw extends AppCompatActivity {

    TextView wPoint, wTaka;

    PaymentDialog paymentDialog;
    Dialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw);
        getSupportActionBar().hide();

        wPoint = findViewById(R.id.w_point_balance);
        wTaka = findViewById(R.id.w_tk_balance);
        wPoint.setText(Dashboard.pCoin);
        wTaka.setText(Dashboard.pTaka);

        paymentDialog = new PaymentDialog(this);
        dialog = new Dialog(this);


    }

    public void recharge(View view) {

        new RechargeDialog(this).showRechargeDialog(100);
    }

    public void bkash(View view) {
        paymentDialog.showPaymentDialog(300,"bKash");
    }

    public void rocket(View view) {
        paymentDialog.showPaymentDialog(300,"Rocket");
    }

    public void nogot(View view) {
        paymentDialog.showPaymentDialog(300,"Nogot");
    }

    public void paypal(View view) {
        dialog.setTitle("Coming soon");
        dialog.setCancelable(true);
        dialog.create();
        dialog.show();
    }

    public void card(View view) {
        dialog.setTitle("Coming soon");
        dialog.setCancelable(true);
        dialog.create();
        dialog.show();
    }
}