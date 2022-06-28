package com.hudhud.megalot.Views.Main.Account.AboutUs;

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

public class AboutAdapter extends RecyclerView.Adapter<AboutAdapter.ViewHolder> {

    ArrayList<DataBottom> aboutData;

    public AboutAdapter(ArrayList<DataBottom> aboutData) {
        this.aboutData = aboutData;
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
        DataBottom about = aboutData.get(position);
        holder.aboutTitle.setText(Html.fromHtml(about.getTitle()));
        holder.aboutDescription.setText(Html.fromHtml(about.getDescription()));
    }

    @Override
    public int getItemCount() {
        return aboutData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView aboutTitle, aboutDescription;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            aboutTitle = itemView.findViewById(R.id.title);
            aboutDescription = itemView.findViewById(R.id.description);
        }
    }
}
