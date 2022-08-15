package com.helloboss.money365;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.helloboss.money365.requesthandler.RequestHandler;
import com.helloboss.money365.task.Task;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class CountryWaring {

    Context context;
    Dialog dialog;
    ProgressDialogM progressDialogM;

    public CountryWaring(Context context) {
        this.context = context;
        dialog = new Dialog(context);
        progressDialogM = new ProgressDialogM(context);
    }
    public void isUSorCA(){

        final String country = "https://www.helloboss365.com/money365/country.php";

        class CountryName extends AsyncTask<String, Void, String>{

            @Override
            protected String doInBackground(String... strings) {

                try{

                    RequestHandler requestHandler = new RequestHandler();
                    //creating request parameters
                    HashMap<String, String> params = new HashMap<>();
                    params.put("phone", "012");
                    //returning the response
                    return requestHandler.sendPostRequest(country, params);

                }catch (Exception e){
                    progressDialogM.hideDialog();
                    e.getMessage();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                progressDialogM.hideDialog();
                //Converting response to JSON Object
                JSONObject obj = null;
                try {
                    obj = new JSONObject(s);

                    //if no error in response
                    if (!obj.getBoolean("error")) {

                        //  Toast.makeText(context, obj.getString("country") + "\n" + obj.getString("region"), Toast.LENGTH_SHORT).show();
                        if (obj.getString("country").equals("US") || obj.getString("country").equals("CA")) {
                       //  Log.i("Country",obj.getString("country") );
                            context.startActivity(new Intent((Dashboard)context, Task.class));

                        }else{

                            dialog.setContentView(R.layout.country_warning);
                            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                            TextView textView = dialog.findViewById(R.id.vpn_connect);
                            ImageView cross = dialog.findViewById(R.id.country_waring_cross);

                            dialog.setCancelable(false);
                            dialog.create();
                            dialog.show();

                            textView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialog.dismiss();
                                }
                            });


                            cross.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialog.dismiss();
                                }
                            });
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialogM.showDialog("Please wait..");
            }
        }

       new CountryName().execute();

    }
}
