package com.my.formerseller.act;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.my.formerseller.R;
import com.my.formerseller.databinding.ActivityForogotPasswordBinding;


public class ForogotPassword extends AppCompatActivity {

    ActivityForogotPasswordBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this, R.layout.activity_forogot_password);

    }
}