package com.ff_queen.admin.Activities;

import android.app.DatePickerDialog;
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
import com.ff_queen.admin.Models.PassBookModel;
import com.ff_queen.admin.R;
import com.ff_queen.admin.Utilities.ApiClient;
import com.ff_queen.admin.Utilities.ProgressUtils;
import com.ff_queen.admin.ViewModel.ApiResponse;
import com.ff_queen.admin.databinding.ActivityBidHistoryBinding;
import com.ff_queen.admin.databinding.ActivityLedgerReportBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BidHistoryActivity extends AppCompatActivity {

    public static ActivityBidHistoryBinding binding;
    private ApiResponse apiResponse;
    private MyInterface myInterface;
    private final int MY_PERMISSIONS_REQUEST_USE_CAMERA = 0x00AF;
    int start_month, start_year, start_day;
    private String start_date = "";
    List<PassBookModel> payment_models = new ArrayList<>();
    String date="";
    String game_id,game_name,cat_id;
    Transaction_Adapter adapter;
    RecyclerView rv_passbook;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBidHistoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        myInterface = ApiClient.getApiClient().create(MyInterface.class);
        getSupportActionBar().setTitle("Bid History");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        game_id=getIntent().getExtras().getString("game_id");
        game_name=getIntent().getExtras().getString("game_name");
        cat_id=getIntent().getExtras().getString("cat_id");

        rv_passbook = findViewById(R.id.rv_transaction);
        rv_passbook.setHasFixedSize(true);
        rv_passbook.setLayoutManager(new LinearLayoutManager(BidHistoryActivity.this,LinearLayoutManager.VERTICAL,false));
        date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        fetch_transaction_history();
    }

    public class Transaction_Adapter extends RecyclerView.Adapter<Transaction_Adapter.MyViewHolder> {

        Context context;
        List<PassBookModel> models;

        public Transaction_Adapter(Context context, List<PassBookModel> models) {
            this.context = context;
            this.models = models;
        }

        @NonNull
        @Override
        public Transaction_Adapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.single_transaction, parent, false);
            return new Transaction_Adapter.MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull Transaction_Adapter.MyViewHolder holder, int position) {


            holder.date.setText(models.get(position).getDate()+"\n"+models.get(position).getTime());
            holder.desc.setText(models.get(position).getDesc());
            holder.p_amount.setText("₹"+models.get(position).getPre_bal());
            holder.txt_amount.setText("₹"+models.get(position).getAmt());
            holder.txt_balance.setText("₹"+models.get(position).getAv_bal());



        }

        @Override
        public int getItemCount() {
            return models.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            TextView desc, date, txt_amount,time,p_amount,txt_balance;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);

                txt_balance = itemView.findViewById(R.id.txt_balance);
                date = itemView.findViewById(R.id.txt_date);
                txt_amount = itemView.findViewById(R.id.txt_amount);
                desc = itemView.findViewById(R.id.desc);
                p_amount = itemView.findViewById(R.id.p_amount);
            }
        }
    }

    private void fetch_transaction_history() {
        Call<String> call = myInterface.fetch_bid_history(game_id,cat_id,date);
        ProgressUtils.showLoadingDialog(BidHistoryActivity.this);
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
                                payment_models.add(new PassBookModel(jsonObject.getString("date"),
                                        jsonObject.getString("time"),
                                        jsonObject.getString("money_status"),
                                        jsonObject.getString("previous_amount"),
                                        jsonObject.getString("new_amount"),
                                        jsonObject.getString("ammount")

                                ));
                            }
                            adapter = new Transaction_Adapter(BidHistoryActivity.this,payment_models);
                            rv_passbook.setAdapter(adapter);
                            ProgressUtils.cancelLoading();

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        ProgressUtils.cancelLoading();
                        Toast.makeText(BidHistoryActivity.this, ""+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(BidHistoryActivity.this, "No Response", Toast.LENGTH_SHORT).show();
                    ProgressUtils.cancelLoading();
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                ProgressUtils.cancelLoading();
                Toast.makeText(BidHistoryActivity.this, "Error", Toast.LENGTH_SHORT).show();
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