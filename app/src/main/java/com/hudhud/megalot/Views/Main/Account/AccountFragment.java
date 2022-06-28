package com.hudhud.megalot.Views.Main.Account;

import static android.content.Context.MODE_PRIVATE;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.hudhud.megalot.AppUtils.AppDefs;
import com.hudhud.megalot.AppUtils.Responses.Bracket;
import com.hudhud.megalot.AppUtils.Responses.User;
import com.hudhud.megalot.AppUtils.Urls;
import com.hudhud.megalot.R;
import com.hudhud.megalot.Views.Main.Account.AboutUs.AboutUsBottomSheetDialogFragment;
import com.hudhud.megalot.Views.Main.Account.FAQs.FAQsBottomSheetDialogFragment;
import com.hudhud.megalot.Views.Main.Account.Language.LanguagesBottomSheetDialogFragment;
import com.hudhud.megalot.Views.Main.Account.PrivacyPolicy.PrivacyBottomSheetDialogFragment;
import com.hudhud.megalot.Views.Main.Account.TermsConditions.TermsConditionsBottomSheetDialogFragment;
import com.hudhud.megalot.Views.Main.Home.WithdrawBottomSheetDialogFragment;
import com.hudhud.megalot.Views.Main.MainActivity;
import com.hudhud.megalot.Views.Splash.SplashActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class AccountFragment extends Fragment {

    MainActivity mainActivity;
    NavController navController;
    ImageView myInformation;
    CircleImageView profilePic;
    TextView username, phone, bracketTitle, popUpTickets, youtubeTickets, installsTickets, startPoint, endPoint, userCardsCount;
    ConstraintLayout aboutUs, termsConditions, privacyPolicy, faqs, language;
    LinearLayoutCompat logout;
    String userCount = "0";
    Bracket bracket = new Bracket();
    SeekBar bracketSeekbar;

    public AccountFragment() {
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
        return inflater.inflate(R.layout.fragment_account, container, false);
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
        getBracket();
    }

    private void initViews(View view){
        navController = Navigation.findNavController(view);
        profilePic = view.findViewById(R.id.profile_image);
        username = view.findViewById(R.id.username);
        phone = view.findViewById(R.id.phone);
        myInformation = view.findViewById(R.id.my_information);
        aboutUs = view.findViewById(R.id.about_us);
        termsConditions = view.findViewById(R.id.terms_conditions);
        privacyPolicy = view.findViewById(R.id.privacy_policy);
        faqs = view.findViewById(R.id.faq);
        logout = view.findViewById(R.id.logout);
        language = view.findViewById(R.id.language);
        bracketSeekbar = view.findViewById(R.id.bracket_seek_bar);
        bracketTitle = view.findViewById(R.id.bracket_title);
        popUpTickets = view.findViewById(R.id.pop_up_ticket);
        youtubeTickets = view.findViewById(R.id.youtube_ticket);
        installsTickets = view.findViewById(R.id.install_ticket);
        startPoint = view.findViewById(R.id.start_points);
        endPoint = view.findViewById(R.id.end_points);
        userCardsCount = view.findViewById(R.id.user_card_count);

        bracketSeekbar.setEnabled(false);
    }

    private void onClick(){
        myInformation.setOnClickListener(view -> navController.navigate(AccountFragmentDirections.actionAccountFragment2ToMyInformationFragment()));
        aboutUs.setOnClickListener(view -> {
            AboutUsBottomSheetDialogFragment aboutUsBottomSheetDialogFragment = new AboutUsBottomSheetDialogFragment();
            aboutUsBottomSheetDialogFragment.show(getChildFragmentManager(),
                    "about_dialog_fragment");
        });
        termsConditions.setOnClickListener(view -> {
            TermsConditionsBottomSheetDialogFragment termsBottomSheetDialogFragment = new TermsConditionsBottomSheetDialogFragment();
            termsBottomSheetDialogFragment.show(getChildFragmentManager(),
                    "terms_dialog_fragment");
        });
        privacyPolicy.setOnClickListener(view -> {
            PrivacyBottomSheetDialogFragment privacyBottomSheetDialogFragment = new PrivacyBottomSheetDialogFragment();
            privacyBottomSheetDialogFragment.show(getChildFragmentManager(),
                    "privacy_dialog_fragment");
        });
        faqs.setOnClickListener(view -> {
            FAQsBottomSheetDialogFragment faQsBottomSheetDialogFragment = new FAQsBottomSheetDialogFragment();
            faQsBottomSheetDialogFragment.show(getChildFragmentManager(),
                    "faqs_dialog_fragment");
        });
        language.setOnClickListener(view -> {
            LanguagesBottomSheetDialogFragment languagesBottomSheetDialogFragment = new LanguagesBottomSheetDialogFragment();
            languagesBottomSheetDialogFragment.show(getChildFragmentManager(),
                    "languages_dialog_fragment");
        });
        logout.setOnClickListener(view -> showLogoutMessage());
    }

    private void setData(){
        if (!AppDefs.user.getImage().isEmpty()){
            Glide.with(mainActivity).load(AppDefs.user.getImage()).into(profilePic);
        }
        username.setText(AppDefs.user.getName());
        phone.setText(AppDefs.user.getPhone());
    }

    private void getBracket(){
        mainActivity.showProgressDialog();
        userCount = "0";
        bracket = new Bracket();
        StringRequest bracketRequest = new StringRequest(Request.Method.GET, Urls.API_URL+"get_bracket", response -> {
            mainActivity.hideProgressDialog();
            try {
                JSONObject responseObj = new JSONObject(response);
                JSONObject resultObj = responseObj.getJSONObject("results");
                userCount = resultObj.getString("countCard");
                JSONObject bracketObj = resultObj.getJSONObject("bracket");
                bracket.setId(bracketObj.getString("id"));
                bracket.setTitle(bracketObj.getString("title"));
                bracket.setPointIn(bracketObj.getString("point_in"));
                bracket.setPointEnd(bracketObj.getString("point_end"));
                bracket.setAppInstall(bracketObj.getString("app_install"));
                bracket.setYoutube(bracketObj.getString("youtube"));
                bracket.setPopUpTicket(bracketObj.getString("poup_teckt"));
                setBracketData();
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
                            mainActivity.showResponseMessage(mainActivity.getResources().getString(R.string.bracket), mainActivity.getResources().getString(R.string.internet_connection_error));
                        }else {
                            mainActivity.showResponseMessage(mainActivity.getResources().getString(R.string.bracket), statusObj.getString("massage"));
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
        mainActivity.queue.add(bracketRequest);
    }

    private void setBracketData(){
        userCardsCount.setText(userCount);
        bracketTitle.setText(bracket.getTitle());
        popUpTickets.setText("+"+bracket.getPopUpTicket());
        youtubeTickets.setText("+"+bracket.getYoutube());
        installsTickets.setText("+"+bracket.getAppInstall());
        startPoint.setText(bracket.getPointIn());
        endPoint.setText(bracket.getPointEnd());
        double end = Double.parseDouble(bracket.getPointEnd());
        double start = Double.parseDouble(bracket.getPointIn());
        double cards = Double.parseDouble(userCount);
        double progress = ((cards-start) * 100 / (end - start));
        bracketSeekbar.setProgress((int) Math.ceil(progress));
    }

    private void showLogoutMessage(){
        AlertDialog.Builder msgDialog = new AlertDialog.Builder(mainActivity);
        msgDialog.setTitle(mainActivity.getResources().getString(R.string.logout));
        msgDialog.setMessage(mainActivity.getResources().getString(R.string.log_out_desc));

        msgDialog.setNegativeButton(mainActivity.getResources().getString(R.string.logout), (dialogInterface, i) -> {
            SharedPreferences preferences = mainActivity.getSharedPreferences(AppDefs.SHARED_PREF_KEY, MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.clear();
            editor.apply();
            AppDefs.user = new User();
            Intent splashIntent = new Intent(mainActivity, SplashActivity.class);
            startActivity(splashIntent);
            mainActivity.finish();
        });

        msgDialog.setPositiveButton(getResources().getString(R.string.cancel), (dialogInterface, i) -> dialogInterface.dismiss());
        msgDialog.show();
    }
}