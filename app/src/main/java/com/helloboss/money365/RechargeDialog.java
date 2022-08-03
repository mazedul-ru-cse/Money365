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
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.helloboss.money365.requesthandler.RequestHandler;

import org.json.JSONObject;

import java.util.HashMap;

public class RechargeDialog {

    Dialog dialog;
    Context context;
    int checkBoxAmount = 0;
    int rechargeAmount = 0;
    int checkMoney = -1;
    String rechargeNumber;
    ProgressDialogM progressDialogM;

    public RechargeDialog(Context context) {
        this.context = context;
        progressDialogM = new ProgressDialogM(context);
    }

    public void showRechargeDialog(int minimumRegAmount){
        CheckBox box1, box2, box3;
        TextView rNumber, rCancel, rSubmit;

        try {
            dialog = new Dialog(context);
            dialog.setContentView(R.layout.recharge_dialog);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            rNumber = dialog.findViewById(R.id.recharge_number);
            rCancel = dialog.findViewById(R.id.recharge_cancel);
            rSubmit = dialog.findViewById(R.id.recharge_submit);

            box1 = dialog.findViewById(R.id.mim_recharge1);
            box2 = dialog.findViewById(R.id.mim_recharge2);
            box3 = dialog.findViewById(R.id.mim_recharge3);

            //Set minimum recharge amount
            box1.setText(minimumRegAmount+"");
            box2.setText(minimumRegAmount*2+"");
            box3.setText(minimumRegAmount*3+"");
            dialog.setCancelable(false);
            dialog.create();
            dialog.show();


            box1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    box2.setChecked(false);
                    box3.setChecked(false);
                    checkBoxAmount = minimumRegAmount;
                }
            });

            box2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    box1.setChecked(false);
                    box3.setChecked(false);
                    checkBoxAmount = minimumRegAmount*2;
                }
            });
            box3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    box1.setChecked(false);
                    box2.setChecked(false);
                    checkBoxAmount = minimumRegAmount*3;
                }
            });

            rCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });

            rSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(!box1.isChecked() &&  !box2.isChecked() &&  !box3.isChecked()){

                        // show warning
                        new WarningBox(context).showWaringBox("Please select recharge amount.",R.drawable.warning);
                        return;
                    }

                    if(rNumber.getText().toString().isEmpty()){

                        // show warning
                        new WarningBox(context).showWaringBox("There is no recharge number..",R.drawable.warning);
                        return;
                    }

                    checkMoney =  ((int)(Integer.parseInt(Dashboard.pCoin) / Dashboard.pCoinUnit) - checkBoxAmount);

                    if(checkMoney >= 0){

                        //set recharge number
                        rechargeNumber = rNumber.getText().toString();
                        // Withdraw money amount
                        rechargeAmount = checkBoxAmount;
                        // Subtract withdraw point from total point
                        checkMoney = ((int)(Integer.parseInt(Dashboard.pCoin) / Dashboard.pCoinUnit) - checkBoxAmount);

                        // Set remains coins
                        Dashboard.pCoin = checkMoney*Dashboard.pCoinUnit+"";

                        dialog.dismiss();

                        //Update coins in database
                       new TaskWithdraw().execute();

                    }else{
                        if(dialog.isShowing())
                            dialog.dismiss();

                        // show warning
                        new WarningBox(context).showWaringBox("You don't have enough coins",R.drawable.warning);
                    }
                }
            });

        }catch (Exception e){
            dialog.dismiss();
        }
    }

    class TaskWithdraw extends AsyncTask<String,Void , String> {

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
                        new RechargeTask().execute();
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


    class RechargeTask extends AsyncTask<String,Void , String> {

        final String UPDATE_COIN_URL = "https://helloboss365.com/money365/recharge.php";
        @Override
        protected String doInBackground(String... task) {

            try {

                RequestHandler requestHandler = new RequestHandler();

                //creating request parameters
                HashMap<String, String> params = new HashMap<>();
                params.put("phone", UserLogin.userID);
                params.put("reg_number", rechargeNumber);
                params.put("reg_amount", rechargeAmount+"");

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
