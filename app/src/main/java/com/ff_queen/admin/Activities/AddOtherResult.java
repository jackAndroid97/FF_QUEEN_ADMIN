package com.ff_queen.admin.Activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.ff_queen.admin.Interfaces.MyInterface;
import com.ff_queen.admin.Models.Category;
import com.ff_queen.admin.Models.Spin_Wining_No_Model;
import com.ff_queen.admin.Models.Timing_Model;
import com.ff_queen.admin.R;
import com.ff_queen.admin.Utilities.ApiClient;
import com.ff_queen.admin.Utilities.ProgressUtils;
import com.ff_queen.admin.databinding.ActivityAddOtherResultBinding;
import com.weiwangcn.betterspinner.library.BetterSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddOtherResult extends AppCompatActivity {

    private BetterSpinner slotSpinner, wining_no_spinner, til_winning_no;
    private TextView dateTextView;
    private TextView selectDateBtn;
    private EditText winningNumberEditTxt;
    private TextView addResultBtn;
    private MyInterface myInterface;
    private List<Category> category_list = new ArrayList<>();
    private String[] sample = {"NO DATA"};
    private String[] winning_no = {"Black","White","Red"};
    private String[] winning_no_thunderball = {"0","1","2","3","4","5","6","7","8","9"};
    private String slotID = "", win_no="";
    private String game_id, game_name;
    private List<Timing_Model> timing_model_list = new ArrayList<>();
    private List<Spin_Wining_No_Model> spin_wining_no_models_list = new ArrayList<>();
    private String winningNumber = "";
    int start_month, start_year, start_day;
    private String start_date = "";
    private String selectedCategory = "";
    private String selectedSlot = "";
    private String result_id;
    private String  spin_number="";
    private String currentDate;
    private ActivityAddOtherResultBinding binding;

    private final int min_two_six = 2;
    private final int max_two_six = 6;

    private final int min_eight_twelve = 8;
    private final int max_eight_twelve = 12;

    private int random_two_six, random_eight_twelve;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAddOtherResultBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        slotSpinner = findViewById(R.id.slot_spinner);
        wining_no_spinner = findViewById(R.id.wining_no_spinner);
        dateTextView = findViewById(R.id.date_view);
        selectDateBtn = findViewById(R.id.select_date_btn);
        winningNumberEditTxt = findViewById(R.id.winning_number_edtTxt);
        addResultBtn = findViewById(R.id.add_result_btn);
        til_winning_no = findViewById(R.id.til_winning_no);

        myInterface = ApiClient.getApiClient().create(MyInterface.class);

        game_id = getIntent().getStringExtra("game_id");
        game_name = getIntent().getStringExtra("game_name");
//        if(getIntent().getStringExtra("type").equals("Edit")){
//            result_id=getIntent().getStringExtra("result_id");
//            slotID = getIntent().getStringExtra("game_time_id");
//           // game_time_id = getIntent().getStringExtra("game_time_id");
//            dateTextView.setText(getIntent().getStringExtra("result_date"));
//            wining_no_spinner.setText(getIntent().getStringExtra("result_date"));
//            til_winning_no.setText(getIntent().getStringExtra("result_result"));
//            win_no=getIntent().getStringExtra("result_result");
//            slotSpinner.setEnabled(false);
//            selectDateBtn.setEnabled(false);
//        }else {
//            slotSpinner.setEnabled(true);
//            selectDateBtn.setEnabled(true);
//        }


        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        currentDate = formatter.format(date);


        getSupportActionBar().setTitle(game_name);




        fetchGameTimings();

        if (game_id.equals("6"))
        {
            wining_no_spinner.setVisibility(View.VISIBLE);
            til_winning_no.setVisibility(View.GONE);
            fetchSpinWiningNo();
        }
        if (game_id.equals("11"))
        {
            wining_no_spinner.setVisibility(View.VISIBLE);
            til_winning_no.setVisibility(View.GONE);
            fetchSpinWiningNo();//lucky Seven Number fetch
        }

        if (game_id.equals("7"))
        {
            til_winning_no.setVisibility(View.VISIBLE);
            ArrayAdapter<String> adapter_winning_no = new ArrayAdapter<String>(this,
                    android.R.layout.simple_dropdown_item_1line, winning_no_thunderball);
            til_winning_no.setAdapter(adapter_winning_no);
        }

        if (game_id.equals("16"))
        {
            til_winning_no.setVisibility(View.VISIBLE);
            ArrayAdapter<String> adapter_winning_no = new ArrayAdapter<String>(this,
                    android.R.layout.simple_dropdown_item_1line, winning_no);
            til_winning_no.setAdapter(adapter_winning_no);
        }


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, sample);

        slotSpinner.setAdapter(adapter);
        wining_no_spinner.setAdapter(adapter);


        til_winning_no.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                win_no = (String) adapterView.getItemAtPosition(i);
            }
        });


        wining_no_spinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Spin_Wining_No_Model model = (Spin_Wining_No_Model) adapterView.getItemAtPosition(i);

                if (game_id.equals("6"))
                {
                    spin_number = model.getId();
                }
                else
                {
                    if (model.getNo().equals("2-6"))
                    {
                        random_two_six = new Random().nextInt((max_two_six - min_two_six) + 1) + min_two_six;

                        spin_number = String.valueOf(random_two_six);

                    }
                    else if (model.getNo().equals("8-12"))
                    {
                        random_eight_twelve = new Random().nextInt((max_eight_twelve - min_eight_twelve) + 1) + min_eight_twelve;

                        spin_number = String.valueOf(random_eight_twelve);
                    }
                    else
                    {
                        spin_number = model.getNo();
                    }

                }



            }
        });


        slotSpinner.setOnItemClickListener((parent, view, position, id) -> {
            Timing_Model timing_model = (Timing_Model) parent.getItemAtPosition(position);
            if (dateTextView.getText().toString().equals(currentDate)) {
                if (!timing_model.status.equals("CLOSED")) {
                    slotID = timing_model.id;
                    selectedSlot = timing_model.baji;
                } else {
                    slotID = "";
                    selectedSlot = "";
                    Toast.makeText(this, "Result timeout!", Toast.LENGTH_SHORT).show();
                }
            } else {
                slotID = timing_model.id;
                selectedSlot = timing_model.baji;
            }
        });

//        winningNumber = winningNumberEditTxt.getText().toString();

        selectDateBtn.setOnClickListener(view -> {
            // Get Current Date
            final Calendar c = Calendar.getInstance();
            int mYear = c.get(Calendar.YEAR);
            int mMonth = c.get(Calendar.MONTH);
            int mDay = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(AddOtherResult.this,
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
            if (slotID.equals("")) {
                Toast.makeText(this, "Select a slot.", Toast.LENGTH_SHORT).show();
            }  else if (dateTextView.getText().toString().equals("Please select a date") || dateTextView.getText().toString().isEmpty() || dateTextView.getText().toString() == null) {
                Toast.makeText(this, "Select a date.", Toast.LENGTH_SHORT).show();
            } /*else if (winningNumberEditTxt.getText().toString().isEmpty()) {
                Toast.makeText(this, "Enter a winning number", Toast.LENGTH_SHORT).show();
            }*/ else  if (game_id.equals("11") && spin_number.equals("")){
                Toast.makeText(this, "Enter a correct number", Toast.LENGTH_SHORT).show();
            } else  if (game_id.equals("7") && win_no.equals("") ){
                Toast.makeText(this, "Enter a correct number", Toast.LENGTH_SHORT).show();
            }
            else if (game_id.equals("16") && (win_no.equals("")))
            {
                Toast.makeText(this, "Enter a correct number", Toast.LENGTH_SHORT).show();
            }
            else if (game_id.equals("6") && spin_number.equals(""))
            {
                Toast.makeText(this, "Enter a correct number", Toast.LENGTH_SHORT).show();
            }
            else {
                addResult();
//                if(getIntent().getStringExtra("type").equals("Add")) {
//
//                    addResult();
//                }else{
//                    edit_result();
//                }
            }
        });
    }
    private void addResult() {
        String resultDate = dateTextView.getText().toString();
        winningNumber = win_no;
        Call<String> call=null;
        if(game_id.equals("11")) {
            call = myInterface.add_lucky_result(game_id, slotID, resultDate, spin_number);
        } else if(game_id.equals("7")) {
            call = myInterface.add_thunder_result(game_id, slotID, resultDate, winningNumber);
        }
        else if(game_id.equals("16")) {
            call = myInterface.add_circle_result(game_id, slotID, resultDate, winningNumber);
        }
        else if(game_id.equals("6")) {
            call = myInterface.add_spin_result(game_id, slotID, resultDate, spin_number);
        }
        ProgressUtils.showLoadingDialog(AddOtherResult.this);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String res = response.body();
                if (res == null) {
                    Toast.makeText(AddOtherResult.this, "No data found.", Toast.LENGTH_SHORT).show();
                    ProgressUtils.cancelLoading();
                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(res);
                        if (jsonObject.getString("rec").equals("0")) {
                            Toast.makeText(AddOtherResult.this, "Couldn't add result.", Toast.LENGTH_SHORT).show();
                        } else if (jsonObject.getString("rec").equals("1")) {
                            Toast.makeText(AddOtherResult.this, "Successfully Added Result!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(AddOtherResult.this, MainActivity.class));
                            finish();
                        } else {
                            ProgressUtils.cancelLoading();
                            Toast.makeText(AddOtherResult.this, "Check details", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        ProgressUtils.cancelLoading();
                        Toast.makeText(AddOtherResult.this, "Something went wrong :(" + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                ProgressUtils.cancelLoading();
                Toast.makeText(AddOtherResult.this, "Something went wrong :(", Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }

   /* private void edit_result() {
        ProgressUtils.showLoadingDialog(this);
        //        String newResult = winningNumberEditTxt.getText().toString();
        Call<String> call=null;
        if(game_id.equals("11")) {
            call = myInterface.edit_lucky_result(game_id, slotID, res spin_number);
        } else if(game_id.equals("7")) {
            call = myInterface.edit_thunder_result(game_id, slotID,  winningNumber);
        }
        else if(game_id.equals("16")) {
            call = myInterface.edit_circle_result(game_id, slotID,  winningNumber);
        }
        else if(game_id.equals("6")) {
            call = myInterface.edit_circle_result(game_id, slotID,  spin_number);
        }
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String res = response.body();
                if (response.body() == null){
                    ProgressUtils.cancelLoading();
                    Toast.makeText(AddOtherResult.this, "Failed!", Toast.LENGTH_SHORT).show();
                }
                else{
                    try {
                        JSONObject jsonObject = new JSONObject(res);
                        if (jsonObject.getString("rec").equals("0")){
                            ProgressUtils.cancelLoading();
                            Toast.makeText(AddOtherResult.this, "Sorry, failed to add result!", Toast.LENGTH_SHORT).show();
                        }
                        else if (jsonObject.getString("rec").equals("1")){
                            ProgressUtils.cancelLoading();
                            Toast.makeText(AddOtherResult.this, "Successfully Added!", Toast.LENGTH_SHORT).show();
                        }
                        else if (jsonObject.getString("rec").equals("2")){
                            ProgressUtils.cancelLoading();
                            Toast.makeText(AddOtherResult.this, "Result Timeout!", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            ProgressUtils.cancelLoading();
                            Toast.makeText(AddOtherResult.this, "Sorry, couldnt add result!", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                ProgressUtils.cancelLoading();
                Toast.makeText(AddOtherResult.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
            }
        });
    }*/



    private void fetchGameTimings() {
        Call<String> call = null;
        if(game_id.equals("11")) {
             call = myInterface.fetchLuckyGameTimings(game_id,"","");
        }else if(game_id.equals("7")) {
            call = myInterface.fetchThunderGameTimings(game_id, "","");
        }
        else if(game_id.equals("16")) {
            call = myInterface.circle_game_timing(game_id,"","");
        }
        else if(game_id.equals("6")) {
            call = myInterface.spin_game_timing(game_id,"","");
        }
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String res = response.body();
                if (res == null) {
                    Toast.makeText(AddOtherResult.this, "Error", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONArray jsonArray = new JSONArray(res);
                        if (jsonArray.length() == 0) {
                            Toast.makeText(AddOtherResult.this, "Not timings found.", Toast.LENGTH_SHORT).show();
                            ProgressUtils.cancelLoading();

                        } else {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                timing_model_list.add(new Timing_Model(jsonObject.getString("id"),
                                        jsonObject.getString("now_time"),
                                        jsonObject.getString("status"),
                                        jsonObject.getString("start_time"),
                                        jsonObject.getString("end_time"),
                                        jsonObject.getString("count"),
                                        jsonObject.getString("date_status"),
                                       "",
                                        ""
                                ));
                                ArrayAdapter<Timing_Model> adapter = new ArrayAdapter<Timing_Model>(AddOtherResult.this,
                                        android.R.layout.simple_dropdown_item_1line, timing_model_list);

                                slotSpinner.setAdapter(adapter);
//                                if(getIntent().getStringExtra("type").equals("Edit")){
//                                    if(jsonObject.getString("id").equals(slotID)) {
//                                        slotSpinner.setText(jsonObject.getString("start_time"));
//                                    }
//                                }
                                ProgressUtils.cancelLoading();
                            }
                        }
                    } catch (JSONException e) {
                        Toast.makeText(AddOtherResult.this, "Something went wrong.", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                        ProgressUtils.cancelLoading();
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(AddOtherResult.this, "Something wnet wrong.", Toast.LENGTH_SHORT).show();
                ProgressUtils.cancelLoading();
            }
        });
    }

    private void fetchSpinWiningNo() {

        Call<String> call = null;

        if (game_id.equals("6"))
        {
            call = myInterface.fetch_spin_no();
        }
        else if (game_id.equals("11"))
        {
            call = myInterface.fetch_lucky_saven_no();
        }

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String res = response.body();
                if (res == null) {
                    Toast.makeText(AddOtherResult.this, "Error", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONArray jsonArray = new JSONArray(res);
                        if (jsonArray.length() == 0) {
                            Toast.makeText(AddOtherResult.this, "Not found.", Toast.LENGTH_SHORT).show();
                            ProgressUtils.cancelLoading();

                        } else {
                            spin_wining_no_models_list.clear();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                spin_wining_no_models_list.add(new Spin_Wining_No_Model(jsonObject.getString("id"),
                                        jsonObject.getString("no")
                                        ));
                                ArrayAdapter<Spin_Wining_No_Model> adapter = new ArrayAdapter<Spin_Wining_No_Model>(AddOtherResult.this,
                                        android.R.layout.simple_dropdown_item_1line, spin_wining_no_models_list);

                                wining_no_spinner.setAdapter(adapter);
                                ProgressUtils.cancelLoading();
                            }
                        }
                    } catch (JSONException e) {
                        Toast.makeText(AddOtherResult.this, "Something went wrong.", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                        ProgressUtils.cancelLoading();
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(AddOtherResult.this, "Something wnet wrong.", Toast.LENGTH_SHORT).show();
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
        super.onBackPressed();
        finish();
    }

}