package com.ff_queen.admin.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ff_queen.admin.Interfaces.MyInterface;
import com.ff_queen.admin.Models.Game_Model;
import com.ff_queen.admin.R;
import com.ff_queen.admin.Utilities.ApiClient;
import com.ff_queen.admin.Utilities.ProgressUtils;
import com.ff_queen.admin.databinding.ActivityGameOffBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GameOffActivity extends AppCompatActivity {

    ActivityGameOffBinding binding;
    MyInterface myInterface;
    List<Game_Model> game_models = new ArrayList<>();
    Lottery_Adapter lottery_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGameOffBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Game Off");

        myInterface = ApiClient.getApiClient().create(MyInterface.class);

        fetch_game();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                startActivity(new Intent(GameOffActivity.this,MainActivity.class));
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void fetch_game() {
        Call<String> call = myInterface.fetch_all_game();
        ProgressUtils.showLoadingDialog(GameOffActivity.this);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.body() != null) {
                    String res = response.body();
                    try {
                        JSONArray jsonArray = new JSONArray(res);
                        if (jsonArray.length() == 0) {
                            ProgressUtils.cancelLoading();
                            game_models.clear();
                            binding.contentGameOff.rvGames.setVisibility(View.GONE);
                        } else {
                            binding.contentGameOff.rvGames.setVisibility(View.VISIBLE);
                            game_models.clear();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                game_models.add(new Game_Model(jsonObject.getString("id"),
                                        jsonObject.getString("game_name"),
                                        jsonObject.getString("status"),
                                        jsonObject.getString("image")));
                            }
                            lottery_adapter = new Lottery_Adapter(GameOffActivity.this, game_models);
                            binding.contentGameOff.rvGames.setAdapter(lottery_adapter);
                            ProgressUtils.cancelLoading();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        ProgressUtils.cancelLoading();
                        Toast.makeText(GameOffActivity.this, "" + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(GameOffActivity.this, "No Response", Toast.LENGTH_SHORT).show();
                    ProgressUtils.cancelLoading();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                ProgressUtils.cancelLoading();
                Toast.makeText(GameOffActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void gameOffStatus(String id, String status)
    {
        Call<String> call = myInterface.game_off(id, status);
        ProgressUtils.showLoadingDialog(GameOffActivity.this);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                try {
                    JSONObject jsonObject = new JSONObject(response.body());

                    if (jsonObject.getString("rec").equals("1"))
                    {
                        //Toast.makeText(GameOffActivity.this, "Game Off", Toast.LENGTH_SHORT).show();
                        fetch_game();
                        ProgressUtils.cancelLoading();
                    }
                    else
                    {
                        Toast.makeText(GameOffActivity.this, "Game Not Off", Toast.LENGTH_SHORT).show();
                        ProgressUtils.cancelLoading();
                    }

                } catch (JSONException e) {

                    Toast.makeText(GameOffActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    ProgressUtils.cancelLoading();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

                Toast.makeText(GameOffActivity.this, "Slow Network", Toast.LENGTH_SHORT).show();
                ProgressUtils.cancelLoading();
            }
        });
    }

    public class Lottery_Adapter extends RecyclerView.Adapter<Lottery_Adapter.MyViewHolder> {

        Context context;
        List<Game_Model> models;

        public Lottery_Adapter(Context context, List<Game_Model> models) {
            this.context = context;
            this.models = models;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.single_lottery, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
            holder.lottery_name.setText(models.get(position).getGame_name());
            holder.status.setText(models.get(position).getStatus());
            if (models.get(position).getStatus().equals("Active")) {
                holder.status.setText("On");
            } else {
                holder.status.setText("Off");
            }
            Glide.with(context)
                    .load(models.get(position).getImage())
                    .into(holder.image);


            holder.status.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (holder.status.getText().toString().equals("Off"))
                    {
                        gameOffStatus(models.get(position).getId(), "Active");
                    }
                    else
                    {
                        gameOffStatus(models.get(position).getId(), "Deactive");
                    }


                }
            });

        }

        @Override
        public int getItemCount() {
            return models.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            TextView lottery_name, status;
            ImageView image;
            LinearLayout linear;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);

                lottery_name = itemView.findViewById(R.id.lottery_name);
                status = itemView.findViewById(R.id.status);
                image = itemView.findViewById(R.id.image);
                linear = itemView.findViewById(R.id.linear);

            }
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(GameOffActivity.this,MainActivity.class));
        finish();
    }
}