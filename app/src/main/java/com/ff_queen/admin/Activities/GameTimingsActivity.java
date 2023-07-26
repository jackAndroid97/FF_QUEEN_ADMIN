package com.ff_queen.admin.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
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
import com.ff_queen.admin.Models.Timing_Model;
import com.ff_queen.admin.R;
import com.ff_queen.admin.Utilities.ApiClient;
import com.ff_queen.admin.databinding.ActivityGameTimingsBinding;
import com.ff_queen.admin.databinding.ActivityResultBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GameTimingsActivity extends AppCompatActivity {

    ActivityGameTimingsBinding binding;

    private GameTimeAdapter adapter;
    private List<Timing_Model> timing_model_list = new ArrayList<>();
    private MyInterface myInterface;
    private String game_id, game_name;
    private String game_image;
    int page=0;
    boolean loadMore= false;
    int currentItems, totalItems, scrollOutItems;
    String url="http://web.easytodb.com/Play_Win/admin_api/Game_image/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGameTimingsBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);


        myInterface = ApiClient.getApiClient().create(MyInterface.class);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        game_id = getIntent().getStringExtra("game_id");
        game_name = getIntent().getStringExtra("game_name");
        game_image = getIntent().getStringExtra("game_image");

        getSupportActionBar().setTitle(game_name);


        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2, LinearLayoutManager.VERTICAL, false);
        binding.contentGameTimings.rvGameTimings.setLayoutManager(gridLayoutManager);
        adapter = new GameTimeAdapter(GameTimingsActivity.this, timing_model_list);
        binding.contentGameTimings.rvGameTimings.setAdapter(adapter);

        fetchGameTimings(game_id, String.valueOf(page));




        binding.contentGameTimings.nestedScroll.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
               /* currentItems = binding.contentGameTimings.rvGameTimings.getLayoutManager().getChildCount();
                totalItems = binding.contentGameTimings.rvGameTimings.getLayoutManager().getItemCount();
                scrollOutItems = manager.findFirstVisibleItemPosition();*/
                if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())){

                    //loadMore=true;
                    /*if (loadMore && (currentItems + scrollOutItems == totalItems)) {*/
                    page++;
                    binding.contentGameTimings.progres.setVisibility(View.VISIBLE);

                        fetchGameTimings(game_id, String.valueOf(page));

                    /* }*/
                }
                if (scrollY < oldScrollY ){
                    loadMore =false;
                }else{
                    loadMore = true;
                }
            }
        });




    }

    private void fetchGameTimings(String game_id, String page) {
        //ProgressUtils.showLoadingDialog(this);
        Call<String> call = myInterface.fatafat_game_timings(game_id, page,"pagination");
       // Toast.makeText(this, "" + game_id, Toast.LENGTH_SHORT).show();
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String res = response.body();
                if (res == null) {
                    //ProgressUtils.cancelLoading();
                    Toast.makeText(GameTimingsActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    binding.contentGameTimings.progres.setVisibility(View.GONE);
                } else {
                    try {
                        JSONArray jsonArray = new JSONArray(res);
                        if (jsonArray.length() == 0) {
                         //   Toast.makeText(GameTimingsActivity.this, "Not timings found.", Toast.LENGTH_SHORT).show();
                            //ProgressUtils.cancelLoading();
                            binding.contentGameTimings.progres.setVisibility(View.GONE);

                        } else {
                            //timing_model_list.clear();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                timing_model_list.add(new Timing_Model(jsonObject.getString("game_id"),
                                        jsonObject.getString("now_time"),
                                        jsonObject.getString("status"),
                                        jsonObject.getString("start_time"),
                                        jsonObject.getString("end_time"),
                                        jsonObject.getString("count"),
                                        jsonObject.getString("date_status")
                                ));

                                adapter.notifyDataSetChanged();
                                //ProgressUtils.cancelLoading();
                            }
                        }
                    } catch (JSONException e) {
                        Toast.makeText(GameTimingsActivity.this, "Something went wrong.", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                        //ProgressUtils.cancelLoading();
                        binding.contentGameTimings.progres.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(GameTimingsActivity.this, "Something went wrong.", Toast.LENGTH_SHORT).show();
                //ProgressUtils.cancelLoading();
                binding.contentGameTimings.progres.setVisibility(View.GONE);
            }
        });
    }





    private class GameTimeAdapter extends RecyclerView.Adapter<GameTimeAdapter.GameTimeViewHolder> {

        private final Context context;
        private List<Timing_Model> list;

        public GameTimeAdapter(@NonNull Context context, @NonNull List<Timing_Model> list) {
            this.list = list;
            this.context = context;
        }

        @NonNull
        @Override
        public GameTimeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.custom_game_timing_layout, parent, false);
            return new GameTimeViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull GameTimeViewHolder holder, int position) {

            Glide.with(context).load(game_image).into(holder.imageView);
            holder.startTime.setText(list.get(position).start_time);
            holder.endTime.setText(list.get(position).end_time);
            holder.baji.setText(list.get(position).baji);

            if(game_image.equals("")){
                Glide.with(context)
                        .load(R.drawable.banner_3)
                        .into(holder.imageView);
            }else{
                Glide.with(context)
                        .load(url+game_image)
                        .into(holder.imageView);
            }


        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public class GameTimeViewHolder extends RecyclerView.ViewHolder {

            private ImageView imageView;
            private TextView baji,baji_text,baji_text_2, startTime, endTime;
            private LinearLayout lin_start_time;

            public GameTimeViewHolder(@NonNull View itemView) {
                super(itemView);

                imageView = itemView.findViewById(R.id.game_image);
                startTime = itemView.findViewById(R.id.starting_time);
                endTime = itemView.findViewById(R.id.ending_time);
                baji = itemView.findViewById(R.id.baji);
                baji_text = itemView.findViewById(R.id.baji_text);
                baji_text_2 = itemView.findViewById(R.id.baji_text_2);
                lin_start_time = itemView.findViewById(R.id.lin_start_time);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}