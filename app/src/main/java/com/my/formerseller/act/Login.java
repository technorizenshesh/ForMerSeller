package com.my.formerseller.act;

import android.content.Intent;
import android.content.pm.Signature;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.my.formerseller.Chat.DataManager;
import com.my.formerseller.Chat.SessionManagerTwo;
import com.my.formerseller.MainActivity;
import com.my.formerseller.Preference;
import com.my.formerseller.R;
import com.my.formerseller.databinding.ActivityLoginBinding;
import com.my.formerseller.model.LoginModel;
import com.my.formerseller.utils.Constant;
import com.my.formerseller.utils.RetrofitClients;
import com.my.formerseller.utils.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

public class Login extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{

    ActivityLoginBinding binding;
    String  result="";
    private SessionManager sessionManager;

    //Google SignIn
    private RelativeLayout RR_faceBook_login;
    private RelativeLayout RR_google_login;
    private SignInButton signInButton;
    FirebaseAuth mAuth;
    private final static int RC_SIGN_IN = 1;
    private GoogleApiClient googleApiClient;
    private static final String TAG = "fireBaseToken";

    String token="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);

        sessionManager = new SessionManager(Login.this);

        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(runnable -> {
            token = runnable.getToken();
            Log.e( "Tokennnn" ,token);
        });

        //Google SignIn
        mAuth = FirebaseAuth.getInstance();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        binding.imgGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(intent, RC_SIGN_IN);
            }
        });

        SetupUI();

    }

    private void SetupUI() {

        binding.loginID.setOnClickListener(v -> {

            validation();

        });

        binding.llSingUp.setOnClickListener(v -> {
             startActivity(new Intent(this, SignUpActivity.class));
        });

        binding.txtForogtPassword.setOnClickListener(v -> {
                startActivity(new Intent(this, ForogotPassword.class));
        });
    }

    //Google Login
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult( requestCode, resultCode, data );
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent( data );
            handleSignInResult( result );
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            GoogleSignInAccount account = result.getSignInAccount();

            String UsernAME=account.getDisplayName();
            String email=account.getEmail();
            String SocialId=account.getId();
            Uri Url=account.getPhotoUrl();

            if (sessionManager.isNetworkAvailable()) {

                SocialLogin(SocialId,UsernAME,email,"","","",Url);

            }else {
                Toast.makeText(Login.this, R.string.checkInternet, Toast.LENGTH_SHORT).show();
            }

        } else {

            Toast.makeText( this, "Login Unsuccessful", Toast.LENGTH_SHORT ).show();

        }
    }



    private void validation() {

       String email = binding.etEmaillogin.getText().toString();
        String password =  binding.etPasswordlogin.getText().toString();

        if(email.equalsIgnoreCase(""))
        {
            Toast.makeText(this, "Please Enter email.", Toast.LENGTH_SHORT).show();

        }else if(password.equalsIgnoreCase(""))
        {
            Toast.makeText(this, "Please Enter Password.", Toast.LENGTH_SHORT).show();

        }else
        {
            if (sessionManager.isNetworkAvailable()) {
                loginMethod(email,password);
            }else {
                Toast.makeText(this, R.string.checkInternet, Toast.LENGTH_SHORT).show();
            }
        }
    }

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }


    private void loginMethod(String emai,String password){

        DataManager.getInstance().showProgressMessage(Login.this,getString(R.string.please_wait));
        Call<LoginModel> call = RetrofitClients
                .getInstance()
                .getApi()
                .login(emai,password,token);
        call.enqueue(new Callback<LoginModel>() {
            @Override
            public void onResponse(Call<LoginModel> call, Response<LoginModel> response) {
                DataManager.getInstance().hideProgressMessage();
                LoginModel finallyPr = response.body();
                String responseString = new Gson().toJson(response.body());
                Log.e(TAG,"Login Response :"+responseString);
                if(finallyPr.getStatus().equals("1")){

                    Preference.save(Login.this,Preference.KEYType_login,finallyPr.getResult().getId());
                    SessionManagerTwo.writeString(Login.this, Constant.USER_INFO, responseString);
                    Toast.makeText(Login.this, finallyPr.getMessage(), Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Login.this,MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    //
                   finish();

                } else {

                    Toast.makeText(Login.this, finallyPr.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<LoginModel> call, Throwable t) {
                DataManager.getInstance().hideProgressMessage();
                call.cancel();
                Toast.makeText(Login.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void SocialLogin(String Social_id,String Name, String email, String mobile, String lat, String lon, Uri url){

        DataManager.getInstance().showProgressMessage(Login.this,getString(R.string.please_wait));

        Call<LoginModel> call = RetrofitClients
                .getInstance()
                .getApi()
                .social_login(Social_id,Name,email,mobile,token,"","",lat,lon,"Seller");
        call.enqueue(new Callback<LoginModel>() {
            @Override
            public void onResponse(Call<LoginModel> call, Response<LoginModel> response) {
                DataManager.getInstance().hideProgressMessage();
                LoginModel finallyPr = response.body();
                String responseString = new Gson().toJson(response.body());
                Log.e(TAG,"Login Response :"+responseString);
                if(finallyPr.getStatus().equals("1")){

                    Preference.save(Login.this,Preference.KEYType_login,finallyPr.getResult().getId());
                    //
                    SessionManagerTwo.writeString(Login.this, Constant.USER_INFO, responseString);
                    Toast.makeText(Login.this, finallyPr.getMessage(), Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Login.this,MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    finish();

                } else {
                    Toast.makeText(Login.this, finallyPr.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<LoginModel> call, Throwable t) {
                DataManager.getInstance().hideProgressMessage();
                call.cancel();
                Toast.makeText(Login.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}