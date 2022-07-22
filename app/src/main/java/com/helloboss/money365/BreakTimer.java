package com.helloboss.money365;

import android.content.Context;
import android.widget.Toast;

public class BreakTimer {

    Context context;
    String date;
    String timerDuration;

    public BreakTimer(Context context) {
        this.context = context;
    }

    public boolean isBreakTime(String oldTime){

        date = oldTime.substring(0,oldTime.indexOf(" "));

        Toast.makeText(context, date, Toast.LENGTH_SHORT).show();

        return false;
    }

    public String getTimerDuration() {

        return timerDuration;
    }

    public void showCounterDownTimer(String timerDuration) {

    }
}
