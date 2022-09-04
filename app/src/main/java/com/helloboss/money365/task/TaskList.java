package com.helloboss.money365.task;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.helloboss.money365.Dashboard;
import com.helloboss.money365.R;
import com.helloboss.money365.spin.SpinnerWheel;
import com.startapp.sdk.adsbase.StartAppAd;

public class TaskList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);
        getSupportActionBar().hide();

        StartAppAd.showAd(this);
    }

    public void spin_task(View view) {


        if (!Dashboard.taskStatus) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);

            dialog.setCancelable(false);
            dialog.setTitle("কাজের বিরতি");
            dialog.setMessage("সাময়িক সময়ের জন্য অ্যাপসের কাজ ইস্তগিত করা হয়েছে। কিছুক্ষন পর আবার চেষ্টা করুন। ধন্যবাদ");
            dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });

            dialog.create();
            dialog.show();
        } else {
            startActivity(new Intent(TaskList.this, SpinnerWheel.class));
            finish();
        }
    }

    public void video_task(View view) {

        if (!Dashboard.taskStatus) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);

            dialog.setCancelable(false);
            dialog.setTitle("কাজের বিরতি");
            dialog.setMessage("সাময়িক সময়ের জন্য অ্যাপসের কাজ ইস্তগিত করা হয়েছে। কিছুক্ষন পর আবার চেষ্টা করুন। ধন্যবাদ");
            dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });

            dialog.create();
            dialog.show();
        } else {
            startActivity(new Intent(TaskList.this, Task.class));
            finish();
        }
    }

    public void youtube_task(View view) {
        Toast.makeText(this, "Coming soon", Toast.LENGTH_SHORT).show();
        StartAppAd.showAd(this);
    }

    public void web_task(View view) {
        Toast.makeText(this, "Coming soon", Toast.LENGTH_SHORT).show();
        StartAppAd.showAd(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        startActivity(new Intent(TaskList.this, Dashboard.class));
        finish();

    }
}