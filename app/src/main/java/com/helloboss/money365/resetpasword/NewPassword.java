package com.helloboss.money365.resetpasword;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.helloboss.money365.ProgressDialogM;
import com.helloboss.money365.R;
import com.helloboss.money365.UserLogin;
import com.helloboss.money365.requesthandler.RequestHandler;

import org.json.JSONObject;

import java.util.HashMap;

public class NewPassword extends AppCompatActivity {

    TextView newPassword, passwordConfirm;
    private ProgressDialogM progressDialogM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_password);

        getSupportActionBar().hide();

        newPassword = findViewById(R.id.new_password);
        passwordConfirm = findViewById(R.id.new_password_confirm);
        progressDialogM = new ProgressDialogM(this);
    }

    public void submit(View view) {

        String password1, password2;
        password1 = newPassword.getText().toString();
        password2 = passwordConfirm.getText().toString();

        if (password1.isEmpty()){
            newPassword.setError("Required");
            return;
        }
        if (password2.isEmpty()){
            passwordConfirm.setError("Required");
            return;
        }

        if(password1.length() < 5 ){
            newPassword.setError("Length at least 6 character.");
            return;
        }

        if(!password1.equals(password2)){
            passwordConfirm.setError("Not Match");
            return;
        }

        changePassword();
    }

    private void changePassword() {

        class ChangePassword extends AsyncTask<String,Void , String> {


            final String CHANGE_PASSWORD_URL = "https://helloboss365.com/money365/change_password.php";
            @Override
            protected String doInBackground(String... task) {

                try {

                    RequestHandler requestHandler = new RequestHandler();
                    //creating request parameters
                    HashMap<String, String> params = new HashMap<>();
                    params.put("phone", UserLogin.userID);
                    params.put("password", newPassword.getText().toString());


                    //returning the response
                    return requestHandler.sendPostRequest(CHANGE_PASSWORD_URL, params);


                }catch (Exception e) {
                    progressDialogM.hideDialog();
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

                        AlertDialog.Builder passwordChangeAlert = new AlertDialog.Builder(NewPassword.this);
                        passwordChangeAlert.setTitle("Password");
                        passwordChangeAlert.setMessage("Your account password is changed successfully.");
                        passwordChangeAlert.setCancelable(false);

                        passwordChangeAlert.setPositiveButton("ok",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                        startActivity(new Intent(NewPassword.this , UserLogin.class));
                                        finish();
                                    }
                                });

                        passwordChangeAlert.create();
                        passwordChangeAlert.show();
                    }

                }catch (Exception e){

                    Toast.makeText(NewPassword.this,e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                }
            }


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialogM.showDialog("Please wait..");
            }
        }
        new ChangePassword().execute();
    }
}