package com.my.formerseller.act;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.my.formerseller.Preference;
import com.my.formerseller.R;
import com.my.formerseller.databinding.ActivityUserDetailsBinding;
import com.my.formerseller.model.ProductDetailsUser;
import com.my.formerseller.utils.RetrofitClients;
import com.my.formerseller.utils.SessionManager;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserDetails extends AppCompatActivity {

    ActivityUserDetailsBinding binding;
    private SessionManager sessionManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_user_details);

        sessionManager = new SessionManager(UserDetails.this);


        binding.RRback.setOnClickListener(v -> {
            onBackPressed();
        });

        if (sessionManager.isNetworkAvailable()) {

            binding.progressBar.setVisibility(View.VISIBLE);

            getOrderDetailsMethod();

        }else {

            Toast.makeText(UserDetails.this, R.string.checkInternet, Toast.LENGTH_SHORT).show();
        }

    }


    private void getOrderDetailsMethod() {

        String id = Preference.get(UserDetails.this, Preference.KEYType_Order_details_id);

        Call<ProductDetailsUser> call = RetrofitClients
                .getInstance()
                .getApi()
                .order_detail(id);
        call.enqueue(new Callback<ProductDetailsUser>() {
            @Override
            public void onResponse(Call<ProductDetailsUser> call, Response<ProductDetailsUser> response) {

                binding.progressBar.setVisibility(View.GONE);

                ProductDetailsUser finallyPr = response.body();

                String status = finallyPr.getStatus();

                if (status.equalsIgnoreCase("1")) {

                    binding.txtPaymentType.setText(finallyPr.getResult().getPaymentType()+" on Delivery");
                    binding.txtUserName.setText(finallyPr.getResult().getUserData().getName());
                    binding.txtOrder.setText(finallyPr.getResult().getProductCount()+" Order");

                    if(finallyPr.getResult().getUserData().getImage() !=null)
                    {
                        Glide.with(UserDetails.this).load(finallyPr.getResult().getUserData().getImage()).placeholder(R.drawable.avtar).circleCrop().into(binding.ImgUser);
                    }

                } else {
                    binding.progressBar.setVisibility(View.GONE);
                }
            }
            @Override
            public void onFailure(Call<ProductDetailsUser> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
            }
        });
    }
}