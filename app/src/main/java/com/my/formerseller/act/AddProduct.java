package com.my.formerseller.act;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import com.my.formerseller.R;
import com.my.formerseller.databinding.ActivityAddProductBinding;

public class AddProduct extends AppCompatActivity {

    ActivityAddProductBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_add_product);

        binding.RRBack.setOnClickListener(v -> {

            onBackPressed();

        });
    }
}