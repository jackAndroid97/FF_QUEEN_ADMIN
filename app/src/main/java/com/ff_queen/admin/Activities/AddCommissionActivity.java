package com.ff_queen.admin.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.ff_queen.admin.R;
import com.ff_queen.admin.databinding.ActivityAddCommissionBinding;

public class AddCommissionActivity extends AppCompatActivity {

    private ActivityAddCommissionBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddCommissionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Add Commission");

        binding.contentAddCommission.btnFatafat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle b = new Bundle();
                b.putString("type","FPK Last Two Digit");
                startActivity(new Intent(AddCommissionActivity.this,CommissionActivity.class).putExtras(b));
            }
        });

        binding.contentAddCommission.btnKoyel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle b = new Bundle();
                b.putString("type","Koyel Dear");
                startActivity(new Intent(AddCommissionActivity.this,CommissionActivity.class).putExtras(b));
            }
        });


        binding.contentAddCommission.btnOthers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle b = new Bundle();
                b.putString("type","Others");
                startActivity(new Intent(AddCommissionActivity.this,CommissionActivity.class).putExtras(b));
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
}