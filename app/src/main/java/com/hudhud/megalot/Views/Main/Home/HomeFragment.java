package com.hudhud.megalot.Views.Main.Home;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.messaging.FirebaseMessaging;
import com.hudhud.megalot.AppUtils.AppDefs;
import com.hudhud.megalot.AppUtils.Responses.LastDrawWinner;
import com.hudhud.megalot.AppUtils.Responses.NextDraw;
import com.hudhud.megalot.AppUtils.Urls;
import com.hudhud.megalot.BuildConfig;
import com.hudhud.megalot.Views.Main.MainActivity;
import com.hudhud.megalot.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class HomeFragment extends Fragment {

    NavController navController;
    ProgressDialog pDialog;
    RequestQueue queue;
    MainActivity mainActivity;
    MaterialButton withdrawBtn;
    MaterialCardView previousWins, myTickets, myWithdrawals, inviteFriends;
    LinearLayoutCompat drawResults;
    TextView totalBalance, totalTickets, welcomeUser, date, days, hours, mins, seconds, nextDrawDate, nextDrawPrize, superWinnerUsername, lastDrawPrize, winningDate, myPrize, no1, no2, no3, no4, no5, no6;
    NextDraw nextDraw = new NextDraw();
    private DecimalFormat formatter = new DecimalFormat("#0.00");
    double prize = 0.00, increaseValue = 0.00;
    String balance = "", tokenId = "";
    LastDrawWinner lastDrawWinner = new LastDrawWinner();

    public HomeFragment() {
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
        return inflater.inflate(R.layout.fragment_home, container, false);
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
        getNextDraw();
        getLastDraw();
        getToken();
    }

    private void initViews(View view){
        pDialog = new ProgressDialog(mainActivity);
        queue = Volley.newRequestQueue(mainActivity);
        navController = Navigation.findNavController(view);
        totalBalance = view.findViewById(R.id.total_balance);
        totalTickets = view.findViewById(R.id.total_tickets);
        withdrawBtn = view.findViewById(R.id.withdraw);
        previousWins = view.findViewById(R.id.previous_wins);
        drawResults = view.findViewById(R.id.draw_results);
        myTickets = view.findViewById(R.id.my_participations);
        myWithdrawals = view.findViewById(R.id.my_withdrawals);
        inviteFriends = view.findViewById(R.id.invite_friends);
        welcomeUser = view.findViewById(R.id.welcome_name);
        date = view.findViewById(R.id.date);
        days = view.findViewById(R.id.days);
        hours = view.findViewById(R.id.hours);
        mins = view.findViewById(R.id.minutes);
        seconds = view.findViewById(R.id.seconds);
        nextDrawDate = view.findViewById(R.id.next_draw_date);
        nextDrawPrize = view.findViewById(R.id.next_draw_prize);
        superWinnerUsername = view.findViewById(R.id.super_winner);
        lastDrawPrize = view.findViewById(R.id.last_draw_prize);
        winningDate = view.findViewById(R.id.winning_number);
        myPrize = view.findViewById(R.id.my_prize);
        no1 = view.findViewById(R.id.no1);
        no2 = view.findViewById(R.id.no2);
        no3 = view.findViewById(R.id.no3);
        no4 = view.findViewById(R.id.no4);
        no5 = view.findViewById(R.id.no5);
        no6 = view.findViewById(R.id.no6);
    }

    private void onClick(){
        withdrawBtn.setOnClickListener(view -> {
            WithdrawBottomSheetDialogFragment withdrawBottomSheetDialogFragment = new WithdrawBottomSheetDialogFragment(balance);
            withdrawBottomSheetDialogFragment.show(getChildFragmentManager(),
                    "withdraw_dialog_fragment");
        });
        previousWins.setOnClickListener(view -> navController.navigate(HomeFragmentDirections.actionHomeFragmentToPreviousWinsFragment()));
        drawResults.setOnClickListener(view -> navController.navigate(HomeFragmentDirections.actionHomeFragmentToDrawResultsFragment(lastDrawWinner.getId())));
        myTickets.setOnClickListener(view -> navController.navigate(HomeFragmentDirections.actionHomeFragmentToMyTicketsFragment2()));
        myWithdrawals.setOnClickListener(view -> navController.navigate(HomeFragmentDirections.actionHomeFragmentToMyWithdrawalsFragment()));
        inviteFriends.setOnClickListener(view -> shareApp());
    }

    private void setData(){
        String firstName = "";
        if (AppDefs.user.getName().contains(" ")) {
            firstName = AppDefs.user.getName().substring(0, AppDefs.user.getName().indexOf(" "));
        }else {
            firstName = AppDefs.user.getName();
        }
        welcomeUser.setText(mainActivity.getResources().getString(R.string.welcome)+" "+ firstName);

        Date c = Calendar.getInstance().getTime();

        SimpleDateFormat df = new SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault());
        String formattedDate = df.format(c);
        date.setText(formattedDate);

    }

    private void getNextDraw(){
        showProgressDialog();
        nextDraw = new NextDraw();
        StringRequest nextDrawRequest = new StringRequest(Request.Method.GET, Urls.API_URL+"get_draw", response -> {
            hideProgressDialog();
            try {
                JSONObject responseObj = new JSONObject(response);
                JSONObject resultObj = responseObj.getJSONObject("results");
                nextDraw.setId(resultObj.getString("id"));
                nextDraw.setPrice(resultObj.getString("price"));
                nextDraw.setDate(resultObj.getString("date"));
                nextDraw.setDateTime(resultObj.getString("date_time"));
                nextDraw.setIncreaseValue(resultObj.getString("increase_value"));

                prize = Double.parseDouble(nextDraw.getPrice());
                increaseValue = ((Double.parseDouble(nextDraw.getIncreaseValue())/24)/60)/60;
                nextDrawDate.setText(nextDraw.getDate());
                nextDrawPrize.setText(formatter.format(prize));

                AppDefs.nextDraw = nextDraw;

                convertToMilliSeconds(nextDraw.getDateTime());
                getTicketData();
                updateTickets();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            hideProgressDialog();
            JSONObject body;
            if(error.networkResponse.data!=null) {
                try {
                    try {
                        body = new JSONObject(new String(error.networkResponse.data,"UTF-8"));
                        JSONObject statusObj = body.getJSONObject("status");
                        if (statusObj.getString("code").equals("500")){
                            showResponseMessage(mainActivity.getResources().getString(R.string.next_draw), mainActivity.getResources().getString(R.string.internet_connection_error));
                        }else {
                            showResponseMessage(mainActivity.getResources().getString(R.string.next_draw), statusObj.getString("massage"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });
        queue.add(nextDrawRequest);
    }

    @SuppressLint("NewApi")
    private void convertToMilliSeconds(String date){
        String newDate = date.replace("/", " ");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy MM dd HH:mm:ss", Locale.ENGLISH);
        LocalDateTime localDate = LocalDateTime.parse(newDate, formatter);
        long timeInMilliseconds = localDate.atOffset(ZoneOffset.UTC).toInstant().toEpochMilli();
        Log.d("timeDate", String.valueOf(timeInMilliseconds));

        Date c = Calendar.getInstance().getTime();

        SimpleDateFormat df = new SimpleDateFormat("yyyy MM dd HH:mm:ss", Locale.getDefault());
        String formattedDate = df.format(c);
        DateTimeFormatter formatterCurrent = DateTimeFormatter.ofPattern("yyyy MM dd HH:mm:ss", Locale.ENGLISH);
        LocalDateTime localDateCurrent = LocalDateTime.parse(formattedDate, formatterCurrent);
        long timeInMillisecondsCurrent = localDateCurrent.atOffset(ZoneOffset.UTC).toInstant().toEpochMilli();
        Log.d("timeDate", String.valueOf(timeInMillisecondsCurrent));

        startCountDown(timeInMilliseconds-timeInMillisecondsCurrent);
    }

    public void startCountDown(long daysIMillis){
        /*    creating object for all text views    */

    /*    172800000 milliseconds = 5days

    1000(1sec) is time interval to call onTick method

    millisUntilFinished is amount of until finished

    */

        new CountDownTimer(daysIMillis, 1000){

            @Override

            public void onTick(long millisUntilFinished) {
                /*            converting the milliseconds into days, hours, minutes and seconds and displaying it in textviews             */

                long s = millisUntilFinished / 1000;
                long m = s / 60;
                long h = m / 60;
                long d = h / 24;

                days.setText(d+"");
                hours.setText(h%24+"");
                mins.setText(m%60+"");
                seconds.setText(s%60+"");

                AppDefs.totalIncreasedValue += increaseValue;
                prize+=increaseValue;
                nextDrawPrize.setText(formatter.format(prize));

            }

            @Override

            public void onFinish() {
                /*            clearing all fields and displaying countdown finished message             */

                days.setText("0");
                hours.setText("00");
                mins.setText("00");
                seconds.setText("00");
            }
        }.start();
    }

    private void getLastDraw(){
        showProgressDialog();
        lastDrawWinner = new LastDrawWinner();
        StringRequest lastDrawRequest = new StringRequest(Request.Method.GET, Urls.API_URL+"super_winner_darw", response -> {
            hideProgressDialog();
            try {
                JSONObject responseObj = new JSONObject(response);
                JSONObject resultObj = responseObj.getJSONObject("results");
                lastDrawWinner.setId(resultObj.getString("id"));
                lastDrawWinner.setPrize(resultObj.getString("price"));
                lastDrawWinner.setIncreaseValue(resultObj.getString("increase_value"));
                lastDrawWinner.setDate(resultObj.getString("date"));
                lastDrawWinner.setDateTime(resultObj.getString("date_time"));
                lastDrawWinner.setNo1(resultObj.getString("no_1"));
                lastDrawWinner.setNo2(resultObj.getString("no_2"));
                lastDrawWinner.setNo3(resultObj.getString("no_3"));
                lastDrawWinner.setNo4(resultObj.getString("no_4"));
                lastDrawWinner.setNo5(resultObj.getString("no_5"));
                lastDrawWinner.setNo6(resultObj.getString("no_6"));
                lastDrawWinner.setMyPrize(resultObj.getString("my_participation"));
                JSONObject usernameObj = resultObj.getJSONObject("big_winner");
                lastDrawWinner.setUsername(usernameObj.getString("Username"));

                lastDrawPrize.setText(formatter.format(Double.parseDouble(lastDrawWinner.getPrize())));
                superWinnerUsername.setText(lastDrawWinner.getUsername());
                winningDate.setText(mainActivity.getResources().getString(R.string.winning_numbers)+" "+lastDrawWinner.getDate());
                myPrize.setText(formatter.format(Double.parseDouble(lastDrawWinner.getMyPrize())));
                no1.setText(lastDrawWinner.getNo1());
                no2.setText(lastDrawWinner.getNo2());
                no3.setText(lastDrawWinner.getNo3());
                no4.setText(lastDrawWinner.getNo4());
                no5.setText(lastDrawWinner.getNo5());
                no6.setText(lastDrawWinner.getNo6());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            hideProgressDialog();
            JSONObject body;
            if(error.networkResponse.data!=null) {
                try {
                    try {
                        body = new JSONObject(new String(error.networkResponse.data,"UTF-8"));
                        JSONObject statusObj = body.getJSONObject("status");
                        if (statusObj.getString("code").equals("500")){
                            showResponseMessage(mainActivity.getResources().getString(R.string.next_draw), mainActivity.getResources().getString(R.string.internet_connection_error));
                        }else {
                            showResponseMessage(mainActivity.getResources().getString(R.string.next_draw), statusObj.getString("massage"));
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
        queue.add(lastDrawRequest);
    }

    private void shareApp(){
        try {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "MegaLot");
            String shareMessage= mainActivity.getResources().getString(R.string.recommend_app)+"\n\n";
            shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID +"\n\n";
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
            startActivity(Intent.createChooser(shareIntent, "choose one"));
        } catch(Exception e) {
        }
    }

    private void getTicketData(){
        JSONObject drawIdObj = new JSONObject();
        try {
            drawIdObj.put("drawId", nextDraw.getId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest ticketDataRequest = new JsonObjectRequest(Request.Method.POST, Urls.API_URL+"get_count_tickets_balance", drawIdObj, response -> {
            try {
                JSONObject resultObj = response.getJSONObject("results");
                totalBalance.setText(resultObj.getString("countBalance"));
                balance = resultObj.getString("countBalance");
                totalTickets.setText(resultObj.getString("countTickets"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            JSONObject body;
            if(error.networkResponse.data!=null) {
                try {
                    try {
                        body = new JSONObject(new String(error.networkResponse.data,"UTF-8"));
                        JSONObject statusObj = body.getJSONObject("status");
                        if (statusObj.getString("code").equals("500")){
                            showResponseMessage(mainActivity.getResources().getString(R.string.home), mainActivity.getResources().getString(R.string.internet_connection_error));
                        }else {
                            showResponseMessage(mainActivity.getResources().getString(R.string.home), statusObj.getString("massage"));
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
        queue.add(ticketDataRequest);
    }

    private void updateTickets(){
        JSONObject drawIdObj = new JSONObject();
        try {
            drawIdObj.put("drawId", nextDraw.getId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest ticketDataRequest = new JsonObjectRequest(Request.Method.POST, Urls.API_URL+"update_tickets", drawIdObj, response -> {

        }, error -> {
            JSONObject body;
            if(error.networkResponse.data!=null) {
                try {
                    try {
                        body = new JSONObject(new String(error.networkResponse.data,"UTF-8"));
                        JSONObject statusObj = body.getJSONObject("status");
                        if (statusObj.getString("code").equals("500")){
                            showResponseMessage(mainActivity.getResources().getString(R.string.home), mainActivity.getResources().getString(R.string.internet_connection_error));
                        }else {
//                            showResponseMessage(mainActivity.getResources().getString(R.string.home), statusObj.getString("massage"));
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
        queue.add(ticketDataRequest);
    }

    private void getToken() {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.w("FAILED", "Fetching FCM registration token failed", task.getException());
                        return;
                    }
                    tokenId = task.getResult();
                    updateToken();
                });
    }

    private void updateToken(){
        JSONObject updateTokenObj = new JSONObject();
        try {
            updateTokenObj.put("fcm_token", tokenId);
            updateTokenObj.put("type_token", "1");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest updateTokenRequest = new JsonObjectRequest(Request.Method.POST, Urls.API_URL+"update_token", updateTokenObj, response -> {

        }, error -> {
            JSONObject body;
            if(error.networkResponse.data!=null) {
                try {
                    try {
                        body = new JSONObject(new String(error.networkResponse.data,"UTF-8"));
                        JSONObject statusObj = body.getJSONObject("status");
                        if (statusObj.getString("code").equals("500")){
                            showResponseMessage(mainActivity.getResources().getString(R.string.home), mainActivity.getResources().getString(R.string.internet_connection_error));
                        }else {
//                            showResponseMessage(mainActivity.getResources().getString(R.string.home), statusObj.getString("massage"));
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
        queue.add(updateTokenRequest);
    }

    public void showProgressDialog() {
        pDialog.setMessage(getResources().getString(R.string.loading));
        pDialog.setCancelable(false);
        pDialog.show();
    }

    public void hideProgressDialog() {
        pDialog.hide();
    }

    public void showResponseMessage(String title, String msg) {
        AlertDialog.Builder msgDialog = new AlertDialog.Builder(mainActivity);
        msgDialog.setTitle(title);
        msgDialog.setMessage(msg);
        msgDialog.setCancelable(false);

        msgDialog.setPositiveButton(getResources().getString(R.string.done), (dialogInterface, i) -> dialogInterface.dismiss());
        msgDialog.show();
    }
}