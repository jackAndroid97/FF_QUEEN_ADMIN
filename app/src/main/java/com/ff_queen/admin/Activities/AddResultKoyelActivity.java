package com.ff_queen.admin.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.ff_queen.admin.Interfaces.MyInterface;
import com.ff_queen.admin.Models.Timing_Model;
import com.ff_queen.admin.R;
import com.ff_queen.admin.Utilities.ApiClient;
import com.ff_queen.admin.Utilities.ProgressUtils;
import com.ff_queen.admin.databinding.ActivityAddResultKoyelBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddResultKoyelActivity extends AppCompatActivity {

    private ActivityAddResultKoyelBinding binding;
    private MyInterface myInterface;
    private String currentDate;
    private String slotID = "", selectedSlot = "", start_date = "", game_id = "", game_name = "";
    int start_month, start_year, start_day;
    private List<Timing_Model> timing_model_list = new ArrayList<>();
    private String[] sample = {""};
    private String last_digits = "", last_two_digits = "", middle_digits = "", cat_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddResultKoyelBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Add Result");
        myInterface = ApiClient.getApiClient().create(MyInterface.class);

        game_id = getIntent().getStringExtra("game_id");
        game_name = getIntent().getStringExtra("game_name");
        cat_id = getIntent().getStringExtra("cat_id");

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        currentDate = formatter.format(date);

        binding.contentAddResultKoyel.slotSpinner.setOnItemClickListener((parent, view, position, id) -> {
            Timing_Model timing_model = (Timing_Model) parent.getItemAtPosition(position);

            slotID = timing_model.id;
            selectedSlot = timing_model.baji;

        });

        if (cat_id.equals("3")) {
            binding.contentAddResultKoyel.winningNumberEdtTxt.setFilters(new InputFilter[]{new InputFilter.LengthFilter(2)});
        } else {
            binding.contentAddResultKoyel.winningNumberEdtTxt.setFilters(new InputFilter[]{new InputFilter.LengthFilter(1)});
        }


        binding.contentAddResultKoyel.selectDateBtn.setOnClickListener(view -> {
            // Get Current Date
            final Calendar c = Calendar.getInstance();
            int mYear = c.get(Calendar.YEAR);
            int mMonth = c.get(Calendar.MONTH);
            int mDay = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(AddResultKoyelActivity.this,
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
                        binding.contentAddResultKoyel.dateView.setText(year + "-" + month + "-" + day);
                        start_date = year + "-" + month + "-" + day;

                    }, mYear, mMonth, mDay);
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
            datePickerDialog.show();
        });


        binding.contentAddResultKoyel.addResultBtn.setOnClickListener(v -> {

            if (slotID.equals("")) {
                Toast.makeText(this, "Select a slot.", Toast.LENGTH_SHORT).show();
            } else if (binding.contentAddResultKoyel.dateView.getText().toString().equals("Please select a date") || binding.contentAddResultKoyel.dateView.getText().toString().isEmpty() || binding.contentAddResultKoyel.dateView.getText().toString() == null) {
                Toast.makeText(this, "Select a date.", Toast.LENGTH_SHORT).show();
            } else if (binding.contentAddResultKoyel.winningNumberEdtTxt.getText().toString().isEmpty()) {
                Toast.makeText(this, "Enter Number", Toast.LENGTH_SHORT).show();
            } else {
                if (cat_id.equals("3")) {
                    if (binding.contentAddResultKoyel.winningNumberEdtTxt.length() != 2) {
                        Toast.makeText(this, "Enter Two Digit Number", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        addResult();
                    }
                }
                else
                {
                    addResult();
                }


            }


        });


        fetchGameTimings();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, sample);

        binding.contentAddResultKoyel.slotSpinner.setAdapter(adapter);

    }


    private void addResult() {
        String resultDate = binding.contentAddResultKoyel.dateView.getText().toString();
        Call<String> call = null;
        if (game_id.equals("18")) {
            call = myInterface.add_koyel_result(game_id, slotID, resultDate, binding.contentAddResultKoyel.winningNumberEdtTxt.getText().toString(),cat_id);
        } else if (game_id.equals("19")) {
            call = myInterface.add_koyel_result(game_id, slotID, resultDate, binding.contentAddResultKoyel.winningNumberEdtTxt.getText().toString(),cat_id);
        } else if (game_id.equals("20")) {
            call = myInterface.add_koyel_result(game_id, slotID, resultDate, binding.contentAddResultKoyel.winningNumberEdtTxt.getText().toString(),cat_id);
        }


        ProgressUtils.showLoadingDialog(AddResultKoyelActivity.this);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String res = response.body();
                if (res == null) {
                    Toast.makeText(AddResultKoyelActivity.this, "No data found.", Toast.LENGTH_SHORT).show();
                    ProgressUtils.cancelLoading();
                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(res);
                        if (jsonObject.getString("rec").equals("0")) {
                            Toast.makeText(AddResultKoyelActivity.this, "Couldn't add result.", Toast.LENGTH_SHORT).show();
                            ProgressUtils.cancelLoading();
                        } else if (jsonObject.getString("rec").equals("1")) {
                            ProgressUtils.cancelLoading();
                            Toast.makeText(AddResultKoyelActivity.this, "Successfully Added Result!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(AddResultKoyelActivity.this, MainActivity.class));
                            finish();
                        } else if (jsonObject.getString("rec").equals("2")) {
                            Toast.makeText(AddResultKoyelActivity.this, "Result Already Uploaded", Toast.LENGTH_SHORT).show();
                            ProgressUtils.cancelLoading();
                        } else {
                            ProgressUtils.cancelLoading();
                            Toast.makeText(AddResultKoyelActivity.this, "Check details", Toast.LENGTH_SHORT).show();
                            ProgressUtils.cancelLoading();
                        }
                    } catch (JSONException e) {
                        ProgressUtils.cancelLoading();
                        Toast.makeText(AddResultKoyelActivity.this, "Something went wrong :(" + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                        ProgressUtils.cancelLoading();
                    }

                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                ProgressUtils.cancelLoading();
                Toast.makeText(AddResultKoyelActivity.this, "Something went wrong :(", Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }


    private void fetchGameTimings() {
        Call<String> call = null;
        if (game_id.equals("18")) {
            call = myInterface.koyel_game_timing(game_id, "", "");
        } else if (game_id.equals("19")) {
            call = myInterface.koyel_game_timing(game_id, "", "");
        } else if (game_id.equals("20")) {
            call = myInterface.koyel_game_timing(game_id, "", "");
        }

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String res = response.body();
                if (res == null) {
                    Toast.makeText(AddResultKoyelActivity.this, "Error", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONArray jsonArray = new JSONArray(res);
                        if (jsonArray.length() == 0) {
                            Toast.makeText(AddResultKoyelActivity.this, "Not timings found.", Toast.LENGTH_SHORT).show();
                            ProgressUtils.cancelLoading();

                        } else {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                timing_model_list.add(new Timing_Model(jsonObject.getString("id"),
                                        jsonObject.getString("now_time"),
                                        jsonObject.getString("status"),
                                        jsonObject.getString("game_time"),
                                        "",
                                        jsonObject.getString("count"),
                                        jsonObject.getString("date_status"),
                                        "",
                                       ""
                                ));
                                ArrayAdapter<Timing_Model> adapter = new ArrayAdapter<Timing_Model>(AddResultKoyelActivity.this,
                                        android.R.layout.simple_dropdown_item_1line, timing_model_list);

                                binding.contentAddResultKoyel.slotSpinner.setAdapter(adapter);
                                ProgressUtils.cancelLoading();
                            }
                        }
                    } catch (JSONException e) {
                        Toast.makeText(AddResultKoyelActivity.this, "Something went wrong.", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                        ProgressUtils.cancelLoading();
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(AddResultKoyelActivity.this, "Something wnet wrong.", Toast.LENGTH_SHORT).show();
                ProgressUtils.cancelLoading();
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