package com.ff_queen.admin.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ff_queen.admin.Adapter.MoneyRequestAdapter;
import com.ff_queen.admin.Adapter.WithdrawAllRequestAdapter;
import com.ff_queen.admin.Interfaces.MyInterface;
import com.ff_queen.admin.Models.Money_Request_Model;
import com.ff_queen.admin.Models.User;
import com.ff_queen.admin.Models.Withdraw_All_Request_Model;
import com.ff_queen.admin.R;
import com.ff_queen.admin.Utilities.ApiClient;
import com.ff_queen.admin.Utilities.ProgressUtils;
import com.ff_queen.admin.ViewModel.ApiResponse;
import com.ff_queen.admin.databinding.ActivityMoneyRequestBinding;
import com.ff_queen.admin.databinding.SingleMoneyRequestBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MoneyRequestActivity extends AppCompatActivity {

    private ActivityMoneyRequestBinding binding;
    private ApiResponse apiResponse;
    private User user;
    private List<Money_Request_Model> request_models = new ArrayList<>();
    private MoneyRequestAdapter adapter;
    String type,date;
    MyInterface myInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMoneyRequestBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);

        initView();
    }

    private void initView() {

        myInterface = ApiClient.getApiClient().create(MyInterface.class);
        user = new User(this);

        getSupportActionBar().setTitle("View Money Request");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        binding.contentMoneyRequest.rvMoneyRequest.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));

        adapter = new MoneyRequestAdapter(this,request_models);
        binding.contentMoneyRequest.rvMoneyRequest.setAdapter(adapter);
        type=getIntent().getExtras().getString("type");
        date=getIntent().getExtras().getString("date");

        if(type.equals("request")){

            fetch_money_request("");
        }else{

            fetch_money_request_two("");
        }
        binding.contentMoneyRequest.buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name=binding.contentMoneyRequest.name.getText().toString();
                if(name.equals("")){
                    binding.contentMoneyRequest.name.setError("Enter mobile number");
                }else{
                    if(type.equals("request")){
                        fetch_money_request(name);
                    }else{
                        fetch_money_request_two(name);
                    }

                }
            }
        });
       // apiResponse.fetchMoneyRequest();



    }

    private void fetch_money_request(String number) {
        Call<String> call = myInterface.fetch_money_request(number,type);
        ProgressUtils.showLoadingDialog(MoneyRequestActivity.this);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.body()!=null){
                    String res = response.body();
                    try {
                        JSONArray jsonArray = new JSONArray(res);
                        if (jsonArray.length() == 0) {
                            ProgressUtils.cancelLoading();
                            // withdraw_request_models.clear();
                            Toast.makeText(MoneyRequestActivity.this, "No Data found", Toast.LENGTH_SHORT).show();
                        } else {
                            binding.contentMoneyRequest.rvMoneyRequest.setVisibility(View.VISIBLE);

                            request_models.clear();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                request_models.add(new Money_Request_Model(
                                        jsonObject.getString("id"),
                                        jsonObject.getString("user_id"),
                                        jsonObject.getString("name"),
                                        jsonObject.getString("amount")+".00",
                                        jsonObject.getString("date"),
                                        jsonObject.getString("bank_name"),
                                        jsonObject.getString("account_no"),
                                        jsonObject.getString("ifsc_code"),
                                        jsonObject.getString("mobile"),
                                        jsonObject.getString("wallet"),
                                        jsonObject.getString("time"),
                                        jsonObject.getString("t_id")
                                ));
                            }
                            adapter = new MoneyRequestAdapter(MoneyRequestActivity.this,request_models);
                            binding.contentMoneyRequest.rvMoneyRequest.setAdapter(adapter);
                            ProgressUtils.cancelLoading();

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        ProgressUtils.cancelLoading();
                        Toast.makeText(MoneyRequestActivity.this, ""+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(MoneyRequestActivity.this, "No Response", Toast.LENGTH_SHORT).show();
                    ProgressUtils.cancelLoading();
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                ProgressUtils.cancelLoading();
                Toast.makeText(MoneyRequestActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void fetch_money_request_two(String number) {
        Call<String> call = myInterface.fetch_money_request_two(date,number);
        ProgressUtils.showLoadingDialog(MoneyRequestActivity.this);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.body()!=null){
                    String res = response.body();
                    try {
                        JSONArray jsonArray = new JSONArray(res);
                        if (jsonArray.length() == 0) {
                            ProgressUtils.cancelLoading();
                            // withdraw_request_models.clear();
                            Toast.makeText(MoneyRequestActivity.this, "No Data found", Toast.LENGTH_SHORT).show();
                        } else {
                            binding.contentMoneyRequest.rvMoneyRequest.setVisibility(View.VISIBLE);

                            request_models.clear();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                request_models.add(new Money_Request_Model(
                                        jsonObject.getString("id"),
                                        jsonObject.getString("user_id"),
                                        jsonObject.getString("name"),
                                        jsonObject.getString("amount")+".00",
                                        jsonObject.getString("date"),
                                        jsonObject.getString("bank_name"),
                                        jsonObject.getString("account_no"),
                                        jsonObject.getString("ifsc_code"),
                                        jsonObject.getString("mobile"),
                                        jsonObject.getString("wallet"),
                                        jsonObject.getString("time"),
                                        jsonObject.getString("t_id")
                                ));
                            }
                            adapter = new MoneyRequestAdapter(MoneyRequestActivity.this,request_models);
                            binding.contentMoneyRequest.rvMoneyRequest.setAdapter(adapter);
                            ProgressUtils.cancelLoading();

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        ProgressUtils.cancelLoading();
                        Toast.makeText(MoneyRequestActivity.this, ""+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(MoneyRequestActivity.this, "No Response", Toast.LENGTH_SHORT).show();
                    ProgressUtils.cancelLoading();
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                ProgressUtils.cancelLoading();
                Toast.makeText(MoneyRequestActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                startActivity(new Intent(MoneyRequestActivity.this,MainActivity.class));
                finishAffinity();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        startActivity(new Intent(MoneyRequestActivity.this,MainActivity.class));
        finishAffinity();
    }
    public class MoneyRequestAdapter extends RecyclerView.Adapter<MoneyRequestAdapter.ViewHolder> {

        private Context context;
        private List<Money_Request_Model> money_request_models;
        private String formatDate;
        private ApiResponse apiResponse;
        private User user;

        public MoneyRequestAdapter(Context context, List<Money_Request_Model> money_request_models) {
            this.context = context;
            this.money_request_models = money_request_models;

            apiResponse = new ApiResponse(context);
            user = new User(context);
        }

        public void updateList(List<Money_Request_Model> request_models)
        {
            money_request_models = request_models;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            SingleMoneyRequestBinding binding = SingleMoneyRequestBinding.inflate(inflater,parent,false);

            return new ViewHolder(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

            Money_Request_Model model = money_request_models.get(position);


            try {

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date date = simpleDateFormat.parse(model.getDate());
                SimpleDateFormat sdf = new SimpleDateFormat("dd, LLL yyyy");
                formatDate = sdf.format(date);

            } catch (ParseException e) {

                e.printStackTrace();
            }


            holder.binding.name.setText(model.getName());
            holder.binding.amount.setText("₹ "+model.getAmount());
            holder.binding.date.setText(formatDate);
            holder.binding.time.setText(model.getTime());
            holder.binding.tId.setText("Transaction Id: "+model.getT_id());

            holder.binding.bankDetails.setText("Bank Name: "+ model.getBank_name()+"\n"+"Account No: "+model.getAccount_no()
                    +"\n"+"IFSC Code: "+model.getIfsc_code());
            holder.binding.mobile.setText("Mobile Number: "+model.getMobile());
            holder.binding.walletBalance.setText("Current Balance: ₹ "+model.getWallet());

            holder.binding.amount.setTextColor(Color.parseColor("#33A01D")); //Green

            if(type.equals("request")){
                holder.binding.btnApproed.setVisibility(View.VISIBLE);
                holder.binding.btnReject.setVisibility(View.VISIBLE);
            }else {
                holder.binding.btnApproed.setVisibility(View.GONE);
                holder.binding.btnReject.setVisibility(View.GONE);
            }

            holder.binding.passbook.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle=new Bundle();
                    bundle.putString("user_id",model.getUser_id());
                    context.startActivity(new Intent(context, Passbook.class).putExtras(bundle));

                }
            });

            holder.binding.btnApproed.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    apiResponse.approvedRequest(model.getUser_id(),model.getAmount(),model.getId(),"Success",holder.binding.remarks.getText().toString());

                }
            });
            holder.binding.btnReject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    apiResponse.approvedRequest(model.getUser_id(),model.getAmount(),model.getId(),"Reject",holder.binding.remarks.getText().toString());

                }
            });

        }

        @Override
        public int getItemCount() {
            return money_request_models.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            SingleMoneyRequestBinding binding;

            public ViewHolder(@NonNull SingleMoneyRequestBinding itemView) {
                super(itemView.getRoot());
                binding = itemView;
            }
        }
    }

}