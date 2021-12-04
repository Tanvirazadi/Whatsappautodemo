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

import com.example.eminz.Service.MysmsService;
import com.example.eminz.Service.Whatsappaccessibility;
import com.example.whatsappdemo.R;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.wafflecopter.multicontactpicker.ContactResult;
import com.wafflecopter.multicontactpicker.LimitColumn;
import com.wafflecopter.multicontactpicker.MultiContactPicker;

import java.util.ArrayList;
import java.util.List;


public class WhatsappbsnsScheduler extends AppCompatActivity {
    private static final int CONTACT_PICKER_REQUEST = 20;
    EditText phn, sms;
    Button chose, whtsapp;
    List<ContactResult> results=new ArrayList<>();
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
        setContentView(R.layout.activity_whatsappbsns_scheduler);

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
                new MultiContactPicker.Builder(WhatsappbsnsScheduler.this) //Activity/fragment context
                        .hideScrollbar(false) //Optional - default: false
                        .showTrack(true) //Optional - default: true
                        .searchIconColor(Color.WHITE) //Option - default: White
                        .setChoiceMode(MultiContactPicker.CHOICE_MODE_MULTIPLE) //Optional - default: CHOICE_MODE_MULTIPLE
                        .handleColor(ContextCompat.getColor(WhatsappbsnsScheduler.this, R.color.purple_500)) //Optional - default: Azure Blue
                        .bubbleColor(ContextCompat.getColor(WhatsappbsnsScheduler.this, R.color.purple_700)) //Optional - default: Azure Blue
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




        whtsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isAccessibilityOn(getApplicationContext())) {
                    Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);

                    MysmsService.startActionWHATSAPP(getApplicationContext(), sms.getText().toString(), results);


                }
            }
        });

        IntentFilter intentFilter=new IntentFilter("my.own.broadcast");
        LocalBroadcastManager.getInstance(this).registerReceiver(localBroadCastReceiver,intentFilter);

    }



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





}