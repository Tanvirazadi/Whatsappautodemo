package com.example.eminz.fragment;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class Fragmentadapter extends FragmentPagerAdapter {
    private final int tabnum;
    public Fragmentadapter(@NonNull FragmentManager fm, int behavior, int tabs) {
        super(fm, behavior);
        this.tabnum=tabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch (position){
            case  0:
              return  new Pending();
            case 1:
                return  new Sent();
            case 2:
                return  new Failed();

            default: return null;

        }


    }

    @Override
    public int getCount() {
        return tabnum;
    }
}
