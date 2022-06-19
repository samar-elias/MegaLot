package com.hudhud.megalot.Views.Registration;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.hudhud.megalot.R;

public class RegistrationActivity extends AppCompatActivity {

    ProgressDialog pDialog;
    public RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        initViews();
    }

    private void initViews(){
        pDialog = new ProgressDialog(this);
        queue = Volley.newRequestQueue(this);
    }

    public void showProgressDialog() {
        pDialog.setMessage(getResources().getString(R.string.loading));
        pDialog.setCancelable(false);
        pDialog.show();
    }

    public void hideProgressDialog() {
        pDialog.hide();
    }

    public void showResponseMessage(String title, String msg) {
        AlertDialog.Builder msgDialog = new AlertDialog.Builder(this);
        msgDialog.setTitle(title);
        msgDialog.setMessage(msg);
        msgDialog.setCancelable(false);

        msgDialog.setPositiveButton(getResources().getString(R.string.done), (dialogInterface, i) -> dialogInterface.dismiss());
        msgDialog.show();
    }
}