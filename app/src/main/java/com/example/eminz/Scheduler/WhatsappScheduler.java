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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
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
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class WhatsappScheduler extends AppCompatActivity  {

    private static final int CONTACT_PICKER_REQUEST = 20;
    String repeatTime;
    String stayTime;
    String drop_item2;
    String drop_item;
    String contentText;
    String contentText2;
    long timesys;
    String formattedDate;
    long timesys2;
    String formattedDate2;
    long timestay;
    int id;
    EditText phn, sms,everytime,endafter;
    Button   whtsapp;
    ImageButton chose;
    Spinner spin1,spin2;
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
        spin1=findViewById(R.id.spinner21);
        spin2=findViewById(R.id.spinner22);




        everytime=findViewById(R.id.edit11);
        repeatTime= everytime.getText().toString();



        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this,R.array.time, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin1.setAdapter(adapter);

        endafter=findViewById(R.id.edit12);
        stayTime=endafter.getText().toString();
        drop_item=spin1.getSelectedItem().toString();


        ArrayAdapter<CharSequence>adapter2=ArrayAdapter.createFromResource(this,R.array.Numbers,android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin2.setAdapter(adapter2);
        drop_item2=spin2.getSelectedItem().toString();


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
                new MultiContactPicker.Builder(WhatsappScheduler.this) //Activity/com.example.eminz.fragment context
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
                TimePickerDialog tpd = TimePickerDialog.newInstance(
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
                tpd.show(getSupportFragmentManager(), "Timepickerdialog");

            }

        });
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog dpd=DatePickerDialog.newInstance(
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                                day=dayOfMonth;
                                mon=monthOfYear;
                                years=year;
                            }
                        }
                );
                dpd.show(getSupportFragmentManager(),"Datepickerdialog");
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


                    if(hr!=100 && sec!=100 && min!=100) {
                        if (day != 31 && mon != 12 && years != 1) {

                            if (!results.isEmpty()) {
                                if (!sms.getText().toString().isEmpty()) {
                                    List<String> numbersList = new ArrayList<String>();
                                    for (int i = 0; i < results.size(); i++) {
                                        numbersList.add(results.get(i).getPhoneNumbers().get(0).getNumber());
                                    }
                                    String[] numbers = numbersList.toArray(new String[0]);
                                    long flexTime = calculateFlex(hr, min, sec, day, mon, years);

                                    Data messageData = new Data.Builder()
                                            .putString("message", sms.getText().toString())
                                            .putStringArray("contacts", numbers)
                                            .build();

                                    PeriodicWorkRequest sendMessagework = new PeriodicWorkRequest.Builder(sendMessageWorker.class, days,
                                            TimeUnit.DAYS,
                                            flexTime, TimeUnit.MILLISECONDS)
                                            .setInputData(messageData)
                                            .addTag("send_message_work")
                                            .build();


                                    WorkManager.getInstance(getApplicationContext()).enqueueUniquePeriodicWork("send_message_work",
                                            ExistingPeriodicWorkPolicy.REPLACE, sendMessagework);
                                    Toast.makeText(WhatsappScheduler.this, "Message is scheduled", Toast.LENGTH_SHORT).show();


                                } else {
                                    Toast.makeText(WhatsappScheduler.this, "Please add message", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(WhatsappScheduler.this, "Select Contact Number", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(WhatsappScheduler.this, "Please select Date", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText(WhatsappScheduler.this, "Please select Time", Toast.LENGTH_SHORT).show();

                    }

                }


        });

        IntentFilter intentFilter=new IntentFilter("my.own.broadcast");
        LocalBroadcastManager.getInstance(this).registerReceiver(localBroadCastReceiver,intentFilter);

    }
    public  long calculatespin(int hourOfTheDay, int minzute, int sec,int day,int mon,int years){
        repeatTime=everytime.getText().toString();
        drop_item = spin1.getSelectedItem().toString();
        stayTime=endafter.getText().toString();
        drop_item2= spin2.getSelectedItem().toString();

        Calendar cal=Calendar.getInstance();

        switch (drop_item2) {
            case "Hours":
                timestay = (Long.parseLong(stayTime) * 60 * 60 * 1000);
                cal.add(Calendar.HOUR_OF_DAY, Integer.parseInt(stayTime));
                break;
            case "Days":
                timestay = (Long.parseLong(stayTime) * 60 * 60 * 24 * 1000);
                cal.add(Calendar.DAY_OF_MONTH, Integer.parseInt(stayTime));
                break;
            case "Weeks":
                timestay = (Long.parseLong(stayTime) * 60 * 60 * 1000 * 24 * 7);
                cal.add(Calendar.WEEK_OF_MONTH, Integer.parseInt(stayTime));
                break;
            case "Months":
                timestay = (Long.parseLong(stayTime) * 60 * 60 * 24 * 1000 * 30);
                cal.add(Calendar.MONTH, Integer.parseInt(stayTime));
                break;
            default:
                timestay = (Long.parseLong(stayTime) * 60 * 60 * 24 * 1000 * 365);
                cal.add(Calendar.YEAR, Integer.parseInt(stayTime));
                break;


        }
        SimpleDateFormat dateFormatter = new SimpleDateFormat("MMM dd, yyyy, hh:mm aa");
        timesys=cal.getTimeInMillis();
        cal.setTimeInMillis(timesys);
        formattedDate=dateFormatter.format(cal.getTime());

        if(repeatTime.length()!=0) {
            Calendar cal2 = Calendar.getInstance();
            cal2.setLenient(true);

            switch (drop_item) {
                case "Hours":
                    //timesys=dt.getTime()+(Long.parseLong(stayTime)*60*60*1000);
                    cal2.add(Calendar.HOUR_OF_DAY, Integer.parseInt(repeatTime));
                    break;
                case "Days":
                    //timesys=dt.getTime()+(Long.parseLong(stayTime)*60*60*24*1000);
                    cal2.add(Calendar.DAY_OF_MONTH, Integer.parseInt(repeatTime));
                    break;
                case "Weeks":
                    //timesys=dt.getTime()+(Long.parseLong(stayTime)*60*60*1000*24*7);
                    cal2.add(Calendar.WEEK_OF_MONTH, Integer.parseInt(repeatTime));
                    break;
                case "Months":
                    //timesys=dt.getTime()+(Long.parseLong(stayTime)*60*60*24*1000*30);
                    cal2.add(Calendar.MONTH, Integer.parseInt(repeatTime));
                    break;
                default:
                    //timesys=dt.getTime()+(Long.parseLong(stayTime)*60*60*24*1000*365);
                    cal2.add(Calendar.YEAR, Integer.parseInt(repeatTime));
                    break;
            }
            SimpleDateFormat dateFormatter2 = new SimpleDateFormat("MMM dd, yyyy, hh:mm aa");
            timesys2 = cal2.getTimeInMillis();
            cal2.setTimeInMillis(timesys2);
            formattedDate2 = dateFormatter2.format(cal2.getTime());
        }

        return Long.parseLong(repeatTime);
    }


    private long calculateFlex(int hourOfTheDay, int minute, int sec,int day,int mon,int years) {




        // Initialize the calendar with today and the preferred time to run the job.
        Calendar cal1 = Calendar.getInstance();
        cal1.set(Calendar.HOUR_OF_DAY, hourOfTheDay);
        cal1.set(Calendar.MINUTE, minute);
        cal1.set(Calendar.SECOND, sec);
        cal1.set(Calendar.DAY_OF_MONTH,day);
        cal1.set(Calendar.MONTH,mon);
        cal1.set(Calendar.YEAR,years);

        // Initialize a calendar with now.
        Calendar cal3 = Calendar.getInstance();






        long delta = (cal3.getTimeInMillis() - cal1.getTimeInMillis());

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