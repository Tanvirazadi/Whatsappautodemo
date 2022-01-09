package com.example.eminz.database;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eminz.database.entities.Schedule;
import com.example.whatsappdemo.R;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SmsAdapter extends RecyclerView.Adapter<SmsAdapter.ViewHolder> {
    private final Context context;
    private List<Schedule> scheduleList = new ArrayList<>();


    public SmsAdapter(Context context, List<Schedule> scheduleList) {
        this.context = context;
        this.scheduleList = scheduleList;
    }

    @NonNull
    @Override
    public SmsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.sms_layout, parent, false);
        return new ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull SmsAdapter.ViewHolder holder, int position) {

        holder.name.setText(this.scheduleList.get(position).recipients);
        Date date = new Date(scheduleList.get(position).date);
        holder.date.setText(date.toString());
        holder.message.setText(this.scheduleList.get(position).body);
        Time time = new Time(scheduleList.get(position).time);
        holder.time.setText(time.toString());
        
    }
    @Override
    public int getItemCount() {
        return scheduleList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name,date,time,message;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.display_name);
            date = itemView.findViewById(R.id.display_messageDate);
            time = itemView.findViewById(R.id.display_messageTime);
            message = itemView.findViewById(R.id.display_message);
        }
    }
}
