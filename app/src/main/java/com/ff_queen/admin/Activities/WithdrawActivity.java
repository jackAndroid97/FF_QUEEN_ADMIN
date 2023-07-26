package com.ff_queen.admin.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.ff_queen.admin.databinding.ActivityWithdrawBinding;

public class WithdrawActivity extends AppCompatActivity {

    ActivityWithdrawBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWithdrawBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setTitle("Withdraw");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        binding.contentWithdraw.userWithdrawBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(WithdrawActivity.this,UserMoneyWithdrawActivity.class));
            }
        });

        binding.contentWithdraw.supDisWithdrawBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(WithdrawActivity.this,SuperDistributorWithdrawActivity.class));
            }
        });

        binding.contentWithdraw.distributorWithdrawBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(WithdrawActivity.this,DistributorWithdrawActivity.class));
            }
        });

        binding.contentWithdraw.retailerWithdrawBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(WithdrawActivity.this,RetailerWithdrawActivity.class));
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