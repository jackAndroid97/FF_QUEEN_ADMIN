package com.ff_queen.admin.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.ff_queen.admin.Interfaces.MyInterface;
import com.ff_queen.admin.R;
import com.ff_queen.admin.Utilities.ApiClient;
import com.ff_queen.admin.Utilities.ProgressUtils;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MarqueeActivity extends AppCompatActivity {

    private MyInterface myInterface;
    private String id, text;
    private TextInputLayout marquee_edit_txt;
    private Button btn_save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marquee);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources()
                .getColor(R.color.primary_dark)));
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Edit/Add Marquee");

        myInterface = ApiClient.getApiClient().create(MyInterface.class);

        marquee_edit_txt = findViewById(R.id.marquee_edit_txt);
        btn_save = findViewById(R.id.btn_save);

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (marquee_edit_txt.getEditText().getText().toString().isEmpty())
                {
                    Toast.makeText(MarqueeActivity.this, "Write Something", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    addMarquee();
                }
            }
        });

        fetchMarquee();

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

    private void fetchMarquee()
    {
        Call<String> call = myInterface.fetch_marquee();
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                String res = response.body();
                if (res == null) {
                    Toast.makeText(MarqueeActivity.this, "Error", Toast.LENGTH_SHORT).show();
                } else {

                    try {
                        JSONObject jsonObject = new JSONObject(res);

                        if (jsonObject.getString("rec").equals("0"))
                        {
                            Toast.makeText(MarqueeActivity.this, "Not Fetch", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            id = jsonObject.getString("id");
                            text = jsonObject.getString("text");
                        }

                    } catch (JSONException e) {

                        Toast.makeText(MarqueeActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

                Toast.makeText(MarqueeActivity.this, "Slow Network", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addMarquee()
    {
        Call<String> call = myInterface.update_marquee(id,marquee_edit_txt.getEditText().getText().toString());
        ProgressUtils.showLoadingDialog(MarqueeActivity.this);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                String res = response.body();
                if (res == null) {
                    Toast.makeText(MarqueeActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    ProgressUtils.cancelLoading();
                } else {

                    try {
                        JSONObject jsonObject = new JSONObject(res);

                        if (jsonObject.getString("rec").equals("0"))
                        {
                            Toast.makeText(MarqueeActivity.this, "Not Save", Toast.LENGTH_SHORT).show();
                            ProgressUtils.cancelLoading();
                        }
                        else
                        {
                            Toast.makeText(MarqueeActivity.this, "Successfully", Toast.LENGTH_SHORT).show();
                            fetchMarquee();
                            ProgressUtils.cancelLoading();

                            marquee_edit_txt.getEditText().setText("");
                        }

                    } catch (JSONException e) {

                        Toast.makeText(MarqueeActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                        ProgressUtils.cancelLoading();
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

                Toast.makeText(MarqueeActivity.this, "Slow Network", Toast.LENGTH_SHORT).show();
                ProgressUtils.cancelLoading();
            }
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}