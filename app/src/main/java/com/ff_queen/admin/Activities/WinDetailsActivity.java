package com.ff_queen.admin.Activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ff_queen.admin.Interfaces.MyInterface;
import com.ff_queen.admin.Models.Baji_Model;
import com.ff_queen.admin.Models.GamePlayDetailsModel;
import com.ff_queen.admin.R;
import com.ff_queen.admin.Utilities.ApiClient;
import com.ff_queen.admin.Utilities.ProgressUtils;
import com.ff_queen.admin.ViewModel.ApiResponse;
import com.ff_queen.admin.databinding.ActivityGamePlayDetailsBinding;
import com.ff_queen.admin.databinding.ActivityWinDetailsBinding;
import com.weiwangcn.betterspinner.library.BetterSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WinDetailsActivity extends AppCompatActivity {

    public static ActivityWinDetailsBinding binding;
    private ApiResponse apiResponse;
    private MyInterface myInterface;
    private BetterSpinner slotSpinner;
    private final int MY_PERMISSIONS_REQUEST_USE_CAMERA = 0x00AF;
    int start_month, start_year, start_day;
    private String start_date = "";
    List<GamePlayDetailsModel> payment_models = new ArrayList<>();
    String date="",b_id="1";
    String user_id;
    Transaction_Adapter adapter;
    RecyclerView rv_passbook;
    private String[] sample = {"NO DATA"};
    private List<Baji_Model> baji_models = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWinDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        myInterface = ApiClient.getApiClient().create(MyInterface.class);
        getSupportActionBar().setTitle("Game Play Details");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        date=getIntent().getExtras().getString("date");
        user_id=getIntent().getExtras().getString("user_id");


        rv_passbook = findViewById(R.id.rv_transaction);
        rv_passbook.setHasFixedSize(true);
        rv_passbook.setLayoutManager(new LinearLayoutManager(WinDetailsActivity.this,LinearLayoutManager.VERTICAL,false));


        fetch_transaction_history();

    }

    public class Transaction_Adapter extends RecyclerView.Adapter<Transaction_Adapter.MyViewHolder> {

        Context context;
        List<GamePlayDetailsModel> models;

        public Transaction_Adapter(Context context, List<GamePlayDetailsModel> models) {
            this.context = context;
            this.models = models;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.single_game_details, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {


            holder.txt_g_name.setText(models.get(position).getGame_name());
            holder.category.setText(models.get(position).getCategory());
            holder.baji.setText("Baji: "+models.get(position).getBaji());
            holder.digit.setText(models.get(position).getDigit());
            holder.txt_amount.setText("₹"+models.get(position).getAmount());




        }

        @Override
        public int getItemCount() {
            return models.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            TextView txt_g_name, category, baji,digit,txt_amount;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);

                txt_g_name = itemView.findViewById(R.id.txt_g_name);
                category = itemView.findViewById(R.id.category);
                baji = itemView.findViewById(R.id.baji);
                digit = itemView.findViewById(R.id.digit);
                txt_amount = itemView.findViewById(R.id.txt_amount);

            }
        }
    }

    private void fetch_transaction_history() {
        Call<String> call = myInterface.fetch_total_gameplay_details(date,user_id);
        ProgressUtils.showLoadingDialog(WinDetailsActivity.this);
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
                                payment_models.add(new GamePlayDetailsModel(

                                        jsonObject.getString("game_name"),
                                        jsonObject.getString("category"),
                                        jsonObject.getString("baji"),
                                        jsonObject.getString("digit"),
                                        jsonObject.getString("rupees")


                                ));
                            }
                            adapter = new Transaction_Adapter(WinDetailsActivity.this,payment_models);
                            rv_passbook.setAdapter(adapter);
                            ProgressUtils.cancelLoading();

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        ProgressUtils.cancelLoading();
                        Toast.makeText(WinDetailsActivity.this, ""+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(WinDetailsActivity.this, "No Response", Toast.LENGTH_SHORT).show();
                    ProgressUtils.cancelLoading();
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                ProgressUtils.cancelLoading();
                Toast.makeText(WinDetailsActivity.this, "Error", Toast.LENGTH_SHORT).show();
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