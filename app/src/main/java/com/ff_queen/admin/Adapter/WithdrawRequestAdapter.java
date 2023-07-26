package com.ff_queen.admin.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ff_queen.admin.Models.User;
import com.ff_queen.admin.Models.Withdraw_Request_Model;
import com.ff_queen.admin.ViewModel.ApiResponse;
import com.ff_queen.admin.databinding.SingleMoneyRequestBinding;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class WithdrawRequestAdapter extends RecyclerView.Adapter<WithdrawRequestAdapter.ViewHolder> {

    private Context context;
    private List<Withdraw_Request_Model> money_request_models;
    private String formatDate;
    private ApiResponse apiResponse;
    private User user;

    public WithdrawRequestAdapter(Context context, List<Withdraw_Request_Model> money_request_models) {
        this.context = context;
        this.money_request_models = money_request_models;

        apiResponse = new ApiResponse(context);
        user = new User(context);
    }

    public void updateList(List<Withdraw_Request_Model> request_models)
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

        Withdraw_Request_Model model = money_request_models.get(position);


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

        holder.binding.amount.setTextColor(Color.parseColor("#33A01D")); //Green

        holder.binding.btnApproed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                apiResponse.withdrawApproved(model.getUser_id(),model.getAmount(),model.getId());

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
