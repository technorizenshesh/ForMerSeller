package com.my.formerseller;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;


import com.google.firebase.iid.FirebaseInstanceId;
import com.my.formerseller.act.Login;

import java.util.Locale;

public class SplashActivity extends AppCompatActivity {

    private ImageView iv_Logo;
    Context mContext = this;
    String token="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        finds();

        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(runnable -> {
            String token = runnable.getToken();
            Log.e( "Tokennnn" ,token);
        });
    }

    private static void updateResources(Context context, String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.locale = locale;
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
    }

    private void finds() {

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {

                String User_id = Preference.get(SplashActivity.this,Preference.KEYType_login);

                if(User_id != null && !User_id.trim().equalsIgnoreCase("0")){

                    Intent intent=new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();

                }else
                {
                    Intent intent=new Intent(SplashActivity.this, Login.class);
                    startActivity(intent);
                    finish();
                }

              /*     Intent i = new Intent(SplashActivity.this, Login.class);
                    startActivity(i);
                    finish();*/
            }
        }, 3000);
    }
}