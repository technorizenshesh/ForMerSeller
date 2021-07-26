package com.my.formerseller.act;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.my.formerseller.R;
import com.my.formerseller.databinding.ActivitySignUpBinding;


public class SignUpActivity extends AppCompatActivity {

    ActivitySignUpBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up);

        SetupUI();
    }

    private void SetupUI() {
        binding.llSingin.setOnClickListener(v -> {
            startActivity(new Intent(this, Login.class));
        });
        binding.SignUp.setOnClickListener(v -> {
            startActivity(new Intent(this, VerificationActivity.class));
        });
    }
}