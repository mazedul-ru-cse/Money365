package com.helloboss.money365;

import android.content.Context;
import android.content.SharedPreferences;

public class StoreUserID {

    public static final String MyPre = "login";
    public static final String sPUserID = "id";
    public static final String sPUserPassword = "password";
    public static final String sPLoginStatus = "status";
    public static final String sPBreakTime = "break";

    SharedPreferences sharedPreferences;

    public StoreUserID(Context context) {
      sharedPreferences = context.getSharedPreferences(MyPre, Context.MODE_PRIVATE);
    }

    public void setBreakTime(String breakTime){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(sPBreakTime, breakTime);
        editor.apply();
    }

    public String getBreakTime(){
        return sharedPreferences.getString(sPBreakTime,"");
    }
    public void setUserIDPassword(String userID, String userPassword,Boolean loginStatus) {

        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(sPUserID, userID);
        editor.putString(sPUserPassword, userPassword);
        editor.putBoolean(sPLoginStatus, loginStatus);
        editor.apply();

    }


    public void setTaskCount(String task, int counter){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(task, counter);
        editor.apply();
    }


    public int getTaskCount(String task){

        return sharedPreferences.getInt(task,0);
    }


    public String getUserID() {

        return sharedPreferences.getString(sPUserID,"");
    }

    public String getUserPassword() {
        return sharedPreferences.getString(sPUserPassword,"");
    }

    public Boolean getLoginStatus() {
        return sharedPreferences.getBoolean(sPLoginStatus,false);
    }
}
