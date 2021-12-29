package com.example.eminz.Scheduler;

import static android.Manifest.permission.READ_CONTACTS;
import static android.Manifest.permission.SEND_SMS;

import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.example.eminz.worker.Smsonetimeworker;
import com.example.whatsappdemo.R;
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

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class SMSScheduler extends AppCompatActivity {
    private static final int CONTACT_PICKER_REQUEST = 20;
    private final BroadcastReceiver localBroadCastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String result = intent.getStringExtra("Result");
            Toast.makeText(context, "result", Toast.LENGTH_SHORT).show();

        }
    };
    public TextView date;
    Spinner spin1, spin2;
    ImageButton contactButton;
    EditText phn, everytime, endafter;
    Button sms;
    String repeatTime;
    String stayTime;
    String drop_item2;
    String drop_item;
    Context context;
    List<ContactResult> results = new ArrayList<>();
    int day;
    int mon;
    int years;
    private TextView time, battery, screen;
    private EditText messageInput, numberInput, nameInput;
    private int hr = 100;
    private int min = 100;
    private int sec = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smsscheduler);
        context = this;


        phn = findViewById(R.id.phon);
        spin1 = findViewById(R.id.spinner1);
        spin2 = findViewById(R.id.spinner2);
        messageInput = findViewById(R.id.smstxt);
        sms = findViewById(R.id.smsschedule);
        contactButton = findViewById(R.id.choos);
        time = findViewById(R.id.smstime);
        date = findViewById(R.id.smsdate);
        everytime = findViewById(R.id.edit11);
        endafter = findViewById(R.id.edit12);
        battery = findViewById(R.id.battery);
        screen = findViewById(R.id.screenlock);

        //Initialise database


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.time, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin1.setAdapter(adapter);

        stayTime = endafter.getText().toString();
        drop_item = spin1.getSelectedItem().toString();

        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.Numbers, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin2.setAdapter(adapter2);
        drop_item2 = spin2.getSelectedItem().toString();
        battery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS);
                startActivity(intent);
            }
        });
        screen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DevicePolicyManager.ACTION_SET_NEW_PASSWORD);
                startActivity(intent);
            }
        });


        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                hr = calendar.get(Calendar.HOUR_OF_DAY);
                min = calendar.get(Calendar.MINUTE);
                sec = calendar.get(Calendar.SECOND);
                TimePickerDialog dpd = TimePickerDialog.newInstance(
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
                                hr = hourOfDay;
                                min = minute;
                                sec = second;
                                time.setText(MessageFormat.format("{0}-{1} ", hourOfDay, minute));

                            }

                        },
                        false
                );
                dpd.show(getSupportFragmentManager(), "Datepickerdialog");

            }
        });


        Dexter.withContext(this)
                .withPermissions(
                        SEND_SMS,
                        READ_CONTACTS

                ).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport report) {/* ... */}

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {/* ... */}
        }).check();

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                years = calendar.get(Calendar.YEAR);
                mon = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePickerDialog view, int yea, int monthOfYear, int dayOfMonth) {
                                years = yea;
                                mon = monthOfYear;
                                day = dayOfMonth;
                                date.setText(new StringBuilder().append(dayOfMonth).append("-").append(monthOfYear + 1).append("-").append(yea).toString());


                            }


                        }
                );
                dpd.show(getSupportFragmentManager(), "Datepickerdialog");


            }

        });
        sms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (hr != 100 && min != 100) {
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

                                        .putString("message", messageInput.getText().toString())
                                        .putStringArray("contacts", numbers)
                                        .putString("Everytime", everytime.getText().toString())
                                        .putString("ending", endafter.getText().toString())
                                        .putString("dropitem", spin1.getSelectedItem().toString())
                                        .putString("dropitem2", spin2.getSelectedItem().toString())
                                        .build();


                                OneTimeWorkRequest sendMessages = new OneTimeWorkRequest.Builder(Smsonetimeworker.class)
                                        .setInitialDelay(flexTime, TimeUnit.MILLISECONDS)
                                        .setInputData(messageData).build();


                                WorkManager.getInstance(getApplicationContext()).enqueue(sendMessages);
                                Toast.makeText(SMSScheduler.this, "Message is scheduled", Toast.LENGTH_SHORT).show();


                            } else {
                                Toast.makeText(SMSScheduler.this, "Please add message", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(SMSScheduler.this, "Select Contact Number", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(SMSScheduler.this, "Please select Date", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(SMSScheduler.this, "Please select Time", Toast.LENGTH_SHORT).show();

                }

            }


        });


        contactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MultiContactPicker.Builder(SMSScheduler.this) //Activity/com.example.eminz.fragment context
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

    private void updateMessageEditText(String selectedMessage) {
        messageInput.setText(selectedMessage);
        Toast.makeText(getApplicationContext(), "Please select new date and time.", Toast.LENGTH_SHORT).show();

    }

    private void updateEnterPhoneNumberEditText(String selectedName) {
        nameInput.setText(selectedName);


    }

    private boolean AirplaneModeOn(Context applicationContext) {
        return Settings.Global.getInt(context.getContentResolver(), Settings.Global.AIRPLANE_MODE_ON, 0) != 0;

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

    private long calculateFlex(int hourOfTheDay, int minute, int sec, int day, int mon, int years) {


        // Initialize the calendar with today and the preferred time to run the job.
        Calendar cal1 = Calendar.getInstance();
        cal1.set(Calendar.HOUR_OF_DAY, hourOfTheDay);
        cal1.set(Calendar.MINUTE, minute);
        cal1.set(Calendar.SECOND, sec);
        cal1.set(Calendar.DAY_OF_MONTH, day);
        cal1.set(Calendar.MONTH, mon);
        cal1.set(Calendar.YEAR, years);

        // Initialize a calendar with now.
        Calendar cal3 = Calendar.getInstance();


        long delta = Math.abs((cal3.getTimeInMillis() - cal1.getTimeInMillis()));

        return delta;
    }


}