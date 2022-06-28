package com.hudhud.megalot.Views.Main.Notifications;

import static android.content.Context.MODE_PRIVATE;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.hudhud.megalot.AppUtils.AppDefs;
import com.hudhud.megalot.AppUtils.Responses.DataBottom;
import com.hudhud.megalot.AppUtils.Responses.Notification;
import com.hudhud.megalot.AppUtils.Responses.User;
import com.hudhud.megalot.AppUtils.Urls;
import com.hudhud.megalot.R;
import com.hudhud.megalot.Views.Main.MainActivity;
import com.hudhud.megalot.Views.Splash.SplashActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NotificationFragment extends Fragment {

    MainActivity mainActivity;
    RecyclerView notificationsRV;
    ArrayList<Notification> notifications = new ArrayList<>();
    TextView noNotifications;
    NestedScrollView nestedScrollView;
    int page = 1;

    public NotificationFragment() {
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
        return inflater.inflate(R.layout.fragment_notification, container, false);
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
        getNotifications();
        onClick();
    }

    private void initViews(View view){
        notificationsRV = view.findViewById(R.id.notifications_RV);
        noNotifications = view.findViewById(R.id.no_notifications);
        nestedScrollView = view.findViewById(R.id.nested_SV);
    }

    private void onClick(){
        nestedScrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()){
                page++;
                getNotifications();
            }
        });
    }

    private void getNotifications(){
        mainActivity.showProgressDialog();
        notifications.clear();
        StringRequest notificationsRequest = new StringRequest(Request.Method.GET, Urls.API_URL+"get_notifications/"+page, response -> {
            mainActivity.hideProgressDialog();
            try {
                JSONObject responseObj = new JSONObject(response);
                JSONArray resultsArray = responseObj.getJSONArray("results");
                for (int i=0; i<resultsArray.length(); i++){
                    JSONObject notificationObj = resultsArray.getJSONObject(i);
                    Notification notification = new Notification(notificationObj.getString("id"), notificationObj.getString("title"), notificationObj.getString("description"), notificationObj.getString("dates"), notificationObj.getString("times"));
                    notifications.add(notification);
                }
                if (notifications.size()>0){
                    noNotifications.setVisibility(View.GONE);
                    notificationsRV.setVisibility(View.VISIBLE);
                    setNotificationsRV();
                }else {
                    noNotifications.setVisibility(View.VISIBLE);
                    notificationsRV.setVisibility(View.GONE);
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
                            mainActivity.showResponseMessage(mainActivity.getResources().getString(R.string.notification), mainActivity.getResources().getString(R.string.internet_connection_error));
                        }else {
                            mainActivity.showResponseMessage(mainActivity.getResources().getString(R.string.notification), statusObj.getString("massage"));
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
                params.put("Lang", AppDefs.getLanguage());
                return params;
            }
        };
        mainActivity.queue.add(notificationsRequest);
    }

    private void setNotificationsRV(){
        NotificationsAdapter notificationsAdapter = new NotificationsAdapter(this, notifications);
        notificationsRV.setAdapter(notificationsAdapter);
        notificationsRV.setLayoutManager(new LinearLayoutManager(mainActivity));
    }

    public void deleteNotification(String id){
        mainActivity.showProgressDialog();
        JSONObject notificationObj = new JSONObject();
        try {
            notificationObj.put("id", id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest deleteNotificationRequest = new JsonObjectRequest(Request.Method.POST, Urls.API_URL+"delete_notifications", notificationObj, response -> {
            mainActivity.hideProgressDialog();
            try {
                JSONObject statusObj = response.getJSONObject("status");
                if (statusObj.getString("code").equals("200")){
                    getNotifications();
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
                            mainActivity.showResponseMessage(mainActivity.getResources().getString(R.string.notification), mainActivity.getResources().getString(R.string.internet_connection_error));
                        }else {
                            mainActivity.showResponseMessage(mainActivity.getResources().getString(R.string.notification), statusObj.getString("massage"));
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
        mainActivity.queue.add(deleteNotificationRequest);
    }

    public void showDeleteNotificationMessage(String id){
        AlertDialog.Builder msgDialog = new AlertDialog.Builder(mainActivity);
        msgDialog.setTitle(mainActivity.getResources().getString(R.string.delete_notification));
        msgDialog.setMessage(mainActivity.getResources().getString(R.string.notification_desc));

        msgDialog.setNegativeButton(mainActivity.getResources().getString(R.string.delete), (dialogInterface, i) -> {
            deleteNotification(id);
        });

        msgDialog.setPositiveButton(getResources().getString(R.string.cancel), (dialogInterface, i) -> dialogInterface.dismiss());
        msgDialog.show();
    }
}