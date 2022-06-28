package com.hudhud.megalot.Views.Main.Home;

import android.content.Context;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.hudhud.megalot.AppUtils.AppDefs;
import com.hudhud.megalot.AppUtils.Urls;
import com.hudhud.megalot.R;
import com.hudhud.megalot.Views.Main.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class WithdrawBottomSheetDialogFragment extends BottomSheetDialogFragment {

    MainActivity mainActivity;
    String balance = "";
    TextView myBalance, withdrawalBtn;
    ImageView close;

    public WithdrawBottomSheetDialogFragment() {
        // Required empty public constructor
    }

    public WithdrawBottomSheetDialogFragment(String balance) {
        this.balance = balance;
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
        return inflater.inflate(R.layout.fragment_withdraw_bottom_sheet_dialog, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        setData();
        onClick();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof MainActivity) {
            mainActivity = (MainActivity) context;
        }
    }

    private void initViews(View view){
        myBalance = view.findViewById(R.id.my_balance);
        withdrawalBtn = view.findViewById(R.id.withdrawal_btn);
        close = view.findViewById(R.id.close);
    }

    private void setData(){
        myBalance.setText(balance);
    }

    private void onClick(){
        close.setOnClickListener(view -> dismiss());
        withdrawalBtn.setOnClickListener(view -> {
            if (Double.parseDouble(balance)<10.00){
                mainActivity.showResponseMessage(mainActivity.getResources().getString(R.string.withdrawal), mainActivity.getResources().getString(R.string.low_balance));
            }else {
                addWithdraw();
            }
        });
    }

    private void addWithdraw(){
        mainActivity.showProgressDialog();
        JSONObject withdrawObj = new JSONObject();
        try {
            withdrawObj.put("price", balance);
            Toast.makeText(mainActivity, mainActivity.getResources().getString(R.string.withdraw_request), Toast.LENGTH_SHORT).show();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest withdrawRequest = new JsonObjectRequest(Request.Method.POST, Urls.API_URL+"add_withdraw", withdrawObj, response -> {
            mainActivity.hideProgressDialog();
        }, error -> {
            mainActivity.hideProgressDialog();
            JSONObject body;
            if(error.networkResponse.data!=null) {
                try {
                    try {
                        body = new JSONObject(new String(error.networkResponse.data,"UTF-8"));
                        JSONObject statusObj = body.getJSONObject("status");
                        if (statusObj.getString("code").equals("500")){
                            mainActivity.showResponseMessage(mainActivity.getResources().getString(R.string.withdraw), mainActivity.getResources().getString(R.string.internet_connection_error));
                        }else {
                            mainActivity.showResponseMessage(mainActivity.getResources().getString(R.string.withdraw), statusObj.getString("massage"));
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
        mainActivity.queue.add(withdrawRequest);
    }
}