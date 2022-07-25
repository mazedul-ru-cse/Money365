package com.helloboss.money365;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class UserRegistration extends AppCompatActivity {

    EditText editName, editEmail, editPhone, editPassword1, editPasswordConfirm , editRefer;
    String  name , email,password1,password2, phone, referCode;

    ProgressDialogM progressDialogM;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_registration);

        getSupportActionBar().hide();
        editName = findViewById(R.id.reg_user_name);
        editEmail = findViewById(R.id.reg_email);
        editPassword1 = findViewById(R.id.reg_password);
        editPasswordConfirm = findViewById(R.id.reg_confirm_password);
        editPhone = findViewById(R.id.reg_phone);
        editRefer = findViewById(R.id.reg_refer_code);

        progressDialogM = new ProgressDialogM(this);

    }

    public void user_register(View view) {

        name = editName.getText().toString();
        email = editEmail.getText().toString();
        phone = editPhone.getText().toString();
        password1 = editPassword1.getText().toString();
        password2 = editPasswordConfirm.getText().toString();
        referCode = editRefer.getText().toString();

        if(name.isEmpty()){
            editName.setError("Name is required");
            return;
        }
        if(email.isEmpty()){
            editEmail.setError("Email is required");
            return;
        }
        if(phone.isEmpty()){
            editPhone.setError("Phone number is required");
            return;
        }
        if(password1.isEmpty()){
            editPassword1.setError("Password is required");
            return;
        }
        if(password2.isEmpty()){
            editPasswordConfirm.setError("Password is required");
            return;
        }

        if(!password1.equals(password2)){

            editPasswordConfirm.setError("Not matched...!");
            return;
        }
        if(referCode.isEmpty()){
            referCode = "#";

        }

       new RegistrationTask().execute();

    }

    public void already_registered(View view) {
        startActivity(new Intent(UserRegistration.this, UserLogin.class));
    }


    class RegistrationTask extends AsyncTask<String ,Void , String>{

        private static final String URL ="jdbc:mysql://helloboss365.com:3306/helloboss_money365";
        private static final String USER = "helloboss_money365";
        private static final String PASSWORD = "helloboss_money365";
        Connection connection;

        @Override
        protected String  doInBackground(String... strings) {

            String query = "";
            try{

                Class.forName("com.mysql.jdbc.Driver");
                connection = DriverManager.getConnection(URL,USER,PASSWORD);

                query = "insert into user_list ( phone, name, email, password, refer) values ("
                        + "'"+phone + "','" + name + "','" + email + "','" + password1 + "','" + referCode + "')";

                Statement statement = connection.createStatement();
                statement.executeUpdate(query);

                query = "insert into account_details (phone, coin, taka, refer_count) values ("
                        + "'"+phone + "',0 , 0 , 0)";

                statement = connection.createStatement();
                statement.executeUpdate(query);

                connection.close();

            }catch (Exception e){

                Log.i("Registration Exception",e.getMessage());
                Log.i("Registration Exception",e.getStackTrace().toString());
                return "no";
            }

            return "yes";
        }

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            progressDialogM.showDialog("Please wait");

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            progressDialogM.hideDialog();

            if(s.equals("yes")) {
                Toast.makeText(getApplicationContext(), "Account created successfully", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(UserRegistration.this, UserLogin.class));
                finish();

            }else {
                editPhone.setError("Phone number is already used");
            }
        }
    }
}

