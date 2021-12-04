package com.example.eminz.Scheduler;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.work.Data;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.example.eminz.worker.sendMessageWorker;
import com.example.whatsappdemo.R;
import com.example.eminz.Service.Whatsappaccessibility;
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

public class WhatsappScheduler extends AppCompatActivity {

    private static final int CONTACT_PICKER_REQUEST = 20;
    EditText phn, sms;
    Button  chose, whtsapp;
    ArrayList<ContactResult> results=new ArrayList<>();
    Button time,date;
    private int hr=100;
    private int min=100;
    private int sec=100;
    private final int days=1;
    int day=31;
    int mon=12;
    int years=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_whatsapp_scheduler);


        phn = findViewById(R.id.phone);
        sms = findViewById(R.id.txt);
        chose = findViewById(R.id.choose);
        whtsapp = findViewById(R.id.whtsapp);
        time=findViewById(R.id.time);
        date=findViewById(R.id.date);

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



        chose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MultiContactPicker.Builder(WhatsappScheduler.this) //Activity/fragment context
                        .hideScrollbar(false) //Optional - default: false
                        .showTrack(true) //Optional - default: true
                        .searchIconColor(Color.WHITE) //Option - default: White
                        .setChoiceMode(MultiContactPicker.CHOICE_MODE_MULTIPLE) //Optional - default: CHOICE_MODE_MULTIPLE
                        .handleColor(ContextCompat.getColor(WhatsappScheduler.this, R.color.purple_500)) //Optional - default: Azure Blue
                        .bubbleColor(ContextCompat.getColor(WhatsappScheduler.this, R.color.purple_700)) //Optional - default: Azure Blue
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
        if (!isAccessibilityOn(getApplicationContext())){
            Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }


        whtsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                    if(hr!=100 && sec!=100 && min!=100){
                        if(!results.isEmpty()){
                            if(!sms.getText().toString().isEmpty()){
                                List<String> numbersList = new ArrayList<String>();
                                for (int i=0;i<results.size();i++){
                                    numbersList.add(results.get(i).getPhoneNumbers().get(0).getNumber());
                                }
                                String[] numbers = numbersList.toArray(new String[0]);
                                long flexTime = calculateFlex(hr,min,sec);

                                Data messageData = new Data.Builder()
                                        .putString("message", sms.getText().toString())
                                        .putStringArray("contacts",numbers)
                                        .build();

                                PeriodicWorkRequest sendMessagework = new PeriodicWorkRequest.Builder(sendMessageWorker.class,days,
                                        TimeUnit.DAYS,
                                        flexTime, TimeUnit.MILLISECONDS)
                                        .setInputData(messageData)
                                        .addTag("send_message_work")
                                        .build();


                                WorkManager.getInstance(getApplicationContext()).enqueueUniquePeriodicWork("send_message_work",
                                        ExistingPeriodicWorkPolicy.REPLACE,sendMessagework);
                                Toast.makeText(WhatsappScheduler.this, "Message is scheduled", Toast.LENGTH_SHORT).show();


                            }else{
                                Toast.makeText(WhatsappScheduler.this, "Please add message", Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Toast.makeText(WhatsappScheduler.this, "Select Contact Number", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(WhatsappScheduler.this, "Please select time", Toast.LENGTH_SHORT).show();
                    }

                }


        });

        IntentFilter intentFilter=new IntentFilter("my.own.broadcast");
        LocalBroadcastManager.getInstance(this).registerReceiver(localBroadCastReceiver,intentFilter);

    }


    private long calculateFlex(int hourOfTheDay, int minute, int sec) {

        // Initialize the calendar with today and the preferred time to run the job.
        Calendar cal1 = Calendar.getInstance();
        cal1.set(Calendar.HOUR_OF_DAY, hourOfTheDay);
        cal1.set(Calendar.MINUTE, minute);
        cal1.set(Calendar.SECOND, sec);

        // Initialize a calendar with now.
        Calendar cal2 = Calendar.getInstance();

        if (cal2.getTimeInMillis() < cal1.getTimeInMillis()) {
            // Add the worker periodicity.
            cal2.setTimeInMillis(cal2.getTimeInMillis() + TimeUnit.DAYS.toMillis(1));
        }

        long delta = (cal2.getTimeInMillis() - cal1.getTimeInMillis());

        return ((delta > PeriodicWorkRequest.MIN_PERIODIC_FLEX_MILLIS) ? delta
                : PeriodicWorkRequest.MIN_PERIODIC_FLEX_MILLIS);
    }





    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CONTACT_PICKER_REQUEST){
            if(resultCode == RESULT_OK) {
                results = MultiContactPicker.obtainResult(data);
                StringBuilder names=new StringBuilder(results.get(0).getDisplayName());
                for (int j=0;j<results.size();j++){
                    if (j !=0)
                        names.append(",").append(results.get(j).getDisplayName());
                }
                phn.setText(names);

                Log.d("MyTag", results.get(0).getDisplayName());
            } else if(resultCode == RESULT_CANCELED){
                System.out.println("User closed the picker without selecting items.");
            }
        }
    }


    private final BroadcastReceiver localBroadCastReceiver =new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String result=intent.getStringExtra("Result");
            Toast.makeText(context, "result", Toast.LENGTH_SHORT).show();

        }
    };
    private boolean isAccessibilityOn(Context context) {

        int accessibilityEnabled = 0;
        final String service = context.getPackageName () + "/" + Whatsappaccessibility.class.getCanonicalName ();
        try {
            accessibilityEnabled = Settings.Secure.getInt (context.getApplicationContext ().getContentResolver (), Settings.Secure.ACCESSIBILITY_ENABLED);
        } catch (Settings.SettingNotFoundException ignored) {  }

        TextUtils.SimpleStringSplitter colonSplitter = new TextUtils.SimpleStringSplitter (':');

        if (accessibilityEnabled == 1) {
            String settingValue = Settings.Secure.getString (context.getApplicationContext ().getContentResolver (), Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
            if (settingValue != null) {
                colonSplitter.setString (settingValue);
                while (colonSplitter.hasNext ()) {
                    String accessibilityService = colonSplitter.next ();

                    if (accessibilityService.equalsIgnoreCase (service)) {
                        return true;
                    }
                }
            }
        }

        return false;


    }



}