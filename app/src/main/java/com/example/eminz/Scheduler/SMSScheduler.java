package com.example.eminz.Scheduler;

import static android.Manifest.permission.READ_CONTACTS;
import static android.Manifest.permission.SEND_SMS;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.android.ex.chips.BaseRecipientAdapter;
import com.android.ex.chips.RecipientEditTextView;
import com.android.ex.chips.recipientchip.DrawableRecipientChip;
import com.example.eminz.database.AppDatabase;
import com.example.eminz.database.AppExecutors;
import com.example.eminz.database.DatabaseClient;
import com.example.eminz.database.daos.ScheduleDao;
import com.example.eminz.database.entities.Schedule;
import com.example.eminz.worker.Smsonetimeworker;
import com.example.whatsappdemo.R;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.onegravity.contactpicker.contact.Contact;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class SMSScheduler extends AppCompatActivity {

    public TextView date;
    Spinner spin1, spin2;
    EditText phn, everytime, endafter;
    Button sms;
    String repeatTime;
    String stayTime;
    String drop_item2;
    String drop_item;
    Context context;
    private final BroadcastReceiver localBroadCastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String result = intent.getStringExtra("Result");
            Toast.makeText(context, "result", Toast.LENGTH_SHORT).show();


        }
    };
    int mon = 11;
    int years = 1;
    List<DrawableRecipientChip> results = new ArrayList<>();
    private List<Contact> mContacts;
    private TextView time, battery, screen;
    int day = 31;
    private int hr = 100;
    private int min = 100;
    private int sec = 100;
    Toolbar toolbar;
    private EditText messageInput;
    private RecipientEditTextView phoneRetvs;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.popup, menu);


        return true;
    }

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
//        contactButton = findViewById(R.id.choos);
        time = findViewById(R.id.smstime);
        date = findViewById(R.id.smsdate);
        everytime = findViewById(R.id.edit11);
        endafter = findViewById(R.id.edit12);
        toolbar = findViewById(R.id.toolsms);
        setSupportActionBar(toolbar);


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

        phoneRetvs = (RecipientEditTextView) findViewById(R.id.phone_retrvssms);
        phoneRetvs.setMaxChips(4);
        phoneRetvs.setChipNotCreatedListener(new RecipientEditTextView.ChipNotCreatedListener() {
            @Override
            public void chipNotCreated(String chipText) {
                Toast.makeText(SMSScheduler.this, "You set the max number of chips to 20. Chip not created for: " + chipText, Toast.LENGTH_SHORT).show();
            }
        });
        phoneRetvs.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
        BaseRecipientAdapter adapters = new BaseRecipientAdapter(BaseRecipientAdapter.QUERY_TYPE_PHONE, this);
        adapters.setShowMobileOnly(true);
        phoneRetvs.setAdapter(adapters);
        phoneRetvs.dismissDropDownOnItemSelected(true);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                DrawableRecipientChip[] chips = phoneRetvs.getSortedRecipients();
                for (DrawableRecipientChip chip : chips) {
                    Log.v("DrawableChip", chip.getEntry().getDisplayName() + " " + chip.getEntry().getDestination());
                }
            }
        }, 5000);


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
                calendar.getTimeInMillis();

                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePickerDialog view, int yea, int monthOfYear, int dayOfMonth) {
                                calendar.set(Calendar.YEAR, yea);
                                calendar.set(Calendar.MONTH, monthOfYear);
                                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                                /*date.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + yea);*/
                                SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy");
                                String dateString = format.format(calendar.getTime());
                                date.setText(dateString);


                            }


                        });

                dpd.show(getSupportFragmentManager(), "Datepickerdialog");


            }

        });




        IntentFilter intentFilter = new IntentFilter("my.own.broadcast");
        LocalBroadcastManager.getInstance(this).registerReceiver(localBroadCastReceiver, intentFilter);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (hr != 100 && min != 100) {
            if (day != 30 && mon != 12 && years != 1) {
                DrawableRecipientChip[] chips = phoneRetvs.getSortedRecipients();
                for (DrawableRecipientChip chip : chips) {
                    Log.v("DrawableChip", chip.getEntry().getDisplayName() + " " + chip.getEntry().getDestination());
                }
                results.addAll(Arrays.asList(chips));
                if (!results.isEmpty()) {
                    if (!messageInput.getText().toString().isEmpty()) {
                        List<String> numbersList = new ArrayList<String>();
                        for (int i = 0; i < results.size(); i++) {
                            numbersList.add(results.get(i).getEntry().getDisplayName() + results.get(i).getEntry().getDestination());
                        }
                        String[] numbers = numbersList.toArray(new String[0]);
                        long flexTime = calculateFlex(hr, min, sec, day, mon, years);
                        Calendar cal = Calendar.getInstance();
                        cal.set(Calendar.HOUR_OF_DAY, hr);
                        cal.set(Calendar.MINUTE, min);
                        cal.set(Calendar.SECOND, sec);
                        cal.set(Calendar.DAY_OF_MONTH, day);
                        cal.set(Calendar.MONTH, mon);
                        cal.set(Calendar.YEAR, years);


                        long dateInMillies = cal.getTimeInMillis();
                        String message = messageInput.getText().toString();
                        final String every = everytime.getText().toString();
                        final String endAfter = endafter.getText().toString();
                        String drop1 = spin1.getSelectedItem().toString();
                        String drop2 = spin2.getSelectedItem().toString();
                        int everyInt = 0, repeatCount = 0;
                        try {
                            everyInt = Integer.parseInt(every);
                            repeatCount = Integer.parseInt(endAfter);
                        } catch (Exception ignored) {

                        }
                        long scheduleId = System.currentTimeMillis();
                        Data messageData = new Data.Builder()

                                .putString("message", message)
                                .putStringArray("contacts", numbers)
                                .putString("Everytime", every)
                                .putString("ending", endAfter)
                                .putString("dropitem", drop1)
                                .putString("dropitem2", drop2)
                                .putLong("smsschdeuleId", scheduleId)
                                .build();

                        Constraints constraints = new Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED)
                                .build();


                        Schedule schedule = new Schedule(scheduleId, "SMS", TextUtils.join(",", numbers), message, dateInMillies, dateInMillies,
                                everyInt, drop1, repeatCount, repeatCount, "Pending");

                        OneTimeWorkRequest sendMessages = new OneTimeWorkRequest.Builder(Smsonetimeworker.class)
                                .setInitialDelay(dateInMillies, TimeUnit.MILLISECONDS)
                                .setInputData(messageData).build();


                        WorkManager.getInstance(getApplicationContext()).enqueue(sendMessages);
                        AppDatabase appDatabase = DatabaseClient.getInstance(getApplicationContext()).getAppDatabase();
                        ScheduleDao scheduleDao = appDatabase.scheduleDaoDao();

                        AppExecutors.getInstance().diskIO().execute(new Runnable() {
                            @Override
                            public void run() {
                                List<Long> insert = scheduleDao.insert(schedule);
                                finish();


                            }
                        });
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

        return true;


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