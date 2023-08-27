package com.ff_queen.admin.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ff_queen.admin.Adapter.UserAdapter;
import com.ff_queen.admin.Interfaces.MyInterface;
import com.ff_queen.admin.Models.User_Model;
import com.ff_queen.admin.R;
import com.ff_queen.admin.Utilities.ApiClient;
import com.ff_queen.admin.Utilities.ProgressUtils;
import com.ff_queen.admin.databinding.ActivityUserBinding;
import com.weiwangcn.betterspinner.library.BetterSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserActivity extends AppCompatActivity {

    private ActivityUserBinding binding;
    private MyInterface myInterface;
    private ArrayList<User_Model> user_models = new ArrayList<>();
    EditText text_date;
    TextView button_search;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources()
                .getColor(R.color.primary_dark)));
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("User");

        myInterface = ApiClient.getApiClient().create(MyInterface.class);
        text_date=findViewById(R.id.text_date);
        button_search=findViewById(R.id.button_search);


        button_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(text_date.getText().toString().equals("")){
                    text_date.setError("Enter a mobile number");
                }else{
                    fetchUser(text_date.getText().toString());
                }
            }
        });
        fetchUser("");

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


    private void fetchUser(String num)
    {

        Call<String> call = myInterface.    fetch_all_user(num);
        ProgressUtils.showLoadingDialog(UserActivity.this);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                String res = response.body();

                if (res == null)
                {
                    Toast.makeText(UserActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    ProgressUtils.cancelLoading();
                }
                else
                {

                    try {
                        JSONArray jsonArray = new JSONArray(res);

                        if (jsonArray.length() ==0)
                        {
                            Toast.makeText(UserActivity.this, "No User", Toast.LENGTH_SHORT).show();
                            ProgressUtils.cancelLoading();
                        }
                        else
                        {
                            user_models.clear();
                            for (int i=0; i<jsonArray.length(); i++)
                            {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                String id = jsonObject.getString("id");
                                String name = jsonObject.getString("name");
                                String mobile = jsonObject.getString("mobile");
                                String email = jsonObject.getString("email");
                                String wallet = jsonObject.getString("wallet");
                                String status = jsonObject.getString("status");
                                String b_name= jsonObject.getString("bank_name");
                                String ifsc = jsonObject.getString("ifsc_number");
                                String a_no = jsonObject.getString("account_number");
                                String upi = jsonObject.getString("upi");
                                String pass = jsonObject.getString("password");

                                user_models.add(new User_Model(id,name,email,mobile,wallet,status,b_name,pass,upi,ifsc,a_no));
                            }

                            binding.contentUser.rvUser.setAdapter(new UserAdapter(UserActivity.this,user_models));
                            ProgressUtils.cancelLoading();

                        }

                    } catch (JSONException e) {

                        Toast.makeText(UserActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                        ProgressUtils.cancelLoading();
                    }

                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

                Toast.makeText(UserActivity.this, "Slow Network", Toast.LENGTH_SHORT).show();
                ProgressUtils.cancelLoading();
            }
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
    public class UserAdapter extends RecyclerView.Adapter<UserAdapter.Viewholder> {

        private Context context;
        private ArrayList<User_Model> user_models;
        BetterSpinner type;
        MyInterface myInterface;
        public UserAdapter(Context context, ArrayList<User_Model> user_models) {
            this.context = context;
            this.user_models = user_models;
            myInterface = ApiClient.getApiClient().create(MyInterface.class);
        }

        @NonNull
        @Override
        public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.custom_user_list,parent,false);

            return new Viewholder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull Viewholder holder, int position) {

            User_Model model = user_models.get(position);

            if (model.getName().equals("null"))
            {
                holder.txt_user_name.setText("No Name");
            }
            else
            {
                holder.txt_user_name.setText(model.getName());
            }

            if (model.getPhone().equals("null"))
            {
                holder.txt_phone.setText("No Number");
            }
            else
            {
                holder.txt_phone.setText("Phone Number : "+model.getPhone());
            }

            if (model.getEmail().equals("null"))
            {
                holder.txt_email.setText("No Email");
            }
            else
            {
                holder.txt_email.setText("Email : "+model.getEmail());
            }
            if (model.getWallet().equals("null"))
            {
                holder.wallet.setText("Wallet amount : 0");
            }
            else
            {
                holder.wallet.setText("Wallet amount : "+model.getWallet());
            }
            holder.status.setText("Status : "+model.getStatus());

            if(model.getStatus().equals("Y")){
                holder.btn_active.setVisibility(View.GONE);
                holder.btn_inactive.setVisibility(View.VISIBLE);
            }else{
                holder.btn_active.setVisibility(View.VISIBLE);
                holder.btn_inactive.setVisibility(View.GONE);
            }
            if (model.getBank_name().equals("null"))
            {
                holder.bank_name.setVisibility(View.GONE);
            }
            else
            {
                holder.bank_name.setVisibility(View.VISIBLE);
            }
            if (model.getBank_name().equals("null"))
            {
                holder.upi.setVisibility(View.GONE);
            }
            else
            {
                holder.upi.setVisibility(View.VISIBLE);
            }
            holder.upi.setText("UPI Id: "+model.getUpi());
            holder.pass.setText("Password: "+model.getPass());
            holder.bank_name.setText("Bank Name: "+model.getBank_name()+"\n\nAccount No: "+model.getAccount_no()+"\n\nIFSC Code: "+model.getIfsc());
            holder.btn_active.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Call<String> call = myInterface.change_status("Y",model.getId());
                    ProgressUtils.showLoadingDialog(context);
                    call.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            String res = response.body();
                            if (res == null){
                                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                                ProgressUtils.cancelLoading();
                            }
                            else {
                                try {
                                    JSONObject jsonObject = new JSONObject(res);
                                    if (jsonObject.getString("rec").equals("0")) {
                                        Toast.makeText(context, "status not change", Toast.LENGTH_SHORT).show();
                                        ProgressUtils.cancelLoading();
                                    } else {
                                        holder.status.setText("Status : "+"N");
                                        holder.btn_active.setVisibility(View.GONE);
                                        holder.btn_inactive.setVisibility(View.VISIBLE);
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
                            Toast.makeText(context, "Something went wrong.", Toast.LENGTH_SHORT).show();
                            ProgressUtils.cancelLoading();
                        }
                    });
                }
            });
            holder.btn_inactive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Call<String> call = myInterface.change_status("N",model.getId());
                    ProgressUtils.showLoadingDialog(context);
                    call.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            String res = response.body();
                            if (res == null){
                                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                                ProgressUtils.cancelLoading();
                            }
                            else {
                                try {
                                    JSONObject jsonObject = new JSONObject(res);
                                    if (jsonObject.getString("rec").equals("0")) {
                                        Toast.makeText(context, "status not change", Toast.LENGTH_SHORT).show();
                                        ProgressUtils.cancelLoading();
                                    } else {
                                        holder.status.setText("Status : "+"Y");
                                        holder.btn_active.setVisibility(View.VISIBLE);
                                        holder.btn_inactive.setVisibility(View.GONE);
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
                            Toast.makeText(context, "Something went wrong.", Toast.LENGTH_SHORT).show();
                            ProgressUtils.cancelLoading();
                        }
                    });
                }
            });
            holder.wallet_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Dialog dialog_wallet = new Dialog(context);
                    dialog_wallet.setContentView(R.layout.dialog_confirmation);
                    EditText desc = dialog_wallet.findViewById(R.id.desc);
                    EditText amount = dialog_wallet.findViewById(R.id.amount);
                    TextView btn_no = dialog_wallet.findViewById(R.id.btn_no);
                    TextView text_play = dialog_wallet.findViewById(R.id.text_play);
                    text_play.setText("Are You Sure You Want To Play ???");
                    type = dialog_wallet.findViewById(R.id.category_spinner);
                    TextView btn_continue = dialog_wallet.findViewById(R.id.btn_continue);
                    String[] sample = {"Credit","Debit"};
                    String am_type = "";
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
                            android.R.layout.simple_dropdown_item_1line, sample);
                    type.setAdapter(adapter);

                    type.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                        }
                    });
                    btn_continue.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(amount.getText().toString().trim().equals("")){
                                amount.setError("Enter Amount");
                            }else if(type.getText().toString().equals("")){
                                Toast.makeText(context, "Select type", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Call<String> call = myInterface.insert_wallet_money(model.getId(),type.getText().toString(), amount.getText().toString().trim(), desc.getText().toString());
                                ProgressUtils.showLoadingDialog(context);
                                call.enqueue(new Callback<String>() {
                                    @Override
                                    public void onResponse(Call<String> call, Response<String> response) {
                                        String res = response.body();
                                        if (res == null) {
                                            Toast.makeText(context, "No data found.", Toast.LENGTH_SHORT).show();
                                            ProgressUtils.cancelLoading();
                                        } else {
                                            try {
                                                JSONObject jsonObject = new JSONObject(res);
                                                if (jsonObject.getString("rec").equals("0")) {
                                                    Toast.makeText(context, "Couldn't add result.", Toast.LENGTH_SHORT).show();
                                                    ProgressUtils.cancelLoading();
                                                }
                                                else if (jsonObject.getString("rec").equals("3")) {
                                                    Toast.makeText(context, "Insufficient Wallet Balance.", Toast.LENGTH_SHORT).show();
                                                    ProgressUtils.cancelLoading();
                                                }
                                                else if (jsonObject.getString("rec").equals("1")) {
                                                    fetchUser("");
                                                    Toast.makeText(context, "Successful", Toast.LENGTH_SHORT).show();
                                                    ProgressUtils.cancelLoading();
                                                    dialog_wallet.dismiss();

                                                } else {
                                                    ProgressUtils.cancelLoading();
                                                    Toast.makeText(context, "Check details", Toast.LENGTH_SHORT).show();
                                                }
                                            } catch (JSONException e) {
                                                ProgressUtils.cancelLoading();
                                                Toast.makeText(context, "Something went wrong :(" + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                                                e.printStackTrace();
                                            }

                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<String> call, Throwable t) {
                                        ProgressUtils.cancelLoading();
                                        Toast.makeText(context, "Something went wrong :(", Toast.LENGTH_SHORT).show();
                                        t.printStackTrace();
                                    }
                                });
                            }
                        }
                    });
                    btn_no.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog_wallet.dismiss();
                        }
                    });
                    dialog_wallet.show();
                }
            });

            holder.passbook.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle b=new Bundle();
                    b.putString("user_id",model.getId());
                    startActivity(new Intent(context,Passbook.class).putExtras(b));
                }
            });
        }

        @Override
        public int getItemCount() {

            return user_models.size();
        }

        public class Viewholder extends RecyclerView.ViewHolder {

            TextView txt_user_name, txt_email, txt_phone,wallet,status,pass,bank_name,upi;
            TextView btn_active,btn_inactive,passbook;
            ImageView wallet_btn;
            public Viewholder(@NonNull View itemView) {
                super(itemView);

                txt_user_name = itemView.findViewById(R.id.txt_user_name);
                txt_email = itemView.findViewById(R.id.txt_email);
                txt_phone = itemView.findViewById(R.id.txt_phone);
                wallet = itemView.findViewById(R.id.wallet);
                status = itemView.findViewById(R.id.status);
                btn_active = itemView.findViewById(R.id.active);
                btn_inactive = itemView.findViewById(R.id.inactive);
                wallet_btn = itemView.findViewById(R.id.wallet_btn);
                passbook = itemView.findViewById(R.id.passbook);
                bank_name = itemView.findViewById(R.id.bank_details);
                upi = itemView.findViewById(R.id.upi);
                pass = itemView.findViewById(R.id.pass);
            }
        }
    }

}