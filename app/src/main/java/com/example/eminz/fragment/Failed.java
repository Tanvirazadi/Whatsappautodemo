package com.example.eminz.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.eminz.Model.Sms;
import com.example.eminz.Scheduler.SMSScheduler;
import com.example.eminz.Sqlite.SmsAdapter;
import com.example.eminz.Sqlite.SmsDatabaseHelper;
import com.example.whatsappdemo.R;

import java.util.ArrayList;


public class Failed extends Fragment {
    private static final String TAG = "tab_sent_sms.xml";
    private SmsDatabaseHelper smsDatabaseHelper;
    private ListView list;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_failed,container,false);

    }
}