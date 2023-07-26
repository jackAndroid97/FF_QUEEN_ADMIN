package com.ff_queen.admin.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.ff_queen.admin.Interfaces.MyInterface;
import com.ff_queen.admin.Models.View_User_Model;
import com.ff_queen.admin.R;
import com.ff_queen.admin.Utilities.ApiClient;
import com.ff_queen.admin.ViewModel.ApiResponse;
import com.ff_queen.admin.databinding.ActivityAddSuperDistributorBinding;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddSuperDistributorActivity extends AppCompatActivity {

    public static ActivityAddSuperDistributorBinding binding;
    private ApiResponse apiResponse;
    private static final String[] TYPE = new String[] {
            "Super Distributor", "Distributor", "Retailer", "User"
    };
    private String [] sample={"No User"};
    public static String type="";
    private MyInterface myInterface;
    List<View_User_Model> super_dis_models = new ArrayList<>();
    List<View_User_Model> dis_models = new ArrayList<>();
    List<View_User_Model> retailer_models = new ArrayList<>();
    private String sup_dis_id="", dis_id="", retailer_id="", retailer_code="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddSuperDistributorBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);

        initView();
    }

    private void initView() {

        apiResponse = new ApiResponse(this);
        myInterface = ApiClient.getApiClient().create(MyInterface.class);

        getSupportActionBar().setTitle("Add User");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, TYPE);

        binding.contentAddSuperDistributor.spinnerType.setAdapter(adapter);

        ArrayAdapter<String> adapter_sup_dis = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, sample);

        binding.contentAddSuperDistributor.spinnerSupDis.setAdapter(adapter_sup_dis);
        binding.contentAddSuperDistributor.spinnerDistributor.setAdapter(adapter_sup_dis);
        binding.contentAddSuperDistributor.spinnerRetailer.setAdapter(adapter_sup_dis);

        binding.contentAddSuperDistributor.spinnerType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                type = (String) adapterView.getItemAtPosition(i);
                binding.contentAddSuperDistributor.spinnerSupDis.setText("");
                binding.contentAddSuperDistributor.spinnerDistributor.setText("");
                binding.contentAddSuperDistributor.spinnerRetailer.setText("");
                if (type.equals("Super Distributor"))
                {
                    binding.contentAddSuperDistributor.spinnerSupDis.setVisibility(View.GONE);
                    binding.contentAddSuperDistributor.spinnerDistributor.setVisibility(View.GONE);
                    binding.contentAddSuperDistributor.spinnerRetailer.setVisibility(View.GONE);
                }
                if (type.equals("Distributor"))
                {
                    binding.contentAddSuperDistributor.spinnerSupDis.setVisibility(View.VISIBLE);
                    binding.contentAddSuperDistributor.spinnerDistributor.setVisibility(View.GONE);
                    binding.contentAddSuperDistributor.spinnerRetailer.setVisibility(View.GONE);
                }
                else if (type.equals("Retailer"))
                {
                    binding.contentAddSuperDistributor.spinnerSupDis.setVisibility(View.VISIBLE);
                    binding.contentAddSuperDistributor.spinnerDistributor.setVisibility(View.VISIBLE);
                    binding.contentAddSuperDistributor.spinnerRetailer.setVisibility(View.GONE);
                }
                else if (type.equals("User"))
                {
                    binding.contentAddSuperDistributor.spinnerSupDis.setVisibility(View.VISIBLE);
                    binding.contentAddSuperDistributor.spinnerDistributor.setVisibility(View.VISIBLE);
                    binding.contentAddSuperDistributor.spinnerRetailer.setVisibility(View.VISIBLE);
                }

            }
        });

        binding.contentAddSuperDistributor.spinnerSupDis.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                View_User_Model model = (View_User_Model) adapterView.getItemAtPosition(i);

                sup_dis_id = model.getId();
                fetchDistributor();
                binding.contentAddSuperDistributor.spinnerDistributor.setText("");
                binding.contentAddSuperDistributor.spinnerRetailer.setText("");
            }
        });

        binding.contentAddSuperDistributor.spinnerDistributor.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                View_User_Model model = (View_User_Model) adapterView.getItemAtPosition(i);

                dis_id = model.getId();
                fetchRetailer();
                binding.contentAddSuperDistributor.spinnerRetailer.setText("");
            }
        });

        binding.contentAddSuperDistributor.spinnerRetailer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                View_User_Model model = (View_User_Model) adapterView.getItemAtPosition(i);

                retailer_id = model.getId();
                retailer_code = model.getRef_code();
            }
        });


        binding.contentAddSuperDistributor.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name = binding.contentAddSuperDistributor.name.getText().toString();
                String email = binding.contentAddSuperDistributor.email.getText().toString();
                String phone = binding.contentAddSuperDistributor.phoneNo.getText().toString();
                String password = binding.contentAddSuperDistributor.password.getText().toString();

                if (type.equals(""))
                {
                    Toast.makeText(AddSuperDistributorActivity.this, "Select Type", Toast.LENGTH_SHORT).show();
                }
                else if (type.equals("Distributor") && sup_dis_id.equals(""))
                {
                    Toast.makeText(AddSuperDistributorActivity.this, "Select Super Distributor", Toast.LENGTH_SHORT).show();
                }
                else if (type.equals("Retailer") && (sup_dis_id.equals("") || dis_id.equals("")))
                {
                    Toast.makeText(AddSuperDistributorActivity.this, "Select Super Distributor & Distributor", Toast.LENGTH_SHORT).show();
                }
                else if (type.equals("User") && (sup_dis_id.equals("") || dis_id.equals("") || retailer_code.equals("")))
                {
                    Toast.makeText(AddSuperDistributorActivity.this, "Select Super Distributor & Distributor & Retailer", Toast.LENGTH_SHORT).show();
                }
                else if (name.isEmpty())
                {
                    toastShort("Name is required");
                }
                else if (phone.isEmpty())
                {
                    toastShort("Phone Number is required");
                }
                else if (email.isEmpty())
                {
                    toastShort("Email is required");
                }
                else if (password.isEmpty())
                {
                    toastShort("Password is required");
                }
                else
                {

                    apiResponse.addSuperDistributor(name,email,phone,password,type,sup_dis_id,dis_id,retailer_id,retailer_code);

                }


            }
        });

        fetchSuperDistributor();

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
                        super_dis_models.clear();

                        //Toast.makeText(AddSuperDistributorActivity.this, "No Super Distributor", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        super_dis_models.clear();
                        super_dis_models = response.body();

                    }
                    ArrayAdapter<View_User_Model> adapter = new ArrayAdapter<>(AddSuperDistributorActivity.this, android.R.layout.simple_dropdown_item_1line,super_dis_models);
                    binding.contentAddSuperDistributor.spinnerSupDis.setAdapter(adapter);
                }
                else
                {
                    Toast.makeText(AddSuperDistributorActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<View_User_Model>> call, Throwable t) {

                Toast.makeText(AddSuperDistributorActivity.this, "Slow Network", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void fetchDistributor()
    {
        Call<List<View_User_Model>> call = myInterface.fetch_distributor(sup_dis_id);
        call.enqueue(new Callback<List<View_User_Model>>() {
            @Override
            public void onResponse(Call<List<View_User_Model>> call, Response<List<View_User_Model>> response) {

                if (response.isSuccessful() && response.body() !=null)
                {
                    JSONArray jsonArray = new JSONArray(response.body());

                    if (jsonArray.length() ==0)
                    {
                        dis_models.clear();

                        //Toast.makeText(AddSuperDistributorActivity.this, "No Distributor", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        dis_models.clear();
                        dis_models = response.body();

                    }
                    ArrayAdapter<View_User_Model> adapter = new ArrayAdapter<>(AddSuperDistributorActivity.this, android.R.layout.simple_dropdown_item_1line,dis_models);
                    binding.contentAddSuperDistributor.spinnerDistributor.setAdapter(adapter);
                }
                else
                {
                    Toast.makeText(AddSuperDistributorActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<View_User_Model>> call, Throwable t) {

                Toast.makeText(AddSuperDistributorActivity.this, "Slow Network", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void fetchRetailer()
    {
        Call<List<View_User_Model>> call = myInterface.fetch_retailer(dis_id);
        call.enqueue(new Callback<List<View_User_Model>>() {
            @Override
            public void onResponse(Call<List<View_User_Model>> call, Response<List<View_User_Model>> response) {

                if (response.isSuccessful() && response.body() !=null)
                {
                    JSONArray jsonArray = new JSONArray(response.body());

                    if (jsonArray.length() ==0)
                    {
                        retailer_models.clear();

                        //Toast.makeText(AddSuperDistributorActivity.this, "No Retailer", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        retailer_models.clear();
                        retailer_models = response.body();

                    }
                    ArrayAdapter<View_User_Model> adapter = new ArrayAdapter<>(AddSuperDistributorActivity.this, android.R.layout.simple_dropdown_item_1line,retailer_models);
                    binding.contentAddSuperDistributor.spinnerRetailer.setAdapter(adapter);
                }
                else
                {
                    Toast.makeText(AddSuperDistributorActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<View_User_Model>> call, Throwable t) {

                Toast.makeText(AddSuperDistributorActivity.this, "Slow Network", Toast.LENGTH_SHORT).show();
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

    private void toastShort(String msg)
    {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onBackPressed() {
        finish();
    }
}