package com.ff_queen.admin.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.DividerItemDecoration;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.ff_queen.admin.Adapter.MoneyRequestAdapter;
import com.ff_queen.admin.Adapter.WithdrawAllRequestAdapter;
import com.ff_queen.admin.Interfaces.MyInterface;
import com.ff_queen.admin.Models.Money_Request_Model;
import com.ff_queen.admin.Models.User;
import com.ff_queen.admin.Models.Withdraw_All_Request_Model;
import com.ff_queen.admin.R;
import com.ff_queen.admin.Utilities.ApiClient;
import com.ff_queen.admin.Utilities.ProgressUtils;
import com.ff_queen.admin.ViewModel.ApiResponse;
import com.ff_queen.admin.databinding.ActivityMoneyRequestBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MoneyRequestActivity extends AppCompatActivity {

    private ActivityMoneyRequestBinding binding;
    private ApiResponse apiResponse;
    private User user;
    private List<Money_Request_Model> request_models = new ArrayList<>();
    private MoneyRequestAdapter adapter;
    MyInterface myInterface;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMoneyRequestBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);

        initView();
    }

    private void initView() {

        myInterface = ApiClient.getApiClient().create(MyInterface.class);
        user = new User(this);

        getSupportActionBar().setTitle("View Money Request");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        binding.contentMoneyRequest.rvMoneyRequest.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));

        adapter = new MoneyRequestAdapter(this,request_models);
        binding.contentMoneyRequest.rvMoneyRequest.setAdapter(adapter);


       // apiResponse.fetchMoneyRequest();
        fetch_money_request();


    }

    private void fetch_money_request() {
        Call<String> call = myInterface.fetch_money_request();
        ProgressUtils.showLoadingDialog(MoneyRequestActivity.this);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.body()!=null){
                    String res = response.body();
                    try {
                        JSONArray jsonArray = new JSONArray(res);
                        if (jsonArray.length() == 0) {
                            ProgressUtils.cancelLoading();
                            // withdraw_request_models.clear();
                            Toast.makeText(MoneyRequestActivity.this, "No Data found", Toast.LENGTH_SHORT).show();
                        } else {
                            binding.contentMoneyRequest.rvMoneyRequest.setVisibility(View.VISIBLE);

                            request_models.clear();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                request_models.add(new Money_Request_Model(
                                        jsonObject.getString("id"),
                                        jsonObject.getString("user_id"),
                                        jsonObject.getString("name"),
                                        jsonObject.getString("amount")+".00",
                                        jsonObject.getString("date"),
                                        jsonObject.getString("bank_name"),
                                        jsonObject.getString("account_no"),
                                        jsonObject.getString("ifsc_code"),
                                        jsonObject.getString("mobile"),
                                        jsonObject.getString("wallet"),
                                        jsonObject.getString("time")
                                ));
                            }
                            adapter = new MoneyRequestAdapter(MoneyRequestActivity.this,request_models);
                            binding.contentMoneyRequest.rvMoneyRequest.setAdapter(adapter);
                            ProgressUtils.cancelLoading();

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        ProgressUtils.cancelLoading();
                        Toast.makeText(MoneyRequestActivity.this, ""+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(MoneyRequestActivity.this, "No Response", Toast.LENGTH_SHORT).show();
                    ProgressUtils.cancelLoading();
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                ProgressUtils.cancelLoading();
                Toast.makeText(MoneyRequestActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                startActivity(new Intent(MoneyRequestActivity.this,MainActivity.class));
                finishAffinity();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        startActivity(new Intent(MoneyRequestActivity.this,MainActivity.class));
        finishAffinity();
    }

}