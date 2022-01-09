package com.example.eminz.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eminz.database.AppDatabase;
import com.example.eminz.database.SmsAdapter;
import com.example.whatsappdemo.R;

public class LastActivity extends AppCompatActivity {
    TextView Name, Date, Time, Msg;
    String name, date, time, msg;
    AppDatabase appDatabase;
    RecyclerView recyclerView;
    SmsAdapter smsAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_last);


        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        date = intent.getStringExtra("date");
        time = intent.getStringExtra("time");
        msg = intent.getStringExtra("message");


        Name = findViewById(R.id.display_names);
        Date = findViewById(R.id.display_messageDates);
        Time = findViewById(R.id.display_messageTimes);
        Msg = findViewById(R.id.display_messages);

        Name.setText(name);
        Date.setText(date);
        Time.setText(time);
        Msg.setText(msg);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.lastmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.delete) {

        }
        return true;
    }
}