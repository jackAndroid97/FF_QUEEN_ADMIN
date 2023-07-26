package com.ff_queen.admin.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ff_queen.admin.Models.Commission_Model;
import com.ff_queen.admin.Models.User;
import com.ff_queen.admin.R;
import com.ff_queen.admin.ViewModel.ApiResponse;
import com.ff_queen.admin.databinding.SingleCommissionBinding;

import java.util.List;

public class CommissionAdapter extends RecyclerView.Adapter<CommissionAdapter.ViewHolder> {

    private Context context;
    private List<Commission_Model> commission_models;
    private User user;
    private ApiResponse apiResponse;

    public CommissionAdapter(Context context, List<Commission_Model> commission_models) {
        this.context = context;
        this.commission_models = commission_models;

        user = new User(context);
        apiResponse = new ApiResponse(context);
    }

    public void updateList(List<Commission_Model> models)
    {
        commission_models = models;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        SingleCommissionBinding binding = SingleCommissionBinding.inflate(inflater,parent,false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {



        holder.binding.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });

    }

    @Override
    public int getItemCount() {
        return commission_models.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        SingleCommissionBinding binding;

        public ViewHolder(@NonNull SingleCommissionBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }

    }
}
