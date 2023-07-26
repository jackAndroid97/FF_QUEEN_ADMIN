package com.ff_queen.admin.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.ff_queen.admin.R;
import com.ff_queen.admin.databinding.ActivityKoyelGameSelectBinding;

public class KoyelGameSelectActivity extends AppCompatActivity {

    ActivityKoyelGameSelectBinding binding;
    private String game_name, game_id, type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityKoyelGameSelectBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);

        game_name = getIntent().getStringExtra("game_name");
        game_id = getIntent().getStringExtra("game_id");
        type = getIntent().getStringExtra("type");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Select Digit");

        binding.contentKoyelGameSelect.linLastDigit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (type.equals("add_result"))
                {
                    Bundle b = new Bundle();
                    b.putString("game_name",game_name);
                    b.putString("game_id",game_id);
                    b.putString("cat_id","1");
                    startActivity(new Intent(KoyelGameSelectActivity.this,AddResultKoyelActivity.class).putExtras(b));
                }
                else
                {
                    Bundle b = new Bundle();
                    b.putString("game_name",game_name);
                    b.putString("game_id",game_id);
                    b.putString("cat_id","1");
                    startActivity(new Intent(KoyelGameSelectActivity.this,KoyelResultActivity.class).putExtras(b));
                }


            }
        });

        binding.contentKoyelGameSelect.linMiddleDigit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (type.equals("add_result"))
                {
                    Bundle b = new Bundle();
                    b.putString("game_name",game_name);
                    b.putString("game_id",game_id);
                    b.putString("cat_id","2");
                    startActivity(new Intent(KoyelGameSelectActivity.this,AddResultKoyelActivity.class).putExtras(b));
                }
                else
                {
                    Bundle b = new Bundle();
                    b.putString("game_name",game_name);
                    b.putString("game_id",game_id);
                    b.putString("cat_id","2");
                    startActivity(new Intent(KoyelGameSelectActivity.this,KoyelResultActivity.class).putExtras(b));
                }
            }
        });

        binding.contentKoyelGameSelect.linLast2Digit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (type.equals("add_result"))
                {
                    Bundle b = new Bundle();
                    b.putString("game_name",game_name);
                    b.putString("game_id",game_id);
                    b.putString("cat_id","3");
                    startActivity(new Intent(KoyelGameSelectActivity.this,AddResultKoyelActivity.class).putExtras(b));
                }
                else
                {
                    Bundle b = new Bundle();
                    b.putString("game_name",game_name);
                    b.putString("game_id",game_id);
                    b.putString("cat_id","3");
                    startActivity(new Intent(KoyelGameSelectActivity.this,KoyelResultActivity.class).putExtras(b));
                }
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId())
        {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

}