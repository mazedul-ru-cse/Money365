package com.helloboss.money365.leaderboard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.helloboss.money365.R;

import java.util.ArrayList;

public class LeaderBoardAdapter extends RecyclerView.Adapter<LeaderBoardAdapter.MyViewHolder> {
    Context context;
    ArrayList<LeaderBoardModel> leaderBoardModelArrayList;

    public LeaderBoardAdapter(Context context, ArrayList<LeaderBoardModel> leaderBoardModelArrayList) {
        this.context = context;
        this.leaderBoardModelArrayList = leaderBoardModelArrayList;
    }

    @NonNull
    @Override
    public LeaderBoardAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){

        View v = LayoutInflater.from(context).inflate(R.layout.leader_board, parent, false);

        return new LeaderBoardAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull LeaderBoardAdapter.MyViewHolder holder, int position) {

        LeaderBoardModel leaderBoardModel = leaderBoardModelArrayList.get(position);

        holder.rank.setText(leaderBoardModel.getRank());
        holder.name.setText(leaderBoardModel.getName());
        holder.amount.setText(leaderBoardModel.getAmount());
    }

    @Override
    public int getItemCount() {
        return leaderBoardModelArrayList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView rank, name, amount;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            rank = itemView.findViewById(R.id.rank_no);
            name = itemView.findViewById(R.id.rank_name);
            amount = itemView.findViewById(R.id.rank_amount);

        }
    }
}