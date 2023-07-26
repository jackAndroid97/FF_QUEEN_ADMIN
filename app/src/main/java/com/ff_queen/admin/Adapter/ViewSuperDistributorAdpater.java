package com.ff_queen.admin.Adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.ff_queen.admin.Activities.DistributorActivity;
import com.ff_queen.admin.Interfaces.MyInterface;
import com.ff_queen.admin.Models.User;
import com.ff_queen.admin.Models.View_User_Model;
import com.ff_queen.admin.Utilities.ApiClient;
import com.ff_queen.admin.Utilities.ProgressUtils;
import com.ff_queen.admin.databinding.SingleViewDetailsBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewSuperDistributorAdpater extends RecyclerView.Adapter<ViewSuperDistributorAdpater.ViewHolder> {

    private Context context;
    private List<View_User_Model> user_models;
    private User user;
    private MyInterface myInterface;

    public ViewSuperDistributorAdpater(Context context, List<View_User_Model> user_models) {
        this.context = context;
        this.user_models = user_models;

        user = new User(context);
        myInterface = ApiClient.getApiClient().create(MyInterface.class);
    }

    public void updateUserList(List<View_User_Model> view_user_models)
    {
        user_models = view_user_models;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        SingleViewDetailsBinding binding = SingleViewDetailsBinding.inflate(inflater,parent,false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.binding.name.setText(user_models.get(position).getName());
        holder.binding.email.setText("Email ID : "+user_models.get(position).getEmail());
        holder.binding.phoneNo.setText("Phone No : "+user_models.get(position).getPhone());

        holder.binding.btnView.setText("View Distributor");

        holder.binding.btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle b = new Bundle();
                b.putString("id",user_models.get(position).getId());
                context.startActivity(new Intent(context, DistributorActivity.class).putExtras(b));
            }
        });

        holder.binding.btnChamgePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new AlertDialog.Builder(context)
                        .setTitle("Change Password")
                        .setMessage("Are you sure you want to change this password?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                changePassword(user_models.get(position).getId(),"Super Distributor");
                            }
                        })

                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();


            }
        });


    }

    @Override
    public int getItemCount() {
        return user_models.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        SingleViewDetailsBinding binding;

        public ViewHolder(@NonNull SingleViewDetailsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }

    }


    private void changePassword(String user_id, String type)
    {
        Call<String> call = myInterface.change_password(user_id,type);
        ProgressUtils.showLoadingDialog(context);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                try {
                    JSONObject jsonObject = new JSONObject(response.body());

                    if (jsonObject.getString("rec").equals("1"))
                    {
                        Toast.makeText(context, "Change Successfully", Toast.LENGTH_SHORT).show();
                        ProgressUtils.cancelLoading();
                    }
                    else
                    {
                        Toast.makeText(context, "Not Change", Toast.LENGTH_SHORT).show();
                        ProgressUtils.cancelLoading();
                    }

                } catch (JSONException e) {

                    Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                    ProgressUtils.cancelLoading();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

                Toast.makeText(context, "Slow Network", Toast.LENGTH_SHORT).show();
                ProgressUtils.cancelLoading();
            }
        });
    }

}
