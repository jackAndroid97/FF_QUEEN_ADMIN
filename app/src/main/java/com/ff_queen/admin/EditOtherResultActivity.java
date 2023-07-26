package com.ff_queen.admin;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.navigation.ui.AppBarConfiguration;

import com.google.android.material.textfield.TextInputLayout;
import com.ff_queen.admin.Interfaces.MyInterface;
import com.ff_queen.admin.Models.Spin_Wining_No_Model;
import com.ff_queen.admin.Utilities.ApiClient;
import com.ff_queen.admin.Utilities.ProgressUtils;
import com.ff_queen.admin.databinding.ActivityEditOtherResultBinding;
import com.weiwangcn.betterspinner.library.BetterSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditOtherResultActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityEditOtherResultBinding binding;

    private BetterSpinner slotSpinner, wining_no_spinner;
    private TextView dateTextView;
    private TextView selectDateBtn;
    private EditText winningNumberEditTxt;
    private TextView addResultBtn;
    private MyInterface myInterface;
    private String result_id;
    private String game_time_id,game_id,game_time;
    private String result_result;
    private String result_date;
    private String spin_number;
    private List<String> categoryList = new ArrayList<>();
    private List<String> slotList = new ArrayList<>();
    private String[] sample = {"NO DATA"};
    private List<Spin_Wining_No_Model> spin_wining_no_models_list = new ArrayList<>();
    private TextInputLayout til_winning_no;
    private int random_two_six, random_eight_twelve;
    private final int min_two_six = 2;
    private final int max_two_six = 6;
    private String[] winning_no = {"Black","White","Red"};
    private String[] winning_no_thunderball = {"0","1","2","3","4","5","6","7","8","9"};

    private final int min_eight_twelve = 8;
    private final int max_eight_twelve = 12;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityEditOtherResultBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        slotSpinner = findViewById(R.id.slot_spinner);
        dateTextView = findViewById(R.id.date_view);
        selectDateBtn = findViewById(R.id.select_date_btn);
        winningNumberEditTxt = findViewById(R.id.winning_number_edtTxt);
        addResultBtn = findViewById(R.id.add_result_btn);
        wining_no_spinner = findViewById(R.id.wining_no_spinner);
        til_winning_no = findViewById(R.id.til_winning_no);

        myInterface = ApiClient.getApiClient().create(MyInterface.class);

        result_id = getIntent().getStringExtra("result_id");
        game_time_id = getIntent().getStringExtra("game_time_id");
        game_id = getIntent().getStringExtra("game_id");

        result_result = getIntent().getStringExtra("result_result");
        result_date = getIntent().getStringExtra("result_date");
        game_time = getIntent().getStringExtra("game_time");


        slotList.add(game_time_id);
        // Have to ask later


        if (game_id !=null)
        {

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
                wining_no_spinner.setVisibility(View.VISIBLE);
                ArrayAdapter<String> adapter_winning_no = new ArrayAdapter<String>(this,
                        android.R.layout.simple_dropdown_item_1line, winning_no_thunderball);
                wining_no_spinner.setAdapter(adapter_winning_no);

            }

            if (game_id.equals("16"))
            {
                wining_no_spinner.setVisibility(View.VISIBLE);
                ArrayAdapter<String> adapter_winning_no = new ArrayAdapter<String>(this,
                        android.R.layout.simple_dropdown_item_1line, winning_no);
                wining_no_spinner.setAdapter(adapter_winning_no);
            }

        }



        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, slotList);

        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, sample);

       // wining_no_spinner.setAdapter(adapter3);


        slotSpinner.setAdapter(adapter2);
        slotSpinner.setSelection(0);
        slotSpinner.setText(game_time);
        slotSpinner.setEnabled(false);

        dateTextView.setText(result_date);
        selectDateBtn.setEnabled(false);

        winningNumberEditTxt.setText(result_result);
        wining_no_spinner.setText(result_result);

        addResultBtn.setOnClickListener(v -> {
            if (game_id.equals("11") && spin_number.equals("")){
                Toast.makeText(this, "Enter a correct number", Toast.LENGTH_SHORT).show();
            } else  if (game_id.equals("7") && Integer.parseInt(wining_no_spinner.getText().toString())>9 ){
                Toast.makeText(this, "Enter a correct number", Toast.LENGTH_SHORT).show();
            }
            else if (game_id.equals("16") && (!wining_no_spinner.getText().toString().equals("Red") && !wining_no_spinner.getText().toString().equals("Black") && !wining_no_spinner.getText().toString().equals("White")))
            {
                Toast.makeText(this, "Enter a correct number", Toast.LENGTH_SHORT).show();
            }
            else {
                addResult();
            }
        });

        wining_no_spinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {



                if (game_id.equals("6"))
                {
                    Spin_Wining_No_Model model = (Spin_Wining_No_Model) adapterView.getItemAtPosition(i);
                    spin_number = model.getId();
                }
                else if (game_id.equals("7"))
                {
                    spin_number = (String) adapterView.getItemAtPosition(i);
                }
                else if (game_id.equals("16"))
                {
                    spin_number = (String) adapterView.getItemAtPosition(i);
                }
                else if (game_id.equals("11"))
                {
                    Spin_Wining_No_Model model = (Spin_Wining_No_Model) adapterView.getItemAtPosition(i);
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



    }

    private void addResult() {
        ProgressUtils.showLoadingDialog(this);
        String newResult;
        Call<String> call=null;
        if(game_id.equals("11")){
       //     newResult = winningNumberEditTxt.getText().toString();
         call = myInterface.edit_lucky_result(result_id,game_time_id,spin_number,result_date);

        }else if(game_id.equals("7")){
            newResult = wining_no_spinner.getText().toString();
         call = myInterface.edit_thunder_result(result_id,game_time_id,spin_number,result_date);
        }
        else if(game_id.equals("16")){
            newResult = wining_no_spinner.getText().toString();
            call = myInterface.edit_circle_result(result_id,game_time_id,spin_number);
        }
        else if(game_id.equals("6")){
            newResult = winningNumberEditTxt.getText().toString();
            call = myInterface.edit_spin_result(result_id,game_time_id,spin_number);
        }
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String res = response.body();
                if (response.body() == null){
                    ProgressUtils.cancelLoading();
                    Toast.makeText(EditOtherResultActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                }
                else{
                    try {
                        JSONObject jsonObject = new JSONObject(res);
                        if (jsonObject.getString("rec").equals("0")){
                            ProgressUtils.cancelLoading();
                            Toast.makeText(EditOtherResultActivity.this, "Sorry, failed to add result!", Toast.LENGTH_SHORT).show();
                        }
                        else if (jsonObject.getString("rec").equals("1")){
                            ProgressUtils.cancelLoading();
                            Toast.makeText(EditOtherResultActivity.this, "Successfully Added!", Toast.LENGTH_SHORT).show();
                        }
                        else if (jsonObject.getString("rec").equals("2")){
                            ProgressUtils.cancelLoading();
                            Toast.makeText(EditOtherResultActivity.this, "Result Timeout!", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            ProgressUtils.cancelLoading();
                            Toast.makeText(EditOtherResultActivity.this, "Sorry, couldnt add result!", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        ProgressUtils.cancelLoading();
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                ProgressUtils.cancelLoading();
                Toast.makeText(EditOtherResultActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(EditOtherResultActivity.this, "Error", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONArray jsonArray = new JSONArray(res);
                        if (jsonArray.length() == 0) {
                            Toast.makeText(EditOtherResultActivity.this, "Not found.", Toast.LENGTH_SHORT).show();
                            ProgressUtils.cancelLoading();

                        } else {
                            spin_wining_no_models_list.clear();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                spin_wining_no_models_list.add(new Spin_Wining_No_Model(jsonObject.getString("id"),
                                        jsonObject.getString("no")
                                ));
                                ArrayAdapter<Spin_Wining_No_Model> adapter = new ArrayAdapter<Spin_Wining_No_Model>(EditOtherResultActivity.this,
                                        android.R.layout.simple_dropdown_item_1line, spin_wining_no_models_list);

                                wining_no_spinner.setAdapter(adapter);
                                ProgressUtils.cancelLoading();
                            }
                        }
                    } catch (JSONException e) {
                        Toast.makeText(EditOtherResultActivity.this, "Something went wrong.", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                        ProgressUtils.cancelLoading();
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(EditOtherResultActivity.this, "Something wnet wrong.", Toast.LENGTH_SHORT).show();
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