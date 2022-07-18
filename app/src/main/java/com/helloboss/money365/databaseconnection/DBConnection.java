package com.helloboss.money365.databaseconnection;
import android.os.AsyncTask;
import android.util.Log;

import com.helloboss.money365.adapter.NewAccountAdapter;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection extends AsyncTask<String ,Void , String> {

    private static final String URL ="jdbc:mysql://sql6.freemysqlhosting.net:3306/sql6507108";
    private static final String USER = "sql6507108";
    private static final String PASSWORD = "pkRblaKCf5";
    Connection connection;

    @Override
    protected String doInBackground(String... strings) {

        try{
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(URL,USER,PASSWORD);


            if(connection != null) {

                String action =  strings[0];

                if(action.equals("New Account")) {
                   // Log.i("Connected to", "Account");

                   return new NewAccountAdapter(strings[1], strings[2], strings[3],
                            strings[4], strings[5] , connection).newAccountCreate();

                }

              //connection.close();

            }

            else {
               // Log.i("Database Connection", "Not Connected");
               // return "Not Connected";
            }

        }catch (Exception e){
            //Log.i("Database Connection", "Exception");
           // return e.getMessage().toString();

        }

        return "Error!!";

    }

}