package com.example.eminz.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eminz.Scheduler.SMSScheduler;
import com.example.eminz.database.AppDatabase;
import com.example.eminz.database.AppExecutors;
import com.example.eminz.database.DatabaseClient;
import com.example.eminz.database.SmsAdapter;
import com.example.eminz.database.daos.ScheduleDao;
import com.example.eminz.database.entities.Schedule;
import com.example.whatsappdemo.R;

import java.util.ArrayList;
import java.util.List;

public class LastActivity extends AppCompatActivity {
    TextView Name, Date, Time, Msg;
    String name, date, time, msg;
    private final List<Schedule> scheduleList = new ArrayList<>();

    AppDatabase appDatabase;
    RecyclerView recyclerView;
    SmsAdapter smsAdapter;
    int position;
    MutableLiveData<List<Schedule>> pendingMutableLiveData = new MutableLiveData<>();
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_last);
        toolbar = findViewById(R.id.toollast);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        date = intent.getStringExtra("date");
        time = intent.getStringExtra("time");
        msg = intent.getStringExtra("message");
        position = intent.getIntExtra("position", 0);


        Name = findViewById(R.id.display_names);
        Date = findViewById(R.id.display_messageDates);
        Time = findViewById(R.id.display_messageTimes);
        Msg = findViewById(R.id.display_messages);

        Name.setText(name);
        Date.setText(date);
        Time.setText(time);
        Msg.setText(msg);

        appDatabase = DatabaseClient.getInstance(getApplicationContext()).getAppDatabase();

        loaddata();


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

            pendingMutableLiveData.observe(this, deletes -> {
                AsyncTask.execute(() -> {
                    appDatabase.scheduleDaoDao().delete(deletes.get(position));
                });

            });
            finish();

        }
        if (item.getItemId() == R.id.edit) {

            Intent intent = new Intent(LastActivity.this, SMSScheduler.class);
            startActivity(intent);
        }
        return true;
    }

    public void loaddata() {
        AppDatabase appDatabase = DatabaseClient.getInstance(getApplicationContext()).getAppDatabase();
        ScheduleDao scheduleDao = appDatabase.scheduleDaoDao();
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                List<Schedule> pending = scheduleDao.findByStatus("Pending");
                pendingMutableLiveData.postValue(pending);
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@Nullable View parent, @NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {
        return super.onCreateView(parent, name, context, attrs);
    }
}