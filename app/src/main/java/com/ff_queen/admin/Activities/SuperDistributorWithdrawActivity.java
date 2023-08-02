package com.ff_queen.admin.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.DividerItemDecoration;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.ff_queen.admin.Adapter.WithdrawAllRequestAdapter;
import com.ff_queen.admin.Interfaces.MyInterface;
import com.ff_queen.admin.Models.User;
import com.ff_queen.admin.Models.Withdraw_All_Request_Model;
import com.ff_queen.admin.Utilities.ApiClient;
import com.ff_queen.admin.Utilities.ProgressUtils;
import com.ff_queen.admin.ViewModel.ApiResponse;
import com.ff_queen.admin.databinding.ActivitySuperDistributorWithdrawBinding;
import com.ff_queen.admin.databinding.ActivityUserMoneyWithdrawBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SuperDistributorWithdrawActivity extends AppCompatActivity {

    private ActivitySuperDistributorWithdrawBinding binding;
    private ApiResponse apiResponse;
    MyInterface myInterface;
    private User user;
    private List<Withdraw_All_Request_Model> withdraw_request_models = new ArrayList<>();
    private WithdrawAllRequestAdapter adapter;
    private static final String type="Super Distributor";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySuperDistributorWithdrawBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);

        initView();

    }

    private void initView() {

        user = new User(this);

        getSupportActionBar().setTitle("Withdraw Request");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        myInterface = ApiClient.getApiClient().create(MyInterface.class);
        binding.contentSuperDistributorWithdraw.rvMoneyRequest.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));




        fetchAllWithdrawRequest();
     /*   apiResponse.getAllWithdrawRequest().observe(this, new Observer<List<Withdraw_All_Request_Model>>() {
            @Override
            public void onChanged(List<Withdraw_All_Request_Model> money_request_models) {

                withdraw_request_models = money_request_models;

                adapter.updateList(withdraw_request_models);
            }
        });*/

    }

    private void fetchAllWithdrawRequest() {
        Call<String> call = myInterface.fetch_all_withdraw_req();
        ProgressUtils.showLoadingDialog(SuperDistributorWithdrawActivity.this);
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
                            Toast.makeText(SuperDistributorWithdrawActivity.this, "No Data found", Toast.LENGTH_SHORT).show();
                        } else {
                            binding.contentSuperDistributorWithdraw.rvMoneyRequest.setVisibility(View.VISIBLE);

                            withdraw_request_models.clear();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                withdraw_request_models.add(new Withdraw_All_Request_Model(
                                        jsonObject.getString("id"),
                                        jsonObject.getString("user_id"),
                                        jsonObject.getString("name"),
                                        jsonObject.getString("new_amount")+".00",
                                        jsonObject.getString("date")
                                ));
                            }
                            adapter = new WithdrawAllRequestAdapter(SuperDistributorWithdrawActivity.this,withdraw_request_models, type);
                            binding.contentSuperDistributorWithdraw.rvMoneyRequest.setAdapter(adapter);
                            ProgressUtils.cancelLoading();

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        ProgressUtils.cancelLoading();
                        Toast.makeText(SuperDistributorWithdrawActivity.this, ""+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(SuperDistributorWithdrawActivity.this, "No Response", Toast.LENGTH_SHORT).show();
                    ProgressUtils.cancelLoading();
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                ProgressUtils.cancelLoading();
                Toast.makeText(SuperDistributorWithdrawActivity.this, "Error", Toast.LENGTH_SHORT).show();
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

        finish();
    }
}