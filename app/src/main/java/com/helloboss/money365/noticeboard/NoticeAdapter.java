package com.helloboss.money365.noticeboard;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.helloboss.money365.R;

import java.util.ArrayList;

public class NoticeAdapter extends RecyclerView.Adapter<NoticeAdapter.MyViewHolder> {
    Context context;
    ArrayList<NoticeModel> noticeModelArrayList;

    public NoticeAdapter(Context context, ArrayList<NoticeModel> noticeModelArrayList) {
        this.context = context;
        this.noticeModelArrayList = noticeModelArrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.notice, parent, false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        NoticeModel noticeModel = noticeModelArrayList.get(position);

        holder.title.setText(noticeModel.getTitle());
        holder.date.setText(noticeModel.getDate());
        holder.body.setText(noticeModel.getBody());

        holder.noticeClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder showNotice = new AlertDialog.Builder(context);
                showNotice.setTitle(noticeModel.getTitle());
                showNotice.setMessage(noticeModel.getBody());
                showNotice.setCancelable(false);

                showNotice.setPositiveButton("ok",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                               dialogInterface.dismiss();
                            }
                        });

                showNotice.create();
                showNotice.show();
            }
        });


    }

    @Override
    public int getItemCount() {
        return noticeModelArrayList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView title, date, body;
        CardView noticeClick;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.notice_title);
            date = itemView.findViewById(R.id.notice_date);
            body = itemView.findViewById(R.id.notice_body);
            noticeClick = itemView.findViewById(R.id.notice_click);

        }
    }
}