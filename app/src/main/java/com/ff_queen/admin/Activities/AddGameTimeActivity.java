package com.ff_queen.admin.Activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.ff_queen.admin.Adapter.GameAdapter;
import com.ff_queen.admin.Interfaces.MyInterface;
import com.ff_queen.admin.Models.Game_Model;
import com.ff_queen.admin.R;
import com.ff_queen.admin.Utilities.ApiClient;
import com.ff_queen.admin.Utilities.ProgressUtils;
import com.ff_queen.admin.ViewModel.ApiResponse;
import com.ff_queen.admin.databinding.ActivityAddGameBinding;
import com.google.android.material.button.MaterialButton;
import com.weiwangcn.betterspinner.library.BetterSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddGameTimeActivity extends AppCompatActivity {


    private ApiResponse apiResponse;
    Bitmap bitmap1=null;
    private String [] sample={"No User"};
    public static String type="";
    private MyInterface myInterface;
    private final int MY_PERMISSIONS_REQUEST_USE_CAMERA = 0x00AF;
    MaterialButton add_button;
    BetterSpinner game_name;
    TextView start_time,end_time;

    String game_id;
    List<Game_Model> game_models = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_game_time);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        myInterface = ApiClient.getApiClient().create(MyInterface.class);
        getSupportActionBar().setTitle("Add Game Time");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        add_button=findViewById(R.id.btn_add);
        game_name=findViewById(R.id.spinner_type);
        start_time=findViewById(R.id.s_time);
        end_time=findViewById(R.id.e_time);

        ArrayAdapter<String> adapter_sup_dis = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, sample);
        game_name.setAdapter(adapter_sup_dis);

        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(game_id.trim().equals("")){
                    Toast.makeText(AddGameTimeActivity.this, "Choose your game", Toast.LENGTH_SHORT).show();
                }
                else if(start_time.getText().toString().trim().equals("")){
                    start_time.setError("Choose start time");
                }
                else if(end_time.getText().toString().trim().equals("")){
                    end_time.setError("Choose end time");
                }else{
                    Call<String> call=myInterface.Add_Game_time(game_id,start_time.getText().toString(),end_time.getText().toString());

                    ProgressUtils.showLoadingDialog(AddGameTimeActivity.this);
                    call.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            if(response.body()!=null){
                                String res=response.body();
                                try {
                                    JSONObject object= new JSONObject(res);
                                    if (object.getString("rec").trim().equals("1")){

                                        Toast.makeText(AddGameTimeActivity.this, "Game time added", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(AddGameTimeActivity.this,MainActivity.class));
                                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                                        finish();
                                        ProgressUtils.cancelLoading();
                                    }else {
                                        Toast.makeText(AddGameTimeActivity.this, "Game time not added", Toast.LENGTH_SHORT).show();
                                        ProgressUtils.cancelLoading();
                                    }
                                } catch (JSONException e) {
                                    ProgressUtils.cancelLoading();
                                    e.printStackTrace();
                                }

                            }else {
                                ProgressUtils.cancelLoading();
                                Toast.makeText(AddGameTimeActivity.this, "No Response", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            ProgressUtils.cancelLoading();
                            Toast.makeText(AddGameTimeActivity.this, "Slow network connection", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
        game_name.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
               game_id=game_models.get(i).getId();
               // Toast.makeText(AddGameTimeActivity.this, ""+game_id, Toast.LENGTH_SHORT).show();
            }
        });
        start_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // on below line we are getting the
                // instance of our calendar.
                final Calendar c = Calendar.getInstance();

                // on below line we are getting our hour, minute.
                int hour = c.get(Calendar.HOUR_OF_DAY);
                int minute = c.get(Calendar.MINUTE);

                // on below line we are initializing our Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(AddGameTimeActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {
                                // on below line we are setting selected time
                                // in our text view.

                                String time=hourOfDay + ":" + minute;
                                try {
                                    final SimpleDateFormat sdf = new SimpleDateFormat("H:mm");
                                    final Date dateObj = sdf.parse(time);
                                   // System.out.println(dateObj);

                                    start_time.setText(new SimpleDateFormat("K:mm a").format(dateObj));
                                   // System.out.println();
                                } catch (final ParseException e) {
                                    e.printStackTrace();
                                }


                            }
                        }, hour, minute, false);
                // at last we are calling show to
                // display our time picker dialog.
                timePickerDialog.show();
            }
        });
        end_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // on below line we are getting the
                // instance of our calendar.
                final Calendar c = Calendar.getInstance();

                // on below line we are getting our hour, minute.
                int hour = c.get(Calendar.HOUR_OF_DAY);
                int minute = c.get(Calendar.MINUTE);

                // on below line we are initializing our Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(AddGameTimeActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {
                                // on below line we are setting selected time
                                // in our text view.
                                String time=hourOfDay + ":" + minute;
                                try {
                                    final SimpleDateFormat sdf = new SimpleDateFormat("H:mm");
                                    final Date dateObj = sdf.parse(time);
                                    // System.out.println(dateObj);

                                    end_time.setText(new SimpleDateFormat("K:mm a").format(dateObj));
                                    // System.out.println();
                                } catch (final ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, hour, minute, false);
                // at last we are calling show to
                // display our time picker dialog.
                timePickerDialog.show();
            }
        });
        fetchGame();

    }
    private void fetchGame() {

        Call<String> call = myInterface.fetch_active_game();
        ProgressUtils.showLoadingDialog(AddGameTimeActivity.this);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.body() != null) {
                    String res = response.body();
                    try {
                        JSONArray jsonArray = new JSONArray(res);
                        if (jsonArray.length() == 0) {
                            ProgressUtils.cancelLoading();

                        } else {
                            game_models.clear();
                            for (int i=0; i<jsonArray.length(); i++)
                            {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String game_id = jsonObject.getString("id");
                                String game_name = jsonObject.getString("game_name");
                                String game_image = jsonObject.getString("image");
                                Game_Model model = new Game_Model(game_id,game_name,game_image);
                                game_models.add(model);
                            }
                            ArrayAdapter<Game_Model> adapter_sup_dis = new ArrayAdapter<Game_Model>(AddGameTimeActivity.this,
                                    android.R.layout.simple_dropdown_item_1line, game_models);
                            game_name.setAdapter(adapter_sup_dis);
                            ProgressUtils.cancelLoading();

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        ProgressUtils.cancelLoading();
                        Toast.makeText(AddGameTimeActivity.this, "" + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(AddGameTimeActivity.this, "No Response", Toast.LENGTH_SHORT).show();
                    ProgressUtils.cancelLoading();
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                ProgressUtils.cancelLoading();
                Toast.makeText(AddGameTimeActivity.this, "Error", Toast.LENGTH_SHORT).show();
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