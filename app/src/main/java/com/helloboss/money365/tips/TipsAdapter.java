package com.helloboss.money365.tips;

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

import java.util.ArrayList;

public class TipsAdapter extends RecyclerView.Adapter<TipsAdapter.MyViewHolder> {
    Context context;
    ArrayList<TipsModel> tipsModelArrayList;

    public TipsAdapter(Context context, ArrayList<TipsModel> tipsModelArrayList) {
        this.context = context;
        this.tipsModelArrayList = tipsModelArrayList;
    }

    @NonNull
    @Override
    public TipsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.tips, parent, false);

        return new TipsAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TipsAdapter.MyViewHolder holder, int position) {

        TipsModel tipsModel = tipsModelArrayList.get(position);
        holder.tIcon.setBackgroundResource(tipsModel.getIconId());
        holder.tTitle.setText(tipsModel.getTitle());

        holder.click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Uri uri = Uri.parse(tipsModel.getLink());
                    context.startActivity(new Intent(Intent.ACTION_VIEW, uri));
                }catch (Exception e){

                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return tipsModelArrayList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView tTitle;
        ImageView tIcon;
        CardView click;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tIcon = itemView.findViewById(R.id.t_icon);
            tTitle = itemView.findViewById(R.id.t_title);
            click = itemView.findViewById(R.id.tips_click);

        }
    }
}
