package com.my.formerseller.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.my.formerseller.MainActivity;
import com.my.formerseller.Preference;
import com.my.formerseller.R;
import com.my.formerseller.act.AddProduct;
import com.my.formerseller.act.EditProfile;
import com.my.formerseller.act.InvoiceActivity;
import com.my.formerseller.act.Login;
import com.my.formerseller.act.MyProduct;
import com.my.formerseller.act.OrderHistory;
import com.my.formerseller.databinding.FragmentProfileBinding;
import com.my.formerseller.model.LoginModel;
import com.my.formerseller.utils.RetrofitClients;
import com.my.formerseller.utils.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ProfileFragment extends Fragment {

    private Fragment fragment;
    private SessionManager sessionManager;
    FragmentProfileBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false);

        binding.RREditProfile.setOnClickListener(v -> {

            startActivity(new Intent(getActivity(), EditProfile.class));

        });
        binding.RRLogout.setOnClickListener(v -> {

            sessionManager.logoutUser();
            Preference.clearPreference(getActivity());
            startActivity(new Intent(getActivity(), Login.class));
            getActivity().finish();

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

        sessionManager = new SessionManager(getActivity());

        if (sessionManager.isNetworkAvailable()) {

            binding.progressBar.setVisibility(View.VISIBLE);

            getProfileMethod();

        }else {
            Toast.makeText(getActivity(), R.string.checkInternet, Toast.LENGTH_SHORT).show();
        }


        return binding.getRoot();
    }

    private void getProfileMethod(){

       String UsserId= Preference.get(getActivity(),Preference.KEYType_login);

        Log.e("User_id -----",""+UsserId);

        Call<LoginModel> call = RetrofitClients
                .getInstance()
                .getApi()
                .get_profile(UsserId);

        call.enqueue(new Callback<LoginModel>() {
            @Override
            public void onResponse(Call<LoginModel> call, Response<LoginModel> response) {

                binding.progressBar.setVisibility(View.GONE);

                LoginModel finallyPr = response.body();

                String status = finallyPr.getStatus();

                if (status.equalsIgnoreCase("1")) {

                    binding.txtName.setText(finallyPr.getResult().getName());
                    binding.txtEmail.setText(finallyPr.getResult().getEmail());

                    Glide.with(getActivity()).load(finallyPr.getResult().getImage()).circleCrop().into(binding.imgUserProfile);

                } else {
                    binding.progressBar.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), finallyPr.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<LoginModel> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
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