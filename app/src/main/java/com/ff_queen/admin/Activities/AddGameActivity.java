package com.ff_queen.admin.Activities;

import android.Manifest;
import android.annotation.SuppressLint;
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
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.ff_queen.admin.Interfaces.MyInterface;
import com.ff_queen.admin.Models.View_User_Model;
import com.ff_queen.admin.R;
import com.ff_queen.admin.Utilities.ApiClient;
import com.ff_queen.admin.Utilities.ProgressUtils;
import com.ff_queen.admin.ViewModel.ApiResponse;
import com.ff_queen.admin.databinding.ActivityAddGameBinding;
import com.ff_queen.admin.databinding.ActivityAddSuperDistributorBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddGameActivity extends AppCompatActivity {

    public static ActivityAddGameBinding binding;
    private ApiResponse apiResponse;
    Bitmap bitmap1=null;
    private String [] sample={"No User"};
    public static String type="";
    private MyInterface myInterface;
    private final int MY_PERMISSIONS_REQUEST_USE_CAMERA = 0x00AF;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddGameBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        myInterface = ApiClient.getApiClient().create(MyInterface.class);
        getSupportActionBar().setTitle("Add Game");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        binding.contentAddGame.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String img=imageToString(bitmap1);
                String name=binding.contentAddGame.name.getText().toString();
                String b_name=binding.contentAddGame.nameBengali.getText().toString();
                if(binding.contentAddGame.name.getText().toString().trim().equals("")){
                    binding.contentAddGame.name.setError("Enter Game Name");
                }
                else if(binding.contentAddGame.nameBengali.getText().toString().trim().equals("")){
                    binding.contentAddGame.nameBengali.setError("Enter Game Bengali Name");
                }
                else if(img.equals("")){
                    Toast.makeText(AddGameActivity.this, "Please choose game image", Toast.LENGTH_SHORT).show();
                }else{
                    Call<String> call=myInterface.Add_Game(name,b_name,img);

                    ProgressUtils.showLoadingDialog(AddGameActivity.this);
                    call.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            if(response.body()!=null){
                                String res=response.body();
                                try {
                                    JSONObject object= new JSONObject(res);
                                    if (object.getString("rec").trim().equals("1")){

                                        Toast.makeText(AddGameActivity.this, "Game added", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(AddGameActivity.this,MainActivity.class));
                                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                                        finish();
                                        ProgressUtils.cancelLoading();
                                    }else {
                                        Toast.makeText(AddGameActivity.this, "Game not added", Toast.LENGTH_SHORT).show();
                                        ProgressUtils.cancelLoading();
                                    }
                                } catch (JSONException e) {
                                    ProgressUtils.cancelLoading();
                                    e.printStackTrace();
                                }

                            }else {
                                ProgressUtils.cancelLoading();
                                Toast.makeText(AddGameActivity.this, "No Response", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            ProgressUtils.cancelLoading();
                            Toast.makeText(AddGameActivity.this, "Slow network connection", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
        binding.contentAddGame.editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkCameraPermission();
            }
        });

    }
    private void checkCameraPermission(){
        if (ContextCompat.checkSelfPermission(AddGameActivity.this, Manifest.permission.CAMERA ) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(AddGameActivity.this, "Permission not available requesting permission", Toast.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(AddGameActivity.this,
                    new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_USE_CAMERA);
        } else {
            //Toast.makeText(this, "Permission has already granted", Toast.LENGTH_SHORT).show();
            Intent intent = new   Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, 2);
        }
    }
    @SuppressLint("MissingSuperCall")
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_USE_CAMERA: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new   Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);
                } else {
                    Toast.makeText(AddGameActivity.this, "Permission denied!", Toast.LENGTH_SHORT).show();
                }
                return;
            }


        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2 && resultCode == RESULT_OK && data != null && data.getData() != null) {

            //onSelectFromGalleryResult(data);
            Uri my_uri = data.getData();

            try {
                bitmap1 = MediaStore.Images.Media.getBitmap(getContentResolver(), my_uri);
                binding.contentAddGame.profileImg.setImageBitmap(bitmap1);
              //  update_profile_image(bitmap1);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String imageToString(Bitmap bitmap) {
        ByteArrayOutputStream bt = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bt);
        byte[] imgBytes = bt.toByteArray();
        String encodeImage = Base64.encodeToString(imgBytes, Base64.DEFAULT);

        return encodeImage;
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