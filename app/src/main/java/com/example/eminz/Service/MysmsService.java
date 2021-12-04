package com.example.eminz.Service;

import android.annotation.SuppressLint;
import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.telephony.SmsManager;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.wafflecopter.multicontactpicker.ContactResult;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class MysmsService extends IntentService {

    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_SMS = "com.example.whatsappdemo.action.FOO";
    private static final String ACTION_WHATSAPP = "com.example.whatsappdemo.action.BAZ";
    private static final String ACTION_WHATSAPPBZNS = "com.example.whatsappdemo.action.BAZ";

    // TODO: Rename parameters
    private static final String MESSAGE = "com.example.whatsappdemo.extra.PARAM1";
    private static final String MOBILE_NUMBER = "com.example.whatsappdemo.extra.PARAM2";

    public MysmsService() {
        super("MysmsService");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionSMS (Context context, String message, List<ContactResult> mobile_number) {
         List<String> numbers=new ArrayList<>();
         for (int i=0;i<mobile_number.size();i++){
             numbers.add(mobile_number.get(i).getPhoneNumbers().get(0).getNumber());
         }
         String [] numbersArray=numbers.toArray(new String[0]);
        Intent intent = new Intent(context, MysmsService.class);
        intent.setAction(ACTION_SMS);
        intent.putExtra(MESSAGE, message);
        intent.putExtra(MOBILE_NUMBER, numbersArray);
        context.startService(intent);
    }

    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method

    public static void startActionWHATSAPP (Context context, String message, List<ContactResult> mobile_number) {
        List<String> numbers=new ArrayList<>();
        for (int i=0;i<mobile_number.size();i++){
            numbers.add(mobile_number.get(i).getPhoneNumbers().get(0).getNumber());
        }
        String [] numbersArray=numbers.toArray(new String[0]);
        Intent intent = new Intent(context, MysmsService.class);
        intent.setAction(ACTION_WHATSAPP);
        intent.putExtra(MESSAGE, message);
        intent.putExtra(MOBILE_NUMBER, numbersArray);
        context.startService(intent);
    }

    public static void startActionWHATSAPPBZNS (Context context, String message, List<ContactResult> mobile_number) {
        List<String> numbers=new ArrayList<>();
        for (int i=0;i<mobile_number.size();i++){
            numbers.add(mobile_number.get(i).getPhoneNumbers().get(0).getNumber());
        }
        String [] numbersArray=numbers.toArray(new String[0]);
        Intent intent = new Intent(context, MysmsService.class);
        intent.setAction(ACTION_WHATSAPP);
        intent.putExtra(MESSAGE, message);
        intent.putExtra(MOBILE_NUMBER, numbersArray);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_SMS.equals(action)) {
                final String message = intent.getStringExtra(MESSAGE);
                final String[] mobile_number = intent.getStringArrayExtra(MOBILE_NUMBER);
                handleActionSMS(message, mobile_number);
            } else if (ACTION_WHATSAPP.equals(action)) {
                final String message = intent.getStringExtra(MESSAGE);
                final String[] mobile_number = intent.getStringArrayExtra(MOBILE_NUMBER);
                handleActionWHATSAPP(message, mobile_number);
            }else if (ACTION_WHATSAPPBZNS.equals(action)){
                final String message = intent.getStringExtra(MESSAGE);
                final String[] mobile_number = intent.getStringArrayExtra(MOBILE_NUMBER);
                handleActionWHATSAPPBZNS(message, mobile_number);
            }
        }
    }

    private void handleActionWHATSAPPBZNS(String message, String[] mobile_number) {
        try {
            PackageManager packageManager=getApplicationContext().getPackageManager();

            if (mobile_number.length!=0){
                for (int i=0; i< mobile_number.length;i++){
                    String number=mobile_number[i];
                    String url="https://api.whatsapp.com/send?phone="+number+"&text="+ URLEncoder.encode(message +"   ","UTF-8");
                    Intent whatsappintent=new Intent(Intent.ACTION_VIEW);
                    whatsappintent.setPackage("com.whatsapp.w4b");
                    whatsappintent.setData(Uri.parse(url));
                    whatsappintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    if (whatsappintent.resolveActivity( packageManager)!=null) {
                        getApplicationContext().startActivity(whatsappintent);
                        Thread.sleep(10000);
                        sendBroadcastMessage("Successful" + number);


                    }else {
                        sendBroadcastMessage("You must have Whatsapp Business installed to enjoy this feature!");
                    }

                }
            }

        }catch (Exception e){
            sendBroadcastMessage("Result"+e.toString());
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionSMS(String message, String[] mobile_number) {
        // TODO: Handle action Foo

        try {
            if (mobile_number.length!=0){
                for (int i=0; i< mobile_number.length;i++){
                    SmsManager smsManager=SmsManager.getDefault();
                    smsManager.sendTextMessage(mobile_number[i],null,message,null,null);
                    sendBroadcastMessage("SMS successfully sent "+mobile_number[i]);
                }
            }

        }catch (Exception e){

        }
    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    @SuppressLint("QueryPermissionsNeeded")
    private void handleActionWHATSAPP(String message, String[] mobile_number) {
        // TODO: Handle action Baz
        try {
            PackageManager packageManager=getApplicationContext().getPackageManager();

            if (mobile_number.length!=0){
                for (int i=0; i< mobile_number.length;i++){
                    String number=mobile_number[i];
                    String url="https://api.whatsapp.com/send?phone="+number+"&text="+ URLEncoder.encode(message +"   ","UTF-8");
                    Intent whatsappintent=new Intent(Intent.ACTION_VIEW);
                    whatsappintent.setPackage("com.whatsapp");
                    whatsappintent.setData(Uri.parse(url));
                    whatsappintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    if (whatsappintent.resolveActivity( packageManager)!=null) {
                        getApplicationContext().startActivity(whatsappintent);
                        Thread.sleep(10000);
                        sendBroadcastMessage("Successful" + number);


                    }else {
                        sendBroadcastMessage("Whatsapp not installed");
                    }

                }
            }

        }catch (Exception e){
        sendBroadcastMessage("Result"+e.toString());
        }

    }

    private  void  sendBroadcastMessage(String message){
        Intent localintent=new Intent("my.own.broadcast");
        localintent.putExtra("result",message);
        LocalBroadcastManager.getInstance(this).sendBroadcast(localintent);

    }
}