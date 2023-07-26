package com.ff_queen.admin.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
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
import android.widget.TextView;
import android.widget.Toast;

import com.ff_queen.admin.EditOtherResultActivity;
import com.ff_queen.admin.Interfaces.MyInterface;
import com.ff_queen.admin.Models.Result;
import com.ff_queen.admin.R;
import com.ff_queen.admin.Utilities.ApiClient;
import com.ff_queen.admin.Utilities.ProgressUtils;
import com.ff_queen.admin.databinding.ActivityGameTimingsBinding;
import com.ff_queen.admin.databinding.ActivityKoyelResultBinding;
import com.ff_queen.admin.databinding.ActivityResultBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResultActivity extends AppCompatActivity {

    ActivityResultBinding binding;
    private MyInterface myInterface;
    private List<Result> results = new ArrayList<>();
    private ResultAdapter adapter;
    private String formattedDate;
    private String game_name;
    private String game_id;
    int page=0;
    boolean loadMore= false;
    int currentItems, totalItems, scrollOutItems;
    private NestedScrollView nested_scroll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityResultBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        myInterface = ApiClient.getApiClient().create(MyInterface.class);

        game_name = getIntent().getStringExtra("game_name");
        game_id   = getIntent().getStringExtra("game_id");

        getSupportActionBar().setTitle(game_name);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        formattedDate = df.format(new Date());


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        binding.contentResult.rvResult.setLayoutManager(linearLayoutManager);

        adapter = new ResultAdapter(ResultActivity.this, results);
        binding.contentResult.rvResult.setAdapter(adapter);

        fetchFatafatResult(String.valueOf(page));

        binding.contentResult.nestedScroll.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
               /* currentItems = rv_game_timing.getLayoutManager().getChildCount();
                totalItems = rv_game_timing.getLayoutManager().getItemCount();
                scrollOutItems = manager.findFirstVisibleItemPosition();*/
                if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())){

                    //loadMore=true;
                    /*if (loadMore && (currentItems + scrollOutItems == totalItems)) {*/
                    page++;
                    binding.contentResult.progres.setVisibility(View.VISIBLE);

                    if(game_id.equals("4")) {
                        fetchFatafatResult(String.valueOf(page));
                    }else if(game_id.equals("11")) {
                        fetchLuckyResult(String.valueOf(page));
                    }else if(game_id.equals("7")) {
                        fetchThunderResult(String.valueOf(page));
                    }
                    else if(game_id.equals("16")) {
                        fetchCircleResult(String.valueOf(page));
                    }
                    else if(game_id.equals("6")) {
                        fetchSpinResult(String.valueOf(page));
                    }

                    if(game_id.equals("4")){
                        binding.contentResult.gameTypeTxt.setVisibility(View.GONE);
                    }else {
                        binding.contentResult.gameTypeTxt.setVisibility(View.VISIBLE);
                    }

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

    private void fetchFatafatResult(String page) {
        Call<String> call = myInterface.fetch_fatafat_result(page);
//        Toast.makeText(this, "" + game_id, Toast.LENGTH_SHORT).show();
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String res = response.body();
                if (res == null) {
                    Toast.makeText(ResultActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    binding.contentResult.progres.setVisibility(View.GONE);
                } else {
                    try {
                        JSONArray jsonArray = new JSONArray(res);
                        if (jsonArray.length() == 0) {
                            Toast.makeText(ResultActivity.this, "Not timings found.", Toast.LENGTH_SHORT).show();
                            ProgressUtils.cancelLoading();
                            binding.contentResult.progres.setVisibility(View.GONE);

                        } else {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                results.add(new Result(jsonObject.getString("id"),
                                        jsonObject.getString("result"),
                                        jsonObject.getString("date"),
                                        jsonObject.getString("start_time"),
                                        jsonObject.getString("cat_name"),
                                        jsonObject.getString("type"),
                                        jsonObject.getString("game_time_id"),
                                        jsonObject.getString("result_date"))
                                );

                                adapter.notifyDataSetChanged();
                            }
                        }
                    } catch (JSONException e) {
                        Toast.makeText(ResultActivity.this, "Something went wrong.", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                        binding.contentResult.progres.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(ResultActivity.this, "Something wnet wrong.", Toast.LENGTH_SHORT).show();
                binding.contentResult.progres.setVisibility(View.GONE);
            }
        });
    }

    private void fetchLuckyResult(String page) {
        Call<String> call = myInterface.fetchLuckyResult(page);
//        Toast.makeText(this, "" + game_id, Toast.LENGTH_SHORT).show();
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String res = response.body();
                if (res == null) {
                    Toast.makeText(ResultActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    binding.contentResult.progres.setVisibility(View.GONE);
                } else {
                    try {
                        JSONArray jsonArray = new JSONArray(res);
                        if (jsonArray.length() == 0) {
                            Toast.makeText(ResultActivity.this, "Not timings found.", Toast.LENGTH_SHORT).show();
                            ProgressUtils.cancelLoading();
                            binding.contentResult.progres.setVisibility(View.GONE);

                        } else {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                results.add(new Result(jsonObject.getString("id"),
                                        jsonObject.getString("result"),
                                        jsonObject.getString("date"),
                                        jsonObject.getString("start_time"),
                                        jsonObject.getString("cat_name"),
                                        jsonObject.getString("type"),
                                        jsonObject.getString("game_time_id"),
                                        jsonObject.getString("result_date"))
                                );

                                adapter.notifyDataSetChanged();
                            }
                        }
                    } catch (JSONException e) {
                        Toast.makeText(ResultActivity.this, "Something went wrong.", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                        binding.contentResult.progres.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(ResultActivity.this, "Something wnet wrong.", Toast.LENGTH_SHORT).show();
                binding.contentResult.progres.setVisibility(View.GONE);
            }
        });
    }

    private void fetchCircleResult(String page) {
        Call<String> call = myInterface.fetch_circle_result(page);
//        Toast.makeText(this, "" + game_id, Toast.LENGTH_SHORT).show();
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String res = response.body();
                if (res == null) {
                    Toast.makeText(ResultActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    binding.contentResult.progres.setVisibility(View.GONE);
                } else {
                    try {
                        JSONArray jsonArray = new JSONArray(res);
                        if (jsonArray.length() == 0) {
                            Toast.makeText(ResultActivity.this, "Not timings found.", Toast.LENGTH_SHORT).show();
                            binding.contentResult.progres.setVisibility(View.GONE);
                            ProgressUtils.cancelLoading();

                        } else {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                results.add(new Result(jsonObject.getString("id"),
                                        jsonObject.getString("result"),
                                        jsonObject.getString("date"),
                                        jsonObject.getString("start_time"),
                                        jsonObject.getString("cat_name"),
                                        jsonObject.getString("type"),
                                        jsonObject.getString("game_time_id"),
                                        jsonObject.getString("result_date"))
                                );

                                adapter.notifyDataSetChanged();
                            }
                        }
                    } catch (JSONException e) {
                        Toast.makeText(ResultActivity.this, "Something went wrong.", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                        binding.contentResult.progres.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(ResultActivity.this, "Something wnet wrong.", Toast.LENGTH_SHORT).show();
                binding.contentResult.progres.setVisibility(View.GONE);
            }
        });
    }

    private void fetchSpinResult(String page) {
        Call<String> call = myInterface.fetch_spin_result(page);
//        Toast.makeText(this, "" + game_id, Toast.LENGTH_SHORT).show();
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String res = response.body();
                if (res == null) {
                    Toast.makeText(ResultActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    binding.contentResult.progres.setVisibility(View.GONE);
                } else {
                    try {
                        JSONArray jsonArray = new JSONArray(res);
                        if (jsonArray.length() == 0) {
                            Toast.makeText(ResultActivity.this, "Not timings found.", Toast.LENGTH_SHORT).show();
                            ProgressUtils.cancelLoading();
                            binding.contentResult.progres.setVisibility(View.GONE);

                        } else {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                results.add(new Result(jsonObject.getString("id"),
                                        jsonObject.getString("result_digit"),
                                        jsonObject.getString("date"),
                                        jsonObject.getString("start_time"),
                                        jsonObject.getString("cat_name"),
                                        jsonObject.getString("type"),
                                        jsonObject.getString("game_time_id"),
                                        jsonObject.getString("result_date"))
                                );

                                adapter.notifyDataSetChanged();
                            }
                        }
                    } catch (JSONException e) {
                        Toast.makeText(ResultActivity.this, "Something went wrong.", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                        binding.contentResult.progres.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(ResultActivity.this, "Something wnet wrong.", Toast.LENGTH_SHORT).show();
                binding.contentResult.progres.setVisibility(View.GONE);
            }
        });
    }

    private void fetchThunderResult(String page) {
        Call<String> call = myInterface.fetchThunderResult(page);
//        Toast.makeText(this, "" + game_id, Toast.LENGTH_SHORT).show();
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String res = response.body();
                if (res == null) {
                    Toast.makeText(ResultActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    binding.contentResult.progres.setVisibility(View.GONE);
                } else {
                    try {
                        JSONArray jsonArray = new JSONArray(res);
                        if (jsonArray.length() == 0) {
                            Toast.makeText(ResultActivity.this, "Not timings found.", Toast.LENGTH_SHORT).show();
                            ProgressUtils.cancelLoading();
                            binding.contentResult.progres.setVisibility(View.GONE);

                        } else {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                results.add(new Result(jsonObject.getString("id"),
                                        jsonObject.getString("result"),
                                        jsonObject.getString("date"),
                                        jsonObject.getString("start_time"),
                                        jsonObject.getString("cat_name"),
                                        jsonObject.getString("type"),
                                        jsonObject.getString("game_time_id"),
                                        jsonObject.getString("result_date"))
                                );

                                adapter.notifyDataSetChanged();
                            }
                        }
                    } catch (JSONException e) {
                        Toast.makeText(ResultActivity.this, "Something went wrong.", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                        binding.contentResult.progres.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(ResultActivity.this, "Something wnet wrong.", Toast.LENGTH_SHORT).show();
                binding.contentResult.progres.setVisibility(View.GONE);
            }
        });
    }

    public class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.MyViewHolder> {
        List<Result> models;
        Context context;

        public ResultAdapter(@NonNull Context context, @NonNull List<Result> models) {
            this.models = models;
            this.context = context;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.single_result, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
            holder.date.setText(models.get(position).result_date);

            holder.starting_time.setText(models.get(position).time);
            if(game_id.equals("4")){
                holder.game_type.setVisibility(View.GONE);
            }else {
                holder.game_type.setVisibility(View.VISIBLE);
            }

            if(game_id.equals("6")){
                holder.game_type.setVisibility(View.GONE);
              //  holder.editBtn.setVisibility(View.GONE);
            }else {
                holder.game_type.setVisibility(View.VISIBLE);
               // holder.editBtn.setVisibility(View.VISIBLE);
            }

            holder.game_type.setText(models.get(position).cat_name);
            holder.result.setText(models.get(position).result);
            Date date2 = null;
            SimpleDateFormat df =new SimpleDateFormat("yyyy-MM-dd");


            holder.editBtn.setOnClickListener(view -> {
                try {
                if(game_id.equals("4")) {

                    if (models.get(position).type.equals("OPEN") && models.get(position).result_date.equals(formattedDate)) {
                        Bundle bundle = new Bundle();
                        bundle.putString("result_id", models.get(position).id);
                        bundle.putString("game_time_id", models.get(position).game_time_id);
                        bundle.putString("result_category", models.get(position).cat_name);
                        bundle.putString("result_result", models.get(position).result);
                        bundle.putString("result_date", models.get(position).result_date);
                        bundle.putString("game_time", models.get(position).time);
                        context.startActivity(new Intent(context, EditResultActivity.class).putExtras(bundle));
                    } else if (df.parse(models.get(position).result_date).after(df.parse(formattedDate)) ) {
                                Bundle bundle = new Bundle();
                                bundle.putString("result_id", models.get(position).id);
                                bundle.putString("game_time_id", models.get(position).game_time_id);
                                bundle.putString("result_category", models.get(position).cat_name);
                                bundle.putString("result_result", models.get(position).result);
                                bundle.putString("result_date", models.get(position).result_date);
                        bundle.putString("game_time", models.get(position).time);
                                context.startActivity(new Intent(context, EditResultActivity.class).putExtras(bundle));
                    } else {
                        Toast.makeText(context, "Result Timeout!", Toast.LENGTH_SHORT).show();
                    }


                }else if(game_id.equals("11")) {
                    if (models.get(position).type.equals("OPEN") && models.get(position).result_date.equals(formattedDate)) {
                        Bundle bundle = new Bundle();
                        bundle.putString("result_id", models.get(position).id);
                        bundle.putString("game_time_id", models.get(position).game_time_id);
                        bundle.putString("result_result", models.get(position).result);
                        bundle.putString("result_date", models.get(position).result_date);
                        bundle.putString("game_id", game_id);
                        bundle.putString("game_time", models.get(position).time);
                        context.startActivity(new Intent(context, EditOtherResultActivity.class).putExtras(bundle));
                    }
                    else if (df.parse(models.get(position).result_date).after(df.parse(formattedDate)) ) {
                        Bundle bundle = new Bundle();
                        bundle.putString("result_id", models.get(position).id);
                        bundle.putString("game_id", game_id);
                        bundle.putString("game_time_id", models.get(position).game_time_id);
                        bundle.putString("result_result", models.get(position).result);
                        bundle.putString("result_date", models.get(position).result_date);
                        bundle.putString("game_time", models.get(position).time);
                        context.startActivity(new Intent(context, EditOtherResultActivity.class).putExtras(bundle));
                    }
                    else {
                        Toast.makeText(context, "Result Timeout!", Toast.LENGTH_SHORT).show();
                    }
                }else if(game_id.equals("7")) {
                    if (models.get(position).type.equals("OPEN") && models.get(position).result_date.equals(formattedDate)) {
                      //  Toast.makeText(context, "1", Toast.LENGTH_SHORT).show();
                        Bundle bundle = new Bundle();
                        bundle.putString("result_id", models.get(position).id);
                        bundle.putString("game_time_id", models.get(position).game_time_id);
                        bundle.putString("result_result", models.get(position).result);
                        bundle.putString("result_date", models.get(position).result_date);
                        bundle.putString("game_id", game_id);
                        bundle.putString("game_time", models.get(position).time);
                        context.startActivity(new Intent(context, EditOtherResultActivity.class).putExtras(bundle));
                    } else if (df.parse(models.get(position).result_date).after(df.parse(formattedDate)) ) {
                        Bundle bundle = new Bundle();
                        bundle.putString("result_id", models.get(position).id);
                        bundle.putString("game_time_id", models.get(position).game_time_id);
                        bundle.putString("game_id", game_id);
                        bundle.putString("result_result", models.get(position).result);
                        bundle.putString("result_date", models.get(position).result_date);
                        bundle.putString("game_time", models.get(position).time);
                        context.startActivity(new Intent(context, EditOtherResultActivity.class).putExtras(bundle));
                    }else {
                        Toast.makeText(context, "Result Timeout!", Toast.LENGTH_SHORT).show();
                    }
                }
                else if(game_id.equals("16")) {
                    if (models.get(position).type.equals("OPEN") && models.get(position).result_date.equals(formattedDate)) {
                      //  Toast.makeText(context, "1", Toast.LENGTH_SHORT).show();
                        Bundle bundle = new Bundle();
                        bundle.putString("result_id", models.get(position).id);
                        bundle.putString("game_time_id", models.get(position).game_time_id);
                        bundle.putString("result_result", models.get(position).result);
                        bundle.putString("result_date", models.get(position).result_date);
                        bundle.putString("game_id", game_id);
                        bundle.putString("game_time", models.get(position).time);
                        context.startActivity(new Intent(context, EditOtherResultActivity.class).putExtras(bundle));
                    } else if (df.parse(models.get(position).result_date).after(df.parse(formattedDate)) ) {
                        Bundle bundle = new Bundle();
                        bundle.putString("result_id", models.get(position).id);
                        bundle.putString("game_time_id", models.get(position).game_time_id);
                        bundle.putString("result_result", models.get(position).result);
                        bundle.putString("result_date", models.get(position).result_date);
                        bundle.putString("game_time", models.get(position).time);
                        bundle.putString("game_id", game_id);
                        context.startActivity(new Intent(context, EditOtherResultActivity.class).putExtras(bundle));
                    }else {
                        Toast.makeText(context, "Result Timeout!", Toast.LENGTH_SHORT).show();

                    }
                }
                else if(game_id.equals("6")) {
                    if (models.get(position).type.equals("OPEN") && models.get(position).result_date.equals(formattedDate)) {
                       // Toast.makeText(context, "1", Toast.LENGTH_SHORT).show();
                        Bundle bundle = new Bundle();
                        bundle.putString("result_id", models.get(position).id);
                        bundle.putString("game_time_id", models.get(position).game_time_id);
                        bundle.putString("result_result", models.get(position).result);
                        bundle.putString("result_date", models.get(position).result_date);
                        bundle.putString("game_id", game_id);
                        bundle.putString("game_time", models.get(position).time);
                        context.startActivity(new Intent(context, EditOtherResultActivity.class).putExtras(bundle));
                    } else if (df.parse(models.get(position).result_date).after(df.parse(formattedDate)) ) {
                        Bundle bundle = new Bundle();
                        bundle.putString("result_id", models.get(position).id);
                        bundle.putString("game_time_id", models.get(position).game_time_id);
                        bundle.putString("result_result", models.get(position).result);
                        bundle.putString("result_date", models.get(position).result_date);
                        bundle.putString("game_time", models.get(position).time);
                        bundle.putString("game_id", game_id);
                        context.startActivity(new Intent(context, EditOtherResultActivity.class).putExtras(bundle));
                    }else {
                        Toast.makeText(context, "Result Timeout!", Toast.LENGTH_SHORT).show();
                    }
                }

                } catch (ParseException e) {
                    e.printStackTrace();
                }
            });
        }

        @Override
        public int getItemCount() {
            return models.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            TextView date, starting_time, game_type, result;
            ImageView editBtn;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                date = itemView.findViewById(R.id.date);
                starting_time = itemView.findViewById(R.id.starting_time);
                game_type = itemView.findViewById(R.id.game_type);
                result = itemView.findViewById(R.id.result);
                editBtn = itemView.findViewById(R.id.editBtn);
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