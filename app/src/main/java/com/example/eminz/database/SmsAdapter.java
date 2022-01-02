package com.example.eminz.database;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eminz.database.entities.Schedule;
import com.example.whatsappdemo.R;

import java.util.ArrayList;
import java.util.List;

public class SmsAdapter extends RecyclerView.Adapter<SmsAdapter.ViewHolder> {
    private  Context context;
    private List<Schedule>scheduleList=new ArrayList<>();



    public SmsAdapter(Context context) {
        this.context = context;

    }

    public SmsAdapter(List<Schedule> scheduleList) {
        this.scheduleList = scheduleList;
    }

    @NonNull
    @Override
    public SmsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view=LayoutInflater.from(context).inflate(R.layout.sms_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SmsAdapter.ViewHolder holder, int position) {
        holder.name.setText(this.scheduleList.get(position).recipients);
        holder.date.setText((int) this.scheduleList.get(position).scheduleAt);
        holder.message.setText(this.scheduleList.get(position).body);

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
