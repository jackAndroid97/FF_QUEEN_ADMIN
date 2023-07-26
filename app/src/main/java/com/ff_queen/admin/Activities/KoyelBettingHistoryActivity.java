package com.ff_queen.admin.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.MenuItem;

import com.ff_queen.admin.Fragments.KoyelAllBidHistoryFragment;
import com.ff_queen.admin.Fragments.KoyelCurrentBidHistoryFragment;
import com.ff_queen.admin.R;
import com.ff_queen.admin.databinding.ActivityKoyelBettingHistoryBinding;

import java.util.ArrayList;
import java.util.List;

public class KoyelBettingHistoryActivity extends AppCompatActivity {

    private ActivityKoyelBettingHistoryBinding binding;
    private String game_name,game_id,cat_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityKoyelBettingHistoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);

        game_name = getIntent().getStringExtra("game_name");
        game_id = getIntent().getStringExtra("game_id");
        cat_id = getIntent().getStringExtra("cat_id");

        getSupportActionBar().setTitle(game_name);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setupViewPager(binding.contentKoyelBettingHistory.viewpager);
        binding.contentKoyelBettingHistory.tabLayout.setupWithViewPager(binding.contentKoyelBettingHistory.viewpager);

    }


    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(),0);
        viewPagerAdapter.addFragment(KoyelCurrentBidHistoryFragment.getInstance(game_id, game_name,cat_id), "Current Bid History");
        viewPagerAdapter.addFragment(KoyelAllBidHistoryFragment.getInstance(game_id, game_name,cat_id), "All Bid History");
        viewPager.setAdapter(viewPagerAdapter);
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {

        private List<Fragment> fragments = new ArrayList<>();
        private List<String> fragmentTitle = new ArrayList<>();

        public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        public void addFragment(Fragment fragment, String title) {
            fragments.add(fragment);
            fragmentTitle.add(title);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitle.get(position);
        }
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

}