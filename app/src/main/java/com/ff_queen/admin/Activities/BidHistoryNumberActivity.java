package com.ff_queen.admin.Activities;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ff_queen.admin.Interfaces.MyInterface;
import com.ff_queen.admin.Models.Baji_Model;
import com.ff_queen.admin.Models.BitNumberModel;
import com.ff_queen.admin.Models.G_T_Model;
import com.ff_queen.admin.Models.PassBookModel;
import com.ff_queen.admin.R;
import com.ff_queen.admin.Utilities.ApiClient;
import com.ff_queen.admin.Utilities.ProgressUtils;
import com.ff_queen.admin.ViewModel.ApiResponse;
import com.ff_queen.admin.databinding.ActivityBidHistoryNumberBinding;
import com.weiwangcn.betterspinner.library.BetterSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BidHistoryNumberActivity extends AppCompatActivity {

    public static ActivityBidHistoryNumberBinding binding;
    private ApiResponse apiResponse;
    private MyInterface myInterface;
    private BetterSpinner slotSpinner;
    private final int MY_PERMISSIONS_REQUEST_USE_CAMERA = 0x00AF;
    int start_month, start_year, start_day;
    private String start_date = "";
    List<BitNumberModel> payment_models = new ArrayList<>();
    String date="",b_id="1";
    String game_id,game_name,cat_id;
    Transaction_Adapter adapter;
    RecyclerView rv_passbook;
    private String[] sample = {"NO DATA"};
    private List<Baji_Model> baji_models = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBidHistoryNumberBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        myInterface = ApiClient.getApiClient().create(MyInterface.class);
        getSupportActionBar().setTitle("Bid History");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        game_id=getIntent().getExtras().getString("game_id");
        game_name=getIntent().getExtras().getString("game_name");
        cat_id=getIntent().getExtras().getString("cat_id");

        rv_passbook = findViewById(R.id.rv_transaction);
        rv_passbook.setHasFixedSize(true);
        rv_passbook.setLayoutManager(new LinearLayoutManager(BidHistoryNumberActivity.this,LinearLayoutManager.VERTICAL,false));
        date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        binding.contentBidHistoryNumber.textDate.setText(date);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, sample);
        binding.contentBidHistoryNumber.baji.setAdapter(adapter);
        binding.contentBidHistoryNumber.baji.setText("Baji: 1");
        fetch_transaction_history("",date,"1");
        binding.contentBidHistoryNumber.textDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(BidHistoryNumberActivity.this,
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
                            binding.contentBidHistoryNumber.textDate.setText(day + "-" + month + "-" + year);
                            start_date = day + "-" + month + "-" + year;

                        }, mYear, mMonth, mDay);
                //  datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
                datePickerDialog.show();
            }
        });
        binding.contentBidHistoryNumber.buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fetch_transaction_history(binding.contentBidHistoryNumber.digit.getText().toString(),start_date,b_id);
            }
        });
        binding.contentBidHistoryNumber.baji.setOnItemClickListener((parent, view, position, id) -> {
            Baji_Model baji_model = (Baji_Model) parent.getItemAtPosition(position);
            b_id = baji_model.getId();
            binding.contentBidHistoryNumber.baji.setText(baji_model.getName());

        });

        fetchBaji();
    }

    public class Transaction_Adapter extends RecyclerView.Adapter<Transaction_Adapter.MyViewHolder> {

        Context context;
        List<BitNumberModel> models;

        public Transaction_Adapter(Context context, List<BitNumberModel> models) {
            this.context = context;
            this.models = models;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.single_bid_number, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {


            holder.txt_digit.setText(models.get(position).getDigit());
            holder.quantity.setText(models.get(position).getTotal_no_wise_rs());

            holder.txt_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle=new Bundle();
                    bundle.putString("game_id",game_id);
                    bundle.putString("cat_id",cat_id);
                    bundle.putString("digit",models.get(position).getDigit());
                    bundle.putString("date",binding.contentBidHistoryNumber.textDate.getText().toString());
                    bundle.putString("baji",b_id);
                    context.startActivity(new Intent(context,BidHistoryActivity.class).putExtras(bundle));
                }
            });


        }

        @Override
        public int getItemCount() {
            return models.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            TextView txt_digit, quantity, txt_view;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);

                txt_digit = itemView.findViewById(R.id.txt_digit);
                quantity = itemView.findViewById(R.id.quantity);
                txt_view = itemView.findViewById(R.id.txt_view);

            }
        }
    }

    private void fetch_transaction_history(String no_digit, String date,String baji) {
        Call<String> call = myInterface.fetch_bid_history_number(game_id,cat_id,date,no_digit,baji);
        ProgressUtils.showLoadingDialog(BidHistoryNumberActivity.this);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.body()!=null){
                    String res = response.body();
                    try {
                        JSONArray jsonArray = new JSONArray(res);
                        if (jsonArray.length() == 0) {
                            ProgressUtils.cancelLoading();
                            payment_models.clear();
                            rv_passbook.setVisibility(View.GONE);
//                            Toast.makeText(getActivity(), "No Data found", Toast.LENGTH_SHORT).show();
                        } else {
                            rv_passbook.setVisibility(View.VISIBLE);
                            payment_models.clear();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                payment_models.add(new BitNumberModel(

                                        jsonObject.getString("digit"),
                                        jsonObject.getString("total_no_wise_rs")


                                ));
                            }
                            adapter = new Transaction_Adapter(BidHistoryNumberActivity.this,payment_models);
                            rv_passbook.setAdapter(adapter);
                            ProgressUtils.cancelLoading();

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        ProgressUtils.cancelLoading();
                        Toast.makeText(BidHistoryNumberActivity.this, ""+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(BidHistoryNumberActivity.this, "No Response", Toast.LENGTH_SHORT).show();
                    ProgressUtils.cancelLoading();
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                ProgressUtils.cancelLoading();
                Toast.makeText(BidHistoryNumberActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchBaji() {
        Call<String> call = myInterface.fetch_baji_dropdown(game_id);
        // Toast.makeText(this, "" + game_id, Toast.LENGTH_SHORT).show();
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String res = response.body();
                if (res == null) {
                    Toast.makeText(BidHistoryNumberActivity.this, "Error", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONArray jsonArray = new JSONArray(res);
                        if (jsonArray.length() == 0) {
                            Toast.makeText(BidHistoryNumberActivity.this, "Not timings found.", Toast.LENGTH_SHORT).show();
                            ProgressUtils.cancelLoading();

                        } else {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                baji_models.add(new Baji_Model(
                                        "Baji: "+jsonObject.getString("baji"),
                                        jsonObject.getString("baji_id"),
                                        jsonObject.getString("baji")

                                ));
                                ArrayAdapter<Baji_Model> adapter = new ArrayAdapter<Baji_Model>(BidHistoryNumberActivity.this,
                                        android.R.layout.simple_dropdown_item_1line, baji_models);

                                binding.contentBidHistoryNumber.baji.setAdapter(adapter);
                                ProgressUtils.cancelLoading();
                            }
                        }
                    } catch (JSONException e) {
                        Toast.makeText(BidHistoryNumberActivity.this, "Something went wrong."+ e.getMessage(), Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                        ProgressUtils.cancelLoading();
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(BidHistoryNumberActivity.this, "Something wnet wrong.", Toast.LENGTH_SHORT).show();
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