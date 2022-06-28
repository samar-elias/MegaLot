package com.hudhud.megalot.Views.Main.Home.PreviousWins;

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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.hudhud.megalot.AppUtils.AppDefs;
import com.hudhud.megalot.AppUtils.Responses.BigWinner;
import com.hudhud.megalot.AppUtils.Responses.Date;
import com.hudhud.megalot.AppUtils.Responses.Draw;
import com.hudhud.megalot.AppUtils.Responses.DrawResult;
import com.hudhud.megalot.AppUtils.Urls;
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

public class PreviousWinsFragment extends Fragment {

    NavController navController;
    MainActivity mainActivity;
    ImageView navigateBack;
    MaterialButton viewMyTicket;
    private DecimalFormat formatter = new DecimalFormat("#0.00");
    TextView date, drawPrizeAmount, myWinningAmount, bigWinnerWinningAmount, bigWinnerTotalTickets, bigWinnerWinningTickets, bigWinnerName, no1, no2, no3, no4, no5, no6;
    RecyclerView resultsRV, datesRV;
    ImageView nextDate, previousDate;
    Draw draw = new Draw();
    ArrayList<Date> dates = new ArrayList<>();
    int position = 0;

    public PreviousWinsFragment() {
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
        return inflater.inflate(R.layout.fragment_previous_wins, container, false);
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
        geDates();
    }

    private void initViews(View view){
        navController = Navigation.findNavController(view);
        navigateBack = view.findViewById(R.id.navigate_back);
        date = view.findViewById(R.id.date);
        datesRV = view.findViewById(R.id.dates_RV);
        nextDate = view.findViewById(R.id.next_date);
        previousDate = view.findViewById(R.id.previous_date);
        viewMyTicket = view.findViewById(R.id.view_my_ticket);
        drawPrizeAmount = view.findViewById(R.id.prize_amount);
        myWinningAmount = view.findViewById(R.id.my_winning_amount);
        bigWinnerWinningAmount = view.findViewById(R.id.big_winner_winning_amount);
        bigWinnerTotalTickets = view.findViewById(R.id.big_winner_total_tickets);
        bigWinnerWinningTickets = view.findViewById(R.id.big_winner_winning_tickets);
        bigWinnerName = view.findViewById(R.id.big_winner_name);
        resultsRV = view.findViewById(R.id.results_RV);
        no1 = view.findViewById(R.id.no1);
        no2 = view.findViewById(R.id.no2);
        no3 = view.findViewById(R.id.no3);
        no4 = view.findViewById(R.id.no4);
        no5 = view.findViewById(R.id.no5);
        no6 = view.findViewById(R.id.no6);
    }

    private void onClick(){
        navigateBack.setOnClickListener(view -> navController.popBackStack());
        viewMyTicket.setOnClickListener(view -> navController.navigate(PreviousWinsFragmentDirections.actionPreviousWinsFragmentToMyTicketsFragment(draw.getId())));
        nextDate.setOnClickListener(view -> {
            if (position != dates.size()-1){
                position++;
                setDate(dates.get(position).getDate());
            }
        });
        previousDate.setOnClickListener(view -> {
            if (position != 0){
                position--;
                setDate(dates.get(position).getDate());
            }
        });
    }

    private void setDate(String dateStr){
        date.setText(dateStr);
        getDrawByDate(dateStr);
        if (position == dates.size()-1){
            Glide.with(mainActivity).load(R.drawable.gray_arrow2).into(nextDate);
            nextDate.setEnabled(false);
            nextDate.setScaleX(1);
        }else {
            Glide.with(mainActivity).load(R.drawable.gray_arrow_previous).into(nextDate);
            nextDate.setEnabled(true);
            nextDate.setScaleX(-1);
        }

        if (position == 0){
            Glide.with(mainActivity).load(R.drawable.gray_arrow2).into(previousDate);
            previousDate.setEnabled(false);
            previousDate.setScaleX(-1);
        }else {
            Glide.with(mainActivity).load(R.drawable.gray_arrow_previous).into(previousDate);
            previousDate.setEnabled(true);
            previousDate.setScaleX(1);
        }
    }

    private void geDates(){
        mainActivity.showProgressDialog();
        dates.clear();
        StringRequest datesRequest = new StringRequest(Request.Method.GET, Urls.API_URL+"get_All_draw", response -> {
            mainActivity.hideProgressDialog();
            try {
                JSONObject responseObj = new JSONObject(response);
                JSONArray resultsArray = responseObj.getJSONArray("results");
                for (int i=0; i<resultsArray.length(); i++){
                    JSONObject dateObj = resultsArray.getJSONObject(i);
                    dates.add(new Date(dateObj.getString("date")));
                }
                setDatesRV();
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
                            mainActivity.showResponseMessage(mainActivity.getResources().getString(R.string.draw_results), mainActivity.getResources().getString(R.string.internet_connection_error));
                        }else {
                            mainActivity.showResponseMessage(mainActivity.getResources().getString(R.string.draw_results), statusObj.getString("massage"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });
        mainActivity.queue.add(datesRequest);
    }
    
    private void setDatesRV(){
        date.setText(dates.get(dates.size()-1).getDate());
        getDrawByDate(dates.get(dates.size()-1).getDate());
        position = dates.size()-1;
        nextDate.setEnabled(false);
//        DatesAdapter datesAdapter = new DatesAdapter(reverseDates, this);
//        datesRV.setAdapter(datesAdapter);
//        datesRV.setLayoutManager(new LinearLayoutManager(mainActivity, RecyclerView.HORIZONTAL, false));
    }

    private void getDrawByDate(String date){
        mainActivity.showProgressDialog();
        draw = new Draw();
        JSONObject drawIdObj = new JSONObject();
        try {
            drawIdObj.put("date",date);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest drawRequest = new JsonObjectRequest(Request.Method.POST, Urls.API_URL+"get_draw_by_date", drawIdObj, response -> {
            mainActivity.hideProgressDialog();
            try {
                JSONObject resultObj = response.getJSONObject("results");
                draw.setId(resultObj.getString("id"));
                draw.setPrize(resultObj.getString("price"));
                draw.setIncreaseValue(resultObj.getString("increase_value"));
                draw.setDate(resultObj.getString("date"));
                draw.setDateTime(resultObj.getString("date_time"));
                draw.setMyPrize(resultObj.getString("my_participation"));
                draw.setNo1(resultObj.getString("no_1"));
                draw.setNo2(resultObj.getString("no_2"));
                draw.setNo3(resultObj.getString("no_3"));
                draw.setNo4(resultObj.getString("no_4"));
                draw.setNo5(resultObj.getString("no_5"));
                draw.setNo6(resultObj.getString("no_6"));

                JSONObject bigWinnerObj = resultObj.getJSONObject("big_winner");
                if (bigWinnerObj.getString("Username").isEmpty()){
                    BigWinner bigWinner = new BigWinner("", "0", "0", "0");
                    draw.setBigWinner(bigWinner);
                }else {
                    BigWinner bigWinner = new BigWinner(bigWinnerObj.getString("Username"), bigWinnerObj.getString("winning_amount"), bigWinnerObj.getString("total_combinations"), bigWinnerObj.getString("winning_combinations"));
                    draw.setBigWinner(bigWinner);
                }

                JSONArray resultsArray = resultObj.getJSONArray("results_center");
                ArrayList<DrawResult> drawResults = new ArrayList<>();
                for (int i=0; i<resultsArray.length(); i++){
                    JSONObject drawResultObj = resultsArray.getJSONObject(i);
                    DrawResult drawResult = new DrawResult(drawResultObj.getString("winning_amount"), drawResultObj.getString("total_combinations"), drawResultObj.getString("winning_combinations"));
                    drawResults.add(drawResult);
                }
                draw.setDrawResults(drawResults);

                setData();
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
                            mainActivity.showResponseMessage(mainActivity.getResources().getString(R.string.draw_results), mainActivity.getResources().getString(R.string.internet_connection_error));
                        }else {
                            mainActivity.showResponseMessage(mainActivity.getResources().getString(R.string.draw_results), statusObj.getString("massage"));
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
        mainActivity.queue.add(drawRequest);
    }

    private void setData(){
        drawPrizeAmount.setText(formatter.format(Double.parseDouble(draw.getPrize())));
        myWinningAmount.setText(formatter.format(Double.parseDouble(draw.getMyPrize())));
        bigWinnerName.setText(draw.getBigWinner().getUsername());
        bigWinnerWinningTickets.setText(draw.getBigWinner().getWinningCombinations());
        bigWinnerWinningAmount.setText(formatter.format(Double.parseDouble(draw.getBigWinner().getWinningAmount())));
        bigWinnerTotalTickets.setText(draw.getBigWinner().getTotalCombinations());
        no1.setText(draw.getNo1());
        no2.setText(draw.getNo2());
        no3.setText(draw.getNo3());
        no4.setText(draw.getNo4());
        no5.setText(draw.getNo5());
        no6.setText(draw.getNo6());

        ResultsAdapter resultsAdapter = new ResultsAdapter(draw.getDrawResults(), this);
        resultsRV.setAdapter(resultsAdapter);
        resultsRV.setLayoutManager(new LinearLayoutManager(mainActivity));
    }
}