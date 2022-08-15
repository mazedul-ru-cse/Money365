package com.helloboss.money365.userguide;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.helloboss.money365.R;
import com.helloboss.money365.noticeboard.NoticeModel;

import java.util.ArrayList;

public class UserGuideAdapter extends RecyclerView.Adapter<UserGuideAdapter.MyViewHolder> {
    Context context;
    ArrayList<UserGuideModel> userGuideModelArrayList;

    public UserGuideAdapter(Context context, ArrayList<UserGuideModel> userGuideModelArrayList) {
        this.context = context;
        this.userGuideModelArrayList = userGuideModelArrayList;
    }

    @NonNull
    @Override
    public UserGuideAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.user_guide, parent, false);

        return new UserGuideAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull UserGuideAdapter.MyViewHolder holder, int position) {

        UserGuideModel userGuideModel = userGuideModelArrayList.get(position);
        holder.ugIcon.setBackgroundResource(userGuideModel.getIconId());
        holder.ugTitle.setText(userGuideModel.getTitle());

        holder.click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Uri uri = Uri.parse(userGuideModel.getLink());
                    context.startActivity(new Intent(Intent.ACTION_VIEW, uri));
                }catch (Exception e){

                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return userGuideModelArrayList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView ugTitle;
        ImageView ugIcon;
        CardView click;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ugIcon = itemView.findViewById(R.id.ug_icon);
            ugTitle = itemView.findViewById(R.id.ug_title);
            click = itemView.findViewById(R.id.ug_click);

        }
    }
}
