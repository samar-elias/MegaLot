package com.hudhud.megalot.Views.Main.Account.AboutUs;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
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

public class AboutUsBottomSheetDialogFragment extends BottomSheetDialogFragment {

    MainActivity mainActivity;
    ImageView close;
    RecyclerView aboutRV;
    ArrayList<DataBottom> aboutData = new ArrayList<>();

    public AboutUsBottomSheetDialogFragment() {
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
        return inflater.inflate(R.layout.fragment_about_us_bottom_sheet_dialog, container, false);
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
        getAbout();
    }

    private void initViews(View view){
        close = view.findViewById(R.id.close);
        aboutRV = view.findViewById(R.id.about_RV);
    }

    private void onClick(){
        close.setOnClickListener(view -> dismiss());
    }

    private void getAbout(){
        mainActivity.showProgressDialog();
        aboutData.clear();
        StringRequest aboutRequest = new StringRequest(Request.Method.GET, Urls.API_URL+"get_about", response -> {
            mainActivity.hideProgressDialog();
            try {
                JSONObject responseObj = new JSONObject(response);
                JSONArray resultsArray = responseObj.getJSONArray("results");
                for (int i=0; i<resultsArray.length(); i++){
                    JSONObject aboutObj = resultsArray.getJSONObject(i);
                    DataBottom about = new DataBottom(aboutObj.getString("id"), aboutObj.getString("title"), aboutObj.getString("description"));
                    aboutData.add(about);
                }
                setAboutRV();
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
                            mainActivity.showResponseMessage(mainActivity.getResources().getString(R.string.about_us), mainActivity.getResources().getString(R.string.internet_connection_error));
                        }else {
                            mainActivity.showResponseMessage(mainActivity.getResources().getString(R.string.about_us), statusObj.getString("massage"));
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
        mainActivity.queue.add(aboutRequest);
    }

    private void setAboutRV(){
        AboutAdapter aboutAdapter = new AboutAdapter(aboutData);
        aboutRV.setAdapter(aboutAdapter);
        aboutRV.setLayoutManager(new LinearLayoutManager(mainActivity));
    }
}