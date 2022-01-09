package com.example.eminz.Activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.eminz.Scheduler.SMSScheduler;
import com.example.eminz.Scheduler.WhatsappScheduler;
import com.example.eminz.Scheduler.WhatsappbsnsScheduler;
import com.example.eminz.Sqlite.Databsehelper;
import com.example.eminz.fragment.Fragmentadapter;
import com.example.whatsappdemo.R;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private static final int CONTACT_PICKER_REQUEST = 20;
    NavigationView nav;
    ActionBarDrawerToggle toggle;
    DrawerLayout drawerLayout;
    ConstraintLayout constraintLayout;
    TabLayout tabLayout;
    ViewPager viewPager;
    TabItem tab1, tab2, tab3;
    TextView text;
    Fragmentadapter fragmentadapter;
    FloatingActionButton button1, button2, button3;
    Databsehelper databsehelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Loadlocale();
        setContentView(R.layout.activity_main);


        nav = findViewById(R.id.navdraw);
        drawerLayout = findViewById(R.id.drawar);
        constraintLayout = findViewById(R.id.bznsconstrain);
        viewPager = findViewById(R.id.viewpag);
        tabLayout = findViewById(R.id.tabs);
        tab1 = findViewById(R.id.pendingtab);
        tab2 = findViewById(R.id.senttab);
        tab3 = findViewById(R.id.failedtab);
        button1 = findViewById(R.id.floating1);
        button2 = findViewById(R.id.floating2);
        button3 = findViewById(R.id.floating3);
        text = findViewById(R.id.accountid);

        Toolbar toolbar = findViewById(R.id.tool);
        setSupportActionBar(toolbar);


        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.setDrawerIndicatorEnabled(true);
        toggle.syncState();
        fragmentadapter = new Fragmentadapter(getSupportFragmentManager(), FragmentPagerAdapter.
                BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, tabLayout.getTabCount());
        viewPager.setAdapter(fragmentadapter);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {


            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));


        nav.setNavigationItemSelectedListener(item -> {
            drawerLayout.closeDrawer(GravityCompat.START);

            if (item.getItemId() == R.id.hindi) {
                setLocal("hi");
                recreate();
            }
            if (item.getItemId() == R.id.eng) {
                setLocal("eng");
                recreate();
            }


            return true;
        });

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SMSScheduler.class);
                startActivity(intent);


            }
        });
        button2.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, WhatsappScheduler.class);
                startActivity(intent);


            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PackageManager packageManager = getApplicationContext().getPackageManager();
                boolean isInstalled = isPackageInstalled(packageManager);
                if (isInstalled) {
                    Intent intent = new Intent(MainActivity.this, WhatsappbsnsScheduler.class);
                    startActivity(intent);


                } else {
                    Snackbar.make(drawerLayout, "You must have Whatsapp Business installed to enjoy this feature!", Snackbar.LENGTH_LONG)
                            .setAction("close", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                }
                            }).setActionTextColor(getResources().getColor(R.color.purple_500))
                            .show();


                }


            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        viewPager.setCurrentItem(0);
    }

    private boolean isPackageInstalled(PackageManager packageManager) {
        try {
            packageManager.getPackageInfo("com.whatsapp.w4b", PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;

        }
    }


    public void setLocal(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());

        SharedPreferences.Editor editor = getSharedPreferences("Settings", Activity.MODE_PRIVATE).edit();
        editor.putString("Language", lang);
        editor.apply();
    }

    public void Loadlocale() {
        SharedPreferences pref = getSharedPreferences("Settings", MODE_PRIVATE);
        String language = pref.getString("Language", "");
        setLocal(language);

    }


    public void onLabelClick(View view) {
        if (view.getId() == R.id.accountid) {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                  
                }
            });

        }
    }
}