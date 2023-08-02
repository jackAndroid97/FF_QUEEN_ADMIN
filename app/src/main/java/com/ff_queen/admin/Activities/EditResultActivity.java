package com.ff_queen.admin.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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

import com.ff_queen.admin.Interfaces.MyInterface;
import com.ff_queen.admin.Models.Spin_Wining_No_Model;
import com.ff_queen.admin.R;
import com.ff_queen.admin.Utilities.ApiClient;
import com.ff_queen.admin.Utilities.ProgressUtils;
import com.weiwangcn.betterspinner.library.BetterSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditResultActivity extends AppCompatActivity {

    private BetterSpinner categorySpinner,wining_no_spinner;
    private BetterSpinner slotSpinner;
    private TextView dateTextView;
    private TextView selectDateBtn;
    private EditText winningNumberEditTxt;
    private TextView addResultBtn;
    private MyInterface myInterface;
    private String result_id;
    private String game_time_id,game_name,game_id;
    private String result_category;
    private List<Spin_Wining_No_Model> spin_wining_no_models_list = new ArrayList<>();
    private String result_result;
    private String result_date;
    private List<String> categoryList = new ArrayList<>();
    private List<String> slotList = new ArrayList<>();
    private String game_time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_result);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        categorySpinner = findViewById(R.id.category_spinner);
        slotSpinner = findViewById(R.id.slot_spinner);
        dateTextView = findViewById(R.id.date_view);
        selectDateBtn = findViewById(R.id.select_date_btn);
        winningNumberEditTxt = findViewById(R.id.winning_number_edtTxt);
        addResultBtn = findViewById(R.id.add_result_btn);
        wining_no_spinner = findViewById(R.id.wining_no_spinner);

        myInterface = ApiClient.getApiClient().create(MyInterface.class);

        result_id = getIntent().getStringExtra("result_id");
        game_time_id = getIntent().getStringExtra("game_time_id");
        result_category = getIntent().getStringExtra("result_category");
        result_result = getIntent().getStringExtra("result_result");
        result_date = getIntent().getStringExtra("result_date");
        game_time = getIntent().getStringExtra("game_time");
        game_name = getIntent().getStringExtra("game_name");
        game_id = getIntent().getStringExtra("game_id");

        categoryList.add(result_category);
        slotList.add(game_time_id);
        // Have to ask later

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, categoryList);

//        int categorySpinnerPosition = adapter.getPosition("SINGLE");

        categorySpinner.setAdapter(adapter);
        categorySpinner.setSelection(0);
        categorySpinner.setEnabled(false);
        categorySpinner.setText(result_category);
        if (result_category.equals("SINGLE")) {

            wining_no_spinner.setVisibility(View.GONE);
            winningNumberEditTxt.setVisibility(View.VISIBLE);
            int maxLength = 1;
            winningNumberEditTxt.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});

        } else {

            wining_no_spinner.setVisibility(View.VISIBLE);
            winningNumberEditTxt.setVisibility(View.GONE);
            fetchFatafatWinningNo();
                /*int maxLength = 3;
                winningNumberEditTxt.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});*/
        }

        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, slotList);

//        int slotSpinnerPosition = adapter.getPosition("17");

        slotSpinner.setAdapter(adapter2);
        slotSpinner.setSelection(0);
        slotSpinner.setEnabled(false);
        slotSpinner.setText(game_time);

        dateTextView.setText(result_date);
        selectDateBtn.setEnabled(false);

        winningNumberEditTxt.setText(result_result);
        wining_no_spinner.setText(result_result);
        wining_no_spinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Spin_Wining_No_Model model = (Spin_Wining_No_Model) adapterView.getItemAtPosition(i);

               // winningNumber = model.getNo();
            }
        });

        addResultBtn.setOnClickListener(v -> {
            if (winningNumberEditTxt.getText().toString().equals("")){
                Toast.makeText(EditResultActivity.this, "Please select a  result!", Toast.LENGTH_SHORT).show();
            }
            else {
                addResult();
            }
        });

    }
    private void fetchFatafatWinningNo() {


        Call<String> call = myInterface.fetch_fatafat_patti_no();
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String res = response.body();
                if (res == null) {
                    Toast.makeText(EditResultActivity.this, "Error", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONArray jsonArray = new JSONArray(res);
                        if (jsonArray.length() == 0) {
                            Toast.makeText(EditResultActivity.this, "Not found.", Toast.LENGTH_SHORT).show();
                            ProgressUtils.cancelLoading();

                        } else {
                            spin_wining_no_models_list.clear();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                spin_wining_no_models_list.add(new Spin_Wining_No_Model(jsonObject.getString("id"),
                                        jsonObject.getString("no")
                                ));
                                ArrayAdapter<Spin_Wining_No_Model> adapter = new ArrayAdapter<Spin_Wining_No_Model>(EditResultActivity.this,
                                        android.R.layout.simple_dropdown_item_1line, spin_wining_no_models_list);

                                wining_no_spinner.setAdapter(adapter);
                                ProgressUtils.cancelLoading();
                            }
                        }
                    } catch (JSONException e) {
                        Toast.makeText(EditResultActivity.this, "Something went wrong.", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                        ProgressUtils.cancelLoading();
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(EditResultActivity.this, "Something wnet wrong.", Toast.LENGTH_SHORT).show();
                ProgressUtils.cancelLoading();
            }
        });
    }


    private void addResult() {
        ProgressUtils.showLoadingDialog(this);
        String newResult="";
      /*  if(result_category.equals("SINGLE")) {
             newResult = winningNumberEditTxt.getText().toString();
        }else{
            newResult=wining_no_spinner.getText().toString();
        }*/
        Call<String> call = myInterface.edit_result(result_id,game_time_id,winningNumberEditTxt.getText().toString());
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String res = response.body();
                if (response.body() == null){
                    ProgressUtils.cancelLoading();
                    Toast.makeText(EditResultActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                }
                else{
                    try {
                        JSONObject jsonObject = new JSONObject(res);
                        if (jsonObject.getString("rec").equals("0")){
                            ProgressUtils.cancelLoading();
                            Toast.makeText(EditResultActivity.this, "Sorry, failed to add result!", Toast.LENGTH_SHORT).show();
                        }
                        else if (jsonObject.getString("rec").equals("1")){
                            Bundle bundle = new Bundle();
                            bundle.putString("game_name",game_name);
                            bundle.putString("game_id", game_id);
                            ProgressUtils.cancelLoading();
                            Toast.makeText(EditResultActivity.this, "Successfully Added!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(EditResultActivity.this, ResultActivity.class).putExtras(bundle));

                        }
                        else if (jsonObject.getString("rec").equals("2")){
                            ProgressUtils.cancelLoading();
                            Toast.makeText(EditResultActivity.this, "Result Timeout!", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            ProgressUtils.cancelLoading();
                            Toast.makeText(EditResultActivity.this, "Sorry, couldnt add result!", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                ProgressUtils.cancelLoading();
                Toast.makeText(EditResultActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
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