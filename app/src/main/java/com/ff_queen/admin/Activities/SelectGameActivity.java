package com.ff_queen.admin.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.ff_queen.admin.Interfaces.MyInterface;
import com.ff_queen.admin.Models.Timing_Model;
import com.ff_queen.admin.R;
import com.ff_queen.admin.Utilities.ApiClient;
import com.ff_queen.admin.databinding.ActivitySelectGameBinding;

import java.util.ArrayList;
import java.util.List;

public class SelectGameActivity extends AppCompatActivity {

    private ActivitySelectGameBinding binding;
    List<Timing_Model> models = new ArrayList<>();
    MyInterface myInterface;
    String game_id="", game_name="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySelectGameBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);

        myInterface = ApiClient.getApiClient().create(MyInterface.class);

        game_id = getIntent().getStringExtra("game_id");
        game_name = getIntent().getStringExtra("game_name");

        if (game_id.equals("4"))
        {
            binding.contentSelectGame.linFatafat.setVisibility(View.VISIBLE);
            binding.contentSelectGame.linKoyel.setVisibility(View.GONE);
        }
        else
        {
            binding.contentSelectGame.linKoyel.setVisibility(View.VISIBLE);
            binding.contentSelectGame.linFatafat.setVisibility(View.GONE);
        }

        getSupportActionBar().setTitle("Select Game");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        binding.contentSelectGame.linSingleSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle bundle = new Bundle();
                bundle.putString("game_id",game_id);
                bundle.putString("game_name",game_name);
                bundle.putString("cat_id","1");
                startActivity(new Intent(SelectGameActivity.this,BettingHistoryActivity.class).putExtras(bundle));
            }
        });

        binding.contentSelectGame.linPattiSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle bundle = new Bundle();
                bundle.putString("game_id",game_id);
                bundle.putString("game_name",game_name);
                bundle.putString("cat_id","2");
                startActivity(new Intent(SelectGameActivity.this,BettingHistoryActivity.class).putExtras(bundle));
            }
        });


        binding.contentSelectGame.linLastDigit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle bundle = new Bundle();
                bundle.putString("game_id",game_id);
                bundle.putString("game_name",game_name);
                bundle.putString("cat_id","1");
                startActivity(new Intent(SelectGameActivity.this,KoyelBettingHistoryActivity.class).putExtras(bundle));
            }
        });


        binding.contentSelectGame.linMiddleDigit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle bundle = new Bundle();
                bundle.putString("game_id",game_id);
                bundle.putString("game_name",game_name);
                bundle.putString("cat_id","2");
                startActivity(new Intent(SelectGameActivity.this,KoyelBettingHistoryActivity.class).putExtras(bundle));
            }
        });


        binding.contentSelectGame.linLast2Digit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle bundle = new Bundle();
                bundle.putString("game_id",game_id);
                bundle.putString("game_name",game_name);
                bundle.putString("cat_id","3");
                startActivity(new Intent(SelectGameActivity.this,KoyelBettingHistoryActivity.class).putExtras(bundle));
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