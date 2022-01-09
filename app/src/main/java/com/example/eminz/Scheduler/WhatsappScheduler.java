package com.example.eminz.Scheduler;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.android.ex.chips.BaseRecipientAdapter;
import com.android.ex.chips.RecipientEditTextView;
import com.android.ex.chips.recipientchip.DrawableRecipientChip;
import com.example.eminz.Service.Whatsappaccessibility;
import com.example.eminz.database.AppDatabase;
import com.example.eminz.database.AppExecutors;
import com.example.eminz.database.DatabaseClient;
import com.example.eminz.database.daos.ScheduleDao;
import com.example.eminz.database.entities.Schedule;
import com.example.eminz.worker.Onetimeworker;
import com.example.whatsappdemo.R;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.wafflecopter.multicontactpicker.ContactResult;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class WhatsappScheduler extends AppCompatActivity {

    private static final int CONTACT_PICKER_REQUEST = 20;


    String repeatTime;
    String stayTime;
    String drop_item2;
    String drop_item;
    EditText sms, everytime, endafter;
    Spinner spin1, spin2;
    ArrayList<ContactResult> results = new ArrayList<>();
    TextView time, date;
    int day = 30;
    int mon = 12;
    int years = 1;
    private int hr = 100;
    private int min = 100;
    private int sec = 100;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_whatsapp_scheduler);


        final RecipientEditTextView phoneRetv = (RecipientEditTextView) findViewById(R.id.phone_retrvs);
        phoneRetv.setMaxChips(4);
        phoneRetv.setChipNotCreatedListener(new RecipientEditTextView.ChipNotCreatedListener() {
            @Override
            public void chipNotCreated(String chipText) {
                Toast.makeText(WhatsappScheduler.this, "You set the max number of chips to 20. Chip not created for: " + chipText, Toast.LENGTH_SHORT).show();
            }
        });
        phoneRetv.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
        BaseRecipientAdapter adapter = new BaseRecipientAdapter(BaseRecipientAdapter.QUERY_TYPE_PHONE, this);
        adapter.setShowMobileOnly(true);
        phoneRetv.setAdapter(adapter);
        phoneRetv.dismissDropDownOnItemSelected(true);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                DrawableRecipientChip[] chips = phoneRetv.getSortedRecipients();
                for (DrawableRecipientChip chip : chips) {
                    Log.v("DrawableChip", chip.getEntry().getDisplayName() + " " + chip.getEntry().getDestination());
                }
            }
        }, 5000);

        final ImageButton showAll = (ImageButton) findViewById(R.id.chooses);
        showAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                phoneRetv.showAllContacts();
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(this).setMessage(R.string.enter_messeage)
                .setTitle("Rminz")
                .setMessage(R.string.message)
                .setView(R.layout.mdtp_time_title_view)
                .setPositiveButton("ENABLE ACCESSIBILITY", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (!isAccessibilityOn(getApplicationContext())) {
                            Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                        if (isAccessibilityOn(getApplicationContext())) {
                            Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    }
                });
        builder.create().show();


        sms = findViewById(R.id.txt);
        time = findViewById(R.id.time);
        date = findViewById(R.id.whatsappdate);
        spin1 = findViewById(R.id.spinner21);
        spin2 = findViewById(R.id.spinner22);


        everytime = findViewById(R.id.edit11);
        repeatTime = everytime.getText().toString();


        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this, R.array.time, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin1.setAdapter(adapter1);

        endafter = findViewById(R.id.edit12);
        stayTime = endafter.getText().toString();
        drop_item = spin1.getSelectedItem().toString();


        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.Numbers, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin2.setAdapter(adapter2);
        drop_item2 = spin2.getSelectedItem().toString();


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
                Calendar calendar = Calendar.getInstance();
                hr = calendar.get(Calendar.HOUR_OF_DAY);
                min = calendar.get(Calendar.MINUTE);
                sec = calendar.get(Calendar.SECOND);
                TimePickerDialog tpd = TimePickerDialog.newInstance(
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
                                hr = hourOfDay;
                                min = minute;
                                time.setText(MessageFormat.format("{0}-{1} ", hourOfDay, minute));

                            }

                        }, false
                );
                tpd.show(getSupportFragmentManager(), "Timepickerdialog");
            }
        });
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
                                date.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + yea);
                                SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy");
                                String dateString = format.format(calendar.getTime());
                                date.setText(dateString);

                            }
                        }
                );
                dpd.show(getSupportFragmentManager(), "Datepickerdialog");

            }
        });


        final BroadcastReceiver localBroadCastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String result = intent.getStringExtra("Result");
                Toast.makeText(context, "result", Toast.LENGTH_SHORT).show();

            }
        };


        IntentFilter intentFilter = new IntentFilter("my.own.broadcast");
        LocalBroadcastManager.getInstance(this).registerReceiver(localBroadCastReceiver, intentFilter);

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


    private boolean isAccessibilityOn(Context context) {

        int accessibilityEnabled = 0;
        final String service = context.getPackageName() + "/" + Whatsappaccessibility.class.getCanonicalName();
        try {
            accessibilityEnabled = Settings.Secure.getInt(context.getApplicationContext().getContentResolver(), Settings.Secure.ACCESSIBILITY_ENABLED);
        } catch (Settings.SettingNotFoundException ignored) {
        }

        TextUtils.SimpleStringSplitter colonSplitter = new TextUtils.SimpleStringSplitter(':');

        if (accessibilityEnabled == 1) {
            String settingValue = Settings.Secure.getString(context.getApplicationContext().getContentResolver(), Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
            if (settingValue != null) {
                colonSplitter.setString(settingValue);
                while (colonSplitter.hasNext()) {
                    String accessibilityService = colonSplitter.next();

                    if (accessibilityService.equalsIgnoreCase(service)) {
                        return true;
                    }
                }
            }
        }

        return false;


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.whatsappmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
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
                        String message = sms.getText().toString();
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
                                .putLong("schdeuleId", scheduleId)
                                .build();

                        Schedule schedule = new Schedule(scheduleId, "WhatsApp", TextUtils.join(",", numbers), message, flexTime, flexTime,
                                everyInt, drop1, repeatCount, repeatCount, "Pending");


                        OneTimeWorkRequest sendMessagework = new OneTimeWorkRequest.Builder(Onetimeworker.class)
                                .setInitialDelay(flexTime, TimeUnit.MILLISECONDS)
                                .setInputData(messageData)
                                .build();
                        WorkManager.getInstance().enqueue(sendMessagework);


                        AppDatabase appDatabase = DatabaseClient.getInstance(getApplicationContext()).getAppDatabase();
                        ScheduleDao scheduleDao = appDatabase.scheduleDaoDao();

                        AppExecutors.getInstance().diskIO().execute(new Runnable() {
                            @Override
                            public void run() {
                                List<Long> insert = scheduleDao.insert(schedule);
                                finish();


                            }
                        });

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


        } else {
            Toast.makeText(WhatsappScheduler.this, "Please select Time", Toast.LENGTH_SHORT).show();

        }


        return true;
    }


}