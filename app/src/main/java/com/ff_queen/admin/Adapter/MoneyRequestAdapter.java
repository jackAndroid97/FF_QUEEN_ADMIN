package com.ff_queen.admin.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.ff_queen.admin.Activities.Passbook;
import com.ff_queen.admin.Models.Money_Request_Model;
import com.ff_queen.admin.Models.User;
import com.ff_queen.admin.ViewModel.ApiResponse;
import com.ff_queen.admin.databinding.SingleMoneyRequestBinding;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MoneyRequestAdapter extends RecyclerView.Adapter<MoneyRequestAdapter.ViewHolder> {

    private Context context;
    private List<Money_Request_Model> money_request_models;
    private String formatDate;
    private ApiResponse apiResponse;
    private User user;

    public MoneyRequestAdapter(Context context, List<Money_Request_Model> money_request_models) {
        this.context = context;
        this.money_request_models = money_request_models;

        apiResponse = new ApiResponse(context);
        user = new User(context);
    }

    public void updateList(List<Money_Request_Model> request_models)
    {
        money_request_models = request_models;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        SingleMoneyRequestBinding  binding = SingleMoneyRequestBinding.inflate(inflater,parent,false);

        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Money_Request_Model model = money_request_models.get(position);


        try {

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = simpleDateFormat.parse(model.getDate());
            SimpleDateFormat sdf = new SimpleDateFormat("dd, LLL yyyy");
            formatDate = sdf.format(date);

        } catch (ParseException e) {

            e.printStackTrace();
        }


        holder.binding.name.setText(model.getName());
        holder.binding.amount.setText("₹ "+model.getAmount());
        holder.binding.date.setText(formatDate);
        holder.binding.time.setText(model.getTime());

        holder.binding.bankDetails.setText("Bank Name: "+ model.getBank_name()+"\n"+"Account No: "+model.getAccount_no()
                +"\n"+"IFSC Code: "+model.getIfsc_code());
        holder.binding.mobile.setText("Mobile Number: "+model.getMobile());
        holder.binding.walletBalance.setText("Current Balance: ₹ "+model.getWallet());

        holder.binding.amount.setTextColor(Color.parseColor("#33A01D")); //Green
        holder.binding.passbook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle=new Bundle();
                bundle.putString("user_id",model.getUser_id());
                context.startActivity(new Intent(context, Passbook.class).putExtras(bundle));

            }
        });

        holder.binding.btnApproed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                apiResponse.approvedRequest(model.getUser_id(),model.getAmount(),model.getId(),"Success",holder.binding.remarks.getText().toString());

            }
        });
        holder.binding.btnReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                apiResponse.approvedRequest(model.getUser_id(),model.getAmount(),model.getId(),"Reject",holder.binding.remarks.getText().toString());

            }
        });

    }

    @Override
    public int getItemCount() {
        return money_request_models.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        SingleMoneyRequestBinding binding;

        public ViewHolder(@NonNull SingleMoneyRequestBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }
}
