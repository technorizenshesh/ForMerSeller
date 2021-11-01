package com.my.formerseller.act;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.my.formerseller.Preference;
import com.my.formerseller.R;
import com.my.formerseller.adapter.MyProductAdapter;
import com.my.formerseller.databinding.ActivityMyProductBinding;
import com.my.formerseller.model.MyProductDataModel;
import com.my.formerseller.model.MyProductModel;
import com.my.formerseller.utils.RetrofitClients;
import com.my.formerseller.utils.SessionManager;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyProduct extends AppCompatActivity {

    MyProductAdapter mAdapter;
    private ArrayList<MyProductDataModel> modelList = new ArrayList<>();
    ActivityMyProductBinding binding;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_product);

        binding.RRBack.setOnClickListener(v -> {

            onBackPressed();

        });

        binding.RRUpdateProduct.setOnClickListener(v ->
        {
            startActivity(new Intent(MyProduct.this,UpdateProductActivity.class));

        });

        sessionManager = new SessionManager(MyProduct.this);

        if (sessionManager.isNetworkAvailable()) {

            binding.progressBar.setVisibility(View.VISIBLE);

            getProfileMethod();

        } else {

            Toast.makeText(MyProduct.this, R.string.checkInternet, Toast.LENGTH_SHORT).show();

        }
    }

    private void setAdapter(ArrayList<MyProductDataModel> modelList) {


        mAdapter = new MyProductAdapter(MyProduct.this, modelList);
        binding.recyclerMyProduct.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MyProduct.this);
        binding.recyclerMyProduct.setLayoutManager(linearLayoutManager);
        binding.recyclerMyProduct.setAdapter(mAdapter);

        mAdapter.SetOnItemClickListener(new MyProductAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, MyProductDataModel model) {

            }
        });
    }

    private void getProfileMethod() {

        String UsserId = Preference.get(MyProduct.this, Preference.KEYType_login);

        Call<MyProductModel> call = RetrofitClients
                .getInstance()
                .getApi()
                .get_product(UsserId, "All");
        call.enqueue(new Callback<MyProductModel>() {
            @Override
            public void onResponse(Call<MyProductModel> call, Response<MyProductModel> response) {

                binding.progressBar.setVisibility(View.GONE);

                MyProductModel finallyPr = response.body();

                String status = finallyPr.getStatus();

                if (status.equalsIgnoreCase("1")) {

                    modelList = (ArrayList<MyProductDataModel>) finallyPr.getResult();

                    setAdapter(modelList);

                } else {
                    binding.progressBar.setVisibility(View.GONE);
                    Toast.makeText(MyProduct.this, finallyPr.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MyProductModel> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                Toast.makeText(MyProduct.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}