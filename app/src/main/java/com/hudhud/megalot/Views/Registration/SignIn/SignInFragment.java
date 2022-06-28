package com.hudhud.megalot.Views.Registration.SignIn;

import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.messaging.FirebaseMessaging;
import com.hudhud.megalot.AppUtils.AppDefs;
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
    EditText emailEdt, passwordEdt;
    String tokenId = "", emailAddress = "", password = "";
    GoogleSignInClient googleSignInClient;
    CallbackManager mCallbackManager;
    LoginButton continueFacebook;
    private static final int GOOGLE_CODE = 0;
    MaterialCardView googleSignUp;

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
        emailEdt = view.findViewById(R.id.email_address_edt);
        passwordEdt = view.findViewById(R.id.password_edt);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.google_client_id))
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(registrationActivity, gso);
        continueFacebook = view.findViewById(R.id.login_fb_btn);
        mCallbackManager = CallbackManager.Factory.create();
    }

    private void onClick(){
        signIn.setOnClickListener(view -> {
            emailAddress = String.valueOf(emailEdt.getText());
            password = String.valueOf(passwordEdt.getText());
            checkEditTexts();
        });
        signUp.setOnClickListener(view -> navController.navigate(SignInFragmentDirections.actionSignInFragmentToSignUpFragment()));
        googleSignUp.setOnClickListener(view -> signUpGoogle());
        continueFacebook.setReadPermissions("email", "public_profile");
        continueFacebook.setFragment(this);
        continueFacebook.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("facebookToken", String.valueOf(loginResult.getAccessToken().getToken()));
                fbSignIn(loginResult.getAccessToken().getToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);
            }
        });
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

    private void checkEditTexts(){
        if (emailAddress.isEmpty()){
            emailEdt.setError(registrationActivity.getResources().getString(R.string.fill_field));
        }else if (password.isEmpty()){
            passwordEdt.setError(registrationActivity.getResources().getString(R.string.fill_field));
        }else if (!emailAddress.contains("@")){
            emailEdt.setError(registrationActivity.getResources().getString(R.string.wrong_email));
        }else {
            signIn();
        }
    }

    private void signIn(){
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
            try {
                if (response.getJSONObject("status").getString("code").equals("200")){
                    AppDefs.user.setToken(response.getString("token"));
                    JSONObject userObj = response.getJSONObject("results");
                    AppDefs.user.setId(userObj.getString("id"));
                    AppDefs.user.setName(userObj.getString("name"));
                    AppDefs.user.setPhone(userObj.getString("phone"));
                    AppDefs.user.setEmail(userObj.getString("email"));
                    AppDefs.user.setImage(userObj.getString("image"));
                    AppDefs.user.setBirthDate(userObj.getString("date_birth"));
                    AppDefs.user.setCountry(userObj.getString("country"));
                    AppDefs.user.setPassword(userObj.getString("password"));
                    AppDefs.user.setFcmToken(userObj.getString("fcm_token"));
                    AppDefs.user.setTypeToken(userObj.getString("type_token"));
                    AppDefs.user.setGoId(userObj.getString("go_id"));
                    AppDefs.user.setFbId(userObj.getString("fb_id"));
                    AppDefs.user.setStatus(userObj.getString("status"));

                    saveUserToSharedPreferences();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
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

    private void signUpGoogle(){
        Intent googleIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(googleIntent, GOOGLE_CODE);
    }

    private void handleGoogleSignInResult(Task<GoogleSignInAccount> completedTask){
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            Log.d("googleToken", account.getId());
//            signIn(account.getIdToken());
        } catch (ApiException e) {
            e.printStackTrace();
            Toast.makeText(registrationActivity, registrationActivity.getResources().getString(R.string.google_signup_failed), Toast.LENGTH_SHORT).show();
        }
    }

    private void fbSignIn(String tokenId){
        JSONObject createAccountObj = new JSONObject();
        try {
            createAccountObj.put("token", tokenId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest signUpRequest = new JsonObjectRequest(Request.Method.POST, Urls.API_URL+"user_facebook_token", createAccountObj, response -> {
            registrationActivity.hideProgressDialog();
            try {
                if (response.getJSONObject("status").getString("code").equals("200")){
                    AppDefs.user.setToken(response.getString("token"));
                    JSONObject userObj = response.getJSONObject("results");
                    AppDefs.user.setId(userObj.getString("id"));
                    AppDefs.user.setName(userObj.getString("name"));
                    AppDefs.user.setPhone(userObj.getString("phone"));
                    AppDefs.user.setEmail(userObj.getString("email"));
                    AppDefs.user.setImage(userObj.getString("image"));
                    AppDefs.user.setBirthDate(userObj.getString("date_birth"));
                    AppDefs.user.setCountry(userObj.getString("country"));
                    AppDefs.user.setPassword(userObj.getString("password"));
                    AppDefs.user.setFcmToken(userObj.getString("fcm_token"));
                    AppDefs.user.setTypeToken(userObj.getString("type_token"));
                    AppDefs.user.setGoId(userObj.getString("go_id"));
                    AppDefs.user.setFbId(userObj.getString("fb_id"));
                    AppDefs.user.setStatus(userObj.getString("status"));

                    saveUserToSharedPreferences();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }, error -> {
            registrationActivity.hideProgressDialog();
            JSONObject body;
            if(error.networkResponse.data!=null) {
                try {
                    try {
                        body = new JSONObject(new String(error.networkResponse.data,"UTF-8"));
                        JSONObject statusObj = body.getJSONObject("status");
                        if (statusObj.getString("code").equals("500")){
                            registrationActivity.showResponseMessage(registrationActivity.getResources().getString(R.string.sign_up), registrationActivity.getResources().getString(R.string.internet_connection_error));
                        }else {
                            registrationActivity.showResponseMessage(registrationActivity.getResources().getString(R.string.sign_up), statusObj.getString("massage"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });
        registrationActivity.queue.add(signUpRequest);
    }

    private void saveUserToSharedPreferences(){
        SharedPreferences sharedPreferences = registrationActivity.getSharedPreferences(AppDefs.SHARED_PREF_KEY,MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(AppDefs.ID_KEY, AppDefs.user.getId());
        editor.putString(AppDefs.NAME_KEY, AppDefs.user.getName());
        editor.putString(AppDefs.PHONE_KEY, AppDefs.user.getPhone());
        editor.putString(AppDefs.PASSWORD_KEY, AppDefs.user.getPassword());
        editor.putString(AppDefs.EMAIL_KEY, AppDefs.user.getEmail());
        editor.putString(AppDefs.IMAGE_KEY, AppDefs.user.getImage());
        editor.putString(AppDefs.COUNTRY_KEY, AppDefs.user.getCountry());
        editor.putString(AppDefs.BIRTH_DATE_KEY, AppDefs.user.getBirthDate());
        editor.putString(AppDefs.FB_ID_KEY, AppDefs.user.getFbId());
        editor.putString(AppDefs.GOOGLE_ID_KEY, AppDefs.user.getGoId());
        editor.putString(AppDefs.TOKEN_KEY, AppDefs.user.getToken());
        editor.putString(AppDefs.STATUS_KEY, AppDefs.user.getStatus());
        editor.putString(AppDefs.user.getFcmToken(), AppDefs.user.getFcmToken());
        editor.putString(AppDefs.TOKEN_TYPE_KEY, AppDefs.user.getTypeToken());
        editor.putString(AppDefs.LANGUAGE_KEY, AppDefs.language);

        editor.apply();

        Intent mainIntent = new Intent(registrationActivity, MainActivity.class);
        startActivity(mainIntent);
        registrationActivity.finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GOOGLE_CODE){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleGoogleSignInResult(task);
        }else {
            mCallbackManager.onActivityResult(requestCode, resultCode, data);

            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}