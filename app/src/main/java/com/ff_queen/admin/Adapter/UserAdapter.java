package com.ff_queen.admin.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ff_queen.admin.Activities.AddResultsActivity;
import com.ff_queen.admin.Activities.Login_Activity;
import com.ff_queen.admin.Activities.MainActivity;
import com.ff_queen.admin.Interfaces.MyInterface;
import com.ff_queen.admin.Models.User_Model;
import com.ff_queen.admin.R;
import com.ff_queen.admin.Utilities.ApiClient;
import com.ff_queen.admin.Utilities.ProgressUtils;
import com.weiwangcn.betterspinner.library.BetterSpinner;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
                                            } else if (jsonObject.getString("rec").equals("1")) {
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
    }

    @Override
    public int getItemCount() {

        return user_models.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {

        TextView txt_user_name, txt_email, txt_phone,wallet,status;
        TextView btn_active,btn_inactive;
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
        }
    }
}
