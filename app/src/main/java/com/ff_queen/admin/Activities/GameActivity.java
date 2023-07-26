package com.ff_queen.admin.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.ff_queen.admin.R;

public class GameActivity extends AppCompatActivity {

    private CardView gameTimingBtn;
    private String game_id;
    private String game_name;
    private CardView bettingHistoryBtn;
    private CardView resultBtn;
    private CardView addResultsBtn;
    private String game_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        gameTimingBtn = findViewById(R.id.game_timing_btn);
        bettingHistoryBtn = findViewById(R.id.betting_history);
        resultBtn = findViewById(R.id.resultsBtn);
        addResultsBtn = findViewById(R.id.add_resultsBtn);

        game_id = getIntent().getStringExtra("game_id");
        game_name = getIntent().getStringExtra("game_name");
        game_image = getIntent().getStringExtra("game_image");


        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(game_name);


        gameTimingBtn.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("game_id",game_id);
            bundle.putString("game_name",game_name);
            bundle.putString("game_image",game_image);
            startActivity(new Intent(this,GameTimingsActivity.class).putExtras(bundle));
        });

        bettingHistoryBtn.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("game_id",game_id);
            bundle.putString("game_name",game_name);
            if (game_id.equals("4"))
            {
                startActivity(new Intent(this,SelectGameActivity.class).putExtras(bundle));
            }
            else
            {
                startActivity(new Intent(this,BettingHistoryActivity.class).putExtras(bundle));
            }

        });

        addResultsBtn.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("game_id",game_id);
            bundle.putString("game_name",game_name);
            if(game_id.equals("4")) {
                startActivity(new Intent(this, AddResultsActivity.class).putExtras(bundle));
            }else{
                startActivity(new Intent(this, AddOtherResult.class).putExtras(bundle));
            }
        });

        resultBtn.setOnClickListener(v -> {
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