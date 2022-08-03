package com.helloboss.money365;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class WarningBox {

    Context context;
    Dialog dialog;

    public WarningBox(Context context) {
        this.context = context;
    }

    public void showWaringBox(String title,int drawable ) {

        try {
            dialog = new Dialog(context);
            dialog.setContentView(R.layout.waring_xml);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            TextView titleTv = dialog.findViewById(R.id.waring_text);
            ImageView cross = dialog.findViewById(R.id.waring_cross);
            ImageView imageView = dialog.findViewById(R.id.waring_image);
            imageView.setBackgroundResource(drawable);

            titleTv.setText(title);
            dialog.setCancelable(false);
            dialog.create();
            dialog.show();

            cross.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                }
            });

        } catch (Exception e) {

        }
    }


    public void hideBox(){
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
    }
}
