package com.ff_queen.admin.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ff_queen.admin.Adapter.WithdrawAllRequestAdapter;
import com.ff_queen.admin.Interfaces.MyInterface;
import com.ff_queen.admin.Models.User;
import com.ff_queen.admin.Models.Withdraw_All_Request_Model;
import com.ff_queen.admin.Utilities.ApiClient;
import com.ff_queen.admin.Utilities.ProgressUtils;
import com.ff_queen.admin.ViewModel.ApiResponse;
import com.ff_queen.admin.databinding.ActivitySuperDistributorWithdrawBinding;
import com.ff_queen.admin.databinding.ActivityUserMoneyWithdrawBinding;
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

public class SuperDistributorWithdrawActivity extends AppCompatActivity {

    private ActivitySuperDistributorWithdrawBinding binding;
    private ApiResponse apiResponse;
    MyInterface myInterface;
    private User user;
    private List<Withdraw_All_Request_Model> withdraw_request_models = new ArrayList<>();
    private WithdrawAllRequestAdapter adapter;
    private static final String type="Super Distributor";
    private String[] sample = {"MANUALLY","REQUEST"};
    String p_type,date;
    String type_txt="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySuperDistributorWithdrawBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);

        initView();

    }

    private void initView() {

        user = new User(this);

        getSupportActionBar().setTitle("Withdraw Request");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        myInterface = ApiClient.getApiClient().create(MyInterface.class);
        binding.contentSuperDistributorWithdraw.rvMoneyRequest.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        p_type=getIntent().getExtras().getString("type");
        date=getIntent().getExtras().getString("date");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, sample);

        binding.contentSuperDistributorWithdraw.type.setAdapter(adapter);
        if(p_type.equals("request")){

            fetchAllWithdrawRequest("");
            binding.contentSuperDistributorWithdraw.type.setVisibility(View.GONE);
        }else{

            fetchAllWithdrawRequestTwo("","");
            binding.contentSuperDistributorWithdraw.type.setVisibility(View.VISIBLE);

        }
        binding.contentSuperDistributorWithdraw.buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name=binding.contentSuperDistributorWithdraw.name.getText().toString();

                    if(p_type.equals("request")){
                        fetchAllWithdrawRequest(name);
                    }else{
                        fetchAllWithdrawRequestTwo(name,type_txt);
                    }


            }
        });

        binding.contentSuperDistributorWithdraw.type.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                type_txt=binding.contentSuperDistributorWithdraw.type.getText().toString();
            }
        });

     /*   apiResponse.getAllWithdrawRequest().observe(this, new Observer<List<Withdraw_All_Request_Model>>() {
            @Override
            public void onChanged(List<Withdraw_All_Request_Model> money_request_models) {

                withdraw_request_models = money_request_models;

                adapter.updateList(withdraw_request_models);
            }
        });*/

    }

    private void fetchAllWithdrawRequest(String name) {
        Call<String> call = myInterface.fetch_all_withdraw_req(name);
        ProgressUtils.showLoadingDialog(SuperDistributorWithdrawActivity.this);
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
                            Toast.makeText(SuperDistributorWithdrawActivity.this, "No Data found", Toast.LENGTH_SHORT).show();
                        } else {
                            binding.contentSuperDistributorWithdraw.rvMoneyRequest.setVisibility(View.VISIBLE);

                            withdraw_request_models.clear();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                withdraw_request_models.add(new Withdraw_All_Request_Model(
                                        jsonObject.getString("id"),
                                        jsonObject.getString("user_id"),
                                        jsonObject.getString("name"),
                                        jsonObject.getString("new_amount"),
                                        jsonObject.getString("date"),
                                        jsonObject.getString("bank_name"),
                                        jsonObject.getString("account_number"),
                                        jsonObject.getString("ifsc_number"),
                                        jsonObject.getString("mobile"),
                                        jsonObject.getString("wallet"),
                                        jsonObject.getString("time"),
                                        jsonObject.getString("upi")
                                ));
                            }
                            adapter = new WithdrawAllRequestAdapter(SuperDistributorWithdrawActivity.this,withdraw_request_models, type);
                            binding.contentSuperDistributorWithdraw.rvMoneyRequest.setAdapter(adapter);
                            ProgressUtils.cancelLoading();

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        ProgressUtils.cancelLoading();
                        Toast.makeText(SuperDistributorWithdrawActivity.this, ""+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(SuperDistributorWithdrawActivity.this, "No Response", Toast.LENGTH_SHORT).show();
                    ProgressUtils.cancelLoading();
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                ProgressUtils.cancelLoading();
                Toast.makeText(SuperDistributorWithdrawActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void fetchAllWithdrawRequestTwo(String number,String type) {
        Call<String> call = myInterface.fetch_all_withdraw_req_two(date,number,type);
        ProgressUtils.showLoadingDialog(SuperDistributorWithdrawActivity.this);
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
                            Toast.makeText(SuperDistributorWithdrawActivity.this, "No Data found", Toast.LENGTH_SHORT).show();
                        } else {
                            binding.contentSuperDistributorWithdraw.rvMoneyRequest.setVisibility(View.VISIBLE);

                            withdraw_request_models.clear();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                withdraw_request_models.add(new Withdraw_All_Request_Model(
                                        jsonObject.getString("id"),
                                        jsonObject.getString("user_id"),
                                        jsonObject.getString("name"),
                                        jsonObject.getString("new_amount"),
                                        jsonObject.getString("date"),
                                        jsonObject.getString("bank_name"),
                                        jsonObject.getString("account_number"),
                                        jsonObject.getString("ifsc_number"),
                                        jsonObject.getString("mobile"),
                                        jsonObject.getString("wallet"),
                                        jsonObject.getString("time"),
                                        jsonObject.getString("upi")
                                ));
                            }
                            adapter = new WithdrawAllRequestAdapter(SuperDistributorWithdrawActivity.this,withdraw_request_models, type);
                            binding.contentSuperDistributorWithdraw.rvMoneyRequest.setAdapter(adapter);
                            ProgressUtils.cancelLoading();

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        ProgressUtils.cancelLoading();
                        Toast.makeText(SuperDistributorWithdrawActivity.this, ""+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(SuperDistributorWithdrawActivity.this, "No Response", Toast.LENGTH_SHORT).show();
                    ProgressUtils.cancelLoading();
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                ProgressUtils.cancelLoading();
                Toast.makeText(SuperDistributorWithdrawActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if(p_type.equals("request")){
                    startActivity(new Intent(SuperDistributorWithdrawActivity.this,MainActivity.class));
                    finishAffinity();
                }else{
                    startActivity(new Intent(SuperDistributorWithdrawActivity.this,LedgerReportActivity.class));
                    finishAffinity();
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        if(p_type.equals("request")){
            startActivity(new Intent(SuperDistributorWithdrawActivity.this,MainActivity.class));
            finishAffinity();
        }else{
            startActivity(new Intent(SuperDistributorWithdrawActivity.this,LedgerReportActivity.class));
            finishAffinity();
        }
    }
    public class WithdrawAllRequestAdapter extends RecyclerView.Adapter<WithdrawAllRequestAdapter.ViewHolder> {

        private Context context;
        private List<Withdraw_All_Request_Model> money_request_models;
        private String formatDate;
        private ApiResponse apiResponse;
        private User user;
        private String type;

        public WithdrawAllRequestAdapter(Context context, List<Withdraw_All_Request_Model> money_request_models, String type) {
            this.context = context;
            this.money_request_models = money_request_models;
            this.type = type;

            apiResponse = new ApiResponse(context);
            user = new User(context);
        }

        public void updateList(List<Withdraw_All_Request_Model> request_models)
        {
            money_request_models = request_models;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            SingleMoneyRequestBinding  binding = SingleMoneyRequestBinding.inflate(inflater,parent,false);

            return new ViewHolder(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

            Withdraw_All_Request_Model model = money_request_models.get(position);


            try {

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date date = simpleDateFormat.parse(model.getDate());
                SimpleDateFormat sdf = new SimpleDateFormat("dd, LLL yyyy");
                formatDate = sdf.format(date);

            } catch (ParseException e) {

                e.printStackTrace();
            }
            if(model.getBank_name().equals("")){
                holder.binding.bankDetails.setVisibility(View.GONE);
            }
            else{
                holder.binding.bankDetails.setVisibility(View.VISIBLE);
            }

            if(p_type.equals("request")){
                holder.binding.btnApproed.setVisibility(View.VISIBLE);
                holder.binding.btnReject.setVisibility(View.VISIBLE);
                holder.binding.remarks.setVisibility(View.VISIBLE);
            }else {
                holder.binding.btnApproed.setVisibility(View.GONE);
                holder.binding.btnReject.setVisibility(View.GONE);
                holder.binding.remarks.setVisibility(View.GONE);
            }
            holder.binding.name.setText(model.getName());
            holder.binding.amount.setText("₹ "+model.getAmount());
            holder.binding.date.setText(formatDate);
            holder.binding.time.setText(model.getTime());
            holder.binding.tId.setText("UPI Id: "+model.getUpi_id());

            holder.binding.bankDetails.setText("Bank Name: "+ model.getBank_name()+"\n"+"Account No: "+model.getAc_no()
                    +"\n"+"IFSC Code: "+model.getIfsc());
            holder.binding.mobile.setText("Mobile Number: "+model.getMobile());
            holder.binding.walletBalance.setText("Current Balance: ₹ "+model.getBalance());


            holder.binding.amount.setTextColor(Color.parseColor("#33A01D")); //Green

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

                  //  apiResponse.withdrawAllApproved(model.getUser_id(),model.getAmount(),model.getId(),"ACCEPT",holder.binding.remarks.getText().toString());
                    Call<String> call = myInterface.withdraw_approved_all(model.getAmount(),model.getUser_id(),model.getId(),"ACCEPT",holder.binding.remarks.getText().toString());
                    ProgressUtils.showLoadingDialog(context);
                    call.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {

                            if (response.isSuccessful() && response.body() != null)
                            {
                                try {
                                    JSONObject jsonObject = new JSONObject(response.body());

                                    if (jsonObject.getString("rec").equals("1"))
                                    {

                                        Toast.makeText(context, "Done", Toast.LENGTH_SHORT).show();
                                        ProgressUtils.cancelLoading();
                                        Bundle bundle=new Bundle();
                                        bundle.putString("type",type);
                                        bundle.putString("date",date);
                                        startActivity(new Intent(SuperDistributorWithdrawActivity.this,SuperDistributorWithdrawActivity.class).putExtras(bundle));
                                        finish();
                                    }
                                    else
                                    {
                                        Toast.makeText(context, "Not Approved", Toast.LENGTH_SHORT).show();
                                        ProgressUtils.cancelLoading();
                                    }

                                } catch (JSONException e) {

                                    Toast.makeText(context, "Something Went wrong", Toast.LENGTH_SHORT).show();
                                    ProgressUtils.cancelLoading();
                                }
                            }
                            else
                            {
                                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                                ProgressUtils.cancelLoading();
                            }

                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {

                            Toast.makeText(context, "Slow Network", Toast.LENGTH_SHORT).show();
                            ProgressUtils.cancelLoading();
                        }
                    });
                }
            });
            holder.binding.btnReject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //apiResponse.withdrawAllApproved(model.getUser_id(),model.getAmount(),model.getId(),"REJECT",holder.binding.remarks.getText().toString());
                    Call<String> call = myInterface.withdraw_approved_all(model.getAmount(),model.getUser_id(),model.getId(),"REJECT",holder.binding.remarks.getText().toString());
                    ProgressUtils.showLoadingDialog(context);
                    call.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {

                            if (response.isSuccessful() && response.body() != null)
                            {
                                try {
                                    JSONObject jsonObject = new JSONObject(response.body());

                                    if (jsonObject.getString("rec").equals("1"))
                                    {

                                        Toast.makeText(context, "Done", Toast.LENGTH_SHORT).show();
                                        ProgressUtils.cancelLoading();
                                        Bundle bundle=new Bundle();
                                        bundle.putString("type",type);
                                        bundle.putString("date",date);
                                        startActivity(new Intent(SuperDistributorWithdrawActivity.this,SuperDistributorWithdrawActivity.class).putExtras(bundle));
                                        finish();
                                    }
                                    else
                                    {
                                        Toast.makeText(context, "Not Approved", Toast.LENGTH_SHORT).show();
                                        ProgressUtils.cancelLoading();
                                    }

                                } catch (JSONException e) {

                                    Toast.makeText(context, "Something Went wrong", Toast.LENGTH_SHORT).show();
                                    ProgressUtils.cancelLoading();
                                }
                            }
                            else
                            {
                                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                                ProgressUtils.cancelLoading();
                            }

                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {

                            Toast.makeText(context, "Slow Network", Toast.LENGTH_SHORT).show();
                            ProgressUtils.cancelLoading();
                        }
                    });
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