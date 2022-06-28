package com.hudhud.megalot.Views.Main.Account.MyInformation;

import static android.content.Context.MODE_PRIVATE;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.hudhud.megalot.AppUtils.AppDefs;
import com.hudhud.megalot.AppUtils.Urls;
import com.hudhud.megalot.R;
import com.hudhud.megalot.Views.Main.MainActivity;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyInformationFragment extends Fragment {

    private static final int REQUEST_CODE = 101;
    MainActivity mainActivity;
    NavController navController;
    ImageView navigateBack;
    CircleImageView profilePic;
    TextView changePicture, birthDate, emailAddress;
    EditText usernameEdt, phoneEdt, countryEdt;
    MaterialButton updateInformation, changePassword;
    String picture = "", username = "", phone = "", email = "", dob = "", country = "";

    public MyInformationFragment() {
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
        return inflater.inflate(R.layout.fragment_my_information, container, false);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof MainActivity) {
            mainActivity = (MainActivity) context;
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        onClick();
        setData();
    }

    private void initViews(View view){
        navController = Navigation.findNavController(view);

        navigateBack = view.findViewById(R.id.navigate_back);
        profilePic = view.findViewById(R.id.profile_image);
        changePicture = view.findViewById(R.id.change_picture);
        usernameEdt = view.findViewById(R.id.username_edt);
        phoneEdt = view.findViewById(R.id.phone_edt);
        birthDate = view.findViewById(R.id.birth_date);
        countryEdt = view.findViewById(R.id.country_edt);
        emailAddress = view.findViewById(R.id.email_address_edt);
        updateInformation = view.findViewById(R.id.update_information);
        changePassword = view.findViewById(R.id.change_password);
    }

    private void onClick(){
        navigateBack.setOnClickListener(view -> navController.popBackStack());

        final Calendar newCalendar = Calendar.getInstance();
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd MMM yyyy");
        final DatePickerDialog StartTime = new DatePickerDialog(mainActivity, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                birthDate.setText(dateFormatter.format(newDate.getTime()));
                dob = String.valueOf(birthDate.getText());
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        birthDate.setOnClickListener(view -> StartTime.show());

        changePicture.setOnClickListener(view -> pickImage());

        updateInformation.setOnClickListener(view -> {
            username = String.valueOf(usernameEdt.getText());
            phone = String.valueOf(phoneEdt.getText());
            country = String.valueOf(countryEdt.getText());
            checkEditTexts();
        });

        changePassword.setOnClickListener(view -> changePasswordPopUp());
    }

    private void setData(){
        if (!AppDefs.user.getImage().isEmpty()){
            Glide.with(mainActivity).load(AppDefs.user.getImage()).into(profilePic);
        }
        usernameEdt.setText(AppDefs.user.getName());
        username = AppDefs.user.getName();
        phoneEdt.setText(AppDefs.user.getPhone());
        phone = AppDefs.user.getPhone();
        birthDate.setText(AppDefs.user.getBirthDate());
        dob = AppDefs.user.getBirthDate();
        countryEdt.setText(AppDefs.user.getCountry());
        country = AppDefs.user.getCountry();
        emailAddress.setText(AppDefs.user.getEmail());
        email = AppDefs.user.getEmail();
        if (!AppDefs.user.getGoId().isEmpty() || !AppDefs.user.getFbId().isEmpty()){
            changePassword.setBackgroundColor(mainActivity.getResources().getColor(R.color.gray));
            changePassword.setEnabled(false);
        }else {
            changePassword.setBackgroundColor(mainActivity.getResources().getColor(R.color.green));
            changePassword.setEnabled(true);
        }
    }

    private void pickImage() {
        if (ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getContext(),
                        Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getContext(),
                        Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.CAMERA},
                    REQUEST_CODE);
        } else {
            PickImageDialog.build(new PickSetup()).setOnPickResult(r -> {
                Glide.with(mainActivity).load(r.getBitmap()).into(profilePic);
                picture = mainActivity.convertToBase64(r.getBitmap());
            }).show(getActivity());
        }
    }

    private void checkEditTexts(){
        if (username.isEmpty()){
            usernameEdt.setError(mainActivity.getResources().getString(R.string.fill_field));
        }else if (phone.isEmpty()){
            phoneEdt.setError(mainActivity.getResources().getString(R.string.fill_field));
        }else if (dob.isEmpty()){
            birthDate.setError(mainActivity.getResources().getString(R.string.fill_field));
        }else if (country.isEmpty()){
            countryEdt.setError(mainActivity.getResources().getString(R.string.fill_field));
        }else {
            updateProfile();
        }
    }

    private void updateProfile(){
        mainActivity.showProgressDialog();
        JSONObject updateProfileObj = new JSONObject();
        try {
            updateProfileObj.put("name", username);
            updateProfileObj.put("phone", phone);
            updateProfileObj.put("email", AppDefs.user.getEmail());
            updateProfileObj.put("date_birth", dob);
            updateProfileObj.put("country", country);
            updateProfileObj.put("image", picture);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest updateProfileRequest = new JsonObjectRequest(Request.Method.POST, Urls.API_URL+"update_account", updateProfileObj, response -> {
            mainActivity.hideProgressDialog();
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
            mainActivity.hideProgressDialog();
            JSONObject body;
            if(error.networkResponse.data!=null) {
                try {
                    try {
                        body = new JSONObject(new String(error.networkResponse.data,"UTF-8"));
                        JSONObject statusObj = body.getJSONObject("status");
                        if (statusObj.getString("code").equals("500")){
                            mainActivity.showResponseMessage(mainActivity.getResources().getString(R.string.update_information), mainActivity.getResources().getString(R.string.internet_connection_error));
                        }else {
                            mainActivity.showResponseMessage(mainActivity.getResources().getString(R.string.update_information), statusObj.getString("massage"));
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
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json; charset=UTF-8");
                params.put("Authorization", "Bearer " + AppDefs.user.getToken());
                return params;
            }
        };
        mainActivity.queue.add(updateProfileRequest);
    }

    public void changePasswordPopUp(){
        View alertView = LayoutInflater.from(getContext()).inflate(R.layout.change_password_pop_up, null);
        android.app.AlertDialog alertBuilder = new android.app.AlertDialog.Builder(getContext()).setView(alertView).show();
        alertBuilder.show();
        alertBuilder.setCancelable(false);

        alertBuilder.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        EditText currentPass = alertView.findViewById(R.id.current_password);
        EditText newPass = alertView.findViewById(R.id.new_password);
        EditText confirmNewPass = alertView.findViewById(R.id.confirm_new_password);
        MaterialButton change = alertView.findViewById(R.id.change);
        MaterialButton cancel = alertView.findViewById(R.id.cancel);

        cancel.setOnClickListener(view -> alertBuilder.dismiss());
        change.setOnClickListener(view -> {
            String current = String.valueOf(currentPass.getText());
            String newPassword = String.valueOf(newPass.getText());
            String confirmNewPassword = String.valueOf(confirmNewPass.getText());
            if (current.isEmpty() || newPassword.isEmpty() || confirmNewPassword.isEmpty()){
                mainActivity.showResponseMessage(getResources().getString(R.string.change_password), getResources().getString(R.string.fill_field));
            }else if (current.equals(AppDefs.user.getPassword()) && newPassword.equals(confirmNewPassword)){
                changePassword(newPassword);
                alertBuilder.dismiss();
            }else if (!current.equals(AppDefs.user.getPassword())){
                mainActivity.showResponseMessage(getResources().getString(R.string.change_password), getResources().getString(R.string.current_password_wrong));
            }else if (!newPassword.equals(confirmNewPassword)){
                mainActivity.showResponseMessage(getResources().getString(R.string.change_password), getResources().getString(R.string.not_equal));
            }
        });
    }

    private void changePassword(String password){
        mainActivity.showProgressDialog();
        JSONObject passwordObj = new JSONObject();
        try {
            passwordObj.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest changePasswordRequest = new JsonObjectRequest(Request.Method.POST, Urls.API_URL+"update_password", passwordObj, response -> {
            mainActivity.hideProgressDialog();
            AppDefs.user.setPassword(password);
            SharedPreferences sharedPreferences = mainActivity.getSharedPreferences(AppDefs.SHARED_PREF_KEY,MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(AppDefs.PASSWORD_KEY, AppDefs.user.getPassword());
            editor.apply();
            Toast.makeText(mainActivity, mainActivity.getResources().getString(R.string.password_changed), Toast.LENGTH_SHORT).show();
        }, error -> {
            mainActivity.hideProgressDialog();
            JSONObject body;
            if(error.networkResponse.data!=null) {
                try {
                    try {
                        body = new JSONObject(new String(error.networkResponse.data,"UTF-8"));
                        JSONObject statusObj = body.getJSONObject("status");
                        if (statusObj.getString("code").equals("500")){
                            mainActivity.showResponseMessage(mainActivity.getResources().getString(R.string.change_password), mainActivity.getResources().getString(R.string.internet_connection_error));
                        }else {
                            mainActivity.showResponseMessage(mainActivity.getResources().getString(R.string.change_password), statusObj.getString("massage"));
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
        mainActivity.queue.add(changePasswordRequest);
    }

    private void saveUserToSharedPreferences(){
        SharedPreferences sharedPreferences = mainActivity.getSharedPreferences(AppDefs.SHARED_PREF_KEY,MODE_PRIVATE);
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

        Toast.makeText(mainActivity, mainActivity.getResources().getString(R.string.update_information_successfully), Toast.LENGTH_SHORT).show();
        navController.popBackStack();
    }
}