package com.example.eminz.Scheduler;

import static android.Manifest.permission.READ_CONTACTS;
import static android.Manifest.permission.SEND_SMS;
import static com.facebook.FacebookSdk.getApplicationContext;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;


import com.example.eminz.Model.Sms;
import com.example.eminz.Service.MysmsService;
import com.example.eminz.Sqlite.SmsDatabaseHelper;
import com.example.eminz.Util.EmptinessTextWatcher;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class SMSScheduler extends AppCompatActivity {
    private static final int CONTACT_PICKER_REQUEST = 20;



    AutoCompleteTextView phn;
    Spinner spin,spin2;
    private TextView displayTime;
    public TextView displayDate;
    private EditText messageInput, numberInput, nameInput;
    private Button selectTimeButton, scheduleButton, selectDateButton;
    ImageButton contactButton;
    public int setHour = -1, setMinute = -1, setDay = -1, setMonth = -1, setYear = -1;
    private static final int REQUEST_SMS = 0;
    private static final int REQUEST_READ_CONTACTS = 3;
    Context context;
    private SmsDatabaseHelper smsDatabaseHelper;
    List<ContactResult> results = new ArrayList<>();
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
        context = this;



        phn = findViewById(R.id.phon);
        spin=findViewById(R.id.spinner1);
        spin2=findViewById(R.id.spinner2);
        messageInput = (EditText) findViewById(R.id.txt);
        selectTimeButton = (Button) findViewById(R.id.times);
        selectDateButton = (Button) findViewById(R.id.dates);
        scheduleButton = findViewById(R.id.send);
        contactButton = (ImageButton) findViewById(R.id.choos);

        //Initialise database
        smsDatabaseHelper = new SmsDatabaseHelper(this);

        TextWatcher watcherEmptiness = new EmptinessTextWatcher(this, phn,messageInput);
        phn.addTextChangedListener(watcherEmptiness);


        ArrayAdapter<CharSequence>adapter=ArrayAdapter.createFromResource(this,R.array.time, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(adapter);
        ArrayAdapter<CharSequence>adapter2=ArrayAdapter.createFromResource(this,R.array.Numbers,android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin2.setAdapter(adapter2);
        Bundle bundle = getIntent().getExtras();


        if (bundle != null) {
            // If bundle is not empty, set name and number to edit texts.
            String selectedName = bundle.getString("name");
            String selectedNumber = bundle.getString("number");
            updateEnterPhoneNumberEditText(selectedName);

            // If bundle contains message, set message to edit text
            if (bundle.getString("message") != null) {
                String selectedMessage = bundle.getString("message");
                updateMessageEditText(selectedMessage);
            }
        }

        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
                Calendar cal3=Calendar.getInstance();
                if (position==0){


                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });





        selectTimeButton.setOnClickListener(new View.OnClickListener() {
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
                        SEND_SMS,
                        READ_CONTACTS

                ).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport report) {/* ... */}

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {/* ... */}
        }).check();

        selectDateButton.setOnClickListener(new View.OnClickListener() {
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
        scheduleButton.setOnClickListener(new View.OnClickListener() {
            @Override
           public void onClick(View v) {

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    // Check permission status of SEND_SMS
                    int SMSPermission = checkSelfPermission(SEND_SMS);
                    // If permission is not granted display message informing user the application requires permission
                    if (SMSPermission != PackageManager.PERMISSION_GRANTED) {
                        requestSmsPermission();
                        return;
                    }
                    validateInput();
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

    private void validateInput() {
        final String contactName = nameInput.getText().toString();
        final String phoneNumber = numberInput.getText().toString();
        final String messageText = messageInput.getText().toString();

        if (contactName.isEmpty()) // Ensure name is not empty
        {
            Toast.makeText(getApplicationContext(), "Please enter a name.", Toast.LENGTH_SHORT).show();
        } else if (phoneNumber.isEmpty()) // Ensure phone number is not empty.
        {
            Toast.makeText(getApplicationContext(), "Please enter a valid phone number.", Toast.LENGTH_SHORT).show();
        } else if (messageText.isEmpty()) // Ensure message is not empty.
        {
            Toast.makeText(getApplicationContext(), "Please enter a message.", Toast.LENGTH_SHORT).show();
        } else if (setDay == -1 || setMonth == -1 || setYear == -1)  // Ensure date has been selected
        {
            Toast.makeText(getApplicationContext(), "Please select a date", Toast.LENGTH_SHORT).show();
        } else if (setHour == -1 || setMinute == -1) // Ensure a time has been selected.
        {
            Toast.makeText(getApplicationContext(), "Please select a time", Toast.LENGTH_SHORT).show();
        } else if (validateSelectedDateTime() == FALSE) // Compare dates, compare to returns negative number if selected date is less than current date
        {
            Toast.makeText(getApplicationContext(), "SMS must be scheduled for a future time", Toast.LENGTH_SHORT).show();

        } else if (AirplaneModeOn(getApplicationContext()) == TRUE) // Check if airplane mode is on
        {
            // Options for dialog
            String[] options = {"Continue to schedule", "Do not schedule", "Cancel"};

            // Build dialog, set title and items as options
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("SMS will not send in airplane mode. Please select an option:");
            builder.setItems(options, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int selectedOption) {
                    // listen for selected item, check selected item and perform appropriate action
                    if (selectedOption == 0) {
                        if (validateSelectedDateTime() == FALSE)  // Ensure time has not changed into past
                        {
                            Toast.makeText(getApplicationContext(), "Time has changed, SMS must be scheduled for a future time", Toast.LENGTH_SHORT).show();
                        } else {
                            addToSms(contactName, messageText);
                        }
                    } else if (selectedOption == 1) {
                        Toast.makeText(context, "SMS has not been scheduled", Toast.LENGTH_LONG).show();
                        resetInput();
                    } else if (selectedOption == 2) {
                        //Do nothing as user has canceled

                    } else {
                        Toast.makeText(context, "Sorry an error occurred.", Toast.LENGTH_LONG).show();
                    }
                }
            });
            builder.show();
        } else //Schedule SMS
        {
            addToSms(contactName, messageText);
        }
    }

    private void resetInput() {
        nameInput.setText("");
        messageInput.setText("");
        displayDate.setText("No Date Selected");
        displayTime.setText("No Time Selected");
    }

    private boolean validateSelectedDateTime() {
        Boolean validDateTime;
        // Get current date and time
        Date currentDateTime = Calendar.getInstance().getTime();
        // Get converted date and time
        Date selectedDateTime = convertSelectedDateTime();

        if (selectedDateTime.compareTo(currentDateTime) < 0) // Compare dates, returns negative number if selected date is less than current date
        {
            validDateTime = FALSE;
        } else {
            validDateTime = TRUE;
        }
        return validDateTime;
    }

    private Date convertSelectedDateTime() {
        Date convertedDateTime = null;
        String selectedDateTime = "";

        // Add 1 to month as months are between 0-11
        int selectedMonth = (setMonth + 1);

        // Join integers and convert to String
        selectedDateTime += setYear + "" + "" + String.format("%02d", selectedMonth) + "" + String.format("%02d", setDay) + "" + String.format("%02d", setHour) + "" + String.format("%02d", setMinute);

        try {
            //Convert String to date format
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
            convertedDateTime = sdf.parse(selectedDateTime);
        } catch (ParseException  e) {
            e.printStackTrace();
        }
        return convertedDateTime;
    }

    private void addToSms(String contactName, String messageText) {
        String name = contactName;
        String message = messageText;
        String messageDate = Integer.toString(setDay) + "/" + Integer.toString(setMonth) + "/" + Integer.toString(setYear); //Convert integers to string
        String messageTime = String.format("%02d:%02d", setHour, setMinute); // Convert to String and format hours and minutes
        String messageStatus = "Pending";

        // Start multi-thread to insert sms to database and start alarm manager
        ScheduleSmsAsyncTask task = new ScheduleSmsAsyncTask();
        task.execute(name, messageDate, messageTime, message, messageStatus);

    }

    private boolean AirplaneModeOn(Context applicationContext) {
        return Settings.Global.getInt(context.getContentResolver(), Settings.Global.AIRPLANE_MODE_ON, 0) != 0;

    }


    private void requestSmsPermission() {
        ActivityCompat.requestPermissions(this, new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);

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


    public static class  scheduler extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... strings) {
            return null;
        }
    }
    public  class ScheduleSmsAsyncTask extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... string) {
            SmsDatabaseHelper smsDatabaseHelper = null;
            String result = "SMS Successfully Scheduled";
            try {
                // Construct a Sms object and pass it to the helper for database insertion
                int SmsID = smsDatabaseHelper.addSms(new Sms(string[0], string[1], string[2], string[3], string[4], string[5]));

                // Create calendar with selected date and time
                Calendar c = Calendar.getInstance();
                c.set(Calendar.YEAR, setYear);
                c.set(Calendar.MONTH, setMonth);
                c.set(Calendar.DAY_OF_MONTH, setDay);
                c.set(Calendar.HOUR_OF_DAY, setHour);
                c.set(Calendar.MINUTE, setMinute);
                c.set(Calendar.SECOND, 0);

                // Create alarm manager
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

                // Pass SmsID to AlarmReceiver class
                Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
                intent.putExtra("SmsID", SmsID);

                //Set SmsID as unique id, Set time to calender, Start alarm
                PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), SmsID, intent, 0);
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
            } catch (Exception e) {
                e.printStackTrace();
                result = "SMS failed to schedule";
            }
            return result;

        }
        }

}