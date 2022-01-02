package com.example.eminz.worker;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.net.URLEncoder;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class Onetimeworker extends Worker {


    public Onetimeworker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {

        Log.d("onetimewhatsapp", "inside do work");
        String everytime = getInputData().getString("Everytime");
        String endingafter = getInputData().getString("ending");
        String message = getInputData().getString("message");
        String[] numbers = getInputData().getStringArray("contacts");
        String dropitem = getInputData().getString("dropitem");
        String dropitem2 = getInputData().getString("dropitem2");


        Log.d("onetimewhatsapp", "periodicscheduler");


        try {
            if (!TextUtils.isEmpty(everytime)) {
                Data periodicdata = new Data.Builder()
                        .putString("message", message)
                        .putStringArray("contacts", numbers)
                        .build();


                PeriodicWorkRequest sendMessagework = new PeriodicWorkRequest.Builder(periodicworker.class, calculatespin(everytime, dropitem),
                        TimeUnit.MILLISECONDS)
                        .setInputData(periodicdata)
                        .addTag("send_periodic_message_work")
                        .build();


                WorkManager.getInstance(getApplicationContext()).enqueueUniquePeriodicWork("send_periodic_message_work",
                        ExistingPeriodicWorkPolicy.REPLACE, sendMessagework);


            } else {
                Log.d("onetimewhatsapp", "Condition not satisfied");
            }
            if (numbers.length != 0) {
                Log.d("onetimewhatsapp", "inside of 2nd if");
                for (int j = 0; j < numbers.length; j++) {
                    Log.d("onetimewhatsapp", "inside for " + "");
                    PackageManager packageManager = Objects.requireNonNull(getApplicationContext()).getPackageManager();
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    try {
                        String url = "https://api.whatsapp.com/send?phone=" + numbers[j] + "&text=" + URLEncoder.encode(message + "   ", "UTF-8");
                        intent.setPackage("com.whatsapp");
                        intent.setData(Uri.parse(url));
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                        if (intent.resolveActivity(packageManager) != null) {
                            Log.d("onetimewhatsapp", "inside resolver");
                            getApplicationContext().startActivity(intent);
                            Thread.sleep(5000);
                            Log.d("onetimewhatsapp", "inside sleep");
                        } else {
                            Log.d("onetimewhatsapp", "inside resolver else");

                        }
                    } catch (Exception e) {
                        Log.d("onetimewhatsapp", "Exception " + e.getMessage());
                        e.printStackTrace();
                    }

                }
            }


        } catch (Exception e) {
            Log.d("onetimesms", "Exception2 " + e.getMessage());
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
}