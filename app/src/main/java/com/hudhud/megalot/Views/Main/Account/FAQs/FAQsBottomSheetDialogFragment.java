package com.hudhud.megalot.Views.Main.Account.FAQs;

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

public class FAQsBottomSheetDialogFragment extends BottomSheetDialogFragment {

    MainActivity mainActivity;
    ImageView close;
    RecyclerView faqRV;
    ArrayList<DataBottom> faqData = new ArrayList<>();

    public FAQsBottomSheetDialogFragment() {
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
        return inflater.inflate(R.layout.fragment_f_a_qs_bottom_sheet_dialog, container, false);
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
        getFAQs();
    }

    private void initViews(View view){
        close = view.findViewById(R.id.close);
        faqRV = view.findViewById(R.id.faq_RV);
    }

    private void onClick(){
        close.setOnClickListener(view -> dismiss());
    }

    private void getFAQs(){
        mainActivity.showProgressDialog();
        faqData.clear();
        StringRequest faqRequest = new StringRequest(Request.Method.GET, Urls.API_URL+"get_faq", response -> {
            mainActivity.hideProgressDialog();
            try {
                JSONObject responseObj = new JSONObject(response);
                JSONArray resultsArray = responseObj.getJSONArray("results");
                for (int i=0; i<resultsArray.length(); i++){
                    JSONObject faqObj = resultsArray.getJSONObject(i);
                    DataBottom faq = new DataBottom(faqObj.getString("id"), faqObj.getString("title"), faqObj.getString("description"));
                    faqData.add(faq);
                }
                setFAQsRV();
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
                            mainActivity.showResponseMessage(mainActivity.getResources().getString(R.string.frequently_asked_questions), mainActivity.getResources().getString(R.string.internet_connection_error));
                        }else {
                            mainActivity.showResponseMessage(mainActivity.getResources().getString(R.string.frequently_asked_questions), statusObj.getString("massage"));
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
        mainActivity.queue.add(faqRequest);
    }

    private void setFAQsRV(){
        FAQAdapter faqAdapter = new FAQAdapter(faqData);
        faqRV.setAdapter(faqAdapter);
        faqRV.setLayoutManager(new LinearLayoutManager(mainActivity));
    }
}