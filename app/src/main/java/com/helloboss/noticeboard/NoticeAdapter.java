package com.helloboss.noticeboard;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
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
    }

    @Override
    public int getItemCount() {
        return noticeModelArrayList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView title, date, body;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.notice_title);
            date = itemView.findViewById(R.id.notice_date);
            body = itemView.findViewById(R.id.notice_body);

        }
    }
}