package com.hudhud.megalot.Views.Main.Home.MyWithdrawals;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hudhud.megalot.AppUtils.Responses.Card;
import com.hudhud.megalot.AppUtils.Responses.Withdraw;
import com.hudhud.megalot.R;
import com.hudhud.megalot.Views.Main.Home.MyTickets.MyTicketFragment;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class WithdrawsAdapter extends RecyclerView.Adapter<WithdrawsAdapter.ViewHolder> {

    Context context;
    ArrayList<Withdraw> withdraws;
    MyWithdrawalsFragment myWithdrawalsFragment;
    private DecimalFormat formatter = new DecimalFormat("#0.00");

    public WithdrawsAdapter(ArrayList<Withdraw> withdraws, MyWithdrawalsFragment myWithdrawalsFragment) {
        this.withdraws = withdraws;
        this.myWithdrawalsFragment = myWithdrawalsFragment;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.withdraw_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Withdraw withdraw = withdraws.get(position);

        holder.day.setText(withdraw.getDay());
        holder.date.setText(withdraw.getDate());
        holder.amount.setText(formatter.format(Double.parseDouble(withdraw.getPrice())));
    }

    @Override
    public int getItemCount() {
        return withdraws.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView day, date, amount;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            day = itemView.findViewById(R.id.day);
            date = itemView.findViewById(R.id.date);
            amount = itemView.findViewById(R.id.amount);
        }
    }
}
