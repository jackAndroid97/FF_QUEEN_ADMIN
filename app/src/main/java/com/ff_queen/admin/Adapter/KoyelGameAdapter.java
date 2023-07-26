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
import com.ff_queen.admin.Activities.DrLotteryActivity;
import com.ff_queen.admin.Activities.ThaiActivity;
import com.ff_queen.admin.Activities.TripuraLotteryActivity;
import com.ff_queen.admin.Models.Game_Model;
import com.ff_queen.admin.R;
import com.ff_queen.admin.databinding.SingleKoyelGameBinding;

import java.util.List;

public class KoyelGameAdapter extends RecyclerView.Adapter<KoyelGameAdapter.MyViewholder> {

    Context context;
    List<Game_Model> game_models;

    public KoyelGameAdapter(Context context, List<Game_Model> game_models) {
        this.context = context;
        this.game_models = game_models;
    }

    @NonNull
    @Override
    public MyViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        SingleKoyelGameBinding binding = SingleKoyelGameBinding.inflate(inflater, parent, false);
        return new MyViewholder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewholder holder, @SuppressLint("RecyclerView") int position) {

        holder.binding.dearLotteryName.setText(game_models.get(position).getGame_name());

        Glide.with(context)
                .load(game_models.get(position).getImage())
                .into(holder.binding.dearLotteryImage);

        holder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (game_models.get(position).getGame_name().equals("Tripura Lottery"))
                {
                    Bundle bundle = new Bundle();
                    bundle.putString("game_id",game_models.get(position).getId());
                    bundle.putString("game_name",game_models.get(position).getGame_name());
                    bundle.putString("game_image",game_models.get(position).getImage());
                    context.startActivity(new Intent(context, TripuraLotteryActivity.class).putExtras(bundle));
                }
                else if (game_models.get(position).getGame_name().equals("Dear Lottery"))
                {

                    Bundle bundle = new Bundle();
                    bundle.putString("game_id",game_models.get(position).getId());
                    bundle.putString("game_name",game_models.get(position).getGame_name());
                    bundle.putString("game_image",game_models.get(position).getImage());
                    context.startActivity(new Intent(context, DrLotteryActivity.class).putExtras(bundle));

                }
                else if (game_models.get(position).getGame_name().equals("Thai Lottery"))
                {
                    Bundle bundle = new Bundle();
                    bundle.putString("game_id",game_models.get(position).getId());
                    bundle.putString("game_name",game_models.get(position).getGame_name());
                    bundle.putString("game_image",game_models.get(position).getImage());
                    context.startActivity(new Intent(context, ThaiActivity.class).putExtras(bundle));
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return game_models.size();
    }

    public class MyViewholder extends RecyclerView.ViewHolder {
        SingleKoyelGameBinding binding;

        public MyViewholder(@NonNull SingleKoyelGameBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
