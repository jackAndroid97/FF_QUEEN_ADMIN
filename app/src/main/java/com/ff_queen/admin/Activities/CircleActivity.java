package com.ff_queen.admin.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.ff_queen.admin.R;
import com.ff_queen.admin.databinding.ActivityCircleBinding;

public class CircleActivity extends AppCompatActivity {

    ActivityCircleBinding binding;
    private String game_id, game_name, game_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCircleBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);

        game_id = getIntent().getStringExtra("game_id");
        game_name = getIntent().getStringExtra("game_name");
        game_image = getIntent().getStringExtra("game_image");

        getSupportActionBar().setTitle(game_name);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        binding.contentCircle.gameTimingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle bundle = new Bundle();
                bundle.putString("game_id",game_id);
                bundle.putString("game_name",game_name);
                bundle.putString("game_image",game_image);
                startActivity(new Intent(CircleActivity.this,GameTimingsActivity.class).putExtras(bundle));
            }
        });


        binding.contentCircle.bettingHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle bundle = new Bundle();
                bundle.putString("game_id",game_id);
                bundle.putString("game_name",game_name);
                startActivity(new Intent(CircleActivity.this,BettingHistoryActivity.class).putExtras(bundle));
            }
        });

        binding.contentCircle.addResultsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle bundle = new Bundle();
                bundle.putString("game_id",game_id);
                bundle.putString("game_name",game_name);
                if(game_id.equals("4")) {
                    startActivity(new Intent(CircleActivity.this, AddResultsActivity.class).putExtras(bundle));
                }else{
                    startActivity(new Intent(CircleActivity.this, AddOtherResult.class).putExtras(bundle));
                }

            }
        });



        binding.contentCircle.resultsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle bundle = new Bundle();
                bundle.putString("game_id",game_id);
                bundle.putString("game_name",game_name);
                startActivity(new Intent(CircleActivity.this, ResultActivity.class).putExtras(bundle));
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
        super.onBackPressed();
        finish();
    }


}