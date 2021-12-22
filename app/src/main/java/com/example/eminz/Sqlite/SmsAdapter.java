package com.example.eminz.Sqlite;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.eminz.Model.Sms;
import com.example.whatsappdemo.R;

import java.util.ArrayList;

public class SmsAdapter extends ArrayAdapter<Sms> {
    public SmsAdapter(Context context, ArrayList<Sms> sms) {
        super(context, 0, sms);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the sms item for this position.
        Sms sms = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view.
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.sms_layout, parent, false);
        }
        // Initialise views.
        TextView display_name = convertView.findViewById(R.id.display_name);
        TextView display_messageDate = convertView.findViewById(R.id.display_messageDate);
        TextView display_messageTime = convertView.findViewById(R.id.display_messageTime);
        TextView display_message = convertView.findViewById(R.id.display_message);

        // set values of text views
        display_name.setText(sms.name);
        display_messageDate.setText(sms.messageDate);
        display_messageTime.setText(sms.messageTime);
        display_message.setText(sms.message);

        // Return view to display on users screen
        return convertView;
    }

}
