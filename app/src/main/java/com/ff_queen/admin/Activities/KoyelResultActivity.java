package com.ff_queen.admin.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ff_queen.admin.Interfaces.MyInterface;
import com.ff_queen.admin.Models.Koyel_Result_Model;
import com.ff_queen.admin.R;
import com.ff_queen.admin.Utilities.ApiClient;
import com.ff_queen.admin.Utilities.ProgressUtils;
import com.ff_queen.admin.databinding.ActivityKoyelResultBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class KoyelResultActivity extends AppCompatActivity {

    private ActivityKoyelResultBinding binding;
    private MyInterface myInterface;
    private List<Koyel_Result_Model> result_models = new ArrayList<>();
    private String game_name,game_id,cat_id;
    TextView nig;
    int page=0;
    boolean loadMore= false;
    int currentItems, totalItems, scrollOutItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityKoyelResultBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);

        myInterface = ApiClient.getApiClient().create(MyInterface.class);
        game_name = getIntent().getStringExtra("game_name");
        game_id = getIntent().getStringExtra("game_id");
        cat_id = getIntent().getStringExtra("cat_id");
        nig=findViewById(R.id.nig);
        getSupportActionBar().setTitle(game_name);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //binding.rvResult.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        if(game_id.equals("20")){
            nig.setVisibility(View.VISIBLE);
        }else {
            nig.setVisibility(View.GONE);
        }
        fetch_koyel_Result(String.valueOf(page));


        binding.contentKoyelResult.nestedScroll.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
               /* currentItems = rv_game_timing.getLayoutManager().getChildCount();
                totalItems = rv_game_timing.getLayoutManager().getItemCount();
                scrollOutItems = manager.findFirstVisibleItemPosition();*/
                if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())){

                    //loadMore=true;
                    /*if (loadMore && (currentItems + scrollOutItems == totalItems)) {*/
                    page++;
                    binding.contentKoyelResult.progres.setVisibility(View.VISIBLE);

                    fetch_koyel_Result(String.valueOf(page));

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void fetch_koyel_Result(String page) {
        Call<String> call = myInterface.fetch_koyel_result(game_id,page,cat_id);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {

                    JSONArray jsonArray = null;
                    try {
                        jsonArray = new JSONArray(response.body());

                        if (jsonArray.length()==0)
                        {
                            binding.contentKoyelResult.rvResult.setVisibility(View.GONE);
                            Toast.makeText(KoyelResultActivity.this, "No Result Found", Toast.LENGTH_SHORT).show();
                            binding.contentKoyelResult.progres.setVisibility(View.GONE);
                        }
                        else
                        {
                            binding.contentKoyelResult.rvResult.setVisibility(View.VISIBLE);
                            result_models.clear();
                            for (int i=0; i<jsonArray.length(); i++)
                            {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String result = jsonObject.getString("result");
                                String result_type = jsonObject.getString("result_type");
                                String result_date = jsonObject.getString("result_date");

                                Koyel_Result_Model model = new Koyel_Result_Model();

                                model.setDate(result_date);

                                if (result_type.equals("MOR"))
                                {
                                    model.setMorning_result(result);
                                }
                                else if (result_type.equals("DAY"))
                                {
                                    model.setDay_result(result);
                                }
                                else if (result_type.equals("EVE"))
                                {
                                    model.setEvening_result(result);
                                }
                                else if (result_type.equals("NIG"))
                                {
                                    model.setNight_result(result);
                                }

                                result_models.add(model);
                            }

                            binding.contentKoyelResult.rvResult.setAdapter(new ResultAdapter(result_models));
                            binding.contentKoyelResult.progres.setVisibility(View.GONE);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        binding.contentKoyelResult.progres.setVisibility(View.GONE);
                    }


                } else {
                    ProgressUtils.cancelLoading();
                    result_models.clear();
                    binding.contentKoyelResult.rvResult.setVisibility(View.GONE);
                    Toast.makeText(KoyelResultActivity.this, "No records found", Toast.LENGTH_SHORT).show();
                    binding.contentKoyelResult.progres.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(KoyelResultActivity.this, "Something went wrong.", Toast.LENGTH_SHORT).show();
                binding.contentKoyelResult.progres.setVisibility(View.GONE);
            }
        });
    }


    public class ResultAdapter extends RecyclerView.Adapter<MyViewHolder> {
        Context context;
        List<Koyel_Result_Model> models;

        public ResultAdapter(@NonNull List<Koyel_Result_Model> models) {
            this.models = models;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.single_koyel_result, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {

            MyViewHolder rowViewHolder = (MyViewHolder) holder;

                Koyel_Result_Model modal = models.get(position);
                if(game_id.equals("20")){
                    rowViewHolder.txt_night.setVisibility(View.VISIBLE);
                }else{
                    rowViewHolder.txt_night.setVisibility(View.GONE);
                }
                rowViewHolder.txt_date.setText(modal.getDate());
                rowViewHolder.txt_morning.setText(modal.getMorning_result());
                rowViewHolder.txt_day.setText(modal.getDay_result());
                rowViewHolder.txt_evening.setText(modal.getEvening_result());
                rowViewHolder.txt_night.setText(modal.getNight_result());


        }

        @Override
        public int getItemCount() {
            return models.size();
        }

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txt_date, txt_morning, txt_day, txt_evening,txt_night;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_date = itemView.findViewById(R.id.txt_date);
            txt_morning = itemView.findViewById(R.id.txt_morning);
            txt_day = itemView.findViewById(R.id.txt_day);
            txt_evening = itemView.findViewById(R.id.txt_evening);
            txt_night = itemView.findViewById(R.id.txt_night);


        }
    }

}