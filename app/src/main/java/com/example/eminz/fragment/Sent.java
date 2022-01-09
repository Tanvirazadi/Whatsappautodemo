package com.example.eminz.fragment;

import static com.facebook.FacebookSdk.getApplicationContext;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eminz.database.AppDatabase;
import com.example.eminz.database.AppExecutors;
import com.example.eminz.database.DatabaseClient;
import com.example.eminz.database.SmsAdapter;
import com.example.eminz.database.daos.ScheduleDao;
import com.example.eminz.database.entities.Schedule;
import com.example.whatsappdemo.R;

import java.util.List;


public class Sent extends Fragment {

    AppDatabase appDatabase;
    RecyclerView recyclerView;
    SmsAdapter smsAdapter;
    MutableLiveData<List<Schedule>> sentMutableLiveData = new MutableLiveData<>();
    Schedule schedule;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sent, container, false);
        recyclerView = view.findViewById(R.id.recyclersent);

        sentMutableLiveData.observe(getViewLifecycleOwner(), sends -> {
            smsAdapter = new SmsAdapter(getContext(), sends);
            recyclerView.setAdapter(smsAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        });
        return view;
    }

    public void loaddata() {
        AppDatabase appDatabase = DatabaseClient.getInstance(getApplicationContext()).getAppDatabase();
        ScheduleDao scheduleDao = appDatabase.scheduleDaoDao();
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                List<Schedule> sent = scheduleDao.findByStatus("Sent");
                sentMutableLiveData.postValue(sent);


            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        loaddata();

    }

    @Override
    public void onResume() {
        super.onResume();
        loaddata();
    }
}