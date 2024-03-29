package com.helloboss.money365;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import android.os.CountDownTimer;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdSettings;
import com.facebook.ads.AudienceNetworkAds;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.Task;
import com.helloboss.money365.leaderboard.LeaderBoard;
import com.helloboss.money365.requesthandler.RequestHandler;
import com.helloboss.money365.noticeboard.NoticeBoard;
import com.helloboss.money365.spin.SpinnerWheel;
import com.helloboss.money365.tips.Tips;
import com.helloboss.money365.userguide.UserGuide;
import com.helloboss.money365.workshow.InterstitialAdsShow;
import com.startapp.sdk.adsbase.StartAppAd;
import com.startapp.sdk.adsbase.StartAppInitProvider;
import com.startapp.sdk.adsbase.StartAppSDK;
import com.startapp.sdk.adsbase.StartAppSDKInternal;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;

public class Dashboard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    Dialog dialog;
    TextView tvHeaderUsername,tvHeaderUserID, tvDashUserName, tvDashTk, tvDashCoin;
    ProgressDialogM progressDialogM;


    public static String pEmail, pName, pTaka, pCoin, pReferCount, pReferCode;
    public static int pCoinUnit = 0,minRecharge,minWithdraw,rPoint,bPoint,sPoint,sRange,sAdsClick;
    private BreakTimer breakTimer;
    private StoreUserID storeUserID;
    public static boolean taskStatus = true;
    public static long breakTime;

    AppUpdateManager appUpdateManager;
    InstallStateUpdatedListener installStateUpdatedListener;
    private int REQUEST_CODE = 123;


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
        breakTimer = new BreakTimer();
        storeUserID = new StoreUserID(this);

        AudienceNetworkAds.initialize(Dashboard.this);
        StartAppSDK.setTestAdsEnabled(false);
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

        StartAppAd.showAd(Dashboard.this);

        new DashboardTask().execute();

//        appUpdateManager = AppUpdateManagerFactory.create(getApplicationContext());
//        installStateUpdatedListener = state -> {
//            if(state.installStatus() == InstallStatus.DOWNLOADED){
//                //popupSnackBarCompleteUpdate();
//            }else if(state.installStatus() == InstallStatus.INSTALLED){
//               // removeInstallStateUpdatedListener();
//            }else{
//                Toast.makeText(this, "InstallStateUpdateListener: state: "+state.installStatus(), Toast.LENGTH_SHORT).show();
//            }
//        };
//        appUpdateManager.registerListener(installStateUpdatedListener);
//        //checkUpdate();
    }


//    private void checkUpdate() {
//
//        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();
//
//        appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
//
//            if(appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
//                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)){
//                startUpdateFlow(appUpdateInfo);
//            }else if(appUpdateInfo.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS){
//                startUpdateFlow(appUpdateInfo);
//            }
//
//        });
//
//    }

//    private void removeInstallStateUpdatedListener() {
//        if(appUpdateManager != null){
//            appUpdateManager.unregisterListener(installStateUpdatedListener);
//        }
//    }
//
//
//    private void popupSnackBarCompleteUpdate() {
//
//        Snackbar.make(findViewById(android.R.id.content).getRootView(), "New app is ready!",
//                        Snackbar.LENGTH_INDEFINITE)
//                .setAction("Install",view -> {
//                    if(appUpdateManager != null){
//                        appUpdateManager.completeUpdate();
//                    }
//                })
//                .setActionTextColor(getResources().getColor(R.color.purple_500))
//                .show();
//
//    }
//
//    private void startUpdateFlow(AppUpdateInfo appUpdateInfo) {
//        try{
//            appUpdateManager.startUpdateFlowForResult(appUpdateInfo,AppUpdateType.IMMEDIATE,this,
//                    REQUEST_CODE);
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//    }
//
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        Toast.makeText(this, "Update start"+resultCode, Toast.LENGTH_SHORT).show();
//
//        if (requestCode == REQUEST_CODE){
//
//            if(resultCode == RESULT_OK){
//                Log.i("Update","Update success! result code"+resultCode);
//                Toast.makeText(this, "Update success! result code"+resultCode, Toast.LENGTH_SHORT).show();
//            }else if(resultCode == RESULT_CANCELED){
//                Log.i("Update","Update canceled by user! result code"+resultCode);
//                Toast.makeText(this, "Update canceled by user! result code"+resultCode, Toast.LENGTH_SHORT).show();
//            }else {
//                Toast.makeText(this, "Update fail! result code"+resultCode, Toast.LENGTH_SHORT).show();
//                checkUpdate();
//            }
//        }
//
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        removeInstallStateUpdatedListener();
//    }


    @Override
    public void onBackPressed() {

        AlertDialog.Builder showNotice = new AlertDialog.Builder(this);
        showNotice.setTitle("Exit");
        showNotice.setMessage("Do you want to exit..?");
        showNotice.setCancelable(false);

        showNotice.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        exitApp();
                    }
                });
        showNotice.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

        showNotice.create();
        showNotice.show();


    }

    private void exitApp() {
        super.onBackPressed();
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

            case R.id.h_share:
                try{
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT,"Money365");
                    String mess = "\nMake money with Money365 and enjoy your own life. Download the app now.\n";
                    mess = mess + "https://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName().toString() + "\n\n";
                    shareIntent.putExtra(Intent.EXTRA_TEXT, mess);
                    startActivity(Intent.createChooser(shareIntent,"Share by"));

                }catch (Exception e){

                }
                break;

            case R.id.h_ratting:
                try{
                    Uri uri = Uri.parse("https://play.google.com/store/apps/details?id="+getApplicationContext().getPackageName().toString());
                    Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                    startActivity(intent);
                }catch (Exception e){

                }
                break;

            case R.id.h_user_guide:
                startActivity(new Intent(Dashboard.this, UserGuide.class));
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

        //Check break time or not

        String checkBreakTime = breakTimer.isBreakTime(storeUserID.getBreakTime());
        if(checkBreakTime != null){
            // breakTimer.showCounterDownTimer();

            try {
                dialog = new Dialog(this);
                dialog.setContentView(R.layout.count_down_timer);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setCancelable(true);

                TextView tvTimer = dialog.findViewById(R.id.timer_count);
                CountDownTimer countDownTimer =  new CountDownTimer(Long.parseLong(checkBreakTime)*1000 ,1000){
                    @Override
                    public void onTick(long l) {

                        NumberFormat f = new DecimalFormat("00");
                        long min = (l/60000)%60;
                        long sec = (l/1000)%60;

                        tvTimer.setText(f.format(min) +":"+f.format(sec));

                    }

                    @Override
                    public void onFinish() {

                        tvTimer.setText("00:00");
                        dialog.dismiss();

                    }
                };

                dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {

                        try {
                            dialog.dismiss();

                        }catch (Exception e){
                            e.getMessage();
                        }
                    }
                });

                dialog.create();
                dialog.show();
                countDownTimer.start();

            }catch (Exception e){

                e.getMessage();
            }
        }
        else
         isUSorCA();

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
                    params.put("phone", UserLogin.userID);
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
                            startActivity(new Intent(Dashboard.this , com.helloboss.money365.task.TaskList.class));
                            finish();

                        }else{

                            Dialog dialog = new Dialog(Dashboard.this);
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

    public void profile(View view) {
        startActivity(new Intent(Dashboard.this, Profile.class));

        // and show interstitial ad
        StartAppAd.showAd(this);

    }

    public void withdraw(View view) {

        startActivity(new Intent(Dashboard.this, Withdraw.class));
        // and show interstitial ad
        StartAppAd.showAd(this);
    }

    public void notice(View view) {

        startActivity(new Intent(Dashboard.this, NoticeBoard.class));
        // and show interstitial ad
        StartAppAd.showAd(this);
    }

    public void leader_board(View view) {

        startActivity(new Intent(Dashboard.this, LeaderBoard.class));
        // and show interstitial ad
        StartAppAd.showAd(this);
    }

    public void user_guide(View view) {
        startActivity(new Intent(Dashboard.this, UserGuide.class));
        // and show interstitial ad
        StartAppAd.showAd(this);
    }

    public void premium(View view) {
        Toast.makeText(this, "Coming soon", Toast.LENGTH_SHORT).show();

    }

    public void contact(View view) {

        startActivity(new Intent(Dashboard.this, Contact.class));
        // and show interstitial ad
        StartAppAd.showAd(this);

    }

    public void tips(View view) {

        startActivity(new Intent(Dashboard.this, Tips.class));
        // and show interstitial ad
        StartAppAd.showAd(this);
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
                    String appUpdateCheck = obj.getString("app_update");
                    pTaka = (coin/pCoinUnit)+"";

                    minRecharge = obj.getInt("min_r");
                    minWithdraw = obj.getInt("min_w");

                    rPoint = obj.getInt("r_point");
                    bPoint = obj.getInt("b_point");
                    sPoint = obj.getInt("s_point");
                    sRange = obj.getInt("s_range");
                    sAdsClick = obj.getInt("s_ads_click");

                    breakTime = obj.getLong("break_time");
                    taskStatus = obj.getBoolean("task_status");


                   // Log.i("Json data",s);

                    //Set retrieved text to TextViews
                    tvHeaderUsername.setText(obj.getString("name"));
                    tvDashUserName.setText(obj.getString("name"));
                    tvDashTk.setText(pTaka);
                    tvDashCoin.setText(obj.getString("coin"));

                    if(!appUpdateCheck.equals("1.1.3") && obj.getBoolean("check_update")){

                        Dialog update = new Dialog(Dashboard.this);
                        update.setContentView(R.layout.app_update_dialog);
                        update.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                        TextView btnUpdate = update.findViewById(R.id.update_now);

                        update.setCancelable(false);
                        update.create();
                        update.show();


                        btnUpdate.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                try {
                                    Uri uri = Uri.parse(obj.getString("link"));
                                    startActivity(new Intent(Intent.ACTION_VIEW, uri));
                                   // update.dismiss();
                                }catch (Exception e){

                                }
                            }
                        });
                    }
                }
            }catch (Exception e   ){
                e.printStackTrace();
                Toast.makeText(Dashboard.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialogM.showDialog("Please wait");
        }
    }
}