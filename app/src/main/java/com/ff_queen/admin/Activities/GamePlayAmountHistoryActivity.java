package com.ff_queen.admin.Activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ff_queen.admin.Interfaces.MyInterface;
import com.ff_queen.admin.Models.BidHistoryModel;
import com.ff_queen.admin.Models.GamePlayModel;
import com.ff_queen.admin.R;
import com.ff_queen.admin.Utilities.ApiClient;
import com.ff_queen.admin.Utilities.ProgressUtils;
import com.ff_queen.admin.ViewModel.ApiResponse;
import com.ff_queen.admin.databinding.ActivityBidHistoryBinding;
import com.ff_queen.admin.databinding.ActivityGamePlayHistoryBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GamePlayAmountHistoryActivity extends AppCompatActivity {

    public static ActivityGamePlayHistoryBinding binding;
    private ApiResponse apiResponse;
    private MyInterface myInterface;
    private final int MY_PERMISSIONS_REQUEST_USE_CAMERA = 0x00AF;
    int start_month, start_year, start_day;
    private String start_date = "";
    List<GamePlayModel> payment_models = new ArrayList<>();
    String date="";
    String game_id,digit,cat_id,baji;
    Transaction_Adapter adapter;
    RecyclerView rv_passbook;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGamePlayHistoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        myInterface = ApiClient.getApiClient().create(MyInterface.class);
        getSupportActionBar().setTitle("Total Game Play");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        date=getIntent().getExtras().getString("date");


        rv_passbook = findViewById(R.id.rv_transaction);
        rv_passbook.setHasFixedSize(true);
        rv_passbook.setLayoutManager(new LinearLayoutManager(GamePlayAmountHistoryActivity.this,LinearLayoutManager.VERTICAL,false));
       // date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        fetch_total_gameplay();


        binding.contentGamePlayHistory.buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name=binding.contentGamePlayHistory.name.getText().toString();
                if(name.equals("")){
                    binding.contentGamePlayHistory.name.setError("Enter mobile number");
                }else{
                   // fetch_transaction_history(name);
                }
            }
        });
    }

    public class Transaction_Adapter extends RecyclerView.Adapter<Transaction_Adapter.MyViewHolder> {

        Context context;
        List<GamePlayModel> models;

        public Transaction_Adapter(Context context, List<GamePlayModel> models) {
            this.context = context;
            this.models = models;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.single_game_play_history, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {

            holder.txt_name.setText(models.get(position).getName());
            holder.txt_date.setText(models.get(position).getDate());
            holder.txt_amt.setText("₹"+models.get(position).getAmt());
            holder.txt_mobile.setText(models.get(position).getMobile());

            holder.txt_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle=new Bundle();
                    bundle.putString("date",models.get(position).getDate());
                    bundle.putString("user_id",models.get(position).getU_id());
                    startActivity(new Intent(GamePlayAmountHistoryActivity.this,GamePlayDetailsActivity.class).putExtras(bundle));
                }
            });


        }

        @Override
        public int getItemCount() {
            return models.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            TextView txt_amt, txt_date, txt_name,txt_mobile,txt_view;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);

                txt_name = itemView.findViewById(R.id.txt_name);
                txt_amt = itemView.findViewById(R.id.txt_amt);
                txt_date = itemView.findViewById(R.id.txt_date);
                txt_mobile = itemView.findViewById(R.id.txt_mobile);
                txt_view = itemView.findViewById(R.id.txt_view);
            }
        }
    }

    private void fetch_total_gameplay() {
        Call<String> call = myInterface.fetch_total_gameplay(date);
        ProgressUtils.showLoadingDialog(GamePlayAmountHistoryActivity.this);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.body()!=null){
                    String res = response.body();
                    try {
                        JSONArray jsonArray = new JSONArray(res);
                        if (jsonArray.length() == 0) {
                            ProgressUtils.cancelLoading();
                            payment_models.clear();
                            rv_passbook.setVisibility(View.GONE);
//                            Toast.makeText(getActivity(), "No Data found", Toast.LENGTH_SHORT).show();
                        } else {
                            rv_passbook.setVisibility(View.VISIBLE);
                            payment_models.clear();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                payment_models.add(new GamePlayModel(
                                        jsonObject.getString("date"),
                                        jsonObject.getString("total_gameplay_amount"),
                                        jsonObject.getString("name"),
                                        jsonObject.getString("mobile"),
                                        jsonObject.getString("user_id")

                                ));
                            }
                            adapter = new Transaction_Adapter(GamePlayAmountHistoryActivity.this,payment_models);
                            rv_passbook.setAdapter(adapter);
                            ProgressUtils.cancelLoading();

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        ProgressUtils.cancelLoading();
                        Toast.makeText(GamePlayAmountHistoryActivity.this, ""+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(GamePlayAmountHistoryActivity.this, "No Response", Toast.LENGTH_SHORT).show();
                    ProgressUtils.cancelLoading();
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                ProgressUtils.cancelLoading();
                Toast.makeText(GamePlayAmountHistoryActivity.this, "Error", Toast.LENGTH_SHORT).show();
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




    @Override
    public void onBackPressed() {
        finish();
    }
}