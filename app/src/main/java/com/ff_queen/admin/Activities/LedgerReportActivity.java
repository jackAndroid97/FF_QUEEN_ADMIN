package com.ff_queen.admin.Activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.ff_queen.admin.Interfaces.MyInterface;
import com.ff_queen.admin.R;
import com.ff_queen.admin.Utilities.ApiClient;
import com.ff_queen.admin.Utilities.ProgressUtils;
import com.ff_queen.admin.ViewModel.ApiResponse;
import com.ff_queen.admin.databinding.ActivityAddGameBinding;
import com.ff_queen.admin.databinding.ActivityLedgerReportBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LedgerReportActivity extends AppCompatActivity {

    public static ActivityLedgerReportBinding binding;
    private ApiResponse apiResponse;
    private MyInterface myInterface;
    private final int MY_PERMISSIONS_REQUEST_USE_CAMERA = 0x00AF;
    int start_month, start_year, start_day;
    private String start_date = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLedgerReportBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        myInterface = ApiClient.getApiClient().create(MyInterface.class);
        getSupportActionBar().setTitle("Ledger Report");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        total_user("");
        binding.contentLedgerReport.aMoneyView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LedgerReportActivity.this,MoneyRequestActivity.class));
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
            }
        });
        binding.contentLedgerReport.wMoneyView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LedgerReportActivity.this,SuperDistributorWithdrawActivity.class));
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
            }
        });
        binding.contentLedgerReport.gPlayView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LedgerReportActivity.this,BidHistoryActivity.class));
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
            }
        });
        binding.contentLedgerReport.textDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(LedgerReportActivity.this,
                        (view1, year, monthOfYear, dayOfMonth) -> {
                            String month, day;
                            monthOfYear = (monthOfYear + 1);
                            if (monthOfYear < 10) {

                                month = "0" + monthOfYear;
                            } else {
                                month = String.valueOf(monthOfYear);
                            }
                            if (dayOfMonth < 10) {

                                day = "0" + dayOfMonth;
                            } else {
                                day = String.valueOf(dayOfMonth);
                            }
                            start_year = year;
                            start_day = dayOfMonth + 1;
                            start_month = monthOfYear - 1;
                            binding.contentLedgerReport.textDate.setText(day + "-" + month + "-" + year);
                            start_date = year + "-" + month + "-" + day;

                        }, mYear, mMonth, mDay);
              //  datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
                datePickerDialog.show();
            }
        });
        binding.contentLedgerReport.buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                total_user(start_date);
            }
        });
    }


    private void total_user(String date) {

        Call<String> call = myInterface.ledger_reports(date);
        ProgressUtils.showLoadingDialog(LedgerReportActivity.this);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String res = response.body();
                if (res == null){
                    Toast.makeText(LedgerReportActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    ProgressUtils.cancelLoading();
                }
                else {
                    try {
                        JSONObject jsonObject = new JSONObject(res);
                        if (jsonObject.getString("rec").equals("0")) {
                            binding.contentLedgerReport.aMoney.setText("₹0");
                            binding.contentLedgerReport.wMoney.setText("₹0");
                            binding.contentLedgerReport.gPlay.setText("₹0");
                            binding.contentLedgerReport.tWinAmount.setText("₹0");

                            ProgressUtils.cancelLoading();
                        } else {
                            binding.contentLedgerReport.aMoney.setText("Total Add Money: ₹"+jsonObject.getString("total_add_money"));
                            binding.contentLedgerReport.wMoney.setText("Total Withdraw Money: ₹"+jsonObject.getString("total_withdraw_amount"));
                            binding.contentLedgerReport.gPlay.setText("Total Gameplay Amount: ₹"+jsonObject.getString("total_gameplay_amount"));
                            binding.contentLedgerReport.tWinAmount.setText("Total Win Amount: ₹"+jsonObject.getString("total_win_amount"));
                            ProgressUtils.cancelLoading();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        ProgressUtils.cancelLoading();
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(LedgerReportActivity.this, "Something went wrong.", Toast.LENGTH_SHORT).show();
                ProgressUtils.cancelLoading();
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