package com.my.formerseller.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.my.formerseller.MainActivity;
import com.my.formerseller.R;
import com.my.formerseller.act.Notification;
import com.my.formerseller.act.OrderDetailsUser;
import com.my.formerseller.adapter.SubProductRecyclerViewAdapter;
import com.my.formerseller.databinding.FragmentHomeBinding;
import com.my.formerseller.model.HomeModel;

import java.util.ArrayList;


public class HomeFragment extends Fragment {

    private Fragment fragment;

    FragmentHomeBinding binding;
    SubProductRecyclerViewAdapter mAdapter;
    private ArrayList<HomeModel> modelList = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);

        binding.RRNotification.setOnClickListener(v -> {

            startActivity(new Intent(getActivity(), Notification.class));


        });


        setAdapter();

        return binding.getRoot();

    }

    private void setAdapter() {

        this.modelList.add(new HomeModel("Corn"));
        this.modelList.add(new HomeModel("Tomotoes"));
        this.modelList.add(new HomeModel("Cassava"));
        this.modelList.add(new HomeModel("Rice"));
        this.modelList.add(new HomeModel("Soyabeans"));
        this.modelList.add(new HomeModel("Cocoa"));

        mAdapter = new SubProductRecyclerViewAdapter(getActivity(),modelList);
        binding.recyclerSearch.setHasFixedSize(true);
        // use a linear layout manager
        //  binding.recyclerSearch.setLayoutManager(new GridLayoutManager(this, 2));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        binding.recyclerSearch.setLayoutManager(linearLayoutManager);
        binding.recyclerSearch.setAdapter(mAdapter);

        mAdapter.SetOnItemClickListener(new SubProductRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, HomeModel model) {

                startActivity(new Intent(getActivity(), OrderDetailsUser.class));

            }
        });
    }


    public void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_homeContainer, fragment);
        transaction.addToBackStack("home");
        transaction.commit();
    }


}