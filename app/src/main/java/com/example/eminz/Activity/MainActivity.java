package com.example.eminz.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;

import com.example.eminz.Scheduler.SMSScheduler;
import com.example.eminz.Scheduler.WhatsappScheduler;
import com.example.eminz.Service.Whatsappaccessibility;
import com.example.whatsappdemo.R;
import com.example.eminz.Scheduler.WhatsappbsnsScheduler;
import com.example.eminz.fragment.Deleted;
import com.example.eminz.fragment.Failed;
import com.example.eminz.fragment.Pending;
import com.example.eminz.fragment.Sent;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;



import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final int CONTACT_PICKER_REQUEST = 20;
    CardView bzns,wtsapp,sms;

    NavigationView nav;
    ActionBarDrawerToggle toggle;
    DrawerLayout drawerLayout;
    ConstraintLayout constraintLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        bzns=findViewById(R.id.bsnscard);
        wtsapp=findViewById(R.id.whatsappcard);
        sms=findViewById(R.id.smscard);

        Toolbar toolbar = findViewById(R.id.tool);
        nav = findViewById(R.id.navdraw);
        drawerLayout = findViewById(R.id.drawar);
        constraintLayout=findViewById(R.id.homelayout);

        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                if (item.getItemId() == R.id.pend) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.pend, new Pending()).commit();
                }
                if (item.getItemId() == R.id.don) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.don, new Sent()).commit();
                }
                if (item.getItemId() == R.id.Fail) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.Fail, new Failed()).commit();
                }
                if (item.getItemId() == R.id.delete) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.delete, new Deleted()).commit();
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        wtsapp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                Intent intent=new Intent(MainActivity.this, WhatsappScheduler.class);
                startActivity(intent);
            }
        });

        sms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, SMSScheduler.class);
                startActivity(intent);
            }
        });
        bzns.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                PackageManager packageManager=getApplicationContext().getPackageManager();
                boolean isInstalled = isPackageInstalled(packageManager);
                if(isInstalled){
                    Intent intent=new Intent(MainActivity.this, WhatsappbsnsScheduler.class);
                    startActivity(intent);
                }else {
                    Snackbar.make(constraintLayout,"You must have Whatsapp Business installed to enjoy this feature!",
                            Snackbar.LENGTH_LONG).setAction("close", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    }).setActionTextColor(getResources().getColor(R.color.purple_500)).show();
                }


            }
        });


    }

    private boolean isPackageInstalled(PackageManager packageManager) {
        try {
            packageManager.getPackageInfo("com.whatsapp.w4b", PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }



}