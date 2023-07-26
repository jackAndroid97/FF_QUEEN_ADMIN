package com.ff_queen.admin.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ff_queen.admin.Activities.CircleActivity;
import com.ff_queen.admin.Activities.GameActivity;
import com.ff_queen.admin.Activities.KoyelActivity;
import com.ff_queen.admin.Activities.SpinActivity;
import com.ff_queen.admin.Models.Game_Model;
import com.ff_queen.admin.R;
import com.ff_queen.admin.Utilities.ApiClient;
import com.ff_queen.admin.databinding.SingleGameListBinding;


import java.util.List;

public class GameAdapter extends RecyclerView.Adapter<GameAdapter.MyViewholder> {

    Context context;
    List<Game_Model> game_models;
    String url="http://web.easytodb.com/Play_Win/admin_api/Game_image/";
    public GameAdapter(Context context, List<Game_Model> game_models) {
        this.context = context;
        this.game_models = game_models;
    }

    @NonNull
    @Override
    public MyViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        SingleGameListBinding binding = SingleGameListBinding.inflate(inflater,parent,false);
        return new MyViewholder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewholder holder, @SuppressLint("RecyclerView") int position) {

        holder.binding.gameName.setText(game_models.get(position).getGame_name());

            if(game_models.get(position).getImage().equals("")){
                Glide.with(context)
                        .load(R.drawable.banner_3)
                        .into(holder.binding.koyelImage);
            }else{
                Glide.with(context)
                        .load(url+game_models.get(position).getImage())
                        .into(holder.binding.koyelImage);
            }




        holder.binding.cardKoyel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("game_id",game_models.get(position).getId());
                bundle.putString("game_name",game_models.get(position).getGame_name());
                bundle.putString("game_image",game_models.get(position).getImage());
                context.startActivity(new Intent(context, GameActivity.class).putExtras(bundle));
            }
        });

    }

    @Override
    public int getItemCount() {
        return game_models.size();
    }

    public class MyViewholder extends RecyclerView.ViewHolder {
        SingleGameListBinding binding;
        public MyViewholder(@NonNull SingleGameListBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }
    }
}
