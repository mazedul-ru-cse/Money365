package com.helloboss.money365;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.helloboss.money365.requesthandler.RequestHandler;

import org.json.JSONObject;

import java.util.HashMap;

public class PaymentDialog  {

    Dialog dialog;
    Context context;
    int checkBoxAmount = 0;
    int paymentAmount = 0;
    int checkMoney = -1;
    String paymentNumber;
    ProgressDialogM progressDialogM;
    String paymentMethod;

    public PaymentDialog(Context context) {
        this.context = context;
        progressDialogM = new ProgressDialogM(context);
    }

    public void showPaymentDialog(int minimumPayAmount, String paymentMethod){

        this.paymentMethod = paymentMethod;

        CheckBox box1, box2, box3;
        TextView pNumber, pCancel, pSubmit,pMethod;

        try {
            dialog = new Dialog(context);
            dialog.setContentView(R.layout.payment_xml);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            pNumber = dialog.findViewById(R.id.payment_number);
            pCancel = dialog.findViewById(R.id.payment_cancel);
            pSubmit = dialog.findViewById(R.id.payment_submit);
            pMethod = dialog.findViewById(R.id.payment_method);

            // Show payment method
            pMethod.setText(paymentMethod);

            box1 = dialog.findViewById(R.id.mim_payment1);
            box2 = dialog.findViewById(R.id.mim_payment2);
            box3 = dialog.findViewById(R.id.mim_payment3);

            //Set minimum recharge amount
            box1.setText(minimumPayAmount+"");
            box2.setText(minimumPayAmount*2+"");
            box3.setText(minimumPayAmount*3+"");
            dialog.setCancelable(false);
            dialog.create();
            dialog.show();


            box1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    box2.setChecked(false);
                    box3.setChecked(false);
                    checkBoxAmount = minimumPayAmount;
                }
            });

            box2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    box1.setChecked(false);
                    box3.setChecked(false);
                    checkBoxAmount = minimumPayAmount*2;
                }
            });
            box3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    box1.setChecked(false);
                    box2.setChecked(false);
                    checkBoxAmount = minimumPayAmount*3;
                }
            });

            pCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(dialog.isShowing())
                        dialog.dismiss();
                }
            });

            pSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(!box1.isChecked() &&  !box2.isChecked() &&  !box3.isChecked()){

                        // show warning
                        new WarningBox(context).showWaringBox("Please select payment amount.",R.drawable.warning);
                        return;
                    }

                    if(pNumber.getText().toString().isEmpty()){

                        // show warning
                        new WarningBox(context).showWaringBox("There is no payment number..",R.drawable.warning);
                        return;
                    }

                    checkMoney =  ((int)(Integer.parseInt(Dashboard.pCoin) / Dashboard.pCoinUnit) - checkBoxAmount);

                    if(checkMoney >= 0){

                        //set payment number
                        paymentNumber = pNumber.getText().toString();
                        // Withdraw money amount
                        paymentAmount = checkBoxAmount;
                        // Subtract withdraw point from total point
                        checkMoney = ((int)(Integer.parseInt(Dashboard.pCoin) / Dashboard.pCoinUnit) - checkBoxAmount);

                        // Set remains coins
                        Dashboard.pCoin = checkMoney*Dashboard.pCoinUnit+"";

                        if(dialog.isShowing())
                            dialog.dismiss();

                        //Update coins in database
                        new UpdateCoins().execute();

                    }else{
                        if(dialog.isShowing())
                            dialog.dismiss();

                        // show warning
                        new WarningBox(context).showWaringBox("You don't have enough coins",R.drawable.warning);
                    }
                }
            });

        }catch (Exception e){
            if(dialog.isShowing())
                dialog.dismiss();
        }
    }

    class UpdateCoins extends AsyncTask<String,Void , String> {

        final String UPDATE_COIN_URL = "https://helloboss365.com/money365/update_coin.php";
        @Override
        protected String doInBackground(String... task) {

            try {

                RequestHandler requestHandler = new RequestHandler();

                //creating request parameters
                HashMap<String, String> params = new HashMap<>();
                params.put("coin", Dashboard.pCoin);
                params.put("phone", UserLogin.userID);

                //returning the response
                return requestHandler.sendPostRequest(UPDATE_COIN_URL, params);


            }catch (Exception e) {

                progressDialogM.hideDialog();
                // Log.i("Task exception", e.getMessage());
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
                    //Request for payment
                    new RequestPayment().execute();
                }


            }catch (Exception e){

            }
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialogM.showDialog("Please wait..");
        }
    }


    class RequestPayment extends AsyncTask<String,Void , String> {

        final String UPDATE_COIN_URL = "https://helloboss365.com/money365/withdraw.php";
        @Override
        protected String doInBackground(String... task) {

            try {

                RequestHandler requestHandler = new RequestHandler();

                //creating request parameters
                HashMap<String, String> params = new HashMap<>();
                params.put("phone", UserLogin.userID+"");
                params.put("pay_method", paymentMethod);
                params.put("payment_num", paymentNumber);
                params.put("amount", paymentAmount+"");

                //returning the response
                return requestHandler.sendPostRequest(UPDATE_COIN_URL, params);


            }catch (Exception e) {

                progressDialogM.hideDialog();
                // Log.i("Task exception", e.getMessage());
                return null;

            }
        }

        @Override
        protected void onPostExecute(String s) {

            super.onPostExecute(s);
            progressDialogM.hideDialog();

            // show warning
            WarningBox warningBox = new WarningBox(context);

            warningBox.showWaringBox("Congratulations", R.drawable.congratulations);

            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run(){
                    warningBox.hideBox();

                    ((Activity) context).finish();
                    context.startActivity(new Intent((Withdraw) context, Dashboard.class));
                }
            }, 1500);

        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialogM.showDialog("Please wait..");
        }
    }

}