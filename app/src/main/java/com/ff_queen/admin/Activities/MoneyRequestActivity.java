package com.ff_queen.admin.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.DividerItemDecoration;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.ff_queen.admin.Adapter.MoneyRequestAdapter;
import com.ff_queen.admin.Models.Money_Request_Model;
import com.ff_queen.admin.Models.User;
import com.ff_queen.admin.R;
import com.ff_queen.admin.ViewModel.ApiResponse;
import com.ff_queen.admin.databinding.ActivityMoneyRequestBinding;

import java.util.ArrayList;
import java.util.List;

public class MoneyRequestActivity extends AppCompatActivity {

    private ActivityMoneyRequestBinding binding;
    private ApiResponse apiResponse;
    private User user;
    private List<Money_Request_Model> request_models = new ArrayList<>();
    private MoneyRequestAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMoneyRequestBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);

        initView();
    }

    private void initView() {

        apiResponse = new ApiResponse(this);
        user = new User(this);

        getSupportActionBar().setTitle("View Money Request");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        binding.contentMoneyRequest.rvMoneyRequest.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));

        adapter = new MoneyRequestAdapter(this,request_models);
        binding.contentMoneyRequest.rvMoneyRequest.setAdapter(adapter);


        apiResponse.fetchMoneyRequest();
        apiResponse.getMoneyRequest().observe(this, new Observer<List<Money_Request_Model>>() {
            @Override
            public void onChanged(List<Money_Request_Model> money_request_models) {

                request_models = money_request_models;

                adapter.updateList(request_models);
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

}