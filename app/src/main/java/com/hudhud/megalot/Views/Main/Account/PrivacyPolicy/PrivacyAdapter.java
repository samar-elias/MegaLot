package com.hudhud.megalot.Views.Main.Account.PrivacyPolicy;

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

public class PrivacyAdapter extends RecyclerView.Adapter<PrivacyAdapter.ViewHolder> {

    ArrayList<DataBottom> privacyData;

    public PrivacyAdapter(ArrayList<DataBottom> privacyData) {
        this.privacyData = privacyData;
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
        DataBottom privacy = privacyData.get(position);
        holder.privacyTitle.setText(Html.fromHtml(privacy.getTitle()));
        holder.privacyDescription.setText(Html.fromHtml(privacy.getDescription()));
    }

    @Override
    public int getItemCount() {
        return privacyData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView privacyTitle, privacyDescription;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            privacyTitle = itemView.findViewById(R.id.title);
            privacyDescription = itemView.findViewById(R.id.description);
        }
    }
}
