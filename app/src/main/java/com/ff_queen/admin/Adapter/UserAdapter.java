package com.ff_queen.admin.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ff_queen.admin.Models.User_Model;
import com.ff_queen.admin.R;

import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.Viewholder> {

    private Context context;
    private ArrayList<User_Model> user_models;

    public UserAdapter(Context context, ArrayList<User_Model> user_models) {
        this.context = context;
        this.user_models = user_models;
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

    }

    @Override
    public int getItemCount() {

        return user_models.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {

        TextView txt_user_name, txt_email, txt_phone;

        public Viewholder(@NonNull View itemView) {
            super(itemView);

            txt_user_name = itemView.findViewById(R.id.txt_user_name);
            txt_email = itemView.findViewById(R.id.txt_email);
            txt_phone = itemView.findViewById(R.id.txt_phone);
        }
    }
}
