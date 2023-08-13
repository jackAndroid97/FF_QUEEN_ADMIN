package com.ff_queen.admin.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ff_queen.admin.EditOtherResultActivity;
import com.ff_queen.admin.Interfaces.MyInterface;
import com.ff_queen.admin.Models.Child_Result;
import com.ff_queen.admin.Models.Result;
import com.ff_queen.admin.Models.Result_Model;
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

    private String formattedDate;
    private String game_name;
    private String game_id;
    int page=0;
    boolean loadMore= false;
    int currentItems, totalItems, scrollOutItems;
    private NestedScrollView nested_scroll;
    Child_Result_Adapter child_result_adapter;
    List<Result_Model> result_models = new ArrayList<>();
    Result_Adapter result_adapter;
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

        binding.contentResult.rvResult.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        binding.contentResult.rvResult.setLayoutManager(linearLayoutManager);


        //binding.contentResult.rvResult.setAdapter(adapter);




        fetch_result();


    }

    private void fetch_result() {
        Call<String> call = myInterface.fetch_result(game_id);
        ProgressUtils.showLoadingDialog(ResultActivity.this);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.body()!=null){
                    String res = response.body();
                    try {
                        JSONObject jsonObject = new JSONObject(res);
                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        if (jsonArray.length() == 0) {
                            ProgressUtils.cancelLoading();
                            result_models.clear();
                            binding.contentResult.noText.setVisibility(View.VISIBLE);
                            binding.contentResult.rvResult.setVisibility(View.GONE);
                        } else {
                            result_models.clear();
                            binding.contentResult.noText.setVisibility(View.GONE);
                            binding.contentResult.rvResult.setVisibility(View.VISIBLE);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject_1 = jsonArray.getJSONObject(i);
                                List<Child_Result> single_results = new ArrayList<>();
                                JSONArray jsonArray_1 = jsonObject_1.getJSONArray("data");
                                for (int j = 0; j < jsonArray_1.length(); j++){
                                    JSONObject json = jsonArray_1.getJSONObject(j);
                                    //JSONObject json1 = jsonArray_1.getJSONObject(1);
                                    single_results.add(new Child_Result(json.getString("SINGLE"),
                                            json.getString("PATTI"),
                                            json.getString("start_time")));

                                }
                                result_models.add(new Result_Model(
                                        jsonObject_1.getString("date"),
                                        single_results));
                            }
                            result_adapter = new Result_Adapter(ResultActivity.this,result_models);
                            binding.contentResult.rvResult.setAdapter(result_adapter);
                            ProgressUtils.cancelLoading();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        ProgressUtils.cancelLoading();
                        Toast.makeText(ResultActivity.this, ""+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(ResultActivity.this, "No Response", Toast.LENGTH_SHORT).show();
                    ProgressUtils.cancelLoading();
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
//                progressDialog.dismiss();
                ProgressUtils.cancelLoading();
                Toast.makeText(ResultActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public class Result_Adapter extends RecyclerView.Adapter<Result_Adapter.MyViewHolder> {

        Context context;
        List<Result_Model> result_model;
        List<Child_Result> single_model;
        List<Child_Result> patti_model;

        public Result_Adapter(Context context, List<Result_Model> result_model) {
            this.context = context;
            this.result_model = result_model;
        }
        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.single_result, parent, false);
            return new MyViewHolder(view);

        }
        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
            Animation animation = AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.item_slide_right);
            holder.itemView.startAnimation(animation);
            holder.text_date.setText("Date: "+result_model.get(position).getDate());
            single_model=result_model.get(position).getSingle_models();
//            patti_model=result_model.get(position).getPatti_models();

            holder.rv_single.setHasFixedSize(true);
            holder.rv_single.setLayoutManager(new GridLayoutManager(ResultActivity.this,8));
            child_result_adapter = new Child_Result_Adapter(context,single_model);
            holder.rv_single.setAdapter(child_result_adapter);

            /*holder.rv_patti.setHasFixedSize(true);
            holder.rv_patti.setLayoutManager(new GridLayoutManager(Result_By_Game.this,8));
            child_result_adapter = new Child_Result_Adapter(context,patti_model);
            holder.rv_patti.setAdapter(child_result_adapter);*/

        }
        @Override
        public int getItemCount() {
            return result_model.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            TextView text_date;
            RecyclerView rv_single,rv_patti;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                text_date = itemView.findViewById(R.id.text_date);
                rv_single = itemView.findViewById(R.id.rv_single);
                /*rv_patti = itemView.findViewById(R.id.rv_patti);*/
            }
        }
    }

    public class Child_Result_Adapter extends RecyclerView.Adapter<Child_Result_Adapter.MyViewHolder> {
        Context context;
        List<Child_Result> child_results;

        public Child_Result_Adapter(Context context, List<Child_Result> child_results) {
            this.context = context;
            this.child_results = child_results;
        }
        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.single_item_result, parent, false);
            return new MyViewHolder(view);
        }
        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
//            holder.text_single_digit.setText(child_results.get(position).getSingle_digit());
//            holder.text_patti_digit.setText(child_results.get(position).getPatti_digit());
            if(child_results.get(position).getSingle_digit().equals("null")){
                holder.text_single_digit.setText("--");
            }else {
                holder.text_single_digit.setText(child_results.get(position).getSingle_digit());
            }

            if(child_results.get(position).getPatti_digit().equals("null")){
                holder.text_patti_digit.setText("--");
            }
            else{
                holder.text_patti_digit.setText(child_results.get(position).getPatti_digit());
                holder.txt_start_date.setText("baji\n"+child_results.get(position).getStart_date());

            }
        }
        @Override
        public int getItemCount() {
            return child_results.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView text_single_digit,text_patti_digit,text_digit,txt_start_date,text_jodi_digit;
            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                text_single_digit = itemView.findViewById(R.id.text_single_digit);
                text_patti_digit = itemView.findViewById(R.id.text_patti_digit);
                txt_start_date = itemView.findViewById(R.id.txt_start_date);
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