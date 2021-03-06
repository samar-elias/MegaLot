package com.hudhud.megalot.Splash;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.hudhud.megalot.R;
import com.hudhud.megalot.Registration.RegistrationActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }

    private void setSplash(){
        new Handler(Looper.myLooper()).postDelayed(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void run() {
                Intent registrationIntent = new Intent(SplashActivity.this, RegistrationActivity.class);
                startActivity(registrationIntent);
                finish();
            }
        }, 2000);

    }
}