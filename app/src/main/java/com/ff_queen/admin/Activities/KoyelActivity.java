package com.ff_queen.admin.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.ff_queen.admin.Adapter.KoyelGameAdapter;
import com.ff_queen.admin.Interfaces.MyInterface;
import com.ff_queen.admin.Models.Game_Model;
import com.ff_queen.admin.R;
import com.ff_queen.admin.Utilities.ApiClient;
import com.ff_queen.admin.Utilities.ProgressUtils;
import com.ff_queen.admin.databinding.ActivityKoyelBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class KoyelActivity extends AppCompatActivity {

    private ActivityKoyelBinding binding;
    private MyInterface myInterface;
    private String dear_game_id="", tripura_game_id="", thai_game_id="", dear_image_url="", tripura_image_url="", thai_image_url="";
    List<Game_Model> game_models = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityKoyelBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setTitle("Koyel");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        myInterface = ApiClient.getApiClient().create(MyInterface.class);

        fetchGame();


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


    private void fetchGame() {
        Call<String> call = myInterface.fetch_koyel_game();
        ProgressUtils.showLoadingDialog(KoyelActivity.this);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.body() != null) {
                    String res = response.body();
                    try {
                        JSONArray jsonArray = new JSONArray(res);
                        if (jsonArray.length() == 0) {
                            ProgressUtils.cancelLoading();

                        } else {

                            game_models.clear();
                            for (int i=0; i<jsonArray.length(); i++)
                            {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String game_id = jsonObject.getString("id");
                                String game_name = jsonObject.getString("game_name");
                                String image = jsonObject.getString("image");

                                game_models.add(new Game_Model(game_id,game_name,"",image));
                            }
                            binding.contentKoyel.rvKoyelGame.setAdapter(new KoyelGameAdapter(KoyelActivity.this,game_models));

                            ProgressUtils.cancelLoading();

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        ProgressUtils.cancelLoading();
                        Toast.makeText(KoyelActivity.this, "" + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(KoyelActivity.this, "No Response", Toast.LENGTH_SHORT).show();
                    ProgressUtils.cancelLoading();
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                ProgressUtils.cancelLoading();
                Toast.makeText(KoyelActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });

    }

}