package com.example.eminz.Scheduler;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import com.example.eminz.Service.MysmsService;
import com.example.whatsappdemo.R;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.wafflecopter.multicontactpicker.ContactResult;
import com.wafflecopter.multicontactpicker.LimitColumn;
import com.wafflecopter.multicontactpicker.MultiContactPicker;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class SMSScheduler extends AppCompatActivity {
    private static final int CONTACT_PICKER_REQUEST = 20;
    EditText phn, sms;
    Button send, chos;
    List<ContactResult> results = new ArrayList<>();
    Button time, date;
    private int hr = 100;
    private int min = 100;
    private int sec = 100;
    private final int days = 1;
    int day = 31;
    int mon = 12;
    int years = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smsscheduler);


        phn = findViewById(R.id.phon);
        sms = findViewById(R.id.tx);
        send = findViewById(R.id.send);
        chos = findViewById(R.id.choos);
        time=findViewById(R.id.times);


        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog dpd = TimePickerDialog.newInstance(
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
                                hr=hourOfDay;
                                min=minute;
                                sec=second;
                            }
                        },
                        false
                );
                dpd.show(getSupportFragmentManager(), "Datepickerdialog");

            }
        });


        Dexter.withContext(this)
                .withPermissions(
                        Manifest.permission.SEND_SMS,
                        Manifest.permission.READ_CONTACTS

                ).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport report) {/* ... */}

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {/* ... */}
        }).check();

        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
           public void onClick(View v) {

                MysmsService.startActionSMS(getApplicationContext(),sms.getText().toString(),results);

            }



        });


        chos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MultiContactPicker.Builder(SMSScheduler.this) //Activity/fragment context
                        .hideScrollbar(false) //Optional - default: false
                        .showTrack(true) //Optional - default: true
                        .searchIconColor(Color.WHITE) //Option - default: White
                        .setChoiceMode(MultiContactPicker.CHOICE_MODE_MULTIPLE) //Optional - default: CHOICE_MODE_MULTIPLE
                        .handleColor(ContextCompat.getColor(SMSScheduler.this, R.color.purple_500)) //Optional - default: Azure Blue
                        .bubbleColor(ContextCompat.getColor(SMSScheduler.this, R.color.purple_700)) //Optional - default: Azure Blue
                        .bubbleTextColor(Color.WHITE) //Optional - default: White
                        .setTitleText("Select Contacts") //Optional - default: Select Contacts
                        .setLoadingType(MultiContactPicker.LOAD_ASYNC) //Optional - default LOAD_ASYNC (wait till all loaded vs stream results)
                        .limitToColumn(LimitColumn.NONE) //Optional - default NONE (Include phone + email, limiting to one can improve loading time)
                        .setActivityAnimations(android.R.anim.fade_in, android.R.anim.fade_out,
                                android.R.anim.fade_in,
                                android.R.anim.fade_out) //Optional - default: No animation overrides
                        .showPickerForResult(CONTACT_PICKER_REQUEST);


            }
        });


        IntentFilter intentFilter = new IntentFilter("my.own.broadcast");
        LocalBroadcastManager.getInstance(this).registerReceiver(localBroadCastReceiver, intentFilter);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CONTACT_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                results = MultiContactPicker.obtainResult(data);
                StringBuilder names = new StringBuilder(results.get(0).getDisplayName());
                for (int j = 0; j < results.size(); j++) {
                    if (j != 0)
                        names.append(",").append(results.get(j).getDisplayName());
                }
                phn.setText(names);

                Log.d("MyTag", results.get(0).getDisplayName());
            } else if (resultCode == RESULT_CANCELED) {
                System.out.println("User closed the picker without selecting items.");
            }
        }
    }

    private final BroadcastReceiver localBroadCastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String result = intent.getStringExtra("Result");
            Toast.makeText(context, "result", Toast.LENGTH_SHORT).show();

        }
    };

    private long calculateFlex(int hr, int min, int sec, int days) {
        Calendar cal1 = Calendar.getInstance();
        cal1.set(Calendar.HOUR_OF_DAY, hr);
        cal1.set(Calendar.MINUTE, min);
        cal1.set(Calendar.SECOND, sec);

        // Initialize a calendar with now.
        Calendar cal2 = Calendar.getInstance();

        if (cal2.getTimeInMillis() < cal1.getTimeInMillis()) {
            // Add the worker periodicity.
            long periodInDays = 0;
            cal2.setTimeInMillis(cal2.getTimeInMillis() + TimeUnit.DAYS.toMillis(periodInDays));
        }

        long delta = (cal2.getTimeInMillis() - cal1.getTimeInMillis());

        return (Math.max(delta, PeriodicWorkRequest.MIN_PERIODIC_FLEX_MILLIS));

    }
}