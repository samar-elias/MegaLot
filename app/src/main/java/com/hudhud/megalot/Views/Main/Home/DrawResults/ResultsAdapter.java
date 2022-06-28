package com.hudhud.megalot.Views.Main.Home.DrawResults;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hudhud.megalot.AppUtils.Responses.DrawResult;
import com.hudhud.megalot.R;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class ResultsAdapter extends RecyclerView.Adapter<ResultsAdapter.ViewHolder> {

    Context context;
    ArrayList<DrawResult> drawResults;
    DrawResultsFragment drawResultsFragment;
    private DecimalFormat formatter = new DecimalFormat("#0.00");

    public ResultsAdapter(ArrayList<DrawResult> drawResults, DrawResultsFragment drawResultsFragment) {
        this.drawResults = drawResults;
        this.drawResultsFragment = drawResultsFragment;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.result_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DrawResult drawResult = drawResults.get(position);

        holder.place.setText(position+1+" "+context.getResources().getString(R.string.place));
        holder.winningAmountTicket.setText(formatter.format(Double.parseDouble(drawResult.getWinningCombinations())));
        holder.winningAmount.setText(formatter.format(Double.parseDouble(drawResult.getWinningAmount())));
        holder.winningTickets.setText(drawResult.getTotalCombinations());
    }

    @Override
    public int getItemCount() {
        return drawResults.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView place, winningAmount, winningTickets, winningAmountTicket;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            place = itemView.findViewById(R.id.place);
            winningAmount = itemView.findViewById(R.id.winning_amount);
            winningTickets = itemView.findViewById(R.id.winning_tickets);
            winningAmountTicket = itemView.findViewById(R.id.winning_amount_ticket);
        }
    }
}
