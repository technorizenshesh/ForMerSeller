package com.my.formerseller.act;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.my.formerseller.MainActivity;
import com.my.formerseller.Preference;
import com.my.formerseller.R;
import com.my.formerseller.adapter.ProductDetailsAdapter;
import com.my.formerseller.adapter.SubProductRecyclerViewAdapter;
import com.my.formerseller.databinding.ActivityOrderDetailsUserBinding;
import com.my.formerseller.model.GetOrderNotification;
import com.my.formerseller.model.ProductDetailsUser;
import com.my.formerseller.utils.RetrofitClients;
import com.my.formerseller.utils.SessionManager;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderDetailsUser extends AppCompatActivity {

    ActivityOrderDetailsUserBinding binding;
    ProductDetailsAdapter mAdapter;
    private ArrayList<ProductDetailsUser.Result.Product> modelList = new ArrayList<>();
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_order_details_user);

        sessionManager = new SessionManager(OrderDetailsUser.this);

        binding.RRBack.setOnClickListener(v -> {

            onBackPressed();

        });

        binding.txtAccept.setOnClickListener(v -> {



        });

        binding.RRUserDetails.setOnClickListener(v -> {

            startActivity(new Intent(this, UserDetails.class));
        });


        if (sessionManager.isNetworkAvailable()) {

            binding.progressBar.setVisibility(View.VISIBLE);

            getOrderDetailsMethod();

        }else {
            Toast.makeText(OrderDetailsUser.this, R.string.checkInternet, Toast.LENGTH_SHORT).show();
        }
    }

    private void setAdapter(ArrayList<ProductDetailsUser.Result.Product> modelList) {

        mAdapter = new ProductDetailsAdapter(OrderDetailsUser.this,modelList);
        binding.recyclerProductDetails.setHasFixedSize(true);
        // use a linear layout manager
        //  binding.recyclerSearch.setLayoutManager(new GridLayoutManager(this, 2));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(OrderDetailsUser.this);
        binding.recyclerProductDetails.setLayoutManager(linearLayoutManager);
        binding.recyclerProductDetails.setAdapter(mAdapter);

        mAdapter.SetOnItemClickListener(new ProductDetailsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, ProductDetailsUser.Result.Product model) {

            }
        });

    }


    private void getOrderDetailsMethod() {

        String id = Preference.get(OrderDetailsUser.this, Preference.KEYType_Order_details_id);

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

                    binding.txtUserName.setText(finallyPr.getResult().getUserData().getName());

                    modelList = (ArrayList<ProductDetailsUser.Result.Product>) finallyPr.getResult().getProductList();

                    setAdapter(modelList);

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