package com.ff_queen.admin.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ff_queen.admin.Interfaces.MyInterface;
import com.ff_queen.admin.R;
import com.ff_queen.admin.Utilities.ApiClient;
import com.ff_queen.admin.Utilities.ProgressUtils;
import com.ff_queen.admin.databinding.ActivityBannerBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BannerActivity extends AppCompatActivity {

    ActivityBannerBinding binding;
    private MyInterface myInterface;
    private static final int PICK_IMAGE_1 = 100;
    private static final int PICK_IMAGE_2 = 101;
    private static final int PICK_IMAGE_3 = 102;
    private static final int PICK_IMAGE_4 = 103;
    private int STORAGE_PERMISSION = 105;
    Uri banner_uri_1, banner_uri_2, banner_uri_3, banner_uri_4;
    Bitmap bitmap_1, bitmap_2, bitmap_3, bitmap_4;
    String temp, hold_gender="", banner_id_1, banner_id_2, banner_id_3, banner_id_4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBannerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources()
                .getColor(R.color.primary_dark)));
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Banners");


        myInterface = ApiClient.getApiClient().create(MyInterface.class);

        fetchBannerImage();

        binding.contentBanner.btnBanner1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ContextCompat.checkSelfPermission(BannerActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
                {

                    Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                    startActivityForResult(gallery.createChooser(gallery,"Select Picture"),PICK_IMAGE_1);

                }
                else
                {
                    requestStroagePermission();
                }

            }
        });


        binding.contentBanner.btnBanner2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ContextCompat.checkSelfPermission(BannerActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
                {

                    Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                    startActivityForResult(gallery.createChooser(gallery,"Select Picture"),PICK_IMAGE_2);

                }
                else
                {
                    requestStroagePermission();
                }
            }
        });


        binding.contentBanner.btnBanner3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ContextCompat.checkSelfPermission(BannerActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
                {

                    Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                    startActivityForResult(gallery.createChooser(gallery,"Select Picture"),PICK_IMAGE_3);

                }
                else
                {
                    requestStroagePermission();
                }
            }
        });


        binding.contentBanner.btnBanner4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ContextCompat.checkSelfPermission(BannerActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
                {

                    Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                    startActivityForResult(gallery.createChooser(gallery,"Select Picture"),PICK_IMAGE_4);

                }
                else
                {
                    requestStroagePermission();
                }
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


    private void requestStroagePermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(BannerActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE))
        {

            new AlertDialog.Builder(BannerActivity.this)
                    .setMessage("Permission needed")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            ActivityCompat.requestPermissions(BannerActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},STORAGE_PERMISSION);


                        }
                    })
                    .setNegativeButton("cancel",null).create().show();

        }
        else
        {
            ActivityCompat.requestPermissions(BannerActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},STORAGE_PERMISSION);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(BannerActivity.this, "Permission Granted", Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(BannerActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_1 && resultCode == RESULT_OK) {
            banner_uri_1 = data.getData();

            try {

                bitmap_1 = MediaStore.Images.Media.getBitmap(BannerActivity.this.getContentResolver(), banner_uri_1);
                binding.contentBanner.imgBanner1.setImageBitmap(bitmap_1);
                String img = bitMapToString(bitmap_1);
                updateBannerImage(banner_id_1,img);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if (requestCode == PICK_IMAGE_2 && resultCode == RESULT_OK) {
            banner_uri_2 = data.getData();

            try {

                bitmap_2 = MediaStore.Images.Media.getBitmap(BannerActivity.this.getContentResolver(), banner_uri_2);
                binding.contentBanner.imgBanner2.setImageBitmap(bitmap_2);
                String img = bitMapToString(bitmap_2);
                updateBannerImage(banner_id_2,img);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if (requestCode == PICK_IMAGE_3 && resultCode == RESULT_OK) {
            banner_uri_3 = data.getData();

            try {

                bitmap_3 = MediaStore.Images.Media.getBitmap(BannerActivity.this.getContentResolver(), banner_uri_3);
                binding.contentBanner.imgBanner3.setImageBitmap(bitmap_3);
                String img = bitMapToString(bitmap_3);
                updateBannerImage(banner_id_3,img);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if (requestCode == PICK_IMAGE_4 && resultCode == RESULT_OK) {
            banner_uri_4 = data.getData();

            try {

                bitmap_4 = MediaStore.Images.Media.getBitmap(BannerActivity.this.getContentResolver(), banner_uri_4);
                binding.contentBanner.imgBanner4.setImageBitmap(bitmap_4);
                String img = bitMapToString(bitmap_4);
                updateBannerImage(banner_id_4,img);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }


    private void fetchBannerImage()
    {
        Call<String> call = myInterface.fetch_banner_image();
        ProgressUtils.showLoadingDialog(BannerActivity.this);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                String res = response.body();

                if (res == null) {
                    Toast.makeText(BannerActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    ProgressUtils.cancelLoading();
                }
                 else
                 {

                     try {
                         JSONArray jsonArray = new JSONArray(res);

                         if (jsonArray.length() ==0)
                         {
                             ProgressUtils.cancelLoading();
                         }
                         else
                         {
                             JSONObject jsonObject_1 = jsonArray.getJSONObject(0);
                             JSONObject jsonObject_2 = jsonArray.getJSONObject(1);
                             JSONObject jsonObject_3 = jsonArray.getJSONObject(2);
                             JSONObject jsonObject_4 = jsonArray.getJSONObject(3);

                             banner_id_1 = jsonObject_1.getString("id");
                             banner_id_2 = jsonObject_2.getString("id");
                             banner_id_3 = jsonObject_3.getString("id");
                             banner_id_4 = jsonObject_4.getString("id");

                             Glide.with(BannerActivity.this).load(jsonObject_1.getString("image")).into(binding.contentBanner.imgBanner1);
                             Glide.with(BannerActivity.this).load(jsonObject_2.getString("image")).into(binding.contentBanner.imgBanner2);
                             Glide.with(BannerActivity.this).load(jsonObject_3.getString("image")).into(binding.contentBanner.imgBanner3);
                             Glide.with(BannerActivity.this).load(jsonObject_4.getString("image")).into(binding.contentBanner.imgBanner4);

                             ProgressUtils.cancelLoading();

                         }

                     } catch (JSONException e) {

                         Toast.makeText(BannerActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                         ProgressUtils.cancelLoading();
                     }
                 }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

                Toast.makeText(BannerActivity.this, "Slow Network", Toast.LENGTH_SHORT).show();
                ProgressUtils.cancelLoading();
            }
        });
    }


    private void updateBannerImage(String id, String image)
    {
        Call<String> call = myInterface.update_banner_image(id,image);
        ProgressUtils.showLoadingDialog(BannerActivity.this);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                String res = response.body();
                if (res == null) {
                    Toast.makeText(BannerActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    ProgressUtils.cancelLoading();
                } else {

                    try {
                        JSONObject jsonObject = new JSONObject(res);

                        if (jsonObject.getString("rec").equals("0"))
                        {
                            Toast.makeText(BannerActivity.this, "Not Upload", Toast.LENGTH_SHORT).show();
                            ProgressUtils.cancelLoading();
                        }
                        else
                        {
                            Toast.makeText(BannerActivity.this, "Successfully Upload", Toast.LENGTH_SHORT).show();
                            fetchBannerImage();
                            ProgressUtils.cancelLoading();

                        }

                    } catch (JSONException e) {

                        Toast.makeText(BannerActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                        ProgressUtils.cancelLoading();
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

                Toast.makeText(BannerActivity.this, "Slow Network", Toast.LENGTH_SHORT).show();
                ProgressUtils.cancelLoading();
            }
        });
    }


    public String bitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100, baos);
        byte [] b=baos.toByteArray();
        temp= Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}