package com.hudhud.megalot.Views.Main.Home.DrawResults;

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
import com.google.android.material.button.MaterialButton;
import com.hudhud.megalot.AppUtils.AppDefs;
import com.hudhud.megalot.AppUtils.Responses.BigWinner;
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

public class DrawResultsFragment extends Fragment {

    NavController navController;
    MainActivity mainActivity;
    ImageView navigateBack;
    MaterialButton viewMyTicket;
    private DecimalFormat formatter = new DecimalFormat("#0.00");
    TextView drawDate, drawPrizeAmount, myWinningAmount, bigWinnerWinningAmount, bigWinnerTotalTickets, bigWinnerWinningTickets, bigWinnerName, no1, no2, no3, no4, no5, no6;
    RecyclerView resultsRV;
    Draw draw = new Draw();
    String id;

    public DrawResultsFragment() {
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
        return inflater.inflate(R.layout.fragment_draw_results, container, false);
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
        getDraw();
    }

    private void initViews(View view){
        if (getArguments() != null){
            id = DrawResultsFragmentArgs.fromBundle(getArguments()).getId();
        }
        navController = Navigation.findNavController(view);
        navigateBack = view.findViewById(R.id.navigate_back);
        viewMyTicket = view.findViewById(R.id.view_my_ticket);
        drawDate = view.findViewById(R.id.draw_date);
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
        viewMyTicket.setOnClickListener(view -> navController.navigate(DrawResultsFragmentDirections.actionDrawResultsFragmentToMyTicketsFragment(draw.getId())));
    }

    private void getDraw(){
        mainActivity.showProgressDialog();
        draw = new Draw();
        JSONObject drawIdObj = new JSONObject();
        try {
            drawIdObj.put("id",id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest drawRequest = new JsonObjectRequest(Request.Method.POST, Urls.API_URL+"get_draw_by_id", drawIdObj, response -> {
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
                BigWinner bigWinner = new BigWinner(bigWinnerObj.getString("Username"), bigWinnerObj.getString("winning_amount"), bigWinnerObj.getString("total_combinations"), bigWinnerObj.getString("winning_combinations"));
                draw.setBigWinner(bigWinner);

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
        drawDate.setText(draw.getDate());
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