package com.example.eminz.database;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eminz.Activity.LastActivity;
import com.example.eminz.database.entities.Schedule;
import com.example.whatsappdemo.R;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
    public void onBindViewHolder(@NonNull SmsAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.name.setText(this.scheduleList.get(position).recipients);
        //String output = formatter.format(calendar.getTime());
        //Date date = new Date(scheduleList.get(position).date);
        //DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(scheduleList.get(position).date);
        SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy- hh:mm a");
        String dateString = format.format(calendar.getTime());
        holder.date.setText(dateString);

        holder.message.setText(this.scheduleList.get(position).body);
        Time time = new Time(scheduleList.get(position).time);
        holder.time.setText(time.toString());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//

                Intent intent = new Intent(context, LastActivity.class);
                intent.putExtra("message", scheduleList.get(position).body);
                intent.putExtra("date", dateString);
                intent.putExtra("time", scheduleList.get(position).time);
                intent.putExtra("name", scheduleList.get(position).recipients);
                intent.putExtra("position", position);
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return scheduleList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, date, time, message;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.display_name);
            date = itemView.findViewById(R.id.display_messageDate);
            time = itemView.findViewById(R.id.display_messageTime);
            message = itemView.findViewById(R.id.display_message);
        }
    }
}
