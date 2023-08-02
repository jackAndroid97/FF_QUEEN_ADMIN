package com.ff_queen.admin.Activities;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.ff_queen.admin.Interfaces.MyInterface;
import com.ff_queen.admin.Models.PassBookModel;
import com.ff_queen.admin.Models.User;
import com.ff_queen.admin.R;
import com.ff_queen.admin.Utilities.ApiClient;
import com.ff_queen.admin.Utilities.ProgressUtils;
import com.ff_queen.admin.databinding.ActivityPassbookBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Passbook extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityPassbookBinding binding;
    MyInterface myInterface;
    User user;
    String user_id;
    RecyclerView rv_passbook;
    List<PassBookModel> payment_models = new ArrayList<>();
    Transaction_Adapter adapter;

    private NetworkChangeReceiver receiver;
    private boolean isConnected = false;
    AlertDialog dialog;
    AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityPassbookBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);



        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        myInterface = ApiClient.getApiClient().create(MyInterface.class);
        user = new User(this);
        user_id = getIntent().getStringExtra("user_id");

        rv_passbook = findViewById(R.id.rv_transaction);
        rv_passbook.setHasFixedSize(true);
        rv_passbook.setLayoutManager(new LinearLayoutManager(Passbook.this,LinearLayoutManager.VERTICAL,false));
        fetch_transaction_history();

        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        receiver = new NetworkChangeReceiver();
        registerReceiver(receiver, filter);
//
//        //Alert Dialog
        builder= new AlertDialog.Builder(this);
        View view1=getLayoutInflater().inflate(R.layout.custom_no_internet,null);
        builder.setView(view1);
        dialog=builder.create();
        dialog.setCancelable(false);

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
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.single_transaction, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {


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
        Call<String> call = myInterface.fetch_transaction_history(user_id);
        ProgressUtils.showLoadingDialog(Passbook.this);
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
                            adapter = new Transaction_Adapter(Passbook.this,payment_models);
                            rv_passbook.setAdapter(adapter);
                            ProgressUtils.cancelLoading();

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        ProgressUtils.cancelLoading();
                        Toast.makeText(Passbook.this, ""+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(Passbook.this, "No Response", Toast.LENGTH_SHORT).show();
                    ProgressUtils.cancelLoading();
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                ProgressUtils.cancelLoading();
                Toast.makeText(Passbook.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed() {
        overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
        finish();
    }

    private boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        if(!isConnected){
                            dialog.dismiss();
                            // networkStatus.setText("Now you are connected to Internet!");
                            isConnected = true;
                            //do your processing here ---
                            //if you need to post any data to the server or get status
                            //update from the server
                        }
                        return true;
                    }
                }
            }
        }
        if(!((Activity) context).isFinishing())
        {
            dialog.show();
        }

        //  networkStatus.setText("You are not connected to Internet!");
        isConnected = false;
        return false;
    }

    public class NetworkChangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (isNetworkAvailable(context)){
                dialog.dismiss();
                isConnected = true;
            }else {
                if(!((Activity) context).isFinishing())
                {
                    dialog.show();
                }

                //  networkStatus.setText("You are not connected to Internet!");
                isConnected = false;
            }
        }
    }
}