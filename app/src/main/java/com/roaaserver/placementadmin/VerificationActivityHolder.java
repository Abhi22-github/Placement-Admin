package com.roaaserver.placementadmin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.widget.ImageButton;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.auth.User;
import com.roaaserver.placementadmin.Adpters.VerificationAdapter;
import com.roaaserver.placementadmin.StudentVerificationFragment.RequestsFragment;
import com.roaaserver.placementadmin.StudentVerificationFragment.UserFragment;

public class VerificationActivityHolder extends AppCompatActivity {

    private ImageButton backButton;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    private Fragment RequestsFragment,UserFragment;
    private VerificationAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification_holder);
        initFields();
        backButton.setOnClickListener(v -> onBackPressed());
        RequestsFragment = new RequestsFragment();
        UserFragment = new UserFragment();
        adapter = new VerificationAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        adapter.addFragment(RequestsFragment,"Requests");
        adapter.addFragment(UserFragment,"User");
        viewPager.setAdapter(adapter);

        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    protected void onStart() {
        super.onStart();
//        RequestsFragment = new RequestsFragment();
//        UserFragment = new UserFragment();
//        VerificationAdapter adapter = new VerificationAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
//        adapter.addFragment(RequestsFragment,"Requests");
//        adapter.addFragment(UserFragment,"User");
//        viewPager.setAdapter(adapter);
//        tabLayout.setupWithViewPager(viewPager);
        adapter.notifyDataSetChanged();

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initFields() {
        backButton = findViewById(R.id.back_button);
        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_pager);
    }
}