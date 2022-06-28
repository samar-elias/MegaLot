package com.hudhud.megalot.Views.Main.Home.MyTickets;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
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
import com.hudhud.megalot.AppUtils.AppDefs;
import com.hudhud.megalot.AppUtils.Responses.Card;
import com.hudhud.megalot.AppUtils.Responses.Date;
import com.hudhud.megalot.AppUtils.Responses.Ticket;
import com.hudhud.megalot.AppUtils.Urls;
import com.hudhud.megalot.R;
import com.hudhud.megalot.Views.Main.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MyTicketsFragment extends Fragment {

    private DecimalFormat formatter = new DecimalFormat("#0.00");
    NavController navController;
    MainActivity mainActivity;
    ImageView navigateBack, nextDate, previousDate;;
    TextView date, prizeAmount, myUsername, totalTickets, winningTickets, no1, no2, no3, no4, no5, no6;
    RecyclerView resultsRV;
    NestedScrollView nestedSV;
    String id;
    Ticket myTicket = new Ticket();
    ArrayList<Card> cards = new ArrayList<>();
    ArrayList<Date> dates = new ArrayList<>();
    int page = 1, position = 0;

    public MyTicketsFragment() {
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
        return inflater.inflate(R.layout.fragment_my_tickets2, container, false);
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
        prizeAmount = view.findViewById(R.id.prize_amount);
        myUsername = view.findViewById(R.id.my_username);
        totalTickets = view.findViewById(R.id.total_tickets);
        winningTickets = view.findViewById(R.id.winning_tickets);
        resultsRV = view.findViewById(R.id.results_RV);
        nestedSV = view.findViewById(R.id.nested_SV);
        nextDate = view.findViewById(R.id.next_date);
        previousDate = view.findViewById(R.id.previous_date);
        no1 = view.findViewById(R.id.no1);
        no2 = view.findViewById(R.id.no2);
        no3 = view.findViewById(R.id.no3);
        no4 = view.findViewById(R.id.no4);
        no5 = view.findViewById(R.id.no5);
        no6 = view.findViewById(R.id.no6);
    }

    private void onClick(){
        navigateBack.setOnClickListener(view -> navController.popBackStack());
        nestedSV.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()){
                page++;
                getCards();
            }
        });
        nextDate.setOnClickListener(view -> {
            if (position != dates.size()-1){
                position++;
                setDate(dates.get(position));
            }
        });
        previousDate.setOnClickListener(view -> {
            if (position != 0){
                position--;
                setDate(dates.get(position));
            }
        });
    }

    private void setDate(Date dateObj){
        date.setText(dateObj.getDate());
        getMyTicket(dateObj.getId());
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
                    dates.add(new Date(dateObj.getString("id"), dateObj.getString("date")));
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
        getMyTicket(dates.get(dates.size()-1).getId());
        position = dates.size()-1;
        nextDate.setEnabled(false);
//        DatesAdapter datesAdapter = new DatesAdapter(reverseDates, this);
//        datesRV.setAdapter(datesAdapter);
//        datesRV.setLayoutManager(new LinearLayoutManager(mainActivity, RecyclerView.HORIZONTAL, false));
    }

    private void getMyTicket(String id){
        this.id = id;
        mainActivity.showProgressDialog();
        myTicket = new Ticket();
        JSONObject idObj = new JSONObject();
        try {
            idObj.put("drawId", id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest getTicketRequest = new JsonObjectRequest(Request.Method.POST, Urls.API_URL+"get_draw_my_card", idObj, response -> {
            try {
                JSONObject resultObj = response.getJSONObject("results");
                myTicket.setId(resultObj.getString("id"));
                myTicket.setPrize(resultObj.getString("price"));
                myTicket.setIncreaseValue(resultObj.getString("increase_value"));
                myTicket.setDate(resultObj.getString("date"));
                myTicket.setDateTime(resultObj.getString("date_time"));
                myTicket.setStatus(resultObj.getString("status"));
                myTicket.setCardClient(resultObj.getString("card_client"));
                myTicket.setWinCardClient(resultObj.getString("win_card_client"));
                myTicket.setNo1(resultObj.getString("no_1"));
                myTicket.setNo2(resultObj.getString("no_2"));
                myTicket.setNo3(resultObj.getString("no_3"));
                myTicket.setNo4(resultObj.getString("no_4"));
                myTicket.setNo5(resultObj.getString("no_5"));
                myTicket.setNo6(resultObj.getString("no_6"));

                setData();
                getCards();
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
                            mainActivity.showResponseMessage(mainActivity.getResources().getString(R.string.my_ticket), mainActivity.getResources().getString(R.string.internet_connection_error));
                        }else {
                            mainActivity.showResponseMessage(mainActivity.getResources().getString(R.string.my_ticket), statusObj.getString("massage"));
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
        mainActivity.queue.add(getTicketRequest);
    }

    private void setData(){
        date.setText(myTicket.getDate());
        prizeAmount.setText(formatter.format(Double.parseDouble(myTicket.getPrize())));
        myUsername.setText(AppDefs.user.getName());
        totalTickets.setText(myTicket.getCardClient());
        winningTickets.setText(myTicket.getWinCardClient());
        no1.setText(myTicket.getNo1());
        no2.setText(myTicket.getNo2());
        no3.setText(myTicket.getNo3());
        no4.setText(myTicket.getNo4());
        no5.setText(myTicket.getNo5());
        no6.setText(myTicket.getNo6());
    }

    private void getCards(){
        cards.clear();
        JSONObject cardObj = new JSONObject();
        try {
            cardObj.put("drawId", id);
            cardObj.put("page", String.valueOf(page));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest getCardsRequest = new JsonObjectRequest(Request.Method.POST, Urls.API_URL+"get_card", cardObj, response -> {
            mainActivity.hideProgressDialog();
            try {
                JSONArray cardsArray = response.getJSONArray("results");
                for (int i=0; i<cardsArray.length(); i++){
                    JSONObject cardsObj = cardsArray.getJSONObject(i);
                    Card card = new Card();
                    card.setPlace(cardsObj.getString("center"));
                    card.setNo1(cardsObj.getString("no_1"));
                    card.setWinNo1(cardsObj.getString("win_no_1"));
                    card.setNo2(cardsObj.getString("no_2"));
                    card.setWinNo2(cardsObj.getString("win_no_2"));
                    card.setNo3(cardsObj.getString("no_3"));
                    card.setWinNo3(cardsObj.getString("win_no_3"));
                    card.setNo4(cardsObj.getString("no_4"));
                    card.setWinNo4(cardsObj.getString("win_no_4"));
                    card.setNo5(cardsObj.getString("no_5"));
                    card.setWinNo5(cardsObj.getString("win_no_5"));
                    card.setNo6(cardsObj.getString("no_6"));
                    card.setWinNo6(cardsObj.getString("win_no_6"));
                    card.setPrize(cardsObj.getString("price"));
                    cards.add(card);
                }
                setCardsRV();
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
                            mainActivity.showResponseMessage(mainActivity.getResources().getString(R.string.my_ticket), mainActivity.getResources().getString(R.string.internet_connection_error));
                        }else {
                            mainActivity.showResponseMessage(mainActivity.getResources().getString(R.string.my_ticket), statusObj.getString("massage"));
                        }
                        cards.clear();
                        setCardsRV();
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
        mainActivity.queue.add(getCardsRequest);
    }

    private void setCardsRV(){
        Cards2Adapter cardsAdapter = new Cards2Adapter(cards, this);
        resultsRV.setAdapter(cardsAdapter);
        resultsRV.setLayoutManager(new LinearLayoutManager(mainActivity));
    }
}