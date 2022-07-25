package com.helloboss.money365;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.widget.TextView;

public class ProgressDialogM {

    Dialog dialog;
    Context context;

    public ProgressDialogM(Context context) {

        this.context = context;
    }

    public void showDialog(String title){

        try {
            dialog = new Dialog(context);
            dialog.setContentView(R.layout.dialog);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            TextView titleTv = dialog.findViewById(R.id.text_view);
            titleTv.setText(title);
            dialog.setCancelable(false);
            dialog.create();
            dialog.show();

        }catch (Exception e){

        }
    }

    public void hideDialog(){

        if(dialog.isShowing())
            dialog.dismiss();
    }
}
