package com.ff_queen.admin.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.ff_queen.admin.Adapter.UserAdapter;
import com.ff_queen.admin.Interfaces.MyInterface;
import com.ff_queen.admin.Models.User_Model;
import com.ff_queen.admin.R;
import com.ff_queen.admin.Utilities.ApiClient;
import com.ff_queen.admin.Utilities.ProgressUtils;
import com.ff_queen.admin.databinding.ActivityUserBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserActivity extends AppCompatActivity {

    private ActivityUserBinding binding;
    private MyInterface myInterface;
    private ArrayList<User_Model> user_models = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources()
                .getColor(R.color.primary_dark)));
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("User");

        myInterface = ApiClient.getApiClient().create(MyInterface.class);

        fetchUser();

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


    private void fetchUser()
    {
        Call<String> call = myInterface.fetch_all_user();
        ProgressUtils.showLoadingDialog(UserActivity.this);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                String res = response.body();

                if (res == null)
                {
                    Toast.makeText(UserActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    ProgressUtils.cancelLoading();
                }
                else
                {

                    try {
                        JSONArray jsonArray = new JSONArray(res);

                        if (jsonArray.length() ==0)
                        {
                            Toast.makeText(UserActivity.this, "No User", Toast.LENGTH_SHORT).show();
                            ProgressUtils.cancelLoading();
                        }
                        else
                        {
                            user_models.clear();
                            for (int i=0; i<jsonArray.length(); i++)
                            {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                String name = jsonObject.getString("name");
                                String mobile = jsonObject.getString("mobile");
                                String email = jsonObject.getString("email");

                                user_models.add(new User_Model(name,email,mobile));
                            }

                            binding.contentUser.rvUser.setAdapter(new UserAdapter(UserActivity.this,user_models));
                            ProgressUtils.cancelLoading();

                        }

                    } catch (JSONException e) {

                        Toast.makeText(UserActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                        ProgressUtils.cancelLoading();
                    }

                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

                Toast.makeText(UserActivity.this, "Slow Network", Toast.LENGTH_SHORT).show();
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