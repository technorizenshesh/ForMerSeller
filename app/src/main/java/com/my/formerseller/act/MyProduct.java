package com.my.formerseller.act;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;

import com.my.formerseller.R;
import com.my.formerseller.adapter.MyProductAdapter;
import com.my.formerseller.adapter.NotificationAdapter;
import com.my.formerseller.databinding.ActivityMyProductBinding;
import com.my.formerseller.model.HomeModel;

import java.util.ArrayList;

public class MyProduct extends AppCompatActivity {

    MyProductAdapter mAdapter;
    private ArrayList<HomeModel> modelList = new ArrayList<>();
    ActivityMyProductBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_my_product);

        setAdapter();
    }

    private void setAdapter() {

        this.modelList.add(new HomeModel("Corn"));
        this.modelList.add(new HomeModel("Tomotoes"));


        mAdapter = new MyProductAdapter(MyProduct.this,modelList);
        binding.recyclerMyProduct.setHasFixedSize(true);
        // use a linear layout manager
        //  binding.recyclerSearch.setLayoutManager(new GridLayoutManager(this, 2));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MyProduct.this);
        binding.recyclerMyProduct.setLayoutManager(linearLayoutManager);
        binding.recyclerMyProduct.setAdapter(mAdapter);

        mAdapter.SetOnItemClickListener(new MyProductAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, HomeModel model) {

            }
        });
    }
}