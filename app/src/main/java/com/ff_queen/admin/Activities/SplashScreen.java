package com.ff_queen.admin.Activities;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextPaint;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.ff_queen.admin.R;

public class SplashScreen extends AppCompatActivity {
    ImageView img_app_logo;
    TextView textView;
    //User user;
    String saved_mpin = "";

    private NetworkChangeReceiver receiver;
    private boolean isConnected = false;
    AlertDialog dialog;
    AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        receiver = new NetworkChangeReceiver();
        registerReceiver(receiver, filter);
//
//        //Alert Dialog
        builder= new AlertDialog.Builder(this);
        View view1=getLayoutInflater().inflate(R.layout.custom_no_internet,null);
        builder.setView(view1);
        dialog=builder.create();
        dialog.setCancelable(false);

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
//        user = new User(this);
//        saved_mpin = user.getMpin_code();

        img_app_logo =findViewById(R.id.img_app_logo);
        textView = (TextView) findViewById(R.id.textView);
        textView.setText("FF QUEEN".toUpperCase());

        TextPaint paint = textView.getPaint();
        float width = paint.measureText("FF QUEEN");

        Shader textShader = new LinearGradient(0, 0, width, textView.getTextSize(),
                new int[]{
                        Color.parseColor("#2b3453"),
                        Color.parseColor("#e1c383"),
                        Color.parseColor("#2b3453"),
                        Color.parseColor("#e1c383"),
                        Color.parseColor("#2b3453"),
                }, null, Shader.TileMode.CLAMP);
        textView.getPaint().setShader(textShader);
        textView.setTextColor(Color.parseColor("#2b3453"));

        textView.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.flash_leave_now));


        Thread timer = new Thread(){

            @Override
            public void run() {

                try {
                    sleep(3000);
//                    if(!saved_mpin.equals("")){
//                        startActivity(new Intent(Splash_Activity.this,MPin.class));
//                        finish();
//                    }else {
                        startActivity(new Intent(SplashScreen.this,Login_Activity.class));
                        overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                        finish();
                  //  }

                    super.run();
                } catch (InterruptedException e){
                    e.printStackTrace();
                }

            }
        };
        timer.start();

    }

    private boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
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

                    Log.e("TAG","HEllow world");

                }

                //  networkStatus.setText("You are not connected to Internet!");
                isConnected = false;
            }
        }
    }
}