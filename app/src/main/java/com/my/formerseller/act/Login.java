package com.my.formerseller.act;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.my.formerseller.MainActivity;
import com.my.formerseller.R;
import com.my.formerseller.databinding.ActivityLoginBinding;


public class Login extends AppCompatActivity {

    ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);

        SetupUI();

    }

    private void SetupUI() {

        binding.loginID.setOnClickListener(v -> {
                startActivity(new Intent(this, MainActivity.class));
        });

        binding.llSingUp.setOnClickListener(v -> {
             startActivity(new Intent(this, SignUpActivity.class));
        });

        binding.txtForogtPassword.setOnClickListener(v -> {
                startActivity(new Intent(this, ForogotPassword.class));
        });

    }
}