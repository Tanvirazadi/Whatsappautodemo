package com.example.eminz.worker;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.eminz.database.AppDatabase;
import com.example.eminz.database.AppExecutors;
import com.example.eminz.database.DatabaseClient;
import com.example.eminz.database.daos.ScheduleDao;
import com.example.eminz.database.entities.Schedule;

import java.util.concurrent.TimeUnit;

public class Smsonetimeworker extends Worker {
    public Smsonetimeworker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {

        Log.d("onetimesms", "inside do work");
        String everytime = getInputData().getString("Everytime");
        String endingafter = getInputData().getString("ending");
        String message = getInputData().getString("message");
        Log.d("onetimesms", "text2 " + message);
        String[] numbers = getInputData().getStringArray("contacts");
        String dropitem = getInputData().getString("dropitem");
        String dropitem2 = getInputData().getString("dropitem2");
        long scheduleId = getInputData().getLong("smsschdeuleId", -1);

        Log.d("onetimesms", "periodic schedule");
        try {
            if (!TextUtils.isEmpty(everytime)) {
                Log.d("onetimesms", "inside of 1st if");
                Data periodicdata = new Data.Builder()
                        .putString("message", message)
                        .putStringArray("contacts", numbers)
                        .build();


                PeriodicWorkRequest sendMessagework = new PeriodicWorkRequest.Builder(Smsperiodicworker.class, calculatespin(everytime, dropitem),
                        TimeUnit.MILLISECONDS)
                        .setInputData(periodicdata)
                        .addTag("send_sms_periodic_message_work")
                        .build();


                WorkManager.getInstance(getApplicationContext()).enqueueUniquePeriodicWork("send_sms_periodic_message_work",
                        ExistingPeriodicWorkPolicy.REPLACE, sendMessagework);


            } else {
                Log.d("onetimesms", "Condition not satisfied");
            }
            if (numbers.length != 0) {
                Log.d("onetimesms", "inside of 2nd if");
                for (int j = 0; j < numbers.length; j++) {
                    Log.d("onetimesms", "inside for " + message);


                    sendSMS(numbers[j], message);

                    AppDatabase appDatabase = DatabaseClient.getInstance(getApplicationContext()).getAppDatabase();
                    ScheduleDao scheduleDao = appDatabase.scheduleDaoDao();
                    AppExecutors.getInstance().diskIO().execute(new Runnable() {
                        @Override
                        public void run() {
                            Schedule Sent = scheduleDao.findById(scheduleId);
                            Sent.status = "Sent";
                            scheduleDao.update(Sent);
                        }
                    });
                }


            }
        } catch (Exception e) {
            Log.d("onetimesms", "Exception " + e.getMessage());
            e.printStackTrace();
        }

        return Result.success();

    }

    public long calculatespin(String everytime, String drop_item) {

        long everytimesint = Long.parseLong(everytime.trim());
        long result;


        switch (drop_item) {
            case "Hours":
                result = everytimesint * 60 * 60 * 1000;
                break;
            case "Days":
                result = everytimesint * 60 * 60 * 24 * 1000;
                break;

            case "Weeks":
                result = everytimesint * 60 * 60 * 1000 * 24 * 7;

                break;
            case "Months":
                result = everytimesint * 60 * 60 * 24 * 1000 * 30;
                break;

            default:
                result = everytimesint * 60 * 60 * 24 * 1000 * 365;

                break;


        }
        return result;


    }

    private void sendSMS(String phoneNumber, String message) {
        String SENT = "SMS_SENT";
        String DELIVERED = "SMS_DELIVERED";

        PendingIntent sentPI = PendingIntent.getBroadcast(getApplicationContext(), 0, new Intent(SENT), 0);
        PendingIntent deliveredPI = PendingIntent.getBroadcast(getApplicationContext(), 0, new Intent(DELIVERED), 0);

        // ---when the SMS has been sent---
        getApplicationContext().registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent arg1) {

                switch (getResultCode()) {

                    case Activity.RESULT_OK:


                        Toast.makeText(getApplicationContext(), "SMS sent Successfully",
                                Toast.LENGTH_SHORT).show();
                        break;

                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:

                        Toast.makeText(getApplicationContext(), "Generic failure",
                                Toast.LENGTH_SHORT).show();
                        break;

                    case SmsManager.RESULT_ERROR_NO_SERVICE:

                        Toast.makeText(getApplicationContext(), "No service",
                                Toast.LENGTH_SHORT).show();
                        break;

                    case SmsManager.RESULT_ERROR_NULL_PDU:

                        Toast.makeText(getApplicationContext(), "Null PDU",
                                Toast.LENGTH_SHORT).show();
                        break;

                    case SmsManager.RESULT_ERROR_RADIO_OFF:

                        Toast.makeText(getApplicationContext(), "Radio off",
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter(SENT));

        // ---when the SMS has been delivered---
        getApplicationContext().registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent arg1) {

                switch (getResultCode()) {

                    case Activity.RESULT_OK:

                        Toast.makeText(getApplicationContext(), "SMS delivered",
                                Toast.LENGTH_SHORT).show();
                        break;

                    case Activity.RESULT_CANCELED:

                        Toast.makeText(getApplicationContext(), "SMS not delivered",
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter(DELIVERED));

        Log.d("onetimesms", "inside sendSms");
        SmsManager sms = SmsManager.getDefault();
        Log.d("onetimesms", "text " + message);
        sms.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI);
        Log.d("onetimesms", "sendsms done");
    }


}
