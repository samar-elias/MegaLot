package com.hudhud.megalot.Views.Registration.SignIn;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.messaging.FirebaseMessaging;
import com.hudhud.megalot.AppUtils.Responses.User;
import com.hudhud.megalot.AppUtils.Urls;
import com.hudhud.megalot.Views.Main.MainActivity;
import com.hudhud.megalot.R;
import com.hudhud.megalot.Views.Registration.RegistrationActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class SignInFragment extends Fragment {

    NavController navController;
    RegistrationActivity registrationActivity;
    MaterialButton signIn;
    TextView signUp;
    String tokenId = "";

    public SignInFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_in, container, false);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof RegistrationActivity) {
            registrationActivity = (RegistrationActivity) context;
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        onClick();
        getToken();
    }

    private void initViews(View view){
        navController = Navigation.findNavController(view);
        signIn = view.findViewById(R.id.sign_in);
        signUp = view.findViewById(R.id.sign_up);
    }

    private void onClick(){
        signIn.setOnClickListener(view -> {
            Intent mainIntent = new Intent(registrationActivity, MainActivity.class);
            startActivity(mainIntent);
            registrationActivity.finish();
        });
        signUp.setOnClickListener(view -> navController.navigate(SignInFragmentDirections.actionSignInFragmentToSignUpFragment()));
    }

    private void getToken() {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.w("FAILED", "Fetching FCM registration token failed", task.getException());
                        return;
                    }
                    tokenId = task.getResult();
                });
    }

    private void signIn(String emailAddress, String password){
        registrationActivity.showProgressDialog();
        JSONObject signInObj = new JSONObject();
        try {
            signInObj.put("email", emailAddress);
            signInObj.put("password", password);
            signInObj.put("fcm_token", tokenId);
            signInObj.put("type_token", "1");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest signInRequest = new JsonObjectRequest(Request.Method.POST, Urls.API_URL+"user_login", signInObj, response -> {
            registrationActivity.hideProgressDialog();

        }, error -> {
            registrationActivity.hideProgressDialog();
            JSONObject body;
            if(error.networkResponse.data!=null) {
                try {
                    try {
                        body = new JSONObject(new String(error.networkResponse.data,"UTF-8"));
                        JSONObject statusObj = body.getJSONObject("status");
                        if (statusObj.getString("code").equals("500")){
                            registrationActivity.showResponseMessage(registrationActivity.getResources().getString(R.string.sign_in), registrationActivity.getResources().getString(R.string.internet_connection_error));
                        }else {
                            registrationActivity.showResponseMessage(registrationActivity.getResources().getString(R.string.sign_in), statusObj.getString("massage"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });
        registrationActivity.queue.add(signInRequest);
    }
}