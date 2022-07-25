package com.helloboss.money365;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.helloboss.money365.task.Task;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Dashboard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;

    TextView tvHeaderUsername,tvHeaderUserID, tvDashUserName, tvDashTk, tvDashCoin;
    ProgressDialogM progressDialogM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        drawerLayout = findViewById(R.id.draw_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);
        tvDashUserName = findViewById(R.id.d_user_name);
        tvDashTk = findViewById(R.id.d_tk);
        tvDashCoin = findViewById(R.id.d_coin);

        progressDialogM = new ProgressDialogM(this);

        //toolbar
        setSupportActionBar(toolbar);


        //Navigation menu
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();


        View hview;
        hview = navigationView.getHeaderView(0);
        navigationView.setNavigationItemSelectedListener(this);

        //Header user name and ID
        tvHeaderUserID = hview.findViewById(R.id.h_user_id);
        tvHeaderUsername = hview.findViewById(R.id.h_user_name);
        tvHeaderUserID.setText("ID : "+UserLogin.userID);

        new DashboardTask().execute();
    }

    // Double press the back button to exit
    int backPressedCount = 0;

    @Override
    public void onBackPressed() {


        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }

        else {
            if (backPressedCount >= 1) {
                super.onBackPressed();
                backPressedCount = 0;
                return;
            } else {
                backPressedCount = backPressedCount + 1;
                Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT).show();
            }
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {

            case R.id.nav_home:
                break;

            case R.id.h_logout:

               ProgressDialogM progressDialogM =  new ProgressDialogM(Dashboard.this);
               progressDialogM.showDialog("Logout");

                new StoreUserID(this).setUserIDPassword("","",false);
                progressDialogM.hideDialog();

                startActivity(new Intent(Dashboard.this , UserLogin.class));
                finish();

                break;
        }

        leftSide();

        return true;
    }

    public void leftSide(){
        // rightSide();
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            drawerLayout.openDrawer(GravityCompat.START);
        }
    }

    public void task(View view) {

        startActivity(new Intent(Dashboard.this, Task.class));
        if(new BreakTimer(this).isBreakTime(new StoreUserID(this).getBreakTime()))
            finish();
    }

    public void profile(View view) {

        startActivity(new Intent(Dashboard.this, Profile.class));
    }

    public void withdraw(View view) {
        startActivity(new Intent(Dashboard.this, Withdraw.class));
    }

    public void notice(View view) {

        StoreUserID storeUserID = new StoreUserID(this);

        Toast.makeText(this, storeUserID.getUserID()+" "+storeUserID.getUserPassword(), Toast.LENGTH_SHORT).show();
    }


    class DashboardTask extends AsyncTask<String ,Void , String> {

        private static final String URL ="jdbc:mysql://sql6.freemysqlhosting.net:3306/sql6507108";
        private static final String USER = "sql6507108";
        private static final String PASSWORD = "pkRblaKCf5";
        String name = null, coin = null, tk = null;
        Connection connection;

        @Override
        protected String doInBackground(String... strings) {
            Statement st;
            ResultSet resultSet;

            try {

                Class.forName("com.mysql.jdbc.Driver");
                connection = DriverManager.getConnection(URL,USER,PASSWORD);

                String query = "SELECT a.`name`, b.`coin`, b.`taka` " +
                        "FROM `user_list` a , `account_details` b" +
                        " where a.`phone`="+ UserLogin.userID+ " and b.`phone`="+UserLogin.userID;

                st = connection.createStatement();
                resultSet = st.executeQuery(query);

                if(resultSet.next()) {
                    name = resultSet.getString("name");
                    tk = resultSet.getString("taka");
                    coin = resultSet.getString("coin");
                }

                connection.close();

            }catch (Exception e){

                progressDialogM.hideDialog();
                Log.i("Profile exception",e.getMessage());

            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            tvHeaderUsername.setText(name);
            tvDashUserName.setText(name);
            tvDashTk.setText(tk);
            tvDashCoin.setText(coin);
            progressDialogM.hideDialog();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialogM.showDialog("Please wait");
        }
    }
}