package com.hudhud.megalot.Views.Main.Account.FAQs;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hudhud.megalot.AppUtils.Responses.DataBottom;
import com.hudhud.megalot.R;

import java.util.ArrayList;

public class FAQAdapter extends RecyclerView.Adapter<FAQAdapter.ViewHolder> {

    ArrayList<DataBottom> faqData;
    boolean isVisible = false;
    Context context;

    public FAQAdapter(ArrayList<DataBottom> faqData) {
        this.faqData = faqData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.faq_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DataBottom faq = faqData.get(position);
        holder.title.setText(Html.fromHtml(faq.getTitle()));
        holder.description.setText(Html.fromHtml(faq.getDescription()));
        holder.viewDescription.setOnClickListener(view -> {
            if (isVisible){
                holder.description.setVisibility(View.GONE);
                holder.line.setVisibility(View.GONE);
                Glide.with(context).load(R.drawable.gray_down_arrow).into(holder.viewDescription);
                isVisible = false;
            }else {
                holder.description.setVisibility(View.VISIBLE);
                holder.line.setVisibility(View.VISIBLE);
                Glide.with(context).load(R.drawable.gray_arrow2).into(holder.viewDescription);
                isVisible = true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return faqData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, description, line;
        ImageView viewDescription;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            description = itemView.findViewById(R.id.description);
            viewDescription = itemView.findViewById(R.id.view_description);
            line = itemView.findViewById(R.id.line);
        }
    }
}
