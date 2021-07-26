package com.my.formerseller.act;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.my.formerseller.R;
import com.my.formerseller.databinding.ActivityEditProfileBinding;


public class EditProfile extends AppCompatActivity {

    ActivityEditProfileBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this, R.layout.activity_edit_profile);

        binding.RRBack.setOnClickListener(v -> {
            onBackPressed();
        });
    }
}