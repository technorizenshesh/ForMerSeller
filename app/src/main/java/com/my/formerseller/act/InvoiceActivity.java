package com.my.formerseller.act;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;

import com.my.formerseller.R;
import com.my.formerseller.adapter.InvoiceAdapter;
import com.my.formerseller.adapter.MyProductAdapter;
import com.my.formerseller.databinding.ActivityInvoiceBinding;
import com.my.formerseller.model.HomeModel;

import java.util.ArrayList;

public class InvoiceActivity extends AppCompatActivity {

    InvoiceAdapter mAdapter;
    private ArrayList<HomeModel> modelList = new ArrayList<>();
    ActivityInvoiceBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_invoice);

        setAdapter();
    }

    private void setAdapter() {

        this.modelList.add(new HomeModel("Corn"));
        this.modelList.add(new HomeModel("Tomotoes"));


        mAdapter = new InvoiceAdapter(InvoiceActivity.this,modelList);
        binding.recyclerInvoices.setHasFixedSize(true);
        // use a linear layout manager
        //  binding.recyclerSearch.setLayoutManager(new GridLayoutManager(this, 2));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(InvoiceActivity.this);
        binding.recyclerInvoices.setLayoutManager(linearLayoutManager);
        binding.recyclerInvoices.setAdapter(mAdapter);

        mAdapter.SetOnItemClickListener(new InvoiceAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, HomeModel model) {

            }
        });
    }
}