package com.ff_queen.admin.ViewModel;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ff_queen.admin.Activities.AddCommissionActivity;
import com.ff_queen.admin.Activities.AddSuperDistributorActivity;
import com.ff_queen.admin.Activities.MainActivity;
import com.ff_queen.admin.Activities.MoneyRequestActivity;
import com.ff_queen.admin.Activities.UserMoneyWithdrawActivity;
import com.ff_queen.admin.Interfaces.MyInterface;
import com.ff_queen.admin.Models.Commission_Model;
import com.ff_queen.admin.Models.Money_Request_Model;
import com.ff_queen.admin.Models.User;
import com.ff_queen.admin.Models.View_User_Model;
import com.ff_queen.admin.Models.Withdraw_All_Request_Model;
import com.ff_queen.admin.Models.Withdraw_Request_Model;
import com.ff_queen.admin.Utilities.ApiClient;
import com.ff_queen.admin.Utilities.ProgressUtils;
import com.ff_queen.admin.databinding.ActivityAddSuperDistributorBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApiResponse extends ViewModel {

    private MyInterface myInterface;
    private Context context;
    private User user;
    private MutableLiveData<List<View_User_Model>> modelList_super_distributor;
    private MutableLiveData<List<View_User_Model>> modelList_distributor;
    private MutableLiveData<List<View_User_Model>> modelList_retailer;
    private MutableLiveData<List<View_User_Model>> modelList_user;
    private MutableLiveData<List<Money_Request_Model>> modelList_money_request;
    private MutableLiveData<List<Withdraw_Request_Model>> modelList_user_withdraw;
    private MutableLiveData<List<Withdraw_All_Request_Model>> modelList_all_withdraw;
    private MutableLiveData<List<Commission_Model>> modelList_commission;

    public ApiResponse(Context context) {

        this.context = context;
        user = new User(context);
        myInterface = ApiClient.getApiClient().create(MyInterface.class);

        modelList_super_distributor = new MutableLiveData<>();
        modelList_distributor = new MutableLiveData<>();
        modelList_retailer = new MutableLiveData<>();
        modelList_user = new MutableLiveData<>();
        modelList_money_request = new MutableLiveData<>();
        modelList_user_withdraw = new MutableLiveData<>();
        modelList_commission = new MutableLiveData<>();
        modelList_all_withdraw = new MutableLiveData<>();
    }

    public LiveData<List<View_User_Model>> getSuperDistributor()
    {
        return modelList_super_distributor;
    }

    public LiveData<List<View_User_Model>> getDistributor()
    {
        return modelList_distributor;
    }

    public LiveData<List<View_User_Model>> getRetailer()
    {
        return modelList_retailer;
    }

    public LiveData<List<View_User_Model>> getUsers()
    {
        return modelList_user;
    }

    public LiveData<List<Money_Request_Model>> getMoneyRequest()
    {
        return modelList_money_request;
    }

    public LiveData<List<Withdraw_Request_Model>> getWithdrawRequest()
    {
        return modelList_user_withdraw;
    }

    public LiveData<List<Withdraw_All_Request_Model>> getAllWithdrawRequest()
    {
        return modelList_all_withdraw;
    }

    public LiveData<List<Commission_Model>> getCommission()
    {
        return modelList_commission;
    }


    public void addSuperDistributor(String name, String email, String phone, String password, String type, String sup_dis_id,
                                    String dis_id, String retailer_id, String retailer_code)
    {

        Call<String> call = myInterface.add_super_distributor(name,email,phone,password,type,sup_dis_id,dis_id,retailer_id,retailer_code);
        ProgressUtils.showLoadingDialog(context);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                if (response.isSuccessful() && response.body() != null)
                {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body());

                        if (jsonObject.getString("rec").equals("1"))
                        {
                            Toast.makeText(context, "Add Successfully", Toast.LENGTH_SHORT).show();
                            ProgressUtils.cancelLoading();
                            AddSuperDistributorActivity.binding.contentAddSuperDistributor.name.setText("");
                            AddSuperDistributorActivity.binding.contentAddSuperDistributor.email.setText("");
                            AddSuperDistributorActivity.binding.contentAddSuperDistributor.phoneNo.setText("");
                            AddSuperDistributorActivity.binding.contentAddSuperDistributor.password.setText("");
                            AddSuperDistributorActivity.binding.contentAddSuperDistributor.spinnerSupDis.setText("");
                            AddSuperDistributorActivity.binding.contentAddSuperDistributor.spinnerDistributor.setText("");
                            AddSuperDistributorActivity.binding.contentAddSuperDistributor.spinnerRetailer.setText("");
                            AddSuperDistributorActivity.binding.contentAddSuperDistributor.spinnerType.setText("");
                        }
                        else if (jsonObject.getString("rec").equals("2"))
                        {
                            Toast.makeText(context, "Mobile user already exists", Toast.LENGTH_SHORT).show();
                            ProgressUtils.cancelLoading();
                        }
                        else
                        {
                            Toast.makeText(context, "Not Add", Toast.LENGTH_SHORT).show();
                            ProgressUtils.cancelLoading();
                        }

                    } catch (JSONException e) {

                        Toast.makeText(context, "Something Went wrong", Toast.LENGTH_SHORT).show();
                        ProgressUtils.cancelLoading();
                    }
                }
                else
                {
                    Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
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


    public void fetchSuperDistributor()
    {
        Call<List<View_User_Model>> call = myInterface.fetch_super_distributor();
        call.enqueue(new Callback<List<View_User_Model>>() {
            @Override
            public void onResponse(Call<List<View_User_Model>> call, Response<List<View_User_Model>> response) {

                if (response.isSuccessful() && response.body() !=null)
                {
                    JSONArray jsonArray = new JSONArray(response.body());

                    if (jsonArray.length() ==0)
                    {
                        Toast.makeText(context, "No Super Distributor", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        modelList_super_distributor.postValue(response.body());
                    }

                }
                else
                {
                    Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<View_User_Model>> call, Throwable t) {

                Toast.makeText(context, "Slow Network", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void fetchDistributor(String id)
    {
        Call<List<View_User_Model>> call = myInterface.fetch_distributor(id);
        call.enqueue(new Callback<List<View_User_Model>>() {
            @Override
            public void onResponse(Call<List<View_User_Model>> call, Response<List<View_User_Model>> response) {

                if (response.isSuccessful() && response.body() !=null)
                {
                    JSONArray jsonArray = new JSONArray(response.body());

                    if (jsonArray.length() ==0)
                    {
                        Toast.makeText(context, "No Distributor", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        modelList_distributor.postValue(response.body());
                    }

                }
                else
                {
                    Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<View_User_Model>> call, Throwable t) {

                Toast.makeText(context, "Slow Network", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void fetchRetailer(String id)
    {
        Call<List<View_User_Model>> call = myInterface.fetch_retailer(id);
        call.enqueue(new Callback<List<View_User_Model>>() {
            @Override
            public void onResponse(Call<List<View_User_Model>> call, Response<List<View_User_Model>> response) {

                if (response.isSuccessful() && response.body() !=null)
                {
                    JSONArray jsonArray = new JSONArray(response.body());

                    if (jsonArray.length() ==0)
                    {
                        Toast.makeText(context, "No Retailer", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        modelList_retailer.postValue(response.body());
                    }

                }
                else
                {
                    Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<View_User_Model>> call, Throwable t) {

                Toast.makeText(context, "Slow Network", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void fetchUser(String ref_code)
    {
        Call<List<View_User_Model>> call = myInterface.fetch_user(ref_code);
        call.enqueue(new Callback<List<View_User_Model>>() {
            @Override
            public void onResponse(Call<List<View_User_Model>> call, Response<List<View_User_Model>> response) {

                if (response.isSuccessful() && response.body() !=null)
                {
                    JSONArray jsonArray = new JSONArray(response.body());

                    if (jsonArray.length() ==0)
                    {
                        Toast.makeText(context, "No Users", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        modelList_user.postValue(response.body());
                    }

                }
                else
                {
                    Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<View_User_Model>> call, Throwable t) {

                Toast.makeText(context, "Slow Network", Toast.LENGTH_SHORT).show();
            }
        });
    }





    public void fetchUserWithdrawRequest()
    {

        Call<List<Withdraw_Request_Model>> call = myInterface.fetch_user_withdraw_request();
        call.enqueue(new Callback<List<Withdraw_Request_Model>>() {
            @Override
            public void onResponse(Call<List<Withdraw_Request_Model>> call, Response<List<Withdraw_Request_Model>> response) {

                if (response.isSuccessful() && response.body() !=null)
                {
                    JSONArray jsonArray = new JSONArray(response.body());

                    if (jsonArray.length()==0)
                    {
                        Toast.makeText(context, "No Withdrawal Request Found", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        modelList_user_withdraw.postValue(response.body());
                    }


                }
                else
                {

                    Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Withdraw_Request_Model>> call, Throwable t) {

                Toast.makeText(context, "Slow Network", Toast.LENGTH_SHORT).show();
            }
        });
    }




    public void approvedRequest(String s_dis_id, String amount, String id,String status,String remarks)
    {


    }


    public void withdrawApproved(String user_id, String amount, String id)
    {
        Call<String> call = myInterface.withdraw_approved(amount,user_id,id);
        ProgressUtils.showLoadingDialog(context);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                if (response.isSuccessful() && response.body() != null)
                {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body());

                        if (jsonObject.getString("rec").equals("1"))
                        {

                            Toast.makeText(context, "Approved Successfully", Toast.LENGTH_SHORT).show();
                            ProgressUtils.cancelLoading();
                            context.startActivity(new Intent(context, UserMoneyWithdrawActivity.class));
                            ((Activity)context).finish();
                        }
                        else
                        {
                            Toast.makeText(context, "Not Approved", Toast.LENGTH_SHORT).show();
                            ProgressUtils.cancelLoading();
                        }

                    } catch (JSONException e) {

                        Toast.makeText(context, "Something Went wrong", Toast.LENGTH_SHORT).show();
                        ProgressUtils.cancelLoading();
                    }
                }
                else
                {
                    Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
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


    public void withdrawAllApproved(String user_id, String amount, String id,String status,String remarks)
    {

    }




    public void editCommission(String amount, String type, String user_type, Dialog dialog)
    {
        Call<String> call = myInterface.edit_commission(amount,type,user_type);
        ProgressUtils.showLoadingDialog(context);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                if (response.isSuccessful() && response.body() != null)
                {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body());

                        if (jsonObject.getString("rec").equals("1"))
                        {

                            Toast.makeText(context, "Submit Successfully", Toast.LENGTH_SHORT).show();
                            ProgressUtils.cancelLoading();
                            dialog.dismiss();
                            context.startActivity(new Intent(context, AddCommissionActivity.class));
                            ((Activity)context).finishAffinity();
                        }
                        else
                        {
                            Toast.makeText(context, "Not Submit", Toast.LENGTH_SHORT).show();
                            ProgressUtils.cancelLoading();
                        }

                    } catch (JSONException e) {

                        Toast.makeText(context, "Something Went wrong", Toast.LENGTH_SHORT).show();
                        ProgressUtils.cancelLoading();
                    }
                }
                else
                {
                    Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
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
