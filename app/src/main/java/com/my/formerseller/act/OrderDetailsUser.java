package com.my.formerseller.act;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;

import com.my.formerseller.MainActivity;
import com.my.formerseller.R;
import com.my.formerseller.databinding.ActivityOrderDetailsUserBinding;

public class OrderDetailsUser extends AppCompatActivity {

    ActivityOrderDetailsUserBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_order_details_user);

        binding.RRBack.setOnClickListener(v -> {

            onBackPressed();
        });

        binding.txtAccept.setOnClickListener(v -> {

            startActivity(new Intent(this, UserDetails.class));

        });
    }
}