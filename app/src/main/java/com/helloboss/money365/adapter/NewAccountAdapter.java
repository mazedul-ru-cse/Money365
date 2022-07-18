package com.helloboss.money365.adapter;

import android.util.Log;

import java.sql.Connection;
import java.sql.Statement;

public class NewAccountAdapter {

    String name, email, password, phone, refer;
    Connection connection;

    public NewAccountAdapter(String name, String email, String password, String phone, String refer , Connection connection) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.refer = refer;
        this.connection = connection;
    }

    public String newAccountCreate(){
        String query = "";

        try {
            query = "insert into user_list (name, email, password, phone, refer) values ("
                    + "'"+name + "','" + email + "','" + password + "','" + phone + "','" + refer + "')";

            Statement statement = connection.createStatement();
            statement.executeUpdate(query);

            //Log.i("New account create", "Account created");
            connection.close();

        }catch (Exception e){
            //Log.i("New account create failure", e.getMessage().toString());
            return "failure";
        }

        return "created";
    }

}
