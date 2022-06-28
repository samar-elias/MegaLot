package com.hudhud.megalot.Views.Main.Account.TermsConditions;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hudhud.megalot.AppUtils.Responses.DataBottom;
import com.hudhud.megalot.R;

import java.util.ArrayList;

public class TermsAdapter extends RecyclerView.Adapter<TermsAdapter.ViewHolder> {

    ArrayList<DataBottom> termsData;

    public TermsAdapter(ArrayList<DataBottom> termsData) {
        this.termsData = termsData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.bottom_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DataBottom terms = termsData.get(position);
        holder.termsTitle.setText(Html.fromHtml(terms.getTitle()));
        holder.termsDescription.setText(Html.fromHtml(terms.getDescription()));
    }

    @Override
    public int getItemCount() {
        return termsData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView termsTitle, termsDescription;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            termsTitle = itemView.findViewById(R.id.title);
            termsDescription = itemView.findViewById(R.id.description);
        }
    }
}
