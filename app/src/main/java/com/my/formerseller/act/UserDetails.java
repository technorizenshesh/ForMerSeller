package com.my.formerseller.act;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import com.my.formerseller.R;
import com.my.formerseller.databinding.ActivityUserDetailsBinding;

public class UserDetails extends AppCompatActivity {

    ActivityUserDetailsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_user_details);

        binding.RRback.setOnClickListener(v -> {

            onBackPressed();

        });

    }
}