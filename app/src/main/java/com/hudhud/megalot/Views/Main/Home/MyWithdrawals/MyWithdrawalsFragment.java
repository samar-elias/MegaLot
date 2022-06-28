package com.hudhud.megalot.Views.Main.Home.MyWithdrawals;

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
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.button.MaterialButton;
import com.hudhud.megalot.AppUtils.AppDefs;
import com.hudhud.megalot.AppUtils.Responses.Withdraw;
import com.hudhud.megalot.AppUtils.Urls;
import com.hudhud.megalot.Views.Main.Home.WithdrawBottomSheetDialogFragment;
import com.hudhud.megalot.Views.Main.MainActivity;
import com.hudhud.megalot.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MyWithdrawalsFragment extends Fragment {

    NavController navController;
    MainActivity mainActivity;
    MaterialButton withdrawBtn;
    TextView withdrawnAmount, totalAmount;
    RecyclerView withdrawsRV;
    ImageView navigateBack;
    ArrayList<Withdraw> withdraws = new ArrayList<>();
    private DecimalFormat formatter = new DecimalFormat("#0.00");
    double balance = 0.00;

    public MyWithdrawalsFragment() {
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
        return inflater.inflate(R.layout.fragment_my_withdrawals, container, false);
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
        getWithdraws();
    }

    private void initViews(View view){
        navController = Navigation.findNavController(view);
        navigateBack = view.findViewById(R.id.navigate_back);
        withdrawBtn = view.findViewById(R.id.withdraw);
        withdrawnAmount = view.findViewById(R.id.withdraw_amount);
        totalAmount = view.findViewById(R.id.total_amount);
        withdrawsRV = view.findViewById(R.id.withdraws_RV);
    }

    private void onClick(){
        navigateBack.setOnClickListener(view -> navController.popBackStack());
        withdrawBtn.setOnClickListener(view -> {
            WithdrawBottomSheetDialogFragment withdrawBottomSheetDialogFragment = new WithdrawBottomSheetDialogFragment(String.valueOf(balance));
            withdrawBottomSheetDialogFragment.show(getChildFragmentManager(),
                    "withdraw_dialog_fragment");
        });
    }

    private void getWithdraws(){
        mainActivity.showProgressDialog();
        withdraws.clear();
        StringRequest withdrawsRequest = new StringRequest(Request.Method.GET, Urls.API_URL+"get_withdraw", response -> {
            mainActivity.hideProgressDialog();
            try {
                JSONObject responseObj = new JSONObject(response);
                JSONObject resultObj = responseObj.getJSONObject("results");
                withdrawnAmount.setText(formatter.format(Double.parseDouble(resultObj.getString("withdrawn_amount"))));
                totalAmount.setText(formatter.format(Double.parseDouble(resultObj.getString("total_accumulated_amount"))));
                balance = Double.parseDouble(resultObj.getString("total_accumulated_amount"))-Double.parseDouble(resultObj.getString("withdrawn_amount"));
                JSONArray withdrawsArray = resultObj.getJSONArray("withdraw");
                for (int i=0; i<withdrawsArray.length(); i++){
                    JSONObject withdrawObj = withdrawsArray.getJSONObject(i);
                    Withdraw withdraw = new Withdraw();
                    withdraw.setId(withdrawObj.getString("id"));
                    withdraw.setClientId(withdrawObj.getString("clientId"));
                    withdraw.setPrice(withdrawObj.getString("price"));
                    withdraw.setDay(withdrawObj.getString("days"));
                    withdraw.setDate(withdrawObj.getString("dates"));
                    withdraw.setStatus(withdrawObj.getString("status"));
                    withdraws.add(withdraw);
                }
                setWithdrawsRV();
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
                            mainActivity.showResponseMessage(mainActivity.getResources().getString(R.string.next_draw), mainActivity.getResources().getString(R.string.internet_connection_error));
                        }else {
                            mainActivity.showResponseMessage(mainActivity.getResources().getString(R.string.next_draw), statusObj.getString("massage"));
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
        mainActivity.queue.add(withdrawsRequest);
    }

    private void setWithdrawsRV(){
        WithdrawsAdapter withdrawsAdapter = new WithdrawsAdapter(withdraws, this);
        withdrawsRV.setAdapter(withdrawsAdapter);
        withdrawsRV.setLayoutManager(new LinearLayoutManager(mainActivity));
    }

}