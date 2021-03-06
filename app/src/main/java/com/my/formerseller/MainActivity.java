package com.my.formerseller;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.my.formerseller.Fragment.ChatFragment;
import com.my.formerseller.Fragment.HomeFragment;
import com.my.formerseller.Fragment.ProfileFragment;
import com.my.formerseller.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    Fragment fragment;

    boolean doubleBackToExitPressedOnce = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_main);

        binding.RRHome.setOnClickListener(v -> {

            binding.imgHome.setImageResource(R.drawable.home);
            binding.imgChat.setImageResource(R.drawable.chat);
            binding.imgProfile.setImageResource(R.drawable.profile);

            binding.txtHome.setTextColor(getResources().getColor(R.color.purple_200));
            binding.txtchat.setTextColor(getResources().getColor(R.color.natural_gray));
            binding.txtprofile.setTextColor(getResources().getColor(R.color.natural_gray));


            fragment = new HomeFragment();
            loadFragment(fragment);
        });

        binding.RRChat.setOnClickListener(v -> {

            binding.txtHome.setTextColor(getResources().getColor(R.color.natural_gray));
            binding.txtchat.setTextColor(getResources().getColor(R.color.purple_200));
            binding.txtprofile.setTextColor(getResources().getColor(R.color.natural_gray));



            binding.imgHome.setImageResource(R.drawable.home_gray);
            binding.imgChat.setImageResource(R.drawable.chat_green);
            binding.imgProfile.setImageResource(R.drawable.profile);


            fragment = new ChatFragment();
            loadFragment(fragment);
        });

        binding.RRProfile.setOnClickListener(v -> {


            binding.txtHome.setTextColor(getResources().getColor(R.color.natural_gray));
            binding.txtchat.setTextColor(getResources().getColor(R.color.natural_gray));
            binding.txtprofile.setTextColor(getResources().getColor(R.color.purple_200));

            binding.imgHome.setImageResource(R.drawable.home_gray);
            binding.imgChat.setImageResource(R.drawable.chat);
            binding.imgProfile.setImageResource(R.drawable.profile_green);

            fragment = new ProfileFragment();
            loadFragment(fragment);
        });

        fragment = new HomeFragment();
        loadFragment(fragment);
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            finishAffinity();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        binding.imgHome.setImageResource(R.drawable.home);
        binding.imgChat.setImageResource(R.drawable.chat);
        binding.imgProfile.setImageResource(R.drawable.profile);

        binding.txtHome.setTextColor(getResources().getColor(R.color.purple_200));
        binding.txtchat.setTextColor(getResources().getColor(R.color.natural_gray));
        binding.txtprofile.setTextColor(getResources().getColor(R.color.natural_gray));

        fragment = new HomeFragment();
        loadFragment(fragment);

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
    public void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction =getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_homeContainer, fragment);
        transaction.addToBackStack("home");
        transaction.commit();
    }
}