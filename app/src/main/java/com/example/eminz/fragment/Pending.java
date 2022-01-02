package com.example.eminz.fragment;

import static com.facebook.FacebookSdk.getApplicationContext;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ComponentActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eminz.Activity.MainActivity;
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
import java.util.Objects;

public class Pending extends Fragment {

    AppDatabase appDatabase;
    RecyclerView recyclerView;
    SmsAdapter smsAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_pending, parent, false);


        recyclerView = view.findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        List<Schedule>scheduleList= new ArrayList<>();
        smsAdapter=new SmsAdapter(scheduleList);
        recyclerView.setAdapter(smsAdapter);


        return view;



    }

    public void loaddata() {
        AppDatabase appDatabase = DatabaseClient.getInstance(getApplicationContext()).getAppDatabase();
        ScheduleDao scheduleDao = appDatabase.scheduleDaoDao();
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                List<Schedule> pending = scheduleDao.findByStatus("Pending");

            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        loaddata();

    }
}