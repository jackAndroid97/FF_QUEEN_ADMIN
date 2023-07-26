package com.ff_queen.admin.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.ff_queen.admin.Fragments.EditProfileFragment;
import com.ff_queen.admin.Fragments.HomeFragment;
import com.ff_queen.admin.R;

public class MainActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {


    private FrameLayout mainFrameLayout;
    private BottomNavigationView mainNavigationBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
      // getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.primary_dark)));

        mainFrameLayout   = findViewById(R.id.main_frame_layout);
        mainNavigationBar = findViewById(R.id.bottom_nav_view);

        replaceFragment(new HomeFragment());

        mainNavigationBar.setOnItemSelectedListener(this);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.nav_home:
                replaceFragment(new HomeFragment());
                break;

            case R.id.nav_edit_profile:
                replaceFragment(new EditProfileFragment());
                break;
        }

        return true;
    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_frame_layout,fragment);
        fragmentTransaction.commit();
    }
}