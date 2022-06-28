package com.hudhud.megalot.Views.Splash;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.hudhud.megalot.AppUtils.AppDefs;
import com.hudhud.megalot.R;
import com.hudhud.megalot.Views.Main.MainActivity;
import com.hudhud.megalot.Views.Registration.RegistrationActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setSplash();
    }

    private void setSplash(){
        new Handler(Looper.myLooper()).postDelayed(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void run() {
                getUserFromSharedPreferences();
            }
        }, 2000);

    }

    private void getUserFromSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences(AppDefs.SHARED_PREF_KEY, MODE_PRIVATE);
        String id = sharedPreferences.getString(AppDefs.ID_KEY, null);

        if (id!=null){
            AppDefs.user.setId(sharedPreferences.getString(AppDefs.ID_KEY, null));
            AppDefs.user.setPassword(sharedPreferences.getString(AppDefs.PASSWORD_KEY, null));
            AppDefs.user.setPhone(sharedPreferences.getString(AppDefs.PHONE_KEY, null));
            AppDefs.user.setStatus(sharedPreferences.getString(AppDefs.STATUS_KEY, null));
            AppDefs.user.setFcmToken(sharedPreferences.getString(AppDefs.FCM_TOKEN_KEY, null));
            AppDefs.user.setFbId(sharedPreferences.getString(AppDefs.FB_ID_KEY, null));
            AppDefs.user.setTypeToken(sharedPreferences.getString(AppDefs.TOKEN_TYPE_KEY, null));
            AppDefs.user.setToken(sharedPreferences.getString(AppDefs.TOKEN_KEY, null));
            AppDefs.user.setName(sharedPreferences.getString(AppDefs.NAME_KEY, null));
            AppDefs.user.setEmail(sharedPreferences.getString(AppDefs.EMAIL_KEY, null));
            AppDefs.user.setGoId(sharedPreferences.getString(AppDefs.GOOGLE_ID_KEY, null));
            AppDefs.user.setImage(sharedPreferences.getString(AppDefs.IMAGE_KEY, null));
            AppDefs.user.setCountry(sharedPreferences.getString(AppDefs.COUNTRY_KEY, null));
            AppDefs.user.setBirthDate(sharedPreferences.getString(AppDefs.BIRTH_DATE_KEY, null));
            AppDefs.language = sharedPreferences.getString(AppDefs.LANGUAGE_KEY, null);


            Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(mainIntent);
        }else {
            Intent loginIntent = new Intent(SplashActivity.this, RegistrationActivity.class);
            startActivity(loginIntent);
        }
        finish();
    }
}