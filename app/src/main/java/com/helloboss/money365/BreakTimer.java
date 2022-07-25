package com.helloboss.money365;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.helloboss.money365.task.Task;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class BreakTimer {

    Context context;
    String date1;
    String date2;
    String timerDuration;
    int hh1;
    int mm1;
    int ss1;
    int ss2;
    int mm2;
    int hh2;
    SimpleDateFormat simpleDateFormat;

    Dialog dialog;
    long diffTime;

    public BreakTimer(Context context) {
        this.context = context;
    }

    public boolean isBreakTime(String oldTime){

        simpleDateFormat = new SimpleDateFormat("dd/MM/yy HH:mm:ss");

        if(oldTime.equals("null")){
            return false;
        }

        try {
            //Old time
            date1 = oldTime.substring(0, oldTime.indexOf(" "));


            //Current time
            Date date = new Date();
            String newTime = simpleDateFormat.format(date);

            date2 = oldTime.substring(0, newTime.indexOf(" "));

            Date t1 = simpleDateFormat.parse(oldTime);
            Date t2 = simpleDateFormat.parse(newTime);


            diffTime = (t2.getTime() - t1.getTime());

            Log.i("Time difference time",diffTime+"");

            long diffSecond = TimeUnit.MILLISECONDS.toSeconds(diffTime);
            Log.i("Time difference in second",diffSecond+"");

            if (!date1.equals(date2)) {
                return false;
            }
           else if(diffSecond >= 300) {
               return false;

            }
           else{
               timerDuration = (300 - diffSecond)+"";
              //  Toast.makeText(context, timerDuration, Toast.LENGTH_SHORT).show();
               return  true;
            }
        }catch (Exception e){
            e.getMessage();
        }
        return false;
    }

    public void showCounterDownTimer() {

        try {
            dialog = new Dialog(context);
            dialog.setContentView(R.layout.count_down_timer);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setCancelable(true);

            TextView tvTimer = dialog.findViewById(R.id.timer_count);

            CountDownTimer countDownTimer =  new CountDownTimer(Long.parseLong(timerDuration)*1000 ,1000){

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
                        context.startActivity(new Intent(context, Dashboard.class));
                        ((Activity) context).finish();
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

}
