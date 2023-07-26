package com.ff_queen.admin.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.ff_queen.admin.R;
import com.ff_queen.admin.databinding.ActivityThaiBinding;

public class ThaiActivity extends AppCompatActivity {

    private ActivityThaiBinding binding;
    private String game_id, game_name, game_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityThaiBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);

        game_id = getIntent().getStringExtra("game_id");
        game_name = getIntent().getStringExtra("game_name");
        game_image = getIntent().getStringExtra("game_image");

        getSupportActionBar().setTitle(game_name);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        binding.contentThai.gameTimingBtn.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("game_id",game_id);
            bundle.putString("game_name",game_name);
            bundle.putString("game_image",game_image);
            startActivity(new Intent(this,GameTimingsActivity.class).putExtras(bundle));
        });


        binding.contentThai.addResultsBtn.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("game_id",game_id);
            bundle.putString("game_name",game_name);
            bundle.putString("type","add_result");
            startActivity(new Intent(this, KoyelGameSelectActivity.class).putExtras(bundle));
        });


        binding.contentThai.bettingHistory.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("game_id",game_id);
            bundle.putString("game_name",game_name);
            startActivity(new Intent(this,SelectGameActivity.class).putExtras(bundle));
        });


        binding.contentThai.resultsBtn.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("game_id",game_id);
            bundle.putString("game_name",game_name);
            bundle.putString("type","view_result");
            startActivity(new Intent(this, KoyelGameSelectActivity.class).putExtras(bundle));
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