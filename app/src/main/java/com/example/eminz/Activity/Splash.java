package com.example.eminz.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.whatsappdemo.R;


public class Splash extends AppCompatActivity {
    Animation zoom;
    ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        zoom = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoom);
        img = findViewById(R.id.image);
        img.startAnimation(zoom);

        Handler h = new Handler();

        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences pref = getSharedPreferences("activity", MODE_PRIVATE);
                boolean loginskip = pref.getBoolean("loginskip", false);

                if (loginskip){
                    Intent i = new Intent(getApplicationContext(),
                            MainActivity.class);
                    startActivity(i);
                }else {
                    Intent i = new Intent(getApplicationContext(),
                            Loginpage.class);
                    startActivity(i);
                }
                finish();






            }
        }, 4000);


    }
}