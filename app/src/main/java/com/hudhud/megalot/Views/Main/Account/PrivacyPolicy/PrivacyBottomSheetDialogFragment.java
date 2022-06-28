package com.hudhud.megalot.Views.Main.Account.PrivacyPolicy;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.hudhud.megalot.AppUtils.AppDefs;
import com.hudhud.megalot.AppUtils.Responses.DataBottom;
import com.hudhud.megalot.AppUtils.Urls;
import com.hudhud.megalot.R;
import com.hudhud.megalot.Views.Main.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PrivacyBottomSheetDialogFragment extends BottomSheetDialogFragment {

    MainActivity mainActivity;
    ImageView close;
    RecyclerView privacyRV;
    ArrayList<DataBottom> privacyData = new ArrayList<>();

    public PrivacyBottomSheetDialogFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_privacy_bottom_sheet_dialog, container, false);
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
        getPrivacy();
    }

    private void initViews(View view){
        close = view.findViewById(R.id.close);
        privacyRV = view.findViewById(R.id.privacy_RV);
    }

    private void onClick(){
        close.setOnClickListener(view -> dismiss());
    }

    private void getPrivacy(){
        mainActivity.showProgressDialog();
        privacyData.clear();
        StringRequest privacyRequest = new StringRequest(Request.Method.GET, Urls.API_URL+"privacy_policy", response -> {
            mainActivity.hideProgressDialog();
            try {
                JSONObject responseObj = new JSONObject(response);
                JSONArray resultsArray = responseObj.getJSONArray("results");
                for (int i=0; i<resultsArray.length(); i++){
                    JSONObject privacyObj = resultsArray.getJSONObject(i);
                    DataBottom privacy = new DataBottom(privacyObj.getString("id"), privacyObj.getString("title"), privacyObj.getString("description"));
                    privacyData.add(privacy);
                }
                setPrivacyRV();
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
                            mainActivity.showResponseMessage(mainActivity.getResources().getString(R.string.privacy_policy), mainActivity.getResources().getString(R.string.internet_connection_error));
                        }else {
                            mainActivity.showResponseMessage(mainActivity.getResources().getString(R.string.privacy_policy), statusObj.getString("massage"));
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
                params.put("Lang", AppDefs.getLanguage());
                return params;
            }
        };
        mainActivity.queue.add(privacyRequest);
    }

    private void setPrivacyRV(){
        PrivacyAdapter privacyAdapter = new PrivacyAdapter(privacyData);
        privacyRV.setAdapter(privacyAdapter);
        privacyRV.setLayoutManager(new LinearLayoutManager(mainActivity));
    }
}