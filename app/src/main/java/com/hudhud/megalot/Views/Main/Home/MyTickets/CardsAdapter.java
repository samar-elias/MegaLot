package com.hudhud.megalot.Views.Main.Home.MyTickets;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hudhud.megalot.AppUtils.Responses.Card;
import com.hudhud.megalot.AppUtils.Responses.DrawResult;
import com.hudhud.megalot.R;
import com.hudhud.megalot.Views.Main.Home.PreviousWins.PreviousWinsFragment;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class CardsAdapter extends RecyclerView.Adapter<CardsAdapter.ViewHolder> {

    Context context;
    ArrayList<Card> cards;
    MyTicketFragment myTicketFragment;
    private DecimalFormat formatter = new DecimalFormat("#0.00");

    public CardsAdapter(ArrayList<Card> cards, MyTicketFragment myTicketFragment) {
        this.cards = cards;
        this.myTicketFragment = myTicketFragment;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.my_result_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Card card = cards.get(position);

        holder.no1.setText(card.getNo1());
        holder.no2.setText(card.getNo2());
        holder.no3.setText(card.getNo3());
        holder.no4.setText(card.getNo4());
        holder.no5.setText(card.getNo5());
        holder.no6.setText(card.getNo6());

        if (card.getWinNo1().equals("1")){
            holder.no1.setBackground(context.getResources().getDrawable(R.drawable.green_circle));
            holder.no1.setTextColor(context.getResources().getColor(R.color.white));
        }else {
            holder.no1.setBackground(context.getResources().getDrawable(R.drawable.gray_circle));
            holder.no1.setTextColor(context.getResources().getColor(R.color.dark_gray));
        }

        if (card.getWinNo2().equals("1")){
            holder.no2.setBackground(context.getResources().getDrawable(R.drawable.green_circle));
            holder.no2.setTextColor(context.getResources().getColor(R.color.white));
        }else {
            holder.no2.setBackground(context.getResources().getDrawable(R.drawable.gray_circle));
            holder.no2.setTextColor(context.getResources().getColor(R.color.dark_gray));
        }

        if (card.getWinNo3().equals("1")){
            holder.no3.setBackground(context.getResources().getDrawable(R.drawable.green_circle));
            holder.no3.setTextColor(context.getResources().getColor(R.color.white));
        }else {
            holder.no3.setBackground(context.getResources().getDrawable(R.drawable.gray_circle));
            holder.no3.setTextColor(context.getResources().getColor(R.color.dark_gray));
        }

        if (card.getWinNo4().equals("1")){
            holder.no4.setBackground(context.getResources().getDrawable(R.drawable.green_circle));
            holder.no4.setTextColor(context.getResources().getColor(R.color.white));
        }else {
            holder.no4.setBackground(context.getResources().getDrawable(R.drawable.gray_circle));
            holder.no4.setTextColor(context.getResources().getColor(R.color.dark_gray));
        }

        if (card.getWinNo5().equals("1")){
            holder.no5.setBackground(context.getResources().getDrawable(R.drawable.green_circle));
            holder.no5.setTextColor(context.getResources().getColor(R.color.white));
        }else {
            holder.no5.setBackground(context.getResources().getDrawable(R.drawable.gray_circle));
            holder.no5.setTextColor(context.getResources().getColor(R.color.dark_gray));
        }

        if (card.getWinNo6().equals("1")){
            holder.no6.setBackground(context.getResources().getDrawable(R.drawable.orange_green_stroke_circle));
            holder.no6.setTextColor(context.getResources().getColor(R.color.green));
        }else {
            holder.no6.setBackground(context.getResources().getDrawable(R.drawable.gray_orange_stroke_circle));
            holder.no6.setTextColor(context.getResources().getColor(R.color.orange));
        }

        switch (card.getPlace()) {
            case "1":
                holder.place.setText("1st " + context.getResources().getString(R.string.place));
                break;
            case "2":
                holder.place.setText("2nd " + context.getResources().getString(R.string.place));
                break;
            case "3":
                holder.place.setText("3rd " + context.getResources().getString(R.string.place));
                break;
            default:
                holder.place.setText(card.getPlace() + "th " + context.getResources().getString(R.string.place));
                break;
        }
        holder.prize.setText(card.getPrize()+" JD");
    }

    @Override
    public int getItemCount() {
        return cards.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView no1, no2, no3, no4, no5, no6, place, prize;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            no1 = itemView.findViewById(R.id.no1);
            no2 = itemView.findViewById(R.id.no2);
            no3 = itemView.findViewById(R.id.no3);
            no4 = itemView.findViewById(R.id.no4);
            no5 = itemView.findViewById(R.id.no5);
            no6 = itemView.findViewById(R.id.no6);
            place = itemView.findViewById(R.id.place);
            prize = itemView.findViewById(R.id.prize_amount);
        }
    }
}
