package com.example.eminz.worker;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.net.URLEncoder;
import java.util.Objects;

public class sendMessageWorker extends Worker {
    public sendMessageWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {

        String message = getInputData().getString("message");
        String[] numbers = getInputData().getStringArray("contacts");

         try {
            if(numbers.length!=0) {
                for(int j=0;j<numbers.length;j++) {
                        PackageManager packageManager = Objects.requireNonNull(getApplicationContext()).getPackageManager();
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        try {
                            String url = "https://api.whatsapp.com/send?phone=" + numbers[j] + "&text=" + URLEncoder.encode(message + "   ", "UTF-8");
                            intent.setPackage("com.whatsapp");
                            intent.setData(Uri.parse(url));
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            if (intent.resolveActivity(packageManager) != null) {
                                getApplicationContext().startActivity(intent);
                                Thread.sleep(5000);
                            }else{
                           }
                        } catch (Exception e) {
                           e.printStackTrace();
                        }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.success();
    }
}
