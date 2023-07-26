package com.ff_queen.admin.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.DividerItemDecoration;

import android.os.Bundle;
import android.view.MenuItem;

import com.ff_queen.admin.Adapter.WithdrawAllRequestAdapter;
import com.ff_queen.admin.Models.User;
import com.ff_queen.admin.Models.Withdraw_All_Request_Model;
import com.ff_queen.admin.R;
import com.ff_queen.admin.ViewModel.ApiResponse;
import com.ff_queen.admin.databinding.ActivityRetailerWithdrawBinding;
import com.ff_queen.admin.databinding.ActivitySuperDistributorWithdrawBinding;

import java.util.ArrayList;
import java.util.List;

public class RetailerWithdrawActivity extends AppCompatActivity {

    private ActivityRetailerWithdrawBinding binding;
    private ApiResponse apiResponse;
    private User user;
    private List<Withdraw_All_Request_Model> withdraw_request_models = new ArrayList<>();
    public static WithdrawAllRequestAdapter adapter;
    private static final String type="Retailer";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRetailerWithdrawBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);

        initView();
    }


    private void initView() {

        apiResponse = new ApiResponse(this);
        user = new User(this);

        getSupportActionBar().setTitle("Retailer Withdraw");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        binding.contentRetailerWithdraw.rvMoneyRequest.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));

        adapter = new WithdrawAllRequestAdapter(this,withdraw_request_models,type);
        binding.contentRetailerWithdraw.rvMoneyRequest.setAdapter(adapter);


        apiResponse.fetchAllWithdrawRequest(type);
        apiResponse.getAllWithdrawRequest().observe(this, new Observer<List<Withdraw_All_Request_Model>>() {
            @Override
            public void onChanged(List<Withdraw_All_Request_Model> money_request_models) {

                withdraw_request_models = money_request_models;

                adapter.updateList(withdraw_request_models);
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