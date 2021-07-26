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

import com.my.formerseller.R;
import com.my.formerseller.act.AddProduct;
import com.my.formerseller.act.EditProfile;
import com.my.formerseller.act.InvoiceActivity;
import com.my.formerseller.act.MyProduct;
import com.my.formerseller.act.OrderHistory;
import com.my.formerseller.databinding.FragmentProfileBinding;


public class ProfileFragment extends Fragment {

    private Fragment fragment;

    FragmentProfileBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false);

        binding.RREditProfile.setOnClickListener(v -> {

            startActivity(new Intent(getActivity(), EditProfile.class));

        });


        binding.RROrderHistory.setOnClickListener(v -> {
            //startActivity(new Intent(getActivity(), OrderHistory.class));
        });

        binding.RRAddProduct.setOnClickListener(v -> {

            startActivity(new Intent(getActivity(), AddProduct.class));
        });

        binding.RRMyProduct.setOnClickListener(v -> {

            startActivity(new Intent(getActivity(), MyProduct.class));
        });
        binding.RROrderHistory.setOnClickListener(v -> {

            startActivity(new Intent(getActivity(), OrderHistory.class));
        });

        binding.RRInvoice.setOnClickListener(v -> {

            startActivity(new Intent(getActivity(), InvoiceActivity.class));
        });


        return binding.getRoot();
    }


    public void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_homeContainer, fragment);
        transaction.addToBackStack("home");
        transaction.commit();
    }


}