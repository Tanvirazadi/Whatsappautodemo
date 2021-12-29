package com.example.eminz.App;

import android.app.Application;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

public class Facebook extends Application {

    @Override
    public void onCreate() {
        FacebookSdk.fullyInitialize();
        AppEventsLogger.activateApp(this);

        super.onCreate();
    }

}
