package com.hudhud.megalot.Views.Main.Notifications;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hudhud.megalot.AppUtils.Responses.DataBottom;
import com.hudhud.megalot.AppUtils.Responses.Notification;
import com.hudhud.megalot.R;

import java.util.ArrayList;

public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.ViewHolder> {

    NotificationFragment notificationFragment;
    ArrayList<Notification> notifications;

    public NotificationsAdapter(NotificationFragment notificationFragment, ArrayList<Notification> notifications) {
        this.notificationFragment = notificationFragment;
        this.notifications = notifications;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.notification_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Notification notification = notifications.get(position);

        holder.title.setText(notification.getTitle());
        holder.description.setText(notification.getDescription());
        holder.timeDate.setText(notification.getTime()+", "+notification.getDate());

        holder.deleteNotification.setOnClickListener(view -> notificationFragment.showDeleteNotificationMessage(notification.getId()));
    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, description, timeDate;
        ImageView deleteNotification;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            description = itemView.findViewById(R.id.description);
            deleteNotification = itemView.findViewById(R.id.delete_notification);
            timeDate = itemView.findViewById(R.id.time_date);
        }
    }
}
