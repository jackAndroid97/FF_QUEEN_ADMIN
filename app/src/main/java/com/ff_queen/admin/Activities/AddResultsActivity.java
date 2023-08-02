package com.ff_queen.admin.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ff_queen.admin.Models.G_T_Model;
import com.ff_queen.admin.Models.Game_Model;
import com.google.android.material.textfield.TextInputLayout;
import com.ff_queen.admin.Interfaces.MyInterface;
import com.ff_queen.admin.Models.Category;
import com.ff_queen.admin.Models.Spin_Wining_No_Model;
import com.ff_queen.admin.Models.Timing_Model;
import com.ff_queen.admin.R;
import com.ff_queen.admin.Utilities.ApiClient;
import com.ff_queen.admin.Utilities.ProgressUtils;
import com.weiwangcn.betterspinner.library.BetterSpinner;

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

public class AddResultsActivity extends AppCompatActivity {

    private BetterSpinner categorySpinner, wining_no_spinner,game_spinner;
    private BetterSpinner slotSpinner;
    private TextView dateTextView;
    private TextView selectDateBtn;
    private EditText winningNumberEditTxt;
    private TextView addResultBtn;
    private MyInterface myInterface;
    private List<Category> category_list = new ArrayList<>();
    private String[] sample = {"NO DATA"};
    private String categoryID = "";
    private String slotID = "";
    private String baji = "";
    private String game_id, game_name;
    private List<G_T_Model> timing_model_list = new ArrayList<>();
    private List<Spin_Wining_No_Model> spin_wining_no_models_list = new ArrayList<>();
    private String winningNumber = "";
    int start_month, start_year, start_day;
    private String start_date = "";
    private String selectedCategory = "";
    private String selectedSlot = "";
    private String result_id;
    private String game_time_id;
    private String currentDate;
    private TextInputLayout til_winning_no;
    List<Game_Model> game_models = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        categorySpinner = findViewById(R.id.category_spinner);
        slotSpinner = findViewById(R.id.slot_spinner);
        dateTextView = findViewById(R.id.date_view);
        selectDateBtn = findViewById(R.id.select_date_btn);
        winningNumberEditTxt = findViewById(R.id.winning_number_edtTxt);
        addResultBtn = findViewById(R.id.add_result_btn);
        wining_no_spinner = findViewById(R.id.wining_no_spinner);
        til_winning_no = findViewById(R.id.til_winning_no);
        game_spinner = findViewById(R.id.game_spinner);

        myInterface = ApiClient.getApiClient().create(MyInterface.class);

        game_id = getIntent().getStringExtra("game_id");
        game_name = getIntent().getStringExtra("game_name");


        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        currentDate = formatter.format(date);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setTitle(game_name);

        result_id = getIntent().getStringExtra("result_id");
        game_time_id = getIntent().getStringExtra("game_time_id");



        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, sample);

        categorySpinner.setAdapter(adapter);
        slotSpinner.setAdapter(adapter);
        wining_no_spinner.setAdapter(adapter);
        game_spinner.setAdapter(adapter);

       // fetchGame();
        fetchCategory();
        fetchGameTimings();

        categorySpinner.setOnItemClickListener((parent, view, position, id) -> {
            Category category = (Category) parent.getItemAtPosition(position);
            categoryID = category.id;
            selectedCategory = category.category;

         /*   if (selectedCategory.equals("SINGLE")) {

                wining_no_spinner.setVisibility(View.GONE);
                til_winning_no.setVisibility(View.VISIBLE);
                int maxLength = 1;
                winningNumberEditTxt.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});

            } else {

                wining_no_spinner.setVisibility(View.VISIBLE);
                til_winning_no.setVisibility(View.GONE);
                fetchFatafatWinningNo();
                *//*int maxLength = 3;
                winningNumberEditTxt.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});*//*
            }*/
        });

      /*  game_spinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Game_Model game_model = (Game_Model) adapterView.getItemAtPosition(i);
               String g_id = game_model.getId();

            }
        });*/


      /*  wining_no_spinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Spin_Wining_No_Model model = (Spin_Wining_No_Model) adapterView.getItemAtPosition(i);

                winningNumber = model.getNo();
            }
        });
*/

        slotSpinner.setOnItemClickListener((parent, view, position, id) -> {
            G_T_Model timing_model = (G_T_Model) parent.getItemAtPosition(position);
            slotID = timing_model.getId();
            baji = timing_model.getCount();
            //selectedSlot = timing_model.baji;

        });

//        winningNumber = winningNumberEditTxt.getText().toString();

        selectDateBtn.setOnClickListener(view -> {
            // Get Current Date
            final Calendar c = Calendar.getInstance();
            int mYear = c.get(Calendar.YEAR);
            int mMonth = c.get(Calendar.MONTH);
            int mDay = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(AddResultsActivity.this,
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
                        dateTextView.setText(year + "-" + month + "-" + day);
                        start_date = year + "-" + month + "-" + day;

                    }, mYear, mMonth, mDay);
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
            datePickerDialog.show();
        });

        addResultBtn.setOnClickListener(v -> {

           /* if (winningNumber.equals(""))
            {
                winningNumber = winningNumberEditTxt.getText().toString();
            }
*/

            if (slotID.equals("")) {
                Toast.makeText(this, "Select a slot.", Toast.LENGTH_SHORT).show();
            } else if (categoryID.equals("")) {
                Toast.makeText(this, "Select a category.", Toast.LENGTH_SHORT).show();
            }
            else if (categoryID.equals("")) {
                Toast.makeText(this, "Select a category.", Toast.LENGTH_SHORT).show();
            } else if (dateTextView.getText().toString().equals("Please select a date") || dateTextView.getText().toString().isEmpty() || dateTextView.getText().toString() == null) {
                Toast.makeText(this, "Select a date.", Toast.LENGTH_SHORT).show();
            } else if (winningNumberEditTxt.getText().toString().equals("")) {
                Toast.makeText(this, "Enter a winning number", Toast.LENGTH_SHORT).show();
            }
            else if(selectedCategory.equals("JODI") && Integer.parseInt(baji)%2!=0){
                Toast.makeText(this, "Jodi not available for this baji", Toast.LENGTH_SHORT).show();
            }
            else {
                addResult();
            }
        });
    }

    private void addResult() {
        String resultDate = dateTextView.getText().toString();
        Call<String> call = myInterface.add_result(game_id, slotID, resultDate, categoryID, winningNumberEditTxt.getText().toString().trim(),baji);
        ProgressUtils.showLoadingDialog(AddResultsActivity.this);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String res = response.body();
                if (res == null) {
                    Toast.makeText(AddResultsActivity.this, "No data found.", Toast.LENGTH_SHORT).show();
                    ProgressUtils.cancelLoading();
                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(res);
                        if (jsonObject.getString("rec").equals("0")) {
                            Toast.makeText(AddResultsActivity.this, "Couldn't add result.", Toast.LENGTH_SHORT).show();
                        } else if (jsonObject.getString("rec").equals("1")) {
                            Toast.makeText(AddResultsActivity.this, "Successfully Added Result!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(AddResultsActivity.this, MainActivity.class));
                            finish();
                        } else {
                            ProgressUtils.cancelLoading();
                            Toast.makeText(AddResultsActivity.this, "Check details", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        ProgressUtils.cancelLoading();
                        Toast.makeText(AddResultsActivity.this, "Something went wrong :(" + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                ProgressUtils.cancelLoading();
                Toast.makeText(AddResultsActivity.this, "Something went wrong :(", Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }


    private void fetchCategory() {
        Call<String> call = myInterface.fetch_category();
        ProgressUtils.showLoadingDialog(AddResultsActivity.this);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.body() != null) {
                    String res = response.body();
                    try {
                        JSONArray jsonArray = new JSONArray(res);
                        if (jsonArray.length() == 0) {
                            ProgressUtils.cancelLoading();
                            Toast.makeText(AddResultsActivity.this, "No Data found", Toast.LENGTH_SHORT).show();
                        } else {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                category_list.add(new Category(jsonObject.getString("id"), jsonObject.getString("category")));

                                ArrayAdapter<Category> categoryAdapter = new ArrayAdapter<>(AddResultsActivity.this,
                                        android.R.layout.simple_dropdown_item_1line, category_list);


                                categorySpinner.setAdapter(categoryAdapter);
                            }
                            ProgressUtils.cancelLoading();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        ProgressUtils.cancelLoading();
                        Toast.makeText(AddResultsActivity.this, "" + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(AddResultsActivity.this, "No Response", Toast.LENGTH_SHORT).show();
                    ProgressUtils.cancelLoading();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                ProgressUtils.cancelLoading();
                Toast.makeText(AddResultsActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }


    /*private void fetchFatafatWinningNo() {


        Call<String> call = myInterface.fetch_fatafat_patti_no();
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String res = response.body();
                if (res == null) {
                    Toast.makeText(AddResultsActivity.this, "Error", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONArray jsonArray = new JSONArray(res);
                        if (jsonArray.length() == 0) {
                            Toast.makeText(AddResultsActivity.this, "Not found.", Toast.LENGTH_SHORT).show();
                            ProgressUtils.cancelLoading();

                        } else {
                            spin_wining_no_models_list.clear();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                spin_wining_no_models_list.add(new Spin_Wining_No_Model(jsonObject.getString("id"),
                                        jsonObject.getString("no")
                                ));
                                ArrayAdapter<Spin_Wining_No_Model> adapter = new ArrayAdapter<Spin_Wining_No_Model>(AddResultsActivity.this,
                                        android.R.layout.simple_dropdown_item_1line, spin_wining_no_models_list);

                                wining_no_spinner.setAdapter(adapter);
                                ProgressUtils.cancelLoading();
                            }
                        }
                    } catch (JSONException e) {
                        Toast.makeText(AddResultsActivity.this, "Something went wrong.", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                        ProgressUtils.cancelLoading();
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(AddResultsActivity.this, "Something wnet wrong.", Toast.LENGTH_SHORT).show();
                ProgressUtils.cancelLoading();
            }
        });
    }*/


    private void fetchGameTimings() {
        Call<String> call = myInterface.fetch_time(game_id);
       // Toast.makeText(this, "" + game_id, Toast.LENGTH_SHORT).show();
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String res = response.body();
                if (res == null) {
                    Toast.makeText(AddResultsActivity.this, "Error", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONArray jsonArray = new JSONArray(res);
                        if (jsonArray.length() == 0) {
                            Toast.makeText(AddResultsActivity.this, "Not timings found.", Toast.LENGTH_SHORT).show();
                            ProgressUtils.cancelLoading();

                        } else {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                timing_model_list.add(new G_T_Model(jsonObject.getString("id"),
                                        jsonObject.getString("start_time"),
                                        jsonObject.getString("count")

                                ));
                                ArrayAdapter<G_T_Model> adapter = new ArrayAdapter<G_T_Model>(AddResultsActivity.this,
                                        android.R.layout.simple_dropdown_item_1line, timing_model_list);

                                slotSpinner.setAdapter(adapter);
                                ProgressUtils.cancelLoading();
                            }
                        }
                    } catch (JSONException e) {
                        Toast.makeText(AddResultsActivity.this, "Something went wrong."+ e.getMessage(), Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                        ProgressUtils.cancelLoading();
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(AddResultsActivity.this, "Something wnet wrong.", Toast.LENGTH_SHORT).show();
                ProgressUtils.cancelLoading();
            }
        });
    }

    private void fetchGame() {

        Call<String> call = myInterface.fetch_active_game();
        ProgressUtils.showLoadingDialog(AddResultsActivity.this);
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
                                String game_image = jsonObject.getString("image");
                                Game_Model model = new Game_Model(game_id,game_name,game_image);
                                game_models.add(model);
                            }
                            ArrayAdapter<Game_Model> gameAdapter = new ArrayAdapter<>(AddResultsActivity.this,
                                    android.R.layout.simple_dropdown_item_1line, game_models);
                            game_spinner.setAdapter(gameAdapter);
                            ProgressUtils.cancelLoading();

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        ProgressUtils.cancelLoading();
                        Toast.makeText(AddResultsActivity.this, "" + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(AddResultsActivity.this, "No Response", Toast.LENGTH_SHORT).show();
                    ProgressUtils.cancelLoading();
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                ProgressUtils.cancelLoading();
                Toast.makeText(AddResultsActivity.this, "Error", Toast.LENGTH_SHORT).show();
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
        super.onBackPressed();
        finish();
    }
}
