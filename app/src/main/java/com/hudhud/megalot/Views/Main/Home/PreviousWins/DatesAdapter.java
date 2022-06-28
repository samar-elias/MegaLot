package com.hudhud.megalot.Views.Main.Home.PreviousWins;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hudhud.megalot.AppUtils.Responses.Date;
import com.hudhud.megalot.AppUtils.Responses.DrawResult;
import com.hudhud.megalot.R;
import com.hudhud.megalot.Views.Main.Home.DrawResults.DrawResultsFragment;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class DatesAdapter extends RecyclerView.Adapter<DatesAdapter.ViewHolder> {

    Context context;
    ArrayList<Date> dates;
    PreviousWinsFragment previousWinsFragment;
    private DecimalFormat formatter = new DecimalFormat("#0.00");

    public DatesAdapter(ArrayList<Date> dates, PreviousWinsFragment previousWinsFragment) {
        this.dates = dates;
        this.previousWinsFragment = previousWinsFragment;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.date_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Date date = dates.get(position);

        holder.date.setText(date.getDate());
    }

    @Override
    public int getItemCount() {
        return dates.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView date;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.date);
        }
    }
}
