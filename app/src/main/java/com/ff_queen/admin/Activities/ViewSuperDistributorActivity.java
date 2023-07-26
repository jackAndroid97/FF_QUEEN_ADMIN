package com.ff_queen.admin.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import android.os.Bundle;
import android.view.MenuItem;

import com.ff_queen.admin.Adapter.ViewSuperDistributorAdpater;
import com.ff_queen.admin.Models.User;
import com.ff_queen.admin.Models.View_User_Model;
import com.ff_queen.admin.R;
import com.ff_queen.admin.ViewModel.ApiResponse;
import com.ff_queen.admin.databinding.ActivityViewSuperDistributorBinding;

import java.util.ArrayList;
import java.util.List;

public class ViewSuperDistributorActivity extends AppCompatActivity {

    private ActivityViewSuperDistributorBinding binding;
    private List<View_User_Model> user_models = new ArrayList<>();
    private ApiResponse apiResponse;
    private ViewSuperDistributorAdpater adpater;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding =ActivityViewSuperDistributorBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);

        initView();
    }

    private void initView() {

        apiResponse = new ApiResponse(this);
        user = new User(this);

        //getSupportActionBar().setTitle(game_name);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        adpater = new ViewSuperDistributorAdpater(this,user_models);
        binding.contentViewSuperDistributor.rvUser.setAdapter(adpater);

        apiResponse.fetchSuperDistributor();

        apiResponse.getSuperDistributor().observe(this, new Observer<List<View_User_Model>>() {
            @Override
            public void onChanged(List<View_User_Model> view_user_models) {

                user_models = view_user_models;

                adpater.updateUserList(user_models);

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