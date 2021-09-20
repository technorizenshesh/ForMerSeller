package com.my.formerseller.act;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.my.formerseller.MainActivity;
import com.my.formerseller.Preference;
import com.my.formerseller.R;
import com.my.formerseller.databinding.ActivityForogotPasswordBinding;
import com.my.formerseller.model.ForogtPassword;
import com.my.formerseller.model.LoginModel;
import com.my.formerseller.utils.RetrofitClients;
import com.my.formerseller.utils.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ForogotPassword extends AppCompatActivity {

    ActivityForogotPasswordBinding binding;
    private SessionManager sessionManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this, R.layout.activity_forogot_password);

        sessionManager = new SessionManager(ForogotPassword.this);

        binding.txtForogtPassword.setOnClickListener(v -> {

            validation();

        });
    }

    private void validation() {

        String email = binding.etEmaillogin.getText().toString();

        if(!isValidEmail(email))
        {
            Toast.makeText(this, "Please Enter email.", Toast.LENGTH_SHORT).show();

        }else
        {
            if (sessionManager.isNetworkAvailable()) {

                binding.progressBar.setVisibility(View.VISIBLE);

                forogotMethod(email);

            }else {
                Toast.makeText(this, R.string.checkInternet, Toast.LENGTH_SHORT).show();
            }
        }
    }

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    private void forogotMethod(String email){

        Call<ForogtPassword> call = RetrofitClients
                .getInstance()
                .getApi()
                .forgot_password(email);

        call.enqueue(new Callback<ForogtPassword>() {
            @Override
            public void onResponse(Call<ForogtPassword> call, Response<ForogtPassword> response) {

                binding.progressBar.setVisibility(View.GONE);

                ForogtPassword finallyPr = response.body();

                String status = finallyPr.getStatus();

                if (status.equalsIgnoreCase("1")) {

                    Toast.makeText(ForogotPassword.this, finallyPr.getMessage(), Toast.LENGTH_SHORT).show();

                    startActivity(new Intent(ForogotPassword.this, Login.class));

                } else {
                    binding.progressBar.setVisibility(View.GONE);
                    Toast.makeText(ForogotPassword.this, finallyPr.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<ForogtPassword> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                Toast.makeText(ForogotPassword.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}