package com.ff_queen.admin.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.ff_queen.admin.R;
import com.ff_queen.admin.databinding.ActivitySpinBinding;

public class SpinActivity extends AppCompatActivity {

    ActivitySpinBinding binding;
    private String game_id, game_name, game_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySpinBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);

        game_id = getIntent().getStringExtra("game_id");
        game_name = getIntent().getStringExtra("game_name");
        game_image = getIntent().getStringExtra("game_image");

        getSupportActionBar().setTitle(game_name);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        binding.contentSpin.gameTimingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle bundle = new Bundle();
                bundle.putString("game_id",game_id);
                bundle.putString("game_name",game_name);
                bundle.putString("game_image",game_image);
                startActivity(new Intent(SpinActivity.this,GameTimingsActivity.class).putExtras(bundle));

            }
        });



        binding.contentSpin.bettingHistory.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("game_id",game_id);
            bundle.putString("game_name",game_name);
            startActivity(new Intent(this,BettingHistoryActivity.class).putExtras(bundle));
        });

        binding.contentSpin.addResultsBtn.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("game_id",game_id);
            bundle.putString("game_name",game_name);
            if(game_id.equals("4")) {
                startActivity(new Intent(this, AddResultsActivity.class).putExtras(bundle));
            }else{
                startActivity(new Intent(this, AddOtherResult.class).putExtras(bundle));
            }
        });



        binding.contentSpin.resultsBtn.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("game_id",game_id);
            bundle.putString("game_name",game_name);
            startActivity(new Intent(this, ResultActivity.class).putExtras(bundle));
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