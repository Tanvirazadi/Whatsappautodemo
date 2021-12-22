package com.example.eminz.worker;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.util.concurrent.TimeUnit;

public class Smsonetimeworker extends Worker {
    public Smsonetimeworker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {


        String everytime = getInputData().getString("Everytime");
        String endingafter = getInputData().getString("ending");
        String message = getInputData().getString("message");
        String[] numbers = getInputData().getStringArray("contacts");
        String dropitem = getInputData().getString("dropitem");
        String dropitem2 = getInputData().getString("dropitem2");
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


        try {
            if (!TextUtils.isEmpty(everytime) && !TextUtils.isEmpty(endingafter)) {

                if (numbers.length != 0) {
                    String join = TextUtils.join("; ", numbers);
                    Intent i = new Intent(android.content.Intent.ACTION_VIEW);
                    i.putExtra("address", join);
                    i.putExtra("sms_body", message);
                    i.setType("vnd.android-dir/mms-sms");
                    getApplicationContext().startActivity(i);

//                    for (int j = 0; j < numbers.length; j++) {
//                        PackageManager packageManager = Objects.requireNonNull(getApplicationContext()).getPackageManager();
//                        Intent intent = new Intent(Intent.ACTION_VIEW);
//                        try {
//                            SmsManager smsManager=SmsManager.getDefault();
//                            smsManager.sendTextMessage(numbers[j],null,message,null,null);
//                            sendBroadcastMessage("SMS successfully sent "+mobile_number[i]);
//                            if (intent.resolveActivity(packageManager) != null) {
//                                getApplicationContext().startActivity(intent);
//                                Thread.sleep(5000);
//                            }
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//
//                    }

                }

            }
        } catch (Exception e) {
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
