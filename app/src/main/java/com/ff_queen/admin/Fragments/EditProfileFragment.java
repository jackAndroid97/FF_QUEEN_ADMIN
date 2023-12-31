package com.ff_queen.admin.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ff_queen.admin.Activities.Login_Activity;
import com.ff_queen.admin.Activities.MainActivity;
import com.google.android.material.textfield.TextInputLayout;
import com.ff_queen.admin.Interfaces.MyInterface;
import com.ff_queen.admin.Models.User;
import com.ff_queen.admin.R;
import com.ff_queen.admin.Utilities.ApiClient;
import com.ff_queen.admin.Utilities.ProgressUtils;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileFragment extends Fragment {

    private TextInputLayout email, password;
    private MyInterface myInterface;
    EditText w_number,youtube_link,h_number;
    private User user;
    private Button btn_save;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);

        myInterface = ApiClient.getApiClient().create(MyInterface.class);
        user = new User(getContext());

        email = view.findViewById(R.id.email);
        password = view.findViewById(R.id.password);
        btn_save = view.findViewById(R.id.btn_save);
        w_number = view.findViewById(R.id.w_number);
        youtube_link = view.findViewById(R.id.youtube);
        h_number = view.findViewById(R.id.h_number);

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (email.getEditText().getText().toString().isEmpty())
                {
                    Toast.makeText(getContext(), "Email is required", Toast.LENGTH_SHORT).show();
                }
                else if (password.getEditText().getText().toString().isEmpty())
                {
                    Toast.makeText(getContext(), "Password is required", Toast.LENGTH_SHORT).show();

                }
                else
                {
                    updateProfile();
                }
            }
        });
        fetch_profile();
        return view;


    }
    private void fetch_profile() {


        Call<String> call = myInterface.fetch_profile();
        ProgressUtils.showLoadingDialog(getActivity());
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String res = response.body();
                if (res == null){
                    Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
                    ProgressUtils.cancelLoading();
                }
                else {
                    try {
                        JSONObject jsonObject = new JSONObject(res);
                        if (jsonObject.getString("rec").equals("0")) {
                            Toast.makeText(getActivity(), "Data not found", Toast.LENGTH_SHORT).show();
                            ProgressUtils.cancelLoading();
                        } else {
                            email.getEditText().setText(jsonObject.getString("email"));
                            password.getEditText().setText(jsonObject.getString("password"));
                            w_number.setText(jsonObject.getString("whatsapp"));
                            youtube_link.setText(jsonObject.getString("link"));
                            h_number.setText(jsonObject.getString("mobile"));
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
                Toast.makeText(getActivity(), "Something went wrong.", Toast.LENGTH_SHORT).show();
                ProgressUtils.cancelLoading();
            }
        });
    }

    private void updateProfile()
    {
        Call<String> call = myInterface.update_admin_profile(user.getUser_id(),
                email.getEditText().getText().toString(),
                password.getEditText().getText().toString(),w_number.getText().toString(),youtube_link.getText().toString(),h_number.getText().toString());
        ProgressUtils.showLoadingDialog(getContext());
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                try {
                    JSONObject jsonObject = new JSONObject(response.body());

                    if (jsonObject.getString("rec").equals("1"))
                    {
                        Toast.makeText(getContext(), "Update Successfully", Toast.LENGTH_SHORT).show();
                        ProgressUtils.cancelLoading();
                        fetch_profile();
                    }
                    else
                    {
                        Toast.makeText(getContext(), "Not Update", Toast.LENGTH_SHORT).show();
                        ProgressUtils.cancelLoading();
                    }

                } catch (JSONException e) {

                    Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                    ProgressUtils.cancelLoading();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

                Toast.makeText(getContext(), "Slow Network", Toast.LENGTH_SHORT).show();
                ProgressUtils.cancelLoading();
            }
        });
    }

}