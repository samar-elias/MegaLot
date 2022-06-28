package com.hudhud.megalot.Views.Main;

import static com.hudhud.megalot.AppUtils.AppDefs.nextDraw;
import static com.hudhud.megalot.AppUtils.AppDefs.totalIncreasedValue;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Base64;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.hudhud.megalot.AppUtils.AppDefs;
import com.hudhud.megalot.AppUtils.Urls;
import com.hudhud.megalot.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    NavController navController;
    BottomNavigationView bottomNavigationView;
    ProgressDialog pDialog;
    public RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navController = Navigation.findNavController(this, R.id.fragment);
        bottomNavigationView = findViewById(R.id.bottom_nav);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);

        initViews();
        Timer myTimer = new Timer ();
        TimerTask myTask = new TimerTask () {
            @Override
            public void run () {
                if (!nextDraw.getId().isEmpty()){
                    updatePrice();
                }
            }
        };

        myTimer.scheduleAtFixedRate(myTask , 0l, (5*1000));
    }

    public String convertToBase64(Bitmap bitmap){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();
        String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
        return encoded;
    }

    private void initViews(){
        pDialog = new ProgressDialog(this);
        queue = Volley.newRequestQueue(this);
    }

    private void updatePrice(){
        JSONObject increaseValueObj = new JSONObject();
        try {
            increaseValueObj.put("drawId", nextDraw.getId());
            increaseValueObj.put("price", String.valueOf(totalIncreasedValue));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest updatePriceRequest = new JsonObjectRequest(Request.Method.POST, Urls.API_URL+"update_price", increaseValueObj, response -> {
            totalIncreasedValue = 0.00;
        }, error -> {
            JSONObject body;
            if(error.networkResponse.data!=null) {
                try {
                    try {
                        body = new JSONObject(new String(error.networkResponse.data,"UTF-8"));
                        JSONObject statusObj = body.getJSONObject("status");
                        if (statusObj.getString("code").equals("500")){
                            showResponseMessage(getResources().getString(R.string.home), getResources().getString(R.string.internet_connection_error));
                        }else {
                            showResponseMessage(getResources().getString(R.string.home), statusObj.getString("massage"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json; charset=UTF-8");
                params.put("Authorization", "Bearer " + AppDefs.user.getToken());
                return params;
            }
        };
        queue.add(updatePriceRequest);
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