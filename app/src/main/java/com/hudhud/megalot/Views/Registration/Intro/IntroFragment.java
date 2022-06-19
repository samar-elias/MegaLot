package com.hudhud.megalot.Views.Registration.Intro;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.tabs.TabLayout;
import com.hudhud.megalot.AppUtils.AppDefs;
import com.hudhud.megalot.AppUtils.Helpers.Helpers;
import com.hudhud.megalot.AppUtils.Responses.Intro;
import com.hudhud.megalot.AppUtils.Urls;
import com.hudhud.megalot.R;
import com.hudhud.megalot.Views.Registration.RegistrationActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class IntroFragment extends Fragment {

    NavController navController;
    RegistrationActivity registrationActivity;
    MaterialButton getStarted;
    ArrayList<Intro> introItems = new ArrayList<>();
    ViewPager viewPager;
    TabLayout tabLayout;
    ProgressDialog pDialog;
    public RequestQueue queue;

    public IntroFragment() {
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
        return inflater.inflate(R.layout.fragment_intro, container, false);
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
        getIntroItems();
    }

    private void initViews(View view){
        pDialog = new ProgressDialog(registrationActivity);
        queue = Volley.newRequestQueue(registrationActivity);
        navController = Navigation.findNavController(view);
        getStarted = view.findViewById(R.id.get_started);
        viewPager = view.findViewById(R.id.viewPager);
        tabLayout = view.findViewById(R.id.tabDots);
    }

    private void onClick(){
        getStarted.setOnClickListener(view -> navController.navigate(IntroFragmentDirections.actionIntroFragmentToSignInFragment()));
    }

    private void getIntroItems(){
        introItems.clear();
        showProgressDialog();
        StringRequest introItemsRequest = new StringRequest(Request.Method.GET, Urls.API_URL+"slider_splash", response -> {
            hideProgressDialog();
            try {
                JSONObject responseObj = new JSONObject(response);
                JSONArray resultsArray = responseObj.getJSONArray("results");
                for (int i=0; i<resultsArray.length(); i++){
                    JSONObject introItemObj = resultsArray.getJSONObject(i);
                    Intro introItem = new Intro(introItemObj.getString("id"), introItemObj.getString("title"), introItemObj.getString("description"), introItemObj.getString("image"), introItemObj.getString("status"));
                    introItems.add(introItem);
                }
                initSlider();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }, error -> {
            hideProgressDialog();
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Lang", AppDefs.getLanguage());
                return params;
            }
        };
        queue.add(introItemsRequest);
    }

    private void initSlider(){
        viewPager.setVisibility(View.VISIBLE);
        tabLayout.setupWithViewPager(viewPager, true);
        ViewPagerAdapter mAdapter = new ViewPagerAdapter(introItems, registrationActivity);
        mAdapter.notifyDataSetChanged();
        viewPager.setOffscreenPageLimit(introItems.size());
        viewPager.setAdapter(mAdapter);

        Helpers.setSliderTimer(3000,viewPager, mAdapter);
    }

    public void showProgressDialog() {
        pDialog.setMessage(getResources().getString(R.string.loading));
        pDialog.setCancelable(false);
        pDialog.show();
    }

    public void hideProgressDialog() {
        pDialog.hide();
    }
}