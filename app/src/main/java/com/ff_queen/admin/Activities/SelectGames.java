package com.ff_queen.admin.Activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.ff_queen.admin.Interfaces.MyInterface;
import com.ff_queen.admin.Models.Timing_Model;
import com.ff_queen.admin.Models.User;
import com.ff_queen.admin.R;
import com.ff_queen.admin.Utilities.ApiClient;
import com.ff_queen.admin.Utilities.ProgressUtils;
import com.ff_queen.admin.databinding.ActivitySelectGamesBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SelectGames extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivitySelectGamesBinding binding;
    RecyclerView rv_games;
    List<Timing_Model> models = new ArrayList<>();
    SelectGame_Adapter adapter;
    String game_id,game_name;
    Window window = this.getWindow();
    private MyInterface myInterface;
    private User user;

    private NetworkChangeReceiver receiver;
    private boolean isConnected = false;
    AlertDialog dialog;
    AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySelectGamesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        receiver = new NetworkChangeReceiver();
        registerReceiver(receiver, filter);

        //Alert Dialog
        builder= new AlertDialog.Builder(this);
        View view1=getLayoutInflater().inflate(R.layout.custom_no_internet,null);
        builder.setView(view1);
        dialog=builder.create();
        dialog.setCancelable(false);

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setTitle("Select Game");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        myInterface = ApiClient.getApiClient().create(MyInterface.class);
        user = new User(this);
        game_id=getIntent().getExtras().getString("game_id");
        game_name=getIntent().getExtras().getString("game_name");


        rv_games = findViewById(R.id.rv_games);
        rv_games.setHasFixedSize(true);
        rv_games.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));

        fetch_category();
    }

    public class SelectGame_Adapter extends RecyclerView.Adapter<SelectGame_Adapter.MyViewHolder> {
        Context context;
        List<Timing_Model> models ;
        public SelectGame_Adapter(Context context, List<Timing_Model> models) {
            this.context = context;
            this.models = models;
        }
        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.single_select, parent, false);
            return new MyViewHolder(view);
        }
        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
            holder.game_title.setText(models.get(position).getName());
//            holder.image.setImageResource(models.get(position).getImage());

            holder.lin_select.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                        Bundle bundle = new Bundle();
                        bundle.putString("game_id", game_id);
                        bundle.putString("cat_id", models.get(position).getId());
                        bundle.putString("game_name", game_name);
                        startActivity(new Intent(SelectGames.this, BidHistoryNumberActivity.class).putExtras(bundle));


                }
            });

        }

        @Override
        public int getItemCount() {
            return models.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            TextView game_title;
            ImageView image;
            LinearLayout lin_select;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);

                image = itemView.findViewById(R.id.image);
                game_title = itemView.findViewById(R.id.game_title);
                lin_select = itemView.findViewById(R.id.lin_select);

            }
        }
    }

    private void fetch_category() {
        Call<String> call = myInterface.fetch_categories();
        ProgressUtils.showLoadingDialog(SelectGames.this);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.body()!=null){
                    String res = response.body();
                    try {
                        JSONArray jsonArray = new JSONArray(res);
                        if (jsonArray.length() == 0) {
                            ProgressUtils.cancelLoading();
                            models.clear();
                            rv_games.setVisibility(View.GONE);
//                            Toast.makeText(getActivity(), "No Data found", Toast.LENGTH_SHORT).show();
                        } else {
                            rv_games.setVisibility(View.VISIBLE);
                            models.clear();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                models.add(new Timing_Model(jsonObject.getString("id"),
                                        jsonObject.getString("category"),
                                        ""));
                            }
                            adapter = new SelectGame_Adapter(SelectGames.this,models);
                            rv_games.setAdapter(adapter);
                            ProgressUtils.cancelLoading();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        ProgressUtils.cancelLoading();
                        Toast.makeText(SelectGames.this, ""+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(SelectGames.this, "No Response", Toast.LENGTH_SHORT).show();
                    ProgressUtils.cancelLoading();
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                ProgressUtils.cancelLoading();
                Toast.makeText(SelectGames.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
        finish();
    }

    private boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        if(!isConnected){
                            dialog.dismiss();
                            // networkStatus.setText("Now you are connected to Internet!");
                            isConnected = true;
                            //do your processing here ---
                            //if you need to post any data to the server or get status
                            //update from the server
                        }
                        return true;
                    }
                }
            }
        }
        if(!((Activity) context).isFinishing())
        {
            dialog.show();
        }

        //  networkStatus.setText("You are not connected to Internet!");
        isConnected = false;
        return false;
    }

    public class NetworkChangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (isNetworkAvailable(context)){
                dialog.dismiss();
                isConnected = true;
            }else {
                if(!((Activity) context).isFinishing())
                {
                    dialog.show();
                }

                //  networkStatus.setText("You are not connected to Internet!");
                isConnected = false;
            }
        }
    }
}