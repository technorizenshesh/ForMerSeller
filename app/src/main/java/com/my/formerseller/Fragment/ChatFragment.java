package com.my.formerseller.Fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.my.formerseller.Preference;
import com.my.formerseller.R;
import com.my.formerseller.adapter.ChatAdapter;
import com.my.formerseller.databinding.ChatFragmentBinding;
import com.my.formerseller.model.ConverSationList;
import com.my.formerseller.utils.RetrofitClients;
import com.my.formerseller.utils.SessionManager;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class ChatFragment extends Fragment {
    ChatFragmentBinding binding;
    private SessionManager sessionManager;
    ChatAdapter mAdapter;

    private ArrayList<ConverSationList.Result> modelList = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.chat_fragment, container, false);

        sessionManager = new SessionManager(getActivity());

        if (sessionManager.isNetworkAvailable()) {

            binding.progressBar.setVisibility(VISIBLE);

            getConversationList();

        } else {
            Toast.makeText(getActivity(), R.string.checkInternet, Toast.LENGTH_SHORT).show();
        }

        return binding.getRoot();

    }


    private void setAdapter(ArrayList<ConverSationList.Result> modelList) {

        mAdapter = new ChatAdapter(getActivity(), modelList);

        binding.recyclerChat.setHasFixedSize(true);
        // use a linear layout manager
        //  binding.recyclerSearch.setLayoutManager(new GridLayoutManager(this, 2));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        binding.recyclerChat.setLayoutManager(linearLayoutManager);
        binding.recyclerChat.setAdapter(mAdapter);

        mAdapter.SetOnItemClickListener(new ChatAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, ConverSationList.Result model) {

                MsgChatFragment  chatFragment = new MsgChatFragment();
                Bundle args = new Bundle();
                args.putString("SellerId", model.getId());
                args.putString("SellerName", model.getName());
                args.putString("SellerImage", model.getImage());
                args.putString("request_id", model.getId());
                chatFragment.setArguments(args);

                loadFragment(chatFragment);

            /*    startActivity(new Intent(getActivity(), MsgChatAct.class).putExtra("SellerId",model.getId())
                        .putExtra("SellerName",model.getName())
                        .putExtra("SellerImage",model.getImage())
                        .putExtra("request_id",model.getId()));
*/
            }
        });

    }

    public void getConversationList() {

        String receiver_id = Preference.get(getActivity(), Preference.KEYType_login);

        Call<ConverSationList> call = RetrofitClients
                .getInstance()
                .getApi()
                .get_conversation(receiver_id);
        call.enqueue(new Callback<ConverSationList>() {
            @Override
            public void onResponse(Call<ConverSationList> call, Response<ConverSationList> response) {
                try {

                    binding.progressBar.setVisibility(GONE);

                    ConverSationList myclass = response.body();

                    String status = String.valueOf(myclass.getStatus());
                    String result = myclass.message;

                    if (status.equalsIgnoreCase("1")) {

                        modelList = (ArrayList<ConverSationList.Result>) myclass.result;
                        setAdapter(modelList);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ConverSationList> call, Throwable t) {
                binding.progressBar.setVisibility(GONE);
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