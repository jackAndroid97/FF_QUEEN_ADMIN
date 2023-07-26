package com.ff_queen.admin.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.ff_queen.admin.Interfaces.MyInterface;
import com.ff_queen.admin.Models.Commission_Model;
import com.ff_queen.admin.R;
import com.ff_queen.admin.Utilities.ApiClient;
import com.ff_queen.admin.ViewModel.ApiResponse;
import com.ff_queen.admin.databinding.ActivityCommissionBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommissionActivity extends AppCompatActivity {

    private ActivityCommissionBinding binding;
    private List<Commission_Model> commission_models = new ArrayList<>();
    private ApiResponse apiResponse;
    private String type="";
    private MyInterface myInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCommissionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);

        initView();
    }


    private void initView() {

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Commission");

        apiResponse = new ApiResponse(this);
        myInterface = ApiClient.getApiClient().create(MyInterface.class);

        type = getIntent().getStringExtra("type");

        getSupportActionBar().setTitle("Commission");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        fetchCommission();

        binding.contentCommission.btnEditSDis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialogEdit("Super Distributor");
            }
        });

        binding.contentCommission.btnEditDis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialogEdit("Distributor");
            }
        });


        binding.contentCommission.btnEditRetailer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialogEdit("Retailer");
            }
        });

    }


    public void fetchCommission()
    {
        Call<String> call = myInterface.fetch_commission(type);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                if (response.isSuccessful() && response.body() !=null)
                {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body());

                        if (jsonObject.getString("rec").equals("0"))
                        {
                            Toast.makeText(CommissionActivity.this, "Commisssion not found", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            String super_distributor = jsonObject.getString("super_distributor");
                            String distributor = jsonObject.getString("distributor");
                            String retailer = jsonObject.getString("retailer");

                            binding.contentCommission.txtSDistributorAmount.setText("Rs "+super_distributor+" /-");
                            binding.contentCommission.txtDistributorAmount.setText("Rs "+distributor+" /-");
                            binding.contentCommission.txtRetailerAmount.setText("Rs "+retailer+" /-");
                        }

                    } catch (JSONException e) {

                        Toast.makeText(CommissionActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }


                }
                else
                {
                    Toast.makeText(CommissionActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

                Toast.makeText(CommissionActivity.this, "Slow Network", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void dialogEdit(String user_type)
    {

        final Dialog dialog = new Dialog(CommissionActivity.this);
        dialog.setContentView(R.layout.dialog_commission);
        TextView btn_submit = dialog.findViewById(R.id.btn_submit);
        TextInputEditText amount = dialog.findViewById(R.id.amount);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int displayWidth = displayMetrics.widthPixels;
        int displayHeight = displayMetrics.heightPixels;
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(dialog.getWindow().getAttributes());
        int dialogWindowWidth = WindowManager.LayoutParams.MATCH_PARENT;
        int dialogWindowHeight = (int) (displayHeight * 0.3f);
        layoutParams.width = dialogWindowWidth;
        layoutParams.height = dialogWindowHeight;
        dialog.getWindow().setAttributes(layoutParams);

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (amount.getText().toString().isEmpty())
                {
                    Toast.makeText(CommissionActivity.this, "Enter Aamount", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    apiResponse.editCommission(amount.getText().toString(), type,user_type, dialog);
                }

            }
        });
        dialog.show();

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