package com.ff_queen.admin.Activities;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.ff_queen.admin.Interfaces.MyInterface;
import com.ff_queen.admin.R;
import com.ff_queen.admin.Utilities.ApiClient;
import com.ff_queen.admin.Utilities.ProgressUtils;
import com.ff_queen.admin.Models.User;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login_Activity extends AppCompatActivity {

    TextView button_login,text_forget;
    EditText edt_number,edt_password;
    MyInterface myInterface;
    User user;
    private String user_id ="";

    private NetworkChangeReceiver receiver;
    private boolean isConnected = false;
    AlertDialog dialog;
    AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        receiver = new NetworkChangeReceiver();
        registerReceiver(receiver, filter);
//
//        //Alert Dialog
        builder= new AlertDialog.Builder(Login_Activity.this);
        View view1=getLayoutInflater().inflate(R.layout.custom_no_internet,null);
        builder.setView(view1);
        dialog=builder.create();
        dialog.setCancelable(false);

        myInterface = ApiClient.getApiClient().create(MyInterface.class);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        );
        this.getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        );

        user = new User(this);
        user_id = user.getUser_id();
        if (!user_id.equals("")){
            startActivity(new Intent(Login_Activity.this,MainActivity.class));
            finishAffinity();
        }
        button_login =findViewById(R.id.btn_login);
        edt_number =findViewById(R.id.edt_number);
        edt_password =findViewById(R.id.edt_password);
        text_forget =findViewById(R.id.text_forget);

        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edt_number.getText().toString().isEmpty())
                    Toast.makeText(Login_Activity.this, "Please enter mobile number", Toast.LENGTH_SHORT).show();
                else if (edt_password.getText().toString().isEmpty())
                    Toast.makeText(Login_Activity.this, "Please enter password", Toast.LENGTH_SHORT).show();
                else {
                    login();
                }
            }
        });



//        text_forget.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(Login_Activity.this,Forget_Password.class));
//                overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
//            }
//        });
    }

    private void login() {
        String mobile   = edt_number.getText().toString();
        String password = edt_password.getText().toString();

        Call<String> call = myInterface.login(mobile,password);
        ProgressUtils.showLoadingDialog(this);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String res = response.body();
                if (res == null){
                    Toast.makeText(Login_Activity.this, "Error", Toast.LENGTH_SHORT).show();
                    ProgressUtils.cancelLoading();
                }
                else {
                    try {
                        JSONObject jsonObject = new JSONObject(res);
                        if (jsonObject.getString("rec").equals("0")) {
                            Toast.makeText(Login_Activity.this, "Admin not found", Toast.LENGTH_SHORT).show();
                            ProgressUtils.cancelLoading();
                        } else {
                            user.setUser_id(jsonObject.getString("id"));
                            startActivity(new Intent(Login_Activity.this,MainActivity.class));
                            finishAffinity();
                            overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                            ProgressUtils.cancelLoading();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(Login_Activity.this, "Something went wrong.", Toast.LENGTH_SHORT).show();
            }
        });
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