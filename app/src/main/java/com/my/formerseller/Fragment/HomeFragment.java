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
import androidx.recyclerview.widget.LinearLayoutManager;

import com.my.formerseller.MainActivity;
import com.my.formerseller.Preference;
import com.my.formerseller.R;
import com.my.formerseller.act.Login;
import com.my.formerseller.act.MyProduct;
import com.my.formerseller.act.Notification;
import com.my.formerseller.act.OrderDetailsUser;
import com.my.formerseller.adapter.SubProductRecyclerViewAdapter;
import com.my.formerseller.databinding.FragmentHomeBinding;
import com.my.formerseller.model.GetOrderNotification;
import com.my.formerseller.model.HomeModel;
import com.my.formerseller.model.MyProductDataModel;
import com.my.formerseller.model.MyProductModel;
import com.my.formerseller.updated_orderSttusListener;
import com.my.formerseller.utils.RetrofitClients;
import com.my.formerseller.utils.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeFragment extends Fragment implements updated_orderSttusListener {

    private Fragment fragment;
    private SessionManager sessionManager;
    FragmentHomeBinding binding;
    SubProductRecyclerViewAdapter mAdapter;
    private ArrayList<GetOrderNotification.Result> modelList = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);

        sessionManager = new SessionManager(getActivity());


        binding.RRNotification.setOnClickListener(v -> {

            startActivity(new Intent(getActivity(), Notification.class));

        });

        binding.RRPending.setOnClickListener(v -> {

            binding.txtPending.setTextColor(getResources().getColor(R.color.white));
            binding.txtAccept.setTextColor(getResources().getColor(R.color.white));
            binding.txttransit.setTextColor(getResources().getColor(R.color.white));
            binding.txtCancel.setTextColor(getResources().getColor(R.color.white));

            binding.RRPending.setBackgroundResource(R.color.purple_200);
            binding.RRAccept.setBackgroundResource(R.color.black);
            binding.RRIntrasit.setBackgroundResource(R.color.black);
            binding.RRCancel.setBackgroundResource(R.color.black);

            if (sessionManager.isNetworkAvailable()) {

                binding.progressBar.setVisibility(View.VISIBLE);

                getNotificationMethod("Pending");

            }else {
                Toast.makeText(getActivity(), R.string.checkInternet, Toast.LENGTH_SHORT).show();
            }
        });

        binding.RRAccept.setOnClickListener(v -> {

            binding.txtPending.setTextColor(getResources().getColor(R.color.white));
            binding.txtAccept.setTextColor(getResources().getColor(R.color.white));
            binding.txttransit.setTextColor(getResources().getColor(R.color.white));
            binding.txtCancel.setTextColor(getResources().getColor(R.color.white));

            binding.RRPending.setBackgroundResource(R.color.black);
            binding.RRAccept.setBackgroundResource(R.color.purple_200);
            binding.RRIntrasit.setBackgroundResource(R.color.black);
            binding.RRCancel.setBackgroundResource(R.color.black);

            if (sessionManager.isNetworkAvailable()) {

                binding.progressBar.setVisibility(View.VISIBLE);

                getNotificationMethod("Accepted");

            }else {
                Toast.makeText(getActivity(), R.string.checkInternet, Toast.LENGTH_SHORT).show();
            }

        });

        binding.RRIntrasit.setOnClickListener(v -> {

            binding.txtPending.setTextColor(getResources().getColor(R.color.white));
            binding.txtAccept.setTextColor(getResources().getColor(R.color.white));
            binding.txttransit.setTextColor(getResources().getColor(R.color.white));
            binding.txtCancel.setTextColor(getResources().getColor(R.color.white));


            binding.RRPending.setBackgroundResource(R.color.black);
            binding.RRAccept.setBackgroundResource(R.color.black);
            binding.RRIntrasit.setBackgroundResource(R.color.purple_200);
            binding.RRCancel.setBackgroundResource(R.color.black);


            if (sessionManager.isNetworkAvailable()) {

                binding.progressBar.setVisibility(View.VISIBLE);

                getNotificationMethod("Transit");

            }else {
                Toast.makeText(getActivity(), R.string.checkInternet, Toast.LENGTH_SHORT).show();
            }

        });

        binding.RRCancel.setOnClickListener(v -> {

            binding.txtPending.setTextColor(getResources().getColor(R.color.white));
            binding.txtAccept.setTextColor(getResources().getColor(R.color.white));
            binding.txttransit.setTextColor(getResources().getColor(R.color.white));
            binding.txtCancel.setTextColor(getResources().getColor(R.color.white));

            binding.RRPending.setBackgroundResource(R.color.black);
            binding.RRAccept.setBackgroundResource(R.color.black);
            binding.RRIntrasit.setBackgroundResource(R.color.black);
            binding.RRCancel.setBackgroundResource(R.color.purple_200);

            if (sessionManager.isNetworkAvailable()) {

                binding.progressBar.setVisibility(View.VISIBLE);

                getNotificationMethod("Cancel");

            }else {
                Toast.makeText(getActivity(), R.string.checkInternet, Toast.LENGTH_SHORT).show();
            }

        });

        if (sessionManager.isNetworkAvailable()) {

            binding.progressBar.setVisibility(View.VISIBLE);

            getNotificationMethod("Pending");

        }else {
            Toast.makeText(getActivity(), R.string.checkInternet, Toast.LENGTH_SHORT).show();
        }


        return binding.getRoot();

    }

    private void setAdapter(ArrayList<GetOrderNotification.Result> modelList) {

        mAdapter = new SubProductRecyclerViewAdapter(getActivity(),modelList,HomeFragment.this);
        binding.recyclerSearch.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        binding.recyclerSearch.setLayoutManager(linearLayoutManager);

        binding.recyclerSearch.setAdapter(mAdapter);

        mAdapter.SetOnItemClickListener(new SubProductRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, GetOrderNotification.Result model) {

            }
        });
    }

    private void update_order_status(String id,String status) {

        Call<ResponseBody> call = RetrofitClients.getInstance().getApi()
                .update_order_status(id,status);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                binding.progressBar.setVisibility(View.GONE);

                String responseString = null;

                try {
                    responseString = response.body().string();

                    try {

                        JSONObject jsonObject = new JSONObject(responseString);

                        String result =jsonObject.getString("result");

                        if (jsonObject.getString("status").equals("1")) {

                            Toast.makeText(getActivity(), ""+result, Toast.LENGTH_SHORT).show();

                            binding.progressBar.setVisibility(View.VISIBLE);
                            getNotificationMethod(status);


                        }else
                        {
                            Toast.makeText(getActivity(), ""+result, Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }


                if (status.equalsIgnoreCase("1")) {


                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                binding.progressBar.setVisibility(View.GONE);

            }
        });
    }

    private void getNotificationMethod(String status) {
        modelList.clear();
        String UsserId = Preference.get(getActivity(), Preference.KEYType_login);

        Call<GetOrderNotification> call = RetrofitClients
                .getInstance()
                .getApi()
                .get_order(UsserId,status);
        call.enqueue(new Callback<GetOrderNotification>() {
            @Override
            public void onResponse(Call<GetOrderNotification> call, Response<GetOrderNotification> response) {

                binding.progressBar.setVisibility(View.GONE);

                GetOrderNotification finallyPr = response.body();

                String status = finallyPr.getStatus();

                if (status.equalsIgnoreCase("1")) {

                    binding.txtEmty.setVisibility(View.GONE);

                    modelList = (ArrayList<GetOrderNotification.Result>) finallyPr.getResult();

                    setAdapter(modelList);

                } else {

                    setAdapter(modelList);

                    binding.txtEmty.setVisibility(View.VISIBLE);
                    binding.progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<GetOrderNotification> call, Throwable t) {
                binding.txtEmty.setVisibility(View.VISIBLE);
                binding.progressBar.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void addItem(String id, String status) {

        if (sessionManager.isNetworkAvailable()) {

            binding.progressBar.setVisibility(View.VISIBLE);

            update_order_status(id,status);

        }else {

            Toast.makeText(getActivity(), R.string.checkInternet, Toast.LENGTH_SHORT).show();
        }
    }
}