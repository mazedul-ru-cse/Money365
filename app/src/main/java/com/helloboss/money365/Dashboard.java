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

import com.facebook.ads.AbstractAdListener;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdSettings;
import com.facebook.ads.AudienceNetworkAds;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;
import com.google.android.material.navigation.NavigationView;
import com.helloboss.money365.adsLoader.FBAdsLoader;
import com.helloboss.money365.requesthandler.RequestHandler;
import com.helloboss.money365.task.Task;
import com.helloboss.noticeboard.NoticeBoard;

import org.json.JSONObject;

import java.util.HashMap;

public class Dashboard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    InterstitialAd interstitialAd;

    TextView tvHeaderUsername,tvHeaderUserID, tvDashUserName, tvDashTk, tvDashCoin;
    ProgressDialogM progressDialogM;

    public static String pEmail, pName, pTaka, pCoin, pReferCount, pReferCode;
    public static int pCoinUnit = 0;

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
                startActivity(new Intent(Dashboard.this , Dashboard.class));
                finish();
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

        if(new CountryWaring(this).isUSorCA()){
            startActivity(new Intent(Dashboard.this, Task.class));
        }

        if(new BreakTimer(this).isBreakTime(new StoreUserID(this).getBreakTime())) {
            finish();
        }
    }

    public void profile(View view) {

        startActivity(new Intent(Dashboard.this, Profile.class));
    }

    public void withdraw(View view) {
        startActivity(new Intent(Dashboard.this, Withdraw.class));
    }

    public void notice(View view) {

        startActivity(new Intent(Dashboard.this , NoticeBoard.class));
    }


    class DashboardTask extends AsyncTask<String ,Void , String> {

        final String DASHBOARD_URL = "https://helloboss365.com/money365/dashboard.php";

        @Override
        protected String doInBackground(String... strings) {

            try {

                RequestHandler requestHandler = new RequestHandler();

                //creating request parameters
                HashMap<String, String> params = new HashMap<>();
                params.put("phone", UserLogin.userID+"");

                //returning the response
                return requestHandler.sendPostRequest(DASHBOARD_URL, params);

            }catch (Exception e){

                progressDialogM.hideDialog();
                Log.i("Profile exception",e.getMessage());
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            progressDialogM.hideDialog();

            try{
                //Converting response to JSON Object
                JSONObject obj = new JSONObject(s);

                //if no error in response
                if (!obj.getBoolean("error")){

                    pCoinUnit = obj.getInt("coin_unit");
                    pName = obj.getString("name");
                    pEmail = obj.getString("email");
                    int coin = obj.getInt("coin");
                    pCoin = obj.getString("coin");
                    pReferCode = obj.getString("refer_code");
                    pReferCount = obj.getString("refer_count");

                    pTaka = (coin/pCoinUnit)+"";

                   // Log.i("Json data",s);

                    //Set retrieved text to TextViews
                    tvHeaderUsername.setText(obj.getString("name"));
                    tvDashUserName.setText(obj.getString("name"));
                    tvDashTk.setText(pTaka);
                    tvDashCoin.setText(obj.getString("coin"));
                }
            }catch (Exception e ){

            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialogM.showDialog("Please wait");
        }
    }
}